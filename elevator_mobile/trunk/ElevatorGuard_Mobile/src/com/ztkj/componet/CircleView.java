package com.ztkj.componet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.chinacnit.elevatorguard.mobile.R;


public class CircleView extends View {

	private Bitmap bm;
	private int drawableid = R.drawable.logo;
	private int stokewidth = (int) TypedValue
			.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
					.getDisplayMetrics());

	private float width; // 宽度
	private float height; // height

	private Paint paint, trackPaint;
	private RectF area; // 圆大小
	private int progress = 0; // 进度值
	private Shader mShader; // 渐变着色器
	
	private Matrix mMatrix = new Matrix();
	private float mRotate;

	public int getProgress() {
		return progress;
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			Log.e("=====", "progress:" + progress);
			progress += 1;
			if (progress >= 101) {
				progress = 0;
			}
			
			setProgress(progress);
		};
	};


	public CircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a  = context.obtainStyledAttributes(attrs, R.styleable.circleview);

		drawableid = a.getResourceId(R.styleable.circleview_drawable,R.drawable.ic_launcher);
		stokewidth = a.getInt(R.styleable.circleview_stokewidth, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));

//		Log.e("===", "`````stokewidth:"+stokewidth);
		a.recycle();
		init();
	}
	
	public CircleView(Context context) {
		super(context);
		init();
	}

	public void setProgress(int value) {
		this.progress = value;
		invalidate();
	}

	public void init() {
		height = width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				50, getResources().getDisplayMetrics());

		paint = new Paint();
		paint.setStrokeWidth(stokewidth);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		
		trackPaint = new Paint();
		trackPaint.setStrokeWidth(stokewidth);
		trackPaint.setStyle(Style.STROKE);
		trackPaint.setColor(Color.GRAY);
		trackPaint.setAntiAlias(true);


	}
	
	/**
	 * 创建一个 原始比例的 图片(fit_center的效果)， 铺满视图，比例不变，与ImageView默认显示scaletype一致。
	 * @param bitmap
	 * @param dstWidth  目标视图的宽度
	 * @param dstHeight 目标视图的高度
	 * @return
	 */
	public static Bitmap createScaledBitmap(Bitmap bitmap,int dstWidth, int dstHeight) {

		Rect srcRect = new Rect(0, 0, bitmap.getWidth(),bitmap.getHeight());
		Rect dstRect = calculateDstRect(bitmap.getWidth(),bitmap.getHeight(), dstWidth, dstHeight);
		Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(),dstRect.height(), Config.ARGB_8888);
		// //mmmmmmmm.println("scaledBitmap:"+scaledBitmap);
		Canvas canvas = new Canvas(scaledBitmap);
		canvas.drawBitmap(bitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));
		
		return scaledBitmap;

	}

	private static Rect calculateDstRect(int srcWidth, int srcHeight,int dstWidth, int dstHeight) {

		final float srcAspect = (float) srcWidth / (float) srcHeight;
		final float dstAspect = (float) dstWidth / (float) dstHeight;
		if (srcAspect > dstAspect) {
			return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
		} else {
			return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (MeasureSpec.EXACTLY != MeasureSpec.getMode(widthMeasureSpec)) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec((int) width,
					MeasureSpec.EXACTLY);
			heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) height,
					MeasureSpec.EXACTLY);
		} else {
			width = MeasureSpec.getSize(widthMeasureSpec);
			height = MeasureSpec.getSize(heightMeasureSpec);
		}
		
		if(bm == null){
			bm = BitmapFactory.decodeResource(getResources(), drawableid);
			Options option = new Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(getResources(), drawableid, option);
			int bmHeight = option.outHeight;
			int bmWidth = option.outHeight;
			
			int value = bmWidth*bmWidth + bmHeight*bmHeight;
			if(Math.sqrt(value)/2 > width/2f){//图片的 对角线的一半 大于举行的一半的话。
				float factor = value/(width*width);
				bm=	createScaledBitmap(bm, (int)(bmWidth/Math.sqrt(factor)) - stokewidth, (int)(bmHeight/Math.sqrt(factor))- stokewidth);
			}
		}

		if (area == null)
			area = new RectF(stokewidth / 2+5, stokewidth / 2+5, width - stokewidth	/ 2-5, height - stokewidth / 2-5);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		if(mShader == null){
//			mShader = new SweepGradient(width/2f, height/2f, new int[] { 0x66378300, 0xFF378300 }, new float[] { 0.7f, 0.7f });
//			mShader = new SweepGradient(width/2f, height/2f,new int[] {Color.LTGRAY, 0xff5CC1F3,0xff0090D9,0xffffffff } ,null);
			mShader = new SweepGradient(width/2f, height/2f,new int[] {Color.LTGRAY,Color.LTGRAY,Color.LTGRAY,Color.LTGRAY,0xff0e97e6 } ,null);
		}
		
		mMatrix.setRotate(mRotate, width/2f, height/2f);
		mShader.setLocalMatrix(mMatrix);
		mRotate += 10;
		if (mRotate >= 360) {
			mRotate = 0;
		}		
		
		paint.setShader(mShader);
//		canvas.drawColor(Color.TRANSPARENT);
//		canvas.drawColor(Color.TRANSPARENT,Mode.CLEAR);  //这个要脱层皮。。。去掉全部底色。
//		canvas.drawCircle(width/2, height/2, width/2-10, trackPaint); //画不准位置的。
		canvas.drawArc(area, 360, 360, false,trackPaint);
		canvas.drawArc(area, 360 * progress / 100, 360, false, paint);
		
//		canvas.drawCircle(width/2f, height/2f, width/2f, paint);
		
		//画图logo
		int picLeft = (int) (width / 2 - bm.getWidth() / 2);
		int picTop = (int) ((height - bm.getHeight()) / 2);
		canvas.drawBitmap(bm, picLeft, picTop, paint);
		// canvas.drawText(value + "%", 250, 250, textpaint);  //画文字，进度值。
		mHandler.removeCallbacksAndMessages(null);
		mHandler.sendEmptyMessageDelayed(0x0001, 17);
	}
	@Override
	protected void onDetachedFromWindow() {
		// TODO Auto-generated method stub
		mHandler.removeCallbacksAndMessages(null);
		super.onDetachedFromWindow();
	}
}