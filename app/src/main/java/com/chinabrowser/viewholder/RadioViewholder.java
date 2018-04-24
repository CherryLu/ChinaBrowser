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

import cn.anyradio.manager.PlayManager;

/**
 * Created by 95470 on 2018/4/15.
 */

public class RadioViewholder extends BaseViewHolder implements View.OnClickListener {
    LinearLayout[] layouts = new LinearLayout[6];
    ImageView[] covers = new ImageView[6];
    ImageView[] plays = new ImageView[6];
    TextView[] names = new TextView[6];
    boolean[] isplay = new boolean[6];
    public RadioViewholder(View itemView) {
        super(itemView);
        for (int i =0;i<isplay.length;i++){
            isplay[i] = false;
        }
        layouts[0] = (LinearLayout) itemView.findViewById(R.id.layout1);
        layouts[1] = (LinearLayout) itemView.findViewById(R.id.layout2);
        layouts[2] = (LinearLayout) itemView.findViewById(R.id.layout3);
        layouts[3] = (LinearLayout) itemView.findViewById(R.id.layout4);
        layouts[4] = (LinearLayout) itemView.findViewById(R.id.layout5);
        layouts[5] = (LinearLayout) itemView.findViewById(R.id.layout6);

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
        setLinten();
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
            layouts[i].setVisibility(View.VISIBLE);
            Content content = contents.get(i);
            names[i].setText(content.getTitle());
            GlideUtils.loadImageView(APP.getContext(),content.getCover_image(),covers[i]);
        }
    }

    private void reset(){
        for (int i =0;i<plays.length;i++){
            plays[i].setImageResource(R.mipmap.radio_play);
        }
    }

    private void setLinten(){
        for (int i =0;i<plays.length;i++){
            plays[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.paly1:
                if (recommend.getContents().size()<1){
                    return;
                }
                if (recommend.getContents().get(0)!=null){
                    reset();
                    if (!isplay[0]){
                        isplay[0] = true;
                        PlayManager.getInstance(v.getContext()).play(recommend.getContents().get(0).getTitle(),recommend.getContents().get(0).getLink_url(),recommend.getContents().get(0).getId(),2);
                        plays[0].setImageResource(R.mipmap.radio_stop);
                    }else {
                        isplay[0] = false;
                        PlayManager.getInstance(v.getContext()).stop();
                        plays[0].setImageResource(R.mipmap.radio_play);
                    }
                }
                break;
            case R.id.paly2:
                if (recommend.getContents().size()<2){
                    return;
                }
                if (recommend.getContents().get(1)!=null){
                    reset();
                    if (!isplay[1]){
                        isplay[1] = true;
                        PlayManager.getInstance(v.getContext()).play(recommend.getContents().get(1).getTitle(),recommend.getContents().get(1).getLink_url(),recommend.getContents().get(1).getId(),2);
                        plays[1].setImageResource(R.mipmap.radio_stop);
                    }else {
                        isplay[1] = false;
                        PlayManager.getInstance(v.getContext()).stop();
                        plays[1].setImageResource(R.mipmap.radio_play);
                    }
                }

                break;
            case R.id.paly3:
                if (recommend.getContents().size()<3){
                    return;
                }
                if (recommend.getContents().get(3)!=null){
                    reset();
                    if (!isplay[2]){
                        isplay[2] = true;
                        PlayManager.getInstance(v.getContext()).play(recommend.getContents().get(2).getTitle(),recommend.getContents().get(2).getLink_url(),recommend.getContents().get(2).getId(),2);
                        plays[2].setImageResource(R.mipmap.radio_stop);
                    }else {
                        isplay[2] = false;
                        PlayManager.getInstance(v.getContext()).stop();
                        plays[2].setImageResource(R.mipmap.radio_play);
                    }
                }
                break;
            case R.id.paly4:
                if (recommend.getContents().size()<4){
                    return;
                }
                if (recommend.getContents().get(3)!=null){
                    reset();
                    if (!isplay[3]){
                        isplay[3] = true;
                        PlayManager.getInstance(v.getContext()).play(recommend.getContents().get(3).getTitle(),recommend.getContents().get(3).getLink_url(),recommend.getContents().get(3).getId(),2);
                        plays[3].setImageResource(R.mipmap.radio_stop);
                    }else {
                        isplay[3] = false;
                        PlayManager.getInstance(v.getContext()).stop();
                        plays[3].setImageResource(R.mipmap.radio_play);
                    }
                }
                break;
            case R.id.paly5:
                if (recommend.getContents().size()<5){
                    return;
                }
                if (recommend.getContents().get(4)!=null){
                    reset();
                    if (!isplay[4]){
                        isplay[4] = true;
                        PlayManager.getInstance(v.getContext()).play(recommend.getContents().get(4).getTitle(),recommend.getContents().get(4).getLink_url(),recommend.getContents().get(4).getId(),2);
                        plays[4].setImageResource(R.mipmap.radio_stop);
                    }else {
                        isplay[4] = false;
                        PlayManager.getInstance(v.getContext()).stop();
                        plays[4].setImageResource(R.mipmap.radio_play);
                    }
                }
                break;
            case R.id.paly6:
                if (recommend.getContents().size()<6){
                    return;
                }
                if (recommend.getContents().get(5)!=null){
                    reset();
                    if (!isplay[5]){
                        isplay[5] = true;
                        PlayManager.getInstance(v.getContext()).play(recommend.getContents().get(5).getTitle(),recommend.getContents().get(5).getLink_url(),recommend.getContents().get(5).getId(),2);
                        plays[5].setImageResource(R.mipmap.radio_stop);
                    }else {
                        isplay[5] = false;
                        PlayManager.getInstance(v.getContext()).stop();
                        plays[5].setImageResource(R.mipmap.radio_play);
                    }
                }
                break;
        }
    }
}
