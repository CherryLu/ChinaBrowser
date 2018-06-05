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
import com.chinabrowser.utils.LabManager;

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

        HomeTab homeTab = APP.curhomeTab;
        if (APP.homeTabs==null){
            APP.homeTabs = new ArrayList<>();
            APP.homeTabs.add(homeTab);
        }else {
            APP.homeTabs.set(APP.current,homeTab);
        }
       /* if (APP.homeTabs.size()>0){
            APP.homeTabs.remove(0);
            if (APP.homeTabs.size()>0){
                APP.homeTabs.add(0,homeTab);
            }else {
                APP.homeTabs.add(homeTab);
            }
        }else {
            APP.homeTabs.add(homeTab);
        }*/
        LabManager.getInstance().setCurrentLab(0);
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
    public void itemClick(int homeTab) {//跳转主页
        HomeTab tab = APP.homeTabs.get(homeTab);
        APP.current = homeTab;
        if (homeCallBack!=null){
            homeCallBack.fromLabs(tab);
        }
        /*if (homeTab==0){
          getActivity().onBackPressed();
        }else {
            if (homeCallBack!=null){
                homeCallBack.backClick();
            }
        }*/
    }

    @Override
    public void delClick(int homeTab) {
        APP.homeTabs.remove(homeTab);
        initList();
    }
}
