package com.RobinNotBad.BiliClient.activity.settings;


import static com.RobinNotBad.BiliClient.activity.settings.WBItest.wbi;

import com.RobinNotBad.BiliClient.activity.base.BaseActivity;
import com.RobinNotBad.BiliClient.api.ConfInfoApi;
import com.RobinNotBad.BiliClient.api.SearchApi;
import com.RobinNotBad.BiliClient.util.JsonUtil;
import com.RobinNotBad.BiliClient.util.MsgUtil;
import com.RobinNotBad.BiliClient.util.NetWorkUtil;
import com.RobinNotBad.BiliClient.util.SharedPreferencesUtil;
import com.google.android.material.card.MaterialCardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.RobinNotBad.BiliClient.R;
import com.RobinNotBad.BiliClient.activity.MenuActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class SettingMainActivity extends BaseActivity {

    public static SettingMainActivity instance = null;
    private int eggClick = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_main);
        Log.e("debug","进入设置页");
        instance = this;    //给菜单页面调用，用来结束本页面

        findViewById(R.id.top).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this,MenuActivity.class);
            intent.putExtra("from",5);
            startActivity(intent);
        });

        //登录
        MaterialCardView login = findViewById(R.id.login);
        if(SharedPreferencesUtil.getLong("mid",0)==0) {
            login.setVisibility(View.VISIBLE);
            login.setOnClickListener(view -> {
                Intent intent = new Intent();
                if(Build.VERSION.SDK_INT>=19) {
                    intent.setClass(this, QRLoginActivity.class);   //去扫码登录页面
                }
                else{
                    intent.setClass(this, SpecialLoginActivity.class);   //4.4以下系统去特殊登录页面
                    intent.putExtra("login",true);
                }
                startActivity(intent);
            });
        }

        //播放器设置
        MaterialCardView playerSetting = findViewById(R.id.playerSetting);
        playerSetting.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, SettingPlayerActivity.class);
            startActivity(intent);
        });

        //界面设置
        MaterialCardView uiSetting = findViewById(R.id.uiSetting);
        uiSetting.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, SettingUIActivity.class);
            startActivity(intent);
        });

        //偏好设置
        MaterialCardView prefSetting = findViewById(R.id.prefSetting);
        prefSetting.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, SettingPrefActivity.class);
            startActivity(intent);
        });

        //开源
        MaterialCardView openSource = findViewById(R.id.openSource);
        openSource.setOnClickListener(view -> MsgUtil.showText(this,"开源信息",getString(R.string.opensource)));

        //关于
        MaterialCardView about = findViewById(R.id.about);
        //about.setOnClickListener(view -> MsgUtil.showText(this,"关于",getString(R.string.about)));
        about.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, SettingAboutActivity.class);
            startActivity(intent);
        });
        
        //彩蛋
        String[] eggList = getResources().getStringArray(R.array.eggs);
        about.setOnLongClickListener(view -> {
            MsgUtil.showText(this,"彩蛋",eggList[eggClick]);
            if(eggClick<eggList.length-1) eggClick++;
            return true;
        });


        //检查更新
        MaterialCardView checkUpdate = findViewById(R.id.checkupdate);
        checkUpdate.setOnClickListener(view -> {
            MsgUtil.toast("正在获取...",this);
            new Thread(() -> {
                try {
                    ConfInfoApi.getUpdate(this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }).start();
        });

        //查看公告
        MaterialCardView announcement = findViewById(R.id.announcement);
        announcement.setOnClickListener(view -> {
            MsgUtil.toast("正在获取...",this);
            new Thread(() -> {
                try {
                    ConfInfoApi.getAnnouncement(this);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }).start();
        });

        MaterialCardView test = findViewById(R.id.test);    //未完成
        test.setOnClickListener(view -> {

            try {
                JSONObject jsonObject = new JSONObject("{\"mid\":474415592}");
                new HTTPGET(this).execute(jsonObject.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
}