package com.mobisquid.mobicash.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransObj;
import com.mobisquid.mobicash.utils.Vars;

import java.util.List;


/**
 * Created by mobicash on 12/8/15.
 */
public class TranslogAdapter extends RecyclerView.Adapter<TranslogAdapter.ContactViewHolder>{
    private Activity activity;
    private List<TransObj> feedItems;
    Vars vars;
    public TranslogAdapter(Activity activity, List<TransObj> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
        vars = new Vars(activity);
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.row_translog, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        TransObj ci = feedItems.get(position);

        holder.title.setText(ci.getTransferName());
        holder.amount.setText(ci.getAmount());
        holder.description.setText(ci.getDescription());
        holder.trans_date.setText(ci.getDate());
        holder.transfername.setText(ci.getName());

    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }


    public class ContactViewHolder extends RecyclerView.ViewHolder{


        protected TextView title,amount,transfername;
       // protected Button notavailable;
        protected TextView description,trans_date;


        public ContactViewHolder(final View itemView) {
            super(itemView);

            title =  (TextView) itemView.findViewById(R.id.title);
            amount = (TextView)  itemView.findViewById(R.id.amount);
            transfername = (TextView)  itemView.findViewById(R.id.transfername);
            trans_date = (TextView)  itemView.findViewById(R.id.trans_date);
            description = (TextView)  itemView.findViewById(R.id.description);



          /*  notavailable = (Button) itemView.findViewById(R.id.notavailable);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });*/
        }

    }
}
