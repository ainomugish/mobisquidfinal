package com.mobisquid.mobicash.payment.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;

import com.mobisquid.mobicash.R;

public class ChoosePaymentOptionsActivity extends AppCompatActivity {
    private Context context;
    AppCompatButton btnProxyCash, btnProxyCard, btnProxyBank, btnProxyFone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        setContentView(R.layout.choose_payments_activity);
        btnProxyCash = (AppCompatButton) findViewById(R.id.btn_proxy_cash);
        btnProxyCash.setOnClickListener(proxyCashListener);

        btnProxyCard = (AppCompatButton) findViewById(R.id.btn_proxy_card);
        btnProxyCard.setOnClickListener(proxyCardListener);

        btnProxyBank = (AppCompatButton) findViewById(R.id.btn_proxy_bank);
        btnProxyBank.setOnClickListener(proxyBankListener);

        btnProxyFone = (AppCompatButton) findViewById(R.id.btn_proxy_fone);
        btnProxyFone.setOnClickListener(proxyFoneListener);
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

    private View.OnClickListener proxyCashListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(PaymentActivity.this, ProxyCashDetailsActivity.class);
            //intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            // startActivityForResult(intent, REQUEST_CODE_ENABLE);
            //startActivity(new Intent(PaymentActivity.this, ProxyCashDetailsActivity.class));
        }
    };

    private View.OnClickListener proxyBankListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

    private View.OnClickListener proxyCardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
    private View.OnClickListener proxyFoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };

}
