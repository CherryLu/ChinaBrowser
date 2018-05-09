package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.NewsData;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class GetHotNewsProtocolPage extends BaseProtocolPage {
    public final static int MSG_WHAT_OK = 10016;
    public final static int MSG_WHAT_NOTCHANGE = 10017;
    public final static int MSG_WHAT_ERROE = 10018;

    public List<NewsData> newsDatas;



    public GetHotNewsProtocolPage(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_news", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "getlist";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpLoadGetHot upGetLinkData = (UpLoadGetHot) param;
            return upGetLinkData.getUploadString();
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
        if (response!=null){
            JSONArray array = getJsonArray(response);
            JSONObject object = JsonUtils.getJsonArray(array,0);
            JSONArray jsonArray = JsonUtils.getJSONArray(object,"data");
            newsDatas = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
                JSONObject obj = JsonUtils.getJsonArray(jsonArray,i);
                NewsData newsData = new NewsData();
                newsData.parse(obj);
                newsDatas.add(newsData);
            }
        }

        return newsDatas;
    }
}
