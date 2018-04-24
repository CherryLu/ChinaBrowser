package com.chinabrowser.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.chinabrowser.R;


public class ExitDialog extends Dialog implements View.OnClickListener {

	public ExitDialog(Context context, String title, String leftxt, String rightxt, DialogClick dialogClick) {
		super(context, R.style._dialog_bg);
		init(title,leftxt,rightxt,dialogClick);
	}

	@Override
	public void onClick(View v) {

	}
	TextView maintitle,left,right;
	protected void init(String title, String leftxt, String rightxt, final DialogClick dialogClick) {
		setContentView(R.layout.exit);
		maintitle = (TextView) findViewById(R.id.maintitle);
		left = (TextView) findViewById(R.id.yes);
		right = (TextView) findViewById(R.id.no);
		maintitle.setText(title);
		left.setText(leftxt);
		right.setText(rightxt);
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialogClick!=null){
					dialogClick.dialogClick(1);
					dismiss();
				}
			}
		});
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dialogClick!=null){
					dialogClick.dialogClick(0);
					dismiss();
				}
			}
		});

		setCanceledOnTouchOutside(false);
		setCancelable(false);

	}


	public void showIt(){
		if (isShowing()){
			return;
		}
		show();


	}


public 	interface DialogClick{
		void dialogClick(int which);
	}



}
