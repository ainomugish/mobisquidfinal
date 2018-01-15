package com.mobisquid.mobicash.payment.model;

import java.util.List;

/**
 * Created by manis on 12/29/2017.
 */

public class AllProxyBankData {
    private List<ProxyBank> proxyBankList;

    public List<ProxyBank> getProxyBankList() {
        return proxyBankList;
    }

    public void setProxyBankList(List<ProxyBank> proxyCashList) {
        this.proxyBankList = proxyCashList;
    }
}
