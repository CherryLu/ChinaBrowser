package com.chinabrowser.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinabrowser.R;


public class RedPacketCustomDialog extends Dialog implements View.OnClickListener {
	int which;
	public RedPacketCustomDialog(Context context,int which) {
		super(context, R.style._dialog_bg);
		init(which);
	}

	@Override
	public void onClick(View v) {

	}
	ImageView imag;
	TextView txt;
	protected void init(int which) {
		setContentView(R.layout.redpacket_dialog);
		imag = (ImageView) findViewById(R.id.pic);
		txt = (TextView) findViewById(R.id.txt);
		setCanceledOnTouchOutside(true);
		setCancelable(true);
		switch (which){
			case 0:
				imag.setImageResource(R.mipmap.success_coll);
				txt.setText(getContext().getText(R.string.collection_success));
				break;
			case 1:
				imag.setImageResource(R.mipmap.cancle_coll);
				txt.setText(getContext().getText(R.string.collection_cancel));
				break;
			case 2:
				imag.setImageResource(R.mipmap.success_coll);
				txt.setText(getContext().getText(R.string.setting_clear_success));
				break;
			case 3:
				break;
		}
	}


	public void showIt(){
		if (isShowing()){
			return;
		}
		show();


	}



}
