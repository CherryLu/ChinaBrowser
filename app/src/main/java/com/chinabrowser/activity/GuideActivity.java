package com.chinabrowser.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chinabrowser.R;
import com.chinabrowser.utils.Navigator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 95470 on 2018/5/23.
 */

public class GuideActivity extends BaseActivity {

    @Bind(R.id.viewpager)
    ViewPager viewpager;

    List<Integer> integers;
    @Bind(R.id.enter)
    LinearLayout enter;
    @Bind(R.id.pic1)
    ImageView pic1;
    @Bind(R.id.pic2)
    ImageView pic2;
    @Bind(R.id.pic3)
    ImageView pic3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        enter.setVisibility(View.GONE);
        integers = new ArrayList<>();
        integers.add(R.mipmap.guide1);
        integers.add(R.mipmap.guide2);
        integers.add(R.mipmap.guide3);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        viewpager.setAdapter(imageAdapter);
        setIndex(0);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    enter.setVisibility(View.VISIBLE);
                } else {
                    enter.setVisibility(View.GONE);
                }
                setIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void setIndex(int index) {
        pic1.setVisibility(View.INVISIBLE);
        pic2.setVisibility(View.INVISIBLE);
        pic3.setVisibility(View.INVISIBLE);

       if (index==0){
           pic1.setVisibility(View.VISIBLE);
       }else if (index==1){
           pic2.setVisibility(View.VISIBLE);
       }else {
           pic3.setVisibility(View.VISIBLE);
       }
    }

    @OnClick(R.id.enter)
    public void onClick() {
        Navigator.startSelectionActivity(this);
        Navigator.finishActivity(this);
    }


    class ImageAdapter extends PagerAdapter {
        List<ImageView> imageViews;


        public ImageAdapter(Context context) {
            imageViews = new ArrayList<>();
            for (int i = 0; i < integers.size(); i++) {
                ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setImageResource(integers.get(i));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageViews.add(imageView);
            }
        }

        @Override
        public int getCount() {
            return imageViews == null ? 0 : imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }
    }


}
