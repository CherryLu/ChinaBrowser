package com.chinabrowser.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.adapter.FragmentAdapter;
import com.chinabrowser.fragment.HistoryFragment;
import com.chinabrowser.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/14.
 */

public class HistoryActivity extends BaseActivity {

    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.today)
    TextView today;
    @Bind(R.id.lastweek)
    TextView lastweek;
    @Bind(R.id.lastmonth)
    TextView lastmonth;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    FragmentAdapter fragmentAdapter;
    List<Fragment> fragmentList;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        title.setText(getResources().getText(R.string.setting_history));
        initTab();
        initViewPager();
    }

    private void initViewPager(){
        fragmentList = new ArrayList<>();

        HistoryFragment today = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("WHICH",1);
        today.setArguments(bundle);
        fragmentList.add(today);

        HistoryFragment lastweek = new HistoryFragment();
        Bundle bundle2 = new Bundle();
        bundle.putInt("WHICH",2);
        lastweek.setArguments(bundle2);
        fragmentList.add(lastweek);

        HistoryFragment lastmonth = new HistoryFragment();
        Bundle bundle3 = new Bundle();
        bundle.putInt("WHICH",3);
        lastmonth.setArguments(bundle3);
        fragmentList.add(lastmonth);

        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        viewpager.setAdapter(fragmentAdapter);


    }

    private void initTab(){
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelctedTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setSelctedTab(int position){
        today.setBackgroundColor(getResources().getColor(R.color.white));
        today.setTextColor(getResources().getColor(R.color.color_txt_gray));

        lastweek.setBackgroundColor(getResources().getColor(R.color.white));
        lastweek.setTextColor(getResources().getColor(R.color.color_txt_gray));

        lastmonth.setBackgroundColor(getResources().getColor(R.color.white));
        lastmonth.setTextColor(getResources().getColor(R.color.color_txt_gray));
        switch (position){
            case 0:
                today.setBackgroundColor(getResources().getColor(R.color.color_base));
                today.setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                lastweek.setBackgroundColor(getResources().getColor(R.color.color_base));
                lastweek.setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                lastmonth.setBackgroundColor(getResources().getColor(R.color.color_base));
                lastmonth.setTextColor(getResources().getColor(R.color.white));
                break;

        }

    }

    @OnClick({R.id.back_image, R.id.today, R.id.lastweek, R.id.lastmonth})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
            case R.id.today:
                setSelctedTab(0);
                viewpager.setCurrentItem(0);
                break;
            case R.id.lastweek:
                setSelctedTab(1);
                viewpager.setCurrentItem(1);
                break;
            case R.id.lastmonth:
                setSelctedTab(2);
                viewpager.setCurrentItem(2);
                break;
        }
    }



}
