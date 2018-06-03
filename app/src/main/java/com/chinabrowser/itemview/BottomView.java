package com.chinabrowser.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;

/**
 * Created by 95470 on 2018/4/16.
 */

public class BottomView extends BaseView {
    Context mContext;
    public BottomView(Context context, ViewGroup parent) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVivew =inflater.inflate(R.layout.bottomline,parent,false);

    }

    @Override
    public void setRecommend(Recommend recommend) {

    }
}
