package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by 95470 on 2018/4/18.
 */

public class NewsData {

    public String create_time;
    public String special_id;
    public String attention_count;
    public String commend;
    public String title;
    public String copy_from;
    public String reply_count;
    public String title_second;
    public String tags;
    public String catalog_id;
    public String update_time;
    public String title_style;
    public String copy_url;
    public String favorite_count;
    public String cover_image;
    public String id;


    public void parse(JSONObject jsonObject){
        if (jsonObject!=null){
            create_time = JsonUtils.getString(jsonObject,"create_time");
            title = JsonUtils.getString(jsonObject,"title");
            copy_from = JsonUtils.getString(jsonObject,"copy_from");
            update_time = JsonUtils.getString(jsonObject,"update_time");
            copy_url = JsonUtils.getString(jsonObject,"copy_url");
            cover_image = JsonUtils.getString(jsonObject,"cover_image");
            id = JsonUtils.getString(jsonObject,"id");
        }
    }

}
