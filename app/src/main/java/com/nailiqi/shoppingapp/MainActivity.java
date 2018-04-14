package com.nailiqi.shoppingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nailiqi.shoppingapp.Models.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context context = MainActivity.this;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    //vars
    private List<Product> productList;
    //internal data structure to sort product by priority
    //product with equal priority will be stored in a list of products
    private TreeMap<Integer, List<Product>> products;

    //widgets
    private EditText mProductname, mPrice, mPriority, mBudget;
    private Button btnAddProduct, btnRetrieveFile, btnShopping, btnRestart, btnSaveFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize widgets and vars
        productList = new ArrayList<>();
        products = new TreeMap<>();

        mProductname = (EditText) findViewById(R.id.etProductName);
        mPrice = (EditText) findViewById(R.id.etPrice);
        mPriority = (EditText) findViewById(R.id.etPriority);
        btnAddProduct = (Button) findViewById(R.id.btnAdd);
        btnRetrieveFile = (Button) findViewById(R.id.btnRetrieve);
        btnShopping = (Button) findViewById(R.id.btnShopping);
        btnRestart = (Button) findViewById(R.id.btnClear);
        btnSaveFile = (Button) findViewById(R.id.btnSave);

        //initialize firebase auth
        setupFirebaseAuth();
        setupWidgets();
    }

    /**
     * set up widgets, add listener
     */
    private void setupWidgets(){

        //add product button
        btnAddProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String productname = mProductname.getText().toString();
                    double price = Double.parseDouble(mPrice.getText().toString());
                    int priority = Integer.parseInt(mPriority.getText().toString());

                    if(price <= 0) {
                        Toast.makeText(context, "Price can't be negative or zero, please try again.",
                                Toast.LENGTH_SHORT).show();
                        refreshProductField();
                    }
                    else if(priority < 1) {
                        Toast.makeText(context, "Priority can't be smaller than 1, please try again.",
                                Toast.LENGTH_SHORT).show();
                        refreshProductField();
                    }
                    else {
                        Product newProduct = new Product(productname,price,priority,0,false);
                        //add product to request list
                        addProductToList(newProduct);

                        refreshProductField();
                    }

                }catch (Exception ex){
                    Toast.makeText(context, "InValid input for product, please try again.",
                            Toast.LENGTH_SHORT).show();
                    refreshProductField();
                }
            }
        });
    }

    /**
     * utility method to add product to product list
     */
    private void addProductToList(Product newProduct) {
        //if product already existed, overwrite its price and priority
        for(Product product : productList) {
            if(product.getName().equals(newProduct.getName())) {
                int oldPriority = product.getPriority();
                productList.remove(product);
                products.get(oldPriority).remove(product);
                if(products.get(oldPriority).size() == 0) {
                    products.remove(oldPriority);
                }
                break;
            }
        }

        if(!products.containsKey(newProduct.getPriority())) {
            products.put(newProduct.getPriority(), new ArrayList<Product>());
        }

        products.get(newProduct.getPriority()).add(newProduct);
        productList.add(newProduct);

        refreshProductList();

        //display shopping list

    }

    /**
     * utility method to refresh add/del product text field
     */
    private void refreshProductField() {
        mProductname.setText(null);
        mPrice.setText(null);
        mPriority.setText(null);
    }

    /**
     * utility method to reset product list products qty to 0 and purchase to false
     */

    private void refreshProductList() {
        for(Product product: productList) {
            product.setPurchased(false);
            product.setQty(0);
        }
    }

    /**
     * checks to see if user is logged in
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in.");

        if(user == null){
            Intent intent = new Intent(context, SigninActivity.class);
            startActivity(intent);
        }
    }

    /**
     * Setup the firebase auth
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //check if the user is logged in
                checkCurrentUser(user);

                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        FirebaseUser user = mAuth.getCurrentUser();

        checkCurrentUser(user);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
