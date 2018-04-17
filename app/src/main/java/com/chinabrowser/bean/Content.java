package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

/**
 * Created by 95470 on 2018/4/15.
 */

public class Content  {
    private String id ;
    private int type ;
    private String title;
    private String subtitle;
    private String from;
    private String time;
    private String url;
    private String link_url;
    private String image_url;

    public String getLink_url() {
        return link_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getFrom() {
        return from;
    }

    public String getTime() {
        return time;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    public void parse(JSONObject jsonObject){
        if (jsonObject!=null){
            id = JsonUtils.getString(jsonObject,"id");
            title = JsonUtils.getString(jsonObject,"title");
            subtitle = JsonUtils.getString(jsonObject,"introduce");
            link_url = JsonUtils.getString(jsonObject,"link_url");
            image_url = JsonUtils.getString(jsonObject,"image_url");
            time = JsonUtils.getString(jsonObject,"create_time");
        }
    }
}
