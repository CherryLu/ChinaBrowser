package com.chinabrowser.net;

import com.chinabrowser.utils.CommUtils;

/**
 * Created by Administrator on 2018/5/29.
 */

public class UpRecommand {

    public String ilanguage  = "";
    public String suserno = "";

    public String getUploadString() {
        StringBuffer sb = new StringBuffer();
        CommUtils.addParam(sb, "ilanguage", ilanguage);
        CommUtils.addParam(sb, "suserno", suserno);
        return sb.toString();
    }
}
