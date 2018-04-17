package com.chinabrowser.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chinabrowser.APP;
import com.chinabrowser.R;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebStorage;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.lang.reflect.Method;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/1.
 */

public class WebViewFragment extends BaseFragment {
    @Bind(R.id.webview)
    WebView webview;

    /**
     * 用户代理 User agents.
     */
    public static String UA_DEFAULT = "";
    public static String UA_DESKTOP = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.7 (KHTML, like Gecko) Chrome/7.0.517.44 Safari/534.7";
    public static String UA_IPHONE = "Mozilla/5.0 (iPhone Simulator; U; CPU iPhone OS 4_1 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Mobile/8B117";
    public static final String[] USER_AGENTS = new String[]{UA_DEFAULT, UA_DESKTOP, UA_IPHONE};

    private Method mMethodFinding = null;// 开始查找
    private Method mMethodFinded  = null;// 查找结束

    private int mLoadPregress = 100;     // 载入进度
    private int position = 0;            // 序号


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_webview, null);
        ButterKnife.bind(this, rootView);
        initWebView(webview);
        String data = "网易科技讯 1月29日消息，乐视网今日公布了2015年度业绩预告，预告显示公司以股东的净利约为5.64亿元至6.37亿元，同比增长55%到75%。以下为预告全文乐视网信息技术（北京）股份有限公司2015 年度业绩预告本公司及其董事会全体成员保证公告内容真实、准确和完整，没有虚假记载、误导性陈述或重大遗漏。一、本期业绩预计情况1．业绩预告期间：2015年1月1日至2015年12月31日2.业绩预告类\n" +
                "\n" +
                "\n" +
                "\n" +
                "<p>\n" +
                "\t<a href=\"http://tech.163.com/company/netease/\" target=\"_blank\">网易</a>科技讯 1月29日消息，<a href=\"http://tech.163.com/company/letv/\">乐视网</a>今日公布了2015年度业绩预告，预告显示公司以股东的净利约为5.64亿元至6.37亿元，同比增长55%到75%。\n" +
                "</p>\n" +
                "<p>\n" +
                "\t<b>以下为预告全文</b>\n" +
                "</p>\n" +
                "<p>\n" +
                "\t乐视网信息技术（北京）股份有限公司\n" +
                "</p>\n" +
                "<p>\n" +
                "\t2015 年度业绩预告\n" +
                "</p>\n" +
                "<p>\n" +
                "\t本公司及其董事会全体成员保证公告内容真实、准确和完整，没有虚假记载、误导性陈述或重大遗漏。\n" +
                "</p>\n" +
                "<p>\n" +
                "\t一、本期业绩预计情况\n" +
                "</p>\n" +
                "<p>\n" +
                "\t1．业绩预告期间：2015年1月1日至2015年12月31日\n" +
                "</p>\n" +
                "<p>\n" +
                "\t2.业绩预告类型： □亏损 □扭亏为盈√同向上升 □同向下降\n" +
                "</p>\n" +
                "<p>\n" +
                "\t项目 本报告期 上年同期\n" +
                "</p>\n" +
                "<p>\n" +
                "\t比上年同期增长 55%—75%\n" +
                "</p>\n" +
                "<p>\n" +
                "\t归属于上市公司 净利润：\n" +
                "</p>\n" +
                "<p>\n" +
                "\t股东的净利润 36,402.95万元\n" +
                "</p>\n" +
                "<p>\n" +
                "\t约为：56,424.57 万元—63,705.16 万元\n" +
                "</p>\n" +
                "<p>\n" +
                "\t二、业绩预告预审计情况\n" +
                "</p>\n" +
                "<p>\n" +
                "\t本次业绩预告未经注册会计师审计。\n" +
                "</p>\n" +
                "<p>\n" +
                "\t<br />\n" +
                "</p>\n" +
                "<div class=\"gg200x300\">\n" +
                "</div>\n" +
                "<p>\n" +
                "\t三、业绩变动原因说明\n" +
                "</p>\n" +
                "<p>\n" +
                "\t报告期内，公司各项主营业务稳定，发展势头良好，尤其是公司全资子公司东阳市乐视花儿影视文化有限公司业绩突出，对公司的净利润增长产生积极推动作用。\n" +
                "</p>\n" +
                "<p>\n" +
                "\t报告期内，非经常性损益对公司净利润的贡献金额预计约为11,558万元。\n" +
                "</p>\n" +
                "<p>\n" +
                "\t四、其他相关说明\n" +
                "</p>\n" +
                "<p>\n" +
                "\t以上数据经公司财务部门初步测算，具体数据将在2015年度报告中详细披露。敬请广大投资者注意投资风险。\n" +
                "</p>\n" +
                "<p>\n" +
                "\t特此公告。\n" +
                "</p>\n" +
                "<p>\n" +
                "\t乐视网信息技术（北京）股份有限公司\n" +
                "</p>\n" +
                "<p>\n" +
                "\t董事会\n" +
                "</p>\n" +
                "\n" +
                "\n" +
                "\n" +
                "upload/video/201803/1080P.mp4";
        webview.loadData(data,"text/html",null);
        return rootView;
    }

    /**
     * 初始化方法: 暂停|继续|查找|查找结束
     */
    private void initMethod() {
        try {
            mMethodFinding = this.getClass().getMethod("setFindIsUp", Boolean.TYPE);
            mMethodFinded  = this.getClass().getMethod("notifyFindDialogDismissed");
        } catch (SecurityException e) {
            e.printStackTrace();
            mMethodFinding = null;
            mMethodFinded  = null;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            mMethodFinding = null;
            mMethodFinded  = null;
        }
    }

    private void initWebView(WebView ActionwebView) {
        ActionwebView.getSettings().setTextSize(WebSettings.TextSize.LARGEST);
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
        ActionwebView.getSettings().setDefaultTextEncodingName("utf-8") ;

        String defaultUa = ActionwebView.getSettings().getUserAgentString()+"/GgBroswer";
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
            view.loadUrl(url);
            return true;
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        }

        public void onPageStarted(WebView view, String url, Bitmap favicon) {

        }

        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);

        }

    }
    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // TODO Auto-generated method stub
            super.onReceivedTitle(view, title);

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
