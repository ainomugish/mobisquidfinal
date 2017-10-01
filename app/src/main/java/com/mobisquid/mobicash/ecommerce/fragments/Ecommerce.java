package com.mobisquid.mobicash.ecommerce.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.adapters.CategoryAdapter;
import com.mobisquid.mobicash.ecommerce.adapters.ImagePagerAdapter;
import com.mobisquid.mobicash.ecommerce.essentials.SessionClass;
import com.mobisquid.mobicash.ecommerce.models.Category;
import com.mobisquid.mobicash.ecommerce.network.APIsEndPoints;
import com.mobisquid.mobicash.ecommerce.network.ApiClient;
import com.mobisquid.mobicash.ecommerce.network.CallApi;
import com.mobisquid.mobicash.ecommerce.network.IResponse;
import com.mobisquid.mobicash.ecommerce.utitlities.LogUtility;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Ecommerce extends BaseFragment implements ITaggingECommerce,IResponse {

    private static final int[] GPS_IMAGES = {R.drawable.slider1,
            R.drawable.slider2,R.drawable.slider3};

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CategoryAdapter adapter;
    private ViewPager viewPager;
    private ImagePagerAdapter pagerAdapter;
    private Handler handler;
    private Runnable runnable;
    private int pageNumber;
    private int maxPage=3;
    private long delay=2000;

    private static final int NO_OF_ROWS = 10;
    private int mLastPageId;
    private boolean isLoadedAll;
    private Paginate.Callbacks callbacks;
    private boolean isCallApi;


    public Ecommerce() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ecommerce, container, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);

        isLoadedAll = false;
        mLastPageId = 0;
        isCallApi = false;
        callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCategories();
                    }
                },500);
            }

            @Override
            public boolean isLoading() {
                return isCallApi;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return isLoadedAll;
            }
        };
        initializeViews(view);
        setAdapter(null);
    }

    private void startSlider(){
        pageNumber=0;
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
              viewPager.setCurrentItem(pageNumber);
              pageNumber++;

             if(pageNumber==maxPage){
                 pageNumber=0;
             }
             handler.postDelayed(runnable,delay);
            }
        };
        handler.postDelayed(runnable,delay);
    }

    private void initializeViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.categories);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        getCategories();
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        pagerAdapter=new ImagePagerAdapter(getChildFragmentManager(),GPS_IMAGES);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        startSlider();
    }

    private void setAdapter(ArrayList<Category> data){
        if(data==null){
            adapter = new CategoryAdapter(new ArrayList<Category>(), getActivity());
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
            Paginate.with(recyclerView, callbacks)
                    .setLoadingTriggerThreshold(1)
                    .addLoadingListItem(true)
                    .build();
        }else
            adapter.addData(data);

    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public String getName() {
        return getString(R.string.ecommerce);
    }

    @Override
    public void onSuccess(String body, String endPoint) {

        switch (endPoint){
            case APIsEndPoints.GET_PARENT_CATEGORIES:
                try {
                    JSONArray array = new JSONArray(body);
                    ArrayList<Category> categories = new ArrayList<>();
                    Gson gson = new GsonBuilder().create();
                    for (int i = 0; i < array.length(); i++) {
                        Category category = gson.fromJson(array.getJSONObject(i).toString(),Category.class);
                        categories.add(category);
                    }
                    isLoadedAll = categories.size()<NO_OF_ROWS;
                    if(categories.size()-1>=0)
                        mLastPageId  = categories.get(categories.size()-1).getId();
                    setAdapter(categories);


                } catch (JSONException e) {
                    e.printStackTrace();

                }
                break;
        }
        isCallApi = false;


    }

    @Override
    public void onError(String error, String endPoint) {
        isCallApi = false;
    }

    @Override
    public void onFailed(Throwable e, String endPoint) {
        isCallApi = false;
    }

    public void getCategories() {
        if(isCallApi)
            return;
        LogUtility.debugLog("Api Called");
        isCallApi = true;
        Call<ResponseBody> getCategories = ApiClient.getApiClient().getCategories(SessionClass.APPLICATION_ID,
                NO_OF_ROWS,mLastPageId);
        CallApi callApi = new CallApi(getContext(), this);
        if(adapter!=null&&adapter.getItemCount()==0)
            callApi.setLoadingMessage(getString(R.string.message_fetching_data));
        callApi.callService(getCategories, APIsEndPoints.GET_PARENT_CATEGORIES);
    }


}
