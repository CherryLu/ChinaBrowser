/****************************
文件名:BaseProtocolPage.java

创建时间:2013-4-2
所在包:
作者:罗泽锋
说明:所有协议类的基类
用来实现所有协议统一逻辑方法

 ****************************/

package com.chinabrowser.net;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import com.chinabrowser.APP;
import com.chinabrowser.activity.BaseActivity;
import com.chinabrowser.utils.ExtraInterface;
import com.chinabrowser.utils.FileUtils;
import com.chinabrowser.utils.JsonUtils;
import com.chinabrowser.utils.LogUtils;
import com.chinabrowser.utils.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;


public abstract class BaseProtocolPage implements Serializable {
	private static final long serialVersionUID = 1L;

	protected Object waitmHandlerList;
	protected Object waitmActivityList;
	// 缓存文件测试调试用开关
	public static boolean USE_CACHE = true;

	// 抽象方法，有派生类决定自己的协议动作名称
	abstract public String getActionName();

	// 抽象方法，有派生类决定自己协议动作对应的参数
	abstract public String getExtParam(Object param);

	// 抽象方法，有派生类决定自己协议动作对应的不加密参数，上传大文件协议专用
	public String getExtNoEncryptParam(Object param){
		return "";
	}

	public String getSavePathKey(Object param) {
		return getExtParam(param);
	}

	// 抽象方法，有派生类决定自己是否支持协议数据缓存
	abstract public boolean supportCacheFile();

	// 抽象方法，由派生类决定自己协议成功的消息what
	abstract public int getMsgWhatOk();

	// 抽象方法，由派生类决定自己协议错误的消息what
	abstract public int getMsgWhatError();

	abstract public int getMsgWhatDataNotChange();

	// 本协议缓存文件的全路径保存地址
	private String savePath = null;
	// 请求协议数据的url地址
	private String pageUrl = null;

	// 协议参数保存对象
	public Object mParam = null;
	// 协议返回码
	private String errcode = "";
	// 协议返回消息
	private String msg = "";

	public String uptime = "";

	// 抽象方法，有派生类 实现，用来保存具体协议数据对象
	public abstract void setData(Object data);

	// Handler数组成员变量
	private ArrayList<Handler> mHandlerList = new ArrayList<Handler>();
	// BaseFragmentActivity数组成员变量
	private ArrayList<BaseActivity> mActivityList = new ArrayList<BaseActivity>();

	// 是否有cache，如果没有cache，根据需要是否显示等待对话框
	private boolean hasCache;

	public static final int NetworkNotConnected = -99999;

	public static final int DEFSULT_MSG_ARG1 = 0;

	DownloadProtocolTask task;
	protected String mActionName;

	// 构造函数，参数协议地址和一个任意类型的参数
	public BaseProtocolPage(String url, Object param, Handler handler,
							BaseActivity activity) {
		init(url, param, handler, activity, false, true);
	}

	public BaseProtocolPage(String url, Object param, Handler handler, BaseActivity activity, boolean initData) {
		init(url, param, handler, activity, initData, true);
	}

	public BaseProtocolPage(String url, Object param, Handler handler,
							BaseActivity activity, boolean initData, String actionName) {
		init(url, param, handler, activity, initData, true, actionName);
	}

	private void initMember(String url, Object param, Handler handler, BaseActivity activity) {
		if (url != null)
			pageUrl = url;
		if (param != null)
			mParam = param;
		addHandler(handler);
		addActivity(activity);
	}

	private void init(String url, Object param, Handler handler, BaseActivity activity, boolean initData, boolean loadCache) {
		init(url, param, handler, activity, initData, loadCache, mActionName);
	}

	private void init(String url, Object param, Handler handler, BaseActivity activity, boolean initData, boolean loadCache, String actionName) {
		mActionName = actionName;
		waitmHandlerList = new Object();
		waitmActivityList = new Object();
		initMember(url, param, handler, activity);

		// 读取缓存文件中的数据，并初始化
		if (supportCacheFile() && USE_CACHE) {
			savePath = FileUtils.getAppBasePath()
					+ APP.ProtocolCachePath
					+ File.separator
					+ FileUtils.convertFilenameFromUrl("", getActionName()
							+ "_" + getSavePathKey(mParam));

			LogUtils.d("BaseProtocolPage ", getClass().getCanonicalName() + " savePath: " + savePath);

			if (loadCache) {
				// 先检查是否有缓存，有缓存，标志位状态更新，在refresh的线程里面决定是否显示等待对话框
				File file = new File(savePath);
				if (file.exists())
					hasCache = true;
				else
					hasCache = false;

				if (initData) {
					loadCacheData(false);
				} else {
					new Thread(new LoadLocalDataThread()).start();
				}
			}
		}
		setDownThreadPriority(false);
	}

