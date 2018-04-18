package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;

/**
 * Created by Administrator on 2018/4/18.
 */

public class GetLinkListProtocolPage extends BaseProtocolPage {
    public final static int MSG_WHAT_OK = 10096;
    public final static int MSG_WHAT_NOTCHANGE = 10097;
    public final static int MSG_WHAT_ERROE = 10098;



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
            return upGetLinkData.toString();
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
        return null;
    }
}
