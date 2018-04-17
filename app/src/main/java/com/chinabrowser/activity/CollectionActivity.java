package com.chinabrowser.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.utils.Navigator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CollectionActivity extends BaseActivity {

    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.collection)
    RecyclerView collection;
    @Bind(R.id.nocollection)
    LinearLayout nocollection;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        title.setText(getResources().getText(R.string.setting_collection));
    }

    @OnClick({R.id.back_image, R.id.nocollection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.nocollection:
                break;
        }
    }
}
