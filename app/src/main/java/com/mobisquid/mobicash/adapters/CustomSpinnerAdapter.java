package com.mobisquid.mobicash.adapters;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.Bouquet;
import com.mobisquid.mobicash.utils.Vars;


/***** Adapter class extends with ArrayAdapter ******/
public class CustomSpinnerAdapter extends BaseAdapter{

   private Activity activity;
   private LayoutInflater inflater;
   private List<Bouquet> feedItems;
   Vars vars;


   public CustomSpinnerAdapter(Activity activity, List<Bouquet> feedItems) {
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


   public void setData(List<Bouquet> data) {
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
         convertView = inflater.inflate(R.layout.spinner_rows ,null);


      TextView name = (TextView) convertView.findViewById(R.id.name);
     // TextView type = (TextView) convertView.findViewById(R.id.type);
      TextView price = (TextView) convertView.findViewById(R.id.price);
     // TextView id = (TextView) convertView.findViewById(R.id.id);

      final Bouquet item = feedItems.get(position);
      if(position==0){
         name.setText("Please select name");
       //  type.setText("");
         price.setText("price");
      //   id.setText("");
     }else{
         name.setText(item.getName());
         //type.setText(item.getType());
         price.setText(item.getPrice());
//         id.setText(item.getId());
      }






      return convertView;
   }

}