package com.mobisquid.mobicash.ecommerce.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.MainActivity;
import com.mobisquid.mobicash.ecommerce.fragments.ProductFragment;
import com.mobisquid.mobicash.ecommerce.fragments.SubCategories;
import com.mobisquid.mobicash.ecommerce.models.Category;
import com.mobisquid.mobicash.ecommerce.models.enums.FragmentAnimationType;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ambre on 5/30/2017 bt ${File}
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CustomViewHolder> {

    private ArrayList<Category> list;
    private Context context;

    public CategoryAdapter(ArrayList<Category> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void addData(ArrayList<Category> data) {
        int startPos = list.size();
        list.addAll(data);
        notifyDataSetChanged();
        //notifyItemRangeInserted(startPos,list.size());
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_list_categories, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        final Category category = list.get(position);
        holder.categoryName.setText(category.getName());
        Picasso.with(context).load(category.getImageId()).into(holder.categoryImage);
        holder.llParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedsFurther(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView categoryName;
        private ImageView categoryImage;
        private LinearLayout llParent;

        CustomViewHolder(View itemView) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryimage);
            categoryName = (TextView) itemView.findViewById(R.id.categoryname);
            llParent = (LinearLayout) itemView.findViewById(R.id.ll_parent);
        }
    }

    private void proceedsFurther(Category category) {
        if (context instanceof MainActivity) {
            MainActivity activity = (MainActivity) this.context;
            if (category.getHasSubCategories()) {
                activity.replaceFragment(SubCategories.newInstance(category.getId(),
                        category.getName()), FragmentAnimationType.SLIDE_FROM_RIGHT, true);
            } else {
                activity.replaceFragment(ProductFragment.newInstance(category.getId(),
                        category.getName()), FragmentAnimationType.SLIDE_FROM_RIGHT, true);
            }
        }

    }
}
