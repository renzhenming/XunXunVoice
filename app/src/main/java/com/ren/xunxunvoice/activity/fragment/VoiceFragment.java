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

public class VoiceFragment extends Fragment implements View.OnClickListener {

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



    public static VoiceFragment newInstance(String param1, String param2) {
        VoiceFragment fragment = new VoiceFragment();
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
        View view = inflater.inflate(R.layout.fragment_voice, container, false);
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
            bean.content = "内容都上来看附件是大方剑履上殿"+i;
            mVoiceList.add(bean);
        }
        final VoiceAdapter voiceAdapter = new VoiceAdapter(mContext,mVoiceList);
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


}
/**
 *
 * LRecyclerView使用示例
 *
 package com.lzx.demo.ui;

 import android.os.Bundle;
 import android.support.v7.app.AppCompatActivity;
 import android.support.v7.widget.LinearLayoutManager;
 import android.support.v7.widget.Toolbar;
 import android.view.LayoutInflater;
 import android.view.MenuItem;
 import android.view.View;
 import android.view.ViewGroup;

 import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
 import com.github.jdsjlzx.interfaces.OnItemClickListener;
 import com.github.jdsjlzx.recyclerview.LRecyclerView;
 import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
 import com.lzx.demo.R;
 import com.lzx.demo.adapter.DataAdapter;
 import com.lzx.demo.bean.ItemModel;
 import com.lzx.demo.view.SampleFooter;
 import com.lzx.demo.view.SampleHeader;

 import java.util.ArrayList;

public class LinearLayoutActivity extends AppCompatActivity {

    private LRecyclerView mRecyclerView = null;

    private DataAdapter mDataAdapter = null;

    private LRecyclerViewAdapter mLRecyclerViewAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_ll_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (LRecyclerView) findViewById(R.id.list);

        //init data
        ArrayList<ItemModel> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ItemModel itemModel = new ItemModel();
            itemModel.title = "item" + i;
            dataList.add(itemModel);
        }

        mDataAdapter = new DataAdapter(this);
        mDataAdapter.setDataList(dataList);

        mLRecyclerViewAdapter = new LRecyclerViewAdapter(mDataAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        DividerDecoration divider = new DividerDecoration.Builder(this)
                .setHeight(R.dimen.default_divider_height)
                .setPadding(R.dimen.default_divider_padding)
                .setColorResource(R.color.split)
                .build();
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(divider);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //add a HeaderView
        View header = LayoutInflater.from(this).inflate(R.layout.sample_header,(ViewGroup)findViewById(android.R.id.content), false);

        mLRecyclerViewAdapter.addHeaderView(header);
        mLRecyclerViewAdapter.addHeaderView(new SampleHeader(this));


        //禁用下拉刷新功能
        mRecyclerView.setPullRefreshEnabled(false);

        //禁用自动加载更多功能
        mRecyclerView.setLoadMoreEnabled(false);

        SampleFooter sampleFooter = new SampleFooter(this);
        sampleFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 加载更多
                ArrayList<ItemModel> dataList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    ItemModel itemModel = new ItemModel();
                    itemModel.title = "item" + (i + mDataAdapter.getItemCount());
                    dataList.add(itemModel);
                }
                mDataAdapter.addAll(dataList);
            }
        });
        //add a FooterView
        mLRecyclerViewAdapter.addFooterView(sampleFooter);


        //删除item
        mLRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mDataAdapter.remove(position);
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

}*/