package com.chinabrowser.bean;

import com.chinabrowser.utils.JsonUtils;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by 95470 on 2018/4/17.
 */

public class UserData implements Serializable{
    private String iusertype;//登录方式
    private String dtinsert;
    private String suserno;//三方id
    private String smail;//邮箱
    private String headeruerl;//头像
    private String userName;//用户名

    public String getHeaderuerl() {
        return headeruerl;
    }

    public String getUserName() {
        return userName;
    }

    public void setHeaderuerl(String headeruerl) {
        this.headeruerl = headeruerl;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIusertype() {
        return iusertype;
    }

    public String getDtinsert() {
        return dtinsert;
    }

    public String getSuserno() {
        return suserno;
    }

    public String getSmail() {
        return smail;
    }

    public void setIusertype(String iusertype) {
        this.iusertype = iusertype;
    }

    public void setDtinsert(String dtinsert) {
        this.dtinsert = dtinsert;
    }

    public void setSuserno(String suserno) {
        this.suserno = suserno;
    }

    public void setSmail(String smail) {
        this.smail = smail;
    }

    public void parse(JSONObject jsonObject){
        if (jsonObject==null){
            return;
        }
        iusertype = JsonUtils.getString(jsonObject,"iusertype");
        dtinsert = JsonUtils.getString(jsonObject,"dtinsert");
        suserno = JsonUtils.getString(jsonObject,"suserno");
        smail = JsonUtils.getString(jsonObject,"suserno");
    }
}
