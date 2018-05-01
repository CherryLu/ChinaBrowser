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
import com.chinabrowser.utils.LogUtils;
import com.chinabrowser.utils.Navigator;

import java.util.List;

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

       textViews[5].setText(itemView.getResources().getText(R.string.more_text));

        imageViews[0] = (ImageView) itemView.findViewById(R.id.cover1);
        imageViews[1] = (ImageView) itemView.findViewById(R.id.cover2);
        imageViews[2] = (ImageView) itemView.findViewById(R.id.cover3);
        imageViews[3] = (ImageView) itemView.findViewById(R.id.cover4);
        imageViews[4] = (ImageView) itemView.findViewById(R.id.cover5);
        imageViews[5] = (ImageView) itemView.findViewById(R.id.cover6);
    }

    @Override
    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
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
                    LogUtils.e("LABES",datas.get(i).getImage_url());
                    LogUtils.e("LABES",datas.get(i).getTitle());
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
                    homeCallBack.startContentByurl(recommend.getContents().get(0));
                }
                break;
            case R.id.layout2:
                if (homeCallBack!=null){
                    homeCallBack.startContentByurl(recommend.getContents().get(1));
                }
                break;
            case R.id.layout3:
                if (homeCallBack!=null){
                    homeCallBack.startContentByurl(recommend.getContents().get(2));
                }
                break;
            case R.id.layout4:
                if (homeCallBack!=null){
                    homeCallBack.startContentByurl(recommend.getContents().get(3));
                }
                break;
            case R.id.layout5:
                if (homeCallBack!=null){
                    homeCallBack.startContentByurl(recommend.getContents().get(4));
                }
                break;
            case R.id.layout6:
                Navigator.startRecommandActivity(v.getContext());
                break;

        }
    }
}
