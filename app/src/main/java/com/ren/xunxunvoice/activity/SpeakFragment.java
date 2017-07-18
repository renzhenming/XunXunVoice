package com.ren.xunxunvoice.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import com.ren.xunxunvoice.R;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class SpeakFragment extends Fragment implements View.OnClickListener {

    @InjectView(R.id.speak_choose_pic)
    RadioButton speakChoosePic;
    @InjectView(R.id.speak_begin_voice)
    RadioButton speakBeginVoice;
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mContext,R.layout.activity_speak,null);
        ButterKnife.inject(this,view);
        initView();
        return view;
    }

    private void initView() {
        speakBeginVoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.speak_begin_voice:
                startRecognize();
                break;
        }
    }

    private void startRecognize() {
//        SpeechRecognizer mAsr = SpeechRecognizer.createSpeechRecognizer(this);
//        //设置语法ID和 SUBJECT 为空，以免因之前有语法调用而设置了此参数；或直接清空所有参数，具体可参考 DEMO 的示例。
//        mAsr.setParameter( SpeechConstant.CLOUD_GRAMMAR, null );
//        mAsr.setParameter( SpeechConstant.SUBJECT, null );
//
//        mAsr.setParameter( SpeechConstant.ENGINE_TYPE, engineType );
//        if( SpeechConstant.TYPE_LOCAL.equals(engineType) ){
//            //使用默认的语记模式
//            mAsr.setParameter( SpeechConstant.ENGINE_MODE, SpeechConstant.MODE_PLUS );
//        }else{
//            //使用默认的 MSC 模式
//            mAsr.setParameter( SpeechConstant.ENGINE_MODE, null );
//        }
//
//        mAsr.startListening(mRecogListener);
    }
}
