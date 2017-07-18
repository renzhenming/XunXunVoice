package com.ren.xunxunvoice.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ren.xunxunvoice.R;
import com.ren.xunxunvoice.activity.bean.VoiceBean;
import com.ren.xunxunvoice.activity.utils.TimeFormatUtil;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/25.
 */
public class VoiceAdapter extends RecyclerView.Adapter {

    private Context context;
    private ArrayList<VoiceBean> voiceList;

    public VoiceAdapter(Context mContext, ArrayList<VoiceBean> mVoiceList) {
        this.context = mContext;
        this.voiceList = mVoiceList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_voice,parent,false);
        return new VoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        VoiceHolder voiceHolder = (VoiceHolder) holder;
        voiceHolder.setData(voiceList.get(position));
        voiceHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return voiceList.size();
    }

    class VoiceHolder extends RecyclerView.ViewHolder{

        private final TextView content;
        private final TextView time;

        public VoiceHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.item_voice_content);
            time = (TextView) itemView.findViewById(R.id.item_voice_time);
        }

        public void setData(VoiceBean voiceBean) {
            content.setText(voiceBean.content+" -- "+TimeFormatUtil.getInterval(context,System.currentTimeMillis()));
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
