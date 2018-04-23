package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 95470 on 2018/4/23.
 */

public class BaseUrl implements Serializable {

    public String variable;
    public String value;

    public void parse(JSONObject jsonObject){
        if (jsonObject!=null){
            variable = JsonUtils.getString(jsonObject,"variable");
            value = JsonUtils.getString(jsonObject,"value");
        }

    }
}
