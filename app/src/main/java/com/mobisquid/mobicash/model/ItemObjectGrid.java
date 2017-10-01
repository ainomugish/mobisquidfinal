package com.mobisquid.mobicash.model;

/**
 * Created by mobicash on 8/13/16.
 */
public class ItemObjectGrid {
    String name;
    int photo;

    public ItemObjectGrid(String name, int photo) {
        this.name = name;
        this.photo = photo;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
