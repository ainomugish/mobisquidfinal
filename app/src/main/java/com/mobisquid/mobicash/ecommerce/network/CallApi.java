package com.mobisquid.mobicash.ecommerce.network;

import android.app.Activity;
import android.content.Context;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.dialogs.ErrorDialog;
import com.mobisquid.mobicash.ecommerce.dialogs.LoadingDialog;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallApi {

    private IResponse iResponse;
    private ErrorDialog errorDialog;
    private LoadingDialog loadingDialog;
    private Context context;
    private String errorMessage;
    private String loadingMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getLoadingMessage() {
        return loadingMessage;
    }

    public CallApi(Context context, IResponse Iresponse) {
        this.iResponse = Iresponse;
        this.errorDialog = new ErrorDialog(context);
        this.loadingDialog = new LoadingDialog(context);
        this.context = context;
    }

    public void callService(Call<ResponseBody> call, final String endPoint) {
        if(getLoadingMessage()!=null)
        showLoadingDialog(true, getLoadingMessage());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response) {
                try {
                    if (response != null) {
                        showLoadingDialog(false, "");
                        if (response.isSuccessful()) {
                            iResponse.onSuccess(response.body().string(), endPoint);
                            showLoadingDialog(false, "");
                        } else if (response.code() == 404)
                            showErrorDialog(true, context.getString(R.string.not_found));
                        else if (response.code() == 500) {
                            showErrorDialog(true, context.getString(R.string.internal_server_error));
                        } else {
                            iResponse.onError(response.body().string(), endPoint);
                            showErrorDialog(true, response.errorBody().string());
                        }
                    }
                } catch (Exception e) {
                    showLoadingDialog(false, "");
                    showErrorDialog(true, e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call,Throwable t) {
                iResponse.onFailed(t, endPoint);
                showLoadingDialog(false, "");
                if (t instanceof SocketTimeoutException) {
                    showErrorDialog(true, context.getString(R.string.server_not_available));
                } else if (t instanceof IOException) {
                    showErrorDialog(true, context.getString(R.string.no_internet));
                } else {
                    showErrorDialog(true, t.getMessage());
                    t.printStackTrace();
                }
            }
        });
    }

    private void showErrorDialog(boolean isShow, String errorMessage) {
        if (isShow && !errorDialog.isShowing()) {
            errorDialog.show();
            errorDialog.setErrorText(errorMessage);
        } else
            errorDialog.dismiss();
    }

    private void showLoadingDialog(boolean isShow, String loadingMessage) {
        if (isShow && !loadingDialog.isShowing()) {
            loadingDialog.show();
            loadingDialog.setMessage(loadingMessage);
        } else {
            loadingDialog.dismiss();
        }
    }

    public void setErrorMessage(String message) {
        errorMessage = message;
    }

    public void setLoadingMessage(String message) {
        loadingMessage = message;
    }
}