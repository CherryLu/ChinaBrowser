package com.chinabrowser.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinabrowser.R;
import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.bean.TranslateEntity;
import com.chinabrowser.ui.CustomListView;
import com.chinabrowser.ui.TosAdapterView;
import com.chinabrowser.ui.TosGallery;
import com.chinabrowser.ui.WheelView;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.TranslateUtil;

/**
 * 情景翻译具体情景界面
 * @author blackieqq
 * @since 2014-8-28
 */
@SuppressLint("InflateParams")
public class TranslateActivity extends BaseActivity implements OnClickListener {

    private ImageView mSortIcon;// 当前分组图标
    private TextView mSortName; // 当前分组名称
    private Button mBtnFrom, mBtnTo;// 目标语种设置
    private ImageView mBtnEx;// 互换
    private TextView title;
    private ImageView back;
    
    private RelativeLayout mChooseLayout;// 原文语种|目标语种选择视图
    private TextView mTvTitle;// 源语言或目标语言标识
    private Button mBtnClose;
    private ListView mLvContent;// 翻译内容
    private SortAdapter mAdapterContent;
    
    private List<Map<String, Object>> mSortList = new ArrayList<Map<String,Object>>();// 翻译内容数据集
    
    // 目标语种设置语言种类列表
    private WheelView mLvLanguage;
    
    // 设置目标语种标识 (原文语种|目标翻译语言)
    private final int LAN_FROM = 0;
    private final int LAN_TO = 1;
    private int mSetLan = LAN_FROM;
    
    // 语音
    private MediaPlayer mPlayer;
    private AssetManager mAssetManager;
    
