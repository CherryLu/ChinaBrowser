package com.chinabrowser.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.chinabrowser.APP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.util.Locale;

import static com.chinabrowser.utils.FileUtils.saveAsFile;
import static com.chinabrowser.utils.NetUtils.ToEncoder;

/**
 * Created by Administrator on 2018/3/27.
 */

public class CommUtils {


    // 获取通用参数和新增参数并加密
    public static String GetCommonParameterAndEncrypt(String addParam) {
        String para1 = CommUtils.GetCommonParameter(addParam);
        return CommUtils.GetEncryptPara(para1);
    }

    /*
	 * 得到通用参数和新增参数
	 */
    public static String GetCommonParameter(String addParam) {
   /*     String userID = "";
        String pwd = "";

        String filePath = AnyRadioApplication.gFilePath + AnyRadioApplication.getSysID() + "_idpwd.key";
        File myFilePath = new File(filePath);
        if (!myFilePath.exists()) {

            LogUtils.d("GetCommonParameter no key file: " , filePath);
        } else {
            try {
                BufferedReader reader = null;
                reader = new BufferedReader(new FileReader(filePath));
                String tempString = reader.readLine();
                if (tempString != null && tempString.indexOf("|") > 0) {
                    String[] pValues = tempString.split("\\|");
                    userID = pValues[0];
                    pwd = pValues[1];
                }
                reader.close();
            } catch (IOException e) {
            }
        }

        String model = android.os.Build.MODEL;
        model = model.replaceAll(" ", "");

        LocationItem li = LocationItem.getInstance();*/
        StringBuffer sb = new StringBuffer();
        sb.append("si");
        sb.append("=");
        sb.append(ToEncoder("" +9001));
        addParam(sb, "me", APP.getImei());
        /*addParam(sb, "vi", "" + AnyRadioApplication.getVersionID());
        addParam(sb, "ci", "" + AnyRadioApplication.getChannelID());
        addParam(sb, "ds", "" + AnyRadioApplication.getSubVersionID());
        addParam(sb, "vn", AnyRadioApplication.getVersionInfo());
        addParam(sb, "vc", AnyRadioApplication.getVersionCode());

        addParam(sb, "ui", userID);
        addParam(sb, "up", pwd);
        if (sb.indexOf("&un=") == -1) {
            addParam(sb, "un", getUserName());
        }

        addParam(sb, "tm", model);
        addParam(sb, "me", AnyRadioApplication.getImei());
        addParam(sb, "m2", AnyRadioApplication.getImei2());
        addParam(sb, "pn", AnyRadioApplication.getPhoneNumber());
        addParam(sb, "ms", AnyRadioApplication.getImsi());
        addParam(sb, "tv", getAndroidSDKVersion());
        addParam(sb, "nt", getSimType());
        addParam(sb, "lc", "");// lac
        addParam(sb, "ce", "");// cellid
        addParam(sb, "ln", li.getProvince() + li.getCity() + li.getDistrict() + li.getAddrStr());
        addParam(sb, "la", li.getLatitude());
        addParam(sb, "lo", li.getLongitude());
        addParam(sb, "pr", getProvince());
        addParam(sb, "ct", getCity());
        addParam(sb, "co", li.getDistrict());
        addParam(sb, "st", li.getAddrStr());
        addParam(sb, "ov", "android" + android.os.Build.VERSION.RELEASE);
        addParam(sb, "tb", android.os.Build.MANUFACTURER);
        int size[] = getScreenSize();
        addParam(sb, "sw", "" + size[0]);
        addParam(sb, "sh", "" + size[1]);
        addParam(sb, "cu", "" + getCpu());
        if (!AnyRadioApplication.getScreenOrientation()) {
            // 横屏
            addParam(sb, "so", "landscape");
        } else {
            addParam(sb, "so", "portrait");
        }
        addParam(sb, "lg", getLanguage(AnyRadioApplication.mContext));
        addParam(sb, "cr", "");// 国家暂时传""
        addParam(sb, "sd", getMetaData("scheme"));
        if (addParam != null) {
            sb.append("&");
            sb.append(addParam);
        }

        String ret = sb.toString();
        LogUtils.DebugLog("GetCommonParameter " + ret);
        NewLogUtils.d("CommonParameter","","ret="+ret);*/
        return addParam;
    }

    public static String GetEncryptPara(String para) {
        StringBuffer sb = new StringBuffer();
        try {
            String urlEncoder = java.net.URLEncoder.encode(para, APP.ENCODE_GBK);
            String crc = MD5(urlEncoder);
            byte buffer[] = new byte[urlEncoder.length()];
            buffer = Encrypt(urlEncoder.getBytes());
            urlEncoder = null;
            String Base64str = new String(Base64.encode(buffer));
            buffer = null;
            String upload = "";
            upload = java.net.URLEncoder.encode(Base64str, APP.ENCODE_GBK);
            Base64str = null;
            sb.append("verf=");
            sb.append(crc);
            sb.append("&pv=3");
            sb.append("&data=");
            sb.append(upload);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            // LogUtils.ShowToast(AnyRadioApplication.mContext, "oom 2608",
            // Toast.LENGTH_SHORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] Encrypt(byte bytes[]) {
        int len = bytes.length;
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (bytes[i] ^ 0x6e);
        }
        return bytes;
    }

