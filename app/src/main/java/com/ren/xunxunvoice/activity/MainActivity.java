package com.ren.xunxunvoice.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.ren.xunxunvoice.R;
import com.ren.xunxunvoice.activity.fragment.FragmentStackManager;
import com.ren.xunxunvoice.activity.fragment.MeFragment;
import com.ren.xunxunvoice.activity.fragment.VoiceFragment;
import com.ren.xunxunvoice.activity.fragment.VoiceFragment2;
import com.ren.xunxunvoice.activity.fragment.VoiceFragment3;
import com.ren.xunxunvoice.activity.utils.ToastUtils;
import com.ren.xunxunvoice.activity.view.SwitchItemView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.main_voice)
    SwitchItemView mainVoice;
    @InjectView(R.id.main_me)
    SwitchItemView mainMe;
    @InjectView(R.id.main_drawer)
    DrawerLayout mainDrawer;

    @InjectView(R.id.main_voice2)
    SwitchItemView mainVoice2;
    @InjectView(R.id.main_voice3)
    SwitchItemView mainVoice3;
    @InjectView(R.id.framelayout)
    FrameLayout framelayout;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ActionBarDrawerToggle toggle;
    private FragmentStackManager fragmentCacheManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
        initParams();
    }

    private void initParams() {
        fragmentCacheManager = new FragmentStackManager();
        fragmentCacheManager.setUp(this, R.id.framelayout);
        fragmentCacheManager.addHorizontalFragment(VoiceFragment.class,null);
        fragmentCacheManager.setMainFragment(VoiceFragment.class);
    }

    private void initView() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        toggle = new ActionBarDrawerToggle(this, mainDrawer, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                ToastUtils.showToast(getApplicationContext(), "open");
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                ToastUtils.showToast(getApplicationContext(), "close");
            }
        };
        mainDrawer.addDrawerListener(toggle);
    }


    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //动画效果执行
        toggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return toggle.onOptionsItemSelected(item);
    }

    @OnClick({R.id.main_voice, R.id.main_voice2, R.id.main_voice3, R.id.main_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_voice:
                fragmentCacheManager.addHorizontalFragment(VoiceFragment.class,null);
                break;
            case R.id.main_voice2:
                fragmentCacheManager.addHorizontalFragment(VoiceFragment2.class,null);
                break;
            case R.id.main_voice3:
                fragmentCacheManager.addHorizontalFragment(VoiceFragment3.class,null);
                break;
            case R.id.main_me:
                fragmentCacheManager.addHorizontalFragment(MeFragment.class,null);
                break;
            }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0){
            fragmentCacheManager.onBackPress();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
