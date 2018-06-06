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
    public void onBindViewHolder(LViewHolder holder, final int position) {


        final Recommend recommend = recommends.get(position);
        if (recommend.isSelect()){
            holder.title.setTextColor(context.getResources().getColor(R.color.color_txt_deep_black));
            holder.re_view.setVisibility(View.VISIBLE);
        }else {
            holder.title.setTextColor(context.getResources().getColor(R.color.color_txt_gray));
            holder.re_view.setVisibility(View.GONE);
        }


        holder.title.setText(recommend.getMaintitle().getTitle_name());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAllFalse();
                recommend.setSelect(true);
                notifyDataSetChanged();
                if (rightClick!=null){
                    rightClick.itemClick(recommend,position);
                }
            }
        });



    }

    private void setAllFalse(){
        if (recommends==null){
            return;
        }
        for (int i =0;i<recommends.size();i++){
            recommends.get(i).setSelect(false);
        }
    }

    @Override
    public int getItemCount() {
        return recommends==null?0:recommends.size();
    }

    class LViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        View re_view;
        public LViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            re_view = itemView.findViewById(R.id.re_view);
        }

    }
}