	// 添加Handler对象
	public void addHandler(Handler handler) {
		synchronized (waitmHandlerList) {
			if (handler != null) {
				if (!mHandlerList.contains(handler)) {
					mHandlerList.add(handler);
					APP.protocolHandlerCount++;
					LogUtils.d("addHandler AnyRadioApplication.protocolHandlerCount: ",APP.protocolHandlerCount+"");
				}
			}
		}
	}

	public void removeHandler(Handler handler) {
		synchronized (waitmHandlerList) {
			if (handler != null) {
				if (mHandlerList.contains(handler)) {

					mHandlerList.remove(handler);
					APP.protocolHandlerCount--;
					LogUtils.d("addHandler AnyRadioApplication.protocolHandlerCount: ",APP.protocolHandlerCount+"");
				}
			}
		}
	}

	// 添加Activity对象
	private void addActivity(BaseActivity activity) {
		synchronized (waitmActivityList) {
			if (activity != null) {
				activity.addProtocolPage(this);
				if (!mActivityList.contains(activity)) {
					LogUtils.d("MonitorHandler activity add " ,activity.getClass().getName());
					mActivityList.add(activity);
					APP.protocolActivityCount++;
					LogUtils.d("addHandler AnyRadioApplication.protocolHandlerCount: ",APP.protocolActivityCount+"");

				}
			} else {
				LogUtils.d(getClass().getCanonicalName(),"addActivity is null");
			}
		}
	}

	public void loadCacheData(boolean sendMsg) {
		// 从缓存文件中获取数据
		Object data = loadDataFromFile(savePath);
		if (data != null) {
			setData(data);
			hasCache = true;
			if (sendMsg) {
				sendMessag2UI(getMsgWhatOk(), DEFSULT_MSG_ARG1);
				hideWaitDialog();
			}
		}
	}

	// 抽象方法，派生类必须实现此方法，根据不同协议解析响应的数据
	abstract public Object parserJson(byte[] response);

	// 更新协议数据的方法，更新结果通过handler的msg异步传送给调用者
	// 此方法适合在从 BaseFragmentActivity 继承的类里调用
	public void refresh(Object param) {
		init(null, param, null, null, false, false, mActionName);
		stratDownloadThread();
	}

	// 更新协议数据的方法，同步调用，不启动线程
	// 此方法适合在从 BaseFragmentActivity 继承的类里调用
	public Message refreshSync(Object param) {
		init(null, param, null, null, false, false, mActionName);
		return runDownload();
	}
	
	// 更新协议扩展方法
	public void refresh(String url, Object param, Handler handler,
                        BaseActivity activity) {
		init(url, param, handler, activity, false, false, mActionName);
		stratDownloadThread();
	}

	private void showWaitDialog() {
		if (showWaitDialog && !hasCache)
			for (int i = 0; i < mActivityList.size(); i++) {
				mActivityList.get(i).showWaitDialog();
			}
	}

	private void hideWaitDialog() {
		// 隐藏等待对话框
		if (showWaitDialog) {
			for (int i = 0; i < mActivityList.size(); i++) {
				if (mActivityList.get(i) != null) {// 退出程序的时候报空指针异常，，苑贺杰
					mActivityList.get(i).hideWaitDialog();
				}

			}
		}
	}

	private boolean compareBtyeArray(byte[] d1, byte[] d2) {
		boolean r = false;
		if (d1.length != d2.length)
			return r;
		for (int i = 0; i < d1.length; i++) {
			if (d1[i] != d2[i])
				return false;
		}
		return true;
	}

