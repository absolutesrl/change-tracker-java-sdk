package it.changetracker.sdk.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class Row {
    String state;
    String key;
    String desc;
    List<Field> fields;
    List<Table> tables;

    public Row() {
        fields = new ArrayList<>();
        tables = new ArrayList<>();
    }
    public Row(String key) {
        this();
        this.key = key;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) { this.key = key; }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    @JsonIgnore
    public boolean isFilled() {
        return fields != null && !fields.isEmpty() || tables != null &&
                tables.stream().anyMatch(t -> t.rows != null &&
                        t.rows.stream().anyMatch(Row::isFilled));
    }

}
