package com.RobinNotBad.BiliClient.util;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.zip.Inflater;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 被 luern0313 创建于 2019/10/13.
 * #以下代码来源于腕上哔哩的开源项目，感谢开源者做出的贡献！
 */

public class NetWorkUtil
{
    private static final AtomicReference<OkHttpClient> INSTANCE = new AtomicReference<>();
    private static OkHttpClient getOkHttpInstance() {
        while(INSTANCE.get() == null){
            INSTANCE.compareAndSet(null, new OkHttpClient
                    .Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS).build());
        }
        return INSTANCE.get();
    }

    public static Response get(String url) throws IOException
    {
        Log.e("debug-get","----------------");
        Log.e("debug-get-url",url);
        Log.e("debug-get","----------------");
        OkHttpClient client = getOkHttpInstance();
        Request.Builder requestb = new Request.Builder().url(url).header("Referer", "https://www.bilibili.com/").addHeader("Accept", "*/*").addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        Request request = requestb.build();
        Response response = client.newCall(request).execute();

        if(response.isSuccessful())
            return response;
        return null;
    }

    public static Response get(String url, ArrayList<String> headers) throws IOException
    {
        Log.e("debug-get","----------------");
        Log.e("debug-get-url",url);
        Log.e("debug-get","----------------");
        OkHttpClient client = getOkHttpInstance();
        Request.Builder requestBuilder = new Request.Builder().url(url).get();
        for(int i = 0; i < headers.size(); i+=2)
            requestBuilder = requestBuilder.addHeader(headers.get(i), headers.get(i+1));
        Request request = requestBuilder.build();
        return client.newCall(request).execute();
    }

    public static Response post(String url, String data, ArrayList<String> headers) throws IOException
    {
        Log.e("debug-post","----------------");
        Log.e("debug-post-url",url);
        Log.e("debug-post-data",data);
        Log.e("debug-post","----------------");
        OkHttpClient client = getOkHttpInstance();
        RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"), data);
        Request.Builder requestBuilder = new Request.Builder().url(url).post(body);
        for(int i = 0; i < headers.size(); i+=2)
            requestBuilder = requestBuilder.addHeader(headers.get(i), headers.get(i+1));
        Request request = requestBuilder.build();
        return client.newCall(request).execute();
    }


    public static byte[] readStream(InputStream inStream) throws IOException
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static byte[] uncompress(byte[] inputByte) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(inputByte.length);
        try
        {
            Inflater inflater = new Inflater(true);
            inflater.setInput(inputByte);
            byte[] buffer = new byte[4 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        byte[] output = outputStream.toByteArray();
        outputStream.close();
        return output;
    }
}
