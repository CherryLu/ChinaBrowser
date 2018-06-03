package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.Content;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/29.
 */

public class GetRecommandList extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 10035;
    public final static int MSG_WHAT_NOTCHANGE = 10036;
    public final static int MSG_WHAT_ERROE = 10037;

    public List<Content> contents;

    public GetRecommandList(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_news", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "getlinklistdefault";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpRecommand upRecommand = (UpRecommand) param;
            return upRecommand.getUploadString();
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
            contents = new ArrayList<>();
            JSONArray array = getJsonArray(response);
            JSONObject obj = JsonUtils.getJsonArray(array,0);
            JSONArray jsonArray = JsonUtils.getJSONArray(obj,"data");
            for (int i = 0;i<jsonArray.length();i++){
                Content content = new Content();
                content.parse(JsonUtils.getJsonArray(array,i));
                contents.add(content);
            }
        }
        return contents;
    }
}