    private SharedPreferences mAppSpf;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.translate_layout);
        
        mAppSpf = getSharedPreferences("TRANSLATE",MODE_PRIVATE);
        
        // 数据集
        mSortList = new TranslateUtil(this).getTranslateChild(getIntent().getIntExtra("pcid", 0), getIntent().getStringExtra("pname"));

        buildComponents();
        title.setText(getText(R.string.translate_list_title));
        mPlayer = new MediaPlayer();
        mAssetManager = getAssets();
        

    }
    
	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}
    
    @Override
	public void onDestroy() {
	    super.onDestroy();
	    mPlayer.release();
	}

	@Override
	public void onClick(View v) {
	    
	    switch (v.getId()) {
            case R.id.back_image:
                Navigator.finishActivity(this);
                break;
	    case R.id.translate_choose_btn_from:// 原文语种设置
	        if (mChooseLayout.isShown()) {
	            showSelectorDialog(false, "");
	        } else {
	            mSetLan = LAN_FROM;
	            showSelectorDialog(true, getString(R.string.translate_language_set_from));
	        }
	        break;
	        
	    case R.id.translate_choose_btn_to:// 目标语种设置
	        if (mChooseLayout.isShown()) {
	            showSelectorDialog(false, "");
	        } else {
	            mSetLan = LAN_TO;
	            showSelectorDialog(true, getString(R.string.translate_language_set_to));
	        }
	        break;
	        
	    case R.id.translate_choose_btn_close:// 关闭语言设置视图
	        showSelectorDialog(false, "");
	        break;
	        
	    case R.id.translate_iv_exchange:// 原文语种和目标语种互换
	        if (mChooseLayout.isShown()) showSelectorDialog(false, "");
	        
	        String from = mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_FROM, TranslateUtil.TRANSLATE_COLUMNS[0]);
	        String to = mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[2]);
	        
	        mAppSpf.edit().putString(TranslateUtil.SPF_TRANSLATE_FROM, to).commit();
	        mAppSpf.edit().putString(TranslateUtil.SPF_TRANSLATE_TO, from).commit();
	        
	        // 显示界面名称
	        mBtnFrom.setText(getLanguageName(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_FROM, TranslateUtil.TRANSLATE_COLUMNS[0])));
	        mBtnTo.setText(getLanguageName(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[2])));
	
	        // 重载列表
	        mAdapterContent.notifyDataSetChanged();
	        break;
	    }
	}

	/**
	 * 返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        if (mChooseLayout.isShown()) {
	            showSelectorDialog(false, "");
	            return true;
	        }
	    }
	    
	    return super.onKeyDown(keyCode, event);
	}

	private void buildComponents() {

        // 分组图标和名称
        mSortIcon = (ImageView) findViewById(R.id.translate_sort_icon);
        mSortName = (TextView) findViewById(R.id.translate_sort_name);       
        
        mSortIcon.setImageResource(TranslateFragment.mCatIcons[getIntent().getIntExtra("position", 0)]);
        mSortName.setText(TranslateFragment.mCatTitles[getIntent().getIntExtra("position", 0)]);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back_image);
        back.setOnClickListener(this);
        mBtnFrom = (Button) findViewById(R.id.translate_choose_btn_from);
        mBtnTo = (Button) findViewById(R.id.translate_choose_btn_to);
        mBtnClose = (Button) findViewById(R.id.translate_choose_btn_close);
        mBtnEx = (ImageView) findViewById(R.id.translate_iv_exchange);
        mChooseLayout = (RelativeLayout) findViewById(R.id.translate_choose_layout);
        mBtnFrom.setOnClickListener(this);
        mBtnTo.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        mBtnEx.setOnClickListener(this);
        
        // 翻译内容
        mLvContent = (ListView) findViewById(R.id.translate_content_listview);
        mAdapterContent = new SortAdapter();
        mLvContent.setAdapter(mAdapterContent);
        
        // 目标语种设置列表
        mTvTitle = (TextView) findViewById(R.id.translate_choose_title);
        mLvLanguage = (WheelView) findViewById(R.id.translate_choose_language);
        mLvLanguage.setAdapter(new WheelViewAdapter(TranslateUtil.TRANSLATE_SELECTOR_STRS));
        mLvLanguage.setUnselectedAlpha(0.34f);
        mLvLanguage.setScrollCycle(false);
        mLvLanguage.setOnItemSelectedListener(mWheelListener);

        // 目标语种设置
        mBtnFrom.setText(getLanguageName(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_FROM, TranslateUtil.TRANSLATE_COLUMNS[0])));
        mBtnTo.setText(getLanguageName(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[2])));
    }
    
    /**
     * 根据记录标识返回目标语种名称
     * @param key
     * @return
     */
    private String getLanguageName(String key) {
        String str = getString(TranslateUtil.TRANSLATE_SELECTOR_STRS[0]);
        for (int i = 0, len = TranslateUtil.TRANSLATE_COLUMNS.length; i < len; i++) {
            if (key.equalsIgnoreCase(TranslateUtil.TRANSLATE_COLUMNS[i])) {
                str = getString(TranslateUtil.TRANSLATE_SELECTOR_STRS[i]);
                break;
            }
        }
        String strs[] = str.split(" ");
        return strs[0];
    }

    /**
     * 显示语言设置对话框(源语言和目标语言)
     */
    private void showSelectorDialog(boolean isShow, String title) {
   /*     if (!isShow) {// 关闭设置视图
            mChooseLayout.setVisibility(View.GONE);
            mChooseLayout.setAnimation(AnimUtil.setTranslateAnim(0, 0, 0, -1, 300));
        } else {      // 显示
            mChooseLayout.setVisibility(View.VISIBLE);
            mChooseLayout.setAnimation(AnimUtil.setTranslateAnim(0, 0, -1, 0, 300));
            mTvTitle.setText(title);

            // 设置当前选择项
            String key = mAppSpf.getString(mSetLan == LAN_FROM ? TranslateUtil.SPF_TRANSLATE_FROM : TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[0]);
            for (int i = 0, len = TranslateUtil.TRANSLATE_COLUMNS.length; i < len; i++) {
                if (key.equalsIgnoreCase(TranslateUtil.TRANSLATE_COLUMNS[i])) {
                    mLvLanguage.setSelection(i, true);
                    break;
                }
            }
        }*/
    }
    
    /**
     * 分类列表适配器
     * @author blackieqq
     * @since 2014-9-10
     */
    private class SortAdapter extends BaseAdapter {
        
        boolean defaultOpen = true;
        
        @Override
        public int getCount() {
            return mSortList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }
        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @SuppressWarnings("unchecked")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.translate_sort_lvitem, null);
                holder.title = (TextView) convertView.findViewById(R.id.translate_sort_lvitem_title);
                holder.childLv = (CustomListView) convertView.findViewById(R.id.translate_sort_lvitem_listview);
                holder.open = (LinearLayout) convertView.findViewById(R.id.translate_sort_lvitem_open);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            // 当分类数目为1时, 默认展开显示
            if (mSortList.size() == 1) {
                holder.open.setVisibility(View.GONE);
                holder.childLv.setVisibility(View.VISIBLE);
            }
            
            // 数据填充
            Map<String, Object> map = mSortList.get(position);
            TranslateEntity entity = (TranslateEntity) map.get("sortEntity");
            List<Map<String, Object>> sentenceList = (List<Map<String, Object>>) map.get("sentenceList");
            holder.title.setText(entity.getName());
            holder.childLv.setAdapter(new ChildAdapter(sentenceList));// 子列表
            
            // 展开事件
            holder.open.setTag(holder.childLv);
            holder.open.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {  
                    
                    CustomListView lv = (CustomListView) v.getTag();
                    if (openChildLv == null) {
                        openChildLv = lv;
                        openChildLv.setVisibility(View.VISIBLE);

                        openSort = (LinearLayout) v;
                        openSort.setBackgroundResource(R.drawable.translate_sort_tvbg_pressed);
                        openArrow = (ImageView) openSort.findViewById(R.id.translate_sort_lvitem_arrow);
                        openArrow.setImageResource(R.mipmap.arrow_top);
                    }
                    else {
                        if (lv == openChildLv) {
                            openChildLv.setVisibility(View.GONE);
                            openChildLv = null;

                            openSort.setBackgroundResource(R.drawable.translate_sort_tvbg_selected);
                            openSort = null;
                            openArrow.setImageResource(R.mipmap.arrow_bottom);
                            openArrow = null;
                        }
                        else {
                            openChildLv.setVisibility(View.GONE);
                            openSort.setBackgroundResource(R.drawable.translate_sort_tvbg_selected);
                            openArrow.setImageResource(R.mipmap.arrow_bottom);
                            
                            openChildLv = lv;
                            openChildLv.setVisibility(View.VISIBLE);
                            ((LinearLayout) v).setBackgroundResource(R.drawable.translate_sort_tvbg_pressed);
                            
                            openSort = (LinearLayout) v;
                            openSort.setBackgroundResource(R.drawable.translate_sort_tvbg_pressed);
                            openArrow = (ImageView) openSort.findViewById(R.id.translate_sort_lvitem_arrow);
                            openArrow.setImageResource(R.mipmap.arrow_top);
                        }
                    }
                }
            });
            
            if (defaultOpen && position == 0) {
                openChildLv = holder.childLv;
                openChildLv.setVisibility(View.VISIBLE);
                openSort = (LinearLayout) holder.open;
                openSort.setBackgroundResource(R.drawable.translate_sort_tvbg_pressed);
                openArrow = (ImageView) openSort.findViewById(R.id.translate_sort_lvitem_arrow);
                openArrow.setImageResource(R.mipmap.arrow_top);
                
                defaultOpen = false;
            }
            
            return convertView;
        }
        
        private LinearLayout openSort;
        private CustomListView openChildLv;
        private ImageView openArrow;

        private class ViewHolder {
            TextView title;
            CustomListView childLv;
            LinearLayout open;
        }
    }
    
    /**
     * 子列表适配器 
     * @author blackieqq
     * @since 2014-9-10
     */
    private class ChildAdapter extends BaseAdapter {

		private final String STR_ZH = "zh/zh_%d.mp3";
        private final String STR_EN = "en/en_%d.mp3";
        private final String STR_TR = "tr/Tr_%d.mp3";
        private final String STR_IT = "it/it_%d.mp3";
        
        private List<Map<String, Object>> mSentenceList = new ArrayList<Map<String,Object>>();
        
        public ChildAdapter(List<Map<String, Object>> sentenceList) {
            this.mSentenceList = sentenceList;
        }
        
        @Override
        public int getCount() {
            return mSentenceList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }
        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getLayoutInflater().inflate(R.layout.translate_child_lvitem, null);
                holder.from = (TextView) convertView.findViewById(R.id.translate_child_lvitem_from);
                holder.to = (TextView) convertView.findViewById(R.id.translate_child_lvitem_to);
                holder.voice = (ImageView) convertView.findViewById(R.id.translate_child_lvitem_voice);
                holder.line = (View) convertView.findViewById(R.id.translate_child_lvitem_line);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }
            
            Map<String, Object> map = mSentenceList.get(position);
            
            TranslateEntity entityFrom = (TranslateEntity) map.get(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_FROM, TranslateUtil.TRANSLATE_COLUMNS[0]));
            TranslateEntity entityTo = (TranslateEntity) map.get(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[2]));
            holder.from.setText(entityFrom.getSentence());
            holder.to.setText(entityTo.getSentence());
            
            // 最后一项隐藏分割线
            if (position==mSentenceList.size()-1) holder.line.setVisibility(View.GONE);
            else holder.line.setVisibility(View.VISIBLE);
            
            // 语音
            holder.voice.setTag(entityTo.getNo());
            holder.voice.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int no = Integer.valueOf(v.getTag().toString());
                    String fileName = String.format(Locale.ENGLISH, STR_ZH, no);
                    // 播放英语
                    if (TranslateUtil.TRANSLATE_COLUMNS[1].equalsIgnoreCase(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[1]))) {
                    	fileName = String.format(Locale.ENGLISH, STR_EN, no);
                    }
                    // 播放土耳其语音
                    else if (TranslateUtil.TRANSLATE_COLUMNS[2].equalsIgnoreCase(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[1]))) {
                        fileName = String.format(Locale.ENGLISH, STR_TR, no);
                    }
                    // 播放汉语
                    else if (TranslateUtil.TRANSLATE_COLUMNS[0].equalsIgnoreCase(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[1]))) {
					    fileName = String.format(Locale.ENGLISH, STR_ZH, no);
					}
                    
                    try {
                        if (mPlayer.isPlaying()) {
                            mPlayer.stop();
                        }
                        mPlayer.reset();
                        
                        AssetFileDescriptor afd = mAssetManager.openFd(fileName);
                        mPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        mPlayer.prepare();
                        mPlayer.start();
                    } catch (IOException e) {
                        Log.e("ex", "ex", e);
                    }
                }
            });
            return convertView;
        }
        
        private class ViewHolder {
            TextView from;
            TextView to;
            ImageView voice;
            View line;
        }
    }
    
    /**
     * TODO 以下为源语言和目标语言设置列表方法
     * Wheel选择监听
     */
    private TosAdapterView.OnItemSelectedListener mWheelListener = new TosAdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(TosAdapterView<?> parent, View view, int position, long id) {
            ((WheelTextView) view).setTextSize(17);
            int index = Integer.parseInt(view.getTag().toString());
            int count = parent.getChildCount();
            if (index < count - 1) {
                ((WheelTextView) parent.getChildAt(index + 1)).setTextSize(15);
            }
            if (index > 0) {
                ((WheelTextView) parent.getChildAt(index - 1)).setTextSize(15);
            }
            
            String save = TranslateUtil.TRANSLATE_COLUMNS[position];
            
            // 设置转换原文语种
            if (mSetLan == LAN_FROM) {
                // 原文语种设置与目标语种相同时
                if (save.equalsIgnoreCase(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[2])))
                    mAppSpf.edit().putString(TranslateUtil.SPF_TRANSLATE_TO, mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_FROM, TranslateUtil.TRANSLATE_COLUMNS[0])).commit();
                
                mAppSpf.edit().putString(TranslateUtil.SPF_TRANSLATE_FROM, save).commit();
            }
            // 设置目标翻译语言
            else {
                // 目标语种设置与原文语种相同时
                if (save.equalsIgnoreCase(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_FROM, TranslateUtil.TRANSLATE_COLUMNS[0])))
                    mAppSpf.edit().putString(TranslateUtil.SPF_TRANSLATE_FROM, mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[2])).commit();
                
                mAppSpf.edit().putString(TranslateUtil.SPF_TRANSLATE_TO, save).commit();
            }
            
            // 显示界面名称
            mBtnFrom.setText(getLanguageName(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_FROM, TranslateUtil.TRANSLATE_COLUMNS[0])));
            mBtnTo.setText(getLanguageName(mAppSpf.getString(TranslateUtil.SPF_TRANSLATE_TO, TranslateUtil.TRANSLATE_COLUMNS[2])));
            
            // 重载列表
            mAdapterContent.notifyDataSetChanged();    
        }
        @Override
        public void onNothingSelected(TosAdapterView<?> parent) {
        }
    };
    
    /**
     * Wheel适配器
     */
    private class WheelViewAdapter extends BaseAdapter {
        int mHeight = 40;
        int[] mData = null;

        public WheelViewAdapter(int[] data) {
            mHeight = (int) dipToPx(TranslateActivity.this, mHeight);
            this.mData = data;
        }

        @Override
        public int getCount() {
            return (null != mData) ? mData.length : 0;
        }

        @Override
        public View getItem(int arg0) {
            return getView(arg0, null, null);
        }

        @Override
        public long getItemId(int arg0) {
            return arg0;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            WheelTextView textView = null;

            if (null == convertView) {
                convertView = new WheelTextView(TranslateActivity.this);
                convertView.setLayoutParams(new TosGallery.LayoutParams(-1, mHeight));
                textView = (WheelTextView) convertView;
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER);
            }
            
            String text = getString(mData[position]);
            if (null == textView) {
                textView = (WheelTextView) convertView;
            }
            
            textView.setText(text);
            return convertView;
        }
    }
    
    /**
     * 字体设置
     */
    private class WheelTextView extends TextView {
        public WheelTextView(Context context) {
            super(context);
        }
        public WheelTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }
        @Override
        public void setTextSize(float size) {
            Context c = getContext();
            Resources r;
            if (c == null) r = Resources.getSystem();
            else r = c.getResources();
           
            float rawSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics());
            if (rawSize != getPaint().getTextSize()) {
                getPaint().setTextSize(rawSize);
                if (getLayout() != null) {
                    invalidate();
                }
            }
        }
    }
    private int dipToPx(Context context, int dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());
    }
}
