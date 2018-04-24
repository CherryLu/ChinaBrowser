package cn.anyradio.manager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import cn.anyradio.lib.AnyRadioApplication;
import cn.anyradio.protocol.AodData;
import cn.anyradio.protocol.AodListData;
import cn.anyradio.protocol.BaseListData;
import cn.anyradio.protocol.GeneralBaseData;
import cn.anyradio.protocol.PlayUrlData;
import cn.anyradio.protocol.RadioData;
import cn.anyradio.protocol.RadioListData;
import cn.anyradio.utils.CommUtils;
import cn.anyradio.utils.LogUtils;
import cn.anyradio.utils.PlayEngineData;
import cn.anyradio.utils.PlayPosLog;
import cn.anyradio.utils.PlaybackEngine;
import cn.anyradio.utils.SettingManager;

@SuppressLint("HandlerLeak")
public class PlayManager {
	public static final int MSG_WHAT_TITLE_CHANGED = 101;
	public static final int MSG_WHAT_PLAYURL_CHANGED = 102;
	public static final int MSG_WHAT_PLAY_INDEX = 103;
	public static final int MSG_WHAT_TIMER_STOP = 104;

	// 播放数据对象
	private BaseListData mPlayListData = null;
	// 播放模式常量定义，0:无 1：顺序播放，2：循环播放，3：随机播放 4:列表循环
	public static final int PLAY_MODE_NONE = 0;
	public static final int PLAY_MODE_ORDER = 1;
	public static final int PLAY_MODE_CYCLE = 2;
	public static final int PLAY_MODE_RANDOM = 3;
	public static final int PLAY_MODE_LIST_CYCLE = 4;
	// 播放类型定义
	public static final int PlayType_None = 0; // 无
	public static final int PlayType_RadioListPlay = 1; // 直播电台列表播放
	public static final int PlayType_AlbumListPlay = 2; // 专辑章节列表播放
	public static final int PlayType_AodListPlay = 3; // 点播列表播放
	public static final int PlayType_RecordListPlay = 4; // 录音播放

	public static final int Refresh_AlbumList = 1105;
	public static final int Refresh_Program = 11106;
	// 播放模式，默认，无
	private int mPlayMode = PLAY_MODE_ORDER;
	// 是否显示过对话框，防止不断重复显示
	private boolean hasShownWarnningDialog = false;
	private boolean hasShownStopPlayDialog = false;

	private ArrayList<Handler> mHandlers = new ArrayList<Handler>();

	// 最后一次收到引擎发过来的播放状态
	private int mLastPlayState = 0;
	// 最后一次收到引擎发过来的播放进度
	private int mLastPlayPosition = 0;
	// 最后一次收到引擎发过来的播放总时长
	private int mLastPlayDuration = 0;
	// 录音状态
	private boolean mRecording = false;

	private boolean isRefreshRadio = false;

	public void release() {
		// 退出程序，记住当前文件的播放位置
		savePlayPos();
		mHandlers.clear();
		stop();
		StopTimer();
		unBindPlayService();

		AnyRadioApplication.gPlayManager = null;
	}

	private void StopTimer() {
		if (checkRefreshTimer != null) {
			checkRefreshTimer.cancel();
			checkRefreshTimer = null;
		}
		if (checkUpdateprogramTimer != null) {
			checkUpdateprogramTimer.cancel();
			checkUpdateprogramTimer = null;
		}
		if (checkPlayerTimer != null) {
			checkPlayerTimer.cancel();
			checkPlayerTimer = null;
		}
	}

	public static PlayManager getInstance(Context context) {
		AnyRadioApplication.mContext = context;
		if (AnyRadioApplication.gPlayManager == null) {
			AnyRadioApplication.gPlayManager = new PlayManager();
			// temp
			LogUtils.DebugLog("PlayManager.getInstance() "
					+ AnyRadioApplication.gPlayManager);
		}
		return AnyRadioApplication.gPlayManager;
	}

