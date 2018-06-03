package com.chinabrowser.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.utils.GlideUtils;
import com.chinabrowser.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class LabelsView extends BaseView implements View.OnClickListener{
    Context mContext;
    LinearLayout[] linearLayouts = new LinearLayout[6];
    TextView[] textViews = new TextView[6];
    ImageView[] imageViews = new ImageView[6];
    List<Content> contents;
    public LabelsView(Context context, ViewGroup parent) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVivew =inflater.inflate(R.layout.labs_item,parent,false);

        linearLayouts[0] = (LinearLayout) mVivew.findViewById(R.id.layout1);
        linearLayouts[1] = (LinearLayout) mVivew.findViewById(R.id.layout2);
        linearLayouts[2] = (LinearLayout) mVivew.findViewById(R.id.layout3);
        linearLayouts[3] = (LinearLayout) mVivew.findViewById(R.id.layout4);
        linearLayouts[4] = (LinearLayout) mVivew.findViewById(R.id.layout5);
        linearLayouts[5] = (LinearLayout) mVivew.findViewById(R.id.layout6);
        for (int i =0;i<linearLayouts.length;i++){
            linearLayouts[i].setOnClickListener(this);
        }
        textViews[0] = (TextView) mVivew.findViewById(R.id.name1);
        textViews[1] = (TextView) mVivew.findViewById(R.id.name2);
        textViews[2] = (TextView) mVivew.findViewById(R.id.name3);
        textViews[3] = (TextView) mVivew.findViewById(R.id.name4);
        textViews[4] = (TextView) mVivew.findViewById(R.id.name5);
        textViews[5] = (TextView) mVivew.findViewById(R.id.name6);

       textViews[5].setText(context.getText(R.string.more_text));

        imageViews[0] = (ImageView) mVivew.findViewById(R.id.cover1);
        imageViews[1] = (ImageView) mVivew.findViewById(R.id.cover2);
        imageViews[2] = (ImageView) mVivew.findViewById(R.id.cover3);
        imageViews[3] = (ImageView) mVivew.findViewById(R.id.cover4);
        imageViews[4] = (ImageView) mVivew.findViewById(R.id.cover5);
        imageViews[5] = (ImageView) mVivew.findViewById(R.id.cover6);
    }

    @Override
    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
        contents = new ArrayList<>();
        if (recommend!=null){
            List<Content> datas = recommend.getContents();
            if (datas!=null){
                int count = 5;
                if (datas.size()>=5){
                    count = 5;
                }else {
                    count = datas.size();
                }
                for (int i =0;i<count;i++){
                    contents.add(datas.get(i));
                    GlideUtils.loadImageView(APP.getContext(),datas.get(i).getCover_image(),imageViews[i]);
                    textViews[i].setText(datas.get(i).getTitle());
                }
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout1:
                if (homeCallBack!=null){
                    if (contents.size()>=1&&contents.get(0)!=null){
                        homeCallBack.startContentByurl(recommend.getContents().get(0));
                    }
                }
                break;
            case R.id.layout2:
                if (homeCallBack!=null){
                    if (contents.size()>=2&&contents.get(1)!=null){
                        homeCallBack.startContentByurl(recommend.getContents().get(1));
                    }
                }
                break;
            case R.id.layout3:
                if (homeCallBack!=null){
                    if (contents.size()>=3&&contents.get(2)!=null){
                        homeCallBack.startContentByurl(recommend.getContents().get(2));
                    }
                }
                break;
            case R.id.layout4:
                if (homeCallBack!=null){
                    if (contents.size()>=4&&contents.get(3)!=null){
                        homeCallBack.startContentByurl(recommend.getContents().get(3));
                    }
                }
                break;
            case R.id.layout5:
                if (homeCallBack!=null){
                    if (contents.size()>=5&&contents.get(4)!=null){
                        homeCallBack.startContentByurl(recommend.getContents().get(4));
                    }
                }
                break;
            case R.id.layout6:
                Navigator.startRecommandActivity(v.getContext());
                break;

        }
    }
}
