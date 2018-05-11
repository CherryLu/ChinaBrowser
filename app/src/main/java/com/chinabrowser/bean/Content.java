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
    private String attach_file;
    private String copy_from;
    private String cover_image;
    private String copy_url;
    private String action;
    private String catalog_id;
    private String newsid;

    public String getCatalog_id() {
        return catalog_id;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

    public String getCopy_from() {
        return copy_from;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getNewsid() {
        return newsid;
    }

    public String getCover_image() {
        return cover_image;
    }

    public String getCopy_url() {
        return copy_url;
    }

    public String getAttach_file() {
        return attach_file;
    }

    public void setCopy_from(String copy_from) {
        this.copy_from = copy_from;
    }

    public void setCover_image(String cover_image) {
        this.cover_image = cover_image;
    }

    public void setCopy_url(String copy_url) {
        this.copy_url = copy_url;
    }

    public void setAttach_file(String attach_file) {
        this.attach_file = attach_file;
    }

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void parse(JSONObject jsonObject){
        if (jsonObject!=null){
            id = JsonUtils.getString(jsonObject,"id");
            title = JsonUtils.getString(jsonObject,"title");
            subtitle = JsonUtils.getString(jsonObject,"introduce");
            link_url = JsonUtils.getString(jsonObject,"link_url");
            image_url = JsonUtils.getString(jsonObject,"image_url");
            time = JsonUtils.getString(jsonObject,"create_time");
            attach_file = JsonUtils.getString(jsonObject,"attach_file");
            copy_from = JsonUtils.getString(jsonObject,"copy_from");
            cover_image = JsonUtils.getString(jsonObject,"cover_image");
            copy_url = JsonUtils.getString(jsonObject,"copy_url");
            action = JsonUtils.getString(jsonObject,"action");
            catalog_id = JsonUtils.getString(jsonObject,"catalog_id");
            newsid = JsonUtils.getString(jsonObject,"newsid");
        }
    }
}
