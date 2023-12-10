package com.RobinNotBad.BiliClient.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.RobinNotBad.BiliClient.R;
import com.RobinNotBad.BiliClient.activity.ImageViewerActivity;
import com.RobinNotBad.BiliClient.model.Dynamic;
import com.RobinNotBad.BiliClient.model.UserInfo;
import com.RobinNotBad.BiliClient.util.LittleToolsUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

//用户信息页专用Adapter 独立出来也是为了做首项不同

public class UserInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<Dynamic> dynamicList;
    UserInfo userInfo;

    public UserInfoAdapter(Context context, ArrayList<Dynamic> dynamicList, UserInfo userInfo) {
        this.context = context;
        this.dynamicList = dynamicList;
        this.userInfo = userInfo;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 0) {
            View view = LayoutInflater.from(this.context).inflate(R.layout.cell_user_info, parent, false);
            return new UserInfoHolder(view);
        }
        else {
            View view = LayoutInflater.from(this.context).inflate(R.layout.cell_dynamic, parent, false);
            return new DynamicHolder(view,false);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof DynamicHolder) {
            int realPosition = position - 1;
            DynamicHolder dynamicHolder = (DynamicHolder) holder;

            dynamicHolder.showDynamic(dynamicList.get(realPosition),context);

            if(dynamicList.get(realPosition).childDynamic != null){
                Log.e("debug","有子动态！");
                View childCard = View.inflate(context,R.layout.cell_dynamic_child,dynamicHolder.extraCard);
                DynamicHolder childHolder = new DynamicHolder(childCard,true);
                childHolder.showDynamic(dynamicList.get(realPosition).childDynamic,context);
            }
        }
        if (holder instanceof UserInfoHolder){
            UserInfoHolder userInfoHolder = (UserInfoHolder) holder;
            userInfoHolder.userName.setText(userInfo.name);
            userInfoHolder.userDesc.setText(userInfo.sign);
            userInfoHolder.userFans.setText(LittleToolsUtil.toWan(userInfo.fans) + "粉丝");
            Glide.with(this.context).load(userInfo.avatar)
                    .placeholder(R.drawable.akari)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(userInfoHolder.userAvatar);

            userInfoHolder.userAvatar.setOnClickListener(view -> {
                Intent intent = new Intent();
                intent.setClass(context, ImageViewerActivity.class);
                ArrayList<String> imageList = new ArrayList<>();
                imageList.add(userInfo.avatar);
                intent.putExtra("imageList", imageList);
                context.startActivity(intent);
            });
        }
    }


    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        if(holder instanceof DynamicHolder) ((DynamicHolder)holder).extraCard.removeAllViews();
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return dynamicList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position==0 ? 0 : 1);
    }

    public static class UserInfoHolder extends RecyclerView.ViewHolder{
        TextView userName,userFans,userDesc;
        ImageView userAvatar;

        public UserInfoHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            userDesc = itemView.findViewById(R.id.userDesc);
            userFans = itemView.findViewById(R.id.userFans);
            userAvatar = itemView.findViewById(R.id.userAvatar);
        }
    }
}