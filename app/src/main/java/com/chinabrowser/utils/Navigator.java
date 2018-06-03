package com.chinabrowser.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.chinabrowser.R;
import com.chinabrowser.activity.AboutActivity;
import com.chinabrowser.activity.CollectionActivity;
import com.chinabrowser.activity.ForgetPswActivity;
import com.chinabrowser.activity.GuideActivity;
import com.chinabrowser.activity.HistoryActivity;
import com.chinabrowser.activity.LoginActivity;
import com.chinabrowser.activity.MainActivity;
import com.chinabrowser.activity.RecommandActivity;
import com.chinabrowser.activity.RegisterActivity;
import com.chinabrowser.activity.SearchActivity;
import com.chinabrowser.activity.SearchAttentiveActivity;
import com.chinabrowser.activity.SelectActivity;
import com.chinabrowser.activity.SettingActivity;
import com.chinabrowser.activity.TranslateActivity;
import com.chinabrowser.activity.WebDetailActivity;


/**
 * 页面跳转工具类
 * Created by Administrator on 2018/3/26.
 */

public class Navigator {
    /**
     * 结束
     * @param activity
     */
    public static void finishActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            activity.overridePendingTransition(R.anim.no_anim, R.anim.push_right_out);
        }
    }

    /**
     * 跳转主页面
     * @param context
     */
    public static void startMainActivity(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转主页面
     * @param context
     */
    public static void startMainActivity(Context context,int which,String id){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("ISURL",which);
        intent.putExtra("URL",id);
        context.startActivity(intent);
    }
    /**
     * 跳转登录
     * @param context
     */
    public static void startLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startSelectionActivity(Context context){
        Intent intent = new Intent(context, SelectActivity.class);
        context.startActivity(intent);
    }


    /**
     * 跳转注册页面
     * @param context
     */
    public static void startRegisterActivity(Context context){
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }
    /**
     * 跳转设置页面
     * @param context
     */
    public static void startSettingActivity(Context context){
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转关于页面
     * @param context
     */
    public static void startAboutActivity(Context context){
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转收藏页面
     * @param context
     */
    public static void startCollectionActicity(Context context){
        Intent intent = new Intent(context, CollectionActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转历史记录页面
     * @param context
     */
    public static void startHistoryActivityActicity(Context context){
        Intent intent = new Intent(context, HistoryActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转搜索页面
     * @param context
     */
    public static void startSearchActicity(Context context){
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转翻译
     * @param context
     */
    public static void startTranslateActivity(Context context,int position){
        Intent intent = new Intent(context, TranslateActivity.class);
        TranslateUtil util = new TranslateUtil(context);
        intent.putExtra("pcid",util.getTranslateSorts().get(position).getCid());
        intent.putExtra("pname",util.getTranslateSorts().get(position).getName());
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    /**
     * 跳转选择引擎页面
     */
    public static void startSetSearchAttentiveActivity(Activity activity){
        Intent intent = new Intent(activity, SearchAttentiveActivity.class);
        activity.startActivityForResult(intent,0);
    }

    /**
     * 跳转详情页
     * @param context
     * @param url
     * @param id
     */
    public static void startWebDetailActivity(Context context,String url,String id){
        Intent intent = new Intent(context, WebDetailActivity.class);
        intent.putExtra("ID",id);
        intent.putExtra("URL",url);
        context.startActivity(intent);

    }

    /**
     * 跳转推荐页
     * @param context
     */
    public static void startRecommandActivity(Context context){
        Intent intent = new Intent(context, RecommandActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转推荐页
     * @param context
     */
    public static void startRecommandActivity(Fragment context){
        Intent intent = new Intent(context.getContext(), RecommandActivity.class);
        context.startActivityForResult(intent,0);
    }

    public static void startFindPSW(Context context){
        Intent intent = new Intent(context, ForgetPswActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转引导页面
     * @param context
     */
    public static void startGuide(Context context){
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }



}
