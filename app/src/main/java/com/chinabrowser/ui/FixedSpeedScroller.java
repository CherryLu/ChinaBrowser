package com.chinabrowser.ui;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class FixedSpeedScroller extends Scroller {
	private int mDuration = 800;

	public FixedSpeedScroller(Context paramContext) {
		super(paramContext);
	}

	public FixedSpeedScroller(Context paramContext,
                              Interpolator paramInterpolator) {
		super(paramContext, paramInterpolator);
	}

	public int getmDuration() {
		return this.mDuration;
	}

	public void setmDuration(int paramInt) {
		this.mDuration = paramInt;
	}

	public void startScroll(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4) {
		super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4,
				this.mDuration);
	}

	public void startScroll(int paramInt1, int paramInt2, int paramInt3,
			int paramInt4, int paramInt5) {
		super.startScroll(paramInt1, paramInt2, paramInt3, paramInt4,
				this.mDuration);
	}
}
