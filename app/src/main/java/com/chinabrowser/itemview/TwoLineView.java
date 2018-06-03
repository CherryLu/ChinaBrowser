package com.chinabrowser.itemview;

import android.content.Context;
import android.text.TextUtils;
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
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.GlideUtils;

import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class TwoLineView extends BaseView implements View.OnClickListener {

    TextView main_title,from,time;
    ImageView pic;
    LinearLayout root;
    TextView main_title2,from2,time2;
    ImageView pic2;

    LinearLayout root2;

    Context mContext;

    public TwoLineView(Context context, ViewGroup parent) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVivew =inflater.inflate(R.layout.twoline_pic_item,parent,false);

        title = (TextView) mVivew.findViewById(R.id.title);
        more = (ImageView) mVivew.findViewById(R.id.more);

        main_title = (TextView) mVivew.findViewById(R.id.main_title);
        from = (TextView) mVivew.findViewById(R.id.from);
        time = (TextView) mVivew.findViewById(R.id.time);
        pic = (ImageView) mVivew.findViewById(R.id.pic);
        root = (LinearLayout) mVivew.findViewById(R.id.root);
        root.setOnClickListener(this);
        main_title2 = (TextView) mVivew.findViewById(R.id.main_title2);
        from2 = (TextView) mVivew.findViewById(R.id.from2);
        time2 = (TextView) mVivew.findViewById(R.id.time2);
        pic2 = (ImageView) mVivew.findViewById(R.id.pic2);
        root2 = (LinearLayout) mVivew.findViewById(R.id.root2);
        root2.setOnClickListener(this);

        more.setOnClickListener(this);
    }

    @Override
    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
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

            List<Content> contents = recommend.getContents();
            for (int i=0;i<contents.size();i++){
                Content content = contents.get(i);
                if (i==0){
                    main_title.setText(content.getTitle());
                    from.setText(content.getCopy_from());
                    GlideUtils.loadImageView(APP.getContext(),content.getCover_image(),pic);
                    time.setText(CommUtils.getTime(content.getTime()));
                }else if (i==1){
                    main_title2.setText(content.getTitle());
                    from2.setText(content.getCopy_from());
                    GlideUtils.loadImageView(APP.getContext(),content.getCover_image(),pic2);
                    time2.setText(CommUtils.getTime(content.getTime()));
                }
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.root:
                if (homeCallBack!=null){
                    homeCallBack.startContent(recommend.getContents().get(0));
                }
                break;
            case R.id.more:
                if (homeCallBack!=null){
                    if ("热点".equals(title.getText().toString())){
                        homeCallBack.titleClick(Constant.HOT_TITLE,recommend.getMaintitle());
                    }else {
                        homeCallBack.titleClick(Constant.CHINA_TITLE,recommend.getMaintitle());
                    }
                }
                break;
            case R.id.root2:
                if (homeCallBack!=null){
                    homeCallBack.startContent(recommend.getContents().get(1));
                }
                break;
        }
    }
}
