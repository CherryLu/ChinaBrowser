package com.chinabrowser.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.chinabrowser.bean.History;
import com.chinabrowser.bean.NewsDetail;
import com.chinabrowser.db.HistoryManager;
import com.chinabrowser.net.CollectionNewsPage;
import com.chinabrowser.net.DelCollectionNewsPage;
import com.chinabrowser.net.GetNewsDetails;
import com.chinabrowser.net.UpCollectionNews;
import com.chinabrowser.net.UpGetNewsDetail;
import com.chinabrowser.ui.BrowserVideoPlayer;
import com.chinabrowser.ui.RedPacketCustomDialog;
import com.chinabrowser.ui.RewritePopwindow;
import com.chinabrowser.utils.CommUtils;
import com.chinabrowser.utils.LogUtils;
import com.chinabrowser.utils.Navigator;
import com.chinabrowser.utils.UserManager;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.facebook.Facebook;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.google.GooglePlus;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.twitter.Twitter;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2018/4/1.
 */

public class WebViewFragment extends BaseFragment implements BrowserVideoPlayer.VideoPlayInterface {
    @Bind(R.id.webview)
    WebView webview;

    /**
     * 用户代理 User agents.
     */
    public static String UA_DEFAULT = "";
    public static String UA_DESKTOP = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Chrome/7.0.517.44 Safari/534.7";
    public static String UA_IPHONE = "Mozilla/5.0 (iPhone Simulator; U; CPU iPhone OS 4_1 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/8B117";
    public static final String[] USER_AGENTS = new String[]{UA_DEFAULT, UA_DESKTOP, UA_IPHONE};
    @Bind(R.id.back_image)
    ImageView backImage;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.right_txt)
    TextView rightTxt;
    @Bind(R.id.collection)
    ImageView collection;
    @Bind(R.id.share)
    ImageView share;
    @Bind(R.id.news_layout)
    LinearLayout newsLayout;
    @Bind(R.id.web_title)
    TextView webTitle;
    @Bind(R.id.from)
    TextView from;
    @Bind(R.id.time)
    TextView time;
    @Bind(R.id.others)
    LinearLayout others;
    //@Bind(R.id.video)
    BrowserVideoPlayer video;
    @Bind(R.id.video_layout)
    RelativeLayout videoLayout;


    private Method mMethodFinding = null;// 开始查找
    private Method mMethodFinded = null;// 查找结束

    private int mLoadPregress = 100;     // 载入进度
    private int position = 0;            // 序号
    private boolean iscollection;

    private String getHtmlData(String bodyHTML) {
        String head = "<head><style>img{max-width: 100%; width:100%; height: auto;}</style></head>";
        return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
    }

    public static String formatA(String value) {
        if (value == null)
            return "";
        //html的标签属性肯定是以单引号或双引号包含
        String pattern = "<a([\\w\\W]*?) href=['|\"]([\\w\\W]*?)['|\"]([\\w\\W]*?)>";
        Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(value);
        while (m.find()) {
            String a = m.group();
            String _url = m.group(2);
            String url = _url;

            try {
                url = URLEncoder.encode(_url, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            url = "";
            //将target属性替换为""，以便所有外链都是_blank打开
            String targetReg = "target=['|\"].*?['|\"']";
            String prefix = m.group(1).replaceAll(targetReg, "");
            String suffix = m.group(3).replaceAll(targetReg, "");
            //整段匹配不会出现错误
            String _a = "";
            value = value.replace(a, _a);
        }
        return value;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GetNewsDetails.MSG_WHAT_OK:
                    if (getNewsDetails != null && getNewsDetails.newsDetail != null) {
                        NewsDetail newsDetail = getNewsDetails.newsDetail;
                        setVideo(newsDetail.vcontent);
                        LogUtils.e("getHtmlData",getHtmlData(newsDetail.content));
                        LogUtils.e("getHtmlData",formatA(getHtmlData(newsDetail.content)));
                        webview.loadData(formatA(getHtmlData(newsDetail.content)), "text/html; charset=UTF-8", null);
                        webTitle.setText(newsDetail.title);
                        from.setText(newsDetail.copy_from);
                        time.setText(CommUtils.getTime(newsDetail.update_time));
                        if (TextUtils.isEmpty(newsDetail.iftag) || newsDetail.iftag.equals("0")) {
                            iscollection = false;
                            collection.setImageResource(R.mipmap.un_collection);
                        } else if (!TextUtils.isEmpty(newsDetail.iftag) && newsDetail.iftag.equals("1")) {
                            iscollection = true;
                            collection.setImageResource(R.mipmap.collection);
                        }
                    }
                    break;
                case GetNewsDetails.MSG_WHAT_ERROE:
                case GetNewsDetails.MSG_WHAT_NOTCHANGE:
                    break;
                case CollectionNewsPage.MSG_WHAT_OK: {
                    final RedPacketCustomDialog customDialog = new RedPacketCustomDialog(getContext(), 0);
                    customDialog.show();
                    collection.setImageResource(R.mipmap.collection);
                    iscollection = true;

                    TimerTask task = new TimerTask() {
                        public void run() {
                            customDialog.dismiss();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);

                    break;
                }
                case CollectionNewsPage.MSG_WHAT_ERROE:
                case CollectionNewsPage.MSG_WHAT_NOTCHANGE:

                    break;
                case DelCollectionNewsPage.MSG_WHAT_OK:{


                    final RedPacketCustomDialog custom = new RedPacketCustomDialog(getContext(), 1);
                    custom.show();
                    collection.setImageResource(R.mipmap.un_collection);
                    iscollection = false;
                    TimerTask task = new TimerTask(){
                        public void run(){
                            custom.dismiss();
                        }
                    };
                    Timer timer = new Timer();
                    timer.schedule(task, 1000);
                    break;
                }
                case DelCollectionNewsPage.MSG_WHAT_ERROE:
                case DelCollectionNewsPage.MSG_WHAT_NOTCHANGE:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setUrl(String url) {
        this.url = url;
        if (webview != null) {
            webview.loadUrl(url);
        }
    }



    public String newsId;
    boolean isUrl;
    String url;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_webview, null);
        ButterKnife.bind(this, rootView);
        rootView.findViewById(R.id.titlle_layout).setVisibility(View.GONE);
        isUrl = getArguments().getBoolean("ISURL");
        initWebView(webview);
        if (isUrl) {
            webTitle.setVisibility(View.GONE);
            others.setVisibility(View.GONE);

            newsLayout.setVisibility(View.GONE);
            webview.setPadding(0,0,0,0);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) webview.getLayoutParams();
            params.setMargins(0,0,0,0);
            webview.setLayoutParams(params);
            url = getArguments().getString("URL");
            LogUtils.e("ZX", "url : " + url);
            webview.loadUrl(url);
        } else {
            webview.setPadding(CommUtils.dip2px(getContext(),12),0,CommUtils.dip2px(getContext(),12),0);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) webview.getLayoutParams();
            params.setMargins(0,CommUtils.dip2px(getContext(),20),0,0);
            webview.setLayoutParams(params);

            webTitle.setVisibility(View.VISIBLE);
            others.setVisibility(View.VISIBLE);
            newsLayout.setVisibility(View.VISIBLE);
            newsId = getArguments().getString("ID");
            getNewsDetails();
        }
        return rootView;
    }

    CollectionNewsPage collectionNewsPage;
    UpCollectionNews upCollectionNews;

    private void checkIsCollection() {
        if (UserManager.getInstance().isLogin()) {
            upCollectionNews = new UpCollectionNews();
            upCollectionNews.id = newsId;
            upCollectionNews.ilanguage = CommUtils.getCurrentLag(getContext()) + 1 + "";
            upCollectionNews.suserno = UserManager.getInstance().getUserId();
            if (collectionNewsPage == null) {
                collectionNewsPage = new CollectionNewsPage(upCollectionNews, handler, null);
            }
            collectionNewsPage.refresh(upCollectionNews);
        } else {
            Navigator.startLoginActivity(getContext());
        }

    }


    public void setFullScreen(){
        if (video!=null){
            video.setFullScreen();
        }
    }

    public void setNomalScreen(){
        if (video!=null){
            video.setNormalScreen();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden){
            if (video!=null&&videoLayout.getVisibility()==View.VISIBLE){
                video.destory();
                videoLayout.removeAllViews();
            }
        }
        super.onHiddenChanged(hidden);
    }

    private void setVideo(String url) {
        if (TextUtils.isEmpty(url)) {
            if (videoLayout!=null){
                videoLayout.setVisibility(View.GONE);
            }
        } else {
            url = CommUtils.getBaseurl(getContext()) + url;
            LogUtils.e("VIDEO", "url : " + url);
            if (videoLayout!=null){
                if (video!=null){
                    video.destory();
                }
                video = new BrowserVideoPlayer(getContext());
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(CommUtils.dip2px(getContext(),336), CommUtils.dip2px(getContext(),140));
                video.setLayoutParams(params);
                videoLayout.addView(video);
                videoLayout.setVisibility(View.VISIBLE);
                video.setActivity(getActivity(),false);
                video.setVideoPlayInterface(this);
                video.setData(url,CommUtils.dip2px(getContext(),175));
            }
        }

    }

    public boolean canGoBack() {
        if (webview != null) {
            return webview.canGoBack();
        }
        return false;
    }

    public void goBack() {
        if (webview != null) {
            webview.goBack();
        }
    }

    public void goForward() {
        if (webview != null) {
            webview.goForward();
        }
    }

    public boolean canGoForward() {
        if (webview != null) {
            return webview.canGoForward();
        }
        return false;
    }

    DelCollectionNewsPage delCollectionNewsPage;
    UpCollectionNews collectionNews;

    private void deleCollection(String id) {
        collectionNews = new UpCollectionNews();
        collectionNews.suserno = UserManager.getInstance().getUserId();
        collectionNews.id = id;
        collectionNews.ilanguage = CommUtils.getCurrentLag(getContext()) + 1 + "";

        if (delCollectionNewsPage == null) {
            delCollectionNewsPage = new DelCollectionNewsPage(collectionNews, handler, null);
        }
        delCollectionNewsPage.refresh(collectionNews);

    }


    GetNewsDetails getNewsDetails;
    UpGetNewsDetail upGetNewsDetail;

    private void getNewsDetails() {
        upGetNewsDetail = new UpGetNewsDetail();
        upGetNewsDetail.id = newsId;
        if (UserManager.getInstance().isLogin()) {
            upGetNewsDetail.suserno = UserManager.getInstance().getUserId();
        }
        if (getNewsDetails == null) {
            getNewsDetails = new GetNewsDetails(upGetNewsDetail, handler, null);
        }
        getNewsDetails.refresh(upGetNewsDetail);
    }

    /**
     * 初始化方法: 暂停|继续|查找|查找结束
     */
    private void initMethod() {
        try {
            mMethodFinding = this.getClass().getMethod("setFindIsUp", Boolean.TYPE);
            mMethodFinded = this.getClass().getMethod("notifyFindDialogDismissed");
        } catch (SecurityException e) {
            e.printStackTrace();
            mMethodFinding = null;
            mMethodFinded = null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            mMethodFinding = null;
            mMethodFinded = null;
        }
    }


    private void initShare() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.share1:
                        if (CommUtils.getCurrentLag(getContext()) == 0) {
                            shareData(0);
                        } else {
                            shareData(4);
                        }
                        break;
                    case R.id.share2:
                        if (CommUtils.getCurrentLag(getContext()) == 0) {
                            shareData(1);
                        } else {
                            shareData(5);
                        }
                        break;
                    case R.id.share3:
                        if (CommUtils.getCurrentLag(getContext()) == 0) {
                            shareData(2);
                        } else {
                            shareData(6);
                        }
                        break;
                    case R.id.share4:
                        if (CommUtils.getCurrentLag(getContext()) == 0) {
                            shareData(3);
                        } else {

                        }
                        break;
                }
            }
        };
        RewritePopwindow rewritePopwindow = new RewritePopwindow(getActivity(), listener);
        rewritePopwindow.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void shareData(int which) {
        if (getNewsDetails != null && getNewsDetails.newsDetail != null) {
            NewsDetail detail = getNewsDetails.newsDetail;
            String plat = "";
            switch (which) {
                case 0: {
                    Wechat.ShareParams shareParams = new Wechat.ShareParams();
                    shareParams.setTitle(detail.title);
                    shareParams.setUrl(detail.copy_url);
                    shareParams.setImageUrl(CommUtils.getBaseurl(getContext()) + detail.cover_image);
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    Platform platform = ShareSDK.getPlatform(Wechat.NAME);
                    platform.share(shareParams);
                    break;
                }
                case 1: {
                    WechatMoments.ShareParams shareParams = new WechatMoments.ShareParams();
                    shareParams.setTitle(detail.title);
                    shareParams.setUrl(detail.copy_url);
                    shareParams.setImageUrl(CommUtils.getBaseurl(getContext()) + detail.cover_image);
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    Platform platform = ShareSDK.getPlatform(WechatMoments.NAME);
                    platform.share(shareParams);
                    break;
                }
                case 2: {
                    SinaWeibo.ShareParams shareParams = new SinaWeibo.ShareParams();
                    shareParams.setTitle(detail.title);
                    shareParams.setText(detail.copy_url);
                    shareParams.setImageUrl(CommUtils.getBaseurl(getContext()) + detail.cover_image);
                    Platform platform = ShareSDK.getPlatform(SinaWeibo.NAME);
                    platform.share(shareParams);
                    break;
                }
                case 3: {
                    QQ.ShareParams shareParams = new QQ.ShareParams();
                    shareParams.setImageUrl(CommUtils.getBaseurl(getContext()) + detail.cover_image);
                    shareParams.setTitleUrl(detail.copy_url);
                    shareParams.setTitle(detail.title);
                    shareParams.setText(detail.title);
                    Platform platform = ShareSDK.getPlatform(QQ.NAME);
                    platform.share(shareParams);
                    break;
                }

                case 4: {
                    GooglePlus.ShareParams shareParams = new GooglePlus.ShareParams();
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    shareParams.setText(detail.title);
                    shareParams.setImageUrl(CommUtils.getBaseurl(getContext()) + detail.cover_image);
                    Platform platform = ShareSDK.getPlatform(GooglePlus.NAME);
                    platform.share(shareParams);
                    break;
                }
                case 5: {
                    Twitter.ShareParams shareParams = new Twitter.ShareParams();
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    shareParams.setText(detail.title);
                    shareParams.setImageUrl(CommUtils.getBaseurl(getContext()) + detail.cover_image);
                    Platform platform = ShareSDK.getPlatform(Twitter.NAME);
                    platform.share(shareParams);
                }
                break;
                case 6: {
                    Facebook.ShareParams shareParams = new Facebook.ShareParams();
                    shareParams.setShareType(Platform.SHARE_WEBPAGE);
                    shareParams.setText(detail.title);
                    shareParams.setImageUrl(CommUtils.getBaseurl(getContext()) + detail.cover_image);
                    Platform platform = ShareSDK.getPlatform(Facebook.NAME);
                    platform.share(shareParams);
                    break;
                }
            }


        }

    }

    private void initWebView(WebView ActionwebView) {
        if (!isUrl) {
            ActionwebView.getSettings().setDefaultFontSize(40);
        }
        // ActionwebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
        ActionwebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //对离线应用的支持
        ActionwebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
        String appCacheDir = APP.getContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        ActionwebView.getSettings().setAppCachePath(appCacheDir);
        ActionwebView.getSettings().setAllowFileAccess(true);
        ActionwebView.getSettings().setAppCacheEnabled(true);
        ActionwebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

        ActionwebView.getSettings().setJavaScriptEnabled(true);
        ActionwebView.setHorizontalScrollBarEnabled(true);
        ActionwebView.setVerticalScrollBarEnabled(true);
        ActionwebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        ActionwebView.setBackgroundColor(0);
        ActionwebView.setDownloadListener(new MyWebViewDownLoadListener());
        ActionwebView.setWebViewClient(new MyWebViewClient());
        ActionwebView.setWebChromeClient(new MyWebChromeClient());
        // 设置可以访问文件
        ActionwebView.getSettings().setAllowFileAccess(true);
        ActionwebView.getSettings().setDatabaseEnabled(true);
        String dir = APP.getContext()
                .getDir("database", Context.MODE_PRIVATE).getPath();
        ActionwebView.getSettings().setDatabasePath(dir);
        // 使用localStorage则必须打开
        ActionwebView.getSettings().setDomStorageEnabled(true);
        ActionwebView.getSettings().setGeolocationEnabled(true);
        //必须加这个视频点击的时候 会转圈下后面就加载失败
        ActionwebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        ActionwebView.getSettings().setUseWideViewPort(true); // 关键点
        ActionwebView.getSettings().setSupportZoom(true); // 支持缩放
        ActionwebView.getSettings().setLoadWithOverviewMode(true);
        ActionwebView.getSettings().setDefaultTextEncodingName("utf-8");
        String defaultUa = ActionwebView.getSettings().getUserAgentString();
        ActionwebView.getSettings().setUserAgent(defaultUa);

//        5.0 以上的手机要加这个
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActionwebView.getSettings().setMixedContentMode(WebSettings.LOAD_NORMAL);
        }
        //initDownUtilsView();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.back_image)
    public void onClick() {
        if (homeCallBack != null) {
            homeCallBack.backClick();
        }
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        webview.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.width = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    @OnClick({R.id.collection, R.id.share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.collection:
                if (UserManager.getInstance().isLogin()) {
                    if (iscollection) {
                        deleCollection(newsId);
                    } else {
                        checkIsCollection();
                    }
                } else {
                    Navigator.startLoginActivity(getContext());
                }

                break;
            case R.id.share:
                initShare();
                break;
        }
    }

    private void intoFull(){
        if (webTitle!=null){
            webTitle.setVisibility(View.GONE);
        }

        if (others!=null){
            others.setVisibility(View.GONE);
        }

        if (webview!=null){
            webview.setVisibility(View.GONE);
        }
        if (videoLayout!=null){

            //2、通过Resources获取
            DisplayMetrics dm = getResources().getDisplayMetrics();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dm.widthPixels,dm.heightPixels);
            videoLayout.setLayoutParams(params);
        }


        if (newsLayout!=null){
            newsLayout.setVisibility(View.GONE);
        }

    }

    private void outFull(){
        if (webTitle!=null){
            webTitle.setVisibility(View.VISIBLE);
        }

        if (others!=null){
            others.setVisibility(View.VISIBLE);
        }

        if (webview!=null){
            webview.setVisibility(View.VISIBLE);
        }

        if (videoLayout!=null){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,CommUtils.dip2px(getContext(),140));
            videoLayout.setLayoutParams(params);
        }

        if (newsLayout!=null){
            newsLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (video!=null){
            video.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (video!=null){
            video.resume();
        }
    }

    @Override
    public void backClick() {

    }

    @Override
    public void shareClick() {
        initShare();

    }

    @Override
    public void tousheClick() {

    }

    @Override
    public void intoFullScreen() {
        intoFull();
    }

    @Override
    public void intoNormalScreen() {
        outFull();

    }

    @Override
    public void playNext() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void seekStop() {

    }

    @Override
    public void startPlay() {

    }

    private class MyWebViewDownLoadListener implements DownloadListener {

        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {//下载
           /* Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);*/
        }

    }

    final class MyWebViewClient extends WebViewClient {


        public boolean shouldOverrideUrlLoading(final WebView view, String url) {
            //view.loadUrl(url);
            return false;
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        public void onPageFinished(WebView view, String url) {
            imgReset();
            //这个是一定要加上那个的,配合scrollView和WebView的height=wrap_content属性使用
            int w = webview.getContentWidth();
            int h = webview.getContentHeight();
            //重新测量
            LogUtils.e("WeView", "w : " + w);
            LogUtils.e("WeView", "h : " + h);
            webview.measure(w, h);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);


        }


    }

    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String ti) {
            // TODO Auto-generated method stub
            super.onReceivedTitle(view, ti);
            if (title != null) {
                title.setText(ti);
            }
            if (isUrl) {
                History history = new History();
                history.setUrl(url);
                history.setTitle(ti);
                Date date = new Date();
                history.setTime(date.getTime());
                HistoryManager.getInstance().addHistory(history);
            }

        }

        @Override
        public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota, long estimatedSize, long totalUsedQuota, WebStorage.QuotaUpdater quotaUpdater) {
            quotaUpdater.updateQuota(estimatedSize * 2);
        }

        // Android > 4.1.1 调用这个方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        }

        @Override
        public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {

        }

    }
}
