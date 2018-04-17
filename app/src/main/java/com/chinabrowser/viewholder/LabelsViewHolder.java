package com.chinabrowser.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;

/**
 * Created by 95470 on 2018/4/15.
 */

public class LabelsViewHolder extends BaseViewHolder implements View.OnClickListener{
    LinearLayout[] linearLayouts = new LinearLayout[6];
    TextView[] textViews = new TextView[6];
    ImageView[] imageViews = new ImageView[6];

    public LabelsViewHolder(View itemView) {
        super(itemView);
        linearLayouts[0] = (LinearLayout) itemView.findViewById(R.id.layout1);
        linearLayouts[1] = (LinearLayout) itemView.findViewById(R.id.layout2);
        linearLayouts[2] = (LinearLayout) itemView.findViewById(R.id.layout3);
        linearLayouts[3] = (LinearLayout) itemView.findViewById(R.id.layout4);
        linearLayouts[4] = (LinearLayout) itemView.findViewById(R.id.layout5);
        linearLayouts[5] = (LinearLayout) itemView.findViewById(R.id.layout6);
        for (int i =0;i<linearLayouts.length;i++){
            linearLayouts[i].setOnClickListener(this);
        }
        textViews[0] = (TextView) itemView.findViewById(R.id.name1);
        textViews[1] = (TextView) itemView.findViewById(R.id.name2);
        textViews[2] = (TextView) itemView.findViewById(R.id.name3);
        textViews[3] = (TextView) itemView.findViewById(R.id.name4);
        textViews[4] = (TextView) itemView.findViewById(R.id.name5);
        textViews[5] = (TextView) itemView.findViewById(R.id.name6);

        imageViews[0] = (ImageView) itemView.findViewById(R.id.cover1);
        imageViews[1] = (ImageView) itemView.findViewById(R.id.cover2);
        imageViews[2] = (ImageView) itemView.findViewById(R.id.cover3);
        imageViews[3] = (ImageView) itemView.findViewById(R.id.cover4);
        imageViews[4] = (ImageView) itemView.findViewById(R.id.cover5);
        imageViews[5] = (ImageView) itemView.findViewById(R.id.cover6);
    }

    @Override
    public void setRecommend(Recommend recommend) {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.layout1:
                break;
            case R.id.layout2:
                break;
            case R.id.layout3:
                break;
            case R.id.layout4:
                break;
            case R.id.layout5:
                break;
            case R.id.layout6:
                break;

        }
    }
}
