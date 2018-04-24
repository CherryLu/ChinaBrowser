package com.chinabrowser.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.bean.Content;
import com.chinabrowser.bean.HomeTab;
import com.chinabrowser.bean.Title;
import com.chinabrowser.cbinterface.HomeCallBack;
import com.chinabrowser.fragment.HomeFragment;
import com.chinabrowser.fragment.HotNewsFragment;
import com.chinabrowser.fragment.LabFragment;
import com.chinabrowser.fragment.SearchFragment;
import com.chinabrowser.fragment.TranslateFragment;
import com.chinabrowser.fragment.WebViewFragment;
import com.chinabrowser.utils.Constant;
import com.chinabrowser.utils.LogUtils;
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
    private WebViewFragment webViewFragment;
    private SearchFragment searchFragment;

    private LabFragment labFragment;

    private Fragment[] fragments = new Fragment[3];
    private int current = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setContainer(0,null,"","");
        // 初始横竖屏状况
        mOrientation = getResources().getConfiguration().orientation;
        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        mHeight = getWindowManager().getDefaultDisplay().getHeight();


        new Handler().postDelayed(new Runnable(){
            public void run() {
                HomeTab homeTab = new HomeTab();
                homeTab.title = getString(R.string.homepage);
                homeTab.bitmap = takeScreenShot(MainActivity.this);
                APP.homeTab  = homeTab;
            }
        }, 500);
    }

    private void setContainer(int which,Title title,String id,String url){
        switch (which){
            case 0:
                if (homeFragment==null){
                    homeFragment = new HomeFragment();
                }
                homeFragment.setHomeCallBack(this);
                getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();
                fragments[0] = homeFragment;
                current = 0;
                break;
            case 1:
                hotNewsFragment = new HotNewsFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("TITLE", title);
                hotNewsFragment.setArguments(bundle);
                hotNewsFragment.homeCallBack = this;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, hotNewsFragment).commit();
                fragments[1] = hotNewsFragment;
                current = 1;
                break;
            case 2:
                webViewFragment = new WebViewFragment();
                Bundle bun = new Bundle();
                bun.putString("ID", id);
                webViewFragment.homeCallBack = this;
                webViewFragment.setArguments(bun);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, webViewFragment).commit();
                if (current==0){
                    fragments[1] = webViewFragment;
                    current = 1;
                }else if (current==1){
                    fragments[2] = webViewFragment;
                    current = 2;
                }
                break;
            case 3:
                if (translateFragment==null){
                    translateFragment = new TranslateFragment();
                    translateFragment.homeCallBack = this;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.container,translateFragment).commit();
                fragments[1] = translateFragment;
                current = 1;
                break;
            case 4: {
                    HomeTab homeTab = new HomeTab();
                    homeTab.bitmap = takeScreenShot(this);
                    homeTab.title = "新页签";
                    labFragment = new LabFragment();
                    labFragment.homeCallBack = this;
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("PAGE",homeTab);
                    labFragment.setArguments(bundle1);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, labFragment).commit();
                    fragments[1] = labFragment;
                    current = 1;
                break;
            }
            case 5:{
                webViewFragment = new WebViewFragment();
                Bundle bund = new Bundle();
                bund.putString("URL", url);
                bund.putBoolean("ISURL",true);
                webViewFragment.homeCallBack = this;
                webViewFragment.setArguments(bund);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, webViewFragment).commit();
                if (current==0){
                    fragments[1] = webViewFragment;
                    current = 1;
                }else if (current==1){
                    fragments[2] = webViewFragment;
                    current = 2;
                }
                break;
            }

            case 6:
                if (searchFragment == null){
                    searchFragment = new SearchFragment();
                }
                searchFragment.homeCallBack = this;
                getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                fragments[1] = searchFragment;

                break;

        }

        isCangoBack();
        isCangoFroward();
    }

    private void isCangoBack(){
     if (current==0){//不可退
         showCanBack(1);
     }else {
         showCanBack(0);
     }
    }

    private void isCangoFroward(){
      if (current==2){
          if (fragments[2] instanceof WebViewFragment){
              webViewFragment = (WebViewFragment) fragments[2];
              if (webViewFragment.canGoForward()){
                  showCanForword(0);
              }
          }else {
              showCanForword(1);
          }
      }else if (current==1){
          if (fragments[2]!=null){
              showCanForword(0);
          }else {
              showCanForword(1);
          }
      }else if (current==0){
          if (fragments[1]!=null){
              showCanForword(0);
          }else {
              showCanForword(1);
          }
      }
    }

    /**
     * 0 可返回 1  不可返回
     * @param which
     */
    private void showCanBack(int which){
        if (which==0){
            indexBottomMenuGoback.setVisibility(View.VISIBLE);
            indexBottomMenuNogoback.setVisibility(View.GONE);
        }else {
            indexBottomMenuGoback.setVisibility(View.GONE);
            indexBottomMenuNogoback.setVisibility(View.VISIBLE);
        }

    }


    /**
     * 0 可向前 1 不可向前
     * @param which
     */
    private void showCanForword(int which){
        if (which == 0){
            indexBottomMenuGoforward.setVisibility(View.VISIBLE);
            indexBottomMenuNogoforward.setVisibility(View.GONE);
        }else {
            indexBottomMenuGoforward.setVisibility(View.GONE);
            indexBottomMenuNogoforward.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.index_bottom_menu_goback,R.id.index_bottom_menu_more, R.id.index_bottom_menu_nogoback, R.id.index_bottom_menu_goforward, R.id.index_bottom_menu_nogoforward, R.id.index_bottom_menu_gohome, R.id.index_bottom_menu_nogohome, R.id.index_bottom_menu_new_window})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.index_bottom_menu_more:
                Navigator.startSettingActivity(this);
                break;
            case R.id.index_bottom_menu_goback:
                if (current==1){
                    Fragment fragment = fragments[1];
                    if (fragment instanceof WebViewFragment){
                        webViewFragment = (WebViewFragment) fragment;
                        if (webViewFragment.canGoBack()){
                            webViewFragment.goBack();
                        }else {
                            setContainer(0,null,"","");
                        }
                    }
                }else if (current ==2){
                    Fragment fragment = fragments[2];
                    if (fragment instanceof WebViewFragment){
                        webViewFragment = (WebViewFragment) fragment;
                        if (webViewFragment.canGoBack()){
                            webViewFragment.goBack();
                        }else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments[1]).commit();
                            current = 1;
                        }
                    }
                }
                isCangoBack();
                isCangoFroward();
                break;
            case R.id.index_bottom_menu_goforward:
                if (current == 0){
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments[1]).commit();
                    current = 1;
                }else if (current==1){
                    Fragment fragment = fragments[1];
                    if (fragment instanceof WebViewFragment){
                        webViewFragment = (WebViewFragment) fragment;
                        if (webViewFragment.canGoForward()){
                            webViewFragment.goForward();
                        }else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragments[2]).commit();
                            current = 2;
                        }
                    }
                }
                isCangoBack();
                isCangoFroward();
                break;
            case R.id.index_bottom_menu_gohome:
              setContainer(0,null,"","");
                break;
            case R.id.index_bottom_menu_new_window://添加页签
                setContainer(4,null,"","");
                break;
        }
    }

    @Override
    public void titleClick(int which,Title title) {
        switch (which){
            case Constant.HOT_TITLE: {
                setContainer(1,title,"","");
                break;
            }
            case Constant.TRA_TITLE: {
                setContainer(1,title,"","");
                break;
            }
            case Constant.CHINA_TITLE:
                setContainer(1,title,"","");
                break;
            case Constant.TRANSLATE_TITLE:
                setContainer(3,title,"","");
                break;
            case Constant.SEARCHLAYOUT:
                setContainer(6,null,"","");

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (current==0){
            super.onBackPressed();
        }else {
            Fragment fragment = fragments[current-1];
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
             current = current -1;
        }

        if (APP.homeTabs==null||APP.homeTabs.size()==0){
            indexBottomTabCount.setText("1");
        }else {
            indexBottomTabCount.setText(APP.homeTabs.size()+"");
        }
    }

    @Override
    public void backClick() {
        setContainer(0,null,"","");
        if (APP.homeTabs==null||APP.homeTabs.size()==0){
            indexBottomTabCount.setText("1");
        }else {
            indexBottomTabCount.setText(APP.homeTabs.size()+"");
        }

    }

    @Override
    public void startContent(Content content) {
        String id = content.getId();
        if (id!=null){
         setContainer(2,null,id,"");
        }
    }

    @Override
    public void startContentByurl(Content content) {
        String url = content.getLink_url();
        if (TextUtils.isEmpty(url)){
            url = content.getCopy_url();
        }
        if (!url.startsWith("https://")){
            url = "https://"+url;
        }
        LogUtils.e("WEB",url);
        setContainer(5,null,"",url);
    }

    public int mOrientation;            // 横竖屏标识
    public int mWidth;
    public int mHeight;
    /**
     * 获取区域截屏，多标签显示
     * @param activity
     * @return
     */
    private Bitmap takeScreenShot(Activity activity){

        View view =activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top + 20;

        int width = mOrientation == Configuration.ORIENTATION_PORTRAIT ? mWidth : mHeight;
        int height = mOrientation == Configuration.ORIENTATION_PORTRAIT ? mWidth : mHeight;

        Bitmap bitmap2 = Bitmap.createBitmap(bitmap,0,statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bitmap2;
    }


}
