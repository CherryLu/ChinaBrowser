package com.chinabrowser.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinabrowser.R;
import com.chinabrowser.adapter.HomeAdapter;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.bean.TranslateEntity;
import com.chinabrowser.net.HomeProtocolPage;
import com.chinabrowser.net.UphomePageData;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.LogUtils;
import com.chinabrowser.utils.TranslateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 95470 on 2018/4/15.
 */

public class HomeFragment extends BaseFragment {

    @Bind(R.id.homelist)
    RecyclerView homelist;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HomeProtocolPage.MSG_WHAT_OK:
                    if (homeProtocolPage!=null&&homeProtocolPage.recommends!=null){
                        setList(homeProtocolPage.recommends);
                    }
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    private HomeProtocolPage homeProtocolPage;
    private UphomePageData uphomePageData;
    private void getData(){
        uphomePageData = new UphomePageData();
        uphomePageData.ilanguage = CommUtils.getCurrentLag(getContext())+1+"";
        if (homeProtocolPage==null){
            homeProtocolPage = new HomeProtocolPage(null,uphomePageData,handler,null);
        }
        homeProtocolPage.refresh(uphomePageData);
    }
    HomeAdapter homeAdapter;
    private void setList(List<Recommend> recommends){
        if (recommends!=null&&recommends.size()>0){
            homeAdapter = new HomeAdapter(getContext(),recommends);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            homelist.setLayoutManager(manager);
            homelist.setAdapter(homeAdapter);
        }else {
            homeAdapter = new HomeAdapter(getContext(),getRecommends());
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            homelist.setLayoutManager(manager);
            homelist.setAdapter(homeAdapter);
        }

    }

    private List<Recommend> getRecommends(){
        List<Recommend> recommends = new ArrayList<>();

        Recommend search = new Recommend();
        search.setType(Constant.SEARCHLAYOUT);
        recommends.add(search);

        Recommend labs = new Recommend();
        labs.setType(Constant.LABS);
        recommends.add(labs);

        Recommend translate = new Recommend();
        translate.setType(Constant.TRANSLATE);
        recommends.add(translate);

        Recommend bottom = new Recommend();
        bottom.setType(Constant.BOTTOM);
        recommends.add(bottom);

        return recommends;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);
        ButterKnife.bind(this, view);
        getData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
