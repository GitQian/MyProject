package com.ztkj.componet;

import android.content.Context;
import android.util.AttributeSet;
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
 * @description XListView's footer
 */
public class JListViewFooter extends LinearLayout {
	private LinearLayout mContainer;
	private RelativeLayout mFooterViewContent;  //实际内容持有的view。
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;
	private TextView mHintStateTextView,mHintTimeTextView,mHintLoadAll;
	
	private int mState = STATE_NORMAL;

	private Animation mRotateReverseAnim;
	private Animation mRotatePositiveAnim;
	
	private final int ROTATE_ANIM_DURATION = 180;
	public final static int STATE_NORMAL = 0;
	public final static int STATE_READY = 1;
	public final static int STATE_LOADING = 2;

	public JListViewFooter(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public JListViewFooter(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		// 初始情况，设置下拉刷新view高度为0
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
		mContainer = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.jlistview_footer, null);
//		mContainer.setGravity(Gravity.TOP);// 默认就是 Top
		addView(mContainer, lp);
		
		mFooterViewContent = (RelativeLayout) mContainer .findViewById(R.id.xlistview_footer_content);

		mArrowImageView = (ImageView)findViewById(R.id.xlistview_footer_arrow);
		mHintStateTextView = (TextView)findViewById(R.id.xlistview_footer_hint_textview);
		mHintTimeTextView = (TextView) findViewById(R.id.xlistview_footer_time);
		mHintLoadAll = (TextView) findViewById(R.id.xlistview_footer_content_loadall_hint); //显示那句 "全部加载完毕"
		mProgressBar = (ProgressBar)findViewById(R.id.xlistview_footer_progressbar);
		
		
		mRotateReverseAnim =  new RotateAnimation(0.0f, -180.0f,
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
		if (state == mState) return ;
		
		if (state == STATE_LOADING) {	// 显示进度
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.INVISIBLE);
			mProgressBar.setVisibility(View.VISIBLE);
		} else {	// 显示箭头图片
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.INVISIBLE);
		}
		
		switch(state){
		case STATE_NORMAL:
			if (mState == STATE_READY) {
				mArrowImageView.startAnimation(mRotatePositiveAnim);
			}
			if (mState == STATE_LOADING) {
				mArrowImageView.clearAnimation();
			}
			mHintStateTextView.setText("上拉加载更多");
			break;
		case STATE_READY:
			if (mState != STATE_READY) {
				mArrowImageView.clearAnimation();
				mArrowImageView.startAnimation(mRotateReverseAnim);
				mHintStateTextView.setText("松开载入更多");
			}
			break;
		case STATE_LOADING:
			mHintStateTextView.setText("正在加载...");
			break;
			default:
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
		//下面 lp.height为0， mContainer.getHeight() 却是120.
//		Log.e(this.getClass().getSimpleName(), "getVisiableHeight()-----lp.height:"+lp.height+"--mContainer.getHeight():"+mContainer.getHeight());
//		return mContainer.getHeight();  
		return lp.height;
	}

	/**
	 * set last refresh time
	 * @param time 
	 */
	public void setRefreshTime(String time) {
		mHintTimeTextView.setText(time);
	}

	public RelativeLayout getFooterViewContent() {
		return mFooterViewContent;
	}

	public void hide() {
//		this.setVisibility(GONE); //无论你是 Gone还是 invisible， ListView.getLastVisiblePosition 都能得到正确， 不知道这里 visible为何…………意思。
		
		setVisiableHeight(0);
	}
	
	/**chage ui when all data was load over*/
	public void onDataLoadOver(){
		this.setVisibility(View.VISIBLE);
//		setVisiableHeight(height)
		mFooterViewContent.setVisibility(View.GONE);
		mHintLoadAll.setVisibility(View.VISIBLE);
	}
	
	/**chage ui when all data was not load over,in other word, there are more data in server*/
	public void onDataNotLoadOver(){
		mFooterViewContent.setVisibility(View.VISIBLE);
		mHintLoadAll.setVisibility(View.GONE);
	}
	
}
