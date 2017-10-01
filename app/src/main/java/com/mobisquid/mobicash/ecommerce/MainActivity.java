package com.mobisquid.mobicash.ecommerce;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.bussiness.Basket;
import com.mobisquid.mobicash.ecommerce.essentials.SharedPrefManager;
import com.mobisquid.mobicash.ecommerce.fragments.BaseFragment;
import com.mobisquid.mobicash.ecommerce.fragments.BasketFragment;
import com.mobisquid.mobicash.ecommerce.fragments.ECommerceSearch;
import com.mobisquid.mobicash.ecommerce.fragments.Ecommerce;
import com.mobisquid.mobicash.ecommerce.models.enums.FragmentAnimationType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.iv_cart)
    ImageView mIvCart;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.tv_cart_count)
    TextView mTvCartCount;
    @BindView(R.id.basket)
    ImageView basket;
    @BindView(R.id.fl_cart)
    FrameLayout mFlCart;
    @BindView(R.id.llback)
    LinearLayout mLlBack;
    @BindView(R.id.ib_main_back)
    ImageButton mIbBack;
    @BindView(R.id.tv_back_furtherName)
    TextView mTvBackFurtherName;
    @BindView(R.id.floating_search)
    FloatingActionButton mFabSearch;
    @BindView(R.id.tv_actionBar_title)
    TextView mTvActionBarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecom);
        ButterKnife.bind(this);
        Basket.getBasket().setList(new SharedPrefManager(this).getProducts(Basket.KEY_BASKET));
        setBackStactListener();
        updateCount();
        replaceFragment(new Ecommerce(), FragmentAnimationType.NO_ANIMATION,false);
    }

    public void translateCart(int startX, int startY) {
        int deltaX = rlMain.getWidth() - 100;
        int deltaY = rlMain.getHeight() - 100;
        int endPos[] = new int[2];
        mTvCartCount.getLocationOnScreen(endPos);
        int deviceHeigth = getWindow().getDecorView().getHeight();
        startY = deviceHeigth - startY - 100;
        endPos[1] = deviceHeigth - endPos[1];
        TranslateAnimation anim = new TranslateAnimation(
                TranslateAnimation.ABSOLUTE, startX,
                TranslateAnimation.ABSOLUTE, endPos[0],
                TranslateAnimation.ABSOLUTE, -startY,
                TranslateAnimation.ABSOLUTE, -deltaY
        );
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIvCart.bringToFront();
                mIvCart.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                updateCount();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setFillAfter(false);
        anim.setDuration(1000);
        mIvCart.startAnimation(anim);
    }

    @OnClick(R.id.basket)
    public void openBasket() {
        if (Basket.getBasket().getList().size() > 0) {
            if (getCurrentFragment(R.id.fl_fragmentContainer) != null && !(getCurrentFragment(R.id.fl_fragmentContainer) instanceof BasketFragment))
                replaceFragment(new BasketFragment(),FragmentAnimationType.GROW_FROM_BOTTOM, true);
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.pleaseadd), Toast.LENGTH_SHORT).show();
        }
    }

    public void updateCount() {
        new SharedPrefManager(this).saveProducts(Basket.KEY_BASKET, Basket.getBasket().getList());
        int count = Basket.getBasket().getTotalCount();
        mIvCart.setVisibility(View.GONE);

        mTvCartCount.setText(count + "");
        mTvCartCount.setVisibility(count != 0 ? View.VISIBLE : View.GONE);
        Animation pulse = AnimationUtils.loadAnimation(MainActivity.this, R.anim.pulse);
        mTvCartCount.startAnimation(pulse);
    }

    public void replaceFragment(BaseFragment fragment, FragmentAnimationType type
                                , boolean isToAddBackStack){
        super.replaceFragment(fragment,type,getSupportFragmentManager(),R.id.fl_fragmentContainer,isToAddBackStack);
    }

    @OnClick(R.id.floating_search)
    public void toSearchEommerce(){
        replaceFragment(new ECommerceSearch(),FragmentAnimationType.GROW_FROM_BOTTOM,true);
    }

    private void setBackStactListener(){
        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if(getSupportFragmentManager().getBackStackEntryCount()>0){
                    mLlBack.setVisibility(View.VISIBLE);
                    mTvActionBarTitle.setVisibility(View.GONE);
                }else {
                    mLlBack.setVisibility(View.GONE);
                    mTvActionBarTitle.setVisibility(View.VISIBLE);
                }
                BaseFragment currentFragment = getCurrentFragment(R.id.fl_fragmentContainer);
                if(currentFragment!=null){
                    if(currentFragment instanceof BasketFragment)
                        mFabSearch.setVisibility(View.GONE);
                    else
                        mFabSearch.setVisibility(View.VISIBLE);
                    mTvBackFurtherName.setText(currentFragment.getName());
                }
            }
        });
    }

    @OnClick(R.id.llback)
    public void popFragment() {
        getSupportFragmentManager().popBackStack();
    }
}
