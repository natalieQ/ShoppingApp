package com.nailiqi.shoppingapp.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nailiqi.shoppingapp.Models.Product;
import com.nailiqi.shoppingapp.R;

import java.util.List;

/**
 * User can edit product name, price, priority for shopping request list (before going shopping)
 * However user can not edit product quantity for shopping request
 * User would be able to edit quantity after shopping is made
 */

public class ShoppingRequestListAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "ShoppingRequestListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private List<Product> productList;


    public ShoppingRequestListAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        mContext = context;
        productList = objects;

    }

    static class ViewHolder{
        EditText mProductname, mPrice, mPriority, mQty;
        TextView mPurchased;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ShoppingRequestListAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new ShoppingRequestListAdapter.ViewHolder();

            holder.mProductname = (EditText) convertView.findViewById(R.id.etProductName);
            holder.mPrice = (EditText) convertView.findViewById(R.id.etPrice);
            holder.mPriority = (EditText) convertView.findViewById(R.id.etPriority);
            holder.mQty = (EditText) convertView.findViewById(R.id.etQty);
            holder.mPurchased = (TextView) convertView.findViewById(R.id.tvPurchased);

            convertView.setTag(holder);
        } else {
            holder = (ShoppingRequestListAdapter.ViewHolder) convertView.getTag();
        }

        //setup views with Product params
        holder.mProductname.setText(getItem(position).getName());
        holder.mPrice.setText(String.valueOf(getItem(position).getPrice()));
        holder.mPriority.setText(String.valueOf(getItem(position).getPriority()));
        holder.mQty.setText(String.valueOf(getItem(position).getQty()));
        holder.mPurchased.setText(String.valueOf(getItem(position).isPurchased()));

        //update productList after editing - product name
        holder.mProductname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText productname = (EditText) v;
                    productList.get(position).setName(productname.getText().toString());
                    Product newProduct = productList.get(position);

                    //if product already existed, overwrite its price and priority
                    for(Product product : productList) {
                        if(product.getName().equals(newProduct.getName())) {
                            productList.remove(product);
                            break;
                        }
                    }
                    productList.add(newProduct);
                }
            }
        });

        //update productList after editing - price
        holder.mPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText price = (EditText) v;
                    try {
                        double newPrice = Double.parseDouble(price.getText().toString());

                        if(newPrice <= 0) {
                            Toast.makeText(mContext, "Price can't be negative or zero",
                                    Toast.LENGTH_SHORT).show();
                        }

                        productList.get(position).setPrice(newPrice);
                    } catch (Exception ex) {
                        Toast.makeText(mContext, "InValid input",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //update productList after editing - priority
        holder.mPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    final int position = v.getId();
                    final EditText priority = (EditText) v;
                    try {
                        int newPriority = Integer.parseInt(priority.getText().toString());

                        if(newPriority < 1) {
                            Toast.makeText(mContext, "Priority can't be smaller than 1.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        productList.get(position).setPriority(newPriority);
                    } catch (Exception ex) {
                        Toast.makeText(mContext, "InValid input",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //update productList after editing - Qty
        holder.mPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    Toast.makeText(mContext, "You cannot change Qty before shopping. You can change it after going shopping",
                            Toast.LENGTH_SHORT).show();
                    holder.mQty.setText("0");
                }
            }
        });

        return convertView;
    }
}
