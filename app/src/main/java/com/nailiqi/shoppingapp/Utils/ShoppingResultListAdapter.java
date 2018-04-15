package com.nailiqi.shoppingapp.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
 * User can edit product quantity for shopping result list (after shopping is made)
 */

public class ShoppingResultListAdapter extends ArrayAdapter<Product> {

    private static final String TAG = "ResultListAdapter";

    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private List<Product> productList;


    public ShoppingResultListAdapter(@NonNull Context context, int resource, @NonNull List<Product> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        mContext = context;
        productList = objects;

    }

    static class ViewHolder{
        EditText  mPrice, mPriority, mQty;
        TextView  mProductname, mPurchased;
        //to record postion for edit text
        int ref;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ShoppingResultListAdapter.ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new ShoppingResultListAdapter.ViewHolder();

            holder.mProductname = (TextView) convertView.findViewById(R.id.tvProductName);
            holder.mPrice = (EditText) convertView.findViewById(R.id.etPrice);
            holder.mPriority = (EditText) convertView.findViewById(R.id.etPriority);
            holder.mQty = (EditText) convertView.findViewById(R.id.etQty);
            holder.mPurchased = (TextView) convertView.findViewById(R.id.tvPurchased);

            convertView.setTag(holder);
        } else {
            holder = (ShoppingResultListAdapter.ViewHolder) convertView.getTag();
        }

        holder.ref = position;

        //setup views with Product params
        holder.mProductname.setText(getItem(position).getName());
        holder.mPrice.setText(String.valueOf(getItem(position).getPrice()));
        holder.mPriority.setText(String.valueOf(getItem(position).getPriority()));
        holder.mQty.setText(String.valueOf(getItem(position).getQty()));
        holder.mPurchased.setText(String.valueOf(getItem(position).isPurchased()));


        //update productList after editing - price
        holder.mPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    final EditText field = (EditText) v;
                    String value = field.getText().toString();
                    Log.d(TAG, "onFocusChange: position " + holder.ref);

                    try {
                        double newPrice = Double.parseDouble(value);

                        if(newPrice <= 0) {
                            Toast.makeText(mContext, "Price can't be negative or zero.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            productList.get(holder.ref).setPrice(newPrice);
                        }

                    } catch (Exception ex) {
                        Toast.makeText(mContext, "InValid input",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //update productList after editing - priority
        holder.mPriority.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){

                    final EditText field = (EditText) v;
                    String value = field.getText().toString();
                    Log.d(TAG, "onFocusChange: position " + holder.ref);

                    try {
                        int newPriority = Integer.parseInt(value);

                        if(newPriority < 1) {
                            Toast.makeText(mContext, "Priority can't be smaller than 1.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            productList.get(holder.ref).setPriority(newPriority);
                        }

                    } catch (Exception ex) {
                        Toast.makeText(mContext, "InValid input",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //update productList after editing - Qty
        holder.mQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    if (!hasFocus){

                        final EditText field = (EditText) v;
                        String value = field.getText().toString();
                        Log.d(TAG, "onFocusChange: position " + holder.ref);

                        try {
                            int newQty = Integer.parseInt(value);

                            if(newQty < 0) {
                                Toast.makeText(mContext, "Quantity can't be smaller than 0.",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                productList.get(holder.ref).setQty(newQty);
                            }

                        } catch (Exception ex) {
                            Toast.makeText(mContext, "InValid input",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });

        return convertView;
    }
}
