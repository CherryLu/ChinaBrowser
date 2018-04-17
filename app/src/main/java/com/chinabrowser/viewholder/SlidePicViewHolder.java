package com.chinabrowser.viewholder;

import android.view.View;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.ui.CustomViewpager;

/**
 * Created by 95470 on 2018/4/15.
 */

public class SlidePicViewHolder extends BaseViewHolder {

    CustomViewpager viewpager;

    public SlidePicViewHolder(View itemView) {
        super(itemView);
        viewpager = (CustomViewpager) itemView.findViewById(R.id.pic);
        initViewPager(viewpager);
    }

    @Override
    public void setRecommend(Recommend recommend) {

    }

    private void initViewPager(CustomViewpager viewpager){

    }
}