	private void initPlayData() {
		/*
		 * // 初始化最近播放列表以及成员变量：mPlayData，取最近播放列表第一项 NewHistoryPlayPage hp = new
		 * NewHistoryPlayPage(); if (hp.mData.size() > 0) { mPlayListData =
		 * hp.getBaseListData(0); if (mPlayListData == null) { mPlayListData =
		 * new RadioListData(); RadioData playData = new RadioData();
		 * playData.url =
		 * "http://live.anyradio.cn/10.163.209.145/radios/100001/index_100001.m3u8"
		 * ;
		 * //"http://115.29.45.74/10.163.209.145/radios/100001/index_100001.m3u8"
		 * ;//"http://115.28.148.40/10.144.161.2/radios/index_100001.m3u8";//
		 * "http://live.3gv.ifeng.com/live/zixun?fmt=mp3_32k_mp3"; playData.name
		 * = "中国之声"; playData.id = "100001"; mPlayListData.mList.add(playData);
		 * PlayUrlData item = new PlayUrlData(); item.url = playData.url;
		 * playData.playUrlList.add(item); } } else { mPlayListData = new
		 * RadioListData(); RadioData playData = new RadioData(); playData.url =
		 * "http://live.anyradio.cn/10.163.209.145/radios/100001/index_100001.m3u8"
		 * ;
		 * //"http://115.29.45.74/10.163.209.145/radios/100001/index_100001.m3u8"
		 * ;//"http://115.28.148.40/10.144.161.2/radios/index_100001.m3u8";//
		 * "http://live.3gv.ifeng.com/live/zixun?fmt=mp3_32k_mp3"; playData.name
		 * = "中国之声"; playData.id = "100001"; mPlayListData.mList.add(playData);
		 * PlayUrlData item = new PlayUrlData(); item.url = playData.url;
		 * playData.playUrlList.add(item);
		 * 
		 * } GeneralBaseData data = getCurPlayData(); if (data != null) { mTitle
		 * = data.name; mPlayUrl = data.url; } LogUtils.d("anyradio",
		 * "initPlayData mTitle: " + mTitle);
		 */
	}

	public GeneralBaseData getCurPlayData() {
		return mPlayListData.getCurPlayData();
	}

	public GeneralBaseData getPrePlayData() {
		return mPlayListData.getPrePlayData();
	}

	public GeneralBaseData getNextPlayData() {
		return mPlayListData.getNextPlayData();
	}

	public BaseListData getPlayListData() {
		if (mPlayListData == null)
			initPlayData();
		return mPlayListData;
	}

	public PlayManager() {
		initPlayData();
		// mPlayMode = GetConf.getInstance().getPlayMode();
		// flowManager = new FlowManager();
		// flowManager.addFlowHandler(mHandler);

		bindPlayService();

		// 重新启动程序，清空定时停止设置时间
		// PrefUtils.setPrefString(AnyRadioApplication.mContext,
		// CommUtils.PrefStopHour, "");
		// PrefUtils.setPrefString(AnyRadioApplication.mContext,
		// CommUtils.PrefStopMinute, "");
	}

	private void bindPlayService() {
		Context c = AnyRadioApplication.getContext();
		if (c != null) {
			Intent intent = new Intent(c, PlayServer.class);
			c.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
			c.startService(intent);
		}
	}

	private void unBindPlayService() {
		Context c = AnyRadioApplication.getContext();
		if (c != null) {
			c.unbindService(mConnection);
			c.stopService(new Intent(c, PlayServer.class));
		}
	}

	private double getPlayPos() {
		double r = 0;
		if (mLastPlayDuration > 0)
			r = (double) mLastPlayPosition / (double) mLastPlayDuration;
		return r;
	}

	private int getPlayPosTime() {
		int r = 0;
		if (mLastPlayDuration > 0)
			r = mLastPlayPosition;
		return r;
	}

	// 播放引擎
	// private PlaybackEngine mPlayEngine = null;
	private String mTitle = "";
	private String mPlayUrl = "";

