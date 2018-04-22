package com.chinabrowser.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.chinabrowser.bean.UserData;
import com.chinabrowser.bean.UserKeeper;
import com.chinabrowser.cbinterface.LoginStateInterface;
import com.chinabrowser.net.UpUserPageData;
import com.chinabrowser.net.UserProtocolPage;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;

import static com.chinabrowser.utils.LoginMode.MAIL;
import static com.chinabrowser.utils.LoginMode.NONE;

/**
 * Created by 95470 on 2018/4/17.
 */

public class UserManager {

    public static final int MSG_WAHT_MAIL_SUCCESS = 1;
    public static final int MSG_WAHT_MAIL_FAIL = -3;

    public static final int MSG_WHAT_THIRD_LOGIN_SUCCESS = 3;
    public static final int MSG_WHAT_THIRD_LOGIN_FAIL = 4;

    public static final int MSG_WHAT_THIRD_SUCCESS = 0;
    public static final int MSG_WHAT_THIRD_FAIL = 1;
    public static final int MSG_WHAT_THIRD_CANCLE = 2;


    UserProtocolPage userProtocolPage;
    UpUserPageData upUserPageData;

    UserData userData;


    public String getUsername(){
        if (userData==null){
            return "";
        }else {
            if (loginMode==LoginMode.MAIL){
                return userData.getSmail();
            }else {
                return userData.getUserName();
            }

        }
    }

    /**
     * 获取用户头像
     * @return
     */
    public  String getUserHeader(){
        if (userData==null){
            return "";
        }else {
            if (loginMode==LoginMode.MAIL){
                return "";
            }else {
                return userData.getHeaderuerl();
            }
        }
    }

    public void clearUserData() {
        String path = FileUtils.getAppBasePath() + "userKeeper.dat";
        ObjectUtils.saveObjectData(null, path);
    }

    /**
     * 登录的方式
     */
    private LoginMode loginMode = NONE;

    // 用户是否登录
    private boolean isLogin = false;
    private Handler mHandler;
    private Handler tHandler;

    private static UserManager userManager;

    private Vector<LoginStateInterface> observersVector = new Vector<LoginStateInterface>();

    private ArrayList<Handler> outHandlers = new ArrayList<Handler>();

    public static UserManager getInstance(){
        if (userManager==null){
            userManager = new UserManager();
        }
        return userManager;
    }



    private void setLogin(boolean login) {
        isLogin = login;
        notifyObservers(login);
        saveUserData();
    }
    public void refreshData(){
        readUserData();
    }

    public boolean isLogin() {
        return isLogin;
    }

    private void saveUserData() {
        // TODO Auto-generated method stub
        String path = FileUtils.getAppBasePath() + "userKeeper.dat";
        UserKeeper keeper = new UserKeeper();
        keeper.isLogin = this.isLogin;
        keeper.mode = this.loginMode;
        keeper.userData = userData;
        if (keeper.userData != null) {
            CommUtils.setUserName(keeper.userData.getSmail());
            CommUtils.setUserPassword(keeper.userData.getSuserno());
            CommUtils.setThirdName(keeper.userData.getUserName());
            CommUtils.setThirdheader(keeper.userData.getHeaderuerl());

        }
        CommUtils.saveObjectData(keeper, path);
    }

    private void readUserData() {
        String path = FileUtils.getAppBasePath() + "userKeeper.dat";
        Object obj = CommUtils.loadObjectData(path);
        if (obj != null) {
            UserKeeper keeper = (UserKeeper) obj;
            this.isLogin = keeper.isLogin;
            this.loginMode = keeper.mode;
            this.userData = keeper.userData;
            if (userData != null) {
                CommUtils.setUserName(userData.getSmail());
                CommUtils.setUserPassword(userData.getSuserno());
                CommUtils.setThirdName(keeper.userData.getUserName());
                CommUtils.setThirdheader(keeper.userData.getHeaderuerl());
            }
            LogUtils.d("readUserData","obj ok");
        } else {
            LogUtils.d("readUserData","obj null");
        }
    }





    /**
     * 登录状态变化的监听注册
     *
     * @param observer
     */
    public void attach(LoginStateInterface observer) {
        observersVector.addElement(observer);
    }

    /**
     * 登录状态变化的监听反注册
     *
     * @param observer
     */
    public void detach(LoginStateInterface observer) {
        observersVector.removeElement(observer);
    }

