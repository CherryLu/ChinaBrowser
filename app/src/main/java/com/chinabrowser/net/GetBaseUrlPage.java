package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.BaseUrl;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95470 on 2018/4/23.
 */

public class GetBaseUrlPage extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 1006;
    public final static int MSG_WHAT_NOTCHANGE = 1007;
    public final static int MSG_WHAT_ERROE = 1008;

    public GetBaseUrlPage(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_user", param, handler, activity);
    }

    public List<BaseUrl> baseUrls;

    @Override
    public String getActionName() {
        return "getsyssetting";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpGetBaseUrl upGetBaseUrl = (UpGetBaseUrl) param;
            return upGetBaseUrl.getUploadString();
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
        baseUrls = new ArrayList<>();
        if (response!=null){
            JSONArray array = getJsonArray(response);
            BaseUrl baseUrl = new BaseUrl();
            for (int i =0;i<array.length();i++){
                JSONObject object = JsonUtils.getJsonArray(array,i);
                baseUrl.parse(object);
                baseUrls.add(baseUrl);
            }
        }
        return baseUrls;
    }
}
