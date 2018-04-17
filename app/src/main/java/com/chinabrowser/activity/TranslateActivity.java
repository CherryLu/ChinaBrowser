package com.chinabrowser.activity;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.ui.WheelView;
import com.chinabrowser.utils.TranslateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/11.
 */

public class TranslateActivity extends BaseActivity {

    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.translate_sort_icon)
    ImageView translateSortIcon;
    @Bind(R.id.translate_sort_name)
    TextView translateSortName;
    @Bind(R.id.translate_choose_btn_from)
    Button translateChooseBtnFrom;
    @Bind(R.id.translate_iv_exchange)
    ImageView translateIvExchange;
    @Bind(R.id.translate_choose_btn_to)
    Button translateChooseBtnTo;
    @Bind(R.id.translate_child_lvitem_line)
    View translateChildLvitemLine;
    @Bind(R.id.translate_content_listview)
    ListView translateContentListview;
    @Bind(R.id.translate_choose_title)
    TextView translateChooseTitle;
    @Bind(R.id.translate_choose_btn_close)
    Button translateChooseBtnClose;
    @Bind(R.id.translate_choose_language)
    WheelView translateChooseLanguage;
    @Bind(R.id.view1)
    View view1;
    @Bind(R.id.view2)
    View view2;
    @Bind(R.id.translate_choose_layout)
    RelativeLayout translateChooseLayout;

    private List<Map<String, Object>> mSortList = new ArrayList<Map<String,Object>>();// 翻译内容数据集
    private final int[] mLanguages = new int[]{R.string.translate_language_zh, R.string.translate_language_tr};

    // 设置目标语种标识 (原文语种|目标翻译语言)
    private final int LAN_FROM = 0;
    private final int LAN_TO = 1;
    private int mSetLan = LAN_FROM;

    // 语音
    private MediaPlayer mPlayer;
    private AssetManager mAssetManager;

    private SharedPreferences mAppSpf;

    public static final int[] mCatTitles = new int[] {R.string.translate_parent_airport, R.string.translate_parent_traffic, R.string.translate_parent_hotel, R.string.translate_parent_restaurant,
            R.string.translate_parent_attraction, R.string.translate_parent_entertainment, R.string.translate_parent_shopping, R.string.translate_parent_bank,
            R.string.translate_parent_postoffice, R.string.translate_parent_car, R.string.translate_parent_life, R.string.translate_parent_hospital,
            R.string.translate_parent_emergency, R.string.translate_parent_classics};
    public static final int[] mCatIcons = new int[] { R.mipmap.translate_icon_airport, R.mipmap.translate_icon_traffic, R.mipmap.translate_icon_hotel, R.mipmap.translate_icon_restaurant,
            R.mipmap.translate_icon_attraction, R.mipmap.translate_icon_entertainment, R.mipmap.translate_icon_shopping, R.mipmap.translate_icon_bank,
            R.mipmap.translate_icon_postoffice, R.mipmap.translate_icon_car, R.mipmap.translate_icon_life, R.mipmap.translate_icon_hospital,
            R.mipmap.translate_icon_emergency, R.mipmap.translate_icon_classics};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traslate);
        ButterKnife.bind(this);
        mAppSpf = getSharedPreferences("LG",MODE_PRIVATE);
        mSortList = new TranslateUtil(this).getTranslateChild(getIntent().getIntExtra("pcid", 0), getIntent().getStringExtra("pname"));

        translateSortIcon.setImageResource(mCatIcons[getIntent().getIntExtra("position", 0)]);
        translateSortName.setText(getString(mCatTitles[getIntent().getIntExtra("position", 0)]));
    }

    @OnClick({R.id.back_image, R.id.translate_choose_btn_from, R.id.translate_iv_exchange, R.id.translate_choose_btn_to})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_image:
                break;
            case R.id.translate_choose_btn_from:
                break;
            case R.id.translate_iv_exchange:
                break;
            case R.id.translate_choose_btn_to:
                break;
        }
    }
}
