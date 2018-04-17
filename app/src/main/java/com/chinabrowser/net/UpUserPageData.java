package com.chinabrowser.net;

import com.chinabrowser.utils.CommUtils;

/**
 * Created by 95470 on 2018/4/17.
 */

public class UpUserPageData {

    public String suserno = "";//三方id  （邮箱登录时改为邮箱）
    public String smail = "";//邮箱
    public String spassword = "";//邮箱登录的密码
    public String susername = "";//昵称
    public String sheadurl = "";//头像
    public String iusertype = "";//登录类型0、邮箱登录1、微信2、QQ3、微博4、facebook5、twitter6、google',


    public String getUploadString() {
        StringBuffer sb = new StringBuffer();
        CommUtils.addParam(sb, "suserno", suserno);
        CommUtils.addParam(sb, "smail", smail);
        CommUtils.addParam(sb, "spassword", spassword);
        CommUtils.addParam(sb, "susername", susername);
        CommUtils.addParam(sb, "sheadurl", sheadurl);
        CommUtils.addParam(sb, "iusertype", iusertype);
        return sb.toString();
    }

}
