package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.HomeTab;
import com.chinabrowser.cbinterface.HomeTabClick;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */

public class LabsManager extends RecyclerView.Adapter<LabsManager.LViewHolder> {
    private Context context;
    private List<HomeTab> homeTabs;
    private HomeTabClick homeTabClick;

    public LabsManager(Context context, List<HomeTab> homeTabs) {
        this.context = context;
        this.homeTabs = homeTabs;
    }

    public void setHomeTabClick(HomeTabClick homeTabClick) {
        this.homeTabClick = homeTabClick;
    }

    @Override
    public LViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.tabmanager_lvitem,null);
        LViewHolder viewHolder = new LViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LViewHolder holder, int position) {
        position = getItemCount()-position-1;
         HomeTab homeTab = homeTabs.get(position);
        holder.tabmanager_lvitem_img.setImageBitmap(homeTab.bitmap);
        holder.tabmanager_lvitem_title.setText(homeTab.title);
        final int finalPosition = position;
        holder.tabmanager_lvitem_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeTabClick!=null){
                    homeTabClick.itemClick(finalPosition);
                }
            }
        });
        if (position!=0){
            holder.tabmanager_lvitem_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeTabClick!=null){
                        homeTabClick.delClick(finalPosition);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return homeTabs==null?0:homeTabs.size();
    }

    class LViewHolder extends RecyclerView.ViewHolder{
        ImageView tabmanager_lvitem_img;
        ImageView tabmanager_lvitem_remove;
        TextView tabmanager_lvitem_title;

        public LViewHolder(View itemView) {
            super(itemView);
            tabmanager_lvitem_img = (ImageView) itemView.findViewById(R.id.tabmanager_lvitem_img);
            tabmanager_lvitem_remove = (ImageView) itemView.findViewById(R.id.tabmanager_lvitem_remove);
            tabmanager_lvitem_title = (TextView) itemView.findViewById(R.id.tabmanager_lvitem_title);
        }
    }
}
