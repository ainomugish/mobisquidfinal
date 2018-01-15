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
import com.mobisquid.mobicash.payment.model.AllProxyBankData;
import com.mobisquid.mobicash.payment.model.AllProxyCashData;
import com.mobisquid.mobicash.payment.model.ProxyBank;
import com.mobisquid.mobicash.payment.model.ProxyCash;
import com.mobisquid.mobicash.payment.utils.PrefrenceData;

import java.util.ArrayList;
import java.util.List;

public class AddProxyBankActivity extends AppCompatActivity {
    private EditText etBankName, etBankCode, etBankAcount, etInternationalCode;
    private Button btnSave;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.add_proxy_bank_details_view);
        context = this;
        initView();

    }

    private void initView() {
        btnSave = (Button) findViewById(R.id.saveBtn);
        btnSave.setOnClickListener(btnClickListener);
        etBankName = (EditText) findViewById(R.id.etBankName);
        etBankCode = (EditText) findViewById(R.id.etBankCode);
        etBankAcount = (EditText) findViewById(R.id.etBankAcount);
        etInternationalCode = (EditText) findViewById(R.id.etInternationalCode);
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
                ProxyBank proxyCash = new ProxyBank();
                proxyCash.setBankName(etBankName.getText().toString());
                proxyCash.setBankAccountNumber(etBankAcount.getText().toString());
                proxyCash.setBankCode(etBankCode.getText().toString());
                proxyCash.setInternationalBank(etInternationalCode.getText().toString());
                AllProxyBankData allProxyCashData = PrefrenceData.getProxyBankData(context);
                if (allProxyCashData != null) {
                    List<ProxyBank> list = allProxyCashData.getProxyBankList();
                    if (list != null) {
                        list.add(proxyCash);
                        allProxyCashData.setProxyBankList(list);
                        PrefrenceData.saveProxyBank(allProxyCashData, context);
                    }

                } else {
                    AllProxyBankData allProxyCashData1 = new AllProxyBankData();
                    List<ProxyBank> list = new ArrayList<>();
                    list.add(proxyCash);
                    allProxyCashData1.setProxyBankList(list);
                    PrefrenceData.saveProxyBank(allProxyCashData1, context);
                }
                finish();
               /* AllProxyCashData allProxyCashData = PrefrenceData.getProxyCashdData(context);
                List<ProxyCash> list = PrefrenceData.getProxyCashdData()*/
            }

        }
    };

    private boolean isValidate() {
       /* if (etMobileNumber.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please Enter Mobile No", Toast.LENGTH_SHORT).show();
            return false;
        } else if (etnationalId.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Please NationalIx", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }*/
        return true;
    }
}
