package com.chinabrowser.net;

import com.chinabrowser.utils.CommUtils;

/**
 * Created by 95470 on 2018/4/23.
 */

public class UpCollectionNews {

    public String ilanguage  = "";
    public String id ="";
    public String suserno = "";

    public String getUploadString() {
        StringBuffer sb = new StringBuffer();
        CommUtils.addParam(sb, "ilanguage", ilanguage);
        CommUtils.addParam(sb, "id", id);
        CommUtils.addParam(sb, "suserno", suserno);
        return sb.toString();
    }
}
