package com.chinabrowser.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.utils.Constant;

/**
 * Created by 95470 on 2018/4/15.
 */

public class TwoLineViewHolder extends BaseViewHolder implements View.OnClickListener {

    TextView main_title,from,time;
    ImageView pic;
    LinearLayout root;
    public TwoLineViewHolder(View itemView) {
        super(itemView);
        main_title = (TextView) itemView.findViewById(R.id.main_title);
        from = (TextView) itemView.findViewById(R.id.from);
        time = (TextView) itemView.findViewById(R.id.time);
        pic = (ImageView) itemView.findViewById(R.id.pic);
        root = (LinearLayout) itemView.findViewById(R.id.root);
        root.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    @Override
    public void setRecommend(Recommend recommend) {
        if (recommend!=null&&recommend.getMaintitle()!=null){
            String str_title = recommend.getMaintitle().getTitle_name();
            if (!TextUtils.isEmpty(str_title)){
                if (title.getResources().getText(R.string.travel).equals(str_title)){
                    title.setText(str_title);
                    title.setBackgroundResource(R.mipmap.travel);
                }else{
                    title.setText(str_title);
                    title.setBackgroundResource(R.mipmap.chinanet);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.root:
            case R.id.more:
                if (homeCallBack!=null){
                    if ("热点".equals(title.getText().toString())){
                        homeCallBack.titleClick(Constant.HOT_TITLE);
                    }else {
                        homeCallBack.titleClick(Constant.CHINA_TITLE);
                    }
                }
                break;
        }
    }
}
