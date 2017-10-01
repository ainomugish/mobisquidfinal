package com.mobisquid.mobicash.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.AuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsAuthButton;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.activities.FinactionalRegistration;
import com.mobisquid.mobicash.activities.FirstActivity;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.Vars;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

public class New_verification_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_LOCATION = "location";
    private static final String ARG_FINANCIAL = "financials";
    private static final String CHANGEPASS = "changepass";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String location, financials, changepass;
    View rootview;
    Vars vars;
    private static final String TWITTER_KEY = "hre2cXh2BdRWYO1HpgG24IwMK ";
    private static final String TWITTER_SECRET = "CgTKo3DRTGfmyBB3Iak7oDe1pJTQzc1yrddh2v8Ob11n8ESHrF";

    public New_verification_frag() {
        // Required empty public constructor
    }

    public static New_verification_frag newInstance(String param1, String param2) {
        New_verification_frag fragment = new New_verification_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vars = new Vars(getActivity());
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            location = getArguments().getString(ARG_LOCATION);
            financials = getArguments().getString(ARG_FINANCIAL);
            changepass = getArguments().getString(CHANGEPASS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.newsms, container, false);
        vars.log("number with code====" + "+" + mParam1 + mParam2);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Digits.Builder digitsBuilder = new Digits.Builder();
        Fabric.with(getActivity(), new TwitterCore(authConfig), digitsBuilder.build());
        AuthConfig.Builder authConfigBuilder = new AuthConfig.Builder()
                .withAuthCallBack(new AuthCallback() {
                    @Override
                    public void success(DigitsSession session, String phoneNumber) {
                        vars.log("mmmmmm=====" + phoneNumber);
                        if(financials!=null){
                            String mynumber=null;
                            Intent notice = new Intent(getActivity(), FinactionalRegistration.class);
                            if(phoneNumber.substring(0,1).equalsIgnoreCase("+")){
                                mynumber = phoneNumber.substring(1);
                            }else{
                                mynumber = phoneNumber;
                            }
                            notice.putExtra("number", mynumber);
                            notice.putExtra("location", location);
                            notice.putExtra("code", mParam1);
                            notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(notice);
                            getActivity().finish();

                        }else {

                            Intent notice = new Intent(getActivity(), FirstActivity.class);
                            notice.putExtra("number", phoneNumber);
                            notice.putExtra("location", location);
                            notice.putExtra("code", mParam1);
                            notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(notice);
                            getActivity().finish();
                        }


                       /* Intent login = new Intent(getActivity(), FirstActivity.class);
                        login.putExtra("number", phoneNumber);
                        login.putExtra("location", location);
                        login.putExtra("code", mParam1);
                        startActivity(login);
                        getActivity().finish();*/
                    }

                    @Override
                    public void failure(DigitsException error) {
                        Alerter.Error(getActivity(), "Authetication Error", "Device already verified or already have an account");
                    }
                })
                .withPhoneNumber("+" + mParam1 + mParam2);

        Digits.authenticate(authConfigBuilder.build());
        Digits.Builder digitsBuilder2 = new Digits.Builder();
        Fabric.with(getActivity(), new TwitterCore(authConfig), digitsBuilder2.build());

        DigitsAuthButton digitsButton = (DigitsAuthButton) rootview.findViewById(R.id.auth_button);
        digitsButton.setCallback(new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {
                vars.log("mmmmmm=====" + phoneNumber);
                if(financials!=null){
                    String mynumber=null;
                    Intent notice = new Intent(getActivity(), FinactionalRegistration.class);
                    if(phoneNumber.substring(0,1).equalsIgnoreCase("+")){
                        mynumber = phoneNumber.substring(1);
                    }else{
                        mynumber = phoneNumber;
                    }
                    notice.putExtra("number", mynumber);
                    notice.putExtra("location", location);
                    notice.putExtra("code", mParam1);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(notice);
                    getActivity().finish();

                }else {

                    Intent notice = new Intent(getActivity(), FirstActivity.class);
                    notice.putExtra("number", phoneNumber);
                    notice.putExtra("location", location);
                    notice.putExtra("code", mParam1);
                    notice.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(notice);
                    getActivity().finish();
                }

            }

            @Override
            public void failure(DigitsException error) {
                Alerter.Error(getActivity(), "Authetication Error", "Device already verified or already have an account");
            }
        });


        return rootview;
    }


}