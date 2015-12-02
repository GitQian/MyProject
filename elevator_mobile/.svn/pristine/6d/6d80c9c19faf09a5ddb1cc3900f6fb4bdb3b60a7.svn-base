package com.chinacnit.elevatorguard.mobile.ui.view;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;


/**
 * PullableListView (下拉上拉的ListView)
 * 
 * @author pyyang
 * @date 创建时间：2015年6月1日 上午9:11:17
 *
 */
public class PullableListView extends ListView implements Pullable {
	
	private boolean mCanPullDown = false;
	private boolean mCanPullUp = false;

    public PullableListView(Context context) {
        super(context);
    }

    public PullableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    public PullableListView(Context context, boolean canPullDown, boolean canPullUp) {
        super(context);
        this.mCanPullDown = canPullDown;
        this.mCanPullUp = canPullUp;
    }

    @Override
    public boolean canPullDown() {
    	if (!mCanPullDown) {
    		return false;
    	}
        if (getCount() == 0) {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop() >= 0) {
            // 滑到ListView的顶部了
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canPullUp() {
    	if (!mCanPullUp) {
    		return false;
    	}
        if (getCount() == 0) {
            // 没有item的时候也可以上拉加载
            return true;
        } else if (getLastVisiblePosition() == (getCount() - 1)) {
            // 滑到底部了
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
                    && getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
                return true;
        }
        return false;
    }
}
