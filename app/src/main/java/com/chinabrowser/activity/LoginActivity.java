package com.chinabrowser.activity;

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
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.SharedPreferencesUtils;
import com.chinabrowser.utils.UserManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/11.
 */

public class LoginActivity extends BaseActivity {


    @Bind(R.id.back_image)
    ImageView backImage;
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
    @Bind(R.id.third_one)
    ImageView thirdOne;
    @Bind(R.id.third_two)
    ImageView thirdTwo;
    @Bind(R.id.third_three)
    ImageView thirdThree;
    boolean isRemember;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UserManager.MSG_WAHT_MAIL_FAIL:
                    showToash("登录失败");
                    break;
                case UserManager.MSG_WAHT_MAIL_SUCCESS:
                    showToash("登录成功");
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        title.setText(R.string.str_login);
        getRemember();
    }

    private void getRemember(){
        String name = (String) SharedPreferencesUtils.getParam(this,"NAME","");
        if (!TextUtils.isEmpty(name)){
            String psw = (String) SharedPreferencesUtils.getParam(this,"PSW","");
            userNameInput.setText(name);
            userPswInput.setText(psw);
        }
    }

    /**
     * 根据语言显示不同第三方
     */
    private void showOtherLogin() {

    }

    @OnClick({R.id.rember_psw, R.id.forget_psw, R.id.logion, R.id.regist, R.id.third_one, R.id.third_two, R.id.third_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rember_psw:
                if (isRemember){
                    isRemember = false;
                    remberPsw.setImageResource(R.mipmap.rember_psw_uncheck);
                }else {
                    isRemember = true;
                    remberPsw.setImageResource(R.mipmap.rember_psw_checked);
                }
                break;
            case R.id.forget_psw:
                break;
            case R.id.logion:
                String name = userNameInput.getText().toString();
                String psw = userPswInput.getText().toString();
                if (TextUtils.isEmpty(name)){
                    showToash(getResources().getString(R.string.str_login_noname));
                }else if (TextUtils.isEmpty(psw)){
                    showToash(getResources().getString(R.string.str_login_nopsw));
                }else if (!isMails(name)){
                    showToash(getResources().getString(R.string.str_login_notmail));
                }else {
                    UserManager.getInstance().loginBymails(handler,name,psw);
                }
                break;
            case R.id.regist:
                Navigator.startRegisterActivity(this);
                break;
            case R.id.third_one:
                break;
            case R.id.third_two:
                break;
            case R.id.third_three:
                break;
        }
    }

    private void setRememberNumber(String name,String psw){
        if (isRemember){
            SharedPreferencesUtils.setParam(this,"NAME",name);
            SharedPreferencesUtils.setParam(this,"PSW",psw);
        }else {
            SharedPreferencesUtils.setParam(this,"NAME","");
            SharedPreferencesUtils.setParam(this,"PSW","");
        }
    }

    private boolean isMails(String mails){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(mails);
        return m.matches();
    }
}
