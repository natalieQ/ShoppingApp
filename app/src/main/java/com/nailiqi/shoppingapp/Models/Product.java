package com.nailiqi.shoppingapp.Models;

public class Product {

    private String name;
    private double price;
    private int priority;
    private int qty;
    private boolean purchased;

    public Product(String name, double price, int priority, int qty, boolean purchased) {
        this.name = name;
        this.price = price;
        this.priority = priority;
        this.qty = qty;
        this.purchased = purchased;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", priority=" + priority +
                ", qty=" + qty +
                ", purchased=" + purchased +
                '}';
    }
}
