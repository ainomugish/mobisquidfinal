package com.mobisquid.mobicash.ecommerce.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.adapters.CategoryAdapter;
import com.mobisquid.mobicash.ecommerce.essentials.SessionClass;
import com.mobisquid.mobicash.ecommerce.models.Category;
import com.mobisquid.mobicash.ecommerce.network.APIsEndPoints;
import com.mobisquid.mobicash.ecommerce.network.ApiClient;
import com.mobisquid.mobicash.ecommerce.network.CallApi;
import com.mobisquid.mobicash.ecommerce.network.IResponse;
import com.paginate.Paginate;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCategories extends BaseFragment implements ITaggingECommerce, IResponse {
    private static final String ARG_PARENT_ID = "parentId";
    private static final String ARG_PARENT_NAME = "name";

    private Integer mParentId;
    private CategoryAdapter mCategoryAdapter;
    private String name;

    RecyclerView mRvCategories;

    private static final int NO_OF_ROWS = 3;
    private int mLastPageId;
    private boolean isApiCalling;
    private boolean isLoadedAll;
    private Paginate.Callbacks callbacks;

    public SubCategories() {
        // Required empty public constructor
    }

    public static SubCategories newInstance(int parentId, String name) {
        Bundle args = new Bundle();
        SubCategories fragment = new SubCategories();
        args.putInt(ARG_PARENT_ID, parentId);
        args.putString(ARG_PARENT_NAME, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParentId = getArguments().getInt(ARG_PARENT_ID);
        name = getArguments().getString(ARG_PARENT_NAME);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRvCategories = (RecyclerView) view.findViewById(R.id.rv_categories);


        callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                getCategories();
            }

            @Override
            public boolean isLoading() {
                return isApiCalling;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return isLoadedAll;
            }
        };
        isLoadedAll = false;
        mLastPageId = 0;
        isApiCalling = false;
        getCategories();
        setAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sub_categories, container, false);
    }

    public void getCategories() {
        if (isApiCalling)
            return;
        isApiCalling = true;
        Call<ResponseBody> mCategories = ApiClient.getApiClient().
                getCategoriesWRTParent(SessionClass.APPLICATION_ID,
                        mParentId, NO_OF_ROWS, mLastPageId);
        CallApi callApi = new CallApi(getContext(), this);
        if (mCategoryAdapter != null && mCategoryAdapter.getItemCount() == 0)
            callApi.setLoadingMessage(getString(R.string.message_fetching_data));
        callApi.callService(mCategories, APIsEndPoints.GET_SUB_CATEGORIES);
    }

    private void setAdapter(ArrayList<Category> data) {
        if (data == null) {
            mCategoryAdapter = new CategoryAdapter(new ArrayList<Category>(), getActivity());
            mRvCategories.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            mRvCategories.setAdapter(mCategoryAdapter);
            Paginate.with(mRvCategories, callbacks)
                    .setLoadingTriggerThreshold(1)
                    .addLoadingListItem(true)
                    .build();
        } else
            mCategoryAdapter.addData(data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void onSuccess(String body, String endPoint) {
        isApiCalling = false;
        switch (endPoint) {
            case APIsEndPoints.GET_SUB_CATEGORIES:
                try {
                    JSONArray array = new JSONArray(body);
                    ArrayList<Category> categories = new ArrayList<>();
                    Gson gson = new GsonBuilder().create();
                    for (int i = 0; i < array.length(); i++) {
                        Category category = gson.fromJson(array.getJSONObject(i).toString(), Category.class);
                        categories.add(category);
                    }
                    isLoadedAll = categories.size() < NO_OF_ROWS;
                    if (categories.size() - 1 >= 0)
                        mLastPageId = categories.get(categories.size() - 1).getId();
                    setAdapter(categories);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onError(String error, String endPoint) {
        isApiCalling = false;
    }

    @Override
    public void onFailed(Throwable e, String endPoint) {
        isApiCalling = false;
    }
}
