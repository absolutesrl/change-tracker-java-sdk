package it.changetracker.sdk.testmodels;

import it.changetracker.sdk.core.Tracker;

import java.util.Date;
import java.util.List;

public class TestModel {

    public String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(float oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String name;
    @Tracker(name = "totalPrice", format = "0.000")
    public double price;
    public float oldPrice;
    public Date date;
    public boolean flagBit;
    @Tracker(mapping = "item.product")
    public TestModelItem item;
    public List<TestModelItem> rows;

    public void setItem(TestModelItem item) {
        this.item = item;
    }

    public TestModelItem getItem() {
        return item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isFlagBit() {
        return flagBit;
    }

    public void setFlagBit(boolean flagBit) {
        this.flagBit = flagBit;
    }

    public List<TestModelItem> getRows() {
        return rows;
    }

    public void setRows(List<TestModelItem> rows) {
        this.rows = rows;
    }
}
