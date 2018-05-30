package com.chinabrowser.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.cbinterface.LoginStateInterface;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.LoginMode;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.SharedPreferencesUtils;
import com.chinabrowser.utils.UserManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chinabrowser.R.id.third_one;

/**
 * Created by Administrator on 2018/4/11.
 */

public class LoginActivity extends BaseActivity {



    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.user_name_input)
    EditText userNameInput;
    @Bind(R.id.user_psw_input)
    EditText userPswInput;
    @Bind(R.id.rember_psw)
    ImageView remberPsw;
    @Bind(R.id.forget_psw)
    LinearLayout forgetPsw;
    @Bind(R.id.logion)
    Button logion;
    @Bind(R.id.regist)
    Button regist;
    @Bind(third_one)
    ImageView thirdOne;
    @Bind(R.id.third_two)
    ImageView thirdTwo;
    @Bind(R.id.third_three)
    ImageView thirdThree;
    boolean isRemember;
    @Bind(R.id.back_image)
    ImageView backImage;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UserManager.MSG_WAHT_MAIL_FAIL:
                    showToash(getString(R.string.setting_loginfail));
                    break;
                case UserManager.MSG_WAHT_MAIL_SUCCESS:
                    showToash(getString(R.string.setting_loginsuccess));
                    break;

            }
            super.handleMessage(msg);
        }
    };
    int lag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        title.setText(R.string.str_login);
        getRemember();
        UserManager.getInstance().attach(new LoginStateInterface() {
            @Override
            public void update(boolean isLogin) {
                hideWaitDialog();
                if (isLogin) {
                    Navigator.finishActivity(LoginActivity.this);
                }
            }
        });
         lag = CommUtils.getCurrentLag(this);
        if (lag==0){
            thirdOne.setImageResource(R.mipmap.weibo);
            thirdTwo.setImageResource(R.mipmap.mht);
            thirdThree.setImageResource(R.mipmap.weixin);
        }else {
            thirdOne.setImageResource(R.mipmap.twitter);
            thirdTwo.setImageResource(R.mipmap.facebook);
            thirdThree.setImageResource(R.mipmap.google);
        }
    }

    private void getRemember() {
        String name = (String) SharedPreferencesUtils.getParam(this, "NAME", "");
        if (!TextUtils.isEmpty(name)) {
            String psw = (String) SharedPreferencesUtils.getParam(this, "PSW", "");
            userNameInput.setText(name);
            userPswInput.setText(psw);
            userNameInput.setSelection(name.length());
        }
    }



    @OnClick({R.id.rember_psw, R.id.back_image,R.id.forget_psw, R.id.logion, R.id.regist, third_one, R.id.third_two, R.id.third_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.rember_psw:
                if (isRemember) {
                    isRemember = false;
                    remberPsw.setImageResource(R.mipmap.rember_psw_uncheck);
                } else {
                    isRemember = true;
                    remberPsw.setImageResource(R.mipmap.rember_psw_checked);
                }
                break;
            case R.id.forget_psw:
                Navigator.startFindPSW(this);
                break;
            case R.id.logion:
                String name = userNameInput.getText().toString();
                String psw = userPswInput.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showToash(getResources().getString(R.string.str_login_noname));
                } else if (TextUtils.isEmpty(psw)) {
                    showToash(getResources().getString(R.string.str_login_nopsw));
                } else if (!isMails(name)) {
                    showToash(getResources().getString(R.string.str_login_notmail));
                } else {
                    UserManager.getInstance().loginBymails(handler, name, psw);
                    setRememberNumber(name, psw);
                }
                break;
            case R.id.regist:
                Navigator.startRegisterActivity(this);
                break;
            case third_one:
                if (lag==0){
                    UserManager.getInstance().loginByThird(LoginMode.SINA);
                }else {
                    UserManager.getInstance().loginByThird(LoginMode.TWITTER);
                }

                break;
            case R.id.third_two:
                if (lag==0){
                    UserManager.getInstance().loginByThird(LoginMode.QQ);
                }else {
                    UserManager.getInstance().loginByThird(LoginMode.FACEBOOK);
                }

                break;
            case R.id.third_three:
                if (lag==0){
                    UserManager.getInstance().loginByThird(LoginMode.WECHAT);
                }else {
                    UserManager.getInstance().loginByThird(LoginMode.GOOGLE);
                }

                break;
        }
    }

    private void setRememberNumber(String name, String psw) {
        if (isRemember) {
            SharedPreferencesUtils.setParam(this, "NAME", name);
            SharedPreferencesUtils.setParam(this, "PSW", psw);
        } else {
            SharedPreferencesUtils.setParam(this, "NAME", "");
            SharedPreferencesUtils.setParam(this, "PSW", "");
        }
    }


    private boolean isMails(String mails) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(mails);
        return m.matches();
    }

    @OnClick(R.id.back_image)
    public void onClick() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        showWaitDialog("登陆中");
        super.onActivityResult(requestCode, resultCode, data);
    }
}
