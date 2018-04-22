package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/19.
 */

public class LinkData implements Serializable {

    private String create_time = "";
    private String ilanguage ="";
    private String image_url = "";
    private String introduce;
    private String lm_name;
    private String title = "";
    private String catalog_id;
    private String update_time;
    private String width;
    private String link_url;
    private String id;
    private String sort_order;
    private String height;

    public void parse(JSONObject jsonObject){
        if (jsonObject==null){
            return;
        }
        create_time = JsonUtils.getString(jsonObject,"create_time");
        ilanguage = JsonUtils.getString(jsonObject,"ilanguage");
        image_url = JsonUtils.getString(jsonObject,"image_url");
        introduce = JsonUtils.getString(jsonObject,"introduce");
        lm_name = JsonUtils.getString(jsonObject,"lm_name");
        title = JsonUtils.getString(jsonObject,"title");
        catalog_id = JsonUtils.getString(jsonObject,"catalog_id");
        update_time = JsonUtils.getString(jsonObject,"update_time");
        width = JsonUtils.getString(jsonObject,"width");
        link_url = JsonUtils.getString(jsonObject,"link_url");
        id = JsonUtils.getString(jsonObject,"id");
        sort_order = JsonUtils.getString(jsonObject,"sort_order");
        height = JsonUtils.getString(jsonObject,"height");

    }

    public String getCreate_time() {
        return create_time;
    }

    public String getIlanguage() {
        return ilanguage;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getIntroduce() {
        return introduce;
    }

    public String getLm_name() {
        return lm_name;
    }

    public String getTitle() {
        return title;
    }

    public String getCatalog_id() {
        return catalog_id;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public String getWidth() {
        return width;
    }

    public String getLink_url() {
        return link_url;
    }

    public String getId() {
        return id;
    }

    public String getSort_order() {
        return sort_order;
    }

    public String getHeight() {
        return height;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setIlanguage(String ilanguage) {
        this.ilanguage = ilanguage;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setLm_name(String lm_name) {
        this.lm_name = lm_name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCatalog_id(String catalog_id) {
        this.catalog_id = catalog_id;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSort_order(String sort_order) {
        this.sort_order = sort_order;
    }

    public void setHeight(String height) {
        this.height = height;
    }


}