	// 播放引擎消息处理
	public void handlePlaybackEngineMessage(int what, int arg1, int arg2) {
		Message msg = new Message();
		msg.what = what;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		sendMessage2UI(msg);
		if (msg.arg1 == PlaybackEngine.MSG_ARG1_PLAY_STATE) {
			if (msg.arg2 == PlaybackEngine.MSG_ARG2_PLAY_STATE_STOP) {
				flushFlow();
			} else if (msg.arg2 == PlaybackEngine.MSG_ARG2_PLAY_STATE_PAUSE) {
			} else if (msg.arg2 == PlaybackEngine.MSG_ARG2_RECORD_FILE_FINISH) {
				flushFlow();
				// FlowManager.WriteFlowToFile();
				// 播放完成，播放位置恢复为0
				savePlayPos(0, 0);
			}
		} else if (msg.arg1 == PlaybackEngine.MSG_ARG1_CAPACITY) {
			// flowManager.FlushFlow(AnyRadioApplication.mContext, msg.arg2);
		}
	}

	// 获取最后一次播放引擎传回的播放状态
	public int getPlayState() {
		return mLastPlayState;
	}

	// 获取最后一次播放引擎传回的播放状态
	public int getPlayType() {
		return mPlayListData.type;
	}

	// 最后一次收到引擎发过来的播放进度
	public int getLastPlayPosition() {
		return mLastPlayPosition;
	}

	// 最后一次收到引擎发过来的播放总时长
	public int getLastPlayDuration() {
		return mLastPlayDuration;
	}

	// 获取正在播放的频道标题
	public String getPlayTitle() {
		return mTitle;
	}

	public Timer checkRefreshTimer = null;

	private void refreshLogo(final String id) {

		TimerTask taskUse = new TimerTask() {
			@Override
			public void run() {
				if (checkRefreshTimer != null) {
					checkRefreshTimer.cancel();
					checkRefreshTimer = null;
				}
			}
		};
		if (checkRefreshTimer != null) {
			checkRefreshTimer.cancel();
			checkRefreshTimer = null;
		}
		checkRefreshTimer = new Timer();
		checkRefreshTimer.schedule(taskUse, 2000, 2000);
	}

	// 播放函数，需要播放数据参数，必须从BaseListData继承
	// data 参数可以传空，或者索引为-1，播放上次数据，否则，判断和正在播放的数据是否相同
	// 如果数据相同，只播放即可，如果不同，用新的数据替换原来的，启动播放
	private String curUrlString = "";

	public boolean play(String name, String url, String verify,int zgb_play_type) {
		Log.e("name url", name + "," + url);
        this.zgb_play_type = zgb_play_type;
		mPlayListData = new RadioListData();
		boolean ret = true;
		RadioData playData = new RadioData();
		playData.url = url;
		playData.name = name;
		playData.id = verify;
		mPlayListData.mList.add(playData);
		PlayUrlData item = new PlayUrlData();
		item.url = playData.url;
		playData.playUrlList.add(item);
		play(mPlayListData, 0, AnyRadioApplication.mContext);
		return ret;
	}

	private void play(BaseListData data, int playIndex, Context context) {
		boolean sameData = false;

		// 记住正在播放的非直播文件位置
		if (!isStop() && !isPause()) {
			// 如果是正在播放，记住当前文件的播放位置
			savePlayPos();
		}
		if (data == null && playIndex == -1) {
			// 继续用原来数据去播放
			sameData = true;
			if (!isStop() && !isPause())
				return;
		} else if (data == null && playIndex != -1) {
			mPlayListData.playIndex = playIndex;
			sameData = false;
		} else {
			// 比较播放数据是否相同
			if (data.mList.size() > 0 && playIndex < data.mList.size()
					&& playIndex < mPlayListData.mList.size()) {
				sameData = curUrlString.equals(data.mList.get(playIndex).url);
				if (!sameData) {
					mPlayListData = data;
					mPlayListData.playIndex = playIndex;
					curUrlString = data.mList.get(playIndex).url;
				}
			} else {
				mPlayListData = data;
				mPlayListData.playIndex = playIndex;
			}
		}

		LogUtils.DebugLog("PlayManager.play() sameData: " + sameData
				+ " data: " + data + " mPlayListData: " + mPlayListData
				+ " playIndex: " + playIndex);
		// 如果3g情况下超了流量不更改设置不能播放

		if (sameData) {
			if (data != null) {
				// 如果播放数据不变，播放列表有变化，更新一下
				if (data.mList.size() > mPlayListData.mList.size()) {
					mPlayListData = data;
				}
				if (!isStop() && !isPause())
					return;
			}
		} else {
			// 如果数据不同，先停止
			stop();
			// 重新播放，初始化位置
			mLastPlayPosition = 0;
			mLastPlayDuration = 0;
			mLastPlayState = 0;
		}

		initPlaybackEngineAndPlay();
	}

