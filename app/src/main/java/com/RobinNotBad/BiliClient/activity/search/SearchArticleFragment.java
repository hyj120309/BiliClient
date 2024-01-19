package com.RobinNotBad.BiliClient.activity.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.RobinNotBad.BiliClient.R;
import com.RobinNotBad.BiliClient.adapter.ArticleCardAdapter;
import com.RobinNotBad.BiliClient.api.SearchApi;
import com.RobinNotBad.BiliClient.model.ArticleInfo;
import com.RobinNotBad.BiliClient.util.CenterThreadPool;
import com.RobinNotBad.BiliClient.util.MsgUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class SearchArticleFragment extends Fragment {
    RecyclerView recyclerView;
    private ConstraintLayout searchBar;
    private ArrayList<ArticleInfo> articleInfoList;

    private ArticleCardAdapter articleCardAdapter;

    private String keyword;
    private boolean refreshing = false;
    private boolean bottom = false;
    private int page = 0;
    private int searchbar_alpha = 100;

    public SearchArticleFragment(){}

    public static SearchArticleFragment newInstance(ArrayList<ArticleInfo> articleInfoList, ConstraintLayout searchBar, String keyword) {
        SearchArticleFragment fragment = new SearchArticleFragment();
        fragment.articleInfoList = articleInfoList;
        fragment.searchBar = searchBar;
        fragment.keyword = keyword;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        CenterThreadPool.run(() -> {
            if(isAdded()) requireActivity().runOnUiThread(() -> {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                articleCardAdapter = new ArticleCardAdapter(requireContext(), articleInfoList);
                recyclerView.setAdapter(articleCardAdapter);

                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        assert manager != null;
                        int lastItemPosition = manager.findLastCompletelyVisibleItemPosition();  //获取最后一个完全显示的itemPosition
                        int itemCount = manager.getItemCount();
                        if (lastItemPosition >= (itemCount - 3) && dy>0 && !refreshing && !bottom) {// 滑动到倒数第三个就可以刷新了
                            refreshing = true;
                            CenterThreadPool.run(() -> continueLoading(requireContext())); //加载第二页
                        }
                        searchbar_alpha = searchbar_alpha - dy;
                        if(searchbar_alpha < 0){
                            searchbar_alpha = 0;
                            searchBar.setVisibility(View.GONE);
                        }else{
                            searchBar.setVisibility(View.VISIBLE);
                        }
                        if(searchbar_alpha > 100){
                            searchbar_alpha = 100;
                        }
                        searchBar.setAlpha(searchbar_alpha / 100f);
                        Log.e("debug","dx=" + dx + ",dy=" + dy);
                    }
                });
            });
        });
    }

    private void continueLoading(Context context){
        refreshing = true;
        page++;
        Log.e("debug","加载下一页");
        int lastSize = articleInfoList.size();
        try {
            JSONArray result =  SearchApi.searchType(keyword,page,"article");
            if(result!=null) {
                SearchApi.getArticlesFromSearchResult(result, articleInfoList);
                CenterThreadPool.runOnMainThread(() -> articleCardAdapter.notifyItemRangeInserted(lastSize + 1,articleInfoList.size()-lastSize));
            }
            else {
                bottom = true;
                MsgUtil.toast("已经到底啦OwO",context);
            }
        } catch (IOException e){
            CenterThreadPool.runOnMainThread(()-> MsgUtil.quickErr(MsgUtil.err_net,context));
            e.printStackTrace();
        } catch (JSONException e) {
            CenterThreadPool.runOnMainThread(()-> MsgUtil.jsonErr(e,context));
            e.printStackTrace();
        }
        refreshing = false;
    }

    public void refresh(ArrayList<ArticleInfo> articleInfoList, String keyword){
        this.keyword = keyword;
        int size_old = this.articleInfoList.size();
        this.articleInfoList = articleInfoList;
        CenterThreadPool.runOnMainThread(()-> {
            articleCardAdapter.notifyItemRangeRemoved(0,size_old);
            articleCardAdapter.notifyItemRangeInserted(0,articleInfoList.size());

            refreshing = false;
            bottom = false;
            page = 0;
            searchbar_alpha = 100;
        });
    }
}
