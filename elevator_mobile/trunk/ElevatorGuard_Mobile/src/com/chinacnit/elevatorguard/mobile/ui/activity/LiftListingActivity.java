package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.ExpandableListAdapter;
import com.chinacnit.elevatorguard.mobile.bean.LiftList;
import com.chinacnit.elevatorguard.mobile.bean.LiftListDetail;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.LiftListTask;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView.OnRefreshLoadListener;
import com.chinacnit.elevatorguard.mobile.ui.view.PullableExpandableListView;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;

/**
 * 电梯列表 界面
 * 
 * @author pyyang
 * @date 创建时间：2015年5月26日 下午3:50:44
 * 
 */
public class LiftListingActivity extends BaseActivity implements
		OnClickListener, OnRefreshLoadListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(LiftListingActivity.class.getSimpleName(), true);
	// 电梯列表适配器
	private ExpandableListAdapter listAdapter;
	private List<LiftListDetail> list = new ArrayList<LiftListDetail>();
	private Map<LiftListDetail, List<Map<String, Object>>> listDataChild;
	private Button wb_btn;
	private Button lift_btn;
	// 返回按钮
	private LinearLayout back_ll;
	// 返回按钮文本
	private TextView back_textview;
	private Button search_button;
	// 带上拉刷新ListView控制器(容器View)
	private PullToRefreshLoadView mPullToRefreshLoadView;
	// 带上拉刷新ListView
	private PullableExpandableListView mExpandableListView;

	// 当前页数
	private int mCurrentPage = 1;

	// 每页显示的行数
	private int mCurrentRows = 10;	

	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				list.addAll((List<LiftListDetail>)msg.obj);
				showList();
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lift_listing);
//		setData1();
		initView();
		setData();
	}
	
	private void initView() {
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.lift_list);
		search_button = (Button) findViewById(R.id.search_button);
		search_button.setVisibility(View.VISIBLE);
		search_button.setOnClickListener(this);
		wb_btn = (Button) findViewById(R.id.weixiu_details_button);
		lift_btn = (Button) findViewById(R.id.lift_details_button);
		showList();
	}

	private void setData() {
		if (null != mLoginDetail) {
			new LiftListTask(this, mCurrentPage, mCurrentRows, R.string.loading, new IResultListener<LiftList>() {
				@Override
				public void onSuccess(LiftList result) {
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = result.getRows();
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(LiftListingActivity.this, R.string.no_data);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(LiftListingActivity.this, R.string.network_unavailable);
				}
			}).execute();
		}
	}
	
	/**
	 * 电梯列表的数据
	 * 
	 * @author: pyyang
	 * @date 创建时间：2015年5月26日 下午3:51:36
	 */
	private void prepareListData() {
		listDataChild = new HashMap<LiftListDetail, List<Map<String, Object>>>();
		// child 数据
		List<Map<String, Object>> list_student = new ArrayList<Map<String, Object>>();
		Map<String, Object> student = new HashMap<String, Object>();
		student.put("weixiu", "维保详情");
		student.put("lift", "电梯详情");
		student.put("settingstag", "设置标签");
		list_student.add(student);
		for (int i = 0; null != list && i < list.size(); i++) {
			listDataChild.put(list.get(i), list_student);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.search_button:
			PageTurnUtil.gotoActivity(LiftListingActivity.this, SearchActivity.class, false);
			break;
		default:
			break;
		}
	}

	protected void showList() {
		if (listAdapter == null && null != list && list.size() > 0) {
			mPullToRefreshLoadView = (PullToRefreshLoadView) findViewById(R.id.lvExp);
			mPullToRefreshLoadView.setOnRefreshLoadListener(LiftListingActivity.this);
			
			mExpandableListView = new PullableExpandableListView(LiftListingActivity.this);
			mExpandableListView.setGroupIndicator(null);
			mExpandableListView.setDivider(getResources().getDrawable(R.drawable.div2));
			mExpandableListView.setDividerHeight(1);
			prepareListData();
			mPullToRefreshLoadView.setRefreshLoadView(LiftListingActivity.this, mExpandableListView);
			listAdapter = new ExpandableListAdapter(LiftListingActivity.this, list, listDataChild);
			mExpandableListView.setAdapter(listAdapter);
			mExpandableListView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					// TODO Auto-generated method stub
					return false;
				}
			});
			mExpandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {

						@Override
						public void onGroupExpand(int groupPosition) {
							for (int i = 0; i < list.size(); i++) {    
				                if (groupPosition != i) {    
				                	mExpandableListView.collapseGroup(i);    
				                }    
				            } 
						}
					});
			mExpandableListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

						@Override
						public void onGroupCollapse(int arg0) {
							// TODO Auto-generated method stub
						}
					});
			mExpandableListView.setOnChildClickListener(new OnChildClickListener() {

				@Override
				public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
					LogUtils.d(LOG_TAG, "onChildClick", "childPosition:" + childPosition);
					return true;
				}
			});
		} else if (listAdapter != null && null != list && list.size() > 0) {
			prepareListData();
			listAdapter.onDateChange(list, listDataChild);
		}
	}

	@Override
	public void onRefresh(PullToRefreshLoadView pullToRefreshLayout, int state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoadMore(PullToRefreshLoadView pullToRefreshLayout, int state) {
		++mCurrentPage;
		LogUtils.d(LOG_TAG, "onLoadMore", "mCurrentPage:" + mCurrentPage);
		if (null != mLoginDetail) {
			new LiftListTask(this, mCurrentPage, mCurrentRows, R.string.loading, new IResultListener<LiftList>() {
				@Override
				public void onSuccess(LiftList result) {
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = result.getRows();
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(LiftListingActivity.this, R.string.no_data);
						mCurrentPage--;
					}
					mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.SUCCEED);
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(LiftListingActivity.this, R.string.network_unavailable);
					mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.FAIL);
				}
			}).execute();
		}
	}

	@Override
	public void onLoadScrollFinish(PullToRefreshLoadView pullToRefreshLayout,
			int state) {
	}
	
	@Override
	protected void onDestroy() {
		if (null != mPullToRefreshLoadView) {
			mPullToRefreshLoadView.destory();
			mPullToRefreshLoadView = null;
		}
		super.onDestroy();
	}

}
