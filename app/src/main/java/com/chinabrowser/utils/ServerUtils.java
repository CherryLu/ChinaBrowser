package com.chinabrowser.utils;

/**
 * Created by Administrator on 2018/3/26.
 */

public class ServerUtils {
    public static final String ServiceName = "api/ServiceCenter.do";

    private static ServerUtils gInstance = null;

    public static ServerUtils getInstance() {
        if (gInstance == null) {
            gInstance = new ServerUtils();
        }
        return gInstance;
    }

   protected String getServerIpFromServer(boolean isDebug){

       return ServicesIP;
   }

    public String ServicesIP = "119.23.56.48:8881";
}
