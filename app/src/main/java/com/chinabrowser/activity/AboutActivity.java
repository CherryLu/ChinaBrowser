package com.chinabrowser.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.utils.CommUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/14.
 */

public class AboutActivity extends BaseActivity {

    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.china)
    TextView china;
    @Bind(R.id.tuerqi)
    TextView tuerqi;
    @Bind(R.id.txt1)
    TextView txt1;
    @Bind(R.id.txt2)
    TextView txt2;
    @Bind(R.id.txt3)
    TextView txt3;
    @Bind(R.id.txt4)
    TextView txt4;
    @Bind(R.id.txt5)
    TextView txt5;
    @Bind(R.id.txt6)
    TextView txt6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        String str = getString(R.string.app_name) + getVerName(this);
        name.setText(str);
        if (CommUtils.getCurrentLag(this) == 0) {
            china.setVisibility(View.VISIBLE);
            tuerqi.setVisibility(View.GONE);
            txt1.setVisibility(View.GONE);
            txt2.setVisibility(View.GONE);
            txt3.setVisibility(View.GONE);
            txt4.setVisibility(View.GONE);
            txt5.setVisibility(View.GONE);
            txt6.setVisibility(View.GONE);
        }else {
            china.setVisibility(View.GONE);
            tuerqi.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            txt3.setVisibility(View.VISIBLE);
            txt4.setVisibility(View.VISIBLE);
            txt5.setVisibility(View.VISIBLE);
            txt6.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
