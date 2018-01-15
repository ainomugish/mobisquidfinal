package com.mobisquid.mobicash.payment.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.github.omadahealth.lollipin.lib.PinActivity;
import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.payment.model.ProxyCard;
import com.mobisquid.mobicash.payment.model.ProxyCash;
import com.mobisquid.mobicash.payment.utils.KeyStoreHelper;
import com.mobisquid.mobicash.payment.utils.PrefrenceData;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class PaymentActivity extends PinActivity {
    AppCompatButton btnProxyCash, btnProxyCard, btnProxyBank, btnProxyFone;
    private static final int REQUEST_CODE_ENABLE = 11;
    private static final int REQUEST_CODE_CASH = 111;
    private static final int REQUEST_CODE_CARD = 112;
    private static final int REQUEST_CODE_BANK = 113;
    private static final int REQUEST_CODE_FONE = 114;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_method_view);
        if (!KeyStoreHelper.isSigningKey(PrefrenceData.KEY_ALIAS)) {
            try {
                KeyStoreHelper.createKeys(this, PrefrenceData.KEY_ALIAS);
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            }
        }
        btnProxyCash = (AppCompatButton) findViewById(R.id.btn_proxy_cash);
        btnProxyCash.setOnClickListener(proxyCashListener);

        btnProxyCard = (AppCompatButton) findViewById(R.id.btn_proxy_card);
        btnProxyCard.setOnClickListener(proxyCardListener);

        btnProxyBank = (AppCompatButton) findViewById(R.id.btn_proxy_bank);
        btnProxyBank.setOnClickListener(proxyBankListener);

        btnProxyFone = (AppCompatButton) findViewById(R.id.btn_proxy_fone);
        btnProxyFone.setOnClickListener(proxyFoneListener);
        if (!PrefrenceData.isPinEnable(this)) {
            Intent intent = new Intent(PaymentActivity.this, CustomPinActivity.class);
            intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            startActivityForResult(intent, REQUEST_CODE_ENABLE);
        }
    }

    private View.OnClickListener proxyCashListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(PaymentActivity.this, ProxyCashDetailsActivity.class);
            //intent.putExtra(AppLock.EXTRA_TYPE, AppLock.ENABLE_PINLOCK);
            // startActivityForResult(intent, REQUEST_CODE_ENABLE);
            checkPin(REQUEST_CODE_CASH);
            //startActivity(new Intent(PaymentActivity.this, ProxyCashDetailsActivity.class));
        }
    };

    private View.OnClickListener proxyBankListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPin(REQUEST_CODE_BANK);
        }
    };

    private View.OnClickListener proxyCardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPin(REQUEST_CODE_CARD);
        }
    };
    private View.OnClickListener proxyFoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPin(REQUEST_CODE_FONE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_ENABLE:
                //Toast.makeText(this, "PinCode enabled", Toast.LENGTH_SHORT).show();
                PrefrenceData.setProxyPin(this, true);
                break;
            case REQUEST_CODE_CASH:
                startActivity(new Intent(PaymentActivity.this, ProxyCashDetailsActivity.class));
                break;
            case REQUEST_CODE_CARD:
                startActivity(new Intent(PaymentActivity.this, ProxyCardDetailsActivity.class));
                break;
            case REQUEST_CODE_FONE:
                startActivity(new Intent(PaymentActivity.this, ProxyFoneDetailsActivity.class));
                break;
            case REQUEST_CODE_BANK:
                startActivity(new Intent(PaymentActivity.this, ProxyBankDetailsActivity.class));
                break;
        }
    }

    private void checkPin(int code) {
        Intent intent = new Intent(PaymentActivity.this, CustomPinActivity.class);
        intent.putExtra(AppLock.EXTRA_TYPE, AppLock.UNLOCK_PIN);
        startActivityForResult(intent, code);
    }
}