	/**
	 * 更新直播的节目表
	 */
	public Timer checkUpdateprogramTimer = null;

	private void updateProgramData(Context context) {
		TimerTask taskUse = new TimerTask() {
			@Override
			public void run() {
				if (checkUpdateprogramTimer != null) {
					checkUpdateprogramTimer.cancel();
					checkUpdateprogramTimer = null;
				}
				String id = getCurPlayData().id;
			}
		};
		if (checkUpdateprogramTimer != null) {
			checkUpdateprogramTimer.cancel();
			checkUpdateprogramTimer = null;
		}
		checkUpdateprogramTimer = new Timer();
		checkUpdateprogramTimer.schedule(taskUse, 2000, 2000);
	}

	// 启动引擎播放
	private void startPlaybackEngine() {
		double pos = PlayPosLog.getInstance().getPos(getCurPlayData());
		if (pos > 0) {
			enginePlay(pos);
		} else {
			enginePlay(0);
		}
	}

	private void enginePlay(double pos) {
		GeneralBaseData data = getCurPlayData();
		if (data != null) {
			mTitle = data.name;
			mPlayUrl = data.url;
		}
		LogUtils.d("anyradio", "enginePlay mTitle: " + mTitle);
		mPlayMode = PlayEngineData.LIVE_TYPE;// GetConf.getInstance().getPlayMode();
		SRVplay(AnyRadioApplication.mContext, mPlayListData, pos,
				SettingManager.getInstance().getPlayBuffer(), mPlayMode);
	}

	// 停止播放
	public void stop() {
		LogUtils.DebugLog("PlayManager.stop");
		SRVstop(AnyRadioApplication.mContext);
	}

	// 暂停播放
	public void pause() {
		LogUtils.DebugLog("PlayManager.pause");
		SRVpause(AnyRadioApplication.mContext, true);
		// 暂停时，记住播放位置
		savePlayPos();
	}

	private void savePlayPos() {
		savePlayPos(getPlayPos(), getPlayPosTime());
	}

	private void savePlayPos(double pos, int time) {
		GeneralBaseData curPlayData = getCurPlayData();
		if (curPlayData != null && !(curPlayData instanceof RadioData)) {
			PlayPosLog.getInstance().setPos(curPlayData, pos);
			PlayPosLog.getInstance().setPosTime(curPlayData, time);
		}
	}

	// 继续播放
	public void resume() {
		LogUtils.DebugLog("PlayManager.resume");
		if (checkFlowOver()) {
			return;
		}
		SRVpause(AnyRadioApplication.mContext, false);
	}

	// 添加观察器
	public void addHandler(Handler handler, boolean updataState) {
		if (handler != null) {
			mHandlers.add(handler);
			if (updataState)
				SRVupdatePlayState(AnyRadioApplication.mContext);
		}

		LogUtils.DebugLog("PlayManager.addHandler mHandlers.size(): "
				+ mHandlers.size());
	}

	// 删除观察器
	public void removeHandler(Handler handler) {
		mHandlers.remove(handler);
		LogUtils.DebugLog("PlayManager.removeHandler mHandlers.size(): "
				+ mHandlers.size());
	}

