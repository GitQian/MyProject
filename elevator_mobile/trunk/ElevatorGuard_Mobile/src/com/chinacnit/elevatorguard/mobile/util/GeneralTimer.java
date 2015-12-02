package com.chinacnit.elevatorguard.mobile.util;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * 辅助定时器，通过构造方法创建，传入回调接口，时间间隔和相关ID， 在onTimeEvent()方法中执行相关定时执行逻辑
 * 
 * @author ssu
 * @date 2015-5-12 下午5:33:30
 */
public class GeneralTimer {

	private static final String LOG_TAG = GeneralTimer.class.getSimpleName();

	/** 回调接口 */
	private TimerListener mListener;

	/** 时间间隔（long millis） */
	private int mClockStep;

	private static Handler mHandler;

	/** 是否在运行 */
	private boolean mIsRunning;

	/** 定时方法执行前后回调接口 */
	private TimerOffsetListener mTimerOffsetListener;

	/** 定时方法执行后时间间隔 */
	private long mOffsetAfter;

	/** 定时方法执行前时间间隔 */
	private long mOffsetBefore;

	/**
	 * 返回定时器是否在运行
	 * 
	 * @return true：定时器在运行中 | false：定时器已停止运行
	 */
	public boolean isRunning() {
		return mIsRunning;
	}

	/**
	 * 定时任务之前执行
	 */
	private Runnable mBeforeTask = new Runnable() {
		@Override
		public void run() {
			mTimerOffsetListener.beforeTimer();
		}
	};

	/**
	 * 定时任务之后执行
	 */
	private Runnable mAfterTask = new Runnable() {
		@Override
		public void run() {
			mTimerOffsetListener.afterTimer();
		}
	};

	private Runnable mDelayTask = new Runnable() {
		@Override
		public void run() {
			if (mIsRunning) {
				if (mListener != null) {
					mListener.onTimerEvent();
				}
				if (mTimerOffsetListener != null) {
					mHandler.postDelayed(mBeforeTask, mClockStep
							- mOffsetBefore);
					mHandler.postDelayed(mAfterTask, mClockStep + mOffsetAfter);
				}
				mHandler.postDelayed(this, mClockStep);
			}
		}
	};

	/**
	 * 构造方法，传入定时回调接口，时间间隔，以及回调ID
	 * 
	 * @param listener
	 *            回调接口
	 * @param clockStep
	 *            时间间隔
	 */
	public GeneralTimer(TimerListener listener, int clockStep) {
		this.mListener = listener;
		this.mClockStep = clockStep;
	}

	public static void init() {
		if (mHandler != null) {
			mHandler.getLooper().quit();
			mHandler = null;
		}
		HandlerThread thread = new HandlerThread(
				GeneralTimer.class.getSimpleName());
		thread.start();
		mHandler = new Handler(thread.getLooper());
	}

	/**
	 * 启动定时器
	 */
	public void startTimer() {
		startTimer(true);
	}

	/**
	 * 是否强制启动定时器
	 * 
	 * @param forceTick
	 *            如果为true，先强制执行一次onTimeEvent()方法
	 */
	public void startTimer(boolean forceTick) {
		if (!mIsRunning) {
			mIsRunning = true;
			if (forceTick) {
				mHandler.post(mDelayTask);
			} else {
				mHandler.postDelayed(mDelayTask, mClockStep);
			}
			if (mTimerOffsetListener != null) {
				mHandler.postDelayed(mBeforeTask, mClockStep - mOffsetBefore);
				mHandler.postDelayed(mAfterTask, mClockStep + mOffsetAfter);
			}
		}

	}

	/**
	 * 获取定时器时间间隔
	 * 
	 * return 时间间隔，单位：毫秒
	 */
	public int getClockStep() {
		return mClockStep;
	}

	/**
	 * 设置定时器时间间隔
	 * 
	 * @param clockStep
	 *            时间间隔，单位：毫秒
	 */
	public void setClockStep(int clockStep) {
		this.mClockStep = clockStep;
	}

	/**
	 * 停止定时器
	 */
	public void stopTimer() {
		mIsRunning = false;
		mHandler.removeCallbacks(mDelayTask);
		mHandler.removeCallbacks(mBeforeTask);
		mHandler.removeCallbacks(mAfterTask);
	}

	public void setTimerOffsetListener(TimerOffsetListener listener,
			long before, long after) {
		this.mOffsetBefore = before;
		this.mOffsetAfter = after;
		this.mTimerOffsetListener = listener;
	}

	/**
	 * 定时方法执行前后回调接口
	 * 
	 * @author chenercai
	 * 
	 */
	public interface TimerOffsetListener {

		/**
		 * 定时方法执行前执行
		 */
		public void beforeTimer();

		/**
		 * 定时方法执行后执行
		 */
		public void afterTimer();
	}

	/**
	 * 重置定时器。先停止当前定时器所有操作，然后重启定时器
	 */
	public void resetTimer() {
		mIsRunning = true;

		mHandler.removeCallbacks(mDelayTask);
		mHandler.removeCallbacks(mBeforeTask);
		mHandler.removeCallbacks(mAfterTask);
		// mHandler.postDelayed(mDelayTask, mClockStep);
		mHandler.post(mDelayTask);

		if (mTimerOffsetListener != null) {
			mHandler.postDelayed(mBeforeTask, mClockStep - mOffsetBefore);
			mHandler.postDelayed(mAfterTask, mClockStep + mOffsetAfter);
		}
	}

	/**
	 * 获取Handler
	 * 
	 * @return Timer里的Handler实例
	 */
	public static Handler getHandler() {
		if (mHandler == null) {
			init();
		}
		return mHandler;
	}

	public interface TimerListener {
		/**
		 * This method is called when the listened timer has triggered a clock
		 * event.
		 */
		public void onTimerEvent();
	}
}
