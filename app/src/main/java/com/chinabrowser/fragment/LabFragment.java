package com.chinabrowser.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.adapter.LabsManager;
import com.chinabrowser.bean.HomeTab;
import com.chinabrowser.cbinterface.HomeTabClick;
import com.chinabrowser.utils.LogUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/23.
 */

public class LabFragment extends BaseFragment implements HomeTabClick {


    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.recyclelist)
    RecyclerView recyclelist;
    @Bind(R.id.add)
    TextView add;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_labs, null);
        ButterKnife.bind(this, view);
        title.setText(getText(R.string.labs));
        if (APP.homeTabs==null){
            APP.homeTabs = new ArrayList<>();
            HomeTab homeTab = (HomeTab) getArguments().get("PAGE");
            APP.homeTabs.add(homeTab);
        }
        initList();
        return view;
    }

    private void initList(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclelist.setLayoutManager(manager);
        LabsManager adapter = new LabsManager(getContext(),APP.homeTabs);
        adapter.setHomeTabClick(this);
        recyclelist.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.back_image, R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                if (homeCallBack!=null){
                    homeCallBack.backClick();
                }
                break;
            case R.id.add:
            APP.homeTabs.add(APP.homeTab);
                initList();
                break;
        }
    }

    @Override
    public void itemClick(HomeTab homeTab) {//跳转主页

    }

    @Override
    public void delClick(HomeTab homeTab) {
        APP.homeTabs.remove(homeTab);
        initList();
    }
}
