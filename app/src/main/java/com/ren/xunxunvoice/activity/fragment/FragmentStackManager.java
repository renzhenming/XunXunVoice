package com.ren.xunxunvoice.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.ren.xunxunvoice.R;

public class FragmentStackManager {

    private FragmentManager mFragmentManager;
    private Activity mActivity;
    private int mContainerId;
    private long mLastBackTime;
    private onBootCallBackListener listener;
    private static boolean BOTTOME_TAB_HOME = false;
    private static final String HOME_FRAGMENT = "VoiceFragment";

    /**
     * We divide the current patterns into two categories:check bottom and check inner
     * check bottom means we add fragment to fragment transition horizontally,often used in bottom tabs
     * check inner means we add fragment to fragment transition layering by layering
     * for this two mode ,we have different method to resolve,so make clear that you know
     * which mode you are in now.
     * if true when click back will clear fragment back stack but home(first page)
     * else will pop top fragment if back once
     */
    private static boolean MODE_IS_CHECK_BOTTOM = false;

    public void setUp(FragmentActivity activity, int containerId) {
        this.mActivity = activity;
        this.mContainerId = containerId;
        mFragmentManager = activity.getSupportFragmentManager();
    }

    /**
     * used in showing a fragment in an activity or in a fragment,you don't have to hide the other fragment
     * when you are going to show another fragment,cause when you want to back you will find the last fragment is right here
     *
     * @param clazz
     * @param arguments
     */
    public void addInnerFragment(Class<?> clazz, Bundle arguments) {
        //when switch from addHorizontal to addInner ,the first thing to do is clear back stack without first fragment too like addHorizontal method
        if (MODE_IS_CHECK_BOTTOM == true){
            int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 1) {
                while (mFragmentManager.getBackStackEntryCount() > 1) {
                    mFragmentManager.popBackStackImmediate();
                }
            }
        }
        MODE_IS_CHECK_BOTTOM = false;
        if (clazz == null)
            return;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //set replace and pop back stack animation
        transaction.setCustomAnimations(R.anim.right_in, R.anim.right_out, R.anim.right_in, R.anim.right_out);
        String tag = clazz.getSimpleName();
        Fragment fragment;
        try {
            fragment = mFragmentManager.findFragmentByTag(tag);

            if (fragment == null) {
                fragment = (Fragment) clazz.newInstance();
                if (arguments != null) {
                    fragment.setArguments(arguments);
                }
                transaction.add(mContainerId, fragment, tag).addToBackStack(tag);
            } else {
                transaction.show(fragment);
            }
            transaction.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * used in bottom tab fragment ,when switch tab,you need to hide the other
     * fragment for the reach of showing this fragment normally
     *
     * @param clazz
     * @param arguments
     */
    public void addHorizontalFragment(Class<?> clazz, Bundle arguments) {

        //when switch from addInner to addHorizontal,the first thing to do is clear back stack without first fragment
        if (MODE_IS_CHECK_BOTTOM == false){
            int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 1) {
                while (mFragmentManager.getBackStackEntryCount() > 1) {
                    mFragmentManager.popBackStackImmediate();
                }
            }
        }
        MODE_IS_CHECK_BOTTOM = true;
        if (clazz == null)
            return;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int count = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            FragmentManager.BackStackEntry stackEntryAt = mFragmentManager.getBackStackEntryAt(i);
            String tagName = stackEntryAt.getName();
            Fragment fragmentByTag = mFragmentManager.findFragmentByTag(tagName);
            if (fragmentByTag != null && fragmentByTag.isVisible()) {
                transaction.hide(fragmentByTag);
            }
        }

        //check if this is the home fragment, if so, when click back,we need to clear the fragment back stack
        // then tell the user that is going to out
        String tag = clazz.getSimpleName();
        if (tag.equals(HOME_FRAGMENT)){
            BOTTOME_TAB_HOME = true;
        }else{
            BOTTOME_TAB_HOME = false;
        }
        Fragment fragment;

        try {
            fragment = mFragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                fragment = (Fragment) clazz.newInstance();
                if (arguments != null) {
                    fragment.setArguments(arguments);
                }
                transaction.add(mContainerId, fragment, tag).addToBackStack(tag);
            } else {
                transaction.show(fragment);
            }
            transaction.commitAllowingStateLoss();
            mFragmentManager.executePendingTransactions();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setListener(onBootCallBackListener listener) {
        this.listener = listener;
    }

    /**
     * use this method in the activity which fragment is attached,override its onBackPressed method
     * then the manager can help you to manage your back stack
     */
    public void onBackPress() {
        if (mActivity.isTaskRoot()) {
            int cnt = mFragmentManager.getBackStackEntryCount();
            long secondClickBackTime = System.currentTimeMillis();
            if (cnt <= 1 && (secondClickBackTime - mLastBackTime) > 2000) {
                if (listener != null) {
                    //when a fragment is shift out from stack ,maybe you need to do something like refresh your ui
                    listener.onBootCallBack();
                }
                mLastBackTime = secondClickBackTime;
                Toast.makeText(mActivity, "再按一次退出", Toast.LENGTH_SHORT).show();
            } else {
                doReturnBack();
            }
        } else {
            doReturnBack();
        }
    }

    private void doReturnBack() {
        int count = mFragmentManager.getBackStackEntryCount();
        if (count <= 1) {
            mActivity.finish();
        } else {
            if (MODE_IS_CHECK_BOTTOM){
                int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
                if (backStackEntryCount > 1) {
                    if (BOTTOME_TAB_HOME){
                        while (mFragmentManager.getBackStackEntryCount() > 1) {
                            mFragmentManager.popBackStackImmediate();
                        }
                        onBackPress();
                    }else{
                        while (mFragmentManager.getBackStackEntryCount() > 1) {
                            mFragmentManager.popBackStackImmediate();
                        }
                    }
                }
            }else{
                mFragmentManager.popBackStackImmediate();
            }
        }
    }

    public interface onBootCallBackListener {
        void onBootCallBack();
    }
}
