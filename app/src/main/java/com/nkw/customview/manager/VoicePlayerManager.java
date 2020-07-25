package com.nkw.customview.manager;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VoicePlayerManager {
    /**
     * int getCurrentPosition();// 得到当前播放位置（ms）
     * int getDuration();// 得到文件的时间（ms）
     * void setLooping(boolean var1);// 设置是否循环播放
     * boolean isLooping();// 是否循环播放
     * boolean isPlaying();// 是否正在播放
     * void pause();// 暂停
     * void prepare();// 同步准备
     * void prepareAsync();// 异步准备
     * void release();// 释放MediaPlayer对象
     * void reset();// 重置MediaPlayer对象
     * void seekTo(int msec);// 指定播放位置（以毫秒为单位）
     * void setDataSource(String path);// 设置播放资源
     * void setScreenOnWhilePlaying(boolean screenOn);// 设置播放的时候一直让屏幕变亮
     * void setWakeMode(Context context, int mode);// 设置唤醒模式
     * void setVolume(float leftVolume, float rightVolume);// 设置音量，参数分别表示左右声道声音大小，取值范围为0~1
     * void start();// 开始播放
     * void stop();// 停止播放
     */
    private static final String TAG = "VoicePlayerManager";
    private static VoicePlayerManager sInstance;
    private MediaPlayer mMediaPlayer;
    private List<String> mNetVoiceSourceList;//网络资源集合
    private int mNowPlayIndex = 0;
    public static int PLAY_STATUS_NORMAL = -1;//初始状态
    public static int PLAY_STATUS_CHANGE_VOICE = 0;//切换语音
    public static int PLAY_STATUS_PLAYING = 1;//正在播放
    public static int PLAY_STATUS_PAUSE = 2;//暂停播放
    public static int PLAY_STATUS_COMPLETE = 3;//播放完成
    public static int PLAY_STATUS_STOP = 4;//重置
    public static int PLAY_STATUS_ERROR = 5;//错误
    public static int PLAY_STATUS_PREPARE_SUCCESS = 6;//准备完成
    //播放初始状态
    private int mPlayStatus = PLAY_STATUS_NORMAL;
    //线程池
    private ScheduledExecutorService mExecutor;
    //获取播放进度任务
    private Runnable mSeekbarPositionUpdateTask;
    //播放器的监听
    private VoicePlayerStatusListener mVoicePlayerStatusListener;
    //声音总时长
    private int mTotalDuration;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = (int) msg.obj;
            if (mVoicePlayerStatusListener != null) {
                int progress = currentPosition * 100 / mTotalDuration;
                mVoicePlayerStatusListener.onPlayPositionChanged(progress);
            }
        }
    };

    private VoicePlayerManager() {

    }

    public static VoicePlayerManager getInstance() {
        if (sInstance == null) {
            synchronized (VoicePlayerManager.class) {
                if (sInstance == null) {
                    sInstance = new VoicePlayerManager();
                }
            }
        }
        return sInstance;
    }

    private void initMediaPlayer() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            //设置播放完成监听
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (mPlayStatus != PLAY_STATUS_ERROR) {
                        mPlayStatus = PLAY_STATUS_COMPLETE;
                        if (mVoicePlayerStatusListener != null) {
                            mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_COMPLETE);
                        }
                    }
                }
            });
            //设置异步准备完成
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    if (mVoicePlayerStatusListener != null) {
                        mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_PREPARE_SUCCESS);
                        //获取音频总时长
                        mTotalDuration = mp.getDuration();
                        mVoicePlayerStatusListener.onPlayTotalDuration(mTotalDuration);
                    }
                    if (mPlayStatus == PLAY_STATUS_CHANGE_VOICE) {
                        mPlayStatus = PLAY_STATUS_PREPARE_SUCCESS;
                        startPlay();
                    } else if (mPlayStatus == PLAY_STATUS_NORMAL) {
                        mPlayStatus = PLAY_STATUS_PREPARE_SUCCESS;
                    }
                }
            });
            //设置网络流媒体缓冲监听
            mMediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    if (mVoicePlayerStatusListener != null && mPlayStatus != PLAY_STATUS_STOP) {
                        mVoicePlayerStatusListener.onPreparedPositionChange(percent);
                    }
                }
            });
            //进度调整完成SeekComplete监听，主要是配合seekTo(int)方法
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    Log.d(TAG, "onSeekComplete");
                    //拖拽处缓冲完成,可以播放
                   /* if (mPlayStatus == PLAY_STATUS_RESET) {
                        //拖拽前是重置
                        loadPlaySource();
                    } else if (mPlayStatus == PLAY_STATUS_PAUSE) {
                        //拖拽前是暂停
                        startPlay();
                    } else if (mPlayStatus == PLAY_STATUS_PLAYING) {
                        //拖拽前就是播放状态
                    }*/
                }
            });
            //设置播放错误监听
            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mPlayStatus = PLAY_STATUS_ERROR;
                    if (mVoicePlayerStatusListener != null) {
                        mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_ERROR);
                    }
                    return false;
                }
            });
        }
    }

    /**
     * 加载资源
     */
    private void loadPlaySource() {
        String playSource = "";
        if (mNetVoiceSourceList != null && mNowPlayIndex < mNetVoiceSourceList.size()) {
            playSource = mNetVoiceSourceList.get(mNowPlayIndex);
        }
        if (TextUtils.isEmpty(playSource)) {
            Log.e(TAG, "播放地址为空");
            return;
        }
        if (mVoicePlayerStatusListener != null) {
            mVoicePlayerStatusListener.onPlayName(mNetVoiceSourceList.get(mNowPlayIndex));
            mVoicePlayerStatusListener.onPlayPositionChanged(0);
            mVoicePlayerStatusListener.onPreparedPositionChange(0);
            mVoicePlayerStatusListener.onPlayTotalDuration(0);
        }
        initMediaPlayer();
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.setDataSource(playSource);
                mMediaPlayer.prepareAsync();
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 开始播放
     */
    public void startPlay() {
        if (mMediaPlayer != null &&
                (mPlayStatus == PLAY_STATUS_PREPARE_SUCCESS || mPlayStatus == PLAY_STATUS_PAUSE || mPlayStatus == PLAY_STATUS_COMPLETE)) {
            //老的状态为准备成功或者暂停时,或播放完毕时
            mMediaPlayer.start();
            mPlayStatus = PLAY_STATUS_PLAYING;
            if (mVoicePlayerStatusListener != null) {
                mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_PLAYING);
            }
        }
        if (mMediaPlayer != null && mPlayStatus == PLAY_STATUS_STOP) {
            mPlayStatus = PLAY_STATUS_CHANGE_VOICE;
            if (mVoicePlayerStatusListener != null) {
                mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_CHANGE_VOICE);
            }
            //老状态为停止时
            mMediaPlayer.prepareAsync();
        }
    }

    /**
     * 暂停
     */
    public void pausePlay() {
        if (mMediaPlayer != null && mPlayStatus == PLAY_STATUS_PLAYING) {
            mMediaPlayer.pause();
            mPlayStatus = PLAY_STATUS_PAUSE;
            if (mVoicePlayerStatusListener != null) {
                mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_PAUSE);
            }
        }
    }

    /**
     * 重置
     */
    public void stopPlay() {
        if (mMediaPlayer != null && (mPlayStatus == PLAY_STATUS_PLAYING || mPlayStatus ==
                PLAY_STATUS_PAUSE || mPlayStatus == PLAY_STATUS_COMPLETE)) {
            mMediaPlayer.stop();
            mPlayStatus = PLAY_STATUS_STOP;
            if (mVoicePlayerStatusListener != null) {
                mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_STOP);
                mVoicePlayerStatusListener.onPlayPositionChanged(0);
                mVoicePlayerStatusListener.onPreparedPositionChange(0);
            }
        }
    }

    /**
     * 释放资源
     */
    public void releasePlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            stopUpdatingCallbackWithPosition();
            mHandler.removeCallbacksAndMessages(null);
            mPlayStatus = PLAY_STATUS_NORMAL;
            if (mVoicePlayerStatusListener != null) {
                mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_NORMAL);
            }
        }
    }

    /**
     * 拖拽到指定位置
     */
    public void seekToPlay(int position) {
        if (mMediaPlayer != null &&
                (mPlayStatus == PLAY_STATUS_PLAYING || mPlayStatus == PLAY_STATUS_PAUSE
                        || mPlayStatus == PLAY_STATUS_COMPLETE)) {
            int nowMsec = mTotalDuration * position / 100;
            mMediaPlayer.seekTo(nowMsec);
        }
    }

    /**
     * 开启线程，获取当前播放的进度
     */
    private void startUpdatingCallbackWithPosition() {
        if (mExecutor == null) {
            mExecutor = Executors.newSingleThreadScheduledExecutor();
        }
        if (mSeekbarPositionUpdateTask == null) {
            mSeekbarPositionUpdateTask = new Runnable() {
                @Override
                public void run() {
                    updateProgressCallbackTask();
                }
            };
        }
        mExecutor.scheduleAtFixedRate(
                mSeekbarPositionUpdateTask,
                0,
                500,
                TimeUnit.MILLISECONDS
        );
    }

    /**
     * 更新当前的进度
     **/
    private void updateProgressCallbackTask() {
        if (mMediaPlayer != null && mPlayStatus == PLAY_STATUS_PLAYING) {
            try {
                int currentPosition = mMediaPlayer.getCurrentPosition();
                mHandler.obtainMessage(10, currentPosition).sendToTarget();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * 播放完成回调监听
     **/
    private void stopUpdatingCallbackWithPosition() {
        if (mExecutor != null) {
            mExecutor.shutdownNow();
            mExecutor = null;
            mSeekbarPositionUpdateTask = null;
        }
    }

    // 下一首
    public void nextPlay() {
        mNowPlayIndex++;
        if (mNowPlayIndex >= mNetVoiceSourceList.size()) {
            mNowPlayIndex = 0;
        }
        mPlayStatus = PLAY_STATUS_CHANGE_VOICE;
        if (mVoicePlayerStatusListener != null) {
            mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_CHANGE_VOICE);
        }
        loadPlaySource();
    }

    // 上一首
    public void prePlay() {
        mNowPlayIndex--;
        if (mNowPlayIndex < 0) {
            mNowPlayIndex = mNetVoiceSourceList.size() - 1;
        }
        mPlayStatus = PLAY_STATUS_CHANGE_VOICE;
        if (mVoicePlayerStatusListener != null) {
            mVoicePlayerStatusListener.onStatusChange(PLAY_STATUS_CHANGE_VOICE);
        }
        loadPlaySource();
    }

    /**
     * 设置播放源
     */
    public void setPlayNetVoiceSource(List<String> netVoiceSourceList) {
        mNetVoiceSourceList = netVoiceSourceList;
        startUpdatingCallbackWithPosition();
        loadPlaySource();
    }

    /**
     * 设置监听
     *
     * @param listener
     */
    public void setVoicePlayerStatusListener(VoicePlayerStatusListener listener) {
        mVoicePlayerStatusListener = listener;
    }

    public interface VoicePlayerStatusListener {
        /**
         * 状态监听
         *
         * @param status
         */
        void onStatusChange(int status);

        /**
         * 播放进度监听
         *
         * @param position
         */
        void onPlayPositionChanged(int position);

        /**
         * 缓冲进度
         *
         * @param position
         */
        void onPreparedPositionChange(int position);

        /**
         * 音频总时长
         *
         * @param duration
         */
        void onPlayTotalDuration(int duration);

        /**
         * 播放的语音名称
         *
         * @param name
         */
        void onPlayName(String name);

    }
}
