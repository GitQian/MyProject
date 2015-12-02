package com.ztkj.componet;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class LinearKeyBoard extends LinearLayout {
	private onKybdsChangeListener mListener;
	private int height=0;
	
	
	
	public LinearKeyBoard(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * set keyboard state listener
	 */
	public void setOnkbdStateListener(onKybdsChangeListener listener) {
		mListener = listener;
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		if(height==0){
			height=getHeight();
			return ;
		}
		
		if(mListener!=null){
			boolean isShow=false;
			if(h!=height){
				isShow=true;
			}
			mListener.onKeyBoardStateChange(isShow);
		}
	}

	public interface onKybdsChangeListener {
		public void onKeyBoardStateChange(boolean isShow);
	}
}