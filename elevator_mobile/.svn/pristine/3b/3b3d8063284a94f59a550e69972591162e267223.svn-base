package com.chinacnit.elevatorguard.mobile.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;
import android.widget.ListView;

/**
 * 
 * @ClassName: PullableListView 
 * @Description: TODO(下拉上拉的ListView) 
 * @author ymchen 
 * @date 2015年5月14日 上午10:44:57 
 *
 */
/**
 * PullableExpandableListView (下拉上拉的ExpandableListView)
 * 
 * @author pyyang
 * @date 创建时间：2015年6月1日 上午9:17:07
 * 
 */
public class PullableExpandableListView extends ExpandableListView implements
		Pullable {

	public PullableExpandableListView(Context context) {
		super(context);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PullableExpandableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown() {
		if (getCount() == 0) {
			// 没有item的时候也可以下拉刷新
			// return true;
			return false;// 此方法返回false屏蔽下拉
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0) {
			// 滑到ExpandableListView的顶部了
			// return true;
			return false;
		} else
			return false;
	}

	@Override
	public boolean canPullUp() {
		if (getCount() == 0) {
			// 没有item的时候也可以上拉加载
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1)) {
			// 滑到底部了
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getBottom() <= getMeasuredHeight())
				return true;
		}
		return false;
	}
}
