package com.mobisquid.mobicash.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.Categorymodel;
import com.mobisquid.mobicash.utils.Vars;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by mobicash on 8/13/16.
 */
public class CategoryAdapter extends RecyclerView.Adapter<RecyclerViewHolders> implements View.OnClickListener  {
    private List<Categorymodel> itemList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    Vars vars;

    public CategoryAdapter(Context context, List<Categorymodel> itemList) {
        this.itemList = itemList;
        this.context = context;
        vars= new Vars(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_grid_category, null);
        layoutView.setOnClickListener(this);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolders holder, final int position) {
        final Categorymodel item = itemList.get(position);
        holder.countryName.setText(itemList.get(position).getName());
        //holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());

        Picasso.with(context)
                .load("http://52.38.75.235:8080"+item.getImage().trim())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.countryPhoto, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed
                        Picasso.with(context)
                                .load("http://52.38.75.235:8080"+item.getImage().trim())
                                .placeholder(R.drawable.eshooping)
                                .error(R.drawable.eshooping)
                                .into(holder.countryPhoto);
                    }
                });

        holder.itemView.setTag(item);

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public void onClick( final View v) {
        onItemClickListener.onItemClick(v, (Categorymodel) v.getTag());
    }

    public interface OnItemClickListener {

        void onItemClick(View view, Categorymodel viewModel);

    }


}
