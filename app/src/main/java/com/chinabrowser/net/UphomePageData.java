package com.chinabrowser.net;

import com.chinabrowser.utils.CommUtils;

/**
 * Created by 95470 on 2018/4/16.
 */

public class UphomePageData {

    public String ilanguage = "";

    public String getUploadString() {
        StringBuffer sb = new StringBuffer();
        CommUtils.addParam(sb, "ilanguage", ilanguage);
        return sb.toString();
    }
}
