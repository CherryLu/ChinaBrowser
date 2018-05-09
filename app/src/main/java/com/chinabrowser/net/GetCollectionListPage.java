package com.chinabrowser.net;

import android.os.Handler;

import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.CollectionItem;
import com.chinabrowser.utils.JsonUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 95470 on 2018/4/23.
 */

public class GetCollectionListPage extends BaseProtocolPage{
    public final static int MSG_WHAT_OK = 10026;
    public final static int MSG_WHAT_NOTCHANGE = 10027;
    public final static int MSG_WHAT_ERROE = 10028;

    public List<CollectionItem> collectionItems;

    public GetCollectionListPage(Object param, Handler handler, BaseActivity activity) {
        super("http://turkey2.china-plus.net:8881/zt/api/zt_collection",param, handler, activity);
    }

    @Override
    public String getActionName() {
        return "getlist";
    }

    @Override
    public String getExtParam(Object param) {
        if (param!=null){
            UpLoadCollectionList upLoadCollectionList = (UpLoadCollectionList) param;
            return upLoadCollectionList.getUploadString();
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
        collectionItems = new ArrayList<>();
        if (response!=null){
            JSONArray array = getJsonArray(response);
            JSONObject object = JsonUtils.getJsonArray(array,0);
            JSONArray jsonArray = JsonUtils.getJSONArray(object,"data");
            for (int i =0;i<jsonArray.length();i++){
                JSONObject object1 = JsonUtils.getJsonArray(jsonArray,i);
                CollectionItem collectionItem = new CollectionItem();
                collectionItem.parse(object1);
                collectionItems.add(collectionItem);
            }
        }
        return collectionItems;
    }
}
