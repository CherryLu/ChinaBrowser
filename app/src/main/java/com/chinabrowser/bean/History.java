package com.chinabrowser.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/14.
 */

public class History implements Serializable {
    private String id;
    private String title;
    private String url;
    private Long time;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

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
