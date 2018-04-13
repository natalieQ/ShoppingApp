package com.nailiqi.shoppingapp.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Budget {

    private double budget;

    public Budget(double budget) {
        this.budget = budget;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    //method to generate result list based on budget
    public List<Product> getResultList(double budget, TreeMap<Integer, List<Product>> products) {

        List<Product> resultList = new ArrayList<>();
        for(List<Product> list : products.values()) {
            for(Product product: list) {
                double unitPrice = product.getPrice();
                int qty = (int) (budget / unitPrice);
                if(qty < 1) {
                    resultList.add(product);
                }else {
                    budget -= qty*unitPrice;
                    product.setQty(qty);
                    product.setPurchased(true);
                    resultList.add(product);
                }
            }
        }
        return resultList;
    }
}
