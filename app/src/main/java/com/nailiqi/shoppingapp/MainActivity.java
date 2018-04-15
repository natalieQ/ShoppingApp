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
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nailiqi.shoppingapp.Models.Budget;
import com.nailiqi.shoppingapp.Models.Product;
import com.nailiqi.shoppingapp.Utils.ShoppingRequestListAdapter;
import com.nailiqi.shoppingapp.Utils.ShoppingResultListAdapter;

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
    private List<Product> requestList;
    private List<Product> resultList;
    //internal data structure to sort product by priority
    //product with equal priority will be stored in a list of products
    private TreeMap<Integer, List<Product>> products;
    private ShoppingRequestListAdapter requestAdapter;
    private ShoppingResultListAdapter resultAdapter;

    //widgets
    private EditText mProductname, mPrice, mPriority, mBudget;
    private Button btnAddProduct, btnRetrieveFile, btnShopping, btnRestart, btnSaveFile;
    private ListView mListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize widgets and vars
        requestList = new ArrayList<>();
        resultList = new ArrayList<>();
        products = new TreeMap<>();

        mProductname = (EditText) findViewById(R.id.etProductName);
        mPrice = (EditText) findViewById(R.id.etPrice);
        mPriority = (EditText) findViewById(R.id.etPriority);
        mBudget = (EditText) findViewById(R.id.etBudget);
        btnAddProduct = (Button) findViewById(R.id.btnAdd);
        btnRetrieveFile = (Button) findViewById(R.id.btnRetrieve);
        btnShopping = (Button) findViewById(R.id.btnShopping);
        btnRestart = (Button) findViewById(R.id.btnClear);
        btnSaveFile = (Button) findViewById(R.id.btnSave);
        mListview = (ListView) findViewById(R.id.listview);

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
                    Toast.makeText(context, "InValid input, please try again.",
                            Toast.LENGTH_SHORT).show();
                    refreshProductField();
                }
            }
        });

        //go shopping button
        btnShopping.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    double budget = Double.parseDouble(mBudget.getText().toString());

                    if(budget <= 0) {
                        Toast.makeText(context, "Budget can't be less than 0, please try again.",
                                Toast.LENGTH_SHORT).show();
                        mBudget.setText(null);
                    }
                    else if(requestList.size() == 0) {
                        Toast.makeText(context, "You have not entered any products to buy yet.",
                                Toast.LENGTH_SHORT).show();
                    }else {

                        //generate new result list based on budget
                        Budget budgetObject = new Budget(budget);
                        resultList = budgetObject.getResultList(budget, products, requestList);

                        //display result
                        mBudget.setText(null);
                        resultAdapter = new ShoppingResultListAdapter(context, R.layout.section_shopping_listview, resultList);
                        mListview.setAdapter(resultAdapter);

                    }

                }catch (Exception ex){
                    Toast.makeText(context, "InValid input for budget.",
                            Toast.LENGTH_SHORT).show();
                    mBudget.setText(null);
                }
            }
        });

        //startover button
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBudget.setText(null);
                refreshProductField();

                requestList.clear();
                resultList.clear();
                products.clear();
                mListview.setAdapter(requestAdapter);
            }
        });
    }

    /**
     * utility method to add product to product list
     */
    private void addProductToList(Product newProduct) {
        //if product already existed, overwrite its price and priority
        for(Product product : requestList) {
            if(product.getName().equals(newProduct.getName())) {
                requestList.remove(product);
                break;
            }
        }

        requestList.add(newProduct);

        //display shopping list
        requestAdapter = new ShoppingRequestListAdapter(context, R.layout.section_shopping_listview, requestList);
        mListview.setAdapter(requestAdapter);

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

    private void refreshProductList(List<Product> productList) {
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
