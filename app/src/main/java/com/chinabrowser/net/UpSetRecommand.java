package com.chinabrowser.net;

import com.chinabrowser.utils.CommUtils;

/**
 * Created by Administrator on 2018/5/29.
 */

public class UpSetRecommand {

    public String ilanguage  = "";
    public String suserno = "";
    public String sset = "";

    public String getUploadString() {
        StringBuffer sb = new StringBuffer();
        CommUtils.addParam(sb, "ilanguage", ilanguage);
        CommUtils.addParam(sb, "suserno", suserno);
        CommUtils.addParam(sb, "sset", sset);
        return sb.toString();
    }
}
