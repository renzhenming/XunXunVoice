package com.ren.xunxunvoice.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.ren.xunxunvoice.R;
import com.ren.xunxunvoice.activity.adapter.MainAdapter;
import com.ren.xunxunvoice.activity.fragment.MeFragment;
import com.ren.xunxunvoice.activity.fragment.VoiceFragment;
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
    @InjectView(R.id.main_viewpager)
    ViewPager mainViewpager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        VoiceFragment voiceFragment = new VoiceFragment();
        MeFragment meFragment = new MeFragment();
        fragments.add(voiceFragment);
        fragments.add(meFragment);

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager(), fragments);
        mainViewpager.setAdapter(adapter);

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

    @OnClick({R.id.main_voice, R.id.main_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_voice:
                break;
            case R.id.main_me:
                break;
        }
    }
}
