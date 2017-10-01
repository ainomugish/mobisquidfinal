package com.mobisquid.mobicash.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.utils.CircleTransform;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by mobicash on 10/3/16.
 */
public class Contact_Adapter extends RecyclerView.Adapter<Contact_Adapter.NoticeViewHolder> implements Filterable {

    List<ContactDetailsDB>  noticelist;
    OnItemClickListener listener;
    private List<ContactDetailsDB> filteredData = null;
    private List<ContactDetailsDB> originalData = null;
    private ItemFilter mFilter;
    Vars vars;
    private SparseBooleanArray selectedItems;
    public Contact_Adapter(Context context, List<ContactDetailsDB> noticelist){
        this.noticelist = noticelist;
        originalData=noticelist;
        this.selectedItems = new SparseBooleanArray();
        vars = new Vars(context);

    }
    public void removeData(int position) {
        System.out.println("removeData==="+position);
        notifyItemRemoved(position);
    }
    @Override
    public NoticeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.

                from(viewGroup.getContext()).
                inflate(R.layout.row_contact_list, viewGroup, false);


        return new NoticeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NoticeViewHolder holder, int position) {
        final ContactDetailsDB item = noticelist.get(position);
        holder.subject.setText(item.getUsername());
       // holder.message.setText(item.getMobile());
        holder.itemView.setActivated(selectedItems.get(position, false));

        Picasso.with(vars.context)
                .load(item.getUrl().trim())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .transform(new CircleTransform())
                .into(holder.profilepic, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        // Try again online if cache failed
                        Picasso.with(vars.context)
                                .load(item.getUrl().trim())
                                .placeholder(R.drawable.noimage)
                                .transform(new CircleTransform())
                                .error(R.drawable.noimage)
                                .into(holder.profilepic);
                    }
                });




    }
    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }
    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }
    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (noticelist != null && noticelist.size() > 0 ) ? noticelist.size() : 0;
    }
    @Override
    public Filter getFilter() {
        mFilter= new ItemFilter();

        return mFilter;
    }
    public ContactDetailsDB getItem(int position) {
        return noticelist.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class NoticeViewHolder extends RecyclerView.ViewHolder{
        protected TextView subject;
     //   protected TextView message;
        protected ImageView profilepic;

        public NoticeViewHolder(final View itemView) {
            super(itemView);

            profilepic = (ImageView)  itemView.findViewById(R.id.person_photo);
            subject =  (TextView) itemView.findViewById(R.id.person_name);
        //    message = (TextView)  itemView.findViewById(R.id.person_number);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }

    }
    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();



            final List<ContactDetailsDB> list = originalData;

            int count = list.size();
            final ArrayList<ContactDetailsDB> nlist = new ArrayList<>(count);

            String filterableString ;



            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getUsername();


                if (filterableString.toLowerCase(Locale.getDefault()).contains(constraint)) {

                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //contactList.clear();
            filteredData = (ArrayList<ContactDetailsDB>) results.values;

            noticelist=filteredData;
            notifyDataSetChanged();
        }

    }
}
