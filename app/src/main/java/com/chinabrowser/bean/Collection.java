package com.chinabrowser.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/14.
 */

public class Collection implements Serializable {

    private String id;
    private String title;
    private String url;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
