package com.chinabrowser.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.cbinterface.LoginStateInterface;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.UserManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/11.
 */

public class RegisterActivity extends BaseActivity {


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
    @Bind(R.id.user_pswconfire_input)
    EditText userPswconfireInput;
    @Bind(R.id.user_agreen)
    TextView userAgreen;
    @Bind(R.id.logion)
    Button logion;
    @Bind(R.id.comeback)
    TextView comeback;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UserManager.MSG_WAHT_MAIL_FAIL:
                    showToash(getString(R.string.str_regist_fail));
                    break;
                case UserManager.MSG_WAHT_MAIL_SUCCESS:
                    showToash(getString(R.string.str_regist_success));
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_register_layout);
        ButterKnife.bind(this);
        title.setText(getResources().getText(R.string.title_register));
        setText();
        UserManager.getInstance().attach(new LoginStateInterface() {
            @Override
            public void update(boolean isLogin) {
                if (isLogin){
                    Navigator.finishActivity(RegisterActivity.this);
                }
            }
        });
    }

    private void setText(){
        String str = userAgreen.getText().toString();
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#31a0ff")), 12,20,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        userAgreen.setText(builder);
    }


    @OnClick({R.id.back_image, R.id.user_agreen, R.id.logion, R.id.comeback})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.user_agreen:
                break;
            case R.id.logion:
                String name = userNameInput.getText().toString();
                String psw = userPswInput.getText().toString();
                String pswcon = userPswconfireInput.getText().toString();
                if (TextUtils.isEmpty(name)){
                    showToash(getResources().getString(R.string.str_login_noname));
                }else if (TextUtils.isEmpty(psw)){
                    showToash(getResources().getString(R.string.str_login_nopsw));
                }else if (TextUtils.isEmpty(pswcon)){
                    showToash(getResources().getString(R.string.str_login_nopsw));
                }else if (!psw.endsWith(pswcon)){
                    showToash(getResources().getString(R.string.str_login_noeque));
                }else if (!isMails(name)){
                    showToash(getResources().getString(R.string.str_login_notmail));
                }else {
                    UserManager.getInstance().loginBymails(handler,name,psw);
                }
                break;
            case R.id.comeback:
                Navigator.finishActivity(this);
                break;
        }
    }


    private boolean isMails(String mails){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(mails);
        return m.matches();
    }

}
