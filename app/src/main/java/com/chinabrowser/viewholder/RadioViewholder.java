package com.chinabrowser.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.utils.GlideUtils;

import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class RadioViewholder extends BaseViewHolder implements View.OnClickListener {
    LinearLayout[] layouts = new LinearLayout[6];
    ImageView[] covers = new ImageView[6];
    ImageView[] plays = new ImageView[6];
    TextView[] names = new TextView[6];
    public RadioViewholder(View itemView) {
        super(itemView);
        covers[0] = (ImageView) itemView.findViewById(R.id.main_pic1);
        covers[1] = (ImageView) itemView.findViewById(R.id.main_pic2);
        covers[2] = (ImageView) itemView.findViewById(R.id.main_pic3);
        covers[3] = (ImageView) itemView.findViewById(R.id.main_pic4);
        covers[4] = (ImageView) itemView.findViewById(R.id.main_pic5);
        covers[5] = (ImageView) itemView.findViewById(R.id.main_pic6);

        plays[0] = (ImageView) itemView.findViewById(R.id.paly1);
        plays[1] = (ImageView) itemView.findViewById(R.id.paly2);
        plays[2] = (ImageView) itemView.findViewById(R.id.paly3);
        plays[3] = (ImageView) itemView.findViewById(R.id.paly4);
        plays[4] = (ImageView) itemView.findViewById(R.id.paly5);
        plays[5] = (ImageView) itemView.findViewById(R.id.paly6);

        names[0] = (TextView) itemView.findViewById(R.id.name1);
        names[1] = (TextView) itemView.findViewById(R.id.name2);
        names[2] = (TextView) itemView.findViewById(R.id.name3);
        names[3] = (TextView) itemView.findViewById(R.id.name4);
        names[4] = (TextView) itemView.findViewById(R.id.name5);
        names[5] = (TextView) itemView.findViewById(R.id.name6);
        more.setVisibility(View.GONE);
        for (int i=0;i<plays.length;i++){
            plays[i].setOnClickListener(this);
        }
    }

    @Override
    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
        title.setBackgroundResource(R.mipmap.radio);
        title.setText("电台");
        List<Content> contents = recommend.getContents();
        int  length = contents.size();
       if (length>=6){
           length = 6;
       }
        for (int i = 0;i<length;i++){
            Content content = contents.get(i);
            names[i].setText(content.getTitle());
            GlideUtils.loadImageView(APP.getContext(),content.getCover_image(),covers[i]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paly1:
                break;
            case R.id.paly2:
                break;
            case R.id.paly3:
                break;
            case R.id.paly4:
                break;
            case R.id.paly5:
                break;
            case R.id.paly6:
                break;
        }
    }
}
