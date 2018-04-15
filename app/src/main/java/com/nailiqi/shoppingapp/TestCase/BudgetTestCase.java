package com.nailiqi.shoppingapp.TestCase;

import com.nailiqi.shoppingapp.Models.Budget;
import com.nailiqi.shoppingapp.Models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Test cases for Budget Class
 */
public class BudgetTestCase {

    private static List<Product> resultList =  new ArrayList<>();
    private static TreeMap<Integer, List<Product>> products = new TreeMap<>();

    public static void main(String[] args) {

        ArrayList<Product> productList = new ArrayList<>();

        productList.add(new Product("apple", 1.0, 3, 0, false));
        productList.add(new Product("banana", 4.0, 1, 0, false));
        productList.add(new Product("orange", 3.0, 2, 0, false));
        productList.add(new Product("milk", 2.0, 2, 0, false));
        productList.add(new Product("egg", 10.0, 3, 0, false));
        productList.add(new Product("chocolate", 15.0, 4, 0, false));
        productList.add(new Product("fish", 3.0, 5, 0, false));

        ArrayList<Double> budgets = new ArrayList<>();
        budgets.add(1.0);
        budgets.add(4.0);
        budgets.add(11.0);
        budgets.add(24.0);
        budgets.add(33.0);
        budgets.add(37.0);
        budgets.add(44.0);


        int round = 1;
        for(double number: budgets) {
            Budget budget = new Budget(number);

            System.out.println();
            System.out.println("---------------Test Case: Round " + round + " -------------------");
            System.out.println("Budget is set at: "+ budget.getBudget());
            resultList = budget.getResultList(number, products, productList);
            System.out.println("-----------------------Shopping result is: ----------------------");
            int totalQty = 0;
            ArrayList<String> result = new ArrayList<>();
            for(Product product : resultList) {
                System.out.println(product.toString());
                if(product.isPurchased() == true) {
                    totalQty += product.getQty();
                    result.add(product.getName());
                }
            }

            System.out.println("Total bought quantity is: " + totalQty);
            System.out.print("Item purchased are: ");
            for(String name: result) {
                System.out.print(name + " ");
            }
            System.out.println();
            round++;
            //refresh products and reset purchased to false
            for(List<Product> list : products.values()) {
                for(Product product: list) {
                    product.setPurchased(false);
                    product.setQty(0);
                }
            }
        }
    }

}
