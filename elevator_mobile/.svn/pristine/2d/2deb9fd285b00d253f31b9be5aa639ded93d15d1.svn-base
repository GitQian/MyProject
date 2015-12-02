package com.chinacnit.elevatorguard.mobile.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义进度加载框
 * @author ssu 
 * @date 2015-5-14 下午5:06:36
 */
public class LoadingView extends View {

	private Matrix mFgMatrix;
	private Bitmap mFgBitmap;

	public LoadingView(Context context) {
		// TODO Auto-generated constructor stub
		super(context);

	}

	public LoadingView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);

	}

	public void setDrawableResId(int iconResId) {
		mFgMatrix = new Matrix();
		mFgBitmap = BitmapFactory.decodeResource(getResources(), iconResId);
		myHandler.sendEmptyMessage(0);
		measure(mFgBitmap.getWidth(), mFgBitmap.getHeight());
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mFgBitmap.getWidth(), mFgBitmap.getHeight());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawBitmap(mFgBitmap, mFgMatrix, null);
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			mFgMatrix.postRotate(10f, mFgBitmap.getWidth() / 2f, mFgBitmap.getHeight() / 2f);
			invalidate();// 更新视图
			myHandler.sendEmptyMessageDelayed(msg.what, 20);
		};
	};

}
