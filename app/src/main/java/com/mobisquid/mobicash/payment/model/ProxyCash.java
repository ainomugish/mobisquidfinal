package com.mobisquid.mobicash.payment.model;

/**
 * Created by manis on 12/29/2017.
 */

public class ProxyCash {
    private String mobNo;
    private String imei;
    private String nationalId;

    public String getMobNo() {
        return mobNo;
    }

    public void setMobNo(String mobNo) {
        this.mobNo = mobNo;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }
}
