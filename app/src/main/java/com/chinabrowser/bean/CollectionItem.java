package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by 95470 on 2018/4/23.
 */

public class CollectionItem {

    public String lm_name ;
    public String title;
    public String copy_from;
    public String id;
    public String copy_url;
    public String catalog_id;


    public void parse(JSONObject jsonObject){
        if (jsonObject!=null){
            lm_name = JsonUtils.getString(jsonObject,"lm_name");
            title = JsonUtils.getString(jsonObject,"title");
            copy_from = JsonUtils.getString(jsonObject,"copy_from");
            id = JsonUtils.getString(jsonObject,"id");
            copy_url = JsonUtils.getString(jsonObject,"copy_url");
            catalog_id = JsonUtils.getString(jsonObject,"catalog_id");
        }
    }
}
