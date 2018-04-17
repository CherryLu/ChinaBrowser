package com.chinabrowser.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.viewholder.BaseViewHolder;
import com.chinabrowser.viewholder.BottomViewHolder;
import com.chinabrowser.viewholder.LabelsViewHolder;
import com.chinabrowser.viewholder.OneLineViewHolder;
import com.chinabrowser.viewholder.RadioViewholder;
import com.chinabrowser.viewholder.SearchViewHolder;
import com.chinabrowser.viewholder.SlidePicViewHolder;
import com.chinabrowser.viewholder.TranslateViewHolder;
import com.chinabrowser.viewholder.TwoLineViewHolder;

import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class HomeAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private Context context;

    private List<Recommend> recommends;

    public HomeAdapter(Context context, List<Recommend> recommends) {
        this.context = context;
        this.recommends = recommends;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(viewType,parent);
    }

    private BaseViewHolder getViewHolder(int viewType,ViewGroup parent){
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType){
            case Constant.TRANSLATE: {
                View view = inflater.inflate(R.layout.translate_first_item,parent,false);
                TranslateViewHolder viewHolder = new TranslateViewHolder(view);
                return viewHolder;
            }
            case Constant.SEARCHLAYOUT:{
                View view = inflater.inflate(R.layout.search_title,parent,false);
                SearchViewHolder viewHolder = new SearchViewHolder(view);
                return viewHolder;
            }

            case Constant.SLIDE:{
                View view = inflater.inflate(R.layout.bigpic_item,parent,false);
                SlidePicViewHolder viewHolder = new SlidePicViewHolder(view);
                return viewHolder;
            }

            case Constant.ONELINE:{
                View view = inflater.inflate(R.layout.oneline_bigpic,parent,false);
                OneLineViewHolder viewHolder = new OneLineViewHolder(view);
                return viewHolder;
            }
            case Constant.TWOLINE:{
                View view = inflater.inflate(R.layout.twoline_pic_item,parent,false);
                TwoLineViewHolder viewHolder = new TwoLineViewHolder(view);
                return viewHolder;
            }
            case Constant.RADIO: {
                View view = inflater.inflate(R.layout.radio_item,parent,false);
                RadioViewholder viewholder = new RadioViewholder(view);
                return viewholder;
            }
            case Constant.LABS:{
                View view = inflater.inflate(R.layout.labs_item,parent,false);
                LabelsViewHolder viewHolder = new LabelsViewHolder(view);
                return viewHolder;
            }
            case Constant.BOTTOM:{
                View view = inflater.inflate(R.layout.bottomline,parent,false);
                BottomViewHolder viewHolder = new BottomViewHolder(view);
                return viewHolder;
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Recommend recommend = recommends.get(position);
        switch (recommend.getType()){
            case Constant.SEARCHLAYOUT:{
                View view = View.inflate(context,R.layout.search_title,null);
                SearchViewHolder viewHolder = new SearchViewHolder(view);
                break;
            }

            case Constant.SLIDE:{
                SlidePicViewHolder viewHolder = (SlidePicViewHolder) holder;
                viewHolder.setRecommend(recommend);
                break;
            }

            case Constant.ONELINE:{
                OneLineViewHolder viewHolder = (OneLineViewHolder) holder;
                viewHolder.setRecommend(recommend);
                break;
            }
            case Constant.TWOLINE:{
                TwoLineViewHolder viewHolder = (TwoLineViewHolder) holder;
                viewHolder.setRecommend(recommend);
                break;
            }
            case Constant.RADIO: {
                RadioViewholder viewholder = (RadioViewholder) holder;
                viewholder.setRecommend(recommend);
                break;
            }
            case Constant.LABS:{
                LabelsViewHolder viewHolder = (LabelsViewHolder) holder;
                viewHolder.setRecommend(recommend);
                break;
            }
            case Constant.TRANSLATE:{
                TranslateViewHolder viewHolder = (TranslateViewHolder) holder;
                viewHolder.setRecommend(recommend);
                break;
            }
            case Constant.BOTTOM:{
                BottomViewHolder viewHolder = (BottomViewHolder) holder;
                viewHolder.setRecommend(recommend);
                break;
            }
        }
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



    @Override
    public int getItemCount() {
        return recommends==null?0:recommends.size();
    }
}
