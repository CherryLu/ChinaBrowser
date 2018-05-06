package com.chinabrowser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.adapter.CollectionAdapter;
import com.chinabrowser.bean.CollectionItem;
import com.chinabrowser.cbinterface.DelCollectionCallBack;
import com.chinabrowser.net.DelCollectionNewsPage;
import com.chinabrowser.net.GetCollectionListPage;
import com.chinabrowser.net.UpCollectionNews;
import com.chinabrowser.net.UpLoadCollectionList;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.UserManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/14.
 */

public class CollectionActivity extends BaseActivity implements DelCollectionCallBack{

    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.collection)
    RecyclerView collection;
    @Bind(R.id.nocollection)
    LinearLayout nocollection;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GetCollectionListPage.MSG_WHAT_OK:
                    if (getCollectionListPage!=null){
                        setListView(getCollectionListPage.collectionItems);
                    }else {
                        showNoCollection();
                    }

                    break;
                case GetCollectionListPage.MSG_WHAT_ERROE:
                case GetCollectionListPage.MSG_WHAT_NOTCHANGE:
                    showNoCollection();
                    break;
                case DelCollectionNewsPage.MSG_WHAT_ERROE:
                case DelCollectionNewsPage.MSG_WHAT_NOTCHANGE:
                        break;
                case DelCollectionNewsPage.MSG_WHAT_OK:
                        showToash("删除成功");
                        getData();
                        break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        ButterKnife.bind(this);
        title.setText(getResources().getText(R.string.setting_collection));
        getData();
    }

    private void showNoCollection(){
        nocollection.setVisibility(View.VISIBLE);
        collection.setVisibility(View.GONE);
    }

    GetCollectionListPage getCollectionListPage;
    UpLoadCollectionList upLoadCollectionList;
    private void getData(){
        upLoadCollectionList = new UpLoadCollectionList();
        upLoadCollectionList.ilanguage = CommUtils.getCurrentLag(this)+1+"";
        upLoadCollectionList.suserno = UserManager.getInstance().getUserId();
        if (getCollectionListPage==null){
            getCollectionListPage = new GetCollectionListPage(upLoadCollectionList,handler,null);
        }
        getCollectionListPage.refresh(upLoadCollectionList);
    }
    CollectionAdapter collectionAdapter;
    private void setListView(List<CollectionItem> collectionItems){
        if (collectionItems==null||collectionItems.size()<=0){
            nocollection.setVisibility(View.VISIBLE);
            collection.setVisibility(View.GONE);
        }else {
            nocollection.setVisibility(View.GONE);
            collection.setVisibility(View.VISIBLE);
            collectionAdapter = new CollectionAdapter(this,collectionItems);
            collectionAdapter.setCollectionCallBack(this);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            collection.setLayoutManager(manager);
            collection.setAdapter(collectionAdapter);
        }
    }



    @OnClick({R.id.back_image, R.id.nocollection})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.nocollection:
                break;
        }
    }

    @Override
    public void itemClick(CollectionItem collectionItem) {//点击
        if (collectionItem!=null){
            Navigator.startMainActivity(this,2,collectionItem.id);
        }


    }

    @Override
    public void swipClick(CollectionItem collectionItem) {//删除
        if (collectionItem!=null){
            delCollection(collectionItem.id);
        }

    }

    private void delCollection(String id){
        if (!TextUtils.isEmpty(id)){
            deleCollection(id);
        }
    }

    DelCollectionNewsPage delCollectionNewsPage;
    UpCollectionNews collectionNews;
    private void deleCollection(String id){
        collectionNews = new UpCollectionNews();
        collectionNews.suserno = UserManager.getInstance().getUserId();
        collectionNews.id = id;
        collectionNews.ilanguage = CommUtils.getCurrentLag(this)+1+"";

        if (delCollectionNewsPage==null){
            delCollectionNewsPage = new DelCollectionNewsPage(collectionNews,handler,this);
        }
        delCollectionNewsPage.refresh(collectionNews);

    }
}
