package com.mobisquid.mobicash.ecommerce.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.network.ApiClient;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageSwiperView extends Fragment {

    private ImageView imageView;
    private int imageId;
    private String imagePath;
    private ProgressBar progressBar;

    public ImageSwiperView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image_swiper_view, container, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (getArguments().containsKey("image"))
            imageId = getArguments().getInt("image");
        else
            imagePath = getArguments().getString("imagePath");
        initiateViews(view);
    }

    private void initiateViews(View view) {
        imageView = (ImageView) view.findViewById(R.id.fullimage);
        progressBar = (ProgressBar) view.findViewById(R.id.imgprogress);
        if(imagePath!=null)
            Picasso.with(getContext()).load(ApiClient.BASE_URL_ECOMMERCE+imagePath).fit().into(imageView);
        else
            Picasso.with(getContext()).load(imageId).fit().into(imageView);
    }


}
