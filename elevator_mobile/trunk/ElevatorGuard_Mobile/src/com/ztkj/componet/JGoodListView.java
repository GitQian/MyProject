/**
 * @file XListView.java
 * @package me.maxwin.view
 * @create Mar 18, 2012 6:28:41 PM
 * @author Maxwin，joychine joychine@qq.com
 * @description An ListView support (a) Pull down to refresh, (b) Pull up to load more.
 * 		Implement IXListViewListener, and see stopRefresh() / stopLoadMore().
 */
package com.ztkj.componet;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Scroller;
/**
 * @description
 *  支持下拉刷新和上拉加载更多的ListView<br>
 *  需要实现  IXListViewListener 接口，处理刷新完成或者是加载更多完成后该ui的变化。<br>
 * 	setPageSize(int pageSize) 该方法设置每次刷新或者是加载更多 获取的数据个数,默认是30。<br>
 *  setFirstLoadPullFresh(boolean isFirstLoadPullFresh) 该方法设置是否在该ListView加载到界面后 延迟500毫秒后 自动刷新，默认是true。<br>
 *  setPullLoadEnable(boolean enable) 设置是否开启上拉加载更多，默认为true<br>
 *  setPullRefreshEnable(boolean enable) 设置是否开启下拉刷新,默认为true，关闭下拉刷新，会同时关闭 首次加载刷新， setFirstLoadPullFresh(false)会被调用(除非你是想在某个时间之后禁用下拉刷新，一般不在创建ListView时就禁用)<br>
 *  setOtherViewCount(int otherViewCount) 设置headerView和FooterView的个数，默认是2.
 *  setShowFooterWhenReresh(boolean isShowFooterWhenReresh) 设置是否在刷新时仍然显示footerview(这个只针对上次状态是全部加载完毕时有效)，默认false
 *  onCompleted(int page) 该方法当你的数据加载完毕后 在你的MainActivity中调用。
 *  <font color='#ff0000'存在bug：getLastVisiblePosition()和 getFirstVisiblePosition 在屏幕未点亮情况下是不对的……nubia z5s mini 4.4.2系统是这样，这个问题发现很久了，s8600 2.3.4系统没这个问题！(不知与安卓原生系统有关还是与厂商rom有关)
 * @file XGoodListView.java
 * @author liucheng  liucheng187@qq.com
 * @create 2015年2月11日 14:18:26
 */
public class JGoodListView extends ListView implements OnScrollListener {
	public static final String TAG = JGoodListView.class.getSimpleName();
	public static final boolean DEBUG = false;
	/**
	 * every operation get data size，defualt is 30<br>
	 * 每次访问网络加载的数据条数,默认30条
	 * */
	private int pageSize =30;
	/**是否 创建 该 ListView时 500毫秒后自动 刷新， 该值在ListView被创建只会被置为true一次*/
	private boolean isFirstLoadPullFresh = true;
	/**刷新时是否 隐藏 footerview*/
	private boolean isShowFooterWhenReresh ;
	
	private float mLastY = -1; // save event y
	private Scroller mHeadScroller,mFooterScroller; // used for scroll back ，need 2 scroller to deal top and bottom
	private OnScrollListener mScrollListener; // user's scroll listener

	// the interface to trigger refresh and load more.
	private IXListViewListener mListViewListener;

	// -- header view
	private JListViewHeader mHeaderView;
	// header view content, use it to calculate the Header's height. And hide it
	// when disable pull refresh.

	private int mHeaderViewHeight; // header view's height
	private boolean mEnablePullRefresh = true;
	private boolean mPullRefreshing = false; // is refreashing.

	// -- footer view
	private JListViewFooter mFooterView;

	private int mFooterViewHeight; // footer equal header view's height  // when pull up >= 50px	 // at bottom, trigger	// load more.
	private boolean mEnablePullLoad = true;
	private boolean mPullLoading;  
	/**标识当前是否全部加载完毕*/
	private boolean isLoadOver;
	
	// total list items, used to detect is at the bottom of listview.
//	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer. 其实可以不用了。
	private int mScrollBack;
	private final static int SCROLLBACK_HEADER = 0;
	private final static int SCROLLBACK_FOOTER = 1;

	private final static int SCROLL_DURATION = 300; // scroll back duration
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
	public JGoodListView(Context context) {
		super(context);
		initWithContext(context);
	}

