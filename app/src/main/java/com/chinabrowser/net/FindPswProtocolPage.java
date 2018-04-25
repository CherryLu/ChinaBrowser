package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;

/**
 * Created by Administrator on 2018/4/26.
 */

public class FindPswProtocolPage extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 10035;
    public final static int MSG_WHAT_NOTCHANGE = 10036;
    public final static int MSG_WHAT_ERROE = 10037;

    public FindPswProtocolPage(Object param, Handler handler, BaseActivity activity) {
        super("http://119.23.56.48:8881/zt/api/ zt_user", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "getpasswordbymail";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpFindPsw upFindPsw = (UpFindPsw) param;
            return upFindPsw.getUploadString();
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
        return "successs";
    }
}
