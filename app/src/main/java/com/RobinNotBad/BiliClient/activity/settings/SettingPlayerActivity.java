package com.RobinNotBad.BiliClient.activity.settings;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import com.RobinNotBad.BiliClient.R;
import com.RobinNotBad.BiliClient.activity.base.BaseActivity;
import com.RobinNotBad.BiliClient.util.LittleToolsUtil;
import com.RobinNotBad.BiliClient.util.SharedPreferencesUtil;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

//选择播放器

public class SettingPlayerActivity extends BaseActivity {

    String player = SharedPreferencesUtil.getString("player","null");
    MaterialCardView mtvPlayer,aliangPlayer;
    SwitchCompat sw_highres;
    ArrayList<MaterialCardView> cardViewList;
    int checkPosition = -1;
    final String[] playerList = {"null","mtvPlayer","aliangPlayer"};


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_player_choose);
        Log.e("debug","选择播放器");

        findViewById(R.id.top).setOnClickListener(view -> finish());
        mtvPlayer = findViewById(R.id.mtvPlayer);
        aliangPlayer = findViewById(R.id.aliangPlayer);
        sw_highres = findViewById(R.id.high_res);

        sw_highres.setChecked(SharedPreferencesUtil.getBoolean("high_res",false));

        cardViewList = new ArrayList<>();
        cardViewList.add(mtvPlayer);
        cardViewList.add(aliangPlayer);

        if(Build.VERSION.SDK_INT <= 19) {
            TextView alplayer_desc = findViewById(R.id.aliangPlayer_desc);
            alplayer_desc.setText("安卓版本过低不支持");

            TextView mtvplayer_desc = findViewById(R.id.mtvPlayer_desc);
            mtvplayer_desc.setText("去交流群里下载改版");
        }

        switch (player){
            case "mtvPlayer":
                setChecked(0);
                break;

            case "aliangPlayer":
                setChecked(1);
                break;

            default:

                break;
        }

        setOnClick();
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onDestroy() {
        SharedPreferencesUtil.putString("player",playerList[checkPosition+1]);
        SharedPreferencesUtil.putBoolean("high_res",sw_highres.isChecked());
        Log.e("debug-选择",playerList[checkPosition+1]);

        super.onDestroy();
    }
    
    private void setOnClick(){
        for (int i = 0; i < cardViewList.size(); i++) {
            int finalI = i;
            cardViewList.get(i).setOnClickListener(view -> {
                setChecked(finalI);
                Log.e("debug","点击了"+finalI);
            });
        }
    }

    private void setChecked(int position){
        checkPosition = position;
        for (int i = 0; i < cardViewList.size(); i++) {
            if(position==i) {
                cardViewList.get(i).setStrokeColor(getResources().getColor(R.color.pink));
                cardViewList.get(i).setStrokeWidth(LittleToolsUtil.dp2px(1,this));
            }
            else{
                cardViewList.get(i).setStrokeColor(getResources().getColor(R.color.gray));
                cardViewList.get(i).setStrokeWidth(LittleToolsUtil.dp2px(0.1f,this));
            }
        }
    }
}