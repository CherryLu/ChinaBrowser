package com.chinabrowser.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;

import com.chinabrowser.R;

/**
 * custom edittext: show clear button  
 * @author blackieqq
 * @since 2014-10-31
 */
@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText implements OnFocusChangeListener, TextWatcher {
    private Drawable mClearDrawable; 
    private boolean hasFoucs;
 
    public CustomEditText(Context context) { 
    	this(context, null); 
    } 
 
    public CustomEditText(Context context, AttributeSet attrs) { 
    	this(context, attrs, android.R.attr.editTextStyle); 
    } 
    
    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    
    
    private void init() { 
    	mClearDrawable = getCompoundDrawables()[2]; 
        if (mClearDrawable == null) { 
//        	throw new NullPointerException("You can add drawableRight attribute in XML");
        	mClearDrawable = getResources().getDrawable(R.mipmap.clearbtn_edt);
        } 
        
        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight()); 
        setClearIconVisible(false); 
        setOnFocusChangeListener(this); 
        addTextChangedListener(this); 
    } 
 
    @Override 
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (getCompoundDrawables()[2] != null) {
				boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight()) && (event.getX() < ((getWidth() - getPaddingRight())));
				if (touchable) {
					this.setText("");
				}
			}
		}
		return super.onTouchEvent(event);
	}
 
    @Override 
    public void onFocusChange(View v, boolean hasFocus) { 
    	this.hasFoucs = hasFocus;
        if (hasFocus) { 
            setClearIconVisible(getText().length() > 0); 
        } else { 
            setClearIconVisible(false); 
        } 
    } 

    /**
     * 
     * @param visible
     */
    protected void setClearIconVisible(boolean visible) { 
        Drawable right = visible ? mClearDrawable : null; 
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]); 
    } 
     
    @Override 
    public void onTextChanged(CharSequence s, int start, int count, int after) { 
        if(hasFoucs){
            setClearIconVisible(s.length() > 0);
        }
    } 
 
    @Override 
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { 
    } 
 
    @Override 
    public void afterTextChanged(Editable s) { 
    } 
   
    /**
     * setShakeAnimation
     */
    public void setShakeAnimation(){
    	this.setAnimation(shakeAnimation(5));
    }

    /**
     * shakeAnimation
     * @param counts
     * @return
     */
    public static Animation shakeAnimation(int counts){
    	Animation translateAnimation = new TranslateAnimation(0, 10, 0, 0);
    	translateAnimation.setInterpolator(new CycleInterpolator(counts));
    	translateAnimation.setDuration(1000);
    	return translateAnimation;
    }
}
