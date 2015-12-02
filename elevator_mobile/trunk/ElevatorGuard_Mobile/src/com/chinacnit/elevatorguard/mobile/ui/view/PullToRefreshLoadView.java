package com.chinacnit.elevatorguard.mobile.ui.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;

/**
 * 
 * @ClassName: PullToRefreshLoadView
 * @Description: TODO(下拉刷新上拉加载自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（
 *               可以是实现Pullable接口的的任何View），还有一个是上拉头)
 * @author ymchen
 * @date 2015年5月12日 下午6:44:47
 * 
 */
public class PullToRefreshLoadView extends LinearLayout {
	public static final String TAG = PullToRefreshLoadView.class.getSimpleName();
	// 初始状态
	public static final int INIT = 0;
	// 释放刷新
	public static final int RELEASE_TO_REFRESH = 1;
	// 正在刷新
	public static final int REFRESHING = 2;
	// 释放加载
	public static final int RELEASE_TO_LOAD = 3;
	// 正在加载
	public static final int LOADING = 4;
	// 操作完毕
	public static final int DONE = 5;
	// 刷新操作
	public static final int ACTION_REFRESH = 6;
	// 加载操作
	public static final int ACTION_LOAD_MORE = 7;
	// 加载滚动结束操作
	public static final int ACTION_LOAD_SCROLL_FINISH = 8;
	// 刷新滚动结束操作
	public static final int ACTION_REFRESH_SCROLL_FINISH = 9;
	// 当前状态
	private int state = INIT;
	// 刷新回调接口
	private OnRefreshLoadListener mListener;
	// 刷新成功
	public static final int SUCCEED = 0;
	// 刷新失败
	public static final int FAIL = 1;
	// 按下Y坐标，上一个事件点Y坐标
	private float downY, lastY;
	// 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
	public float pullDownY = 0;
	// 上拉的距离
	private float pullUpY = 0;
	// 释放刷新的距离
	private float refreshDistance;
	// 释放加载的距离
	private float loadmoreDistance;
	// 定时器
	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 8;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 2;
	// 下拉箭头的转180°动画
	private RotateAnimation rotateAnimation;
	// 均匀旋转动画
	private RotateAnimation refreshingAnimation;
	// 下拉头
	private View refreshView;
	// 下拉的箭头
	private ImageView pullDownArrowImg;
	// 正在刷新的图标
	private ImageView refreshingImg;
	// 刷新结果图标
	private ImageView refreshStateImg;
	// 刷新状态
	private TextView refreshStateTv;
	// 刷新结果：成功或失败
	private TextView refreshResultTv;
	// 上次更新时间
	private TextView updateTimeTv;
	// 上拉头
	private View loadmoreView;
	// 上拉的箭头
	private ImageView pullUpArrowImg;
	// 正在加载的图标
	private ImageView loadingImg;
	// 加载结果图标
	private ImageView loadStateImg;
	// 加载结果：成功或失败
	private TextView loadStateTv;
	// 实现了Pullable接口的View
	private View pullableView;
	// 过滤多点触碰
	private int mEvents;
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;
	private Context mContext;
	// 下拉头高度
	private int headerHeight;
	// 上拉头高度
	private int footerHeight;

