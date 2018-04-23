package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.cbinterface.RightClick;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.LViewHolder> {
    private Context context;
    private List<Recommend> recommends;
    private RightClick rightClick;

    public void setRightClick(RightClick rightClick) {
        this.rightClick = rightClick;
    }

    public LeftAdapter(Context context, List<Recommend> recommends) {
        this.context = context;
        this.recommends = recommends;
    }

    @Override
    public LViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.left_item,null);
        LViewHolder viewHolder = new LViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LViewHolder holder, int position) {
        holder.title.setTextColor(context.getResources().getColor(R.color.color_txt_gray));
        final Recommend recommend = recommends.get(position);
        holder.title.setText(recommend.getMaintitle().getTitle_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightClick!=null){
                    rightClick.itemClick(recommend);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return recommends==null?0:recommends.size();
    }

    class LViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public LViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
