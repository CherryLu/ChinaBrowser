package com.chinabrowser.viewholder;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chinabrowser.R;
import com.chinabrowser.adapter.ImagePagerAdapter;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.cbinterface.PagerClick;
import com.chinabrowser.ui.CustomViewpager;
import com.chinabrowser.utils.CommUtils;

import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class SlidePicViewHolder extends BaseViewHolder implements PagerClick {

    CustomViewpager viewpager;
    Context context;
    public SlidePicViewHolder(View itemView,Context context) {
        super(itemView);
        viewpager = (CustomViewpager) itemView.findViewById(R.id.pic);
        initViewPager(viewpager);
        this.context = context;
    }

    @Override
    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
        List<Content> contentLis = recommend.getContents();
        if (contentLis!=null&&contentLis.size()>0){
            ImagePagerAdapter adapter = new ImagePagerAdapter(contentLis,context);
            adapter.setPagerClick(this);
            viewpager.setAdapter(adapter);
            //title.setText(contentLis.get(0).getTitle());
            viewpager.setCurrentItem((contentLis.size()) * 100);
            CommUtils.changeViewPagerSpeedScroll(viewpager);

        }

    }

    private void initViewPager(final CustomViewpager viewpager){
        viewpager.setAutoSlide(true);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                if (arg0 == 0) {
                    viewpager.onResume();
                } else {
                    viewpager.onPause();
                }
            }

            @Override
            public void onPageSelected(int arg0) {

            }
        });

    }


    @Override
    public void pagerClick(int position) {
        Content content = recommend.getContents().get(position);
        if (homeCallBack!=null){
            homeCallBack.startContent(content);
        }
    }
}
