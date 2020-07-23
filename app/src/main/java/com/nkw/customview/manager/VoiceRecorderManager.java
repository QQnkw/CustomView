package com.nkw.customview.manager;

import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;

public class VoiceRecorderManager {
    private final String TAG = "VoiceRecorderManager";

    private static VoiceRecorderManager sVoiceRecorderManager;
    //语音文件夹
    private String mVoiceFileDirPath = "";
    //语音文件
    private String mVoiceFilePath = "";
    //是否在录音
    private boolean isRecording = false;
    //分贝
    private final int CODE_DECIBEL = 0;
    //计时
    private final int CODE_TIME = 1;
    private long mStartTime = 0L;
    private MediaRecorder mMediaRecorder;
    private RecorderSateListener mRecorderSateListener;
    private int mCountTime = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mRecorderSateListener == null) {
                return;
            }
            switch (msg.what) {
                case CODE_DECIBEL:
                    float value = (float) msg.obj;
                    mRecorderSateListener.decibelPercentageListener(value);
                    break;
                case CODE_TIME:
                    mCountTime++;
                    mRecorderSateListener.recordTime(mCountTime);
                    sendEmptyMessageDelayed(CODE_TIME, 1000);
                    break;
            }
        }
    };

    private VoiceRecorderManager() {
    }

    public static VoiceRecorderManager getInstance() {
        if (sVoiceRecorderManager == null) {
            synchronized (VoiceRecorderManager.class) {
                if (sVoiceRecorderManager == null) {
                    sVoiceRecorderManager = new VoiceRecorderManager();
                }
            }
        }
        return sVoiceRecorderManager;
    }

    /**
     * 开始录制
     */
    public void startRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }
        try {
            mMediaRecorder = new MediaRecorder();
            //设置音频源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            //设置输出文件格式
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            //设置音频编码器
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            //设置输出文件名
            if (mVoiceFileDirPath.isEmpty()) {
                throw new Exception("语音文件夹路径不能为空");
            }
            mStartTime = System.currentTimeMillis();
            mVoiceFilePath = mVoiceFileDirPath + File.separator + mStartTime + ".amr";
            mMediaRecorder.setOutputFile(mVoiceFilePath);
            /*可以用默认,不用设置START*/
            //设置录制的音频通道数
            mMediaRecorder.setAudioChannels(1);
            //设置录制的音频采样率
            mMediaRecorder.setAudioSamplingRate(8000);
            //设置录制的音频编码比特率
            mMediaRecorder.setAudioEncodingBitRate(64);
            /*可以用默认,不用设置END*/
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            if (mRecorderSateListener != null) {
                mRecorderSateListener.onStart();
            }
            //录音时长
            mCountTime = 0;
            mHandler.sendEmptyMessageDelayed(CODE_TIME,1000);
            //线程控制器
            isRecording = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (isRecording) {
                            //获得录音的音量，范围 0-32767, 归一化到0 ~ 1
                            float percentage = mMediaRecorder.getMaxAmplitude() * 1.5f / 32767;
                            Message dbMessage = new Message();
                            dbMessage.what = CODE_DECIBEL;
                            dbMessage.obj = percentage;
                            mHandler.sendMessage(dbMessage);
                            SystemClock.sleep(100);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }
                }
            }).start();
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
    }

    /**
     * 取消录音
     */
    public void cancelRecorder() {
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
                File file = new File(mVoiceFilePath);
                if (file.exists()) {
                    file.delete();
                }
                if (mRecorderSateListener != null) {
                    mRecorderSateListener.onCancel();
                }
            }
        } catch (IllegalStateException e) {
            Log.d(TAG, e.toString());
        }
        isRecording = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 停止录音
     */
    public void stopRecorder() {
        try {
            if (mMediaRecorder != null) {
                mMediaRecorder.stop();
                mMediaRecorder.release();
                mMediaRecorder = null;
                File file = new File(mVoiceFilePath);
                if (file.exists() && file.length() == 0) {
                    file.delete();
                }
                if (mRecorderSateListener != null) {
                    mRecorderSateListener.onStop();
                    if (mCountTime < 3) {
                        if (file.exists()) {
                            file.delete();
                        }
                        mRecorderSateListener.onVoiceTimeTooShort();
                    }
                }
            }
        } catch (IllegalStateException e) {
            Log.d(TAG, e.toString());
        }
        isRecording = false;
        mHandler.removeCallbacksAndMessages(null);
    }

    /**
     * 设置录音监听
     *
     * @param listener
     */
    public void setRecorderSateListener(RecorderSateListener listener) {
        mRecorderSateListener = listener;
    }

    /**
     * 设置录音文件的路径
     */
    public void setVoiceFileDirPath(String voiceFileDirPath) {
        mVoiceFileDirPath = voiceFileDirPath;
    }

    public interface RecorderSateListener {
        void onStart();

        void onStop();

        void onCancel();

        void decibelPercentageListener(float value);

        void recordTime(long countTime);

        void onVoiceTimeTooShort();
    }
}
