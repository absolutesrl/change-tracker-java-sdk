package com.absolute.changetracker.sdk.testmodels;

import com.absolute.changetracker.sdk.core.Tracker;

public class AnnotatedTestModelWithTypo {
    //typo on field mapping
    @Tracker(mapping = "field.naem")
    private TestModel field;

    public void setField(TestModel field) {
        this.field = field;
    }

    public TestModel getField() {
        return field;
    }
}
