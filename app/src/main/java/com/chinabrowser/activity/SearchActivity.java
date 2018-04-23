package com.chinabrowser.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.adapter.SearchRecommandAdapter;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.SearchRecommand;
import com.chinabrowser.utils.CommUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/15.
 */

public class SearchActivity extends BaseActivity {

    @Bind(R.id.txt_input)
    EditText txtInput;
    @Bind(R.id.textright)
    TextView textright;
    @Bind(R.id.list)
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        textright.setVisibility(View.GONE);
        initLis();
        setListView();

    }
    private void initLis(){
        txtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                showRight(keyword);
            }
        });
    }

    private void showRight(String keywork){
        if (!TextUtils.isEmpty(keywork)){
            textright.setVisibility(View.VISIBLE);
            if (keywork.endsWith(".com")||keywork.endsWith(".cn")){
                textright.setText("跳转");
            }else {
                textright.setText("搜索");
            }
        }
    }

    SearchRecommandAdapter recommandAdapter;
    private void setListView(){
        recommandAdapter = new SearchRecommandAdapter(this,getList());
        GridLayoutManager manager = new GridLayoutManager(this,5);
        list.setLayoutManager(manager);
        list.setAdapter(recommandAdapter);
    }

    private List<SearchRecommand> getList(){
        List<SearchRecommand> recommands = new ArrayList<>();
        for (int i =0;i<11;i++){
            SearchRecommand recommand = new SearchRecommand();
            switch (i){
                case 0:
                    recommand.setName("淘宝");
                    recommand.setImages(R.mipmap.taobao);
                    recommands.add(recommand);
                    break;
                case 1:
                    recommand.setName("网易");
                    recommand.setImages(R.mipmap.wangyi);
                    recommands.add(recommand);
                    break;
                case 2:
                    recommand.setName("美团");
                    recommand.setImages(R.mipmap.meituan);
                    recommands.add(recommand);
                    break;
                case 3:
                    recommand.setName("优酷");
                    recommand.setImages(R.mipmap.youku);
                    recommands.add(recommand);
                    break;
                case 4:
                    recommand.setName("知乎");
                    recommand.setImages(R.mipmap.zhihu);
                    recommands.add(recommand);
                    break;
                case 5:
                    recommand.setName("京东");
                    recommand.setImages(R.mipmap.jingdong);
                    recommands.add(recommand);
                    break;
                case 6:
                    recommand.setName("腾讯视频");
                    recommand.setImages(R.mipmap.tengxun);
                    recommands.add(recommand);
                    break;
            }

        }

        return recommands;
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration{


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = CommUtils.dip2px(SearchActivity.this,10);
            outRect.right = CommUtils.dip2px(SearchActivity.this,10);
            outRect.bottom = CommUtils.dip2px(SearchActivity.this,10);
            //outRect.top = CommUtils.dip2px(SearchActivity.this,6);

        }
    }

    @OnClick(R.id.textright)
    public void onClick() {
        String str = textright.getText().toString();
        if ("跳转".endsWith(str)){

        }else if ("搜索".endsWith(str)){
            String url = CommUtils.getsearchurl(this,str);
            showToash(url);
        }

    }
}
