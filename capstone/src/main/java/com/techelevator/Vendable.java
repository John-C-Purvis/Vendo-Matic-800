package com.techelevator;

public class Vendable {

    private String slotLocation;
    private String productName;
    private double purchasePrice;
    private String productType;
    private int numberInStock;

    public Vendable(String slotLocation, String productName, double purchasePrice, String productType, int numberInStock) {
        this.slotLocation = slotLocation;
        this.productName = productName;
        this.purchasePrice = purchasePrice;
        this.productType = productType;
        this.numberInStock = numberInStock;
    }

    public String getSlotLocation() {
        return slotLocation;
    }

    public void setSlotLocation(String slotLocation) {
        this.slotLocation = slotLocation;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public int getNumberInStock() {
        return numberInStock;
    }

    public void setNumberInStock(int numberInStock) {
        this.numberInStock = numberInStock;
    }
}
