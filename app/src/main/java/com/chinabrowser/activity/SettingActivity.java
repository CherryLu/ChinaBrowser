package com.chinabrowser.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.SharedPreferencesUtils;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chinabrowser.R.id.search_name;

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
    @Bind(search_name)
    TextView searchName;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        setSearchName(CommUtils.getCurrentSearch(this));
    }

    private void setSearchName(int position){
        if (position==0){
            searchName.setText(getResources().getText(R.string.search1));
        }else if (position==1){
            searchName.setText(getResources().getText(R.string.search2));
        }else if (position==0){
            searchName.setText(getResources().getText(R.string.search3));
        }
    }

    private void selectLag(int position){
        china.setImageResource(R.mipmap.unselected_imag);
        turkish.setImageResource(R.mipmap.unselected_imag);
        if (position ==0){
            Resources resources = this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            config.locale = Locale.CHINESE;
            resources.updateConfiguration(config, dm);
            SharedPreferencesUtils.setParam(this, Constant.LAG,0);
        }else {

            Resources resources = this.getResources();
            DisplayMetrics dm = resources.getDisplayMetrics();
            Configuration config = resources.getConfiguration();
            config.locale = new Locale("tr");
            resources.updateConfiguration(config, dm);
            SharedPreferencesUtils.setParam(this,Constant.LAG,1);

        }
    }


    @OnClick({R.id.back_image, R.id.header_image,R.id.china, R.id.turkish, search_name, R.id.about_us, R.id.loginout, R.id.clear_account,R.id.login_txt, R.id.collection, R.id.history, R.id.search_attentive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.china:
                selectLag(0);
                break;
            case R.id.turkish:
                selectLag(1);
                break;
            case R.id.about_us:
                Navigator.startAboutActivity(this);
                break;
            case R.id.loginout:

                break;
            case R.id.clear_account:
                break;
            case R.id.login_txt:
                Navigator.startLoginActivity(this);
                break;
            case R.id.collection:
                Navigator.startCollectionActicity(this);
                break;
            case R.id.history:
                Navigator.startHistoryActivityActicity(this);
                break;
            case R.id.search_attentive:
                Navigator.startSetSearchAttentiveActivity(this);
                break;
            case R.id.header_image:
                Navigator.startLoginActivity(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==0){
            setSearchName(resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}