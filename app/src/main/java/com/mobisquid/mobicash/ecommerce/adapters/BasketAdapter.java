package com.mobisquid.mobicash.ecommerce.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.MainActivity;
import com.mobisquid.mobicash.ecommerce.bussiness.Basket;
import com.mobisquid.mobicash.ecommerce.callbacks.IUpdateTotal;
import com.mobisquid.mobicash.ecommerce.models.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ambre on 6/2/2017 bt ${File}
 */

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.CustomViewHolder> {

    private ArrayList<ProductModel> list;
    private Context context;
    private IUpdateTotal updateTotal;

    public BasketAdapter(Context context, IUpdateTotal updateTotal) {
        this.context = context;
        this.list= Basket.getBasket().getList();
        this.updateTotal=updateTotal;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_row_basket, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ProductModel productModel = list.get(position);
        holder.productPrice.setText(String.format(context.getString(R.string.price), productModel.getPrice()));
        holder.productName.setText(productModel.getName());
        holder.quantity.setText(String.valueOf(productModel.getQuantity()));
        Picasso.with(context).load(productModel.getImage(0)).into(holder.productImage);
        holder.total.setText(String.valueOf(Basket.getBasket().getTotalOfThisItem(productModel)));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView productImage;
        private TextView productName;
        private TextView productPrice;
        private TextView total;
        private TextView subtract, add;
        private TextView quantity;
        private TextView remove;

        public CustomViewHolder(View itemView) {
            super(itemView);
            remove = (TextView) itemView.findViewById(R.id.remove);
            quantity = (TextView) itemView.findViewById(R.id.quantity);
            subtract = (TextView) itemView.findViewById(R.id.subtract);
            add = (TextView) itemView.findViewById(R.id.add);
            total = (TextView) itemView.findViewById(R.id.total);
            productImage = (ImageView) itemView.findViewById(R.id.productimage);
            productName = (TextView) itemView.findViewById(R.id.productname);
            productPrice = (TextView) itemView.findViewById(R.id.productprice);

            subtract.setOnClickListener(this);
            add.setOnClickListener(this);
            remove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.subtract: {
                    int quantity = list.get(getAdapterPosition()).getQuantity();
                    list.get(getAdapterPosition()).setQuantity(--quantity);
                    updateTotal.updateTotal();
                    updateCount();
                    if (quantity == 0) {
                        list.remove(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                    } else {
                        notifyItemChanged(getAdapterPosition());
                    }
                }
                break;

                case R.id.add: {
                    int quantity = list.get(getAdapterPosition()).getQuantity();
                    list.get(getAdapterPosition()).setQuantity(++quantity);
                    updateTotal.updateTotal();
                    updateCount();
                    notifyItemChanged(getAdapterPosition());
                }
                break;

                case R.id.remove: {
                    list.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    updateTotal.updateTotal();
                    updateCount();
                }
                break;
            }
        }
    }

    private void updateCount(){
        if (context instanceof MainActivity) {
            ((MainActivity) context).updateCount();
        }
    }
}
