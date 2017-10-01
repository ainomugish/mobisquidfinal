package com.mobisquid.mobicash.fragments;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.PaymentDetails;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Contents;
import com.mobisquid.mobicash.utils.QRCodeEncoder;
import com.mobisquid.mobicash.utils.Vars;

import static android.content.Context.WINDOW_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Myqrcode extends Fragment {
    View rootview;
    Vars vars;
    Alerter alerter;



    public Myqrcode() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vars = new Vars(getActivity());
        alerter= new Alerter(getActivity());
        setHasOptionsMenu(true);
        ActionBar actionBar =((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        rootview = inflater.inflate(R.layout.fragment_myqrcode, container, false);
        WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        Point point = new Point();

        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int smallerDimension = width < height ? width : height;
        smallerDimension = smallerDimension * 3/4;

        QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(vars.qrcode,
                null,
                Contents.Type.TEXT, BarcodeFormat.QR_CODE.toString(),
                smallerDimension);
        try {
            Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
            ImageView myImage = (ImageView) rootview.findViewById(R.id.imageView1);
            myImage.setImageBitmap(bitmap);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        rootview.findViewById(R.id.chkrequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent paidment = new Intent(getActivity(), PaymentDetails.class);
                paidment.putExtra("unpaid","unpaid");
                getActivity().startActivity(paidment);
            }
        });
        return rootview;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        if(vars.active==null) {
            alerter.login("Financial services", "You are currently logged out, please login or register");
        }else {

        }
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Present Qrcode");
        super.onResume();
    }
}
