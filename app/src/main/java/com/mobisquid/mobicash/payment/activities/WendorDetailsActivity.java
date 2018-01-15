package com.mobisquid.mobicash.payment.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.payment.qrcodereader.DataModel;

public class WendorDetailsActivity extends AppCompatActivity {
    private TextView tvTransactionId, tvTransactionDate, tvAmount, tvMerchantId;
    private Context context;
    public static String KEY_WENDOR = "wendorData";
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        setContentView(R.layout.wendor_details_activity);
        initView();
    }

    private void initView() {
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvTransactionId = (TextView) findViewById(R.id.tvTransactionId);
        tvTransactionDate = (TextView) findViewById(R.id.tvTransactionDate);
        tvMerchantId = (TextView) findViewById(R.id.tvMerchantId);
        btnPay = (Button) findViewById(R.id.btnPay);
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, PaymentActivity.class));
            }
        });
        if (getIntent().hasExtra(KEY_WENDOR)) {
            DataModel dataModel = (DataModel) getIntent().getSerializableExtra(KEY_WENDOR);
            String transcationId = dataModel.getTransactionId();
            String transcationDate = dataModel.getTransactionDate();
            String amount = dataModel.getAmount();
            String merchantId = dataModel.getMerchantId();

            if (dataModel != null) {
                tvTransactionId.setText(transcationId != null ? transcationId : "");
                tvTransactionDate.setText(transcationDate != null ? transcationDate : "");
                tvAmount.setText(amount != null ? amount : "");
                tvMerchantId.setText(merchantId != null ? merchantId : "");
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
