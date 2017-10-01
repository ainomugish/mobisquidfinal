package com.mobisquid.mobicash.ecommerce.bussiness;

import android.support.annotation.Nullable;


import com.mobisquid.mobicash.ecommerce.models.ProductModel;

import java.util.ArrayList;

/**
 * Created by ambre on 6/2/2017 bt ${File}
 */

public class Basket {

    public static final String KEY_BASKET = "basket";

    private static Basket basket = new Basket();
    private ArrayList<ProductModel> list;

    private Basket() {
        list = new ArrayList<>();
    }

    public void setList(@Nullable ArrayList<ProductModel> list) {
        if(list==null)
            this.list = new ArrayList<>();
        else
              this.list = list;
    }

    public static Basket getBasket() {
        return basket;
    }

    public void clearBasket(){
        list.clear();
    }

    public void addToBasket(ProductModel productModel) {

        if (!list.contains(productModel)) {
            productModel.setQuantity(1);
            list.add(productModel);
        } else {
            int quantity = productModel.getQuantity();
            productModel.setQuantity(++quantity);
        }
    }

    public double getTotalOfThisItem(ProductModel productModel) {
        return productModel.getPrice() * productModel.getQuantity();
    }

    public double getTotalOfBasket() {
        double total = 0;
        for (int i = 0; i < list.size(); i++) {
            total = total + getTotalOfThisItem(list.get(i));
        }
        return total;
    }

    public ArrayList<ProductModel> getList() {
        return list;
    }

    public int getTotalCount(){
        int quantitiy = 0;
        for (ProductModel productModel : basket.getList()) {
            quantitiy+=productModel.getQuantity();
        }
        return quantitiy;
    }
}