	private void sendMessage2UI(Message msg) {

		switch (msg.what) {
		case PlaybackEngine.MSG_WHAT_PLAY_DEMAND_TIME: {
			mLastPlayPosition = msg.arg1;
			mLastPlayDuration = msg.arg2;
			break;
		}
		case PlaybackEngine.MSG_WHAT_PLAY_DEMAND: {
			if (msg.arg1 == PlaybackEngine.MSG_ARG1_PLAY_STATE) {
				// 保存最后一次发送给UI的播放状态
				if (mLastPlayState != msg.arg2) {
					LogUtils.DebugLog("PlayManager mLastPlayState from "
							+ mLastPlayState + " changed to " + msg.arg2);
					mLastPlayState = msg.arg2;

				} else {
					return;
				}

				// 忽略停止消息，不给UI发送停止消息
				if (msg.arg2 == PlaybackEngine.MSG_ARG2_PLAY_STATE_STOP) {
					mRecording = false;
					mLastPlayState = 0;
					return;
				}
			} else if (msg.arg1 == PlaybackEngine.MSG_ARG1_RECORD_STATE) {
				if (msg.arg2 == PlaybackEngine.MSG_ARG2_RECORD_STOP)
					mRecording = false;
			} else if (msg.arg1 == PlaybackEngine.MSG_ARG1_CURRENT_TIME) {
				// 保存播放进度信息
				mLastPlayPosition = msg.arg2;
			} else if (msg.arg1 == PlaybackEngine.MSG_ARG1_TOTAL_DURATION) {
				// 保存播放进度信息
				mLastPlayDuration = msg.arg2;
			}
			break;
		}
		}

		for (int i = 0; i < mHandlers.size(); i++) {
			Message message = new Message();
			message.copyFrom(msg);
			mHandlers.get(i).sendMessage(message);
		}
	}

	// 启动录音功能
	public void StartToRecord() {
		mRecording = true;
		SRVStartToRecord(AnyRadioApplication.mContext);
		LogUtils.DebugLog("实时录音 ...");
	}

	// 保存流量信息到文件
	protected void flushFlow() {
		// if (getPlayType() != PlayType_RecordListPlay)
		// FlowManager.WriteFlowToFile();
	}

	// 停止录音形成录音文件
	public void StopToRecord() {
		mRecording = false;
		SRVStopToRecord(AnyRadioApplication.mContext);
	}

	// 查看当前是否处于录音中
	public boolean Recording() {
		return mRecording;
	}

	public void seek(double persent) {
		LogUtils.DebugLog("PlayManager.SeekToPlay " + persent);
		SRVseek(AnyRadioApplication.mContext, persent);
	}

	public String getPlayUrl() {
		return mPlayUrl;
	}

	public int getPlayMode() {
		if (AnyRadioApplication.Play_allAlbumOrNot == true)// 从专辑播放，播放该专辑按钮进入页面
		{

			mPlayMode = PLAY_MODE_ORDER;// 顺序播放
			SRVchangePlayMode(AnyRadioApplication.mContext, mPlayMode);
			AnyRadioApplication.Play_allAlbumOrNot = false;
		} else {
			// CommUtils.showToast(AnyRadioApplication.mContext, "其他播放");
			// mPlayMode = GetConf.getInstance().getPlayMode();
		}
		return mPlayMode;
	}

	// 设置播放模式
	public void changePlayMode() {

		switch (getPlayType()) {
		case PlayType_AlbumListPlay:
		case PlayType_RecordListPlay:
		case PlayType_AodListPlay: {
			// 列表模式播放，需要切换4种状态，顺序，循环，随机，和无,增加列表循环
			mPlayMode++;
			if (mPlayMode > PLAY_MODE_LIST_CYCLE)
				mPlayMode = PLAY_MODE_NONE;
			SRVchangePlayMode(AnyRadioApplication.mContext, mPlayMode);
			break;
		}
		default: {
			break;
		}
		}
	}

	/**
	 * 设置播放模式
	 * 
	 * @param mode
	 */
	public void setPlayMode(int mode) {
		if (mode >= 0 && mode <= PLAY_MODE_LIST_CYCLE) {
			mPlayMode = mode;
			SRVchangePlayMode(AnyRadioApplication.mContext, mPlayMode);
		}
	}

	public boolean isPause() {
		return isPause(mLastPlayState);
	}

	// 检查PlayManager是否正在播放状态
	public boolean isStop() {
		return isStop(mLastPlayState);
	}

