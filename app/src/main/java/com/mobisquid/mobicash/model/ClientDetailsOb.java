package com.mobisquid.mobicash.model;


import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by MOBICASH on 18-Apr-15.
 */
public class ClientDetailsOb {
   Gson gson;
   JSONObject json = new JSONObject();
   JSONObject json_details = new JSONObject();
   public ClientDetailsOb (){

   }
   public ClientDetailsOb(String jsons_string){
      try {
         json = new JSONObject(jsons_string);
         if (json.getString("message") != null) {
            message = json.getString("message");
         }
         if (json.getString("result") != null) {
            result = json.getString("result");
         }
         if (json.getString("details") != null) {
            details = json.getString("details");
            json_details = new JSONObject(details);

            if (json_details.getString("client_phone") != null) {
               clientnumber = json_details.getString("client_phone");
            }
            if (json_details.getString("client_nationalid") != null) {
               clientID = json_details.getString("client_nationalid");
            }
            if (json_details.getString("clientName") != null) {
               clientname = json_details.getString("clientName");
            }
            if (json_details.getString("clientphoto") != null) {
               clientphoto = json_details.getString("clientphoto");
            }
         }
      }catch (JSONException e){
         e.printStackTrace();

      }
   }


   private String message;
   private String result;

   public String getClientnumber() {
      return clientnumber;
   }

   public void setClientnumber(String clientnumber) {
      this.clientnumber = clientnumber;
   }

   public String getClientphoto() {
      return clientphoto;
   }

   public void setClientphoto(String clientphoto) {
      this.clientphoto = clientphoto;
   }

   public String getClientname() {
      return clientname;
   }

   public void setClientname(String clientname) {
      this.clientname = clientname;
   }

   public String getClientID() {
      return clientID;
   }

   public void setClientID(String clientID) {
      this.clientID = clientID;
   }

   public String getResult() {
      return result;
   }

   public void setResult(String result) {
      this.result = result;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   private String clientnumber;
   private String clientID;
   private String clientname;
   private String clientphoto;

   public String getDetails() {
      return details;
   }

   public void setDetails(String details) {
      this.details = details;
   }

   private String details;


}
