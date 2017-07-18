package com.ren.xunxunvoice.activity.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class FragmentCacheManager {

    public FragmentCacheManager() {
        //mCacheFragment = new SparseArray<>();
    }

    private FragmentManager mFragmentManager;

    private Activity mActivity;

    private Fragment mFragment;

    private int mContainerId;

    private long mLastBackTime;

    private onBootCallBackListener listener;
    //缓存的Fragment集合数据
    //private SparseArray<FragmentInfo> mCacheFragment;
    public static boolean DEBUG = false;
    //当前展示的Fragment
    private Fragment mCurrentFragment;
    private int mCurrentFragmentIndex = -1;


    public void setUp(FragmentActivity activity, int containerId) {
        if (mFragment != null) {
            throw new RuntimeException("you have setup for Fragment");
        }
        this.mActivity = activity;
        this.mContainerId = containerId;
        mFragmentManager = activity.getSupportFragmentManager();
    }

    public void setUp(Fragment fragment, int containerId) {
        if (mActivity != null) {
            throw new RuntimeException("you have setup for Activity");
        }
        this.mFragment = fragment;
        this.mContainerId = containerId;
        mFragmentManager = fragment.getChildFragmentManager();
        //Fragment所在的Activity
        mActivity = fragment.getActivity();
    }

    /*public void setCurrentFragment(int index) {
        if (index < 0) {
            return;
        }

        if (index == mCurrentFragmentIndex) {
            return;
        }
        FragmentInfo info = mCacheFragment.get(index);
        mCurrentFragmentIndex = index;
        goToThisFragment(info);
    }*/


    /***********************************************************************/

    public void addFragment(Class<?> clazz,Bundle arguments) {
        if (clazz == null)
            return;
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int count = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            FragmentManager.BackStackEntry stackEntryAt = mFragmentManager.getBackStackEntryAt(i);
            String tagName = stackEntryAt.getName();
            Fragment fragmentByTag = mFragmentManager.findFragmentByTag(tagName);
            if (fragmentByTag != null && fragmentByTag.isVisible()){
                transaction.hide(fragmentByTag);
            }
        }


        String tag = clazz.getSimpleName();
        Fragment fragment = null;
        try {
            fragment = mFragmentManager.findFragmentByTag(tag);
            if (fragment == null) {
                //创建对象将数据传递给Fragment对象
                fragment = (Fragment) clazz.newInstance();
                if (arguments != null) {
                    fragment.setArguments(arguments);
                }
                transaction.add(mContainerId, fragment, tag).addToBackStack(tag);
            }else{
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


    /***********************************************************************/


    /**
     * 添加Fragment到管理栈里，同一个实力对象只会创建一次
     * 功能实现原理FragmentTabhost相同,注意hide和detach区别
     */
    /*public void addFragmentToCache(int index, Class<?> clss) {
        FragmentInfo info = createInfo(clss.getName(), clss, null);
        mCacheFragment.put(index, info);
    }

    public void addFragmentToCache(int index, Class<?> clss, String tag) {
        FragmentInfo info = createInfo(tag, clss, null);
        mCacheFragment.put(index, info);
    }*/

    /**
     * 要实现同一个对象多次创建必须通过不同的Tag来做唯一标示
     *
     * @param index Fragment对应的索引，通过索引找到对应的显示Fragment
     * @param clss  需要创建的Fragment.class 文件
     * @param tag   Fragment的唯一标示
     * @param args  Bundle 会传递给生产的Fragment对象，*/
    /*public void addFragmentToCache(int index, Class<?> clss, String tag, Bundle args) {
        FragmentInfo info = createInfo(tag, clss, args);
        mCacheFragment.put(index, info);
    }

    public void addFragmentToCache(int index, Class<?> clss, Bundle args) {
        FragmentInfo info = createInfo(clss.getName(), clss, args);
        mCacheFragment.put(index, info);
    }
*/
    public void setListener(onBootCallBackListener listener) {
        this.listener = listener;
    }

    /*static final class FragmentInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        Fragment fragment;

        FragmentInfo(String _tag, Class<?> _class, Bundle _args) {
            tag = _tag;
            clss = _class;
            args = _args;
        }
    }*/

    /*private FragmentInfo createInfo(String tag, Class<?> clss, Bundle args) {
        return new FragmentInfo(tag, clss, args);
    }
*/
    /*private void goToThisFragment(FragmentInfo param) {
        int containerId = mContainerId;
        Class<?> cls = param.clss;
        if (cls == null) {
            return;
        }
        try {
            if (DEBUG) {
                //LogUtil.d("before operate, stack entry count: " + mFragmentManager.getBackStackEntryCount());
            }
            String fragmentTag = param.tag;
            //通过Tag查找活动的Fragment，相同到Fragment可以创建多个实力对象通过设置不同到Tag
            Fragment fragment = mFragmentManager.findFragmentByTag(fragmentTag);
            if (fragment == null) {
                //创建对象将数据传递给Fragment对象
                fragment = (Fragment) cls.newInstance();
                if (param.args != null) {
                    fragment.setArguments(param.args);
                }
                param.fragment = fragment;
                if (DEBUG) {
                    //LogUtil.i("newInstance " + fragmentTag);
                }
            }
            if (mCurrentFragment != null && mCurrentFragment != fragment) {
                //去除跟Activity关联的Fragment
                //detach会将Fragment所占用的View从父容器中移除，但不会完全销毁，还处于活动状态
                mFragmentManager.beginTransaction().detach(mCurrentFragment).commit();
                if (DEBUG) {
                    //LogUtil.d("detach " + mCurrentFragment.getClass().getName());
                }
            }

            FragmentTransaction ft = mFragmentManager.beginTransaction();

            if (fragment.isDetached()) {
                //重新关联到Activity
                ft.attach(fragment);
                if (DEBUG) {
                    //LogUtil.d("attach " + fragmentTag);
                }
            } else {
                if (DEBUG) {
                    //LogUtil.d(fragmentTag + " is added");
                }
                if (!fragment.isAdded()) {
                    ft.add(containerId, fragment, fragmentTag);
                }
            }
            mCurrentFragment = fragment;

            ft.commitAllowingStateLoss();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

*/
    public void onBackPress() {

        if (mActivity.isTaskRoot()) {
            int cnt = mFragmentManager.getBackStackEntryCount();
            long secondClickBackTime = System.currentTimeMillis();
            if (cnt <= 1 && (secondClickBackTime - mLastBackTime) > 2000) {
                if (listener != null) {
                    listener.onBootCallBack();
                }
                mLastBackTime = secondClickBackTime;
                Toast.makeText(mActivity,"再按一次退出",Toast.LENGTH_SHORT).show();
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
            mFragmentManager.popBackStackImmediate();
        }
    }

    private void showFragment(String tag){
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.show(fragment);
        transaction.commitAllowingStateLoss();
        mFragmentManager.executePendingTransactions();
    }

    private void hideFragment(){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        int count = mFragmentManager.getBackStackEntryCount();
        for (int i = 0; i < count; i++) {
            FragmentManager.BackStackEntry stackEntryAt = mFragmentManager.getBackStackEntryAt(i);
            String tagName = stackEntryAt.getName();
            Fragment fragmentByTag = mFragmentManager.findFragmentByTag(tagName);
            if (fragmentByTag != null && fragmentByTag.isVisible()){
                transaction.hide(fragmentByTag).commitAllowingStateLoss();
                mFragmentManager.executePendingTransactions();
            }
        }
    }

    public interface onBootCallBackListener {
        //返回键事件触发
        void onBootCallBack();
    }
}
/**
 *
 * Fragment 中View的复用问题
 通过上个类实现了Fragment这个类的实例化对象的复用管理，Fragment 的OnCreate方法只会走一遍，但是Fragment的OnCreateView方法 会在每次可见的时候调用，Fragment首次实例化的时候将OnCreateView 方法实例化的View对象存在全局变量中，在Framgent被切到当前的时候使用全局的View对象。

 private View mView;//缓存在Fragment的View对象,需要注意对象重复使用的时候数据的更新
 public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
 if（mView == null）{
 mView = inflater.inflate(layoutId,null);
 }
 return mView;
 }

 FragmentCacheManager 使用方法：

 private FragmentCacheManager fragmentCacheManager;

 //MainActivity底部4个Fragment
 protected void initParams() {
 fragmentCacheManager = new FragmentCacheManager();
 fragmentCacheManager.setUp(this, R.id.fl_content);
 fragmentCacheManager.addFragmentToCache(1, HomeFragment.class);
 fragmentCacheManager.addFragmentToCache(2, ClassifyFragment.class);
 fragmentCacheManager.addFragmentToCache(3, ShoppingFragment.class);
 fragmentCacheManager.addFragmentToCache(4, MyFragment.class);
 fragmentCacheManager.setListener(new FragmentCacheManager.onBootCallBackListener() {
@Override
public void onBootCallBack() {
checkItem(1);
}
});
 }

 //分类导航栏数据
 private void createManager(List<ClassifyItemModel> list) {

 for (int i = 0; i < list.size(); i++) {
 ClassifyItemModel mode = list.get(i);
 Bundle bundle = new Bundle();//相同的Fragment需要传递不同的Tag来标示哦
 bundle.putSerializable("model", mode);
 mFragmentCacheManager.addFragmentToCache(i, ClassifyItemDetailsFragment.class, mode.getId(), bundle);
 }
 mFragmentCacheManager.setCurrentFragment(0);
 }

 */