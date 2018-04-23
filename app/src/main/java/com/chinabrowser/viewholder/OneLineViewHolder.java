package com.chinabrowser.viewholder;

import android.text.TextUtils;
import android.view.View;
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

public class OneLineViewHolder extends BaseViewHolder implements View.OnClickListener {
    TextView main_title,from,time;
    LinearLayout root;
    ImageView main_pic;

    TextView main_title2,from2,time2;
    LinearLayout root2;
    ImageView main_pic2;
    public OneLineViewHolder(View itemView) {
        super(itemView);
        main_title = (TextView) itemView.findViewById(R.id.main_title);
        from = (TextView) itemView.findViewById(R.id.from);
        time = (TextView) itemView.findViewById(R.id.time);
        root = (LinearLayout) itemView.findViewById(R.id.root);
        main_title2 = (TextView) itemView.findViewById(R.id.main_title2);
        from2 = (TextView) itemView.findViewById(R.id.from2);
        time2 = (TextView) itemView.findViewById(R.id.time2);
        main_pic = (ImageView) itemView.findViewById(R.id.main_pic);

        main_pic2 = (ImageView) itemView.findViewById(R.id.main_pic2);

        root2 = (LinearLayout) itemView.findViewById(R.id.root2);
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
                    GlideUtils.loadImageView(APP.getContext(),content.getCover_image(),main_pic);
                    time.setText(CommUtils.getTime(content.getTime()));
                }else if (i==1){
                    main_title2.setText(content.getTitle());
                    from2.setText(content.getCopy_from());
                    GlideUtils.loadImageView(APP.getContext(),content.getCover_image(),main_pic2);
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
                    homeCallBack.titleClick(Constant.TRA_TITLE,recommend.getMaintitle());
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
