package com.mobisquid.mobicash.ecommerce.fragments;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
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
import com.mobisquid.mobicash.ecommerce.utitlities.ViewUtility;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class ECommerceSearch extends BaseFragment implements IResponse {


    private Unbinder mUnBinder;
    @BindView(R.id.et_search_products)
    EditText mEtSearch;
    @BindView(R.id.rv_products)
    RecyclerView mRvProducts;
    @BindView(R.id.tv_no_item)
    TextView mTvNoItem;

    private static final int NO_OF_ROWS = 10;

    private Integer mLastProductId = null;
    private boolean isCallingApi;
    private ProductsAdapter mProductsAdapter;
    private Paginate.Callbacks callbacks;
    boolean isLoadedAll;


    public ECommerceSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ecommerce_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchProducts(mEtSearch.getText().toString());
                    }
                },500);

            }

            @Override
            public boolean isLoading() {
                return isCallingApi;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return isLoadedAll;
            }
        };
        setAdapter(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    @OnEditorAction(R.id.et_search_products)
    public boolean searchForProduct(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            if (mProductsAdapter != null) {
                isLoadedAll = false;
                mProductsAdapter.clearData();
                mLastProductId = null;
            }
            searchProducts(mEtSearch.getText().toString());
            mEtSearch.clearFocus();
            ViewUtility.hideKeyboard(getActivity(),null);
            return true;
        }

        return false;
    }

    public void searchProducts(String searchTerm) {
        isCallingApi = true;
        mRvProducts.setVisibility(View.VISIBLE);
        if (searchTerm.trim().isEmpty()) {
            return;
        }
        Call<ResponseBody> responseBodyCall = ApiClient.getApiClient().searchProducts(searchTerm, NO_OF_ROWS,
                SessionClass.APPLICATION_ID, mLastProductId);
        LogUtility.debugLog(responseBodyCall.request().url().toString());
        CallApi callApi = new CallApi(getContext(), this);
        callApi.callService(responseBodyCall, APIsEndPoints.SEARCH_PRODUCT);
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
        setNoData(mProductsAdapter.getItemCount());
    }

    public void setNoData(int count) {
        mTvNoItem.setVisibility(count == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSuccess(String body, String endPoint) {
        isCallingApi = false;
        switch (endPoint) {
            case APIsEndPoints.SEARCH_PRODUCT:
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
                        mLastProductId = products.get(products.size() - 1).getId();
                    setAdapter(products);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(String error, String endPoint) {
        isCallingApi = false;
    }

    @Override
    public void onFailed(Throwable e, String endPoint) {
        isCallingApi = false;
    }
}
