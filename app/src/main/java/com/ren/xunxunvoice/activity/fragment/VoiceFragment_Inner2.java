package com.ren.xunxunvoice.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.ren.xunxunvoice.R;
import com.ren.xunxunvoice.activity.SpeakActivity;
import com.ren.xunxunvoice.activity.adapter.VoiceAdapter;
import com.ren.xunxunvoice.activity.bean.VoiceBean;
import com.ren.xunxunvoice.activity.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VoiceFragment_Inner2 extends Fragment implements View.OnClickListener, VoiceAdapter.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<VoiceBean> mVoiceList = new ArrayList<>();

    /**服务器端一共多少条数据*/
    private static final int TOTAL_COUNTER = 10000;//如果服务器没有返回总数据或者总页数，这里设置为最大值比如10000，什么时候没有数据了根据接口返回判断
    /**每一页展示多少条数据*/
    private static final int REQUEST_COUNT = 10;
    /**已经获取到多少条数据了*/
    private static int mCurrentCounter = 0;

    @InjectView(R.id.voice_file_list)
    LRecyclerView voiceFileList;
    @InjectView(R.id.voice_float_button)
    FloatingActionButton voiceFloatButton;

    private String mParam1;
    private String mParam2;
    private Context mContext;
    private int pageSize = 10;



    public static VoiceFragment_Inner2 newInstance(String param1, String param2) {
        VoiceFragment_Inner2 fragment = new VoiceFragment_Inner2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voice_inner_2, container, false);
        ButterKnife.inject(this, view);
        initView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    private void initView() {
        for (int i = 0; i <10; i++) {
            VoiceBean bean = new VoiceBean();
            bean.content = "VoiceFragment_Inner_Two"+i;
            mVoiceList.add(bean);
        }
        final VoiceAdapter voiceAdapter = new VoiceAdapter(mContext,mVoiceList);
        voiceAdapter.setOnItemClickListener(this);
        LRecyclerViewAdapter adapter = new LRecyclerViewAdapter(voiceAdapter);
        DividerDecoration divider = new DividerDecoration.Builder(mContext)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.color_eeeeee)
                .build();

        voiceFileList.addItemDecoration(divider);
        voiceFileList.setAdapter(adapter);
        voiceFileList.setLayoutManager(new LinearLayoutManager(mContext));
        voiceFileList.setOnRefreshListener(refreshListener);
        voiceFileList.setOnLoadMoreListener(loadMoreListener);
        voiceFileList.setLScrollListener(lScrollListener);
        //设置头部加载颜色
        voiceFileList.setHeaderViewColor(R.color.colorAccent, R.color.colorPrimary ,android.R.color.white);
        //设置底部加载颜色
        voiceFileList.setFooterViewColor(R.color.colorAccent, R.color.colorPrimary ,android.R.color.white);
        //设置底部加载文字提示
        voiceFileList.setFooterViewHint("拼命加载中","已经全部为你呈现了","网络不给力啊，点击再试一次吧");
        //voiceFileList.refresh();

        voiceFloatButton.setOnClickListener(this);
    }

    private OnLoadMoreListener loadMoreListener = new OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (mVoiceList.size() == pageSize) {
                ToastUtils.showToast(mContext, "加载下一页");
            } else {
                //the end
                voiceFileList.setNoMore(true);
            }
        }
    };
    private OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh() {


        }
    };
    private LRecyclerView.LScrollListener lScrollListener = new LRecyclerView.LScrollListener() {

        @Override
        public void onScrollUp() {
        }

        @Override
        public void onScrollDown() {
        }

        @Override
        public void onScrolled(int distanceX, int distanceY) {
        }

        @Override
        public void onScrollStateChanged(int state) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.voice_float_button:
                startActivity(new Intent(mContext,SpeakActivity.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onItemClick(int position) {
        FragmentCacheManager fragmentCacheManager = new FragmentCacheManager();
        fragmentCacheManager.setUp(getActivity(), R.id.framelayout);

        fragmentCacheManager.addInnerFragment(VoiceFragment_Inner3.class,null);
    }
}