package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.model.TransactionObj;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Vars;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsandCondiftions extends Fragment {
    Vars vars;
    String url;
    View rootview;


    public TermsandCondiftions() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.termsandconditions_frg, container, false);
        TextView foo = (TextView)rootview.findViewById(R.id.read);
        foo.setText(Html.fromHtml(getString(R.string.nice_html)));

        return rootview;
    }

    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getResources().getString(R.string.termcondition));

        super.onResume();
    }
}
