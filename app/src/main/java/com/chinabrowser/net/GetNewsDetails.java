package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.NewsDetail;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by 95470 on 2018/4/23.
 */

public class GetNewsDetails extends BaseProtocolPage {

    public final static int MSG_WHAT_OK = 1005;
    public final static int MSG_WHAT_NOTCHANGE = 1004;
    public final static int MSG_WHAT_ERROE = 1003;

    public NewsDetail newsDetail;

    public GetNewsDetails(Object param, Handler handler, BaseActivity activity) {
        super("http://119.23.56.48:8881/zt/api/zt_news", param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "getcontent";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpGetNewsDetail getNewsDetails = (UpGetNewsDetail) param;
            return getNewsDetails.getUploadString();
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
            newsDetail = new NewsDetail();
            JSONArray array = getJsonArray(response);
            JSONObject object = JsonUtils.getJsonArray(array,0);
            newsDetail.parse(object);
        }
        return newsDetail;
    }
}