	public static boolean isPause(int playState) {
		return playState == PlaybackEngine.MSG_ARG2_PLAY_STATE_PAUSE;
	}

	public static boolean isPlaying(int playState) {
		return !(isStop(playState) || isPause(playState));
	}

	public static boolean isStop(int playState) {
		boolean ret = false;
		if (playState == 0
				|| playState == PlaybackEngine.MSG_ARG2_PLAY_STATE_STOP
				|| playState == PlaybackEngine.MSG_ARG2_RECORD_FILE_FINISH
				|| playState == PlaybackEngine.MSG_ARG2_LIVE_URL_FORMAT_ERROR) {
			ret = true;
		}
		return ret;
	}

	public boolean isBuffering() {
		return isBuffering(mLastPlayState);
	}

	public static boolean isBuffering(int playState) {
		boolean ret = false;
		if (playState == PlaybackEngine.MSG_ARG2_PLAY_STATE_CONNECTION
				|| playState == PlaybackEngine.MSG_ARG2_PLAY_STATE_INTERACTION
				|| playState == PlaybackEngine.MSG_ARG2_PLAY_STATE_BUFFERING) {
			ret = true;
		}
		return ret;
	}

	// 检查是否是服务器电台,true 服务器下发的电台，false 自定义电台
	public boolean isFromSrvRadio() {
		return CommUtils.isFromSrvRadio(getCurPlayData().id);
	}

	private boolean checkVaild() {
		// 只有点播以及直播，使用网络的时候才开始检测，本地播放不进行检测
		boolean ret = false;
		if (mPlayListData instanceof RadioListData) {
			// 直播
			ret = true;
		} else if (mPlayListData instanceof AodListData) {
			AodData data = (AodData) mPlayListData.getCurPlayData();
			// 点播，只有网络播放监控
			if (CommUtils.existIgnoreCase(data.url, "http://")) {
				ret = true;
			} else {
				ret = false;
			}
		}
		return ret;
	}

