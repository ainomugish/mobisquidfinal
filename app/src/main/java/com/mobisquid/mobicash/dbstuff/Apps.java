package com.mobisquid.mobicash.dbstuff;

import com.orm.SugarRecord;

/**
 * Created by andrew on 7/1/17.
 */

public class Apps extends SugarRecord {
    public Apps() {
    }

    boolean social;
    boolean finance;
    boolean services;
    boolean business;
    boolean eshop;
    boolean beacon;
    boolean crowdpolice;

    public Apps(boolean social, boolean finance, boolean services,
                boolean business,
                boolean eshop,
                boolean beacon,
                boolean crowdpolice) {

        this.social = social;
        this.finance = finance;
        this.services = services;
        this.business = business;
        this.eshop = eshop;
        this.beacon = beacon;
        this.crowdpolice = crowdpolice;
    }

    public boolean isSocial() {
        return social;
    }

    public void setSocial(boolean social) {
        this.social = social;
    }

    public boolean isFinance() {
        return finance;
    }

    public void setFinance(boolean finance) {
        this.finance = finance;
    }

    public boolean isServices() {
        return services;
    }

    public void setServices(boolean services) {
        this.services = services;
    }

    public boolean isBusiness() {
        return business;
    }

    public void setBusiness(boolean business) {
        this.business = business;
    }

    public boolean isEshop() {
        return eshop;
    }

    public void setEshop(boolean eshop) {
        this.eshop = eshop;
    }

    public boolean isBeacon() {
        return beacon;
    }

    public void setBeacon(boolean beacon) {
        this.beacon = beacon;
    }

    public boolean isCrowdpolice() {
        return crowdpolice;
    }

    public void setCrowdpolice(boolean crowdpolice) {
        this.crowdpolice = crowdpolice;
    }


}
