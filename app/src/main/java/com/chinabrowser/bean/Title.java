package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 95470 on 2018/4/16.
 */

public class Title implements Serializable {
    private String title_name;
    private String catalog_id;

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }


    public String getTitle_name() {
        return title_name;
    }

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public void parse(JSONObject object){
        title_name = JsonUtils.getString(object,"title_name");
        catalog_id = JsonUtils.getString(object,"catalog_id");
    }
}