	public static final int MSG_LOGO_REFRESH = 199;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
		}

	};

	// 设置第一次提醒流量达到警告值后是否再次提醒
	public void setHasShownWarnningDialog(boolean hasShown) {
		hasShownWarnningDialog = hasShown;
	}

	public void removeCheckTimerStopMessage() {
	}

	public void setTimerStop(int hour, int minute) {
	}

	/**
	 * 设置暂停倒计时
	 * 
	 * @param seconds
	 *            单位秒
	 */
	public void setTimerStop(long seconds) {
	}

	public void setmPlayListData(BaseListData playListData) {
	}

	private long lastPlayerTime = 0L;
	private long currentPlayerTime = 0L;
	public Timer checkPlayerTimer = null;

	public void initPlaybackEngineAndPlay() {
		// 初始化播放引擎需要的数据
		// if (lastPlayerTime == 0) {
		// lastPlayerTime = System.currentTimeMillis();
		// }
		stop();
		currentPlayerTime = System.currentTimeMillis();
		if (currentPlayerTime - lastPlayerTime >= 2000) {
			lastPlayerTime = System.currentTimeMillis();
			startPlaybackEngine();
		} else {
			lastPlayerTime = System.currentTimeMillis();
			TimerTask taskUse = new TimerTask() {
				@Override
				public void run() {
					if (checkPlayerTimer != null) {
						checkPlayerTimer.cancel();
						checkPlayerTimer = null;
					}
					startPlaybackEngine();
				}
			};
			if (checkPlayerTimer != null) {
				checkPlayerTimer.cancel();
				checkPlayerTimer = null;
			}
			checkPlayerTimer = new Timer();
			checkPlayerTimer.schedule(taskUse, 2000, 2000);
		}
	}

	public static void startPlayService(Context context, Intent i) {
		context.startService(i);
	}

	public static void SRVseek(Context context, double v) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_SEEK);
		i.putExtra(PlayServer.EXTRA_SEEK, v);
		startPlayService(context, i);
	}

	public static void SRVsetChannel(Context context, String channel) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_SETCHANNEL);
		i.putExtra(PlayServer.ACTION_SETCHANNEL, channel);
		startPlayService(context, i);
	}
    public static int zgb_play_type;
	public static void SRVplay(Context context, BaseListData data, double pos,
			int bufferTime, int playMode) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_PLAY);
		i.putExtra(PlayServer.EXTRA_PLAY_DATA, data);
		i.putExtra(PlayServer.EXTRA_SEEK, pos);
		i.putExtra(PlayServer.EXTRA_PLAY_MODE, playMode);
		i.putExtra(PlayServer.EXTRA_SETBUFFERTIME, bufferTime);
        //zgb add
        i.putExtra(PlayServer.ZGB_PLAY_TYPE, zgb_play_type);
		startPlayService(context, i);
	}

	public static void SRVpause(Context context, boolean v) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_PAUSE);
		i.putExtra(PlayServer.EXTRA_PAUSE, v);
		startPlayService(context, i);
	}

	public static void SRVLogEnable(Context context, boolean v) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_LOG);
		i.putExtra(PlayServer.EXTRA_PAUSE, v);
		startPlayService(context, i);
	}

	public static void SRVAutoTestEnable(Context context, boolean v) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_AUTOTEST);
		i.putExtra(PlayServer.EXTRA_PAUSE, v);
		startPlayService(context, i);
	}

	public static void SRVstop(Context context) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_STOP);
		startPlayService(context, i);
	}

	public static void SRVStartToRecord(Context context) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_STARTTORECORD);
		startPlayService(context, i);
	}

	public static void SRVStopToRecord(Context context) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_STOPTORECORD);
		startPlayService(context, i);
	}

	/**
	 * 启动以秒为单位的倒计时
	 * 
	 * @param mContext
	 * @param seconds
	 */
	public static void SRVsetTimerStop(Context context, long seconds) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_SET_TIMER_STOP_SECONDS);
		i.putExtra(PlayServer.EXTRA_SECONDS, seconds);
		startPlayService(context, i);
	}

	public static void SRVsetTimerStop(Context context, int hour, int minute) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_SET_TIMER_STOP);
		i.putExtra(PlayServer.EXTRA_HOUR, hour);
		i.putExtra(PlayServer.EXTRA_MINUTE, minute);
		startPlayService(context, i);
	}

	private void SRVchangePlayMode(Context context, int v) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_CHANGE_PLAY_MODE);
		i.putExtra(PlayServer.EXTRA_PLAY_MODE, v);
		startPlayService(context, i);
	}

	private void SRVremoveCheckTimerStopMessage(Context context) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_REMOVE_CHECKTIMER_STOPMESSAGE);
		startPlayService(context, i);
	}

	private void SRVupdatePlayState(Context context) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_UPDATE_STATE);
		startPlayService(context, i);
	}

	private void SRVRefreshPlayState(Context context, BaseListData data) {
		if (context == null) {
			return;
		}
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_REFRESH_BASELISTDATA);
		i.putExtra(PlayServer.EXTRA_PLAY_DATA, data);
		startPlayService(context, i);
	}

	public static void SRVEmptyFlow(Context context) {
		Intent i = new Intent(context, PlayServer.class);
		i.setAction(PlayServer.ACTION_EMPTY_FLOW);
		startPlayService(context, i);
	}

	// ----------------------------------------------------------------------
	private IRemotePlayService mService = null;

	// ----------------------------------------------------------------------
	// Code showing how to deal with callbacks.
	// ----------------------------------------------------------------------

	/**
	 * This implementation is used to receive callbacks from the remote service.
	 */
	private IRemotePlayServiceCallback mCallback = new IRemotePlayServiceCallback.Stub() {
		/**
		 * This is called by the remote service regularly to tell us about new
		 * values. Note that IPC calls are dispatched through a thread pool
		 * running in each process, so the code executing here will NOT be
		 * running in our main thread like most other things -- so, to update
		 * the UI, we need to use a Handler to hop over there.
		 */
		@Override
		public void playStateChanged(int what, int arg1, int arg2)
				throws RemoteException {
			handlePlaybackEngineMessage(what, arg1, arg2);
		}

		@Override
		public void playInfoChanged(int what, String value)
				throws RemoteException {
			switch (what) {
			case MSG_WHAT_TITLE_CHANGED:
				if (!TextUtils.isEmpty(value)) {
					mTitle = value;
					LogUtils.d("anyradio", "playInfoChanged mTitle: " + mTitle);
				}
				break;

			case MSG_WHAT_PLAYURL_CHANGED:
				if (!TextUtils.isEmpty(value))
					mPlayUrl = value;
				break;

			case MSG_WHAT_PLAY_INDEX:
				if (!TextUtils.isEmpty(value)) {
					if (mPlayListData.playIndex != CommUtils.convert2int(value)
							&& getPlayType() == PlayType_AlbumListPlay
							&& mPlayMode == PLAY_MODE_ORDER) {
						if (CommUtils.convert2int(value) == mPlayListData.mList
								.size() - 1) {
							refreshAlbumChaptersListData();
						}
					}

					if (mPlayListData != null)
						mPlayListData.playIndex = CommUtils.convert2int(value);

				}
				break;
			case MSG_WHAT_TIMER_STOP: {
				if (TextUtils.isEmpty(value)) {
				} else {
					// 通知刷新最后剩余时间
					Message msg = new Message();
					msg.what = MSG_WHAT_TIMER_STOP;
					msg.obj = value;
					sendMessage2UI(msg);
				}
				break;
			}
			default:
				break;
			}
		}
	};

	/**
	 * Class for interacting with the main interface of the service.
	 */
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			// This is called when the connection with the service has been
			// established, giving us the service object we can use to
			// interact with the service. We are communicating with our
			// service through an IDL interface, so get a client-side
			// representation of that from the raw service object.
			mService = IRemotePlayService.Stub.asInterface(service);
			LogUtils.DebugLog("ServiceConnection onServiceConnected "
					+ mService);
			// We want to monitor the service for as long as we are
			// connected to it.
			try {
				mService.registerCallback(mCallback);
				LogUtils.DebugLog("ServiceConnection registerCallback "
						+ mCallback);
			} catch (RemoteException e) {
				// In this case the service has crashed before we could even
				// do anything with it; we can count on soon being
				// disconnected (and then reconnected if it can be restarted)
				// so there is no need to do anything here.
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			mService = null;
			LogUtils.DebugLog("ServiceConnection onServiceDisconnected");

			// 崩溃重新连接播放服务
			bindPlayService();
		}
	};

	private boolean checkFlowOver() {
		if (mPlayListData == null)
			return false;

		if (!SettingManager.getInstance().isCheckOverflow()) {
			// 如果未打开流量监控，不检查
			return false;
		}

		if (mPlayListData instanceof AodListData) {
			// 如果是本地文件，不检查
			AodData data = (AodData) getCurPlayData();
			if (data != null && data.isLocalFile()) {
				return false;
			}
		}

		if (CommUtils.isWifi(AnyRadioApplication.mContext)) {
			// 如果是wifi，不检查
			return false;
		}

		return true;
	}

	public int getLastState() {
		LogUtils.DebugLog("PlayManager.getLastState mLastPlayState: "
				+ mLastPlayState);
		return mLastPlayState;
	}

	private void refreshAlbumChaptersListData() {

	}

	private void initAlbumChaptersListData() {
	}

	public boolean isRefreshRadio() {
		return isRefreshRadio;
	}

	// private ProgramData mCurProgramData;
	// public static final int MSG_UPDATE_CURPROGRAM = 105;
	//
	// public ProgramData getCurProgramData() {
	// return mCurProgramData;
	// }
	//
	// /**
	// * 设置当前的播放节目
	// *
	// * @param data
	// */
	// public void setCurProgram(ProgramData data) {
	// if (data == mCurProgramData)
	// return;
	// this.mCurProgramData = data;
	// for (Handler h : mHandlers) {
	// Message msg = new Message();
	// msg.what = MSG_UPDATE_CURPROGRAM;
	// h.sendMessage(msg);
	// }
	// }
}
