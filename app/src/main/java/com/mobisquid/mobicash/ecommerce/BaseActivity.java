package com.mobisquid.mobicash.ecommerce;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.mobisquid.mobicash.R;
import com.mobisquid.mobicash.ecommerce.fragments.BaseFragment;
import com.mobisquid.mobicash.ecommerce.models.enums.FragmentAnimationType;
import com.mobisquid.mobicash.ecommerce.utitlities.ViewUtility;


/**
 * Created by Zeera on 7/18/2017 bt ${File}
 */

public class BaseActivity extends AppCompatActivity {

    public void replaceFragment(BaseFragment fragment, FragmentAnimationType type,
                                FragmentManager mFragmentManager,
                                int mContainerID, boolean isToAddBackStack) {
        if (mFragmentManager == null) {
            return;
        }
        ViewUtility.hideKeyboard(this,null);
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        setAnimationFragment(type,fragmentTransaction);
        fragmentTransaction.replace(mContainerID, fragment, "KK");
        if (isToAddBackStack)
            fragmentTransaction.addToBackStack(null);

        try {
            fragmentTransaction.commitAllowingStateLoss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAnimationFragment(FragmentAnimationType type, FragmentTransaction ft){
        switch (type){
            case NO_ANIMATION:
                break;
            case SLIDE_FROM_LEFT:
                ft.setCustomAnimations(R.anim.slide_in_from_left,R.anim.slide_out_to_right,
                        R.anim.slide_in_from_right,R.anim.slide_out_to_left);
                break;
            case SLIDE_FROM_RIGHT:
                ft.setCustomAnimations(R.anim.slide_in_from_right,R.anim.slide_out_to_left,
                        R.anim.slide_in_from_left,R.anim.slide_out_to_right);
                break;
            case FADE_IN:
                ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out,
                        android.R.anim.fade_in,android.R.anim.fade_out);
                break;
            case GROW_FROM_BOTTOM:
                ft.setCustomAnimations(R.anim.from_bottom,android.R.anim.fade_out,
                        android.R.anim.fade_in,R.anim.to_bottom);
                break;
        }
    }


    public BaseFragment getCurrentFragment(int containerID) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(containerID);
        if (fragment instanceof BaseFragment)
            return ((BaseFragment) fragment);
        return null;
    }
}
