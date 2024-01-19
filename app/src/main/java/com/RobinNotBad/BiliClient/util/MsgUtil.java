package com.RobinNotBad.BiliClient.util;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.RobinNotBad.BiliClient.activity.ShowTextActivity;

import org.json.JSONException;

public class MsgUtil {
    public static int err_other = 0;
    public static int err_net = 1;
    public static int err_json = 2;
    private static final String[] texts = {"其它错误(＃°Д°)","网络错误(＃°Д°)","数据解析错误(＃°Д°)"};

    public static void toast(String str, Context context){
        Toast toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
            toast.show();
    }

    public static void jsonErr(JSONException e, Context context){
        toast("数据解析错误(＃°Д°)\n" + e.toString(),context);
    }
    public static void netErr(Context context) {
        quickErr(err_net,context);
    }

    public static void quickErr(int err,Context context){
        toast(texts[err],context);
    }

    public static void showText(Context context,String title,String text){
        Intent testIntent = new Intent()
                .setClass(context, ShowTextActivity.class)
                .putExtra("title",title)
                .putExtra("content",text);
        context.startActivity(testIntent);
    }
}
