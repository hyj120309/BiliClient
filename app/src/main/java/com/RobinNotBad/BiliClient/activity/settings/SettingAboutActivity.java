package com.RobinNotBad.BiliClient.activity.settings;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.RobinNotBad.BiliClient.R;
import com.RobinNotBad.BiliClient.activity.base.BaseActivity;

public class SettingAboutActivity extends BaseActivity {
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);
        Log.e("debug","进入关于页面");

        findViewById(R.id.top).setOnClickListener(view -> finish());
        
        try{
            ((TextView)findViewById(R.id.app_version)).setText(getPackageManager().getPackageInfo(getPackageName(),0).versionName);
        }catch(PackageManager.NameNotFoundException e){
            e.printStackTrace();
        }
    }
}