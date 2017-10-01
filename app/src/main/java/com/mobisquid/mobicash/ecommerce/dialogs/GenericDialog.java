package com.mobisquid.mobicash.ecommerce.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobisquid.mobicash.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Zeera on 6/28/2017 bt ${File}
 */

public class GenericDialog extends Dialog {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_message)
    TextView mTvDesriptions;
    @BindView(R.id.ll_multiple)
    LinearLayout mllMultipleButtons;
    @BindView(R.id.btn_single)
    Button mBtnSingle;
    @BindView(R.id.btn_left)
    Button mBtnLeft;
    @BindView(R.id.btn_right)
    Button mBtnRight;

    private IDialogDouble iDialogDouble;
    private IDialogSingle iDialogSingle;

    String title,message,left,right,actionBtn;


    public GenericDialog(@NonNull Context context, @Nullable String title,
                         String message, String actionBtn, IDialogSingle iDialogSingle) {
        super(context);
        this.title = title;
        this.message=message;
        this.actionBtn=actionBtn;
        this.iDialogSingle = iDialogSingle;
    }

    public GenericDialog(@NonNull Context context, @Nullable String title,
                         String message, String left, String right, IDialogDouble iDialogDouble) {
        super(context);
        this.title = title;
        this.message=message;
        this.left=left;
        this.right = right;
        this.iDialogDouble = iDialogDouble;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_generic);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ButterKnife.bind(this,getWindow().getDecorView());

        this.setCancelable(true);
        this.setCanceledOnTouchOutside(true);

        mBtnLeft.setText(left);
        mBtnSingle.setText(actionBtn);
        mBtnRight.setText(right);

        mTvDesriptions.setText(message);
        if(title==null){
            mTvTitle.setVisibility(View.GONE);
        }else{
            mTvTitle.setText(title);
        }

        if (iDialogDouble != null) {
            mllMultipleButtons.setVisibility(View.VISIBLE);
            mBtnSingle.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.btn_single)
    void singleBtnClick(){
        if(iDialogSingle!=null)
            iDialogSingle.onClick(this);
        dismiss();
    }

    @OnClick(R.id.btn_right)
    void btnRight(){
        if(iDialogDouble!=null)
            iDialogDouble.onRightClick(this);
    }

    @OnClick(R.id.btn_left)
    void btnLeft(){
        if(iDialogDouble!=null)
            iDialogDouble.onLeftClick(this);
    }
}
