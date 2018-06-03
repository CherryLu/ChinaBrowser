package com.chinabrowser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.adapter.LeftAdapter;
import com.chinabrowser.adapter.RightAdapter;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.bean.Title;
import com.chinabrowser.cbinterface.RightClick;
import com.chinabrowser.net.GetLinkListProtocolPage;
import com.chinabrowser.net.GetRecommandList;
import com.chinabrowser.net.SetRecommandList;
import com.chinabrowser.net.UpGetLinkData;
import com.chinabrowser.net.UpRecommand;
import com.chinabrowser.net.UpSetRecommand;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.UserManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 95470 on 2018/4/23.
 */

public class RecommandActivity extends BaseActivity implements RightClick {

    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.leftlist)
    RecyclerView leftlist;
    @Bind(R.id.rightlist)
    RecyclerView rightlist;

    private LeftAdapter leftAdapter;
    private RightAdapter rightAdapter;

    private List<Recommend> recommends;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GetLinkListProtocolPage.MSG_WHAT_OK:
                    if (getLinkListProtocolPage!=null){
                        APP.linkDatas =getLinkListProtocolPage.linkDatas;
                    }
                    recommends = APP.linkDatas;
                    APP.recommend = CommUtils.getHotRecommand(APP.linkDatas,getString(R.string.hot_recommnd));
                    recommends.set(0,APP.recommend);
                    initView();
                    break;
                case GetLinkListProtocolPage.MSG_WHAT_ERROE:
                    break;
                case GetLinkListProtocolPage.MSG_WHAT_NOTCHANGE:
                    break;
                case GetRecommandList.MSG_WHAT_NOTCHANGE:
                case GetRecommandList.MSG_WHAT_OK:
                    if (recommandList!=null&&recommandList.contents!=null&&recommandList.contents.size()>0){
                        if (recommandList.contents.size()>0){
                            Recommend recommend = new Recommend();
                            Title title = new Title();
                            title.setTitle_name(getString(R.string.hot_recommnd));
                            recommend.setMaintitle(title);
                            recommend.setContents(recommandList.contents);
                            APP.recommend = recommend;
                        }
                    }
                    recommends = APP.linkDatas;
                    recommends.set(0,APP.recommend);
                    initView();
                    break;
                case GetRecommandList.MSG_WHAT_ERROE:
                    break;
                case SetRecommandList.MSG_WHAT_OK:
                    hideWaitDialog();
                    break;
                case SetRecommandList.MSG_WHAT_ERROE:
                    hideWaitDialog();
                    break;
                case SetRecommandList.MSG_WHAT_NOTCHANGE:
                    hideWaitDialog();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomend);
        ButterKnife.bind(this);
        if (UserManager.getInstance().isLogin()){
            getRecommand();
        }else {
            getLinkList();
        }

        title.setText(getText(R.string.searech_recommend));
    }

    GetLinkListProtocolPage getLinkListProtocolPage;
    UpGetLinkData upGetLinkData;
    private void getLinkList(){
        upGetLinkData = new UpGetLinkData();
        upGetLinkData.ilanguage = CommUtils.getCurrentLag(this)+1+"";
        if (getLinkListProtocolPage==null){
            getLinkListProtocolPage = new GetLinkListProtocolPage(upGetLinkData,handler,null);
        }
        getLinkListProtocolPage.refresh(upGetLinkData);

    }


    GetRecommandList recommandList;
    UpRecommand recommand;

    private void getRecommand(){
        recommand = new UpRecommand();
        recommand.ilanguage = CommUtils.getCurrentLag(this)+1+"";
        recommand.suserno = UserManager.getInstance().getUserId();
        if (recommandList ==null){
            recommandList = new GetRecommandList(recommand,handler,null);
        }
        recommandList.refresh(recommand);
    }

    private void initView(){
        recommends.get(0).setSelect(true);
        leftAdapter = new LeftAdapter(this, recommends);
        leftAdapter.setRightClick(this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        leftlist.setLayoutManager(manager);
        leftlist.setAdapter(leftAdapter);

        rightAdapter = new RightAdapter(this, recommends.get(0).getContents());
        rightAdapter.setRightClick(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        rightlist.setLayoutManager(gridLayoutManager);
        rightlist.setAdapter(rightAdapter);


    }

    @OnClick(R.id.back_image)
    public void onClick() {
        Navigator.finishActivity(this);
    }

    @Override
    public void itemClick(Recommend recommend,int which) {
        rightAdapter = new RightAdapter(this, recommend.getContents());
        rightAdapter.setRightClick(this);
        if (which ==0){
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
            rightlist.setLayoutManager(gridLayoutManager);
            rightlist.setAdapter(rightAdapter);
        }else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
            rightlist.setLayoutManager(gridLayoutManager);
            rightlist.setAdapter(rightAdapter);
        }

    }

    @Override
    public void startUrl(Content content) {
        if (content!=null){
            Navigator.startMainActivity(this,1,content.getLink_url());
        }

    }
    Content deleteConment;
    Content addContent;
    @Override
    public void deleteContent(Content content) {//删除接口
        if (UserManager.getInstance().isLogin()){
            showWaitDialog("添加中...");
            deleteConment = content;
            setHotRecommand(CommUtils.getIds(content,0));
        }else {
            Navigator.startLoginActivity(this);
        }


    }

    @Override
    public void addContent(Content content) {
        showWaitDialog("添加中...");
        if (UserManager.getInstance().isLogin()){
            if (CommUtils.hasSame(content)){
                showToash("已添加");
            }else {
                addContent = content;
                setHotRecommand(CommUtils.getIds(content,1));
            }

        }else {
            Navigator.startLoginActivity(this);
        }

    }

    UpSetRecommand upSetRecommand;
    SetRecommandList setRecommandList;
    private void setHotRecommand(String ids){
        upSetRecommand = new UpSetRecommand();
        upSetRecommand.ilanguage = CommUtils.getCurrentLag(this)+1+"";
        upSetRecommand.suserno = UserManager.getInstance().getUserId();
        upSetRecommand.sset = ids;
        if (setRecommandList==null){
            setRecommandList = new SetRecommandList(upSetRecommand,handler,null);
        }

        setRecommandList.refresh(upSetRecommand);

    }
}
