package com.mobisquid.mobicash.model;

/**
 * Created by mobicash on 10/12/15.
 */
public class Person {

    String name;

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    String contactid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    String phonenumber;
public Person(String fullname , String phonen ,String contactids){
    name = fullname;
    phonenumber = phonen;
    contactid = contactids;

}

}
