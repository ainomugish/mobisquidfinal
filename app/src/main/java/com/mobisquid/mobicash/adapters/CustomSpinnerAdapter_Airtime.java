package com.mobisquid.mobicash.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.NetworkObject;
import com.mobisquid.mobicash.utils.Vars;

import java.util.List;
;

/***** Adapter class extends with ArrayAdapter ******/
public class CustomSpinnerAdapter_Airtime extends BaseAdapter{

   private Activity activity;
   private LayoutInflater inflater;
   private List<NetworkObject> feedItems;
   Vars vars;


   public CustomSpinnerAdapter_Airtime(Activity activity, List<NetworkObject> feedItems) {
      this.activity = activity;
      this.feedItems = feedItems;
      vars = new Vars(activity);
   }

   @Override
   public int getCount() {
      return feedItems.size();
   }

   @Override
   public Object getItem(int location) {
      return feedItems.get(location);
   }


   public void setData(List<NetworkObject> data) {
      this.feedItems = data;
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @SuppressWarnings("null")
   @Override
   public View getView(int position, View convertView, ViewGroup parent) {

      if (inflater == null)
         inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      if (convertView == null)
         convertView = inflater.inflate(R.layout.newtwork_airtime_row_list ,null);


      TextView name = (TextView) convertView.findViewById(R.id.description);
     // TextView type = (TextView) convertView.findViewById(R.id.type);
    //  TextView price = (TextView) convertView.findViewById(R.id.price);
     // TextView id = (TextView) convertView.findViewById(R.id.id);

      final NetworkObject item = feedItems.get(position);
      if(position==0){
         name.setText("Please select");

     }else{
         name.setText(item.getDescription());

      }






      return convertView;
   }

}