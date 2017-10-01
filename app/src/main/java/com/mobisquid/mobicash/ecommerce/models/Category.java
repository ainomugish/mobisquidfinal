package com.mobisquid.mobicash.ecommerce.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mobisquid.mobicash.ecommerce.network.ApiClient;

/**
 * Created by ambre on 5/30/2017.
 */

public class Category {
    @SerializedName("Id")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Image")
    @Expose
    private String image;
    @SerializedName("ParentId")
    @Expose
    private Integer parentId;
    @SerializedName("hasSubCategories")
    @Expose
    private Boolean hasSubCategories;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("ApplicationId")
    @Expose
    private String applicationId;

    public Category() {
    }


    public String getImageId() {
        return ApiClient.BASE_URL_ECOMMERCE+image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getHasSubCategories() {
        return hasSubCategories;
    }

    public Integer getId() {
        return id;
    }
}
