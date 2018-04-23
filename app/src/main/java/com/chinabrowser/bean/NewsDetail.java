package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by 95470 on 2018/4/23.
 */

public class NewsDetail {

    public String vcontent;
    public String post_id;
    public String introduce;
    public String seo_description;
    public String content;

    public void parse(JSONObject object){
        if (object!=null){
            vcontent = JsonUtils.getString(object,"vcontent");
            post_id = JsonUtils.getString(object,"post_id");
            introduce = JsonUtils.getString(object,"introduce");
            seo_description = JsonUtils.getString(object,"seo_description");
            content = JsonUtils.getString(object,"content");
        }
    }

}
