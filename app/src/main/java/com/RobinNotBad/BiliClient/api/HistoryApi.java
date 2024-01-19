package com.RobinNotBad.BiliClient.api;

import com.RobinNotBad.BiliClient.model.VideoCard;
import com.RobinNotBad.BiliClient.util.LittleToolsUtil;
import com.RobinNotBad.BiliClient.util.NetWorkUtil;
import com.RobinNotBad.BiliClient.util.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Response;

public class HistoryApi {
    public static void reportHistory(long aid, long cid,long mid, long progress) throws IOException {
        String url = "https://api.bilibili.com/x/report/web/heartbeat";
        String per = "aid=" + aid + "&cid=" + cid + "&mid=" + mid + "&csrf=" + SharedPreferencesUtil.getString("csrf","") + "&played_time=" + progress + "&realtime=0&start_ts=" + (System.currentTimeMillis() / 1000) + "&type=3&dt=2&play_type=1";
        NetWorkUtil.post(url,per,ConfInfoApi.webHeaders);
    }

    public static int getHistory(int page, ArrayList<VideoCard> videoList) throws IOException, JSONException {
        String url = "https://api.bilibili.com/x/v2/history?pn=" + page + "&ps=30";
        Response response = NetWorkUtil.get(url,ConfInfoApi.webHeaders);
        JSONObject result = new JSONObject(Objects.requireNonNull(response.body()).string());
        if(!result.isNull("data")){
            JSONArray data = result.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject videoCard = data.getJSONObject(i);
                long aid = videoCard.getLong("aid");
                String bvid = videoCard.getString("bvid");
                String title = videoCard.getString("title");
                String cover = videoCard.getString("pic");
                String upName = videoCard.getJSONObject("owner").getString("name");
                long view = videoCard.getJSONObject("stat").getLong("view");
                String viewStr = LittleToolsUtil.toWan(view) + "观看";
                videoList.add(new VideoCard(title,upName,viewStr,cover,aid,bvid));
            }
            return 0;
        }
        else return 1;
    }
}
