package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by 95470 on 2018/4/16.
 */

public class Title {
    private String title_name;

    public String getTitle_name() {
        return title_name;
    }

    public void setTitle_name(String title_name) {
        this.title_name = title_name;
    }

    public void parse(JSONObject object){
        title_name = JsonUtils.getString(object,"title_name");
    }
}
