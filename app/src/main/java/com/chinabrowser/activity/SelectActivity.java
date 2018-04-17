package com.chinabrowser.activity;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
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

/**
 * Created by Adminstor on 2018/3/27.
 */

public class SelectActivity extends BaseActivity {

    @Bind(R.id.china_line)
    View chinaLine;
    @Bind(R.id.chines_lag)
    TextView chinesLag;
    @Bind(R.id.turkish_lag)
    TextView turkishLag;
    @Bind(R.id.turkish_line)
    View turkishLine;

    private boolean ischina = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDefalutLan();
        init();
        setContentView(R.layout.activity_select);
        ButterKnife.bind(this);
    }

    private void getDefalutLan(){
        Constant.language = (int) SharedPreferencesUtils.getParam(this, Constant.LAG, 0);
      switch (Constant.language){
          case 0: {
              Resources resources = this.getResources();
              DisplayMetrics dm = resources.getDisplayMetrics();
              Configuration config = resources.getConfiguration();
              config.locale = Locale.CHINESE;
              resources.updateConfiguration(config, dm);
              break;
          }
          case 1: {
              Resources resources = this.getResources();
              DisplayMetrics dm = resources.getDisplayMetrics();
              Configuration config = resources.getConfiguration();
              config.locale = new Locale("tr");
              resources.updateConfiguration(config, dm);
          }
              break;
      }
    }


    private void init() {
        int i = (int) SharedPreferencesUtils.getParam(this, Constant.FIRST, 0);
        if (i != 0) {
            Navigator.startMainActivity(this);
            finish();
        }else {
            SharedPreferencesUtils.setParam(this,Constant.FIRST,1);
        }

    }

    @OnClick({R.id.chines_lag, R.id.turkish_lag})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chines_lag: {
                chinaLine.setVisibility(View.INVISIBLE);
                turkishLine.setVisibility(View.VISIBLE);

                CommUtils.changeLag(this,0);


                Navigator.startMainActivity(this);
                finish();
                break;
            }
            case R.id.turkish_lag: {
                chinaLine.setVisibility(View.VISIBLE);
                turkishLine.setVisibility(View.GONE);
                CommUtils.changeLag(this,1);

                Navigator.startMainActivity(this);
                finish();
                break;
            }
        }
    }


}