	// 下载协议数据对象并解析处理函数
	private synchronized Message runDownload() {

		Message ret = new Message();
		if (!NetUtils.isNetworkConnected(APP.getContext())) {
			sendMessag2UI(getMsgWhatError(), NetworkNotConnected);
			ret.what = getMsgWhatError();
			return ret;
		}

		showWaitDialog();

		byte[] response = null;
		response = NetUtils.getHttpDataUsePostForAction(pageUrl, getActionName(), getExtParam(mParam), getExtNoEncryptParam(mParam), isUseSingleAppserv);
		LogUtils.d(getClass().getCanonicalName()," server parserJson start" + getExtParam(mParam));

		errcode = "";
		msg = "";

		int msgWhat = -1;

		if (response != null && supportCacheFile()) {
			// 需要检查缓存文件和下载文件数据是否一致，如果一致，只通知UI没有更新即可
			byte[] cacheData = FileUtils.getFileDataByte(savePath);
			if (cacheData != null) {
				if (compareBtyeArray(response, cacheData)) {
					// 完全一致，不需要处理，统计UI无新的数据
					msgWhat = getMsgWhatDataNotChange();
				}
			}
		}

		// 解释数据
		Object res = parserJson(response);
		LogUtils.d(getClass().getCanonicalName()," server parserJson end " + res);

		// 判断是否解析成功
		if (res != null) {
			if (msgWhat < 0) {
				msgWhat = getMsgWhatOk();
				if (supportCacheFile()) {
					// 如果支持缓存，保存协议数据内容到缓存文件
					FileUtils.saveAsFile(response, savePath);
					LogUtils.d("BaseProtocolPage2 " , getClass().getCanonicalName() + " savePath: " + savePath);
				}
			}
			// 保存数据到成员变量
			setData(res);
		} else {
			msgWhat = getMsgWhatError();
		}

		/*// 统一处理协议的action load事件
		JSONArray actionArray = getActionJsonArray(response);
		if (actionArray != null && actionArray.length() > 0) {
			// 解析
			ArrayList<Action> actionList = new ArrayList<Action>();
			for (int i = 0; i < actionArray.length(); ++i) {
				Action item = new Action();
				actionList.add(item);
				item.parse((JSONObject) JsonUtils.getJsonArray(actionArray, i));
			}

			// 处理load事件
			BaseFragmentActivity activity = null;
			if (mActivityList.size() > 0)
				activity = mActivityList.get(0);
			if (activity != null) {
				activity.actionLoadOnClick(actionList);
			}
		}*/

		int arg1 = DEFSULT_MSG_ARG1;
		if (msgWhat == getMsgWhatError()) {
			try {
				arg1 = Integer.valueOf(errcode);
			} catch (Exception e) {
				e.printStackTrace();
			}
			sendMessag2UI(msgWhat, arg1);
		} else {
			sendMessag2UI(msgWhat, DEFSULT_MSG_ARG1);
		}
		hideWaitDialog();
		
		ret.what = msgWhat;
		ret.arg1 = arg1;
		return ret;
	}

	private void sendMessag2UI(int msgWhat, int msgArg1) {
		// 发送消息给UI
		if (mHandlerList.size() == 0)
			return;

		String debugTips = "Protocol: \naction: " + getActionName()
				+ "\nparam: " + getExtParam(mParam) + "\ncode: " + errcode
				+ "\nmsg: " + msg;

		Bundle bundle = new Bundle();
		bundle.putString("msg", debugTips);

		LogUtils.d("BaseProtocolPage.sendMessag2UI: " ,debugTips);

		for (int i = 0; i < mHandlerList.size(); i++) {
			Message msg = mHandlerList.get(i).obtainMessage();
			msg.what = msgWhat;
			msg.arg1 = msgArg1;
			msg.obj = this.msg;
			if (extraInterface != null) {
				msg.arg2 = extraInterface.getExtra();
			}
			msg.setData(bundle);
			mHandlerList.get(i).sendMessage(msg);
		}
	}

	// 启动下载线程
	private void stratDownloadThread() {
		// 启动后台线程下载数据
		// TODO：此处需要检查相同的下载任务是否已经在队列中，
		// 如果已经在下载队列中，不在启动新的线程下载
		// DownloadThread dt = new DownloadThread();
		// new Thread(dt).start();

		// BaseProtocolPage.initExecutor();
		task = new DownloadProtocolTask(this);
		// Log.i("lzf", "DownloadProtocolTask stratDownloadThread " + this +
		// " task: " + task);
		// task.executeOnExecutor(downloadTaskExecutor, "");
		task.execute("");
	}

	// 从缓存文件中load协议数据对象
	public Object loadDataFromFile(String path) {
		byte[] response = FileUtils.getFileDataByte(path);
		LogUtils.d(getClass().getCanonicalName(), " local parserJson start");
		Object res = parserJson(response);
		LogUtils.d(getClass().getCanonicalName()," local parserJson end " + res);
		return res;
	}

