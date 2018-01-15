package com.mobisquid.mobicash.payment.model;

import java.util.List;

/**
 * Created by manis on 12/29/2017.
 */

public class AllProxyCardData {
    private List<ProxyCard> proxyCardList;

    public List<ProxyCard> getProxyCardList() {
        return proxyCardList;
    }

    public void setProxyCashList(List<ProxyCard> proxyCashList) {
        this.proxyCardList = proxyCashList;
    }
}