    public static boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /*
    * 字符串转换成整形
    */
    public static int convert2int(String value) {
        int ret = 0;
        if (!TextUtils.isEmpty(value)) {
            try {
                ret = Integer.parseInt(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    /**
     * 数据处理对象
     * @param context
     * @return
     */
    public static SQLiteDatabase openDatabase(Context context, String fileName, int resId) {
        String databaseFilename = context.getFileStreamPath(fileName).getPath();
        if (!context.getFileStreamPath(fileName).exists()) {
            copyDatabase(context, databaseFilename, resId);
        }
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(databaseFilename, null);
        return database;
    }

    /**
     * 数据拷贝
     * @param context
     * @param filePath
     */
    public static void copyDatabase(Context context, String filePath, int resId) {
        try {
            InputStream is = context.getResources().openRawResource(resId);
            FileOutputStream fos = new FileOutputStream(filePath);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前语言
     * 0 中文
     * 1 土耳其语
     * @param context
     */
    public static int getCurrentLag(Context context){
      return (int) SharedPreferencesUtils.getParam(context, Constant.LAG,0);
    }

    /**
     * 获取当前搜索引擎
     * @param context
     * @return
     */
    public static int getCurrentSearch(Context context){
        int searchattentive = (int) SharedPreferencesUtils.getParam(context, Constant.SEARCH,0);
        return searchattentive;
    }

    /**
     * 设置搜索引擎
     * @param context
     * @param which
     */
    public static void setCurrentSearch(Context context,int which){
        SharedPreferencesUtils.setParam(context, Constant.SEARCH,which);
    }

    /**
     * 拼接关键字搜索baseurl
     * @param context
     * @return
     */
    public static String getsearchurl(Context context,String keywork){
        int lag = getCurrentLag(context);
        int search = getCurrentSearch(context);
        String url = "";
        switch (lag){
            case 0:
                switch (search){
                    case 0:
                        url = "https://www.baidu.com/s?wd="+keywork;
                        break;
                    case 1:
                        url = "https://www.sogou.com/web?query="+keywork;//
                        break;
                    case 2:
                        url ="https://www.so.com/s?q="+keywork;//
                        break;
                }
                break;
            case 1:
                switch (search){
                    case 0:
                        url = "https://www.google.com.hk/search?q=";
                        break;
                    case 1:
                        url = "https://cn.bing.com/search?q="+keywork;
                        break;
                    case 2:
                        url = "https://www.yandex.com/search/?text="+keywork;
                        break;
                }
                break;
        }


        return url;
    }

    /**
     * 切换语言
     * @param context
     * @param position
     */
    public static void changeLag(Context context,int position){
        if (position==0){
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            config.locale = Locale.CHINESE;
            resources.updateConfiguration(config, dm);
            SharedPreferencesUtils.setParam(context,Constant.LAG,0);
        }else {
            Resources resources = context.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            config.locale = new Locale("tr");
            resources.updateConfiguration(config, dm);
            SharedPreferencesUtils.setParam(context,Constant.LAG,1);

        }
        setCurrentSearch(context,0);
    }


    public static boolean isAccessibilityEnabled(Context context) {
        int accessibilityEnabled = 0;
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(context.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
        }

        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {

            String settingValue = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                TextUtils.SimpleStringSplitter splitter = mStringColonSplitter;
                splitter.setString(settingValue);
                while (splitter.hasNext()) {
                    return true;
                }
            }
        } else {
        }
        return accessibilityFound;
    }


    // 添加参数
    public static void addParam(StringBuffer s, String key, String value) {
        String v = ToEncoder(value);
        String lastChar = null;
        String str = s.toString();
        int len = str.length();
        if (len > 0)
            lastChar = str.substring(len - 1, len);
        if (lastChar != null && !lastChar.equals("&"))
            s.append("&");
        s.append(key);
        s.append("=");
        s.append(v);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    /**
     * 保存登录账号，本地保存，避免使用usermanager单例的进程安全问题
     *
     * @param name
     */
    public static void setUserName(String name) {
        try {
            String nameBase64 = Base64Utils.encode(name.getBytes());
            SharedPreferencesUtils.setParam(APP.getContext(),"registername",nameBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取用户登录的用户名
     *
     * @return
     */
    public static String getUserName() {
        if (!UserManager.getInstance().isLogin()) {
            return "";
        }
        String name = (String) SharedPreferencesUtils.getParam(APP.getContext(),"registername","");
        try {
            return new String(Base64Utils.decode(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return name;
    }

    public static void setUserPassword(String pwd) {
        try {
            String pwdBase64 = Base64Utils.encode(pwd.getBytes());
            SharedPreferencesUtils.setParam(APP.getContext(),"registerpwd",pwdBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 将数据缓存到文件
    public static void saveObjectData(Object obj, String fileName) {
        if (TextUtils.isEmpty(fileName))
            return;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream ostream = null;
            ostream = new ObjectOutputStream(baos);
            ostream.writeObject((Object) obj);
            ostream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        try {
            is.available();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        saveAsFile(is, fileName);

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
