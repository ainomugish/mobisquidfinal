package com.mobisquid.mobicash.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.adapters.CategoryAdapter;
import com.mobisquid.mobicash.model.Categorymodel;
import com.mobisquid.mobicash.utils.Alerter;
import com.mobisquid.mobicash.utils.ConnectionClass;
import com.mobisquid.mobicash.utils.Globals;
import com.mobisquid.mobicash.utils.Vars;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class EshoppingFragment extends Fragment implements ViewPagerEx.OnPageChangeListener, CategoryAdapter.OnItemClickListener {
    View rootview;
    private SliderLayout mDemoSlider;
    CategoryAdapter rcAdapter;
    Vars vars;
    List<Categorymodel> allItems = new ArrayList<>();

    public EshoppingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vars = new Vars(getActivity());
        rootview = inflater.inflate(R.layout.fragment_eshopping, container, false);
        mDemoSlider = (SliderLayout) getActivity().findViewById(R.id.slider);
        HashMap<String, String> url_maps = new HashMap<String, String>();
        url_maps.put("Business solution", "http://www.mobicashonline.com/app/themes/mobicash/img/business/sliderBusiness.jpg");
        url_maps.put("Pay Taxes", "https://www.mcash.rw/images/banner2.jpg");
        url_maps.put("Pay TV subscriptions", "https://www.mcash.rw/images/banner4.jpg");
        url_maps.put("Top-up Airtime", "https://www.mcash.rw/images/banner1.jpg");
        url_maps.put("Pay electricity", "https://www.mcash.rw/images/banner3.jpg");
        for (String name : url_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra", name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        RecyclerView rView = (RecyclerView) rootview.findViewById(R.id.recycler_view);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        rcAdapter = new CategoryAdapter(getActivity(), allItems);
        rcAdapter.setOnItemClickListener(this);
        rView.setAdapter(rcAdapter);
        return rootview;
    }

    private List<Categorymodel> getAllItemList() {



        Alerter.showdialog(getActivity());

        String url = "http://52.38.75.235:8080/api/Category/GetAllCategoryList?ApplicationId=2";
        try{
            JSONObject json = new JSONObject();
            json.put("ApplicationId","2");
            vars.log("JSON==="+json.toString());
            ConnectionClass.JsonString(Request.Method.GET, getActivity(), url, json, "CAT", new ConnectionClass.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Alerter.stopdialog();
                    try {
                        // JSONObject jObject = new JSONObject(result);

                        JSONArray latestArray = new JSONArray(result);
                        if (latestArray instanceof JSONArray) {
                            if(!allItems.isEmpty()){
                                allItems.clear();
                            }
                            for (int i = 0; i < latestArray.length(); i++) {
                                Categorymodel latest = new Gson().fromJson(latestArray.getString(i),Categorymodel.class);
                                allItems.add(latest);
                            }

                        }

                        rcAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        return allItems;
    }

    @Override
    public void onResume() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        Globals.whichfag = "eshop";
        toolbar.setTitle("Categories");
        int color = ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark);
        toolbar.setTitleTextColor(color);
        if(allItems.isEmpty()){
            allItems = getAllItemList();
        }

        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(View view, Categorymodel viewModel) {

    }
}
