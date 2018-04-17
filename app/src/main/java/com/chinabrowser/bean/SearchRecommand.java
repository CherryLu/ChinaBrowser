package com.chinabrowser.bean;

/**
 * Created by Administrator on 2018/4/17.
 */

public class SearchRecommand {
    private String id;
    private String name;
    private String url;

    private int images;

    public int getImages() {
        return images;
    }

    public void setImages(int images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