    /**
     * 登录状态变化 广播
     *
     * @param islogin
     */
    private void notifyObservers(boolean islogin) {
        Enumeration<LoginStateInterface> enumeration = observers();
        while (enumeration.hasMoreElements()) {
            ((LoginStateInterface) enumeration.nextElement()).update(islogin);
        }
    }

    private Enumeration<LoginStateInterface> observers() {
        return ((Vector<LoginStateInterface>) observersVector.clone()).elements();
    }

    private String userName;
    private String headerurl;

    private UserManager() {
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what){
                    case UserProtocolPage.MSG_WHAT_ERROE:
                    case UserProtocolPage.MSG_WHAT_NOTCHANGE:
                        sendOutEmptyMsg(MSG_WAHT_MAIL_FAIL);
                        updateLoginState(false, NONE,userProtocolPage.userData);
                        break;
                    case UserProtocolPage.MSG_WHAT_OK:
                        sendOutEmptyMsg(MSG_WAHT_MAIL_SUCCESS);
                        updateLoginState(true, MAIL,userProtocolPage.userData);
                        refreshData();
                        break;
                    case MSG_WHAT_THIRD_SUCCESS:
                        Platform platform = (Platform) msg.obj;
                        String userid = platform.getDb().getUserId();
                         headerurl = platform.getDb().getUserIcon();
                         userName = platform.getDb().getUserName();
                        thirdLogin(userid);
                        break;
                    case MSG_WHAT_THIRD_FAIL:
                        break;
                    case MSG_WHAT_THIRD_CANCLE:
                        break;
                }
                super.dispatchMessage(msg);
            }
        };

        tHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case UserProtocolPage.MSG_WHAT_ERROE:
                    case UserProtocolPage.MSG_WHAT_NOTCHANGE:
                        sendOutEmptyMsg(MSG_WAHT_MAIL_FAIL);
                        updateLoginState(false, NONE,userProtocolPage.userData);
                        break;
                    case UserProtocolPage.MSG_WHAT_OK:
                        if (userProtocolPage!=null&&userProtocolPage.userData!=null){
                            if (!TextUtils.isEmpty(userName)){
                                userProtocolPage.userData.setUserName(userName);
                                userName = "";
                            }
                           if (!TextUtils.isEmpty(headerurl)){
                               userProtocolPage.userData.setHeaderuerl(headerurl);
                               headerurl = "";
                           }
                        }
                        sendOutEmptyMsg(MSG_WAHT_MAIL_SUCCESS);
                        updateLoginState(true, loginMode,userProtocolPage.userData);
                        refreshData();
                        break;
                }

                super.handleMessage(msg);
            }
        };
    }

    /**
     * 更新用户的登录状态
     *
     * @param isLogin
     * @param mode
     * @param infoData 服务器返回的data类型 用来本地保存
     */
    private void updateLoginState(boolean isLogin, LoginMode mode, UserData infoData) {
        if (isLogin) {
            loginMode = mode;
        } else {
            loginMode = NONE;
        }
        this.userData = infoData;
        setLogin(isLogin);
    }


    public void loginBymails(Handler handler,String mails,String psw){
        addOutHandler(handler);
        upUserPageData = new UpUserPageData();
        upUserPageData.suserno = mails;
        upUserPageData.smail = mails;
        upUserPageData.spassword = psw;
        upUserPageData.iusertype = "0";
        if (userProtocolPage==null){
            userProtocolPage = new UserProtocolPage(upUserPageData,mHandler,null);
        }
        userProtocolPage.refresh(upUserPageData);
    }

    public void thirdLogin(String userid){
        upUserPageData = new UpUserPageData();
        upUserPageData.suserno = userid;
        switch (loginMode){
            case QQ:
                upUserPageData.iusertype = "2";
                break;
            case WECHAT:
                upUserPageData.iusertype = "1";
                break;
            case SINA:
                upUserPageData.iusertype = "3";
                break;
            case GOOGLE:
                upUserPageData.iusertype = "6";
                break;
            case TWITTER:
                upUserPageData.iusertype = "5";
                break;
            case FACEBOOK:
                upUserPageData.iusertype = "4";
                break;
        }
        if (userProtocolPage==null){
            userProtocolPage = new UserProtocolPage(upUserPageData,tHandler,null);
        }
        if (!TextUtils.isEmpty(upUserPageData.iusertype)){
            userProtocolPage.refresh(upUserPageData);
        }



    }


    public void loginByThird(LoginMode loginMode){
        this.loginMode = loginMode;
        Platform platform = null;
        switch (loginMode){
            case QQ:
                platform = ShareSDK.getPlatform(QQ.NAME);
                break;
            case WECHAT:
                platform = ShareSDK.getPlatform(Wechat.NAME);
                break;
            case SINA:
                platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                break;
            case GOOGLE:
                platform = ShareSDK.getPlatform(GooglePlus.NAME);
                break;
            case TWITTER:
                platform = ShareSDK.getPlatform(Twitter.NAME);
                break;
            case FACEBOOK:
                platform = ShareSDK.getPlatform(Facebook.NAME);
                break;
        }
        if (platform!=null){
            platform.setPlatformActionListener(new PlatformActionListener() {
                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                        Message message = Message.obtain();
                        message.obj = platform;
                        message.what = MSG_WHAT_THIRD_SUCCESS;
                        mHandler.sendMessage(message);
                }

                @Override
                public void onError(Platform platform, int i, Throwable throwable) {
                    Message message = Message.obtain();
                    message.what = MSG_WHAT_THIRD_FAIL;
                    mHandler.sendMessage(message);
                }

                @Override
                public void onCancel(Platform platform, int i) {
                    Message message = Message.obtain();
                    message.what = MSG_WHAT_THIRD_CANCLE;
                    mHandler.sendMessage(message);
                }
            });
            platform.showUser(null);
        }

    }


    public void loginout(){
        setLogin(false);
        // 用户之前的登录状态
        if (loginMode!=LoginMode.MAIL) {
            Platform platform = null;
            switch (loginMode){
                case QQ:
                    platform = ShareSDK.getPlatform(QQ.NAME);
                    break;
                case WECHAT:
                    platform = ShareSDK.getPlatform(Wechat.NAME);
                    break;
                case SINA:
                    platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                    break;
                case GOOGLE:
                    break;
                case TWITTER:
                    break;
                case FACEBOOK:
                    platform = ShareSDK.getPlatform(Facebook.NAME);
                    break;
            }

            if (platform!=null){
                if (platform.isAuthValid()){
                    platform.removeAccount(true);
                }

            }
        }

        userData = null;
        loginMode = NONE;


        CommUtils.setUserName("");
        CommUtils.setUserPassword("");
        clearUserData();

    }

    public void shareTo(int shareto){
        Platform platform = null;
        switch (shareto){
        case 0://微信
            platform = ShareSDK.getPlatform(Wechat.NAME);
            break;
        case 1://朋友圈
            //platform = ShareSDK.getPlatform(Wechat.);
           break;
        case 2://微博
            platform = ShareSDK.getPlatform(SinaWeibo.NAME);
            break;
        case 3://QQ
            platform = ShareSDK.getPlatform(QQ.NAME);
             break;
       case 4://google
           platform = ShareSDK.getPlatform(GooglePlus.NAME);
            break;
       case 6://twitter
           platform = ShareSDK.getPlatform(Twitter.NAME);
            break;
       case 7://facebook
           platform = ShareSDK.getPlatform(Facebook.NAME);
            break;
        }
        QQ.ShareParams params = new QQ.ShareParams();
        if (platform!=null){
            platform.share(params);
        }
    }


    private void sendOutEmptyMsg(int what) {
        Message msg = new Message();
        msg.what = what;
        sendOutMsg(msg);
    }

    private void sendOutMsg(Message msg) {
        for (int i = 0; i < outHandlers.size(); i++) {
            Handler handler = outHandlers.get(i);
            if (handler != null) {
                Message msg2 = new Message();
                msg2.copyFrom(msg);
                handler.sendMessage(msg2);
            }
        }
    }

    public void addOutHandler(Handler outHandler) {
        if (outHandler == null) {
            return;
        }
        if (!outHandlers.contains(outHandler)) {
            outHandlers.add(outHandler);
        }
    }

    public void removeOutHandler(Handler outHandler) {
        if (outHandler == null) {
            return;
        }

        if (outHandlers.contains(outHandler)) {
            outHandlers.remove(outHandler);
        }
    }
}
