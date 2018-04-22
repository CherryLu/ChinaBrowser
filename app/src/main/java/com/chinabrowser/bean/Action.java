package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

import java.util.StringTokenizer;

/**
 * Created by Administrator on 2018/4/22.
 */

public class Action  {
    private String request_action;
    private String jumppage;
    private String _do;
    private String event;
    private String request_url;
    private String request_parameter;


    public void parse(JSONObject jsonObject){
        if (jsonObject!=null){
            request_action = JsonUtils.getString(jsonObject,"request_action");
            jumppage = JsonUtils.getString(jsonObject,"jumppage");
            _do = JsonUtils.getString(jsonObject,"do");
            event = JsonUtils.getString(jsonObject,"event");
            request_url = JsonUtils.getString(jsonObject,"request_url");
            request_parameter = JsonUtils.getString(jsonObject,"request_parameter");
        }
    }

    public void setRequest_action(String request_action) {
        this.request_action = request_action;
    }

    public void setJumppage(String jumppage) {
        this.jumppage = jumppage;
    }

    public void set_do(String _do) {
        this._do = _do;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setRequest_url(String request_url) {
        this.request_url = request_url;
    }

    public void setRequest_parameter(String request_parameter) {
        this.request_parameter = request_parameter;
    }

    public String getRequest_action() {
        return request_action;
    }

    public String getJumppage() {
        return jumppage;
    }

    public String get_do() {
        return _do;
    }

    public String getEvent() {
        return event;
    }

    public String getRequest_url() {
        return request_url;
    }

    public String getRequest_parameter() {
        return request_parameter;
    }
}
