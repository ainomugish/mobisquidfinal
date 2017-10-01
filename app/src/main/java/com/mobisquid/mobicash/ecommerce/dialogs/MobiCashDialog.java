package com.mobisquid.mobicash.ecommerce.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.widget.AppCompatEditText;
import android.view.Window;
import android.widget.Toast;

import com.mobisquid.mobicash.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeera on 7/16/2017 bt ${File}
 */

public class MobiCashDialog extends Dialog {

    @BindView(R.id.et_phoneNumber)
    AppCompatEditText mPhoneNumber;
    @BindView(R.id.et_pin)
    AppCompatEditText mPin;

    private IMobiCashPay mListener;

    public MobiCashDialog(@NonNull Context context) {
        super(context);
    }

    public MobiCashDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public MobiCashDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_mobicash);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ButterKnife.bind(this, getWindow().getDecorView());
        }
    }

    @OnClick(R.id.btn_close)
    public void closeDialog(){
        this.dismiss();
    }

    @OnClick(R.id.btn_ok)
    public void onOk(){

        if(!mPhoneNumber.getText().toString().trim().isEmpty()&& !mPin.getText().toString().trim().isEmpty()){
            if (mListener != null) {
                mListener.pay(mPhoneNumber.getText().toString(),mPin.getText().toString());
            }
            //dismiss();
        }else{
            String error = "Please fill all field before proceeding";
            Toast.makeText(getContext(),error,Toast.LENGTH_LONG).show();
        }


    }

    public void setmListener(IMobiCashPay mListener) {
        this.mListener = mListener;
    }

    public interface IMobiCashPay{
        void pay(String phone, String pin);
    }
}
