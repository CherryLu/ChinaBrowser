package com.chinabrowser.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.chinabrowser.bean.UserData;
import com.chinabrowser.bean.UserKeeper;
import com.chinabrowser.net.UpUserPageData;
import com.chinabrowser.net.UserProtocolPage;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by 95470 on 2018/4/17.
 */

public class UserManager {

    public static final int MSG_WAHT_MAIL_SUCCESS = 1;
    public static final int MSG_WAHT_MAIL_FAIL = -3;
    public static final int MSG_WAHT_MAIL_PARAMETER_ERROR = -4;
    public static final int MSG_WAHT_MAIL_NOACTIV = -5;

    UserProtocolPage userProtocolPage;
    UpUserPageData upUserPageData;

    UserData userData;

    /**
     * 登录的方式
     */
    private LoginMode loginMode = LoginMode.NONE;

    // 用户是否登录
    private boolean isLogin = false;
    private Handler mHandler;

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

    private UserManager() {
        mHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void dispatchMessage(Message msg) {
                switch (msg.what){
                    case UserProtocolPage.MSG_WHAT_ERROE:
                    case UserProtocolPage.MSG_WHAT_NOTCHANGE:
                        sendOutEmptyMsg(MSG_WAHT_MAIL_FAIL);
                        updateLoginState(false,LoginMode.NONE,userProtocolPage.userData);
                        break;
                    case UserProtocolPage.MSG_WHAT_OK:
                        sendOutEmptyMsg(MSG_WAHT_MAIL_SUCCESS);
                        updateLoginState(true,LoginMode.MAIL,userProtocolPage.userData);
                        refreshData();
                        break;
                }
                super.dispatchMessage(msg);
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
            loginMode = LoginMode.NONE;
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


    private void loginout(){

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
