package com.chinabrowser;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.chinabrowser.bean.BaseUrl;
import com.chinabrowser.bean.HomeTab;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.net.GetBaseUrlPage;
import com.chinabrowser.net.GetLinkListProtocolPage;
import com.chinabrowser.net.UpGetBaseUrl;
import com.chinabrowser.net.UpGetLinkData;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.CrashHandlerUtil;
import com.chinabrowser.utils.FileUtils;
import com.chinabrowser.utils.LabManager;
import com.mob.MobSDK;
import com.tencent.smtt.sdk.QbSdk;

import java.util.List;

/**
 * Created by Administrator on 2018/3/26.
 */

public class APP extends Application {
    private static Context context;

    public final static String ProtocolCachePath = "protocol";
    public static final String ENCODE_GBK = "gbk";
    public static final String ENCODE_UTF = "utf-8";
    public static final String AppBaseFolder = "AnyRadio";

    public static int protocolHandlerCount = 0;
    public static int protocolActivityCount = 0;
    public static boolean isX5Init;

    public static String gFilePath = "";
    public static String gMyFilePath;
    public static String gFileFolder;
    public static String gFileFolderAudioPack;
    public static String gFileFolderAudio;
    public static String gFileFolderRecommend;
    public static String gFileProtocolCachePath;
    public static String gFileFolderUpImage;
    public static String gFileFolderDownImage;
    public static String gFileFolderAudioAD;
    public static String gFileFolderHistoryPlay;
    public static List<Recommend> linkDatas;
    public static List<BaseUrl> baseUrls;
    public static List<HomeTab> homeTabs;
    public static HomeTab homeTab;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case GetLinkListProtocolPage.MSG_WHAT_OK:
                    if (getLinkListProtocolPage!=null){
                        APP.linkDatas =getLinkListProtocolPage.linkDatas;
                    }
                    break;
                case GetLinkListProtocolPage.MSG_WHAT_NOTCHANGE:
                case GetLinkListProtocolPage.MSG_WHAT_ERROE:
                    break;
                case GetBaseUrlPage.MSG_WHAT_OK:
                    if (getBaseUrlPage!=null){
                        baseUrls = getBaseUrlPage.baseUrls;
                    }
                    break;
                case GetBaseUrlPage.MSG_WHAT_ERROE:
                case GetBaseUrlPage.MSG_WHAT_NOTCHANGE:

                    break;


            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandlerUtil.getInstance().init(this);
        context = this;
        FileUtils.InitSD();
        initX5WebView();
        MobSDK.init(this);
        getBaseurl();
        getLinkList();
        LabManager.getInstance();
    }

    GetLinkListProtocolPage getLinkListProtocolPage;
    UpGetLinkData upGetLinkData;
    private void getLinkList(){
        upGetLinkData = new UpGetLinkData();
        upGetLinkData.ilanguage = CommUtils.getCurrentLag(this)+1+"";
        if (getLinkListProtocolPage==null){
            getLinkListProtocolPage = new GetLinkListProtocolPage(upGetLinkData,handler,null);
        }
        getLinkListProtocolPage.refresh(upGetLinkData);

    }
    GetBaseUrlPage getBaseUrlPage;
    UpGetBaseUrl upGetBaseUrl;
    private void getBaseurl(){
        upGetBaseUrl = new UpGetBaseUrl();
        upGetBaseUrl.variable = CommUtils.getCurrentLag(this)+"";
        if (getBaseUrlPage==null){
            getBaseUrlPage = new GetBaseUrlPage(upGetBaseUrl,handler,null);
        }
        getBaseUrlPage.refresh(upGetBaseUrl);
    }
    private void initX5WebView(){
        QbSdk.PreInitCallback initCallback = new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean b) {
                isX5Init = b;

            }
        };
        QbSdk.initX5Environment(this,initCallback);
    }

    public static Context getContext() {
        return context;
    }

    private static String gImei = "";
    private static String gImei2 = "";

    public static String getImei() {
        if (TextUtils.isEmpty(gImei)) {
            if (context != null) {
                gImei = Settings.System.getString(context.getContentResolver(), "android_id");
                if (TextUtils.isEmpty(gImei)) {
                    gImei = getImei2();
                }
            }
        }
        return gImei;
    }

    // 获取真正IMEI号
    public static String getImei2() {
        if (TextUtils.isEmpty(gImei2)) {
            if (context != null) {
                gImei2 = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            }
        }
        return gImei2;
    }

    public static String getAppBaseFolder() {
            return "com.chinabrowser.browser";
    }
}
