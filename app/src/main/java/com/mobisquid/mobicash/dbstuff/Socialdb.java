package com.mobisquid.mobicash.dbstuff;

import com.orm.SugarRecord;

/**
 * Created by mobicash on 11/28/16.
 */

public class Socialdb extends SugarRecord {
    public Socialdb() {
    }

    Integer userid;
    String fullname;
    String mobile;
    String privacy;
    String imei;
    String location;
    float longitude;
    float latitude;
    String language;
    String profileurl;
    String code;
    String accesscode;
    String status;
    String created;
    String username;

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccesscode() {
        return accesscode;
    }

    public void setAccesscode(String accesscode) {
        this.accesscode = accesscode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socialdb(Integer userid,
                    String fullname,
                    String mobile,
                    String privacy,
                    String imei,
                    String location,
                    float longitude,
                    float latitude,
                    String language,
                    String profileurl,
                    String code,
                    String accesscode,
                    String status,
                    String created,
                    String username) {

        this.userid = userid;
        this.fullname = fullname;
        this.mobile = mobile;
        this.privacy = privacy;
        this.imei = imei;
        this.location = location;
        this.longitude = longitude;
        this.latitude = latitude;
        this.language = language;
        this.profileurl = profileurl;
        this.code = code;
        this.accesscode = accesscode;
        this.status = status;
        this.created = created;
        this.username = username;

    }
}
