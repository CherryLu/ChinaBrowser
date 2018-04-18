package com.chinabrowser.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.cbinterface.HomeCallBack;
import com.chinabrowser.fragment.HomeFragment;
import com.chinabrowser.fragment.HotNewsFragment;
import com.chinabrowser.fragment.TranslateFragment;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.Navigator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements HomeCallBack {


    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.index_bottom_line)
    TextView indexBottomLine;
    @Bind(R.id.index_bottom_menu_goback)
    LinearLayout indexBottomMenuGoback;
    @Bind(R.id.index_bottom_menu_nogoback)
    LinearLayout indexBottomMenuNogoback;
    @Bind(R.id.index_bottom_menu_goforward)
    LinearLayout indexBottomMenuGoforward;
    @Bind(R.id.index_bottom_menu_nogoforward)
    LinearLayout indexBottomMenuNogoforward;
    @Bind(R.id.index_bottom_menu_more)
    FrameLayout indexBottomMenuMore;
    @Bind(R.id.index_bottom_iv_menu_gohome)
    ImageView indexBottomIvMenuGohome;
    @Bind(R.id.index_bottom_menu_gohome)
    LinearLayout indexBottomMenuGohome;
    @Bind(R.id.index_bottom_iv_menu_nogohome)
    ImageView indexBottomIvMenuNogohome;
    @Bind(R.id.index_bottom_menu_nogohome)
    LinearLayout indexBottomMenuNogohome;
    @Bind(R.id.index_bottom_tab_count)
    TextView indexBottomTabCount;
    @Bind(R.id.index_bottom_menu_new_window)
    LinearLayout indexBottomMenuNewWindow;

    private HomeFragment homeFragment;
    private HotNewsFragment hotNewsFragment;
    private TranslateFragment translateFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
         homeFragment = new HomeFragment();
        homeFragment.setHomeCallBack(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
    }

    @OnClick({R.id.index_bottom_menu_goback,R.id.index_bottom_menu_more, R.id.index_bottom_menu_nogoback, R.id.index_bottom_menu_goforward, R.id.index_bottom_menu_nogoforward, R.id.index_bottom_menu_gohome, R.id.index_bottom_menu_nogohome, R.id.index_bottom_menu_new_window})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index_bottom_menu_more:
                Navigator.startSettingActivity(this);
                break;
            case R.id.index_bottom_menu_goback:
                break;
            case R.id.index_bottom_menu_nogoback:
                break;
            case R.id.index_bottom_menu_goforward:
                break;
            case R.id.index_bottom_menu_nogoforward:
                break;
            case R.id.index_bottom_menu_gohome:
                break;
            case R.id.index_bottom_menu_nogohome:
                if (homeFragment==null){
                    homeFragment = new HomeFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                break;
            case R.id.index_bottom_menu_new_window://添加页签

                break;
        }
    }

    @Override
    public void titleClick(int which) {
        switch (which){
            case Constant.HOT_TITLE:
                if (hotNewsFragment==null){
                    hotNewsFragment = new HotNewsFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,hotNewsFragment).commit();
                break;
            case Constant.TRA_TITLE:

                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                break;
            case Constant.CHINA_TITLE:

                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                break;
            case Constant.TRANSLATE_TITLE:
                if (translateFragment==null){
                    translateFragment = new TranslateFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,translateFragment).commit();
                break;


        }
    }
}
