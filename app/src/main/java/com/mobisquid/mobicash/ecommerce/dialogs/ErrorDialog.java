package com.mobisquid.mobicash.ecommerce.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.mobisquid.mobicash.R;


public class ErrorDialog extends Dialog implements View.OnClickListener {

    private TextView errorText;

    public ErrorDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_error);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        errorText = (TextView) findViewById(R.id.error_text);
        findViewById(R.id.no).setOnClickListener(this);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }

    public void setErrorText(String text) {
        errorText.setText(text);
    }


    @Override
    public void onClick(View v) {
        dismiss();
    }
}
