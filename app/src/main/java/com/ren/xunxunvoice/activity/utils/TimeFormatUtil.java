package com.ren.xunxunvoice.activity.utils;

import android.content.Context;

import com.ren.xunxunvoice.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by renzhenming on 2017/6/1.
 */
public class TimeFormatUtil {

    public static String getInterval(Context context,long timeStamp) {
        if (context == null)
            return null;
        Date data = new Date(timeStamp*1000);
        String interval;
        long millisecond = new Date().getTime() - data.getTime();
        long second = millisecond / 1000;                                       //second为秒
        if (second <= 0) {
            second = 0;
        }
        /*if (second >= 0 && second < 60 ){
            interval = context.getString(R.string.a_moment_ago);
        }else if (second >= 60 && second <= 60 * 60) {                                 //大于1分钟 小于1小时
            long minute = second / 60;
            if (minute == 1){
                interval = minute + context.getResources().getString(R.string.minute_before);
            }else{
                interval = minute + context.getResources().getString(R.string.minutes_before);
            }
        } else if (second > 60 * 60 && second <= 60 * 60 * 24) {     */         //大于1小时 小于24小时
            interval = getFormatTime(data, "HH:mm:ss");
        /*  } else if (second > 60 * 60 * 24 && second <= 60 * 60 * 24 * 2) {       //大于1天 小于2天
            interval = context.getResources().getString(R.string.yesterday);
        } else if (second > 60 * 60 * 24 * 2 && second <= 60 * 60 * 24 * 3) {   //大于2天 小于3天
            interval = context.getResources().getString(R.string.the_day_before_yesterday);
        } else {                                                                //三天之外
            interval = getFormatTime(data, "MM-dd");
        }*/
        return interval;

    }

    public static String getFormatTime(Date date, String Sdf) {
        return (new SimpleDateFormat(Sdf)).format(date);
    }

    public static Date timeStamp2Date(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return new Date(timeStamp * 1000);
    }
}
