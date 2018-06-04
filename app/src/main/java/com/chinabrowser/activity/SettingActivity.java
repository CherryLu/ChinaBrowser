package com.chinabrowser.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.KillSelfService;
import com.chinabrowser.R;
import com.chinabrowser.cbinterface.LoginStateInterface;
import com.chinabrowser.ui.ExitDialog;
import com.chinabrowser.ui.RedPacketCustomDialog;
import com.chinabrowser.utils.AppCacheUtils;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.GlideUtils;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.SharedPreferencesUtils;
import com.chinabrowser.utils.UserManager;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2018/3/27.
 */

public class SettingActivity extends BaseActivity {


    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.header_image)
    ImageView headerImage;
    @Bind(R.id.china)
    ImageView china;
    @Bind(R.id.turkish)
    ImageView turkish;
    @Bind(R.id.about_us)
    TextView aboutUs;
    @Bind(R.id.loginout)
    TextView loginout;
    @Bind(R.id.clear_account)
    TextView clearAccount;
    @Bind(R.id.login_txt)
    TextView loginTxt;
    @Bind(R.id.collection)
    LinearLayout collection;
    @Bind(R.id.history)
    LinearLayout history;
    @Bind(R.id.search_attentive)
    LinearLayout searchAttentive;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.lag_switch)
    TextView lagSwitch;
    @Bind(R.id.collection_txt)
    TextView collectionTxt;
    @Bind(R.id.history_txt)
    TextView historyTxt;
    @Bind(R.id.search_attentive_txt)
    TextView searchAttentiveTxt;
    @Bind(R.id.search_name)
    TextView searchName;
    @Bind(R.id.cleardata)
    TextView cleardata;

    LoginStateInterface loginStateInterface;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 200:
                    RedPacketCustomDialog customDialog = new RedPacketCustomDialog(SettingActivity.this,2);
                    customDialog.showIt();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setCurrentLag();
        setSearchName(CommUtils.getCurrentSearch(this));
        title.setText(getString(R.string.setting_title));
        if (UserManager.getInstance().isLogin()) {
            loginTxt.setText(UserManager.getInstance().getUsername());
            GlideUtils.loadImageView(this,UserManager.getInstance().getUserHeader(),headerImage);
            loginout.setVisibility(View.VISIBLE);
            clearAccount.setVisibility(View.GONE);
        } else {
            headerImage.setImageResource(R.mipmap.defaluthead);
            loginTxt.setText(getText(R.string.setting_login));
            loginout.setVisibility(View.GONE);
            clearAccount.setVisibility(View.GONE);
        }

        loginStateInterface = new LoginStateInterface() {
            @Override
            public void update(boolean isLogin) {
                if (isLogin) {
                    GlideUtils.loadImageView(SettingActivity.this,UserManager.getInstance().getUserHeader(),headerImage);
                    loginTxt.setText(UserManager.getInstance().getUsername());
                    loginout.setVisibility(View.VISIBLE);
                    clearAccount.setVisibility(View.GONE);
                } else {
                    headerImage.setImageResource(R.mipmap.defaluthead);
                    loginTxt.setText(getText(R.string.setting_login));
                    loginout.setVisibility(View.GONE);
                    clearAccount.setVisibility(View.GONE);
                }
            }
        };

        UserManager.getInstance().attach(loginStateInterface);
    }

    private void setCurrentLag(){
       int lag =  CommUtils.getCurrentLag(this);
        china.setImageResource(R.mipmap.unselected_imag);
        turkish.setImageResource(R.mipmap.unselected_imag);
        if (lag==0){
            china.setImageResource(R.mipmap.selected_imag);
        }else {
            turkish.setImageResource(R.mipmap.selected_imag);
        }
    }

    private void setSearchName(int position) {
        if (position == 0) {
            searchName.setText(getResources().getText(R.string.search1));
        } else if (position == 1) {
            searchName.setText(getResources().getText(R.string.search2));
        } else if (position == 2) {
            searchName.setText(getResources().getText(R.string.search3));
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void selectLag(int position) {
        china.setImageResource(R.mipmap.unselected_imag);
        turkish.setImageResource(R.mipmap.unselected_imag);
        if (position == 0) {
            china.setImageResource(R.mipmap.selected_imag);
            Resources resources = this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            Locale locale = new Locale(Locale.CHINESE.getLanguage());
            config.setLocale(locale);
            //config.locale = Locale.CHINESE;
            resources.updateConfiguration(config, dm);
            SharedPreferencesUtils.setParam(this, Constant.LAG, 0);
        } else {
            turkish.setImageResource(R.mipmap.selected_imag);
            Resources resources = this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            Locale locale = new Locale("tr");
            config.setLocale(locale);
            //config.locale = new Locale("tr");
            resources.updateConfiguration(config, dm);
            SharedPreferencesUtils.setParam(this, Constant.LAG, 1);
        }

        title.setText(getString(R.string.setting_title));
        lagSwitch.setText(getText(R.string.setting_switch));
        collectionTxt.setText(getText(R.string.setting_switch));
        historyTxt.setText(getText(R.string.setting_history));
        searchAttentiveTxt.setText(getText(R.string.setting_search));
        setSearchName(CommUtils.getCurrentSearch(this));
        aboutUs.setText(getText(R.string.setting_about));
        cleardata.setText(getText(R.string.setting_clear));
        loginout.setText(getText(R.string.setting_loginout));
        clearAccount.setText(getText(R.string.setting_clear_account));
    }

    private void restartAPP(){
        Intent intent = new Intent(this, KillSelfService.class);
        intent.putExtra("PackageName",getPackageName());
        intent.putExtra("Delayed",100);
        startService(intent);
    }



    @OnClick({R.id.back_image, R.id.header_image, R.id.china, R.id.turkish,  R.id.about_us, R.id.loginout, R.id.clear_account, R.id.login_txt, R.id.collection, R.id.history, R.id.search_attentive,R.id.cleardata})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.china:
                if (CommUtils.getCurrentLag(this)==0){
                    return;
                }
                ExitDialog dialog = new ExitDialog(this, getString(R.string.restartapp), getString(R.string.setting_login_yes), getString(R.string.setting_login_no), new ExitDialog.DialogClick() {
                    @Override
                    public void dialogClick(int which) {
                        if (which==1){
                            selectLag(0);
                            restartAPP();
                        }
                    }
                });
                dialog.showIt();
                break;
            case R.id.turkish:
                if (CommUtils.getCurrentLag(this)==1){
                    return;
                }
                ExitDialog dialogs = new ExitDialog(this, getString(R.string.restartapp), getString(R.string.setting_login_yes), getString(R.string.setting_login_no), new ExitDialog.DialogClick() {
                    @Override
                    public void dialogClick(int which) {
                        if (which==1){
                            selectLag(1);
                            restartAPP();
                        }
                    }
                });
                dialogs.showIt();
                break;
            case R.id.about_us:
                Navigator.startAboutActivity(this);
                break;
            case R.id.loginout:
                ExitDialog exitDialog = new ExitDialog(this, getString(R.string.setting_login_que), getString(R.string.setting_login_yes), getString(R.string.setting_login_no), new ExitDialog.DialogClick() {
                    @Override
                    public void dialogClick(int which) {
                        if (which==1){
                            UserManager.getInstance().loginout();

                        }
                    }
                });
                exitDialog.showIt();

                break;
            case R.id.clear_account:
                break;
            case R.id.login_txt:
                if (!UserManager.getInstance().isLogin()){
                    Navigator.startLoginActivity(this);
                }

                break;
            case R.id.collection:
                if (UserManager.getInstance().isLogin()){
                    Navigator.startCollectionActicity(this);
                }else {
                    Navigator.startLoginActivity(this);
                }

                break;
            case R.id.history:
                Navigator.startHistoryActivityActicity(this);
                break;
            case R.id.search_attentive:
                Navigator.startSetSearchAttentiveActivity(this);
                break;
            case R.id.header_image:
                if (!UserManager.getInstance().isLogin()){
                    Navigator.startLoginActivity(this);
                }
                break;
            case R.id.cleardata:
                doClearCache();
                break;
        }
    }

    private void doClearCache() {
        showWaitDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                AppCacheUtils.clearCache(SettingActivity.this);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideWaitDialog();
                        if (handler!=null){
                            handler.sendEmptyMessage(200);
                        }

                    }
                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            setSearchName(resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserManager.getInstance().detach(loginStateInterface);
    }
}
