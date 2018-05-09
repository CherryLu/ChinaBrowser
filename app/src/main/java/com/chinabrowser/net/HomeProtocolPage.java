package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.APP;
import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95470 on 2018/4/16.
 */

public class HomeProtocolPage extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 10086;
    public final static int MSG_WHAT_NOTCHANGE = 10087;
    public final static int MSG_WHAT_ERROE = 10088;

    public List<Recommend> recommends;

    public HomeProtocolPage(String url, Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_news", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "gethomepage";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UphomePageData pageData = (UphomePageData) param;
            return pageData.getUploadString();
        }
        return "";
    }

    @Override
    public boolean supportCacheFile() {
        return false;
    }

    @Override
    public int getMsgWhatOk() {
        return MSG_WHAT_OK;
    }

    @Override
    public int getMsgWhatError() {
        return MSG_WHAT_ERROE;
    }

    @Override
    public int getMsgWhatDataNotChange() {
        return MSG_WHAT_NOTCHANGE;
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public Object parserJson(byte[] response) {
        recommends = new ArrayList<>();
        if (response==null){
            return null;
        }
        JSONArray array = getJsonArray(response);
        Recommend search = new Recommend();
        search.setType(Constant.SEARCHLAYOUT);
        Recommend labs = new Recommend();
        labs.setType(Constant.LABS);
        labs.setContents(getContents(APP.linkDatas));
        recommends.add(search);
        recommends.add(labs);
        if (array!=null){
            for (int i =0;i<array.length();i++){
                JSONObject object = JsonUtils.getJsonArray(array,i);
                Recommend recommend = new Recommend();
                recommend.parse(object);
                recommends.add(recommend);
            }
        }

        Recommend translate = new Recommend();
        translate.setType(Constant.TRANSLATE);
        recommends.add(translate);
        Recommend bottom = new Recommend();
        bottom.setType(Constant.BOTTOM);
        recommends.add(bottom);
        return recommends;
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
}
