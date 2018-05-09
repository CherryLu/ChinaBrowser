package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;

/**
 * Created by 95470 on 2018/4/23.
 */

public class CollectionNewsPage extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 10030;
    public final static int MSG_WHAT_NOTCHANGE = 10031;
    public final static int MSG_WHAT_ERROE = 10032;

    public CollectionNewsPage(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_collection", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "add";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpCollectionNews collectionNews = (UpCollectionNews) param;
            return collectionNews.getUploadString();
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
        return "success";
    }
}
