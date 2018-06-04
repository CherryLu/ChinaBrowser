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
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.adapter.NewsAdapter;
import com.chinabrowser.adapter.TwoLineNewsAdapter;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.NewsData;
import com.chinabrowser.bean.Title;
import com.chinabrowser.cbinterface.PagerClick;
import com.chinabrowser.net.GetHotNewsProtocolPage;
import com.chinabrowser.net.UpLoadGetHot;
import com.chinabrowser.utils.CommUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 95470 on 2018/4/18.
 */

public class HotNewsFragment extends BaseFragment implements PagerClick {


    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.list)
    RecyclerView list;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GetHotNewsProtocolPage.MSG_WHAT_OK:
                    if (getHotNewsProtocolPage != null) {
                        setList(getHotNewsProtocolPage.newsDatas);
                    }
                    break;
                case GetHotNewsProtocolPage.MSG_WHAT_ERROE:
                case GetHotNewsProtocolPage.MSG_WHAT_NOTCHANGE:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    Title ti;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotnews, null);
        ButterKnife.bind(this, view);
        ti = (Title) getArguments().getSerializable("TITLE");
        title.setText(ti.getTitle_name());
        getData();
        return view;
    }

    GetHotNewsProtocolPage getHotNewsProtocolPage;
    UpLoadGetHot upLoadGetHot;

    private void getData() {
        upLoadGetHot = new UpLoadGetHot();
        upLoadGetHot.ilanguage = CommUtils.getCurrentLag(getContext()) + 1 + "";
        upLoadGetHot.catalog_id = ti.getCatalog_id();
        upLoadGetHot.pageindex = "1";
        if (getHotNewsProtocolPage == null) {
            getHotNewsProtocolPage = new GetHotNewsProtocolPage(upLoadGetHot, handler, null);
        }
        getHotNewsProtocolPage.refresh(upLoadGetHot);
    }

    NewsAdapter newsAdapter;
    TwoLineNewsAdapter lineNewsAdapter;
    private void setList(List<NewsData> newsDatas) {
        if (getString(R.string.travel).equals(ti.getTitle_name())){
            newsAdapter = new NewsAdapter(getContext(), newsDatas);
            newsAdapter.setPagerClick(this);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(manager);
            list.setAdapter(newsAdapter);
        }else {
            lineNewsAdapter = new TwoLineNewsAdapter(getContext(), newsDatas);
            lineNewsAdapter.setPagerClick(this);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            list.setLayoutManager(manager);
            list.setAdapter(lineNewsAdapter);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            // remove掉保存的Fragment
            String FRAGMENTS_TAG = "android:support:fragments";
            outState.remove(FRAGMENTS_TAG);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.back_image)
    public void onClick() {
        if (homeCallBack!=null){
            homeCallBack.backClick();
        }
    }

    @Override
    public void pagerClick(int position) {
        if (homeCallBack!=null){
         NewsData newsData  = getHotNewsProtocolPage.newsDatas.get(position);
            Content content = new Content();
            content.setId(newsData.id);
            homeCallBack.startContent(content);
        }
    }
}
