package com.mobisquid.mobicash.ecommerce.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobisquid.mobicash.ecommerce.network.ApiClient;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Zeera on 5/30/2017 bt ${File}
 */

public class ProductModel implements Serializable{
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Image")
    @Expose
    private List<String> image;
    @SerializedName("Color")
    @Expose
    private String color;
    @SerializedName("Price")
    @Expose
    private double price;
    @SerializedName("Cost")
    @Expose
    private double cost;
    @SerializedName("ProductCategories")
    @Expose
    private Object productCategories;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("ApplicationId")
    @Expose
    private String applicationId;

    private int quantity;


    public Integer getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage(int posi) {
        if(image.size()<=posi)
            return "";
        return ApiClient.BASE_URL_ECOMMERCE+image.get(posi);
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public String getColor() {
        return color;
    }


    public double getPrice() {
        return price;
    }
}
