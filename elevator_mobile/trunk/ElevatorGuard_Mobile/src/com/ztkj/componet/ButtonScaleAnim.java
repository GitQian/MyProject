package com.ztkj.componet;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.chinacnit.elevatorguard.mobile.R;



public class ButtonScaleAnim extends LinearLayout{
	private Animation mScaleBig,mScaleSmall;
	private boolean isEnd=false;
	private boolean canPerform=true;
	private boolean canClick=true;
	
	private Handler handler =new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			canClick=true;
			return false;
		}
	});
	public ButtonScaleAnim(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mScaleBig=AnimationUtils.loadAnimation(context, R.anim.scale_button_zoom);
		mScaleSmall=AnimationUtils.loadAnimation(context, R.anim.scale_button_narrow);
		
		mScaleBig.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if(canPerform){
					canClick=false;
					setPressed(false);
					performClick();
					handler.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							handler.sendEmptyMessage(0);
						}
					}, 1000);
				}
			}
		});
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(!canClick){
			return false;
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isEnd=false;
			canPerform=true;
			startAnimation(mScaleSmall);
			
			break;
		case MotionEvent.ACTION_MOVE:
			float x=event.getX();
			float y=event.getY();
			if((x<0||x>getWidth()||y<0||y>getHeight())&&!isEnd){
				isEnd=true;
				canPerform=false;
				startAnimation(mScaleBig);
			}
			break;
		case MotionEvent.ACTION_UP:
			if(!isEnd){
				isEnd=true;
				canPerform=true;
				setPressed(false);
				startAnimation(mScaleBig);
				return true;
			}
			break;
		default:
			//action_outside
			canPerform=false;
			startAnimation(mScaleBig);
			break;
		}
		return super.onTouchEvent(event);
	}

}
