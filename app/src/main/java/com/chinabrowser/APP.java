package com.chinabrowser;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.chinabrowser.utils.FileUtils;
import com.mob.MobSDK;
import com.tencent.smtt.sdk.QbSdk;

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

    @Override
    public void onCreate() {
        super.onCreate();
        //CrashHandlerUtil.getInstance().init(this);
        context = this;
        FileUtils.InitSD();
        initX5WebView();
        MobSDK.init(this);
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
