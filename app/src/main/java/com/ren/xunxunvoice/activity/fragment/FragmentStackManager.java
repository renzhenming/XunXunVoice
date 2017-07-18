package com.ren.xunxunvoice.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.Toast;

import com.ren.xunxunvoice.R;

public class FragmentStackManager {

    private FragmentManager mFragmentManager;
    private Activity mActivity;
    private int mContainerId;
    private long mLastBackTime;
    private onBootCallBackListener listener;

    //if true when click back will clear fragment back stack but home(first page)
    //else will pop top fragment if back once
    private static boolean MODE_IMMEDIATE_HOME = false;
    private static String mMainFragment;

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
        MODE_IMMEDIATE_HOME = false;
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
     * indicate the first showing fragment,it like a launch page for the application
     * @param clazz
     */
    public void setMainFragment(Class<?> clazz){
        if (clazz == null)
            return;
        mMainFragment = clazz.getSimpleName();
    }

    /**
     * used in bottom tab fragment ,when switch tab,you need to hide the other
     * fragment for the reach of showing this fragment normally
     *
     * @param clazz
     * @param arguments
     */
    public void addHorizontalFragment(Class<?> clazz, Bundle arguments) {

        //when switch from addInner to addHorizontal,the first thing to do is clear back stack but first fragment
        if (MODE_IMMEDIATE_HOME == false){
            int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
            if (backStackEntryCount > 1) {
                while (mFragmentManager.getBackStackEntryCount() > 1) {
                    mFragmentManager.popBackStackImmediate();
                }
            }
        }
        MODE_IMMEDIATE_HOME = true;
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
            if (MODE_IMMEDIATE_HOME){
                int backStackEntryCount = mFragmentManager.getBackStackEntryCount();
                if (backStackEntryCount > 1) {
                    /*FragmentManager.BackStackEntry backStackEntryAt = mFragmentManager.getBackStackEntryAt(0);
                    String name = backStackEntryAt.getName();
                    if (!TextUtils.isEmpty(name) && TextUtils.equals(name,mMainFragment)){
                        while (mFragmentManager.getBackStackEntryCount() > 0) {
                            mFragmentManager.popBackStackImmediate();
                        }
                    }else{*/
                        while (mFragmentManager.getBackStackEntryCount() > 1) {
                            mFragmentManager.popBackStackImmediate();
                        }
                   /* }*/
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
