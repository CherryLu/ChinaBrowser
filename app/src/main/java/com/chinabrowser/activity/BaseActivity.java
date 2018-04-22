package com.chinabrowser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;


import com.chinabrowser.net.BaseProtocolPage;
import com.chinabrowser.utils.LogUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by Android on 2017/2/13.
 */
public class BaseActivity extends AppCompatActivity {
    private ArrayList<BaseProtocolPage> mProtocolPageList = new ArrayList<BaseProtocolPage>();
    private Handler fHandler = null;
    private static final int MSG_WHAT_SHOW_WAIT_DIALOG = 104;
    private static final int MSG_WHAT_HIDE_WAIT_DIALOG = 105;
    private String showMessage = "";
    private int dialogId = -1;
    private static final int DIALOG_WAIT = 102;
    private static final int MSG_WHAT_SHOW_TOASH = 106;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 隐藏状态栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        fHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case MSG_WHAT_SHOW_TOASH:
                        String str = (String) msg.obj;
                        Toast.makeText(BaseActivity.this,str,Toast.LENGTH_SHORT).show();
                        break;
                    case MSG_WHAT_HIDE_WAIT_DIALOG:
                        if (!isFinishing()) {
                            if (dialogId == DIALOG_WAIT) {
                                dialogId = -1;
                                try {
                                    dismissDialog(DIALOG_WAIT);
                                } catch (Exception e) {
                                }
                            }
                        }
                        break;
                    case MSG_WHAT_SHOW_WAIT_DIALOG:
                        if (!isFinishing()) {
                            if (dialogId == -1) {
                                dialogId = DIALOG_WAIT;
                                showDialog(DIALOG_WAIT);
                            }
                        }
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }



    public void addProtocolPage(BaseProtocolPage protocolPage) {
        if (!this.mProtocolPageList.contains(protocolPage))
            this.mProtocolPageList.add(protocolPage);

        LogUtils.d(getClass().getCanonicalName() + " addProtocolPage mProtocolPageList size: " , mProtocolPageList.size() + "" );
    }

    // 此函数可能会从非UI线程调用过来，需要用handler处理
    public void showWaitDialog() {
        sendShowWaitDialogMsg("正在加载");
    }

    public void hideWaitDialog() {
        fHandler.removeMessages(MSG_WHAT_HIDE_WAIT_DIALOG);
        sendHideWaitDialogMsg();
    }

    private void sendHideWaitDialogMsg() {
        fHandler.removeMessages(MSG_WHAT_HIDE_WAIT_DIALOG);
        Message msg = Message.obtain();
        msg.what = MSG_WHAT_HIDE_WAIT_DIALOG;
        fHandler.sendMessage(msg);
    }

    public void showWaitDialog(String waitMessage){
        sendShowWaitDialogMsg(waitMessage);
    }


    private void sendShowWaitDialogMsg(String waitMessage) {
        fHandler.removeMessages(MSG_WHAT_SHOW_WAIT_DIALOG);

        showMessage = waitMessage;
        Message msg = Message.obtain();
        msg.what = MSG_WHAT_SHOW_WAIT_DIALOG;
        fHandler.sendMessage(msg);
    }

    public void showToash(String str){
        Message message = Message.obtain();
        message.what = MSG_WHAT_SHOW_TOASH;
        message.obj = str;
        fHandler.sendMessage(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (mProtocolPageList != null) {
            for (int i = 0; i < mProtocolPageList.size(); i++) {
                LogUtils.d(getClass().getCanonicalName() + " onDestroy mProtocolPageList " , i + " " + mProtocolPageList.get(i));
                mProtocolPageList.get(i).cancel();
                mProtocolPageList.get(i).delAllHandler();
                mProtocolPageList.get(i).delAllActivity();
            }
            mProtocolPageList.clear();
        }
    }
}
