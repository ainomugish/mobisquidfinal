package com.mobisquid.mobicash.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisquid.mobicash.R;


/**
 * Created by mobicash on 8/13/16.
 */
public class RecyclerViewHolders extends RecyclerView.ViewHolder {

    public TextView countryName,couter;
    public ImageView countryPhoto;

    public RecyclerViewHolders(View itemView) {
        super(itemView);
        countryName = (TextView)itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView)itemView.findViewById(R.id.country_photo);
        couter = (TextView)itemView.findViewById(R.id.count);
    }

}
