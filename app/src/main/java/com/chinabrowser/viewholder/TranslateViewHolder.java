package com.chinabrowser.viewholder;

import android.view.View;
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

public class TranslateViewHolder extends BaseViewHolder implements View.OnClickListener {

    LinearLayout[] layouts = new LinearLayout[6];
    ImageView[] imageViews = new ImageView[6];
    TextView[] textViews = new TextView[6];


    public TranslateViewHolder(View itemView) {
        super(itemView);
        layouts[0] = (LinearLayout) itemView.findViewById(R.id.layout1);
        layouts[1] = (LinearLayout) itemView.findViewById(R.id.layout2);
        layouts[2] = (LinearLayout) itemView.findViewById(R.id.layout3);
        layouts[3] = (LinearLayout) itemView.findViewById(R.id.layout4);
        layouts[4] = (LinearLayout) itemView.findViewById(R.id.layout5);
        layouts[5] = (LinearLayout) itemView.findViewById(R.id.layout6);
        for (int i=0;i<layouts.length;i++){
            layouts[i].setOnClickListener(this);
        }

        imageViews[0] = (ImageView) itemView.findViewById(R.id.cover1);
        imageViews[1] = (ImageView) itemView.findViewById(R.id.cover2);
        imageViews[2] = (ImageView) itemView.findViewById(R.id.cover3);
        imageViews[3] = (ImageView) itemView.findViewById(R.id.cover4);
        imageViews[4] = (ImageView) itemView.findViewById(R.id.cover5);
        imageViews[5] = (ImageView) itemView.findViewById(R.id.cover6);

        textViews[0] = (TextView) itemView.findViewById(R.id.name1);
        textViews[1] = (TextView) itemView.findViewById(R.id.name2);
        textViews[2] = (TextView) itemView.findViewById(R.id.name3);
        textViews[3] = (TextView) itemView.findViewById(R.id.name4);
        textViews[4] = (TextView) itemView.findViewById(R.id.name5);
        textViews[5] = (TextView) itemView.findViewById(R.id.name6);

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
                    homeCallBack.titleClick(Constant.TRANSLATE_TITLE);
                }
                break;

        }
    }
}
