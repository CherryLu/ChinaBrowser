package com.chinabrowser.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.chinabrowser.R;
import com.chinabrowser.activity.AboutActivity;
import com.chinabrowser.activity.CollectionActivity;
import com.chinabrowser.activity.HistoryActivity;
import com.chinabrowser.activity.LoginActivity;
import com.chinabrowser.activity.MainActivity;
import com.chinabrowser.activity.RegisterActivity;
import com.chinabrowser.activity.SearchActivity;
import com.chinabrowser.activity.SearchAttentiveActivity;
import com.chinabrowser.activity.SettingActivity;
import com.chinabrowser.fragment.TranslateActivity;


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
     * 跳转登录
     * @param context
     */
    public static void startLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
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
     * 跳转选择殷勤页面
     */
    public static void startSetSearchAttentiveActivity(Activity activity){
        Intent intent = new Intent(activity, SearchAttentiveActivity.class);
        activity.startActivityForResult(intent,0);
    }



}
