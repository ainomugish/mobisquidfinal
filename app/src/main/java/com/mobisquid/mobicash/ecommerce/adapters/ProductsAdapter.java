package com.mobisquid.mobicash.ecommerce.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.MainActivity;
import com.mobisquid.mobicash.ecommerce.fragments.ProductInfoFragment;
import com.mobisquid.mobicash.ecommerce.models.ProductModel;
import com.mobisquid.mobicash.ecommerce.models.enums.FragmentAnimationType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.CustomViewHolder> {

    private ArrayList<ProductModel> list;
    private Context context;

    public ProductsAdapter(ArrayList<ProductModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void addData(ArrayList<ProductModel> list) {
        int startPos = list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(startPos, list.size());
    }

    public void clearData() {
        list.clear();
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_list_products, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final ProductModel productModel = list.get(position);
        holder.productName.setText(productModel.getName());
        holder.productPrice.setText(String.format(Locale.ENGLISH, "Price : %.2f", productModel.getPrice()));
        if (!productModel.getImage(0).isEmpty())
            Picasso.with(context).load(productModel.getImage(0)).into(holder.productImage);
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedsFurther(productModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView productName, productPrice;
        private ImageView productImage;
        private LinearLayout llParent;

        CustomViewHolder(View itemView) {
            super(itemView);
            productImage = (ImageView) itemView.findViewById(R.id.product_image);
            productPrice = (TextView) itemView.findViewById(R.id.product_price);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            llParent = (LinearLayout) itemView.findViewById(R.id.ll_parent);
            llParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    proceedsFurther(list.get(getAdapterPosition()));
                }
            });
        }
    }

    private void proceedsFurther(ProductModel productModel) {
        ProductInfoFragment fragment = new ProductInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", productModel);
        fragment.setArguments(bundle);
        if (context instanceof MainActivity) {
            ((MainActivity) context).replaceFragment(fragment, FragmentAnimationType.GROW_FROM_BOTTOM,true);
        }
    }
}
