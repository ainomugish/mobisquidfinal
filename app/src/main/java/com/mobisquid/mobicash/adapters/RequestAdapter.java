package com.mobisquid.mobicash.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.Transactions;

import java.util.List;

/**
 * Created by mobicash on 10/3/16.
 */
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.RecentViewHolder> {

    OnItemClickListener listener;
    List<Transactions>  requestlist;
    private SparseBooleanArray selectedItems;
    Context context;
    public RequestAdapter(Context context ,List<Transactions> requestlist){
        this.requestlist = requestlist;
        this.selectedItems = new SparseBooleanArray();
        this.context = context;

    }
    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.

                from(viewGroup.getContext()).
                inflate(R.layout.row_request_list, viewGroup, false);
        return new RecentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentViewHolder holder, int position) {
        Transactions item = requestlist.get(position);

        holder.name.setText(item.getReceiverName());
        holder.amount.setText(item.getAmount());
        holder.date.setText(item.getTimestamp());
        holder.details.setText(item.getDetails());
        holder.itemView.setActivated(selectedItems.get(position, false));


    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (requestlist != null && requestlist.size() > 0 ) ? requestlist.size() : 0;
    }
    public Transactions getItem(int position) {
        return requestlist.get(position);
    }
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder{
        protected TextView name,details;
        protected TextView amount,date;

        public RecentViewHolder(final View itemView) {
            super(itemView);

            name =  (TextView) itemView.findViewById(R.id.name);
            amount = (TextView)  itemView.findViewById(R.id.amount);
            date =  (TextView) itemView.findViewById(R.id.date);
            details =  (TextView) itemView.findViewById(R.id.details);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }

    }

}
