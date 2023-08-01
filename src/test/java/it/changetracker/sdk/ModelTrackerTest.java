package it.changetracker.sdk;

import it.changetracker.sdk.core.FieldMapper;
import it.changetracker.sdk.models.Field;
import it.changetracker.sdk.testmodels.AnnotatedTestModel;
import it.changetracker.sdk.testmodels.AnnotatedTestModelWithTypo;
import it.changetracker.sdk.testmodels.TestModel;
import it.changetracker.sdk.testmodels.TestModelItem;
import it.changetracker.sdk.utils.ListHelper;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


class ModelTrackerTest {

    private TestModel fillModel() {
        var testModel = new TestModel();
        testModel.setId("123");
        testModel.setName("test");
        testModel.setDate(new Date());
        testModel.setPrice(100.15);
        testModel.setOldPrice(95.5587F);
        testModel.setFlagBit(true);

        testModel.setItem(new TestModelItem("item_123", "item 123", 100, 63.22));

        testModel.setRows(new ArrayList<>(Arrays.asList(new TestModelItem("row_1", "row 1", 1, 10.00),
                new TestModelItem("row_2", "row 2", 10, 100.34),
                new TestModelItem("row_3", "row 3", 10, 100.34))));

        return testModel;
    }

    @Test
    public void baseTrackingTest() {
        var model = fillModel();
        var map = ModelTracker.createMap(model).mapAll().ignore("oldPrice")
                .map(el -> el.getName() + " mapped", "name");

        var fields = map.toList();

        assertNotNull(fields);
        assertEquals(fields.size(), 6);
    }

    @Test
    void mapRowTableModelWithLinkedTables() {
        var model = fillModel();
        var row = ModelTracker.createMap(model).mapAll().toRow("BEFORE", List.of(
                ModelTracker.toTable("rows", model.getRows().stream().map(el -> ModelTracker.mapAll(el).toRow(el.productId)).toList()))
        );

        assertEquals(row.getKey(), "BEFORE");
        assertNotNull(row.getTables());
        assertEquals(row.getTables().size(), 1);
        assertEquals(row.getTables().get(0).getName(), "rows");

        var linkedTable = row.getTables().get(0);
        var linkedTableRows = linkedTable.getRows();

        assertNotNull(linkedTableRows);
        assertEquals(linkedTableRows.size(), 3);
        assertTrue(linkedTableRows.stream().allMatch(el -> Objects.equals(el.getKey(), "row_1") || Objects.equals(el.getKey(), "row_2") || Objects.equals(el.getKey(), "row_3")));

        var linkedRow = linkedTableRows.get(0);

        assertNotNull(linkedRow.getFields());
        assertEquals(linkedRow.getFields().size(), 4);
    }

