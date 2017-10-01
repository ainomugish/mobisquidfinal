package com.mobisquid.mobicash.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MOBICASH on 24-Apr-15.
 */
public class BouquetOb {

   JSONObject json = new JSONObject();

   JSONObject json_fin = new JSONObject();
   Bouquet bouquetobj;


   private String message;
   private String id;

   public String getCommission() {
      return commission;
   }

   public void setCommission(String commission) {
      this.commission = commission;
   }

   public String getPrice() {
      return price;
   }

   public void setPrice(String price) {
      this.price = price;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   List<Bouquet> bList = new ArrayList<Bouquet>();
   private String type;
   private String name;
   private String price;
   private String commission;

   private String result;
   private String bouquets;

   public String getBouquet() {
      return bouquet;
   }

   public void setBouquet(String bouquet) {
      this.bouquet = bouquet;
   }

   private String bouquet;

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public String getResult() {
      return result;
   }

   public void setResult(String result) {
      this.result = result;
   }

   public String getBouquets() {
      return bouquets;
   }

   public void setBouquets(String bouquets) {
      this.bouquets = bouquets;
   }

   public BouquetOb(String jsonn) {
      try {
         json = new JSONObject(jsonn);
         if (json.getString("message") != null) {
            message = json.getString("message");
         }
         if (json.getString("result") != null) {
            result = json.getString("result");
         }
         if (json.getString("bouquets") != null) {
            bouquets = json.getString("bouquets");
         }
      }catch (JSONException e){
         e.printStackTrace();
      }

   }

   public List<Bouquet> showResults(String jsonString) {
      try {

         json = new JSONObject(jsonString);
         if (json.getString("message") != null) {
            message = json.getString("message");
         }
         if (json.getString("result") != null) {
            result = json.getString("result");
         }
         if (json.getString("bouquets") != null) {
            bouquets = json.getString("bouquets");
            json_fin = new JSONObject(bouquets);

            if (jsonString != null && !jsonString.isEmpty()) {
               JSONObject reader = new JSONObject(jsonString);

               JSONObject bouquetObject = reader.getJSONObject("bouquets");
               JSONArray bouquetArray = bouquetObject.getJSONArray("bouquet");

               for (int i = 0; i < bouquetArray.length(); i++) {
                  bouquetobj = new Bouquet();
                  JSONObject c = bouquetArray.getJSONObject(i);
                  String id = c.getString("id");
                  String type = c.getString("type");
                  String name = c.getString("name");
                  String price = c.getString("price");
                  String commission = c.getString("commission");

                  bouquetobj.setId(id);
                  bouquetobj.setType(type);
                  bouquetobj.setName(name);
                  bouquetobj.setPrice(price);
                  bouquetobj.setCommission(commission);
                  if(id==null||type==null||name==null||price==null){

                  }else {
                     bList.add(bouquetobj);
                  }
               }
               return bList;
            }else {
               setBoquetToNull();
               return null;
            }
         } else {
            return null;
         }

      } catch (JSONException e) {
         e.printStackTrace();
         setBoquetToNull();
         return null;
      }

   }
   private void setBoquetToNull() {
      bouquetobj.setId("0");
      bouquetobj.setName("Error");
      bouquetobj.setPrice("0");
      bouquetobj.setType("0");
      bouquetobj.setCommission("0");
      bList.add(bouquetobj);

   }
}

