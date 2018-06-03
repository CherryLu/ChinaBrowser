package com.chinabrowser.itemview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.bean.Recommend;
import com.chinabrowser.cbinterface.HomeCallBack;

/**
 * Created by 95470 on 2018/6/2.
 */

public class BaseView {
    public Recommend recommend;
    public TextView title;
    public ImageView more;
    HomeCallBack homeCallBack;
    public View mVivew;

    public void setHomeCallBack(HomeCallBack homeCallBack) {
        this.homeCallBack = homeCallBack;
    }

    public  void setRecommend(Recommend recommend){
        this.recommend = recommend;
    }
}
