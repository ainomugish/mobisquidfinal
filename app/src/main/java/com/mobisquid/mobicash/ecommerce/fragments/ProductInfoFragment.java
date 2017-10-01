package com.mobisquid.mobicash.ecommerce.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.MainActivity;
import com.mobisquid.mobicash.ecommerce.adapters.ImagePagerAdapter;
import com.mobisquid.mobicash.ecommerce.bussiness.Basket;
import com.mobisquid.mobicash.ecommerce.models.ProductModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProductInfoFragment extends BaseFragment implements View.OnClickListener, ITaggingECommerce {

    private TextView productId, productDescription;
    private TextView productColor, productCost;
    private TextView productName;
    private ProductModel productModel;
    private TextView info;
    private Button mBtnAddToCart;
    private LinearLayout infoView;
    private ViewPager viewPager;
    private ImagePagerAdapter pagerAdapter;


    public ProductInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_info, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productModel = (ProductModel) getArguments().getSerializable("product");
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);


        productColor = (TextView) view.findViewById(R.id.productcolor);
        productCost = (TextView) view.findViewById(R.id.productprice);
        productDescription = (TextView) view.findViewById(R.id.productdescription);
        productId = (TextView) view.findViewById(R.id.productid);
        productName = (TextView) view.findViewById(R.id.productname);
        info = (TextView) view.findViewById(R.id.info);
        mBtnAddToCart = (Button) view.findViewById(R.id.btn_add_to_cart);
        infoView = (LinearLayout) view.findViewById(R.id.details);
        showHideInfo(0);
        info.setOnClickListener(this);
        mBtnAddToCart.setOnClickListener(this);

        //// TODO: 5/31/2017  need to update it using server image now :P
        //images=getIntent().getIntArrayExtra(Keys.IMAGES);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        pagerAdapter = new ImagePagerAdapter(getChildFragmentManager(), productModel.getImage());

        setInfoViews();

        //getProductInfo();
    }

    private void setInfoViews() {
        viewPager.setAdapter(pagerAdapter);
        productId.setText(String.format(getString(R.string.productid), productModel.getId()));
        productName.setText(productModel.getName());
        productColor.setText(String.format(getString(R.string.color), productModel.getColor()));
        productDescription.setText(String.format(getString(R.string.description), productModel.getDescription()));
        productCost.setText(String.format(getString(R.string.price), productModel.getPrice()));
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.info: {
                showHideInfo(500);
            }
            break;
            case R.id.btn_add_to_cart:
                int[] pos = new int[2];
                if(infoView.getVisibility()==View.VISIBLE)
                    showHideInfo(100);
                mBtnAddToCart.getLocationOnScreen(pos);
                if (getContext() instanceof MainActivity) {
                    int x = mBtnAddToCart.getWidth() / 2 + pos[0];
                    ((MainActivity) getContext()).translateCart(x, pos[1]);
                    Basket.getBasket().addToBasket(productModel);
                }
                break;
        }
    }

    private void showHideInfo(long duration) {
        if (infoView.getVisibility() == View.GONE) {
            info.setText(getString(R.string.infominus));
            infoView.animate()
                    .translationY(0).alpha(1.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            super.onAnimationStart(animation);
                            infoView.setVisibility(View.VISIBLE);
                            infoView.setAlpha(1.0f);
                        }
                    });
        } else {
            info.setText(getString(R.string.infoadd));
            infoView.animate()
                    .translationY(-infoView.getHeight()).alpha(0.0f)
                    .setDuration(duration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            infoView.setVisibility(View.GONE);
                        }
                    });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setRevealEffect() {
        infoView.setVisibility(infoView.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        // get the center for the clipping circle
        int cx = (infoView.getLeft() + infoView.getRight()) / 2;
        int cy = (infoView.getTop() + infoView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, infoView.getWidth() - cx);
        int dy = Math.max(cy, infoView.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        // Android native animator
        Animator animator =
                ViewAnimationUtils.createCircularReveal(infoView, cx, cy, 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1500);
        animator.start();
    }

    @Override
    public String getName() {
        return productModel.getName();
    }


}

