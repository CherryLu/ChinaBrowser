/****************************
 文件名:CustomViewpager.java
 创建时间:2013-3-4 上午下午01:19:48
 所在包:cindy.example.slidingmenu
 作者:苑贺杰
 说明:ListView header 里用到，为了解决ViewPager潜逃ViewPager时，里面ViewPager不能滑动的问题
 ****************************/

package com.chinabrowser.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.chinabrowser.utils.CommUtils;


public class CustomViewpager extends ViewPager {

	// 消息定义，定期切换图片
	private static final int MSG_WHAT = 1;
	// 切换图片时间间隔，毫秒
	private static final int ScrollTime = 5000;
	private int mIsDown = 1;
	private float mLastMotionX;
	private float mLastMotionY;

	private Context context;
	private boolean mIsAutoSlide = false;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_WHAT)
				changeToNext();
			super.handleMessage(msg);
		}

	};

	public CustomViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CustomViewpager(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		final float x = ev.getX();
		final float y = ev.getY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			mIsDown = 1;
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mIsDown == 1) {
				float offX = Math.abs(x - mLastMotionX);
				float offY = Math.abs(y - mLastMotionY);

				if (offX < offY-5) {
					// 如果是上下滑动，事件处理给父类
					mIsDown = 0;
					if (isDisTouch) {
						getParent().requestDisallowInterceptTouchEvent(false);
					} else {
					}
				} else if (offX > 5) {
					mIsDown = 0;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	private boolean isDisTouch = true;

	public void setDisTouchEnable(boolean enable) {
		isDisTouch = enable;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);

	}

	@Override
	public void setAdapter(PagerAdapter arg0) {
		if (arg0.getCount() > 1) {
			onResume();
		}
		super.setAdapter(arg0);
	}

	private void changeToNext() {
		PagerAdapter pd = getAdapter();
		int c = pd.getCount();

		if (c <= 1)
			return;

		int cur = getCurrentItem();

		cur++;
		if (cur >= c) {
			cur = 0;
		}
		if (c > 1) {
			setCurrentItem(cur, true);

			if (mIsAutoSlide)
				mHandler.sendEmptyMessageDelayed(MSG_WHAT, ScrollTime);
		}
	}

	public void onPause() {
		mHandler.removeCallbacksAndMessages(null);
	}

	public void onResume() {
		if (!mIsAutoSlide)
			return;
		if (!CommUtils.isAccessibilityEnabled(context)) {
			mHandler.removeCallbacksAndMessages(null);
			mHandler.sendEmptyMessageDelayed(MSG_WHAT, ScrollTime);
		}
	}

	public boolean getAutoSlide() {
		return mIsAutoSlide;
	}

	public void setAutoSlide(boolean mIsAutoSlide) {
		this.mIsAutoSlide = mIsAutoSlide;
	}

}
