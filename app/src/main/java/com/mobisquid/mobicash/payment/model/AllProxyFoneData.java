package com.mobisquid.mobicash.payment.model;

import java.util.List;

/**
 * Created by manis on 12/29/2017.
 */

public class AllProxyFoneData {
    private List<ProxyFone> proxyBankList;

    public List<ProxyFone> getProxyFoneList() {
        return proxyBankList;
    }

    public void setProxyFoneList(List<ProxyFone> proxyCashList) {
        this.proxyBankList = proxyCashList;
    }
}
