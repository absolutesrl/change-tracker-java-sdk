package it.changetracker.sdk.testmodels;

import it.changetracker.sdk.core.Tracker;

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
