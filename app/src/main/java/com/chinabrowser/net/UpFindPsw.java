package com.chinabrowser.net;

import com.chinabrowser.utils.CommUtils;

/**
 * Created by Administrator on 2018/4/26.
 */

public class UpFindPsw {

    public String smail   = "";


    public String getUploadString() {
        StringBuffer sb = new StringBuffer();
        CommUtils.addParam(sb, "smail ", smail );
        return sb.toString();
    }
}
