package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.Recommend;
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

    public List<Recommend> linkDatas;


    public GetLinkListProtocolPage(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_news", param, handler, activity);
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
            for (int j =0;j<array.length();j++){
                JSONObject obj = JsonUtils.getJsonArray(array,j);
                Recommend data = new Recommend();
                data.parse(obj);
                linkDatas.add(data);
            }

        }
        return linkDatas;
    }
}
