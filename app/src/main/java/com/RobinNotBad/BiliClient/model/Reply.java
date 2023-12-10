package com.RobinNotBad.BiliClient.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Reply {
    public long rpid;
    public String pubTime;
    public long senderID;
    public String senderName;
    public String senderAvatar;
    public String message;
    public JSONArray emote;
    public ArrayList<String> pictureList;
    public int likeCount;
    public boolean upLiked;
    public boolean upReplied;
    public boolean liked;
    public int childCount;
    public ArrayList<String> childMsgList;

    public Reply(){}
}