    @Test
    void mappingObjectProps() {
        var model = fillModel();
        var map = ModelTracker.createMap(model).map(el -> el.item.getProductId(), "itemId")
                .map("item.product", "item");

        var fields = map.toList();

        assertNotNull(fields);
        assertEquals(fields.size(), 2);

        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "itemId")));
        assertEquals(ListHelper.safeFindFirst(fields, el -> Objects.equals(el.getName(), "itemId")).map(Field::getPrevValue).orElse(null), model.getItem().getProductId());

        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "item")));
        assertEquals(ListHelper.safeFindFirst(fields, el -> Objects.equals(el.getName(), "item")).map(Field::getPrevValue).orElse(null), model.getItem().getProduct());
    }

    @Test
    void mapUsingAttributes() {
        // Questi sono i servizi di base che saranno oggeti di DependecyInjection
        var model = new AnnotatedTestModel();

        model.setFieldMappedWithOtherName("field mapped as User.date");
        model.setFieldMappedWithOtherName2("field mapped as User.price");
        var product = new AnnotatedTestModel();
        var productRegistry = new TestModel();
        productRegistry.setName("product registry");
        product.setRegistry(productRegistry);
        model.setProduct(product);

        model.setNullProduct(new AnnotatedTestModel());
        var ignoredFieldObject = new TestModel();
        model.setIgnoredObjectField(ignoredFieldObject);

        ignoredFieldObject = new TestModel();
        ignoredFieldObject.setName("Ignored");
        model.setIgnoredField1(ignoredFieldObject);

        model.setIgnoredField2("Ignored");
        var registry = new TestModel();
        registry.setName("registry");
        model.setRegistry(registry);
        var user = new TestModel();
        user.setName("user");
        var userDate = new GregorianCalendar(2023, Calendar.JULY, 10).getTime();
        user.setDate(userDate);
        var userPrice = 126.72;
        user.setPrice(userPrice);
        model.setUser(user);
        var row1 = new TestModelItem();
        row1.setProductId("P1");
        row1.setProduct("product1");
        row1.setStock(10);
        row1.setPrice(100.34);
        var row2 = new TestModelItem();
        row1.setProductId("P2");
        row1.setProduct("product2");
        row1.setStock(10);
        row1.setPrice(100.34);
        model.setRows(List.of(row1, row2));

        var map = ModelTracker.mapAll(model);

        var fields = map.toList();

        //il campo Utente dev'essere mappato come "User"
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "currentUser")));
        assertFalse(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "user")));

        //il campo CampoMappatoComeAltro viene mappato come Utente?.Data con nome "UtenteData" e quindi dev'essere presente un campo UtenteData di contenente una data
        assertFalse(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "fieldMappedWithOtherName")));
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "userDate") && Objects.equals(el.getPrevValue(), FieldMapper.convertValue(userDate))));

        //il campo CampoMappatoComeAltro2 viene mappato come Utente?.Prezzo con nome "UtentePrezzo" e quindi dev'essere presente un campo UtentePrezzo di contenente una prezzo con formattazione invariant
        assertFalse(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "fieldMappedWithOtherName2")));
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "userPrice") && Objects.equals(el.getPrevValue(), FieldMapper.convertValue(userPrice))));

        //il campo anagrafica dev'essere mappato come Anagrafica (recuperato dal nome del campo)
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "registry")));

        //i campi "CampoIgnorato1" e "CampoIgnorato2" non devono essere presenti
        assertTrue(fields.stream().noneMatch(el -> List.of("ignoredField1", "ignoredField2").contains(el.getName())));

        //il valore del campo Prodotto deve corrispondere a model.Prodotto.Anagrafica.Descrizione come definito nell'attributo Mapping

        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "productRegistry")));
        assertEquals(model.getProduct().getRegistry().getName(),
                fields.stream().filter(el -> Objects.equals(el.getName(), "productRegistry")).map(Field::getPrevValue).findFirst().orElse(null));

        //il campo ProdottoNull non è valorizzato e deve corrispondere a model.Prodotto.Anagrafica.Descrizione come definito nell'attributo Mapping
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "nullProduct")));
        assertEquals("",
                fields.stream().filter(el -> Objects.equals(el.getName(), "nullProduct")).map(Field::getPrevValue).findFirst().orElse(null));

        //mappando con Map anche i campi ignorati da attributo devono essere aggiunti
        assertFalse(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "ignoredField1")));
        assertFalse(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "ignoredField2")));

        map = map.map("ignoredField2");
        fields = map.toList();

        assertFalse(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "ignoredField1")));
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "ignoredField2")));

        //i campi non primitivi sono ignorati di default da MapAll
        assertFalse(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "ignoredObjectField")));
    }

    @Test
    public void TestErroreTypoTrackingAttributi() {
        var standardOut = System.out;
        var outputStreamCaptor = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamCaptor));

        //Mappo come "Campo.Descr" invece di "Campo.Descrizione" e mi aspetto una ArgumentException
        var model = new AnnotatedTestModelWithTypo();
        var field = new TestModel();
        field.setName("description");
        model.setField(field);
        var fields = ModelTracker.mapAll(model).toList();

        assertTrue(outputStreamCaptor.toString().startsWith("ChangeTracker, error"));
        assertTrue(outputStreamCaptor.toString().length()>0);
        System.setOut(standardOut);
        System.out.println(outputStreamCaptor);

    }

    @Test
    public void mapAttributeeToNullSubmodelData() {

        var model = new AnnotatedTestModel();
        var nullProductModel = new AnnotatedTestModel();

        //set product to null
        nullProductModel.setRegistry(null);
        model.setNullProduct(nullProductModel);

        var map = ModelTracker.mapAll(model);
        var fields = map.toList();

        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(), "nullProduct")&& Objects.equals(el.getPrevValue(), "")));
    }

    @Test
    void singleTextMapTest() {
        var registryName = "test map";
        var dateTime = new Date();
        //Mappo come "Campo.Anagrafica.Descrizione" e imposto il modello Anagrafica a null prima del mapAll. Mi aspetto che restituisca un errore gestito
        var model = new AnnotatedTestModel();
        var registry = new TestModel();
        registry.setName(registryName);
        registry.setDate(dateTime);
        registry.setPrice(15.894);
        registry.setFlagBit(true);
        model.setRegistry(registry);

        //Prima provo che il mapping funzioni correttamente nel caso in cui il modello fosse riempito. Mi aspetto che riesca correttamente a recuperare il campo
        var fields = ModelTracker.map(model, "registry.name", "description")
                .map("registry.date")
                .map("registry.price")
                .map("registry.flagBit", "bit").toList();

        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(),"description") && Objects.equals(el.getPrevValue(), registryName)));
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(),"registryDate") && Objects.equals(el.getPrevValue(), FieldMapper.convertValue(dateTime))));
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(),"registryPrice") && Objects.equals(el.getPrevValue(), "15.894")));
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(),"bit") && Objects.equals(el.getPrevValue(), "true")));

        model.setRegistry(null);

        fields = ModelTracker.createMap(model).map("registry.name", "description").map("registry.flagBit").toList();

        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(),"description") && Objects.equals(el.getPrevValue(),"")));
        assertTrue(fields.stream().anyMatch(el -> Objects.equals(el.getName(),"registryFlagBit")));
    }

}

