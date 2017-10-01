package com.mobisquid.mobicash.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.utils.Vars;


/**
 * A simple {@link Fragment} subclass.
 */
public class Frequentlyasked extends Fragment {
        View rootview;
        String url;
    ProgressBar progressBar;
    Vars vars;
    private WebView webview;


    public Frequentlyasked() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_frequentlyasked, container, false);
        vars = new Vars(getActivity());
        webview = (WebView) rootview.findViewById(R.id.mobishop);
         url="https://support.mobisquid.com/faq.html";

        progressBar = (ProgressBar) rootview.findViewById(R.id.progressBar);


        //enable jave script
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        //progressBar.setProgress(webview.getProgress());
        //getAssets();

        //webview.loadUrl("file:///android_asset/www/index.htm");
        webview.loadUrl(url);

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                webview.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                progressBar.setVisibility(View.VISIBLE);

            }
        });
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("FAQ");
    }
}
