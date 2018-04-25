package cn.anyradio.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Date;

import cn.anyradio.bean.HeartParam;
import cn.anyradio.lib.AnyRadioApplication;
import cn.anyradio.protocol.AodData;
import cn.anyradio.protocol.AodListData;
import cn.anyradio.protocol.BaseListData;
import cn.anyradio.protocol.GeneralBaseData;
import cn.anyradio.protocol.RadioData;
import cn.anyradio.protocol.RadioListData;
import cn.anyradio.utils.CommUtils;
import cn.anyradio.utils.IRemotePlayService;
import cn.anyradio.utils.IRemotePlayServiceCallback;
import cn.anyradio.utils.LogUtils;
import cn.anyradio.utils.PlayEngineData;
import cn.anyradio.utils.PlaybackEngine;


@SuppressLint({ "HandlerLeak", "HandlerLeak" })
public class PlayServer extends Service {
	public static final String ACTION_SEEK = "seek";
	public static final String ACTION_SETCHANNEL = "setchannel";
	public static final String ACTION_PLAY = "play";
	public static final String ACTION_PAUSE = "pause";
	public static final String ACTION_LOG = "log";
	public static final String ACTION_STOP = "stop";
	public static final String ACTION_STARTTORECORD = "StartToRecord";
	public static final String ACTION_STOPTORECORD = "StopToRecord";
	public static final String ACTION_SET_TIMER_STOP = "SetTimerStop";
	public static final String ACTION_SET_TIMER_STOP_SECONDS = "SetTimerStop_Seconds";
	public static final String ACTION_REMOVE_CHECKTIMER_STOPMESSAGE = "removeCheckTimerStopMessage";
	public static final String ACTION_CHANGE_PLAY_MODE = "changePlayMode";
	public static final String ACTION_UPDATE_STATE = "updateState";

	public static final String ACTION_REFRESH_BASELISTDATA = "refreshBaseListData";

	public static final String ACTION_EMPTY_FLOW = "emptyFlow";
	public static final String EXTRA_PLAY_DATA = "playData";
	public static final String EXTRA_PAUSE = "pause";
	public static final String EXTRA_SEEK = "seek";
	public static final String EXTRA_HOUR = "hour";
	public static final String EXTRA_SECONDS = "seconds";
	public static final String EXTRA_HAS_SHOWN_WARNNING = "HasShownWarnning";
	public static final String EXTRA_MINUTE = "minute";
	public static final String EXTRA_PLAY_MODE = "playMode";
	public static final String EXTRA_SETBUFFERTIME = "SetBufferTime";
	public static final String ACTION_AUTOTEST = "autotest";
	public static final String ZGB_PLAY_TYPE = "zgb_play_type";
	private NotificationManager mNotificationManager;
	public static boolean mIsAutoStop = false;
	private HomeReceiver mHomeReceiver;

	private AudioManager mAudioManager;
	private OnAudioFocusChangeListener mOnAudioFocusChangeListener;
	final RemoteCallbackList<IRemotePlayServiceCallback> mCallbacks = new RemoteCallbackList<IRemotePlayServiceCallback>();

	private AnyRadio_BroadcastReceiver mAnyRadio_BroadcastReceiver;
	private Location location;

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */

	@Override
	public void onCreate() {

		LogUtils.DebugLog("PlayServer onCreate");

		AnyRadioApplication.mContext = this;
		// 初始化sd卡
		CommUtils.InitSD();
		AnyRadioApplication.setIntChannelIDCode(this);

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mIsAutoStop = false;
		// InitWifiWakeLock();
	}

	@SuppressLint("NewApi")
	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		LogUtils.DebugLog("PlayServer onDestroy");
		stopForeground(true);

		unRregisterReceiver();

		// Unregister all callbacks.
		mCallbacks.kill();

