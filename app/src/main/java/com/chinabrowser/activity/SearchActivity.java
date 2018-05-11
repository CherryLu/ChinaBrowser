package com.chinabrowser.activity;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.adapter.SearchRecommandAdapter;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.bean.SearchRecommand;
import com.chinabrowser.cbinterface.SearchItemClick;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/15.
 */

public class SearchActivity extends BaseActivity implements SearchItemClick {

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
        showSoftInputFromWindow(this,txtInput);
    }

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
            if (keywork.endsWith(".com")||keywork.endsWith(".info")||
                    keywork.endsWith(".net")||keywork.endsWith(".top")||
                    keywork.endsWith(".xyz")||keywork.endsWith(".club")||
                    keywork.endsWith(".vip")||keywork.endsWith(".shop")
                    ||keywork.endsWith(".wang")||keywork.endsWith(".cc")
                    ||keywork.endsWith(".ink")||keywork.endsWith(".ltd")
                    ||keywork.endsWith(".biz")||keywork.endsWith(".group")
                    ||keywork.endsWith(".link")||keywork.endsWith(".ren")
                    ||keywork.endsWith(".tv")||keywork.endsWith(".tr")){
                textright.setText(getText(R.string.web_into));
            }else {
                textright.setText(getText(R.string.web_search));
            }
        }
    }

    SearchRecommandAdapter recommandAdapter;
    private void setListView(){
        recommandAdapter = new SearchRecommandAdapter(this,getList());
        recommandAdapter.setItemClick(this);
        GridLayoutManager manager = new GridLayoutManager(this,5);
        list.setLayoutManager(manager);
        list.setAdapter(recommandAdapter);
    }

    private List<Content> getContents(List<Recommend> recommends){
        List<Content> contents = new ArrayList<>();
        if (recommends==null){
            return contents;
        }

        for (int i =0;i<recommends.size();i++){
            contents.addAll(recommends.get(i).getContents());
        }

        return contents;

    }

    private List<SearchRecommand> getList(){
        List<SearchRecommand> recommands = new ArrayList<>();
        List<Content> contents = getContents(APP.linkDatas);
        for (int i=0;i<contents.size();i++){
            Content content = contents.get(i);
            SearchRecommand recommand = new SearchRecommand();
            recommand.setName(content.getTitle());
            recommand.setImages(content.getCover_image());
            recommand.setUrl(content.getLink_url());
            recommands.add(recommand);
        }

        return recommands;
    }

    @Override
    public void ItemClick(SearchRecommand recommand) {
        Navigator.startMainActivity(this,1,recommand.getUrl());
        Navigator.finishActivity(this);
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
        String key = txtInput.getText().toString();
        if (getText(R.string.web_into).equals(str)){
            Navigator.startMainActivity(this,1,key);


        }else if (getText(R.string.web_search).equals(str)){
            String url = CommUtils.getsearchurl(this,key);
            Navigator.startMainActivity(this,1,url);

        }
        txtInput.setText("");
        Navigator.finishActivity(this);

    }
}
