package com.nailiqi.shoppingapp.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public List<Product> getResultList(double budget, TreeMap<Integer, List<Product>> products, List<Product> requestList) {

        addToProducts(products, requestList);

        List<Product> resultList = new ArrayList<>();
        for(List<Product> list : products.values()) {

            //sort list by price in descending order. So amount of money left is minimized
            Collections.sort(list, new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    double diff = o1.getPrice() - o2.getPrice();
                    if(diff > 0){
                        return 1;
                    }
                    else if(diff < 0){
                        return -1;
                    } else {
                        return 0;
                    }
                }
            });
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

    private void addToProducts(TreeMap<Integer, List<Product>> products, List<Product> requestList){
        for(Product product: requestList){
            if(!products.containsKey(product.getPriority())) {
                products.put(product.getPriority(), new ArrayList<Product>());
            }

            products.get(product.getPriority()).add(product);
        }
    }
}
