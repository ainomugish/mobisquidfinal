package com.mobisquid.mobicash.model;

/**
 * Created by MOBICASH on 24-Apr-15.
 */
public class Bouquet {
   private String id;
   private String name;
   private String price;

   public Bouquet() {
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
   }

   public String type;

   public String getCommission() {
      return commission;
   }

   public void setCommission(String commission) {
      this.commission = commission;
   }

   public String commission;
   public Bouquet(String id, String name, String price,String type,String commission) {
      super();
      this.id = id;
      this.name = name;
      this.price = price;
      this.type = type;
      this.commission =commission;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getPrice() {
      return price;
   }

   public void setPrice(String price) {
      this.price = price;
   }

}
