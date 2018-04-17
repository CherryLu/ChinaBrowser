
package com.chinabrowser.ui;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

/**
 * 字体设置
 */
public class WheelTextView extends TextView {
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
        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        float rawSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size,
                r.getDisplayMetrics());
        if (rawSize != getPaint().getTextSize()) {
            getPaint().setTextSize(rawSize);
            if (getLayout() != null) {
                invalidate();
            }
        }
    }

    public static int dipToPx(Context context, int dipValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, context.getResources().getDisplayMetrics());
    }

}
