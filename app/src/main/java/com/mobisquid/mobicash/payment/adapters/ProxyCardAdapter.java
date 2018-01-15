package com.mobisquid.mobicash.payment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.payment.model.ProxyCard;
import com.mobisquid.mobicash.payment.model.ProxyCash;

import java.util.List;


/**
 * Created by mobicash on 8/13/16.
 */
public class ProxyCardAdapter extends RecyclerView.Adapter<ProxyCashViewHolder> {
    private List<ProxyCard> itemList;
    private Context context;
    private OnItemClickListener listener;

    public ProxyCardAdapter(Context context, List<ProxyCard> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.context = context;
        this.listener = listener;
    }


    @Override
    public ProxyCashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, null);
        ProxyCashViewHolder rcv = new ProxyCashViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final ProxyCashViewHolder holder, final int position) {
        final ProxyCard item = itemList.get(position);
        holder.tvName.setText(item.getCardNumber());
        holder.itemView.setTag(item);
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(v, item);
                }
            }
        });
        //holder.countryName.setText(itemList.get(position).getName());
        //holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());
        //holder.itemView.setTag(item);

    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }


    public interface OnItemClickListener {

        void onItemClick(View view, ProxyCard viewModel);

    }
    public void refreshList(List<ProxyCard> list) {
        itemList = list;
        notifyDataSetChanged();
    }

}
