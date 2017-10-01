package com.mobisquid.mobicash.ecommerce.network;

public interface IResponse {


    void onSuccess(String body, String endPoint);

    void onError(String error, String endPoint);

    void onFailed(Throwable e, String endPoint);

}
