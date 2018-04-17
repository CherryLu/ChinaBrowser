package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chinabrowser.R;

/**
 * Created by Administrator on 2018/4/15.
 */

public class LeftAdapter extends RecyclerView.Adapter<LeftAdapter.LViewHolder> {
    private Context context;

    @Override
    public LViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.left_item,null);
        LViewHolder viewHolder = new LViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LViewHolder holder, int position) {
        if (position==0){
            holder.title.setTextColor(context.getResources().getColor(R.color.color_txt_deep_black));
        }else {
            holder.title.setTextColor(context.getResources().getColor(R.color.color_txt_gray));
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class LViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        public LViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
