package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

import java.util.StringTokenizer;

/**
 * Created by 95470 on 2018/4/23.
 */

public class NewsDetail {

    public String vcontent;
    public String post_id;
    public String introduce;
    public String seo_description;
    public String content;
    public String iftag;
    public String title;
    public String copy_from;
    public String copy_url;
    public String cover_image;
    public String create_time;
    public String update_time;


    public void parse(JSONObject object){
        if (object!=null){
            vcontent = JsonUtils.getString(object,"vcontent");
            post_id = JsonUtils.getString(object,"post_id");
            introduce = JsonUtils.getString(object,"introduce");
            seo_description = JsonUtils.getString(object,"seo_description");
            content = JsonUtils.getString(object,"content");
            iftag = JsonUtils.getString(object,"iftag");
            title = JsonUtils.getString(object,"title");
            copy_from = JsonUtils.getString(object,"copy_from");
            copy_url = JsonUtils.getString(object,"copy_url");
            create_time = JsonUtils.getString(object,"create_time");
            update_time = JsonUtils.getString(object,"update_time");
            cover_image = JsonUtils.getString(object,"cover_image");
        }
    }

}
