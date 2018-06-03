package com.chinabrowser.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chinabrowser.bean.Recommend;
import com.chinabrowser.cbinterface.HomeCallBack;
import com.chinabrowser.itemview.BaseView;
import com.chinabrowser.itemview.BottomView;
import com.chinabrowser.itemview.OneLineView;
import com.chinabrowser.itemview.RadioView;
import com.chinabrowser.itemview.SlidePicView;
import com.chinabrowser.itemview.TranslateView;
import com.chinabrowser.itemview.TwoLineView;
import com.chinabrowser.utils.Constant;

import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class HomeListAdapter extends BaseAdapter {

    private Context context;

    private List<Recommend> recommends;

    private HomeCallBack homeCallBack;

    public HomeListAdapter(Context context, List<Recommend> recommends) {
        this.context = context;
        this.recommends = recommends;
    }


    public void setHomeCallBack(HomeCallBack homeCallBack) {
        this.homeCallBack = homeCallBack;
    }



    private BaseView getViewHolder(int viewType,ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case Constant.TRANSLATE: {
                TranslateView translateView = new TranslateView(context,parent);
                translateView.setHomeCallBack(homeCallBack);
                return translateView;
            }

            case Constant.SLIDE:{
                SlidePicView picView = new SlidePicView(context,parent);
                picView.setHomeCallBack(homeCallBack);
                return picView;
            }

            case Constant.ONELINE:{
                OneLineView oneLineView = new OneLineView(context,parent);
                oneLineView.setHomeCallBack(homeCallBack);
                return oneLineView;
            }
            case Constant.TWOLINE:{
                TwoLineView lineView = new TwoLineView(context,parent);
                lineView.setHomeCallBack(homeCallBack);
                return lineView;
            }
            case Constant.RADIO: {
                RadioView radioView = new RadioView(context,parent);
                radioView.setHomeCallBack(homeCallBack);
                return radioView;
            }

            case Constant.BOTTOM:{
                BottomView bottomView = new BottomView(context,parent);
                bottomView.setHomeCallBack(homeCallBack);
                return bottomView;
            }
        }
        return null;
    }



    @Override
    public int getCount() {
        return recommends==null?0:recommends.size();
    }

    @Override
    public Object getItem(int position) {
        return recommends==null?null:recommends.get(position);
    }



    @Override
    public long getItemId(int position) {
        return position;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recommend recommend = recommends.get(position);
        View view = convertView;
       // if (view==null){
            BaseView baseView = getViewHolder(recommend.getType(),parent);
            baseView.setRecommend(recommend);
            view = baseView.mVivew;
            view.setTag(baseView);
       // }else {
           // BaseView baseView = (BaseView) view.getTag();
          //  baseView.setRecommend(recommend);
            //baseView.setHomeCallBack(homeCallBack);
       // }
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        Recommend recommend = recommends.get(position);
        if (recommend!=null){
            return recommend.getType();
        }else {
            return 0;
        }

    }

}
