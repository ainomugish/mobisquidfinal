package com.mobisquid.mobicash.model;

/**
 * Created by andrew on 7/30/17.
 */

public class Categorymodel {
    int Id;
    String Name;
    String Image;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getParentId() {
        return ParentId;
    }

    public void setParentId(int parentId) {
        ParentId = parentId;
    }

    public boolean isHasSubCategories() {
        return hasSubCategories;
    }

    public void setHasSubCategories(boolean hasSubCategories) {
        this.hasSubCategories = hasSubCategories;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getApplicationId() {
        return ApplicationId;
    }

    public void setApplicationId(String applicationId) {
        ApplicationId = applicationId;
    }

    int ParentId;
    boolean hasSubCategories;
    boolean Status;
    String ApplicationId;
}
