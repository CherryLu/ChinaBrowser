package com.chinabrowser.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.cbinterface.HomeCallBack;

/**
 * Created by 95470 on 2018/4/15.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {
    public Recommend recommend;
    public TextView title;
    public ImageView more;
    HomeCallBack homeCallBack;
    public BaseViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        more = (ImageView) itemView.findViewById(R.id.more);
    }

    public void setHomeCallBack(HomeCallBack homeCallBack) {
        this.homeCallBack = homeCallBack;
    }

    public abstract void setRecommend(Recommend recommend);
}
