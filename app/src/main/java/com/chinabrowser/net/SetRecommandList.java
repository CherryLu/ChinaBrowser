package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;

/**
 * Created by Administrator on 2018/5/29.
 */

public class SetRecommandList extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 10038;
    public final static int MSG_WHAT_NOTCHANGE = 10039;
    public final static int MSG_WHAT_ERROE = 10040;


    public SetRecommandList(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_news", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "setlinkdefault";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpSetRecommand upRecommand = (UpSetRecommand) param;
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

        return null;
    }
}
