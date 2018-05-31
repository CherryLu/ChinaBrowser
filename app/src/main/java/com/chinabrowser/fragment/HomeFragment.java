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
import android.widget.LinearLayout;

import com.chinabrowser.R;
import com.chinabrowser.adapter.HomeAdapter;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.cbinterface.HomeCallBack;
import com.chinabrowser.net.GetRecommandList;
import com.chinabrowser.net.HomeProtocolPage;
import com.chinabrowser.net.UpRecommand;
import com.chinabrowser.net.UphomePageData;
import com.chinabrowser.ui.DefineBAGRefreshWithLoadView;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;

/**
 * Created by 95470 on 2018/4/15.
 */

public class HomeFragment extends BaseFragment implements BGARefreshLayout.BGARefreshLayoutDelegate {
    HomeCallBack homeCallBack;

    @Bind(R.id.homelist)
    RecyclerView homelist;
    @Bind(R.id.bga_rl)
    BGARefreshLayout bgaRl;
    @Bind(R.id.no_content)
    LinearLayout noContent;
    private DefineBAGRefreshWithLoadView defineBAGRefreshWithLoadView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HomeProtocolPage.MSG_WHAT_OK:
                    if (homeProtocolPage != null && homeProtocolPage.recommends != null) {
                        setList(homeProtocolPage.recommends);
                    }
                    if (bgaRl != null) {
                        bgaRl.endRefreshing();
                    }
                    if (homeCallBack!=null){
                        homeCallBack.getPhoto();
                    }

                    break;
                case HomeProtocolPage.MSG_WHAT_ERROE:
                case HomeProtocolPage.MSG_WHAT_NOTCHANGE:
                    setList(null);
                    if (bgaRl != null) {
                        bgaRl.endRefreshing();
                    }

                    if (homeCallBack!=null){
                        homeCallBack.getPhoto();
                    }
                    break;
                case GetRecommandList.MSG_WHAT_NOTCHANGE:
                case GetRecommandList.MSG_WHAT_OK:
                    if (recommandList!=null&&recommandList.contents!=null){

                    }
                    break;
                case GetRecommandList.MSG_WHAT_ERROE:
                    break;

            }
            super.handleMessage(msg);
        }
    };

    public void setHomeCallBack(HomeCallBack homeCallBack) {
        this.homeCallBack = homeCallBack;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getRecommand();

    }

    private HomeProtocolPage homeProtocolPage;
    private UphomePageData uphomePageData;

    private void getData() {
        uphomePageData = new UphomePageData();
        uphomePageData.ilanguage = CommUtils.getCurrentLag(getContext()) + 1 + "";
        if (homeProtocolPage == null) {
            homeProtocolPage = new HomeProtocolPage(null, uphomePageData, handler, null);
        }
        homeProtocolPage.refresh(uphomePageData);
    }

    GetRecommandList recommandList;
    UpRecommand recommand;

    private void getRecommand(){
        recommand = new UpRecommand();
        recommand.ilanguage = CommUtils.getCurrentLag(getContext())+1+"";
        recommand.suserno = UserManager.getInstance().getUserId();
        if (recommandList ==null){
            recommandList = new GetRecommandList(recommand,handler,null);
        }
        recommandList.refresh(recommand);
    }

    HomeAdapter homeAdapter;

    private void setList(List<Recommend> recommends) {
        if (recommends != null && recommends.size() > 0) {
            bgaRl.setVisibility(View.VISIBLE);
            noContent.setVisibility(View.GONE);
            homeAdapter = new HomeAdapter(getContext(), recommends);
            homeAdapter.setHomeCallBack(homeCallBack);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            homelist.setLayoutManager(manager);
            homelist.setAdapter(homeAdapter);
        } else {
            bgaRl.setVisibility(View.GONE);
            noContent.setVisibility(View.VISIBLE);
            noContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getData();
                }
            });
        }

    }

    private List<Recommend> getRecommends() {
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
        homelist = (RecyclerView) view.findViewById(R.id.homelist);
        getData();
        setBgaRefreshLayout();
        return view;
    }

    private void setBgaRefreshLayout() {
        defineBAGRefreshWithLoadView = new DefineBAGRefreshWithLoadView(getContext(), false, true);
        bgaRl.setRefreshViewHolder(defineBAGRefreshWithLoadView);
        bgaRl.setDelegate(this);
        defineBAGRefreshWithLoadView.updateLoadingMoreText("加载更多");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
