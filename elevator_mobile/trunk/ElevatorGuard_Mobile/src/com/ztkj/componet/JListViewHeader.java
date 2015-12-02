package com.ztkj.componet;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;

/**
 * @file XListViewHeader.java
 * @create 2015-02-10 16:28:56
 * @author joychine
 * @description XListView's header
 */
public class JListViewHeader extends LinearLayout {
	private LinearLayout mContainer;
	private RelativeLayout mHeaderViewContent;  //实际上持有内容的那个view
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintStateTextView,mHintTimeTextView;
	private int mState = STATE_NORMAL;

	private Animation mRotateReverseAnim;
	private Animation mRotatePositiveAnim;

	private final int ROTATE_ANIM_DURATION = 180;

	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_REFRESHING = 2;

	public JListViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public JListViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
//		this.setClickable(false);
//		this.setEnabled(false);
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//点击无状态变化。。。。必须给个监听。
			}
		});
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.jlistview_header, null);
		mContainer.setGravity(Gravity.BOTTOM); //默认置顶
		addView(mContainer, lp);
		
		mHeaderViewContent = (RelativeLayout) mContainer .findViewById(R.id.xlistview_header_content);

		mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
		mHintStateTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
		mHintTimeTextView = (TextView) findViewById(R.id.xlistview_header_time);
		mProgressBar = (ProgressBar) findViewById(R.id.xlistview_header_progressbar);

		mRotateReverseAnim = new RotateAnimation(0.0f, -180.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateReverseAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotateReverseAnim.setFillAfter(true);
		mRotatePositiveAnim = new RotateAnimation(-180.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotatePositiveAnim.setDuration(ROTATE_ANIM_DURATION);
		mRotatePositiveAnim.setFillAfter(true);
	}

	public void setState(int state) {
		this.setVisibility(View.VISIBLE); //改变状态，无论如何都要显示自己
		if (state == mState)
			return;
		
		if (state == STATE_REFRESHING) {
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotatePositiveAnim);
			}
			if (mState == STATE_REFRESHING) {
				mArrowImageView.clearAnimation();
			}
			mHintStateTextView.setText("下拉刷新");
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateReverseAnim);
				mHintStateTextView.setText("松开刷新数据");
			}
			break;
		case STATE_REFRESHING:
			mHintStateTextView.setText("正在加载...");
			break;
		}
		mState = state;
	}

	public void setVisiableHeight(int height) {
		if (height < 0)
			height = 0;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
		lp.height = height;
		mContainer.setLayoutParams(lp);
	}

	public int getVisiableHeight() {
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mContainer.getLayoutParams();
		return lp.height;
//		return mContainer.getHeight();  得到高度和得到 lp.height不是同一回事，，不一定会相等我擦。。
	}

	/**
	 * set last refresh time
	 * @param time 
	 */
	public void setRefreshTime(String time) {
		mHintTimeTextView.setText(time);
	}

	public RelativeLayout getHeaderViewContent() {
		return mHeaderViewContent;
	}
	
	
}
