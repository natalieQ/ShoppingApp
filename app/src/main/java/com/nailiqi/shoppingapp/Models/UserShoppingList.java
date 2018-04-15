package com.nailiqi.shoppingapp.Models;

public class UserShoppingList {

    private String user_id;
    private String shoppingList;

    public UserShoppingList(String user_id, String shoppingList) {
        this.user_id = user_id;
        this.shoppingList = shoppingList;
    }

    public UserShoppingList() { }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(String shoppingList) {
        this.shoppingList = shoppingList;
    }


    @Override
    public String toString() {
        return "UserShoppingList{" +
                "user_id='" + user_id + '\'' +
                ", shoppingList='" + shoppingList + '\'' +
                '}';
    }
}
