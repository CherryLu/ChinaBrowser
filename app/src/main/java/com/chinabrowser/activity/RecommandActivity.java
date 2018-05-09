package com.chinabrowser.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.adapter.LeftAdapter;
import com.chinabrowser.adapter.RightAdapter;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.cbinterface.RightClick;
import com.chinabrowser.utils.Navigator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 95470 on 2018/4/23.
 */

public class RecommandActivity extends BaseActivity implements RightClick {

    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.leftlist)
    RecyclerView leftlist;
    @Bind(R.id.rightlist)
    RecyclerView rightlist;

    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomend);
        ButterKnife.bind(this);
        initView();
        title.setText(getText(R.string.searech_recommend));
    }

    private void initView(){
        leftAdapter = new LeftAdapter(this, APP.linkDatas);
        leftAdapter.setRightClick(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        leftlist.setLayoutManager(manager);
        leftlist.setAdapter(leftAdapter);

        rightAdapter = new RightAdapter(this, APP.linkDatas.get(0).getContents());
        rightAdapter.setRightClick(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rightlist.setLayoutManager(gridLayoutManager);
        rightlist.setAdapter(rightAdapter);


    }

    @OnClick(R.id.back_image)
    public void onClick() {
        Navigator.finishActivity(this);
    }

    @Override
    public void itemClick(Recommend recommend) {
        rightAdapter = new RightAdapter(this, recommend.getContents());
        rightAdapter.setRightClick(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rightlist.setLayoutManager(gridLayoutManager);
        rightlist.setAdapter(rightAdapter);
    }

    @Override
    public void startUrl(Content content) {
        if (content!=null){
            Navigator.startMainActivity(this,1,content.getLink_url());
        }

    }
}
