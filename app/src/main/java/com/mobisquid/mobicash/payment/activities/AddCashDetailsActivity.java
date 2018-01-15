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
import com.mobisquid.mobicash.payment.model.AllProxyCashData;
import com.mobisquid.mobicash.payment.model.ProxyCash;
import com.mobisquid.mobicash.payment.utils.PrefrenceData;
import com.mobisquid.mobicash.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class AddCashDetailsActivity extends AppCompatActivity {
    private EditText etMobileNumber, etnationalId;
    private TextView tvImei;
    private Button btnSave;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.add_proxy_cash_details_view);
        context = this;
        initView();
    }

    private void initView() {
        etMobileNumber = (EditText) findViewById(R.id.etMobileNumber);
        etnationalId = (EditText) findViewById(R.id.etnationalId);
        tvImei = (TextView) findViewById(R.id.tvImei);
        tvImei.setText(Utils.getDeviceImei(this));
        btnSave = (Button) findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(btnClickListener);

    }

    private View.OnClickListener btnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isValidate()) {
                ProxyCash proxyCash = new ProxyCash();
                proxyCash.setMobNo(etMobileNumber.getText().toString());
                proxyCash.setNationalId(etnationalId.getText().toString());
                AllProxyCashData allProxyCashData = PrefrenceData.getProxyCashdData(context);
                if (allProxyCashData != null) {
                    List<ProxyCash> list = allProxyCashData.getProxyCashList();
                    if (list != null) {
                        list.add(proxyCash);
                        allProxyCashData.setProxyCashList(list);
                        PrefrenceData.saveProxyCash(allProxyCashData, context);
                    }

                } else {
                    AllProxyCashData allProxyCashData1 = new AllProxyCashData();
                    List<ProxyCash> list = new ArrayList<>();
                    list.add(proxyCash);
                    allProxyCashData1.setProxyCashList(list);
                    PrefrenceData.saveProxyCash(allProxyCashData1, context);

                }
                finish();
               /* AllProxyCashData allProxyCashData = PrefrenceData.getProxyCashdData(context);
                List<ProxyCash> list = PrefrenceData.getProxyCashdData()*/
            }

        }
    };

    private boolean isValidate() {
        if (etMobileNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Mobile No", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etnationalId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please NationalIx", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }

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
}
