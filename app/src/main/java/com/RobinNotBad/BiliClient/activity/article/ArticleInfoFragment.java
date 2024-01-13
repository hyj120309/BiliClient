package com.RobinNotBad.BiliClient.activity.article;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RobinNotBad.BiliClient.R;
import com.RobinNotBad.BiliClient.adapter.ArticleContentAdapter;
import com.RobinNotBad.BiliClient.model.ArticleInfo;
import com.RobinNotBad.BiliClient.model.ArticleLine;
import com.RobinNotBad.BiliClient.util.CenterThreadPool;
import com.RobinNotBad.BiliClient.util.JsonUtil;
import com.RobinNotBad.BiliClient.util.LittleToolsUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;

public class ArticleInfoFragment extends Fragment {
    public ArticleInfo articleInfo;
    RecyclerView recyclerView;
    ArrayList<ArticleLine> lineList;
    public ArticleInfoFragment(){}

    public static ArticleInfoFragment newInstance(ArticleInfo articleInfo) {
        ArticleInfoFragment fragment = new ArticleInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("articleInfo", articleInfo);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            articleInfo = (ArticleInfo) getArguments().getSerializable("articleInfo");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple_list, container, false);
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);

        //开始解析html的内容
        lineList = new ArrayList<>();

        CenterThreadPool.run(()-> {
            try {
                JSONObject jsonObject = new JSONObject(articleInfo.content);
                for (int i = 0;i<jsonObject.getJSONArray("ops").length();i++){ //遍历Array
                    JSONObject element = jsonObject.getJSONArray("ops").getJSONObject(i);
                    if(element.has("insert")){
                        if(element.get("insert") instanceof JSONObject){ //有图片
                            lineList.add(new ArticleLine(1, JsonUtil.searchString(element.getJSONObject("insert"),"url",""),""));
                        }else{ //纯文字
                            lineList.add(new ArticleLine(0,element.getString("insert"),""));
                        }
                    }
                }
            }catch (JSONException e){
                e.printStackTrace();
                //如果抛出了JSONException就说明是HTML而不是JSON
                Document document = Jsoup.parse(articleInfo.content);
                loadContentHtml(document.select("body").get(0));
            }

            if (isAdded()) requireActivity().runOnUiThread(() -> {
                ArticleContentAdapter adapter = new ArticleContentAdapter(requireContext(),articleInfo,lineList);
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private void loadContentHtml(Element element){
        for(Element e : element.children()){
            if (e.is("p")){
                lineList.add(new ArticleLine(0,e.text(),""));
            }
            else if (e.is("strong")){
                lineList.add(new ArticleLine(0,e.text(),"strong"));
            }
            else if (e.is("br")){
                lineList.add(new ArticleLine(0,"","br"));
            }
            else if (e.is("img")){
                lineList.add(new ArticleLine(1,"http:" + e.attr("src") + "@50q.webp",""));
            }
            else loadContentHtml(e);
        }
    }
}