	/**
	 * 执行自动回滚的handler
	 */
	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2 / getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch) {
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == REFRESHING && pullDownY <= refreshDistance) {
					pullDownY = refreshDistance;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadmoreDistance) {
					pullUpY = -loadmoreDistance;
					timer.cancel();
				}
			}
			if (pullDownY > 0) {
				pullDownY -= MOVE_SPEED;
			} else if (pullUpY < 0) {
				pullUpY += MOVE_SPEED;
			}
			if (pullDownY < 0) {
				// 已完成回弹
				pullDownY = 0;
				pullDownArrowImg.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING) {
					changeState(INIT);
				}
				timer.cancel();
				// 回滚完成时进行操作，防止还未回滚完
				requestLayout();
			}
			if (pullUpY > 0) {
				// 已完成回弹
				pullUpY = 0;
				pullUpArrowImg.clearAnimation();
				// 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING) {
					changeState(INIT);
				}
				timer.cancel();
				// 回滚完成时进行操作，防止还未回滚完
				if (mListener != null) {
					mListener.onLoadScrollFinish(PullToRefreshLoadView.this, ACTION_LOAD_SCROLL_FINISH);
				}
				requestLayout();
			}
			// 刷新布局,会自动调用onLayout
			requestLayout();
			// 没有拖拉或者回弹完成
			if (pullDownY + Math.abs(pullUpY) == 0) {
				timer.cancel();
			}
		}
	};

	/**
	 * 
	 * @Title: setOnRefreshLoadListener
	 * @Description: TODO(设置刷新加载监听)
	 * @param @param listener 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setOnRefreshLoadListener(OnRefreshLoadListener listener) {
		mListener = listener;
	}

	public PullToRefreshLoadView(Context context) {
		super(context);
		initView(context);
	}

	public PullToRefreshLoadView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	@SuppressLint("NewApi")
	public PullToRefreshLoadView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 
	 * @Title: initView
	 * @Description: TODO(初始化)
	 * @param @param context 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void initView(Context context) {
		mContext = context;
		timer = new MyTimer(updateHandler);
		// 180度箭头旋转动画
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.reverse_anim);
		// 刷新动画
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.rotating);
		// 添加匀速转动动画，线性变化
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	/**
	 * 
	 * @Title: setRefreshLoadView
	 * @Description: TODO(设置刷新加载视图)
	 * @param @param context
	 * @param @param view 内容视图
	 * @param @param isSwitchView 是否切换视图
	 * @return void 返回类型
	 * @throws
	 */
	/*
	 * public void setRefreshLoadView(Context context, View view, boolean
	 * isSwitchView) { //移除所有视图 this.removeAllViews(); //内容视图 pullableView =
	 * view; setHeaderView(context); setContentView(view); if (!isSwitchView) {
	 * setFooterView(context); }
	 * 
	 * 
	 * }
	 */
	public void setRefreshLoadView(Context context, View view) {
		// 移除所有视图
		this.removeAllViews();
		// 内容视图
		pullableView = view;
		setHeaderView(context);
		setContentView(view);
		setFooterView(context);
	}

	/**
	 * 
	 * @Title: setFooterView
	 * @Description: TODO(设置上拉头视图)
	 * @param @param context 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void setFooterView(Context context) {
		// TODO Auto-generated method stub
		loadmoreView = LayoutInflater.from(context).inflate(R.layout.load_more, this, false);
		pullUpArrowImg = (ImageView) loadmoreView.findViewById(R.id.pullup_icon);
		loadStateTv = (TextView) loadmoreView.findViewById(R.id.loadstate_tv);
		loadingImg = (ImageView) loadmoreView.findViewById(R.id.loading_icon);
		loadStateImg = (ImageView) loadmoreView.findViewById(R.id.loadstate_iv);
		footerHeight = dip2px(context, 80);
		loadmoreDistance = footerHeight;
		LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, footerHeight);
		addView(loadmoreView, lp);
	}

	/**
	 * 
	 * @Title: setContentView
	 * @Description: TODO(设置内容视图)
	 * @param @param view 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setContentView(View view) {
		// TODO Auto-generated method stub
		LayoutParams contnteParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(view, contnteParams);
	}

	/**
	 * 
	 * @Title: setHeaderView
	 * @Description: TODO(设置下拉头视图)
	 * @param @param context 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setHeaderView(Context context) {
		// TODO Auto-generated method stub
		// 初始化下拉布局
		refreshView = LayoutInflater.from(context).inflate(
				R.layout.refresh_head, this, false); 
		pullDownArrowImg = (ImageView) refreshView.findViewById(R.id.pull_icon); 
		refreshStateImg = (ImageView) refreshView
				.findViewById(R.id.refresh_result_iv);
		refreshStateTv = (TextView) refreshView.findViewById(R.id.state_tv);
		refreshingImg = (ImageView) refreshView
				.findViewById(R.id.refreshing_icon);
		refreshResultTv = (TextView) refreshView
				.findViewById(R.id.refresh_result_tv);
		// refreshStateImg = (ImageView)
		// refreshView.findViewById(R.id.state_iv);
		updateTimeTv = (TextView) refreshView.findViewById(R.id.update_time_tv);
		updateHeaderTimeStamp();
		headerHeight = dip2px(context, 80);
		refreshDistance = headerHeight;
		LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, headerHeight);
		lp.topMargin = -headerHeight;
		addView(refreshView, lp);
	}

	/**
	 * 
	 * @Title: updateHeaderTimeStamp
	 * @Description: TODO(更新刷新时间)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void updateHeaderTimeStamp() {
		// 设置更新时间
		SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat
				.getInstance();
		sdf.applyPattern("MM-dd HH:mm");
		updateTimeTv.setText(mContext
				.getString(R.string.pull_to_refresh_update_time_label)
				+ sdf.format(new Date()));
	}

	/**
	 * 
	 * @Title: dip2px
	 * @Description: TODO(根据分辨率，将dip转化为px)
	 * @param @param context
	 * @param @param dpValue dip值
	 * @return int 头部视图高度
	 * @throws
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 
	 * @Title: hide
	 * @Description: TODO(执行上拉头和下拉头的隐藏工作)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void hide() {
		timer.schedule(5);
	}

	/**
	 * 
	 * @Title: refreshFinish
	 * @Description: TODO(完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法)
	 * @param @param refreshResult
	 *        PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 * @return void 返回类型
	 * @throws
	 */
	public void refreshFinish(int refreshResult) {
		refreshingImg.clearAnimation();
		refreshingImg.setVisibility(View.GONE);
		refreshStateTv.setVisibility(View.GONE);
		updateTimeTv.setVisibility(View.GONE);
		switch (refreshResult) {
		case SUCCEED:
			// 刷新成功
			refreshResultTv.setVisibility(View.VISIBLE);
			refreshStateImg.setVisibility(View.VISIBLE);
			refreshResultTv.setText(R.string.refresh_succeed);
			refreshStateImg.setImageResource(R.drawable.refresh_succeed);
			break;
		case FAIL:
		default:
			// 刷新失败
			refreshResultTv.setVisibility(View.VISIBLE);
			refreshStateImg.setVisibility(View.VISIBLE);
			refreshResultTv.setText(R.string.refresh_fail);
			refreshStateImg.setImageResource(R.drawable.refresh_failed);
			break;
		}
		if (pullDownY > 0) {
			// 刷新结果停留1秒
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					changeState(DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 1000);
		} else {
			changeState(DONE);
			hide();
		}
	}

	/**
	 * 
	 * @Title: loadmoreFinish
	 * @Description: TODO(加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法)
	 * @param @param refreshResult
	 *        PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 * @return void 返回类型
	 * @throws
	 */
	public void loadmoreFinish(int refreshResult) {
		loadingImg.clearAnimation();
		loadingImg.setVisibility(View.GONE);
		switch (refreshResult) {
		case SUCCEED:
			// 加载成功
			loadStateImg.setVisibility(View.VISIBLE);
			loadStateTv.setText(R.string.load_succeed);
			loadStateImg.setBackgroundResource(R.drawable.refresh_succeed);
			break;
		case FAIL:
		default:
			// 加载失败
			loadStateImg.setVisibility(View.VISIBLE);
			loadStateTv.setText(R.string.load_fail);
			loadStateImg.setBackgroundResource(R.drawable.refresh_failed);
			break;
		}
		if (pullUpY < 0) {
			// 刷新结果停留1秒
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					changeState(DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 1000);
		} else {
			changeState(DONE);
			hide();
		}
	}

	/**
	 * 
	 * @Title: changeState
	 * @Description: TODO(改变当前上拉头和下拉头的状态)
	 * @param @param to 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void changeState(int to) {
		state = to;
		switch (state) {
		case INIT:
			// 下拉布局初始状态
			refreshStateTv.setVisibility(View.VISIBLE);
			updateTimeTv.setVisibility(View.VISIBLE);
			refreshResultTv.setVisibility(View.GONE);
			refreshStateImg.setVisibility(View.GONE);
			// 更新时间
			updateHeaderTimeStamp();
			refreshStateTv.setText(R.string.pull_to_refresh_pull_label);
			pullDownArrowImg.clearAnimation();
			pullDownArrowImg.setVisibility(View.VISIBLE);
			// 上拉布局初始状态
			loadStateImg.setVisibility(View.GONE);
			loadStateTv.setText(R.string.pull_to_load_pull_label);
			pullUpArrowImg.clearAnimation();
			pullUpArrowImg.setVisibility(View.VISIBLE);
			break;
		case RELEASE_TO_REFRESH:
			// 释放刷新状态
			refreshStateTv.setText(R.string.pull_to_refresh_release_label);
			pullDownArrowImg.startAnimation(rotateAnimation);
			break;
		case REFRESHING:
			// 正在刷新状态
			pullDownArrowImg.clearAnimation();
			refreshingImg.setVisibility(View.VISIBLE);
			pullDownArrowImg.setVisibility(View.INVISIBLE);
			refreshingImg.startAnimation(refreshingAnimation);
			refreshStateTv.setText(R.string.pull_to_refresh_refreshing_label);
			break;
		case RELEASE_TO_LOAD:
			// 释放加载状态
			loadStateTv.setText(R.string.pull_to_load_release_label);
			pullUpArrowImg.startAnimation(rotateAnimation);
			break;
		case LOADING:
			// 正在加载状态
			pullUpArrowImg.clearAnimation();
			loadingImg.setVisibility(View.VISIBLE);
			pullUpArrowImg.setVisibility(View.INVISIBLE);
			loadingImg.startAnimation(refreshingAnimation);
			loadStateTv.setText(R.string.pull_to_load_loading_label);
			break;
		case DONE:
			// 刷新或加载完毕，啥都不做
			break;
		}
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}

	/*
	 * （非 Javadoc）由父控件决定是否分发事件，防止事件冲突
	 * 
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			// 当前状态为正在刷新或者正在加载时，不处理事件,交给父类处理
			if (state == REFRESHING || state == LOADING) {
				return true;
			}
			if (mEvents == 0) {
				if (pullDownY > 0 || (((Pullable) pullableView).canPullDown()
								&& canPullDown && state != LOADING)) {
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0) {
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight())
						pullDownY = getMeasuredHeight();
					if (state == REFRESHING) {
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
				} else if (pullUpY < 0
						|| (((Pullable) pullableView).canPullUp() && canPullUp && state != REFRESHING)) {
					// 可以上拉，正在刷新时不能上拉
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0) {
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight())
						pullUpY = -getMeasuredHeight();
					if (state == LOADING) {
						// 正在加载的时候触摸移动
						isTouch = true;
					}
				} else
					releasePull();
			} else
				mEvents = 0;
			lastY = ev.getY();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			if (pullDownY > 0 || pullUpY < 0)
				requestLayout();
			if (pullDownY > 0) {
				if (pullDownY <= refreshDistance
						&& (state == RELEASE_TO_REFRESH || state == DONE)) {
					// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
					changeState(INIT);
				}
				if (pullDownY >= refreshDistance && state == INIT) {
					// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
					changeState(RELEASE_TO_REFRESH);
				}
			} else if (pullUpY < 0) {
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpY <= loadmoreDistance
						&& (state == RELEASE_TO_LOAD || state == DONE)) {
					changeState(INIT);
				}
				// 上拉操作
				if (-pullUpY >= loadmoreDistance && state == INIT) {
					changeState(RELEASE_TO_LOAD);
				}
			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownY + Math.abs(pullUpY)) > 8) {
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			// 当前状态为正在刷新或者正在加载时，不处理事件，交给父类处理
			if (state == REFRESHING || state == LOADING) {
				return super.dispatchTouchEvent(ev);
			}
			if (pullDownY > refreshDistance || -pullUpY > loadmoreDistance)
			// 正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
			{
				isTouch = false;
			}
			if (state == RELEASE_TO_REFRESH) {
				changeState(REFRESHING);
				// 刷新操作
				if (mListener != null)
					mListener.onRefresh(this, ACTION_REFRESH);
			} else if (state == RELEASE_TO_LOAD && loadmoreView != null) {
				changeState(LOADING);
				// 加载操作
				if (mListener != null)
					mListener.onLoadMore(this, ACTION_LOAD_MORE);
			}
			hide();
		default:
			break;
		}
		// 事件分发交给父类来处理
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 
	 * @ClassName: AutoRefreshAndLoadTask
	 * @Description: TODO(自动模拟手指滑动的task)
	 * @author ymchen
	 * @date 2015年5月13日 下午5:01:35
	 * 
	 */
	private class AutoRefreshAndLoadTask extends
			AsyncTask<Integer, Float, String> {

		@Override
		protected String doInBackground(Integer... params) {
			while (pullDownY < 4 / 3 * refreshDistance) {
				pullDownY += MOVE_SPEED;
				publishProgress(pullDownY);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			changeState(REFRESHING);
			// 刷新操作
			if (mListener != null)
				mListener.onRefresh(PullToRefreshLoadView.this, ACTION_REFRESH);
			hide();
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			if (pullDownY > refreshDistance)
				changeState(RELEASE_TO_REFRESH);
			requestLayout();
		}
	}

	/**
	 * 
	 * @Title: autoRefresh
	 * @Description: TODO(自动刷新)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void autoRefresh() {
		AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
		task.execute();
	}

	/**
	 * 
	 * @Title: autoLoad
	 * @Description: TODO(自动加载)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void autoLoad() {
		pullUpY = -loadmoreDistance;
		requestLayout();
		changeState(LOADING);
		// 加载操作
		if (mListener != null)
			mListener.onLoadMore(this, ACTION_LOAD_MORE);
	}

	/**
	 * 
	 * @Title: removeFooterView
	 * @Description: TODO(移除加载视图，不进行加载操作)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	public void removeFooterView() {
		if (loadmoreView != null) {
			removeView(loadmoreView);
			invalidate();
			loadmoreView = null;
		}
	}

	public View getFooterView() {
		return loadmoreView;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (null != refreshView) {
			// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
			refreshView.layout(0, (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
					refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
			pullableView.layout(0, (int) (pullDownY + pullUpY),
					pullableView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
					+ pullableView.getMeasuredHeight());
			if (loadmoreView != null) {
				loadmoreView.layout(
						0,
						(int) (pullDownY + pullUpY)
						+ pullableView.getMeasuredHeight(),
						loadmoreView.getMeasuredWidth(),
						(int) (pullDownY + pullUpY)
						+ pullableView.getMeasuredHeight()
						+ loadmoreView.getMeasuredHeight());
			}
		}

	}

	/**
	 * 
	 * @ClassName: MyTimer
	 * @Description: TODO(自定义定时器)
	 * @author ymchen
	 * @date 2015年5月13日 下午5:03:38
	 * 
	 */
	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}
		
		public void destory() {
			mTask = null;
			timer = null;
		}

		/**
		 * 
		 * @ClassName: MyTask
		 * @Description: TODO(定时器任务)
		 * @author ymchen
		 * @date 2015年5月13日 下午5:03:55
		 * 
		 */
		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}

		}
	}

	/**
	 * 
	 * @ClassName: OnRefreshLoadListener
	 * @Description: TODO(刷新加载接口)
	 * @author ymchen
	 * @date 2015年5月13日 下午1:31:06
	 * 
	 */
	public interface OnRefreshLoadListener {
		/**
		 * 
		 * @Title: onRefresh
		 * @Description: TODO(刷新操作)
		 * @param @param pullToRefreshLayout 设定文件
		 * @param @param state 状态
		 * @return void 返回类型
		 * @throws
		 */
		void onRefresh(PullToRefreshLoadView pullToRefreshLayout, int state);

		/**
		 * 
		 * @Title: onLoadMore
		 * @Description: TODO(加载操作)
		 * @param @param pullToRefreshLayout 设定文件
		 * @param @param state 状态
		 * @return void 返回类型
		 * @throws
		 */
		void onLoadMore(PullToRefreshLoadView pullToRefreshLayout, int state);

		/**
		 * 
		 * @Title: onLoadScrollFinish
		 * @Description: TODO(加载回滚完成时操作)
		 * @param @param pullToRefreshLayout 设定文件
		 * @param @param state 状态
		 * @return void 返回类型
		 * @throws
		 */
		void onLoadScrollFinish(PullToRefreshLoadView pullToRefreshLayout,
				int state);
	}
	
	public void destory() {
		this.pullableView = null;
		this.mContext = null;
		this.mListener = null;
		if (null != timer) {
			timer.destory();
			timer = null;
		}
	}
}
