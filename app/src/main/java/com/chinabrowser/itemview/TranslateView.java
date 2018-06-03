package com.chinabrowser.itemview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.Navigator;

/**
 * Created by 95470 on 2018/4/15.
 */

public class TranslateView extends BaseView implements View.OnClickListener {

    LinearLayout[] layouts = new LinearLayout[6];
    ImageView[] imageViews = new ImageView[6];
    TextView[] textViews = new TextView[6];

    Context mContext;


    public TranslateView(Context context, ViewGroup parent) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVivew =inflater.inflate(R.layout.translate_first_item,parent,false);

        title = (TextView) mVivew.findViewById(R.id.title);
        more = (ImageView) mVivew.findViewById(R.id.more);

        layouts[0] = (LinearLayout) mVivew.findViewById(R.id.layout1);
        layouts[1] = (LinearLayout) mVivew.findViewById(R.id.layout2);
        layouts[2] = (LinearLayout) mVivew.findViewById(R.id.layout3);
        layouts[3] = (LinearLayout) mVivew.findViewById(R.id.layout4);
        layouts[4] = (LinearLayout) mVivew.findViewById(R.id.layout5);
        layouts[5] = (LinearLayout) mVivew.findViewById(R.id.layout6);
        for (int i=0;i<layouts.length;i++){
            layouts[i].setOnClickListener(this);
        }

        imageViews[0] = (ImageView) mVivew.findViewById(R.id.cover1);
        imageViews[1] = (ImageView) mVivew.findViewById(R.id.cover2);
        imageViews[2] = (ImageView) mVivew.findViewById(R.id.cover3);
        imageViews[3] = (ImageView) mVivew.findViewById(R.id.cover4);
        imageViews[4] = (ImageView) mVivew.findViewById(R.id.cover5);
        imageViews[5] = (ImageView) mVivew.findViewById(R.id.cover6);

        textViews[0] = (TextView) mVivew.findViewById(R.id.name1);
        textViews[1] = (TextView) mVivew.findViewById(R.id.name2);
        textViews[2] = (TextView) mVivew.findViewById(R.id.name3);
        textViews[3] = (TextView) mVivew.findViewById(R.id.name4);
        textViews[4] = (TextView) mVivew.findViewById(R.id.name5);
        textViews[5] = (TextView) mVivew.findViewById(R.id.name6);

        title.setBackgroundResource(R.mipmap.translate);
        title.setText(title.getResources().getText(R.string.translate_title));
        more.setOnClickListener(this);
    }

    @Override
    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout1:
                Navigator.startTranslateActivity(v.getContext(),0);
                break;
            case R.id.layout2:
                Navigator.startTranslateActivity(v.getContext(),1);
                break;
            case R.id.layout3:
                Navigator.startTranslateActivity(v.getContext(),2);
                break;
            case R.id.layout4:
                Navigator.startTranslateActivity(v.getContext(),3);
                break;
            case R.id.layout5:
                Navigator.startTranslateActivity(v.getContext(),4);
                break;
            case R.id.layout6:
                Navigator.startTranslateActivity(v.getContext(),5);
                break;
            case R.id.more://回调 切换Fragment
                if (homeCallBack!=null){
                    homeCallBack.titleClick(Constant.TRANSLATE_TITLE,recommend.getMaintitle());
                }
                break;

        }
    }
}
