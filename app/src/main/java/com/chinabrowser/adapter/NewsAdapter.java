package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.NewsData;
import com.chinabrowser.cbinterface.PagerClick;
import com.chinabrowser.utils.GlideUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by 95470 on 2018/4/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.HViewHolder> {
    private Context context;
    private List<NewsData> newsDatas;
    private PagerClick pagerClick;

    public void setPagerClick(com.chinabrowser.cbinterface.PagerClick pagerClick) {
        this.pagerClick = pagerClick;
    }

    public NewsAdapter(Context context, List<NewsData> newsDatas) {
        this.context = context;
        this.newsDatas = newsDatas;
    }

    @Override
    public HViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.hotnews_item,parent,false);
        HViewHolder viewHolder = new HViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HViewHolder holder, final int position) {
        NewsData newsData =  newsDatas.get(position);
        holder.maintitle.setText(newsData.title);
        holder.from.setText(newsData.copy_from);
        holder.time.setText(getData(newsData.update_time));
        GlideUtils.loadImageView(context,newsData.cover_image,holder.pic);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pagerClick!=null){
                    pagerClick.pagerClick(position);
                }
            }
        });

    }

    public String getData(String str){
        try {
            Long current = Long.parseLong(str);
            Date date = new Date(current);
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            return format1.format(date);
        }catch (Exception e){
            return "";
        }

    }


    @Override
    public int getItemCount() {
        return newsDatas==null?0:newsDatas.size();
    }

    class HViewHolder extends RecyclerView.ViewHolder{
        TextView maintitle,from,time;
        ImageView pic;
        public HViewHolder(View itemView) {
            super(itemView);
            maintitle = (TextView) itemView.findViewById(R.id.main_title);
            from = (TextView) itemView.findViewById(R.id.from);
            time = (TextView) itemView.findViewById(R.id.time);
            pic = (ImageView) itemView.findViewById(R.id.pic);
        }
    }
}
