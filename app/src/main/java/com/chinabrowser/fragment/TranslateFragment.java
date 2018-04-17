package com.chinabrowser.fragment;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.adapter.FirstTranslateAdapter;
import com.chinabrowser.utils.CommUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/11.
 */

public class TranslateFragment extends BaseFragment {


    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.recyclelist)
    RecyclerView recyclelist;
    private List<Map<String, Object>> mSortList = new ArrayList<Map<String, Object>>();// 翻译内容数据集
    private final int[] mLanguages = new int[]{R.string.translate_language_zh, R.string.translate_language_tr};

    // 设置目标语种标识 (原文语种|目标翻译语言)
    private final int LAN_FROM = 0;
    private final int LAN_TO = 1;
    private int mSetLan = LAN_FROM;

    // 语音
    private MediaPlayer mPlayer;
    private AssetManager mAssetManager;

    private SharedPreferences mAppSpf;

    private FirstTranslateAdapter translateAdapter;

    public static final int[] mCatTitles = new int[]{R.string.translate_parent_airport, R.string.translate_parent_traffic, R.string.translate_parent_hotel, R.string.translate_parent_restaurant,
            R.string.translate_parent_attraction, R.string.translate_parent_entertainment, R.string.translate_parent_shopping, R.string.translate_parent_bank,
            R.string.translate_parent_postoffice, R.string.translate_parent_car, R.string.translate_parent_life, R.string.translate_parent_hospital,
            R.string.translate_parent_emergency, R.string.translate_parent_classics};
    public static final int[] mCatIcons = new int[]{R.mipmap.airport, R.mipmap.traffic, R.mipmap.hotel, R.mipmap.restaurant,
            R.mipmap.attractions, R.mipmap.game, R.mipmap.malls, R.mipmap.back,
            R.mipmap.post, R.mipmap.car, R.mipmap.life, R.mipmap.hospital,
            R.mipmap.emergency, R.mipmap.message};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_traslate,null);
        ButterKnife.bind(view);
        initList();
        return view;
    }


    private void initList(){
        translateAdapter = new FirstTranslateAdapter(getContext());
        GridLayoutManager manager = new GridLayoutManager(getContext(),3);
        recyclelist.setLayoutManager(manager);
        recyclelist.setAdapter(translateAdapter);
        recyclelist.addItemDecoration(new SpaceItemDecoration());
    }

    class SpaceItemDecoration extends RecyclerView.ItemDecoration{


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = CommUtils.dip2px(getContext(),6);
            outRect.right = CommUtils.dip2px(getContext(),6);
            outRect.bottom = CommUtils.dip2px(getContext(),6);
            outRect.top = CommUtils.dip2px(getContext(),6);

        }
    }


}
