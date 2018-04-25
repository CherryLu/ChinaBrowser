package com.chinabrowser.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.net.FindPswProtocolPage;
import com.chinabrowser.net.UpFindPsw;
import com.chinabrowser.utils.Navigator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/26.
 */

public class ForgetPswActivity extends BaseActivity {

    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.user_name_input)
    EditText userNameInput;
    @Bind(R.id.logion)
    Button logion;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case FindPswProtocolPage.MSG_WHAT_ERROE:
                    logion.setClickable(true);
                    break;
                case FindPswProtocolPage.MSG_WHAT_NOTCHANGE:
                    logion.setClickable(true);
                    break;
                case FindPswProtocolPage.MSG_WHAT_OK:
                    logion.setClickable(true);
                    showToash(getString(R.string.send_mails));
                    Navigator.finishActivity(ForgetPswActivity.this);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpsw);
        ButterKnife.bind(this);
        title.setText(getString(R.string.find_psw));
    }


    @OnClick({R.id.back_image, R.id.logion})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.logion:
                String name = userNameInput.getText().toString();
                if (TextUtils.isEmpty(name)){
                    showToash(getResources().getString(R.string.str_login_noname));
                }else if (!isMails(name)){
                    showToash(getResources().getString(R.string.str_login_notmail));
                }else {
                    getData(name);
                    logion.setClickable(false);
                }
                break;
        }
    }
    FindPswProtocolPage findPswProtocolPage;
    UpFindPsw upFindPsw;
    private void getData(String mails){
        upFindPsw = new UpFindPsw();
        upFindPsw.smail = mails;

        if (findPswProtocolPage==null){
            findPswProtocolPage = new FindPswProtocolPage(upFindPsw,handler,this);
        }
        findPswProtocolPage.refresh(upFindPsw);
    }


    private boolean isMails(String mails){
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(RULE_EMAIL);
        Matcher m = p.matcher(mails);
        return m.matches();
    }
}
