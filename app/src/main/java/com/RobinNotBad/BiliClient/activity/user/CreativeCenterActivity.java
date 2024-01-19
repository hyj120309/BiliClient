package com.RobinNotBad.BiliClient.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.RobinNotBad.BiliClient.R;
import com.RobinNotBad.BiliClient.activity.MenuActivity;
import com.RobinNotBad.BiliClient.activity.base.BaseActivity;
import com.RobinNotBad.BiliClient.api.CreativeCenterApi;
import com.RobinNotBad.BiliClient.util.CenterThreadPool;
import com.RobinNotBad.BiliClient.util.LittleToolsUtil;
import com.RobinNotBad.BiliClient.util.MsgUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CreativeCenterActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creative_center);

        findViewById(R.id.top).setOnClickListener(view -> finish());

        CenterThreadPool.run(() -> {
            try {
                JSONObject stats = CreativeCenterApi.getVideoStat();
                runOnUiThread(() -> {
                    try {
                        ((TextView)findViewById(R.id.totalFans_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_fans")) + ((stats.getInt("incr_fans") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("incr_fans")));
                        ((TextView)findViewById(R.id.totalClick_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_click")) + ((stats.getInt("incr_click") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("incr_click")));
                        ((TextView)findViewById(R.id.totalLike_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_like")) + ((stats.getInt("inc_like") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("inc_like")));
                        ((TextView)findViewById(R.id.totalCoin_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_coin")) + ((stats.getInt("inc_coin") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("inc_coin")));
                        ((TextView)findViewById(R.id.totalFavourite_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_fav")) + ((stats.getInt("inc_fav") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("inc_fav")));
                        ((TextView)findViewById(R.id.totalShare_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_share")) + ((stats.getInt("inc_share") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("inc_share")));
                        ((TextView)findViewById(R.id.totalReply_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_reply")) + ((stats.getInt("incr_reply") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("incr_reply")));
                        ((TextView)findViewById(R.id.totalDm_number)).setText(LittleToolsUtil.toWan(stats.getInt("total_dm")) + ((stats.getInt("incr_dm") < 0) ? "-" : "+") + LittleToolsUtil.toWan(stats.getInt("incr_dm")));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        runOnUiThread(() -> MsgUtil.toast("加载失败",this));
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> MsgUtil.toast("加载失败",this));
            } catch (JSONException e) {
                e.printStackTrace();
                runOnUiThread(() -> MsgUtil.toast("加载失败",this));
            }

        });
    }
}
