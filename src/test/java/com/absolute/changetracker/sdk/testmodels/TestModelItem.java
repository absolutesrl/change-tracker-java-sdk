package com.absolute.changetracker.sdk.testmodels;

public class TestModelItem {
    public String productId;
    public String product;
    public int stock;
    public double price;

    public TestModelItem() {
    }

    public TestModelItem(String productId, String product, int stock, double price) {
        this.productId = productId;
        this.product = product;
        this.stock = stock;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
