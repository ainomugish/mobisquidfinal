package com.mobisquid.mobicash.payment.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.payment.model.AllProxyCardData;
import com.mobisquid.mobicash.payment.model.AllProxyCashData;
import com.mobisquid.mobicash.payment.model.ProxyCard;
import com.mobisquid.mobicash.payment.model.ProxyCash;
import com.mobisquid.mobicash.payment.utils.PrefrenceData;
import com.mobisquid.mobicash.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddCardDetailsActivity extends AppCompatActivity {
    private EditText etCardNumber, etExpiryDate, etPin;
    private TextView tvImei;
    private Button btnSave;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.add_proxy_card_details_view);
        context = this;
        initView();
    }

    private void initView() {
        etCardNumber = (EditText) findViewById(R.id.etCardNumber);
        etExpiryDate = (EditText) findViewById(R.id.etExpiryDate);
        etPin = (EditText) findViewById(R.id.etPin);
        tvImei = (TextView) findViewById(R.id.tvImei);
        tvImei.setText(Utils.getDeviceImei(this));
        btnSave = (Button) findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(btnClickListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean isValidate() {
        if (etCardNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Card No", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etExpiryDate.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Expiry Date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etPin.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter pin", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isValidate()) {
                ProxyCard proxyCash = new ProxyCard();
                proxyCash.setCardNumber(etCardNumber.getText().toString());
                proxyCash.setExpiryDate(etExpiryDate.getText().toString());
                proxyCash.setPin(etPin.getText().toString());
                AllProxyCardData allProxyCashData = PrefrenceData.getProxyCardData(context);
                if (allProxyCashData != null) {
                    List<ProxyCard> list = allProxyCashData.getProxyCardList();
                    if (list != null ) {
                        list.add(proxyCash);
                        allProxyCashData.setProxyCashList(list);
                        PrefrenceData.saveProxyCard(allProxyCashData, context);
                    }

                } else {
                    AllProxyCardData allProxyCashData1 = new AllProxyCardData();
                    List<ProxyCard> list = new ArrayList<>();
                    list.add(proxyCash);
                    allProxyCashData1.setProxyCashList(list);
                    PrefrenceData.saveProxyCard(allProxyCashData1, context);

                }
                finish();
               /* AllProxyCashData allProxyCashData = PrefrenceData.getProxyCashdData(context);
                List<ProxyCash> list = PrefrenceData.getProxyCashdData()*/
            }

        }
    };
}
