package com.RobinNotBad.BiliClient.util;

import android.content.Context;
import android.webkit.URLUtil;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//2023-07-25

public class LittleToolsUtil {
    public static String toWan(long num){
        if(num>=10000){
            return String.format(Locale.CHINA, "%.1f", (float)num/10000) + "万";
        }
        else return String.valueOf(num);
    }

    public static String htmlToString(String html){
        return html.replace("&lt;","<")
                .replace("&gt;",">")
                .replace("&quot;","\"")
                .replace("&amp;","&")
                .replace("&#39;", "'")
                .replace("&#34;", "\"")
                .replace("&#38;", "&")
                .replace("&#60;", "<")
                .replace("&#62;", ">");
    }

    public static String stringToFile(String str){
        return str.replace("|", "｜")
                .replace(":", "：")
                .replace("*", "﹡")
                .replace("?", "？")
                .replace("\"", "”")
                .replace("<", "＜")
                .replace(">", "＞")
                .replace("/", "／")
                .replace("\\", "＼");    //文件名里不能包含非法字符
    }

    public static String unEscape(String str){
        return str.replaceAll("\\\\(.)","$1");
    }

    public static int dp2px(float dpValue, Context context)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(float spValue,Context context)
    {
        final float fontScale = context.getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String getFileNameFromLink(String link){
        int length = link.length();
        for (int i = length - 1; i > 0; i--) {
            if(link.charAt(i)=='/'){
                return link.substring(i+1);
            }
        }
        return "fail";
    }
    public static String extractCodeFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            String path = url.getPath();

            // 使用正则表达式匹配URL路径
            Pattern pattern = Pattern.compile("/([^/]+)\\.\\w+$");
            Matcher matcher = pattern.matcher(path);

            if (matcher.find()) {
                String code = matcher.group(1);
                return code;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getFileFirstName(String file){
        for (int i = 0; i < file.length(); i++) {
            if(file.charAt(i)=='.'){
                return file.substring(0,i);
            }
        }
        return "fail";
    }

    public static String getInfoFromCookie(String name, String cookie)
    {
        String[] cookies = cookie.split("; ");
        for(String i : cookies)
        {
            if(i.contains(name + "="))
                return i.substring(name.length() + 1);
        }
        return "";
    }
}