		if (mPlayEngine != null) {
			mPlayEngine.Stop();
		}
		ReleaseWifiWakeLock();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		LogUtils.DebugLog("PlayServer.onStart startId: " + startId);
		super.onStart(intent, startId);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		LogUtils.DebugLog("PlayServer.onStartCommand flags: " + flags
				+ " startId: " + startId);
		handleStartIntent(intent);
		return super.onStartCommand(intent, flags, startId);
	}

	private void handleStartIntent(Intent intent) {

		if (intent == null) {
			return;
		}

		String action = intent.getAction();
		LogUtils.DebugLog("PlayServer.handleStartIntent action: " + action
				+ " mPlayEngine: " + mPlayEngine);

		if (TextUtils.isEmpty(action))
			return;

		if (action.equals(ACTION_SEEK)) {
			mIsAutoStop = false;
			InitWifiWakeLock();
			if (mPlayEngine != null) {
				double v = intent.getDoubleExtra(EXTRA_SEEK, 0);
				mPlayEngine.seek(v);
			}
		} else if (action.equals(ACTION_SETCHANNEL)) {
			String textString = intent.getStringExtra(ACTION_SETCHANNEL);
			AnyRadioApplication.gIntSysID = CommUtils.convert2int(textString
					.substring(0, 4));
			AnyRadioApplication.gIntVersionID = CommUtils
					.convert2int(textString.substring(4, 8));
			AnyRadioApplication.gIntChannelID = CommUtils
					.convert2int(textString.substring(8, 12));
			AnyRadioApplication.gSubIntVersionID = CommUtils
					.convert2int(textString.substring(12, 16));

			CommUtils.showToast(PlayServer.this, "设置成功，新的设置为：" + textString);

		} else if (action.equals(ACTION_PLAY)) {
			InitWifiWakeLock();
			mIsAutoStop = false;
			RregisterReceiver();

			Bundle b = intent.getExtras();
			if (b != null) {
				mPlayMode = b.getInt(EXTRA_PLAY_MODE);
				mPlayListData = (BaseListData) b
						.getSerializable(EXTRA_PLAY_DATA);
				PlayEngineData playEngineData = getPlayEngineData(mPlayListData);
				int setBufferTime = b.getInt(EXTRA_SETBUFFERTIME, 0);
				double pos = b.getDouble(EXTRA_SEEK, 0);

				// 检查和当前播放的是否是同一个
				// 如果是同一个，如果正在播放，忽略，如果暂停，继续播放，如果停止，开始播放
				// 如果不是同一个，先停止当前，在new一个新对象播放

				boolean sameData = checkSameData(playEngineData, setBufferTime);

				if (mPlayEngine == null)
					sameData = false;
				LogUtils.DebugLog("PlayServer sameData: " + sameData);

				LogUtils.DebugLog("PlayServer mPos: " + mPos + " pos: " + pos);

				mPlayEngineData = playEngineData;
                mPlayEngineData.playType_t = b.getInt(ZGB_PLAY_TYPE);
				mSetBufferTime = setBufferTime;
				mPos = pos;

				if (sameData) {
					if (PlayManager.isPause(mLastPlayState)) {
						mPlayEngine.Pause(false);
					} else if (PlayManager.isStop(mLastPlayState)) {
						if (mPlayEngineData.playType_t == PlayEngineData.LIVE_TYPE) {
							// 直播类型
							LogUtils.DebugLog("PlayServer live play");
							mPlayEngine.Play(0);
						} else {
							// 点播或录音
							LogUtils.DebugLog("PlayServer no live seek");
							if (pos > 0) {
								Toast.makeText(PlayServer.this, "正在定位到上次播放位置。",
										Toast.LENGTH_SHORT).show();
							}
							mPlayEngine.Play(mPos);
						}
					} else {
						// 当前已经在播放，忽略
						LogUtils.DebugLog("PlayServer playing ignore");
						// playStateChanged(mLastPlayState);
					}
				} else {
					LogUtils.DebugLog("PlayServer new PlayEngine "
							+ playEngineData);

					if (pos > 0) {
						Toast.makeText(PlayServer.this, "正在定位到上次播放位置。",
								Toast.LENGTH_SHORT).show();
					}

					if (mPlayEngine != null) {
						if (PlayManager.isPause(mLastPlayState)) {
							LogUtils.DebugLog("PlayServer new PlayEngine and old PlayEnging isPause");
							mPlayEngine.Pause(false);
						}
						mPlayEngine.Stop();
					}
					startPlaybackEngine(true);
					initHeart();
					// // 心跳上传
					// mStartTime = System.currentTimeMillis();
					// mLastSyncTime = mStartTime;
					// uploadPlayHeartbeat(UploadPlayHeartbeatData.STG_Start);
				}
			}
		} else if (action.equals(ACTION_PAUSE)) {
			boolean v = intent.getBooleanExtra(EXTRA_PAUSE, false);

			if (mPlayEngine != null) {
				mPlayEngine.Pause(v);

				if (v) {
					ReleaseWifiWakeLock();
				} else {
					InitWifiWakeLock();
				}
			}
			LogUtils.DebugLog("PlayEngineManager playServer 暂停播放 v" + "v");
			LogUtils.DebugLog("PlayServer pause " + v);

			// if (!v) {
			// mStartTime = System.currentTimeMillis();
			// mLastSyncTime = mStartTime;
			// }

			// 自动暂停中时，如果用户暂停，恢复标志位，收到AudioFocus消息不继续播放
			if (mIsAutoStop) {
				// 如果是自动暂停，手动继续播放，需要重新注册AudioFocus
				unRregisterReceiver();
				mIsAutoStop = false;
			}

			if (v) {
				// 暂停，取消注册AudioFocus通知
				unRregisterReceiver();
			} else {
				// 继续播放，重新注册AudioFocus，通知其他播放程序暂停
				RregisterReceiver();
			}
		} else if (action.equals(ACTION_LOG)) {
			LogUtils.LOG_ON = intent.getBooleanExtra(EXTRA_PAUSE, false);
		} else if (action.equals(ACTION_AUTOTEST)) {
			LogUtils.AUTOTEST = intent.getBooleanExtra(EXTRA_PAUSE, false);
		} else if (action.equals(ACTION_STOP)) {
			mIsAutoStop = false;
			if (mPlayEngine != null)
				mPlayEngine.Stop();
			unRregisterReceiver();
		} else if (action.equals(ACTION_STARTTORECORD)) {
			if (mPlayEngine != null)
				mPlayEngine.StartToRecord();
		} else if (action.equals(ACTION_STOPTORECORD)) {
			if (mPlayEngine != null)
				mPlayEngine.StopToRecord();
		} else if (action.equals(ACTION_SET_TIMER_STOP)) {
			Bundle b = intent.getExtras();
			if (b != null) {
				int hour = b.getInt(EXTRA_HOUR);
				int minute = b.getInt(EXTRA_MINUTE);
				setTimerStop(hour, minute);
			}
		} else if (action.equals(ACTION_SET_TIMER_STOP_SECONDS)) {
			Bundle b = intent.getExtras();
			if (b != null) {
				long seconds = b.getLong(EXTRA_SECONDS);
				setTimerStop(seconds);
			}
		} else if (action.equals(ACTION_REMOVE_CHECKTIMER_STOPMESSAGE)) {
			removeCheckTimerStopMessage();
		} else if (action.equals(ACTION_CHANGE_PLAY_MODE)) {
			Bundle b = intent.getExtras();
			if (b != null) {
				mPlayMode = b.getInt(EXTRA_PLAY_MODE);
			}
		} else if (action.equals(ACTION_UPDATE_STATE)) {
			// 更新播放状态
			if (!TextUtils.isEmpty(getTitle()))
				playInfoChanged(PlayManager.MSG_WHAT_TITLE_CHANGED, getTitle());
			if (!TextUtils.isEmpty(getPlayUrl()))
				playInfoChanged(PlayManager.MSG_WHAT_PLAYURL_CHANGED,
						getPlayUrl());
			if (mPlayListData != null) {
				playInfoChanged(PlayManager.MSG_WHAT_PLAY_INDEX,
						Integer.toString(mPlayListData.playIndex));
				Message msg = new Message();
				msg.what = PlaybackEngine.MSG_WHAT_PLAY_DEMAND;
				msg.arg1 = PlaybackEngine.MSG_ARG1_PLAY_STATE;
				msg.arg2 = mLastPlayState;
				playStateChanged(msg);
			}
		} else if (action.equals(ACTION_REFRESH_BASELISTDATA)) {
			Bundle b = intent.getExtras();
			mPlayListData = (BaseListData) b.getSerializable(EXTRA_PLAY_DATA);
		}
	}

	private void initHeart() {
		// mStartTime = 0;
		// mLastSyncTime = 0;
		// mBufferingTime = 0;
	}

	private String getTitle() {
		GeneralBaseData d = getCurPlayData();
		if (d != null)
			return d.name;
		return "";
	}

	// zgb add
	private String getContent() {
		GeneralBaseData d = getCurPlayData();
		if (d != null)
			return d.intro;
		return "";
	}

	private String mPlayUrl;

	private String getPlayUrl() {
		GeneralBaseData d = getCurPlayData();
		if (d != null)
			return d.url;
		return "";
	}

	private boolean checkSameData(PlayEngineData playEngineData,
			int setBufferTime) {
		if (mPlayEngineData == null) {
			LogUtils.DebugLog("PlayServer checkSameData diff mPlayEngineData: "
					+ mPlayEngineData);
			return false;
		}

		// if (mSetBufferTime != setBufferTime) {
		// LogUtils.DebugLog("PlayServer checkSameData diff mSetBufferTime: "
		// + mSetBufferTime);
		// return false;
		// }

		if (mPlayEngineData.playType_t != playEngineData.playType_t) {
			LogUtils.DebugLog("PlayServer checkSameData diff playEngineData.playType_t: "
					+ playEngineData.playType_t);
			return false;
		}

		if (!mPlayUrl.equals(playEngineData.url_t)) {
			LogUtils.DebugLog("PlayServer checkSameData diff mPlayEngineData.url_t: "
					+ playEngineData.url_t + " mPlayUrl：" + mPlayUrl);
			return false;
		}

		if (playEngineData.playType_t == PlayEngineData.RECORD_TYPE) {
			return mPlayEngineData.recordItemBean
					.equals(playEngineData.recordItemBean);
		}

		return true;
	}

	/**
	 * The IRemoteInterface is defined through IDL
	 */
	private final IRemotePlayService.Stub mBinder = new IRemotePlayService.Stub() {
		public void registerCallback(IRemotePlayServiceCallback cb) {
			if (cb != null) {
				mCallbacks.register(cb);
				mLastPlayState = 0;
			}
		}

		public void unregisterCallback(IRemotePlayServiceCallback cb) {
			if (cb != null) {
				mCallbacks.unregister(cb);
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@SuppressWarnings("deprecation")
	private void showNotification() {

//		if (mNotificationManager == null)
//			return;
//
//		String text = getString(R.string.playing);
//		text += " " + getTitle();
//
//		Notification notification = new Notification(R.drawable.ic_launcher,
//				text, // 这个地方的图标名称改动时候详细考虑，因为是横屏的打包的时候需要将他们都替换成ic_launcher_notification_hd所以更改必须慎重
//				System.currentTimeMillis());
//		notification.flags = notification.flags | Notification.FLAG_NO_CLEAR
//				| Notification.FLAG_ONGOING_EVENT;
//
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		PendingIntent contentIntent = PendingIntent.getActivity(this, 1,
//				intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//		// Set the info for the views that show in the notification panel.
//		notification.setLatestEventInfo(this,
//				getText(R.string.local_service_label), text, contentIntent);
//
//		// Send the notification.
//		// We use a layout id because it is a unique number. We use it later
//		// to
//		// cancel.
//
//		startForeground(R.string.local_service_label, notification);

	}

	public void updateNotification(boolean isPlay, String title, String content) {
		/*if (isPlay) {
			remoteViews.setImageViewResource(R.id.notification_play_or_pause,
					R.drawable.notification_pause);
		} else {
			remoteViews.setImageViewResource(R.id.notification_play_or_pause,
					R.drawable.notification_play);
		}
		remoteViews.setTextViewText(R.id.notification_title, title);
		remoteViews.setTextViewText(R.id.notification_content, content);

		mNotificationManager.notify(NotificationActions.notification_id,
				notification);*/

	}

	RemoteViews remoteViews;
	Notification notification;

	// zgb添加自定义通知栏
	@TargetApi(16)
	public void sendResidentNoticeType0(Context context, String title,
			String content) {
		/*if (mNotificationManager == null)
			return;

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context);
		builder.setOngoing(false);
		builder.setAutoCancel(true);

		remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.notification_layout);
		remoteViews.setImageViewResource(R.id.notification_icon,
				R.drawable.diskbg);
		remoteViews.setTextViewText(R.id.notification_title, title);
		remoteViews.setTextViewText(R.id.notification_content, content);

		// int requestCode1 = (int) SystemClock.uptimeMillis();
		int requestCode1 = 100001;
		Intent intent1 = new Intent(NotificationActions.notification_action);
		intent1.putExtra(NotificationActions.notification_intent_name,
				NotificationActions.notification_status_x);
		PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context,
				requestCode1, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notification_close,
				pendingIntent1);
		int requestCode2 = 100002;
		Intent intent2 = new Intent(NotificationActions.notification_action);
		intent2.putExtra(NotificationActions.notification_intent_name,
				NotificationActions.notification_status_left);
		PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context,
				requestCode2, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notification_left,
				pendingIntent2);

		int requestCode3 = 100003;
		Intent intent3 = new Intent(NotificationActions.notification_action);
		intent3.putExtra(NotificationActions.notification_intent_name,
				NotificationActions.notification_status_play);
		PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context,
				requestCode3, intent3, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notification_play_or_pause,
				pendingIntent3);

		int requestCode4 = 100004;
		Intent intent4 = new Intent(NotificationActions.notification_action);
		intent4.putExtra(NotificationActions.notification_intent_name,
				NotificationActions.notification_status_right);
		PendingIntent pendingIntent4 = PendingIntent.getBroadcast(context,
				requestCode4, intent4, PendingIntent.FLAG_UPDATE_CURRENT);
		remoteViews.setOnClickPendingIntent(R.id.notification_right,
				pendingIntent4);

		int requestCode5 = 100005;
		Intent intent5 = new Intent();
		intent5.setClass(this, MainActivity.class);
		intent5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		intent5.putExtra(NotificationActions.comeToNotification, true);
		PendingIntent intentContent5 = PendingIntent.getActivity(this,
				requestCode5, intent5, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setSmallIcon(R.drawable.diskbg);
		notification = builder.build();
		// if (android.os.Build.VERSION.SDK_INT >= 16) {
		// notification = builder.build();
		// notification.bigContentView = remoteViews;
		// }
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_NO_CLEAR;
		notification.contentView = remoteViews;
		notification.contentIntent = intentContent5;
		// NotificationManager manager = (NotificationManager) context
		// .getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NotificationActions.notification_id,
				notification);*/

	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("PLAYSTATECHANGE")) {

				if (PlayManager.getInstance(context).isPause()
						|| PlayManager.getInstance(context).isStop()) {
					PlayManager.getInstance(context).resume();
				} else {
					PlayManager.getInstance(context).pause();
				}
			}
		}
	};

	private void RregisterReceiver() {
		LogUtils.DebugLog("PlayServer RregisterReceiver");

		if (mHomeReceiver != null) {

		} else {
			mHomeReceiver = new HomeReceiver();
			IntentFilter filter = new IntentFilter(
					"lenovo.intent.action.TASK_REMOVED_FROM_RECENT");
			registerReceiver(mHomeReceiver, filter);
		}

		if (Build.VERSION.SDK_INT >= 8) {
			if (mAudioManager != null && mOnAudioFocusChangeListener != null) {
				mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);

				int ret = mAudioManager.requestAudioFocus(
						mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN);
			} else {
				mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
				mOnAudioFocusChangeListener = new OnAudioFocusChangeListener() {
					public void onAudioFocusChange(int focusChange) {
						LogUtils.DebugLog("PlayServer onAudioFocusChange "
								+ focusChange);
						Log.e("onAudioFocusChange",""+focusChange);
						if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT||
								focusChange == AudioManager.AUDIOFOCUS_LOSS) {
							autoPause(PlayServer.this);
						} else if (focusChange == AudioManager.AUDIOFOCUS_GAIN
								|| focusChange == AudioManager.AUDIOFOCUS_GAIN_TRANSIENT) {
							autoResume(PlayServer.this);
						}
//						else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
//							autoStop(PlayServer.this);
//						}
					}
				};

				int ret = mAudioManager.requestAudioFocus(
						mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
						AudioManager.AUDIOFOCUS_GAIN);

				LogUtils.DebugLog("PlayServer requestAudioFocus = " + ret);
			}
		}

		if (mAnyRadio_BroadcastReceiver != null) {

		} else {
			mAnyRadio_BroadcastReceiver = new AnyRadio_BroadcastReceiver();
			IntentFilter filter = new IntentFilter(
					"android.intent.action.PHONE_STATE");
			registerReceiver(mAnyRadio_BroadcastReceiver, filter);
		}
	}

	private void unRregisterReceiver() {
		LogUtils.DebugLog("PlayServer unRregisterReceiver");
		if (mHomeReceiver != null) {
			unregisterReceiver(mHomeReceiver);
			mHomeReceiver = null;
		}
		if (mAudioManager != null) {
			mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
			mAudioManager = null;
		}
		if (mAnyRadio_BroadcastReceiver != null) {
			unregisterReceiver(mAnyRadio_BroadcastReceiver);
			mAnyRadio_BroadcastReceiver = null;
		}

	}

	public static synchronized void autoPause(Context context) {
		LogUtils.DebugLog("MeidaPlay autoPause mIsAutoStop: " + mIsAutoStop);
		if (!mIsAutoStop) {
			if (PlayManager.isPlaying(mLastPlayState)) {
				// 如果正在播放，暂停
				mIsAutoStop = true;

				if (mPlayEngine != null) {
					mPlayEngine.Pause(true);
				}
			}
		} else {
			// 可能会多次调用，忽略
		}
	}

	public static synchronized void autoStop(Context context) {
		LogUtils.DebugLog("MeidaPlay autoPause mIsAutoStop: " + mIsAutoStop);
		Log.e("onAudioFocusChange",""+1);
		if (!mIsAutoStop) {
			if (PlayManager.isPlaying(mLastPlayState)) {
				// 如果正在播放，暂停
				Log.e("onAudioFocusChange",""+2);
				mIsAutoStop = true;
				if (mPlayEngine != null) {
					Log.e("onAudioFocusChange",""+3);
					mPlayEngine.Stop();
				}
			}
		} else {
			// 可能会多次调用，忽略
		}
	}

	public static synchronized void autoResume(Context context) {
		LogUtils.DebugLog("MeidaPlay autoResume mIsAutoStop: " + mIsAutoStop);
		if (mIsAutoStop) {
			mIsAutoStop = false;
			if (mPlayEngine != null) {
				mPlayEngine.Pause(false);
			}
		} else {
			// 可能会多次调用，忽略
		}
	}

	// 播放引擎
	private static PlaybackEngine mPlayEngine = null;
	private PlayEngineData mPlayEngineData;
	private int mSetBufferTime = 0;
	private double mPos = 0;

	// 创建2个Handler用来在切换播放时候，忽略上一个handler的播放引擎消息
	private int curHandler = 0;
	// 第一个Handler
	private Handler playHandler0 = new Handler() {
		public void handleMessage(Message msg) {
			handlePlaybackEngineMessage(msg, 0);
		}
	};

	// 第二个Handler
	private Handler playHandler1 = new Handler() {
		public void handleMessage(Message msg) {
			handlePlaybackEngineMessage(msg, 1);
		}
	};

	private BaseListData mPlayListData;
	private int mPlayMode = PlayManager.PLAY_MODE_NONE;
	private long mIntervalTime = 60; // 单位：秒
	private long mStartTime = 0;
	private long mLastSyncTime = 0;
	private long mBufferingTime = 0;// 缓冲时间
	private boolean isRunPlayHeartbeatPage;
	private static int mLastPlayState = 0;
	private long mBufferingStartTime = 0;
	public static final int MSG_WHAT_FLOW_CHANGE = 101;
	public static final String Action_Flow_Changed = "cn.anyradio.action.flow.changed";

	// 设置定时停止播放
	private final int MSG_WHAT_CHECK_TIMER_STOP = 1;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WHAT_CHECK_TIMER_STOP: {
				long delaySeconds = getOffSecond();
				String v = null;
				if (delaySeconds > 0) {
					if (delaySeconds > 60 * 60) {
						v = CommUtils.formatTime(delaySeconds / (60 * 60))
								+ ":"
								+ CommUtils
										.formatTime((delaySeconds / 60) % 60)
								+ ":" + CommUtils.formatTime(delaySeconds % 60);
					} else if (delaySeconds > 60) {
						v = CommUtils.formatTime(delaySeconds / 60) + ":"
								+ CommUtils.formatTime(delaySeconds % 60);
					} else {
						v = CommUtils.formatTime(delaySeconds);
					}
					sendCheckTimerStopMessage(1000);
				}
				playInfoChanged(PlayManager.MSG_WHAT_TIMER_STOP, v);

				break;
			}
			}
		}
	};
	private int mHour;
	private int mMinute;
	private long mSeconds = -1;

	// 从两个Handler中和上一个不一样的Handler
	private Handler getNewPlayHandler() {
		Handler playHandler;
		if (curHandler == 0) {
			curHandler = 1;
			playHandler = playHandler1;
		} else {
			curHandler = 0;
			playHandler = playHandler0;
		}
		return playHandler;
	}

	public GeneralBaseData getCurPlayData() {
		if (mPlayListData != null)
			return mPlayListData.getCurPlayData();
		return null;
	}

	private HeartParam heartParam = new HeartParam();

	// 播放引擎消息处理
	private void handlePlaybackEngineMessage(Message msg, int index) {
		if (curHandler != index) {
			// Handler和当前正在使用的Hanlder不一致，忽略
			LogUtils.DebugLog("PlayServer ignore playHandler curHandler: "
					+ curHandler + " index: " + index);
			return;
		}

		// 播放过程中的重要信息提醒
		if (msg.what == PlaybackEngine.MSG_WHAT_PLAY_INFO) {
			LogUtils.DebugLog("PlayServer PlayInfo " + msg.arg1 + " "
					+ msg.arg2);
			if (PlayManager.isPlaying(mLastPlayState)) {
				switch (msg.arg1) {
				case PlaybackEngine.MSG_ARG1_REPEAT_CONNECT:
					if (LogUtils.LOG_ON)
						CommUtils.showToast(PlayServer.this, "连接网络失败，将在 "
								+ msg.arg2 + " 秒后重试。");
					break;
				case PlaybackEngine.MSG_ARG1_BUFFER_TIME_CHANGE:
					if (LogUtils.LOG_ON)
						CommUtils.showToast(PlayServer.this, "缓冲时间智能调整为："
								+ msg.arg2 + " 秒");
					break;
				case PlaybackEngine.MSG_ARG1_BUFFER_SIZE_CHANGE:
					if (LogUtils.LOG_ON)
						CommUtils.showToast(PlayServer.this, "缓冲大小智能调整为："
								+ msg.arg2 / 1000 + " k");
					break;
				case PlaybackEngine.MSG_ARG1_BITPERSEC:
					if (LogUtils.LOG_ON)
						CommUtils.showToast(PlayServer.this, "每秒钟的PCM byte："
								+ msg.arg2);
					break;
				default:
					break;
				}
			}
			return;
		}

		if (msg.arg1 == PlaybackEngine.MSG_ARG1_PLAY_STATE
				&& msg.what == PlaybackEngine.MSG_WHAT_PLAY_DEMAND) {
			if (mLastPlayState != msg.arg2) {
				mLastPlayState = msg.arg2;
				if (mLastPlayState == PlaybackEngine.MSG_ARG2_PLAY_STATE_FIRSTPLAY) {
					heartParam = (HeartParam) msg.getData().getSerializable(
							"HeartPara");
				}

				// playStateChanged(mLastPlayState);
				// showNotification();
//				sendResidentNoticeType0(this, getTitle(), getContent());
			} else {
				if (msg.arg2 == PlaybackEngine.MSG_ARG2_PLAY_STATE_PLAYING) {
					// playStateChanged(msg.arg2);
				}
			}

			if (msg.arg2 == PlaybackEngine.MSG_ARG2_RECORD_FILE_FINISH) {
				BaseListData d = mPlayListData;
				if (mPlayMode == PlayManager.PLAY_MODE_ORDER) {
					// 如果是顺序播放模式，播放完成后，需要自动播放下一首
					if (d.playIndex < d.mList.size() - 1) {
						d.playIndex++;
						mPlayEngineData = getPlayEngineData(d);

						startPlaybackEngine(false);
					}
				} else if (mPlayMode == PlayManager.PLAY_MODE_CYCLE) {
					// 如果是循环播放模式，播放完成后，需要自动播放停止再播放

					startPlaybackEngine(false);
				} else if (mPlayMode == PlayManager.PLAY_MODE_RANDOM) {
					// 如果是随机播放模式，播放完成后，需要自动播放停止再播放
					if (d.mList.size() > 1) {
						d.playIndex = CommUtils.getRandom(d.mList.size(),
								d.playIndex);
						mPlayEngineData = getPlayEngineData(d);
					}

					startPlaybackEngine(false);

				} else if (mPlayMode == PlayManager.PLAY_MODE_LIST_CYCLE) {
					// 列表循环，播放到最后一首，从头开始继续播放
					if (d.playIndex < d.mList.size() - 1) {
						d.playIndex++;
					} else {
						d.playIndex = 0;
					}
					mPlayEngineData = getPlayEngineData(d);

					startPlaybackEngine(false);
				} else {
					// 如果无播放模式，不处理
				}
				playInfoChanged(PlayManager.MSG_WHAT_PLAY_INDEX,
						Integer.toString(d.playIndex));
			}
		}
		playStateChanged(msg);
	}

	private void playStateChanged(Message msg) {
		final int N = mCallbacks.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				mCallbacks.getBroadcastItem(i).playStateChanged(msg.what,
						msg.arg1, msg.arg2);
			} catch (RemoteException e) {
				LogUtils.PST(e);
			}
		}
		mCallbacks.finishBroadcast();
	}

	// 播放引擎消息处理
	private void playInfoChanged(int what, String value) {
		final int N = mCallbacks.beginBroadcast();
		for (int i = 0; i < N; i++) {
			try {
				mCallbacks.getBroadcastItem(i).playInfoChanged(what, value);
			} catch (RemoteException e) {
				LogUtils.PST(e);
			}
		}
		mCallbacks.finishBroadcast();
	}

	private void startPlaybackEngine(boolean b) {
		if (mPlayEngineData == null)
			return;
		mPlayUrl = getPlayUrl();
		if (mPlayEngine == null) {
			mPlayEngine = new PlaybackEngine(mPlayEngineData, getNewPlayHandler(), PlayServer.this);
		} else {
			mPlayEngine.SetPara(mPlayEngineData, getNewPlayHandler());
		}
		mPlayEngine.SetBufferTime(mSetBufferTime);
		if (!b) {
			mPos = 0;
		} else {

		}
		mPlayEngine.Play(mPos);
	}

	public void removeCheckTimerStopMessage() {
		mHandler.removeMessages(MSG_WHAT_CHECK_TIMER_STOP);
	}

	private void sendCheckTimerStopMessage(long delayTime) {
		Message msg = mHandler.obtainMessage();
		msg.what = MSG_WHAT_CHECK_TIMER_STOP;
		mHandler.sendMessageDelayed(msg, delayTime);
	}

	private long getOffSecond() {
		long result = mSeconds--;
		return result;
	}

	private long getOffSecond(int hour, int minute) {
		Date dt = new Date(System.currentTimeMillis());

		long stopTimeSeconds = hour * 60 * 60 + minute * 60;
		long curTimeSeconds = dt.getHours() * 60 * 60 + dt.getMinutes() * 60
				+ dt.getSeconds();

		long delaySeconds = stopTimeSeconds - curTimeSeconds;

		if (delaySeconds < 0) {
			if (hour == dt.getHours() && minute == dt.getMinutes()) {
				delaySeconds = 0;
			} else {
				delaySeconds += 24 * 60 * 60;
			}
		}

		return delaySeconds;
	}

	public void setTimerStop(long seconds) {
		removeCheckTimerStopMessage();

		mSeconds = seconds;

		// 启动一秒周期的定时器
		if (mSeconds > 0)
			sendCheckTimerStopMessage(10);
	}

	public void setTimerStop(int hour, int minute) {
		removeCheckTimerStopMessage();

		mHour = hour;
		mMinute = minute;

		long delaySeconds = getOffSecond(mHour, mMinute);

		// 启动一秒周期的定时器
		if (delaySeconds > 0)
			sendCheckTimerStopMessage(10);
	}

	// 获取播放引擎数据，如果返回null，表示数据无法播放，或者专辑播放已经播放完毕
	private PlayEngineData getPlayEngineData(BaseListData playData) {
		PlayEngineData playEngineData = new PlayEngineData();
		try {

			if (mPlayListData instanceof RadioListData) {
				RadioData data = (RadioData) playData.getCurPlayData();
				playEngineData.playType_t = PlayEngineData.LIVE_TYPE;
				playEngineData.url_t = data.url;
				playEngineData.live_Channel_Name = data.name;
				playEngineData.playUrlList = data.playUrlList;
				playEngineData.live_Channel_ID = data.id;
			} else if (mPlayListData instanceof AodListData) {
				AodData data = (AodData) playData.getCurPlayData();
				playEngineData.playType_t = PlayEngineData.DEAMND_TYPE;
				playEngineData.playUrlList = data.playUrlList;
				playEngineData.url_t = data.url;
				playEngineData.duration = data.duration;
			} else {
				playEngineData = null;
				LogUtils.DebugLog("getPlayEngineData not supported playData: "
						+ playData);
			}

			LogUtils.DebugLog("PlayServer getPlayEngineData ret: "
					+ playEngineData);

			if (LogUtils.LOG_ON) {
				playInfoChanged(PlayManager.MSG_WHAT_TITLE_CHANGED,
						getPlayUrl());
			} else {
				playInfoChanged(PlayManager.MSG_WHAT_TITLE_CHANGED, getTitle());
			}
			playInfoChanged(PlayManager.MSG_WHAT_PLAYURL_CHANGED, getPlayUrl());

			if (playEngineData != null)
				playEngineData.live_Channel_RecordPath = AnyRadioApplication.gFileFolderAudio;
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(PlayServer.this, "文件播放失败", Toast.LENGTH_SHORT)
					.show();
			return null;
		}
		return playEngineData;
	}

	private WakeLock wakeLock;
	private WifiLock mWifiLock;

	public void InitWifiWakeLock() {
		Context context = AnyRadioApplication.getContext();
		if (context == null)
			return;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return;
		}
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();

		if (activeNetInfo == null) {
			return;
		}
		String type = activeNetInfo.getTypeName().toUpperCase();
		if (AnyRadioApplication.supportWifiPower()) {

			if (wakeLock == null) {
				PowerManager pm = (PowerManager) context
						.getApplicationContext().getSystemService(
								Context.POWER_SERVICE);
				wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
						"Service");
				wakeLock.setReferenceCounted(false);
				wakeLock.acquire();
			}

			if (type.equals("WIFI")) {
				if (mWifiLock == null) {
					WifiManager wifiManager = (WifiManager) context
							.getSystemService(Context.WIFI_SERVICE);
					mWifiLock = wifiManager.createWifiLock("test_wifi");
					mWifiLock.setReferenceCounted(true);
					mWifiLock.acquire();
				}
			}
		}
	}

	public void ReleaseWifiWakeLock() {
		if (AnyRadioApplication.supportWifiPower()) {
			if (mWifiLock != null && mWifiLock.isHeld()) {
				mWifiLock.release();
				mWifiLock = null;
			}
			if (wakeLock != null && wakeLock.isHeld()) {
				wakeLock.release();
				wakeLock = null;
			}
		}
	}

}
