package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;

/**
 * Created by Administrator on 2018/4/18.
 */

public class GetLinkListProtocolPage extends BaseProtocolPage {

    public GetLinkListProtocolPage(String url, Object param, Handler handler, BaseActivity activity) {
        super(url, param, handler, activity);
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public String getExtParam(Object param) {
        return null;
    }

    @Override
    public boolean supportCacheFile() {
        return false;
    }

    @Override
    public int getMsgWhatOk() {
        return 0;
    }

    @Override
    public int getMsgWhatError() {
        return 0;
    }

    @Override
    public int getMsgWhatDataNotChange() {
        return 0;
    }

    @Override
    public void setData(Object data) {

    }

    @Override
    public Object parserJson(byte[] response) {
        return null;
    }
}
