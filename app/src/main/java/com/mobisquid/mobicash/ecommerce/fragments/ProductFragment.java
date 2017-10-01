package com.mobisquid.mobicash.ecommerce.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.adapters.ProductsAdapter;
import com.mobisquid.mobicash.ecommerce.essentials.SessionClass;
import com.mobisquid.mobicash.ecommerce.models.ProductModel;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductFragment extends BaseFragment implements ITaggingECommerce, IResponse {
    private static final String ARG_CATEGORY_ID = "categoryId";
    private static final String ARG_NAME = "name";
    private Integer mCategoryId;
    private Unbinder mUnbinder;
    private boolean isCallApi;

    @BindView(R.id.rv_products)
    RecyclerView mRvProducts;


    private ProductsAdapter mProductsAdapter;
    private String name;

    private static final int NO_OF_ROWS = 3;
    private int mLastPageId;
    private boolean isLoadedAll;
    private Paginate.Callbacks callbacks;

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(int categoryId, String name) {

        Bundle args = new Bundle();

        ProductFragment fragment = new ProductFragment();
        args.putInt(ARG_CATEGORY_ID, categoryId);
        args.putString(ARG_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryId = getArguments().getInt(ARG_CATEGORY_ID);
        name = getArguments().getString(ARG_NAME);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);


        callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                getProducts();
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
        isLoadedAll = false;
        mLastPageId = 0;
        isCallApi = false;
        getProducts();
        setAdapter(null);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    public void getProducts() {
        if (isCallApi)
            return;
        isCallApi = true;
        Call<ResponseBody> mCategories = ApiClient.getApiClient().
                getProductByCategory(SessionClass.APPLICATION_ID
                , mCategoryId, NO_OF_ROWS, mLastPageId);
        CallApi callApi = new CallApi(getContext(), this);
        if(mProductsAdapter!=null&&mProductsAdapter.getItemCount()==0)
            callApi.setLoadingMessage("Fetching ");
        callApi.callService(mCategories, APIsEndPoints.GET_PRODUCTS);
    }

    private void setAdapter(ArrayList<ProductModel> data) {
        if (data == null) {
            LogUtility.debugLog("adapter Set");
            mProductsAdapter = new ProductsAdapter(new ArrayList<ProductModel>(), getActivity());
            mRvProducts.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mRvProducts.setAdapter(mProductsAdapter);
            Paginate.with(mRvProducts, callbacks)
                    .setLoadingTriggerThreshold(1)
                    .addLoadingListItem(true)
                    .build();
        } else
            mProductsAdapter.addData(data);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void onSuccess(String body, String endPoint) {
        isCallApi = false;
        switch (endPoint) {
            case APIsEndPoints.GET_PRODUCTS:
                try {
                    JSONArray array = new JSONArray(body);
                    ArrayList<ProductModel> products = new ArrayList<>();
                    Gson gson = new GsonBuilder().create();
                    for (int i = 0; i < array.length(); i++) {
                        ProductModel product = gson.fromJson(array.getJSONObject(i).
                                toString(), ProductModel.class);
                        products.add(product);
                    }
                    isLoadedAll = products.size() < NO_OF_ROWS;
                    if (products.size() - 1 >= 0)
                        mLastPageId = products.get(products.size() - 1).getId();
                    setAdapter(products);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @Override
    public void onError(String error, String endPoint) {
        isCallApi = false;
    }

    @Override
    public void onFailed(Throwable e, String endPoint) {
        isCallApi = false;
    }
}
