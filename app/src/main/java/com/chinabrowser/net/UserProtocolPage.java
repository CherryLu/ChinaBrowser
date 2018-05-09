package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.UserData;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 95470 on 2018/4/17.
 */

public class UserProtocolPage extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 10090;
    public final static int MSG_WHAT_NOTCHANGE = 10091;
    public final static int MSG_WHAT_ERROE = 10092;

    public UserData userData;

    public UserProtocolPage(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_user", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "login";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpUserPageData upUserPageData = (UpUserPageData) param;
            return upUserPageData.getUploadString();
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
            userData = new UserData();
            userData.parse(object);
        }
        return userData;
    }
}
