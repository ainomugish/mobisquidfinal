package com.mobisquid.mobicash.ecommerce.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.Window;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.models.enums.PaymentMethod;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeera on 7/16/2017 bt ${File}
 */

public class PaymentDialog extends Dialog {
    private IDialogItemSelected mListener;

    public PaymentDialog(@NonNull Context context) {
        super(context);
    }

    public PaymentDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public PaymentDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setmListener(IDialogItemSelected mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment);
        if (getWindow() != null) {
            getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            ButterKnife.bind(this, getWindow().getDecorView());
        }
    }
    @OnClick(R.id.iv_mobi)
    public void mobiSelected(){
        if(mListener!=null){
            mListener.itemSelected(PaymentMethod.MOBICASH);
        }
        this.dismiss();
    }

    @OnClick(R.id.iv_paypal)
    public void paypalSelected(){
        if(mListener!=null){
            mListener.itemSelected(PaymentMethod.PAYPAL);
        }
        Toast.makeText(getContext(),"Payment method not available",Toast.LENGTH_LONG).show();
    }
    @OnClick(R.id.iv_skyrill)
    public void skyrillSelected(){
        if(mListener!=null){
            mListener.itemSelected(PaymentMethod.SKYRILL);
        }
        Toast.makeText(getContext(),"Payment method not available",Toast.LENGTH_LONG).show();
    }
    @OnClick(R.id.iv_credit_card)
    public void creditCardSelected(){
        if(mListener!=null){
            mListener.itemSelected(PaymentMethod.CREDIT_CARD);
        }
        Toast.makeText(getContext(),"Payment method not available",Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.btn_close)
    public void  closeDialog(){
        this.dismiss();
    }

    public interface IDialogItemSelected{
        void  itemSelected(PaymentMethod method);
    }
}
