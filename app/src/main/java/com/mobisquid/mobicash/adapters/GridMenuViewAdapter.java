package com.mobisquid.mobicash.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.ItemObjectGrid;
import com.mobisquid.mobicash.utils.Vars;

import java.util.List;



/**
 * Created by mobicash on 8/13/16.
 */
public class GridMenuViewAdapter extends RecyclerView.Adapter<RecyclerViewHolders> implements View.OnClickListener  {
    private List<ItemObjectGrid> itemList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    Vars vars;

    public GridMenuViewAdapter(Context context, List<ItemObjectGrid> itemList) {
        this.itemList = itemList;
        this.context = context;
        vars= new Vars(context);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gridview, null);
        layoutView.setOnClickListener(this);
        RecyclerViewHolders rcv = new RecyclerViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolders holder, int position) {
        ItemObjectGrid item = itemList.get(position);
        holder.countryName.setText(itemList.get(position).getName());
        holder.countryPhoto.setImageResource(itemList.get(position).getPhoto());
        holder.itemView.setTag(item);
        if(position==0){

           /* List<MessageDb> unread = Select.from(MessageDb.class)
                    .where(Condition.prop("recep")
                            .eq(vars.chk)).and(Condition.prop("extra").notEq("read")).list();
            if(!unread.isEmpty()){
                holder.couter.setVisibility(View.VISIBLE);
                holder.couter.setText(String.valueOf(unread.size()));

            }else{
                holder.couter.setVisibility(View.GONE);
            }*/

        }
        if(position==2){

            holder.couter.setVisibility(View.VISIBLE);
            holder.couter.setText("!");
        }
        if(position==3){




        }
        if(position==4){




        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    @Override
    public void onClick( final View v) {
        onItemClickListener.onItemClick(v, (ItemObjectGrid) v.getTag());
    }

    public interface OnItemClickListener {

        void onItemClick(View view, ItemObjectGrid viewModel);

    }


}
