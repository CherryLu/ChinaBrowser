package com.chinabrowser.viewholder;

import android.view.View;
import android.widget.LinearLayout;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.utils.Constant;

/**
 * Created by 95470 on 2018/4/15.
 */

public class SearchViewHolder extends BaseViewHolder implements View.OnClickListener{

    LinearLayout linearLayout;
    public SearchViewHolder(View itemView) {
        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.seaecharea);
        linearLayout.setOnClickListener(this);
    }

    @Override
    public void setRecommend(Recommend recommend) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.seaecharea:
                if (homeCallBack!=null){
                    homeCallBack.titleClick(Constant.SEARCHLAYOUT,null);
                }
                break;
        }

    }
}
