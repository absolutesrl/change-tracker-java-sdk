package it.changetracker.sdk;

import it.changetracker.sdk.core.ChangeTrackerCalculator;
import it.changetracker.sdk.core.StandardChangeTrackerCalculator;
import it.changetracker.sdk.models.RowStatus;
import it.changetracker.sdk.testmodels.TestModel;
import it.changetracker.sdk.testmodels.TestModelItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ChangeTrackerCalculatorTest {

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
    void diffBaseTest() {
        ChangeTrackerCalculator calculator = new StandardChangeTrackerCalculator();

        var model = fillModel();
        var prevMap = ModelTracker.mapAll(model).toRow("BEFORE");

        model.setName(model.getName() + "_modified");
        model.setPrice(200.15);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(model.getDate());
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        model.setDate(calendar.getTime());
        model.setFlagBit(false);

        var nextMap = ModelTracker.mapAll(model).toRow("AFTER");

        var diffRow = calculator.diff(prevMap, nextMap);

        assertEquals(diffRow.getState(), RowStatus.Modified.toString());
        Assertions.assertEquals(diffRow.getKey(), "BEFORE");
        assertNotNull(diffRow.getFields());
        assertNotNull(diffRow.getTables());
        Assertions.assertEquals(diffRow.getFields().size(), 4);
        Assertions.assertEquals(diffRow.getTables().size(), 0);
    }

    @Test
    void diffWithsubModelsTest() {
        ChangeTrackerCalculator calculator = new StandardChangeTrackerCalculator();

        var model = fillModel();
        var prevMap = ModelTracker.mapAll(model).toRow("BEFORE", List.of(
                ModelTracker.toTable("rows", model.rows.stream().map(el -> ModelTracker.mapAll(el).toRow(el.productId)).toList()))
        );

        var rows = model.getRows();
        rows.get(0).price = 20.0;
        rows.remove(1);
        rows.add(new TestModelItem("row_3_alt", "row3 alt", 6, 65.32));

        var nextMap = ModelTracker.mapAll(model).toRow("AFTER", List.of(
                ModelTracker.toTable("rows", model.rows.stream().map(el -> ModelTracker.mapAll(el).toRow(el.productId)).toList()))
        );

        var diffRow = calculator.diff(prevMap, nextMap);

        Assertions.assertEquals(diffRow.getKey(), "BEFORE");
        assertNotNull(diffRow.getFields());
        assertNotNull(diffRow.getTables());
        Assertions.assertEquals(diffRow.getFields().size(), 0);
        Assertions.assertEquals(diffRow.getTables().size(), 1);

        var table = diffRow.getTables().get(0);
        Assertions.assertEquals(table.getName(), "rows");

        var tableRows = table.getRows();

        assertNotNull(tableRows);
        Assertions.assertEquals(tableRows.size(), 3);
        Assertions.assertTrue(tableRows.stream().anyMatch(el -> Objects.equals(el.getKey(), "row_1") && Objects.equals(el.getState(), RowStatus.Modified.toString())));
        Assertions.assertTrue(tableRows.stream().anyMatch(el -> Objects.equals(el.getKey(), "row_2") && Objects.equals(el.getState(), RowStatus.Deleted.toString())));
        Assertions.assertTrue(tableRows.stream().anyMatch(el -> Objects.equals(el.getKey(), "row_3_alt") && Objects.equals(el.getState(), RowStatus.New.toString())));
    }

    @Test
    void diffNewTest() {
        ChangeTrackerCalculator calculator = new StandardChangeTrackerCalculator();
        var model = fillModel();
        var nextMap = ModelTracker.mapAll(model).toRow("AFTER");

        var diff = calculator.diff(null, nextMap);

        assertEquals(diff.getState(), RowStatus.New.toString());
    }

    @Test
    void diffDeleteTest() {
        ChangeTrackerCalculator calculator = new StandardChangeTrackerCalculator();
        var model = fillModel();
        var prevMap = ModelTracker.mapAll(model).toRow("BEFORE");

        var diff = calculator.diff(prevMap, null);

        assertEquals(diff.getState(), RowStatus.Deleted.toString());
    }

    @Test
    void diffUnchangedTest() {
        ChangeTrackerCalculator calculator = new StandardChangeTrackerCalculator();
        var model = fillModel();
        var prevMap = ModelTracker.mapAll(model).toRow("BEFORE");

        //no model change

        var nextMap = ModelTracker.mapAll(model).toRow("BEFORE");

        var diff = calculator.diff(prevMap, nextMap);

        assertEquals(diff.getState(), RowStatus.Unchanged.toString());
    }
}








