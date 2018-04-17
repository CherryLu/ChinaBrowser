package com.chinabrowser.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.bean.Recommend;

/**
 * Created by 95470 on 2018/4/15.
 */

public class TitleViewHolder extends BaseViewHolder implements View.OnClickListener{
    TextView title;
    ImageView more;
    public TitleViewHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        more = (ImageView) itemView.findViewById(R.id.more);
    }

    @Override
    public void setRecommend(Recommend recommend) {

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more:
                break;

        }
    }
}
