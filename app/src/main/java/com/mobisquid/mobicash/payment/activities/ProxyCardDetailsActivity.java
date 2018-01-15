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
import com.mobisquid.mobicash.payment.adapters.ProxyCardAdapter;
import com.mobisquid.mobicash.payment.adapters.ProxyCashAdapter;
import com.mobisquid.mobicash.payment.model.AllProxyCardData;
import com.mobisquid.mobicash.payment.model.AllProxyCashData;
import com.mobisquid.mobicash.payment.model.ProxyCard;
import com.mobisquid.mobicash.payment.model.ProxyCash;
import com.mobisquid.mobicash.payment.utils.PrefrenceData;

import java.util.ArrayList;
import java.util.List;

public class ProxyCardDetailsActivity extends PinCompatActivity implements ProxyCardAdapter.OnItemClickListener {
    ProxyCardAdapter rcAdapter;
    FloatingActionButton btnAdd;
    private AllProxyCardData data;
    List<ProxyCard> list;
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
                startActivity(new Intent(ProxyCardDetailsActivity.this, AddCardDetailsActivity.class));
            }
        });
        RecyclerView rView = (RecyclerView) findViewById(R.id.rvPayment);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        rView.addItemDecoration(dividerItemDecoration);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new LinearLayoutManager(this));
        data = PrefrenceData.getProxyCardData(this);
        if (data != null) {
            list = data.getProxyCardList();
            if (list == null) {
                list = new ArrayList<>();
            }
            rcAdapter = new ProxyCardAdapter(this, list, this);
            rView.setAdapter(rcAdapter);
        } else {
            data = new AllProxyCardData();
            list = new ArrayList<>();
            data.setProxyCashList(list);
            rcAdapter = new ProxyCardAdapter(this, list, this);
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
            if (PrefrenceData.getProxyCardData(this) != null && PrefrenceData.getProxyCardData(this).getProxyCardList() != null) {
                list = PrefrenceData.getProxyCardData(this).getProxyCardList();
                rcAdapter.refreshList(list);
                setAddButtonVisibility();
            }
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


    private void showCustomDialog(String message, String title, final ProxyCard proxyCash) {
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
                            //List<ProxyCard> list = data.getProxyCardList();
                            for (int j = 0; j < list.size(); j++) {
                                if (list.get(j).getCardNumber().equals(proxyCash.getCardNumber())) {
                                    list.remove(j);
                                }
                            }

                            data.setProxyCashList(list);
                            PrefrenceData.saveProxyCard(data, context);
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
    public void onItemClick(View view, ProxyCard viewModel) {
        showCustomDialog("Do you want to delete", "Alert", viewModel);
    }
}
