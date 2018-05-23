package com.chinabrowser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import com.chinabrowser.R;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.SharedPreferencesUtils;

/**
 * Created by 95470 on 2018/5/8.
 */

public class StartActivity extends BaseActivity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    Navigator.startSelectionActivity(StartActivity.this);
                    Navigator.finishActivity(StartActivity.this);
                    break;
                case 100:
                    SharedPreferencesUtils.setParam(StartActivity.this,"GUIDE",1);
                    Navigator.startGuide(StartActivity.this);
                    Navigator.finishActivity(StartActivity.this);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        //隐藏标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        window.setFlags(flag, flag);
        setContentView(R.layout.activity_start);
        int i = (int) SharedPreferencesUtils.getParam(this,"GUIDE",0);
        if (i==0){
            handler.sendEmptyMessageDelayed(100,1000);
        }else {
            handler.sendEmptyMessageDelayed(200,1000);
        }


    }
}
