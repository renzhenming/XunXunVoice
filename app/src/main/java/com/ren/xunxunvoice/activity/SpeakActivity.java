package com.ren.xunxunvoice.activity;

import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

import com.iflytek.cloud.SpeechConstant;
import com.ren.xunxunvoice.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SpeakActivity extends AppCompatActivity implements View.OnClickListener {

    @InjectView(R.id.speak_choose_pic)
    RadioButton speakChoosePic;
    @InjectView(R.id.speak_begin_voice)
    RadioButton speakBeginVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speak);
        ButterKnife.inject(this);
        initView();
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
