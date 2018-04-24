package com.chinabrowser.net;

import com.chinabrowser.utils.CommUtils;

/**
 * Created by 95470 on 2018/4/18.
 */

public class UpLoadGetHot {
    public String ilanguage;
    public String catalog_id;
    public String pageindex;
    public String pagesize;

    public String getUploadString() {
        StringBuffer sb = new StringBuffer();
        CommUtils.addParam(sb, "ilanguage", ilanguage);
        CommUtils.addParam(sb, "catalog_id", catalog_id);
        CommUtils.addParam(sb, "pageindex", pageindex);
        CommUtils.addParam(sb, "pagesize", "100");
        return sb.toString();
    }
}