	// 通过数据返回json数组
	public JSONArray getJsonArray(byte[] response) {
		if (response != null) {
			String json = NetUtils.byteToString(response);
			// 解析数据
			try {
				JSONObject jo = new JSONObject(json);
				errcode = JsonUtils.getString(jo, "errcode");
				msg = JsonUtils.getString(jo, "msg");
				uptime = JsonUtils.getString(jo, "uptime");
				if (errcode.equals("1") && msg.equals("success")) {
					JSONArray jsonArray = JsonUtils.getJSONArray(jo, "result");
					return jsonArray;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 通过数据返回action对象json数组
	private JSONArray getActionJsonArray(byte[] response) {
		if (response != null) {
			String json = NetUtils.byteToString(response);
			// 解析数据
			try {
				JSONObject jo = new JSONObject(json);
				JSONArray jsonArray = JsonUtils.getJSONArray(jo, "action");
				return jsonArray;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// 如果界面退出，请主动调用此函数，页面将不会收到此消息
	// 此函数派生类可以重载，自己实现，比如RecommendPageList需要把子页面的handler删除
	public void delAllHandler() {
		synchronized (waitmHandlerList) {
			for (int i = 0; i < mHandlerList.size(); i++) {

				mHandlerList.set(i, null);
				APP.protocolHandlerCount--;

			}
			mHandlerList.clear();
		}
	}

	// 如果界面退出，请主动调用此函数，页面将不会收到此消息
	// 此函数派生类可以重载，自己实现，比如RecommendPageList需要把子页面的handler删除
	public void delAllActivity() {
		synchronized (waitmActivityList) {
			for (int i = 0; i < mActivityList.size(); i++) {
				mActivityList.set(i, null);
				APP.protocolActivityCount--;

			}
			mActivityList.clear();
		}
	}

	// 是否在协议联网过程中显示等待对话框
	private boolean showWaitDialog = false;

	private static int downThreadPriority = Thread.NORM_PRIORITY - 1;

	// 共用方法，设置是否在协议请求的时候弹出等待对话框
	public void setShowWaitDialogState(boolean show) {
		showWaitDialog = false;
	}

	class LoadLocalDataThread implements Runnable {
		public void run() {
			NetUtils.setThreadPriorityLow();
			loadCacheData(true);
		}
	}

	public void setDownThreadPriority(boolean high) {
		if (high)
			downThreadPriority = Thread.MAX_PRIORITY;
		else
			downThreadPriority = Thread.NORM_PRIORITY - 1;
	}

	/**
	 * 下载的线程
	 * 
	 * @author luozefeng
	 */
	private static int DownloadProtocolTaskCount = 0;

	private class DownloadProtocolTask extends AsyncTask<Object, Void, Boolean> {
		BaseProtocolPage protocolPage;

		public DownloadProtocolTask(BaseProtocolPage baseProtocolPage) {
			protocolPage = baseProtocolPage;
		}

		@Override
		protected Boolean doInBackground(Object... params) {
			DownloadProtocolTaskCount++;
			// Log.d("lzf", "DownloadProtocolTask.doInBackground begin "
			// + protocolPage + " TaskCount: " + DownloadProtocolTaskCount);
			// Log.i("lzf", "DownloadProtocolTask.doInBackground " +
			// protocolPage + " task:" + protocolPage.task);

			// Log.d("lzf", "DownloadProtocolTask.doInBackground 0 " +
			// protocolPage + " " + protocolPage.getActionName() + " " +
			// protocolPage.getExtParam(protocolPage.mParam).toString());
			try {
				protocolPage.runDownload();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.d("lzf", "DownloadProtocolTask.doInBackground 1 " +
			// protocolPage + " " + protocolPage.getActionName() + " " +
			// protocolPage.getExtParam(protocolPage.mParam).toString());
			DownloadProtocolTaskCount--;
			// Log.d("lzf", "DownloadProtocolTask.doInBackground end "
			// + protocolPage + " TaskCount: " + DownloadProtocolTaskCount);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean ret) {
		}

		@Override
		protected void onCancelled(Boolean ret) {
			// Log.w("lzf", "DownloadProtocolTask.onCancelled " + protocolPage +
			// " " + protocolPage.getActionName() + " " +
			// protocolPage.getExtParam(protocolPage.mParam).toString());
			super.onCancelled(ret);
		}
	}

	public void cancel() {
		// Log.w("lzf", "DownloadProtocolTask. cancel " + this);
		if (task != null) {
			task.cancel(USE_CACHE);
			task = null;
		}
	}

	private ExtraInterface extraInterface;

	public void setExtraInterface(ExtraInterface extraInterface) {
		this.extraInterface = extraInterface;
	}

	// 使用单独的getappserv使用协议
	public boolean isUseSingleAppserv = false;

	public void useSingleAppServer(boolean useSingleAppserv) {

		isUseSingleAppserv = useSingleAppserv;
	}
}
