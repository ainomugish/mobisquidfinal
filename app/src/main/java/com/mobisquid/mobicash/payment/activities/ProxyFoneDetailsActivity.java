package com.mobisquid.mobicash.payment.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.github.omadahealth.lollipin.lib.PinCompatActivity;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.payment.adapters.ProxyBankAdapter;
import com.mobisquid.mobicash.payment.adapters.ProxyFoneAdapter;
import com.mobisquid.mobicash.payment.model.AllProxyBankData;
import com.mobisquid.mobicash.payment.model.AllProxyFoneData;
import com.mobisquid.mobicash.payment.model.ProxyBank;
import com.mobisquid.mobicash.payment.model.ProxyFone;
import com.mobisquid.mobicash.payment.utils.PrefrenceData;

import java.util.ArrayList;
import java.util.List;

public class ProxyFoneDetailsActivity extends PinCompatActivity implements ProxyFoneAdapter.OnItemClickListener {
    ProxyFoneAdapter rcAdapter;
    FloatingActionButton btnAdd;
    private AllProxyFoneData data;
    List<ProxyFone> list;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        setContentView(R.layout.payemt_proxy_cash_details_activity);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProxyFoneDetailsActivity.this, AddProxyFoneActivity.class));
            }
        });
        RecyclerView rView = (RecyclerView) findViewById(R.id.rvPayment);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rView.addItemDecoration(dividerItemDecoration);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(this));
        data = PrefrenceData.getProxyFoneData(this);
        if (data != null) {
            list = data.getProxyFoneList();
            if (list == null) {
                list = new ArrayList<>();
            }
            rcAdapter = new ProxyFoneAdapter(this, list, this);
            rView.setAdapter(rcAdapter);
        } else {
            data = new AllProxyFoneData();
            list = new ArrayList<>();
            data.setProxyFoneList(list);
            rcAdapter = new ProxyFoneAdapter(this, list, this);
            rView.setAdapter(rcAdapter);
        }


    }

    private void setAddButtonVisibility() {
        if (list != null) {
            if (list.size() >= 3) {
                btnAdd.setVisibility(View.GONE);
            } else {
                btnAdd.setVisibility(View.VISIBLE);
            }
        } else {
            btnAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (rcAdapter != null) {
            if (PrefrenceData.getProxyFoneData(this) != null) {
                list = PrefrenceData.getProxyFoneData(this).getProxyFoneList();
                rcAdapter.refreshList(list);
            }
            setAddButtonVisibility();
        }
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


    private void showCustomDialog(String message, String title, final ProxyFone proxyCash) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (proxyCash != null) {
                            // List<ProxyFone> list = data.getProxyFoneList();
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getMobileNo().equals(proxyCash.getMobileNo())) {
                                    list.remove(j);
                                }
                            }

                            data.setProxyFoneList(list);
                            PrefrenceData.saveProxyFone(data, context);
                            rcAdapter.refreshList(list);
                            setAddButtonVisibility();
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    @Override
    public void onItemClick(View view, ProxyFone viewModel) {
        showCustomDialog("Do you want to delete", "Alert", viewModel);
    }
}