	public JGoodListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	public JGoodListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initWithContext(context);
	}

	private void initWithContext(Context context) {
		mHeadScroller = new Scroller(context, new DecelerateInterpolator());
		mFooterScroller = new Scroller(context, new DecelerateInterpolator());
		// XListView need the scroll event, and it will dispatch the event to
		// user's listener (as a proxy).
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new JListViewHeader(context);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new JListViewFooter(context);
		addFooterView(mFooterView);

		// init header height
		mHeaderView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						mHeaderViewHeight = mHeaderView.getHeaderViewContent().getHeight();
						
						mFooterViewHeight = mHeaderViewHeight;
						
						if(DEBUG)Log.e(TAG, "onGlobalLayout...mHeaderViewHeight:"+mHeaderViewHeight);
						getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
		mHeaderView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				if(isFirstLoadPullFresh && mEnablePullRefresh){
					if(mListViewListener==null)
						throw new IllegalArgumentException("must set IXListViewListener !!!");
						
					setFreshing();
				}
			}
		}, 500);
	}
	
    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();  
        if (params == null) {
            params = new ViewGroup.LayoutParams(  
                    ViewGroup.LayoutParams.FILL_PARENT,  
                    ViewGroup.LayoutParams.WRAP_CONTENT);  
        }  
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,   params.width);  
        int lpHeight = params.height;  
        int childHeightSpec;  
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,  
                    MeasureSpec.EXACTLY);  
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,  
                    MeasureSpec.UNSPECIFIED);  
        }
        child.measure(childWidthSpec, childHeightSpec);  
    }
    
    

	/**
	 * enable or disable pull down refresh feature.
	 *   isFirstLoadPullFresh = false is to be set also.
	 * @param enable
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			isFirstLoadPullFresh = false;
			mHeaderView.setVisibility(View.GONE);
		} else {
			mHeaderView.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * enable or disable pull up load more feature.
	 * @param enable
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) { // disable, hide the content
			mFooterView.setVisibility(View.GONE);
//			mFooterView.getFooterViewContent().setVisibility(View.GONE);
		} else {
			mFooterView.setVisibility(View.VISIBLE);
			mFooterView.getFooterViewContent().setVisibility(View.VISIBLE);
		}
	}
	
	
	/**
	 * I judge operation type(refresh or loadmore) by page, so take care of  page<br>
	 * when pull down refresh or pull up loadmore over,please call this method.
	 */
	public void onCompleted(int page){
		if(page <1) throw new IllegalArgumentException("page can not <1");
//		if(getListView().getAdapter() instanceof HeaderViewListAdapter){
		int amount = ((HeaderViewListAdapter)getAdapter()).getWrappedAdapter().getCount();
		int needAmount = page * pageSize;
		
//	Log.e(TAG, "needAmount:"+needAmount+"--amount:"+amount+"--page:"+page);
		if(DEBUG)Log.e(TAG, "onCompleted()----amount:"+amount+"--needAmount:"+needAmount);
		
		if(DEBUG)Log.e(TAG, "onCompleted()-----mPullLoading:"+mPullLoading+"--mPullRefreshing:"+mPullRefreshing);
		if(amount !=0 &&((needAmount%amount !=0 && needAmount/amount<=page)||(page==1 && needAmount%amount !=0) ||(needAmount>amount) )){ //data load over  好不好判断。。
//			Log.e(TAG, "加载全部做完了。。amount:"+amount);
			setLoadOver();
		}else{ //left data
			if(page == 1 && amount ==0){//网络加载失败的现象，也显示 加载完成。
				setLoadOver();
			}else{
				setLoadNotOver();
			}
		}
		
		setRefreshTime(new Date().toLocaleString());
		stopRefresh();
		stopLoadMore();
	}
	
	/**change ui features when all data was load over*/
	private void setLoadOver(){
		isLoadOver = true;
		mFooterView.onDataLoadOver();
		mFooterView.setVisiableHeight(mFooterViewHeight);
		if(DEBUG)
			Log.i(TAG, "setLoadOverDeal() --mFooterViewContent:"+mFooterView.getFooterViewContent().getHeight());
	}
	
	/**change ui features when all data is remaining*/
	private void setLoadNotOver() {
		isLoadOver = false;
		mFooterView.onDataNotLoadOver();
	}
	
	/**
	 * stop refresh, reset header view.
	 */
	private void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * stop load more, reset footer view.
	 */
	private void stopLoadMore() {
		if(DEBUG)Log.e(TAG, "getAdapter().getCount(): "+getAdapter().getCount()+"--firstitemPoint: "+getFirstVisiblePosition()+"  lastitempoint:"+getLastVisiblePosition());
	/*	postDelayed(new Runnable() {
			@Override
			public void run() {
				if(DEBUG)Log.e(TAG, "2秒后---stopLoadMore()--getAdapter().getCount(): "+getAdapter().getCount()+"--firstitemPoint: "+getFirstVisiblePosition()+"  lastitempoint:"+getLastVisiblePosition());
			}
		}, 2000);*/
		
		if (mPullLoading == true ) {
			mPullLoading = false;
//			Log.e(TAG, "真想只有一个。。。。isLoadOver:"+isLoadOver);
//			mFooterView.setState(XListViewFooter.STATE_NORMAL);
			if(!isLoadOver) resetFooterHeight();   //if isLoadOver to be true don't hide the footerview.
		}else{
			//如果上次是加载完毕，现在footer的高度还要整回到为0, 这是肯定是刷新的操作。 不需要动画立刻隐藏。
			if(!isLoadOver) mFooterView.hide();
		}
	}

	/**
	 * set last refresh time
	 * @param time
	 */
	private void setRefreshTime(String time) {
		mHeaderView.setRefreshTime(time);
		mFooterView.setRefreshTime(time);
	}
	
	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public boolean isFirstLoadPullFresh() {
		return isFirstLoadPullFresh;
	}

	public void setFirstLoadPullFresh(boolean isFirstLoadPullFresh) {
		this.isFirstLoadPullFresh = isFirstLoadPullFresh;
	}
	
	public boolean isShowFooterWhenReresh() {
		return isShowFooterWhenReresh;
	}

	public void setShowFooterWhenReresh(boolean isShowFooterWhenReresh) {
		this.isShowFooterWhenReresh = isShowFooterWhenReresh;
	}

	private void invokeOnScrolling() {
		if (mScrollListener instanceof OnXScrollListener) {  
			OnXScrollListener l = (OnXScrollListener) mScrollListener;
			l.onXScrolling(this);
		}
	}

	/**
	 * reset header view's height. maybe now state is refreh has done or  refresing when u operate  
	 */
	private void resetHeaderHeight() {
		int visibleHeight = mHeaderView.getVisiableHeight();  //黑屏时。。。。得到0………… 不黑屏可以wrap_content得到120………下面语句处理。。…  joychine mark
		if(visibleHeight == 0 && isFirstLoadPullFresh ){
			isFirstLoadPullFresh = false;
			visibleHeight = mHeaderViewHeight;
		}
		
		if(DEBUG)
			Log.e(TAG, "resetHeaderHeight()----mPullRefreshing:"+mPullRefreshing+"---VisiableHeigh："+visibleHeight+"---mHeaderViewHeight："+ mHeaderViewHeight);
		
		if (visibleHeight == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullRefreshing && visibleHeight <= mHeaderViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullRefreshing && visibleHeight > mHeaderViewHeight) {
			finalHeight = mHeaderViewHeight;
		}
		mScrollBack = SCROLLBACK_HEADER;
		if(DEBUG)
			Log.e(TAG, "resetHeaderHeight()--mHeaderViewHeight:"+mHeaderViewHeight+"--finalHeight:"+finalHeight+"--(finalHeight - visibleHeight):"+(finalHeight - visibleHeight));
		mHeadScroller.startScroll(0, visibleHeight, 0, finalHeight - visibleHeight,SCROLL_DURATION);
		
		// trigger computeScroll
		invalidate();
	}
	
	/**
	 * reset header view's height.
	 */
	private void resetFooterHeight() {
		int visibleHeight = mFooterView.getVisiableHeight();
		
		if(DEBUG) Log.e(TAG, "resetFooterHeight()----mPullLoading:"+mPullLoading+"---visibleHeight:"+visibleHeight+"----mFooterViewHeight:"+mFooterViewHeight);
		
		if (visibleHeight == 0) // not visible.
			return;
		// refreshing and header isn't shown fully. do nothing.
		if (mPullLoading && visibleHeight <= mFooterViewHeight) {
			return;
		}
		int finalHeight = 0; // default: scroll back to dismiss header.
		// is refreshing, just scroll back to show all the header.
		if (mPullLoading && visibleHeight > mFooterViewHeight) {
			finalHeight = mFooterViewHeight;
		}
		mScrollBack = SCROLLBACK_FOOTER;
		if(DEBUG) Log.e(TAG, "resetFooterHeight()--mFooterViewHeight:"+mFooterViewHeight+"--finalHeight:"+finalHeight+"--(finalHeight - visibleHeight):"+(finalHeight - visibleHeight));
		mFooterScroller.startScroll(0, visibleHeight, 0, (finalHeight - visibleHeight),SCROLL_DURATION);
		
		// trigger computeScroll
		invalidate();
	}

	private void updateHeaderHeight(float delta) {
		mHeaderView.setVisiableHeight((int) delta + mHeaderView.getVisiableHeight());
		if (mEnablePullRefresh && !mPullRefreshing) { // 未处于刷新状态，更新箭头
			if (mHeaderView.getVisiableHeight() > mHeaderViewHeight) {
				mHeaderView.setState(JListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(JListViewHeader.STATE_NORMAL);
			}
			setSelection(0); // scroll to top each time
		}
	}
	
	private void updateFooterHeight(float delta) {
		mFooterView.setVisiableHeight((int) delta + mFooterView.getVisiableHeight());
		if (mEnablePullLoad && !mPullLoading) { //// height enough to invoke load // more.
			if (mFooterView.getVisiableHeight() > mFooterViewHeight) {
				mFooterView.setState(JListViewFooter.STATE_READY);
			} else {
				mFooterView.setState(JListViewFooter.STATE_NORMAL);
			}
			setSelection(getAdapter().getCount() - 1); // scroll to bottom， android 2.3 need do this。。。。
		}
		
	}
	
	/**not use*/
	private void startLoadMore() {
		mPullLoading = true;
		mFooterView.setState(JListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {  
			mListViewListener.onLoadMore();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			
			//头部刷新条件
//			Log.e(TAG, "getFirstVisiblePosition()："+getFirstVisiblePosition());
//			Log.e(TAG, "!mPullLoading："+!mPullLoading);
//			Log.e(TAG, "isLoadOver："+isLoadOver);
//			Log.e(TAG, "mFooterView.getVisiableHeight()："+mFooterView.getVisiableHeight());
//			Log.e(TAG, "mHeaderView.getVisiableHeight()："+mHeaderView.getVisiableHeight());
//			Log.e(TAG, "deltaY："+deltaY);
//			
//			
//			Log.e(TAG, "getLastVisiblePosition() == mTotalItemCount - 1?"+(getLastVisiblePosition() == getCount() - 1));
//			Log.e(TAG, "!mPullRefreshing?"+(!mPullRefreshing));
//			Log.e(TAG, "mHeaderView.getVisiableHeight() <= 0?"+(mHeaderView.getVisiableHeight() <= 0));   // 居然不是false。。。。
//			Log.e(TAG, "mHeaderView.getVisiableHeight()："+(mHeaderView.getVisiableHeight()));   // 居然不是false。。。。  47……
//			Log.e(TAG, "mFooterView.getVisiableHeight() > 0?"+(mFooterView.getVisiableHeight() > 0));
//			Log.e(TAG, "deltaY < 0?"+(deltaY < 0));
//			Log.e(TAG, "(HeaderViewListAdapter)getAdapter()).getWrappedAdapter().getCount()>0?"+(((HeaderViewListAdapter)getAdapter()).getWrappedAdapter().getCount()>0));
			if (mEnablePullRefresh && getFirstVisiblePosition() == 0 && !mPullLoading && (isLoadOver ||mFooterView.getVisiableHeight() <= 0) && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				// the first item is showing, header has shown or pull down.
//				Log.i(TAG, "可以下拉刷新咯。啊啊啊啊isLoadOver:"+isLoadOver);
				updateHeaderHeight(deltaY / OFFSET_RADIO);
				invokeOnScrolling();
				//&& getFirstVisiblePosition() !=0
			} else if (mEnablePullLoad  && getLastVisiblePosition() == getCount() - 1 && !mPullRefreshing && mHeaderView.getVisiableHeight() <= 0 && (mFooterView.getVisiableHeight() > 0 || deltaY < 0) && ((HeaderViewListAdapter)getAdapter()).getWrappedAdapter().getCount()>0) {
				// last item, already pulled up or want to pull up.
//				Log.i(TAG, "可以上拉加载了。。。。啊啊啊啊isLoadOver:"+isLoadOver);
				if(!isLoadOver) updateFooterHeight(-deltaY / OFFSET_RADIO);
				invokeOnScrolling();
			}
			break;
		default:
			mLastY = -1; // reset
			if (getFirstVisiblePosition() == 0 && mHeaderView.getVisiableHeight()>0) {//getFirstVisiblePosition 只要不为 gone就能看到……………… 所以触屏一下也会进入需要 &&判断。
				if(DEBUG)Log.e(TAG, "getFirstVisiblePosition() == 0:"+(getFirstVisiblePosition() == 0));
				// invoke refresh
				if (mEnablePullRefresh && !mPullRefreshing && !mPullLoading && mHeaderView.getVisiableHeight() > mHeaderViewHeight ) {
					mPullRefreshing = true;
					mHeaderView.setState(JListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						
						if(!isShowFooterWhenReresh) mFooterView.hide();  //刷新时，先隐藏 footer的上次记录信息，不论现在footer是否可见。
						mListViewListener.onRefresh();
					}
				}
				resetHeaderHeight();
			}
			
			if (getLastVisiblePosition() == getCount() - 1 && mFooterView.getVisiableHeight()>0 && !isLoadOver) {
				if(DEBUG)Log.e(TAG, "getLastVisiblePosition() == mTotalItemCount - 1:"+(getLastVisiblePosition() == getCount() - 1));
				// invoke load more.
				if (mEnablePullLoad && !mPullRefreshing && !mPullLoading && mFooterView.getVisiableHeight() > mFooterViewHeight  ) {
					mPullLoading = true;
					mFooterView.setState(JListViewFooter.STATE_LOADING);
					if (mListViewListener != null) {
						mListViewListener.onLoadMore();
					}
				}
				if(!isLoadOver) resetFooterHeight(); 
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	public void computeScroll() {
		if (mHeadScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
//				 Log.v(TAG, "mHeadScroller.computeScrollOffset()-------mHeadScroller.getCurrY()："+mHeadScroller.getCurrY());
				mHeaderView.setVisiableHeight(mHeadScroller.getCurrY());
			}
//			else {
//				mFooterView.setVisiableHeight(mHeadScroller.getCurrY());
//			}
//			postInvalidate();
			invalidate();
			invokeOnScrolling();
		}else{
//			Log.w(TAG, "和想象中的不一样。。。。mHeadScroller.computeScrollOffset()不被调用。。。");  2个 scroller不会同时执行…………o(︶︿︶)o 唉
		}
		if (mFooterScroller.computeScrollOffset()) {
			 if(mScrollBack == SCROLLBACK_FOOTER){
//				 Log.v(TAG, "mFooterScroller.computeScrollOffset()-----mFooterScroller.getCurrY()："+mFooterScroller.getCurrY());
				mFooterView.setVisiableHeight(mFooterScroller.getCurrY()); 
			}
//			postInvalidate();
			invalidate();
			invokeOnScrolling();
		}
		super.computeScroll();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		// send to user's listener
//		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,totalItemCount);
		}
	}

	/**
	 * you can listen ListView.OnScrollListener or this one. it will invoke
	 * onXScrolling when header/footer scroll back.
	 */
	public interface OnXScrollListener extends OnScrollListener {
		public void onXScrolling(View view);
	}
	
	/**
	 * 上拉刷新，下拉加载更多， 需要实现该接口。<br>
	 * implements this interface to get refresh/load more event.
	 */
	public interface IXListViewListener {
		public void onRefresh();
		public void onLoadMore();
	}
	
	public void setXListViewListener(IXListViewListener listener) {
		mListViewListener = listener;
	}
	
	/**
	 * 刚开始就下拉加载数据， 请在代码中设置。,或者调用该方法 触发与下拉刷新相同效果。
	 */
	public void setFreshing() {
		if (mEnablePullRefresh && mListViewListener != null ) {
			mHeaderView.setVisiableHeight(mHeaderViewHeight);
			mPullRefreshing = true;
			mHeaderView.setState(JListViewHeader.STATE_REFRESHING);
			mListViewListener.onRefresh();
		}
	}
	
}
