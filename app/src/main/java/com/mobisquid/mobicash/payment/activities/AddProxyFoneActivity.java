package com.mobisquid.mobicash.payment.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.payment.model.AllProxyCardData;
import com.mobisquid.mobicash.payment.model.AllProxyFoneData;
import com.mobisquid.mobicash.payment.model.ProxyCard;
import com.mobisquid.mobicash.payment.model.ProxyFone;
import com.mobisquid.mobicash.payment.utils.PrefrenceData;
import com.mobisquid.mobicash.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddProxyFoneActivity extends AppCompatActivity {
    private EditText etAcountBalance, etMobileNumber;
    private TextView tvImei;
    private Button btnSave;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_proxyfone_details_view);
        context = this;
        initView();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initView() {
        etAcountBalance = (EditText) findViewById(R.id.etAcountBalance);
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
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

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isValidate()) {
                ProxyFone proxyCash = new ProxyFone();
                proxyCash.setMobileNo(etMobileNumber.getText().toString());
                proxyCash.setBalance(etAcountBalance.getText().toString());
                AllProxyFoneData allProxyCashData = PrefrenceData.getProxyFoneData(context);
                if (allProxyCashData != null) {
                    List<ProxyFone> list = allProxyCashData.getProxyFoneList();
                    if (list != null ) {
                        list.add(proxyCash);
                        allProxyCashData.setProxyFoneList(list);
                        PrefrenceData.saveProxyFone(allProxyCashData, context);
                    }

                } else {
                    AllProxyFoneData allProxyCashData1 = new AllProxyFoneData();
                    List<ProxyFone> list = new ArrayList<>();
                    list.add(proxyCash);
                    allProxyCashData1.setProxyFoneList(list);
                    PrefrenceData.saveProxyFone(allProxyCashData1, context);

                }
                finish();
               /* AllProxyCashData allProxyCashData = PrefrenceData.getProxyCashdData(context);
                List<ProxyCash> list = PrefrenceData.getProxyCashdData()*/
            }

        }
    };

    private boolean isValidate() {
      /*  if (etCardNumber.getText().toString().trim().isEmpty()) {
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
        }*/
        return true;

    }
}
