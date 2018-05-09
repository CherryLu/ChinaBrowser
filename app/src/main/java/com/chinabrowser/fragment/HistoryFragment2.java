package com.chinabrowser.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chinabrowser.R;
import com.chinabrowser.adapter.HistoryAdapter;
import com.chinabrowser.bean.History;
import com.chinabrowser.db.HistoryManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/14.
 */

public class HistoryFragment2 extends BaseFragment {

    @Bind(R.id.recycleview)
    RecyclerView recycleview;
    HistoryAdapter historyAdapter;
    List<History> histories;
    @Bind(R.id.nohistory)
    LinearLayout nohistory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_history, null);
        ButterKnife.bind(this, rootView);
        histories = HistoryManager.getInstance().queryDataWeek();
        if (histories!=null&&histories.size()>0){
            nohistory.setVisibility(View.GONE);
            recycleview.setVisibility(View.VISIBLE);

            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            historyAdapter = new HistoryAdapter(getContext(), histories);
            recycleview.setLayoutManager(manager);
            recycleview.setAdapter(historyAdapter);
        }else {
            nohistory.setVisibility(View.VISIBLE);
            recycleview.setVisibility(View.GONE);
        }


        return rootView;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
