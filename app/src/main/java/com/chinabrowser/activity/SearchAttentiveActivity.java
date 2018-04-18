package com.chinabrowser.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Navigator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 95470 on 2018/4/15.
 */

public class SearchAttentiveActivity extends BaseActivity {

    @Bind(R.id.selected1)
    ImageView selected1;
    @Bind(R.id.search1)
    LinearLayout search1;
    @Bind(R.id.selected2)
    ImageView selected2;
    @Bind(R.id.search2)
    LinearLayout search2;
    @Bind(R.id.selected3)
    ImageView selected3;
    @Bind(R.id.search3)
    LinearLayout search3;
    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    int result ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchattentive);
        ButterKnife.bind(this);
        title.setText(getResources().getText(R.string.attentive));
        result = CommUtils.getCurrentSearch(this);
        setDefaltSearch(result);
    }

    private void setDefaltSearch(int position){

        selected1.setVisibility(View.GONE);
        selected2.setVisibility(View.GONE);
        selected3.setVisibility(View.GONE);

        if (position==0){
            selected1.setVisibility(View.VISIBLE);
        }else if (position==1){
            selected2.setVisibility(View.VISIBLE);
        }else if (position==2){
            selected3.setVisibility(View.VISIBLE);
        }
        result = position;
        CommUtils.setCurrentSearch(this,position);
    }
    @OnClick({R.id.search1, R.id.search2, R.id.search3,R.id.back_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search1:
                setDefaltSearch(0);
                break;
            case R.id.search2:
                setDefaltSearch(1);
                break;
            case R.id.search3:
                setDefaltSearch(2);
                break;
            case R.id.back_image:
                setResult(result);
                Navigator.finishActivity(this);
                break;
        }
    }

}
