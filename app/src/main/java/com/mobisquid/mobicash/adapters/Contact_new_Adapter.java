package com.mobisquid.mobicash.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.dbstuff.ContactDetailsDB;
import com.mobisquid.mobicash.model.Person;
import com.mobisquid.mobicash.utils.Vars;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by mobicash on 11/21/15.
 */
public class Contact_new_Adapter extends RecyclerView.Adapter<Contact_new_Adapter.ContactViewHolder> implements Filterable{
    List<Person> contactList;
    Context context;
    ImageLoader imageLoader;
    public static LayoutInflater inflater = null;
    private List<Person> filteredData = null;
    private List<Person> originalData = null;
    private boolean firstSearch=true;
    private ItemFilter mFilter;
    Vars vars;
    OnItemClickListener listener;

    public Contact_new_Adapter(Context context, List<Person> contactList){

        vars = new Vars(context);
        originalData=contactList;

        this.context = context;
        this.contactList = contactList;

    }
    public Person getItem(int position) {
        return contactList != null ? contactList.get(position) : null;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.

                from(viewGroup.getContext()).
                inflate(R.layout.row_invite_contacts, viewGroup, false);


        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, final int position) {
        holder.vName.setText(contactList.get(position).getName());
        holder.vnumber.setText(contactList.get(position).getPhonenumber());
        holder.notavailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("smsto:" + contactList.get(position).getPhonenumber());
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", "Hello buddy Mobisquid is really cool join me by downloading it from: http://support.mobisquid.com/");
                vars.context.startActivity(it);
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public Filter getFilter() {
        mFilter= new ItemFilter();

        return mFilter;
    }


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();



            final List<Person> list = originalData;

            int count = list.size();
            final ArrayList<Person> nlist = new ArrayList<Person>(count);

            String filterableString ;



            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getName();


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
            filteredData = (ArrayList<Person>) results.values;

            contactList=filteredData;
            notifyDataSetChanged();
        }

    }
    public class ContactViewHolder extends RecyclerView.ViewHolder{


        protected TextView vName;
        protected Button notavailable;
        protected TextView vnumber;
        protected ImageView profilepic;


        public ContactViewHolder(final View itemView) {
            super(itemView);

            vName =  (TextView) itemView.findViewById(R.id.person_name);
            vnumber = (TextView)  itemView.findViewById(R.id.person_number);
            profilepic = (ImageView)  itemView.findViewById(R.id.person_photo);
            notavailable = (Button) itemView.findViewById(R.id.notavailable);

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
