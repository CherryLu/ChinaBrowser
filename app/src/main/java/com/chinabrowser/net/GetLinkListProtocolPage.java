package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.LinkData;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/18.
 */

public class GetLinkListProtocolPage extends BaseProtocolPage {
    public final static int MSG_WHAT_OK = 10096;
    public final static int MSG_WHAT_NOTCHANGE = 10097;
    public final static int MSG_WHAT_ERROE = 10098;

    public List<LinkData> linkDatas;


    public GetLinkListProtocolPage(Object param, Handler handler, BaseActivity activity) {
        super("http://119.23.56.48:8881/zt/api/zt_news", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "getlinklist";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpGetLinkData upGetLinkData = (UpGetLinkData) param;
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
            linkDatas = new ArrayList<>();
            JSONArray array = getJsonArray(response);
            JSONObject obj = JsonUtils.getJsonArray(array,0);
            JSONArray arrays = JsonUtils.getJSONArray(obj,"data");
            for (int i = 0;i<arrays.length();i++){
                JSONObject object = JsonUtils.getJsonArray(arrays,i);
                LinkData data = new LinkData();
                data.parse(object);
                linkDatas.add(data);
            }
        }
        return linkDatas;
    }
}
