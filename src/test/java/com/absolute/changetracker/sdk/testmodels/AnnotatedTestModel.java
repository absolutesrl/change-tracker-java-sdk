package com.absolute.changetracker.sdk.testmodels;

import com.absolute.changetracker.sdk.core.Tracker;

import java.util.List;

public class AnnotatedTestModel{
    @Tracker(name = "userDate", mapping = "user.date")
    String fieldMappedWithOtherName;

    @Tracker(name = "userPrice", mapping = "user.price")
    String fieldMappedWithOtherName2;

    @Tracker(name = "currentUser", mapping = "user.name")
    TestModel user;

    @Tracker(mapping = "registry.name")
    TestModel registry;

    @Tracker(name = "productRegistry", mapping = "product.registry.name")
    AnnotatedTestModel product;

    @Tracker(mapping = "nullProduct.registry.name")
    AnnotatedTestModel nullProduct;

    TestModel ignoredObjectField;

    @Tracker(ignore = true)
    TestModel ignoredField1;

    @Tracker(ignore = true)
    String ignoredField2;

    List<TestModelItem> rows;

    public List<TestModelItem> getRows() {
        return rows;
    }

    public void setRows(List<TestModelItem> rows) {
        this.rows = rows;
    }

    public String getFieldMappedWithOtherName() {
        return fieldMappedWithOtherName;
    }

    public void setFieldMappedWithOtherName(String fieldMappedWithOtherName) {
        this.fieldMappedWithOtherName = fieldMappedWithOtherName;
    }

    public String getFieldMappedWithOtherName2() {
        return fieldMappedWithOtherName2;
    }

    public void setFieldMappedWithOtherName2(String fieldMappedWithOtherName2) {
        this.fieldMappedWithOtherName2 = fieldMappedWithOtherName2;
    }

    public TestModel getUser() {
        return user;
    }

    public void setUser(TestModel user) {
        this.user = user;
    }

    public TestModel getRegistry() {
        return registry;
    }

    public void setRegistry(TestModel registry) {
        this.registry = registry;
    }

    public AnnotatedTestModel getProduct() {
        return product;
    }

    public void setProduct(AnnotatedTestModel product) {
        this.product = product;
    }

    public AnnotatedTestModel getNullProduct() {
        return nullProduct;
    }

    public void setNullProduct(AnnotatedTestModel nullProduct) {
        this.nullProduct = nullProduct;
    }

    public TestModel getIgnoredObjectField() {
        return ignoredObjectField;
    }

    public void setIgnoredObjectField(TestModel ignoredObjectField) {
        this.ignoredObjectField = ignoredObjectField;
    }

    public TestModel getIgnoredField1() {
        return ignoredField1;
    }

    public void setIgnoredField1(TestModel ignoredField1) {
        this.ignoredField1 = ignoredField1;
    }

    public String getIgnoredField2() {
        return ignoredField2;
    }

    public void setIgnoredField2(String ignoredField2) {
        this.ignoredField2 = ignoredField2;
    }
}