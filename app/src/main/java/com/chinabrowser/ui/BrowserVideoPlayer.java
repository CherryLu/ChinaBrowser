package com.chinabrowser.ui;


import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.chinabrowser.R;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import cn.anyradio.utils.CommUtils;
import tv.danmaku.ijk.media.example.widget.media.IjkVideoViewAlone;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import static android.content.Context.AUDIO_SERVICE;

/**
 * Created by Administrator on 2018/4/28.
 */

public class BrowserVideoPlayer extends RelativeLayout implements View.OnClickListener,View.OnTouchListener,SeekBar.OnSeekBarChangeListener,
        IMediaPlayer.OnPreparedListener,IMediaPlayer.OnErrorListener,IMediaPlayer.OnCompletionListener{



    AudioManager mAudioManager;
    private float startY;//记录手指按下时的Y坐标
    private float startX;//记录手指按下时的Y坐标
    private int curVol;//当前系统音量
    private float curBright = 0.0f;//当前亮度
    private int maxVolme;
    private int fromVol;//记录进入播放器前的音量
    private String startTime, endTime;

    /**
     * 数据变量
     **/
    private float touchLastX;
    private int position, touchStep, touchPosition, duration, curDuration, lastDuration, skipDuration;
    private boolean videoControllerShow;
    private String formatTotalTime;
    private int mPositionWhenPaused = -1;

    /**
     * handler相关变量
     **/
    public static final int UPDATE_DURATION = 1071;// 更新进度
    public static final int UTILS_BAR_CLOSE = 1073;// 工具条自动消失
    public static final int TEST_REPLAY = 1075;// 在出现错误的时候尝试重新播放

    private Context mContext;
    private Activity mActivity;

    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case UPDATE_DURATION:
                    if (videoView != null) {
                        curDuration = videoView.getCurrentPosition();
//					NewLogUtils.d("videoview", "", "curDuration="+curDuration);
                        videoSeekBar.setProgress(curDuration);
                        if (lastDuration == curDuration && videoView.isPlaying()) {//正在缓冲
                            progressBar.setVisibility(View.VISIBLE);
                            //视频开始卡顿时
                        } else {//正常播放
                            progressBar.setVisibility(View.GONE);
                            //视频结束卡顿时

                        }
                        lastDuration = curDuration;
                    }
                    break;
                case UTILS_BAR_CLOSE:
                    if (!isAn) {
                        utilsBarClose();
                        removetAutoUtilsBarClose();
                    }
                    break;
                case TEST_REPLAY:
                    int erroDur = msg.arg1;
                    videoView.seekTo(erroDur);
                    videoView.start();
                    if (skipDuration > 0) {
                        videoView.seekTo(skipDuration);
                    }
                    skipDuration = 0;
                    break;
            }
        }

    };

    public BrowserVideoPlayer(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public BrowserVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public BrowserVideoPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    private void init() {
        mAudioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
        maxVolme = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        fromVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        initView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

    }

    private View mainView;
    private RelativeLayout viewBox;
    private IjkVideoViewAlone videoView;
    private ImageView videoPauseBtn,screenSwitchBtn,share_video,backView;
    private TextView nameView,videoCurTimeText,videoTotalTimeText,volume_view,brightness_view,waitTxtView;
    private SeekBar videoSeekBar;
    private LinearLayout progressBar;
    private RelativeLayout centerLayout,videUpLayout;
    private LinearLayout videoControllerLayout;
    private boolean isFull = false;
    private boolean isForceLandscape;
    private String playUrl;
    private int mParentHeight;

    private void initView() {
        mainView = LayoutInflater.from(mContext).inflate(R.layout.video_layout, null);
        centerLayout = mainView.findViewById(R.id.video_play_center_layout);
        centerLayout.setVisibility(View.GONE);
        volume_view = (TextView) mainView.findViewById(R.id.volume_txt);
        brightness_view = (TextView) mainView.findViewById(R.id.brightness_txt);

        viewBox = (RelativeLayout) mainView.findViewById(R.id.viewBox);
        videoView = (IjkVideoViewAlone) mainView.findViewById(R.id.videoView);
        videoPauseBtn = (ImageView) mainView.findViewById(R.id.videoPauseBtn);
        videoPauseBtn.setOnClickListener(this);
        screenSwitchBtn = mainView.findViewById(R.id.screen_status_img);

        videoControllerLayout = (LinearLayout) mainView.findViewById(R.id.videoControllerLayout);
        videoControllerLayout.setOnClickListener(this);

        videUpLayout = mainView.findViewById(R.id.videoUpLayout);
        videUpLayout.setVisibility(GONE);

        videoCurTimeText = (TextView) mainView.findViewById(R.id.videoCurTime);
        videoTotalTimeText = (TextView) mainView.findViewById(R.id.videoTotalTime);
        videoSeekBar = (SeekBar) mainView.findViewById(R.id.videoSeekBar);
        progressBar = mainView.findViewById(R.id.progressBar_layout);
        progressBar.setVisibility(View.GONE);

        waitTxtView = (TextView) mainView.findViewById(R.id.video_wait_msg);

        videoSeekBar.setOnSeekBarChangeListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        if (Build.VERSION.SDK_INT >= 17) {
            videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {

                @Override
                public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                    return false;
                }

            });
        }

        backView = (ImageView) mainView.findViewById(R.id.video_up_back);
        share_video = mainView.findViewById(R.id.share_video);
        screenSwitchBtn.setOnClickListener(this);

        videoView.setOnErrorListener(this);
        viewBox.setOnClickListener(this);
        backView.setOnClickListener(this);
        share_video.setOnClickListener(this);

        viewBox.setOnTouchListener(this);
        videoSeekBar.setOnTouchListener(this);
        screenSwitchBtn.setOnTouchListener(this);

        nameView = (TextView) mainView.findViewById(R.id.video_name);

        addView(mainView);
        utilsBarClose();
    }

    public int getCurDuration() {
        if (videoView != null) {
            return videoView.getCurrentPosition();
        }
        return 0;
    }

    /***
     * 根据存储进度设置播放进度
     * @param skipDuration
     */
    public void setSkipDuration(int skipDuration) {
        this.skipDuration = skipDuration;
    }



    /**
     * 上面工具栏上移动动画开启
     */
    private void startUpBarAnUp() {

        if (videUpLayout.getVisibility() == View.GONE) {
            return;
        }
        if (upBarUpAnSet == null) {
            upBarUpAnSet = new AlphaAnimation(1.0f, 0.0f);
            upBarUpAnSet.setDuration(barAnDuration);
            upBarUpAnSet.setFillAfter(true);
            upBarUpAnSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAn = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        videUpLayout.startAnimation(upBarUpAnSet);
    }

    /**
     * 上面工具栏下移动动画开启
     */
    private void startUpBarAnDown() {

        if (upBarDownAnSet == null) {

            upBarDownAnSet = new AlphaAnimation(0.0f, 1.0f);
            upBarDownAnSet.setDuration(barAnDuration);
            upBarDownAnSet.setFillAfter(true);
            upBarDownAnSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAn = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        videUpLayout.startAnimation(upBarDownAnSet);
    }

    /**
     * 下面工具栏下移动动画开启
     */
    private void startDownBarAnDown() {
        if (videoControllerLayout.getVisibility() == View.GONE) {
            return;
        }
        if (downBarDownAnSet == null) {
            downBarDownAnSet = new AlphaAnimation(1.0f, 0.0f);
            downBarDownAnSet.setDuration(barAnDuration);
            downBarDownAnSet.setFillAfter(true);

            downBarDownAnSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    isAn = false;
                    videoControllerLayout.clearAnimation();
                    videoControllerLayout.invalidate();
                    videUpLayout.clearAnimation();
                    videUpLayout.invalidate();
                    videoControllerLayout.setVisibility(View.GONE);
                    videUpLayout.setVisibility(View.GONE);
                    videoPauseBtn.setVisibility(GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
        videoControllerLayout.startAnimation(downBarDownAnSet);
    }

    /**
     * 开始工具条自动消失计数器
     */
    private void startAutoUtilsBarClose() {
        removetAutoUtilsBarClose();
        mHandler.sendEmptyMessageDelayed(UTILS_BAR_CLOSE, 4000);
    }

    private void removetAutoUtilsBarClose() {
        mHandler.removeMessages(UTILS_BAR_CLOSE);
    }

    /**
     * 下面工具栏上移动动画开启
     */
    private void startDownBarAnUp() {
        if (downBarUpAnSet == null) {
            downBarUpAnSet = new AlphaAnimation(0.0f, 1.0f);
            downBarUpAnSet.setDuration(barAnDuration);
            downBarUpAnSet.setFillAfter(true);
            downBarUpAnSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    videoControllerLayout.clearAnimation();
                    videoControllerLayout.invalidate();
                    isAn = false;
                    startAutoUtilsBarClose();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
        videoControllerLayout.startAnimation(downBarUpAnSet);
    }

    /**
     * 工具栏消失
     */
    private void utilsBarClose() {
        if (videoControllerLayout.getVisibility() == View.GONE) {
            return;
        }
        if (videUpLayout.getVisibility() == View.GONE) {
            return;
        }
        if (!isAn) {
            startDownBarAnDown();
            startUpBarAnUp();

            isAn = true;
        }
    }

    /**
     * 开关工具栏
     *
     * @return
     */
    private void optionUtilsBar() {
        if (videoControllerLayout.getVisibility() == View.VISIBLE) {
            utilsBarClose();
            videoPauseBtn.setVisibility(GONE);
        } else {
            utilsBarShow();
            if (videoView.isPlaying()){
                videoPauseBtn.setImageResource(R.mipmap.video_stop);
            }else {
                videoPauseBtn.setImageResource(R.mipmap.video_start);
            }
            videoPauseBtn.setVisibility(VISIBLE);
        }

    }

    /**
     * 销毁方法
     */
    public void destory() {
        if (videoView != null) {
            videoView.destroyDrawingCache();
            videoView.release(true);
            videoView.stopBackgroundPlay();
            stopDurationTimer();
            videoView.stopPlayback();
            videoView = null;
        }
        stopDurationTimer();
        removeHandler();

    }

    /**
     * 工具栏出现
     */
    private void utilsBarShow() {

        if (!isAn) {
            videoControllerLayout.setVisibility(View.VISIBLE);
            startDownBarAnUp();
            videUpLayout.setVisibility(View.VISIBLE);
            startUpBarAnDown();

            isAn = true;
        }
    }



    /**
     * 设置activity
     *
     * @param activity
     * @param isForceLandscape 是否强制横屏
     */
    public void setActivity(Activity activity, boolean isForceLandscape) {
        mActivity = activity;
        this.isForceLandscape = isForceLandscape;
        if (isForceLandscape) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            if (backView != null) {
                backView.setVisibility(View.VISIBLE);
            }
        } else {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    public void setVideoPlayInterface(VideoPlayInterface videoPlayInterface) {
        this.videoPlayInterface = videoPlayInterface;
    }



    /**
     * 传入参数
     *
     * @param url
     */
    public void setData(String url, int parentHeight) {
        this.playUrl = url;
        mParentHeight = parentHeight;
        start();
        if (!com.chinabrowser.utils.CommUtils.isWifiConnected(getContext())){
            pause();
            ExitDialog dialog = new ExitDialog(getContext(), getContext().getString(R.string.str_nowifi)+"\n"+getContext().getString(R.string.str_use4g), getContext().getString(R.string.setting_login_yes), getContext().getString(R.string.setting_login_no), new ExitDialog.DialogClick() {
                @Override
                public void dialogClick(int which) {
                    if (which==1){
                        resume();
                    }
                }
            });
            dialog.showIt();
        }

    }


    /**
     * 返回按钮逻辑
     */
    public void backLogic() {
        if (isForceLandscape) {
            if (mActivity != null && !mActivity.isFinishing()) {//结束当前Fragment

            }
            return;
        }
        if (isFullScreen) {
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            if (videoPlayInterface != null) {
                videoPlayInterface.backClick();
            }
        }
    }

    /**
     * 其他变量
     **/
    private Timer durationTimer;
    private TimerTask durationTimeTask;
    private boolean isFullScreen;// 是否全屏
    private boolean isAn;// 是否在执行动画
    private VideoPlayInterface videoPlayInterface;
    private AlphaAnimation downBarDownAnSet, downBarUpAnSet, upBarDownAnSet, upBarUpAnSet;
    private final int barAnDuration = 300;

    /**
     * 暂停播放
     */
    public void pause() {
        videoView.pause();
        stopDurationTimer();
        if (videoPlayInterface != null) {
            videoPlayInterface.pause();
        }
    }

    /**
     * 继续播放
     */
    public void resume() {
        startDurationTimer();
        videoView.start();
        if (videoPlayInterface != null) {
            videoPlayInterface.resume();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.viewBox:
                optionUtilsBar();
                break;
            case R.id.video_up_back:
                backLogic();
                break;
            case R.id.share_video:
                if (videoPlayInterface != null) {
                    videoPlayInterface.shareClick();
                }
                break;
            case R.id.videoPauseBtn:
                if (videoView.isPlaying()){
                    videoPauseBtn.setImageResource(R.mipmap.video_start);
                    pause();
                }else {
                    videoPauseBtn.setImageResource(R.mipmap.video_stop);
                    resume();
                }
                break;
            case R.id.screen_status_img:
                setOrientation();
                break;
        }

    }

    /**
     * 设置横竖屏
     */
    private void setOrientation() {
        if (mActivity != null) {
            if (mActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT && mActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED) {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    // 进入全屏时候调用
    public void setFullScreen() {
        isFull = true;
        screenSwitchBtn.setImageResource(R.mipmap.back_nomal);
        this.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        videoView.requestLayout();
        isFullScreen = true;
        startAutoUtilsBarClose();
        nameView.setVisibility(View.VISIBLE);
        utilsBarClose();
        if (videoPlayInterface != null) {
            videoPlayInterface.intoFullScreen();
        }
        if (mActivity != null) {
            WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            mActivity.getWindow().setAttributes(attrs);
            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    // 退出全屏时候调用
    public void setNormalScreen() {
        isFull = false;
        screenSwitchBtn.setImageResource(R.mipmap.full_screen);
        this.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mParentHeight));
        videoView.requestLayout();
        isFullScreen = false;
        removetAutoUtilsBarClose();
        utilsBarClose();
        videoControllerLayout.setVisibility(View.VISIBLE);
        videUpLayout.setVisibility(View.VISIBLE);
        nameView.setVisibility(View.GONE);
        if (videoPlayInterface != null) {
            videoPlayInterface.intoNormalScreen();
        }
        if (mActivity != null) {
            WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
            attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mActivity.getWindow().setAttributes(attrs);
            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && v.getId() != R.id.viewBox) {
            startAutoUtilsBarClose();
        }
        if (v.getId() == R.id.viewBox) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startY = event.getY();
                    startX = event.getX();
                    curVol = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                    WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                    curBright = lp.screenBrightness;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!isFullScreen) {
                        break;
                    }
                    float endY = event.getY();
                    float distanceY = startY - endY;
                    int screenWidth = isFull ? CommUtils.getScreenHeight() : CommUtils.getScreenWidth();
                    int screenHeight = isFull ? CommUtils.getScreenWidth() : CommUtils.getScreenHeight();
                    if (startX > screenWidth / 2) {
                        if (Math.abs(distanceY) > 50) {
                            //屏幕右半部分上滑，声音变大，下滑，声音变小
                            int curvol = (int) (curVol + (distanceY / screenHeight) * maxVolme);
                            int volume = Math.min(Math.max(0, curvol), maxVolme);
//						NewLogUtils.d("videoview", "", "volume="+volume+":maxVolme="+maxVolme);
                            updateAudioVolume(volume);
                        }
                    } else {
                        if (Math.abs(distanceY) > 50) {
                            changeAppBrightness((int) (curBright * 255 + distanceY * 255 / screenHeight), false);
                        }
                    }

                    break;
                case MotionEvent.ACTION_UP:
                    centerLayout.setVisibility(View.GONE);
                    break;
            }
        }

        return super.onTouchEvent(event);
    }

    /****
     * 更新音量
     * @param volume
     */
    public void updateAudioVolume(int volume) {
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
        int percent = volume * 100 / maxVolme;
        centerLayout.setVisibility(View.VISIBLE);
        volume_view.setVisibility(View.VISIBLE);
        brightness_view.setVisibility(View.GONE);
        volume_view.setText("音量:" + percent + "%");
    }
    /**
     * 改变App当前Window亮度
     *
     * @param brightness
     */
    public void changeAppBrightness(int brightness, boolean isFirst) {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        if (brightness == -1) {
            lp.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        } else {
            lp.screenBrightness = (brightness <= 0 ? 1 : brightness) / 255f;
        }
        lp.screenBrightness = Math.max(lp.screenBrightness, 0);
        lp.screenBrightness = Math.min(lp.screenBrightness, 1);
        window.setAttributes(lp);
        int percent = (int) (lp.screenBrightness * 100 / 1);
        centerLayout.setVisibility(View.VISIBLE);
        brightness_view.setVisibility(View.VISIBLE);
        volume_view.setVisibility(View.GONE);
        brightness_view.setText("亮度:" + percent + "%");
        if (isFirst) {
            centerLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 启动进度检测定时器
     */
    private void startDurationTimer() {
        stopDurationTimer();
        if (durationTimer == null) {
            durationTimeTask = new TimerTask() {

                @Override
                public void run() {
                    mHandler.sendEmptyMessage(UPDATE_DURATION);
                }
            };
            durationTimer = new Timer();
            durationTimer.schedule(durationTimeTask, 0, 1000);
        }
    }

    /**
     * 关闭进度检测定时器
     */
    private void stopDurationTimer() {
        if (durationTimer != null) {
            durationTimer.cancel();
            durationTimer = null;
        }
        if (durationTimeTask != null) {
            durationTimeTask.cancel();
            durationTimeTask = null;
        }
    }

    /**
     * 移除handler消息
     */
    private void removeHandler() {
        if (mHandler != null) {
            mHandler.removeMessages(UPDATE_DURATION);
            mHandler.removeMessages(UTILS_BAR_CLOSE);
            mHandler.removeMessages(TEST_REPLAY);
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        int[] time = com.chinabrowser.utils.CommUtils.getMinuteAndSecond(progress);
        videoCurTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        videoView.pause();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        videoView.seekTo(videoSeekBar.getProgress());
        videoView.start();
        startDurationTimer();
        // 拖动之后到指定的时间位置
        if (videoPlayInterface != null) {
            videoPlayInterface.seekStop();
        }
    }

    /**
     * 设置进度条是否能拖拽
     */
    public void setSeekBarDrag() {
        boolean isDrag = true;
            isDrag = true;
        videoSeekBar.setEnabled(isDrag);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (videoView==null){
            return;
        }
        duration = videoView.getDuration();
        int[] time = com.chinabrowser.utils.CommUtils.getMinuteAndSecond(duration);
        videoTotalTimeText.setText(String.format("%02d:%02d", time[0], time[1]));
        formatTotalTime = String.format("%02d:%02d", time[0], time[1]);
        videoSeekBar.setMax(duration);
        progressBar.setVisibility(View.GONE);
        iMediaPlayer.start();
        videoPauseBtn.setEnabled(true);
        setSeekBarDrag();
        startDurationTimer();
        // 初始化总时间等一些界面显示，同时用timer定时去修改时间进度textView

        iMediaPlayer.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

            }

        });

    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        videoView.seekTo(0);
        videoSeekBar.setProgress(0);
        videoPauseBtn.setImageResource(R.mipmap.video_start);
        if (videoPlayInterface != null) {
            videoPlayInterface.playNext();
        }

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        Message msg = new Message();
        msg.what = TEST_REPLAY;
        msg.arg1 = videoView.getCurrentPosition();
        mHandler.sendMessageDelayed(msg, 2000);
        return true;
    }

    /**
     * 开始播放
     */
    public void start() {
        if (!TextUtils.isEmpty(playUrl)) {
            videoPauseBtn.setVisibility(GONE);
            videoControllerLayout.setVisibility(GONE);
            doPlay();
        }
    }

    private void doPlay() {
        stopDurationTimer();
        videoPauseBtn.setEnabled(false);
        videoSeekBar.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> headerMap = new HashMap<String, String>();
//		headerMap.put("User-Agent", "MOBILE");
        headerMap.put("User-Agent", "Android");
        headerMap.put("Accept-Encoding", "gzip,deflate");
        headerMap.put("Accept-Language", "zh-cn");
        headerMap.put("Accept", "*/*");
        videoView.setVideoURI(Uri.parse(playUrl), headerMap);
        videoView.requestFocus();
        videoView.start();
        if (videoPlayInterface != null) {
            videoPlayInterface.seekStop();
        }
    }

    public interface VideoPlayInterface {
        public void backClick();// 返回键监听

        public void shareClick();// 分享接口

        public void tousheClick();//投射接口

        public void intoFullScreen();//进入全屏事件

        public void intoNormalScreen();//进入正常屏幕事件

        public void playNext();//播放下一个事件

        public void pause();//暂停事件 返回当前时间(毫秒)

        public void resume();

        public void seekStop();//拖动停止

        public void startPlay();//开始播放
    }
}
