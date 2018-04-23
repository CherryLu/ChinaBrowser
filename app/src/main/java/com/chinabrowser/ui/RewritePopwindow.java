package com.chinabrowser.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chinabrowser.R;

/**
 * Created by 95470 on 2018/4/23.
 */

public class RewritePopwindow extends PopupWindow {

    private View mView;

    public RewritePopwindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        initView(context, itemsOnClick);
    }



    private void initView(final Activity context, View.OnClickListener itemsOnClick) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.pop_share, null);
        LinearLayout weiXFriend = (LinearLayout) mView.findViewById(R.id.share1);
        LinearLayout friendster = (LinearLayout) mView.findViewById(R.id.share2);
        LinearLayout QQFriend = (LinearLayout) mView.findViewById(R.id.share3);
        LinearLayout QQZone = (LinearLayout) mView.findViewById(R.id.share4);
        TextView canaleTv = (TextView) mView.findViewById(R.id.cancel_button);
        canaleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置按钮监听
        weiXFriend.setOnClickListener(itemsOnClick);
        friendster.setOnClickListener(itemsOnClick);
        QQFriend.setOnClickListener(itemsOnClick);
        QQZone.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(WindowManager.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置PopupWindow可触摸
        this.setTouchable(true);
        this.setBackgroundDrawable(new BitmapDrawable());

    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
     /*   WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);*/
    }
}
