package cn.anyradio.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import cn.anyradio.utils.LogUtils;

public class AnyRadio_BroadcastReceiver extends BroadcastReceiver {
	private long lastCallTime = 0;
	private Context mContext;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		TelephonyManager telephony = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		CustomPhoneStateListener customPhoneListener = new CustomPhoneStateListener();
		telephony.listen(customPhoneListener,
				PhoneStateListener.LISTEN_CALL_STATE);
	}

	public class CustomPhoneStateListener extends PhoneStateListener {


		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			LogUtils.DebugLog("onCallStateChanged state " + state + " " + incomingNumber);
			
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: {
				lastCallTime = System.currentTimeMillis();
				PlayServer.autoPause(mContext);
				break;
			}
			case TelephonyManager.CALL_STATE_IDLE: {
				long offTime = System.currentTimeMillis()-lastCallTime;
				LogUtils.DebugLog("onCallStateChanged offTime " + offTime);
				// 双卡双待手机这个消息来得比较乱，在收到呼入/呼出电话后，立即就收到此消息，时间间隔小于0.3秒忽略
				if (offTime < 300)
					return;
				PlayServer.autoResume(mContext);
				break;
			}
			case TelephonyManager.CALL_STATE_OFFHOOK: {
				lastCallTime = System.currentTimeMillis();
				PlayServer.autoPause(mContext);
				break;
			}
			}
		}
	}
}
