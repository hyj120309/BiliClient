package com.RobinNotBad.BiliClient.activity.base;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.RobinNotBad.BiliClient.BiliClient;
import com.RobinNotBad.BiliClient.util.SharedPreferencesUtil;


public class BaseActivity extends AppCompatActivity {
    //调整应用内dpi的代码，其他Activity要继承于BaseActivity才能调大小
    @Override
    protected void attachBaseContext(Context newBase) {
        newBase = BiliClient.getFitDisplayContext(newBase);
        super.attachBaseContext(newBase);
    }

    //调整页面边距，参考了hankmi的方式
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int paddingH_percent = SharedPreferencesUtil.getInt("paddingH_percent",0);
        int paddingV_percent = SharedPreferencesUtil.getInt("paddingV_percent",0);
        if(paddingH_percent != 0 || paddingV_percent != 0) {
            Log.e("debug","调整边距");
            View rootView = this.getWindow().getDecorView().getRootView();
            WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getRealMetrics(metrics);
            int paddingV = metrics.heightPixels * paddingV_percent / 100;
            int paddingH = metrics.widthPixels * paddingH_percent / 100;
            rootView.setPadding(paddingH, paddingV, paddingH, paddingV);
        }
    }
    @Override
    public void onBackPressed() {
        if(!SharedPreferencesUtil.getBoolean("back_disable",false)) super.onBackPressed();
    }

}
