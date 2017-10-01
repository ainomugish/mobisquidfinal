package com.mobisquid.mobicash.ecommerce.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.TextView;

import com.mobisquid.mobicash.R;


public class LoadingDialog extends Dialog {


    private TextView textView;


    public String getMessage() {
        return textView.getText().toString();
    }

    public void setMessage(String message) {
        textView.setText(message);
    }

    public LoadingDialog(@NonNull Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_progress);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        textView = (TextView) findViewById(R.id.loading_text);
        this.setCancelable(false);
        this.setCanceledOnTouchOutside(false);
    }
}
