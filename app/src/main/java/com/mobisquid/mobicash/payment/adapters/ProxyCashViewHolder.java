package com.mobisquid.mobicash.payment.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobisquid.mobicash.R;


/**
 * Created by mobicash on 8/13/16.
 */
public class ProxyCashViewHolder extends RecyclerView.ViewHolder {

    public TextView tvName;
    public ImageView ivDelete;

    public ProxyCashViewHolder(View itemView) {
        super(itemView);
        tvName = (TextView) itemView.findViewById(R.id.tvName);
        ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
    }

}
