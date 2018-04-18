package com.chinabrowser.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.net.GetHotNewsProtocolPage;
import com.chinabrowser.net.UpLoadGetHot;
import com.chinabrowser.utils.CommUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 95470 on 2018/4/18.
 */

public class HotNewsFragment extends BaseFragment {


    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.list)
    RecyclerView list;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){

            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotnews, null);
        ButterKnife.bind(this, view);
        getData();
        return view;
    }
    GetHotNewsProtocolPage getHotNewsProtocolPage;
    UpLoadGetHot upLoadGetHot;
    private void getData(){
        upLoadGetHot = new UpLoadGetHot();
        upLoadGetHot.ilanguage = CommUtils.getCurrentLag(getContext())+1+"";
        upLoadGetHot.catalog_id = "1";
        upLoadGetHot.pageindex = "1";
        if (getHotNewsProtocolPage==null){
            getHotNewsProtocolPage = new GetHotNewsProtocolPage(upLoadGetHot,handler,null);
        }
        getHotNewsProtocolPage.refresh(upLoadGetHot);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
