package com.chinabrowser.itemview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.adapter.ImagePagerAdapter;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.Recommend;
import com.chinabrowser.bean.Title;
import com.chinabrowser.cbinterface.PagerClick;
import com.chinabrowser.ui.CustomViewpager;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.Constant;

import java.util.List;

/**
 * Created by 95470 on 2018/4/15.
 */

public class SlidePicView extends BaseView implements PagerClick {

    CustomViewpager viewpager;
    Context mContext;

    public SlidePicView(Context context, ViewGroup parent) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mVivew =inflater.inflate(R.layout.bigpic_item,parent,false);
        title = (TextView) mVivew.findViewById(R.id.title);
        more = (ImageView) mVivew.findViewById(R.id.more);

        viewpager = (CustomViewpager) mVivew.findViewById(R.id.pic);
        initViewPager(viewpager);
    }

    @Override
    public void setRecommend(Recommend recommend) {
        this.recommend = recommend;
        List<Content> contentLis = recommend.getContents();
        if (contentLis!=null&&contentLis.size()>0){
            ImagePagerAdapter adapter = new ImagePagerAdapter(contentLis,mContext);
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
        if (TextUtils.isEmpty(content.getAction())){
            return;
        }

        if (content.getAction().equals("1")){
            if (homeCallBack!=null){
                content.setId(content.getNewsid());
                homeCallBack.startContent(content);
            }
        }else if (content.getAction().equals("2")){
            if (homeCallBack!=null){
                Title title = new Title();
                title.setTitle_name("");
                title.setCatalog_id(content.getCatalog_id());
                homeCallBack.titleClick(Constant.SLIDE,title);
            }
        }else if (content.getAction().equals("3")){
            if (homeCallBack!=null){
                homeCallBack.startContentByurl(content);
            }
        }

    }
}
