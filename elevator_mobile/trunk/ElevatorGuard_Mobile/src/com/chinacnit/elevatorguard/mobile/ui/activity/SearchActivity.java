package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

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
 * 搜索
 * 
 * @author: pyyang
 * @date 创建时间：2015年7月2日 上午11:26:24
 */
public class SearchActivity extends BaseActivity implements OnRefreshLoadListener, OnClickListener, OnEditorActionListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(SearchActivity.class.getSimpleName(), true);
	private ImageView back;
	private EditText et_search;
	private PullToRefreshLoadView list_search;
	// 带上拉刷新ListView控制器(容器View)
	private PullToRefreshLoadView mPullToRefreshLoadView;
	// 带上拉刷新ListView
	private PullableExpandableListView mExpandableListView;
	private ExpandableListAdapter adapter;
	private List<LiftListDetail> list = new ArrayList<LiftListDetail>();
	private Map<LiftListDetail, List<Map<String, Object>>> listChild;
	private ImageView mIvSearch;

	/** 输入的查询条件 */
	private String input_info;
	// 当前页数
	private int mCurrentPage = 1;
	// 每页显示的行数
	private int mCurrentRows = 10;	
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				list.clear();
				list.addAll((List<LiftListDetail>)msg.obj);
				showList();
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		initview();
	}

	private void initview() {
		mIvSearch = (ImageView) findViewById(R.id.search_image);
		mIvSearch.setOnClickListener(this);
		list_search = (PullToRefreshLoadView) findViewById(R.id.list_search);
		list_search.setOnRefreshLoadListener(SearchActivity.this);
		et_search = (EditText) findViewById(R.id.edit_search);
		et_search.addTextChangedListener(new TextWatcher_s());
		et_search.setOnEditorActionListener(this);
		back = (ImageView) findViewById(R.id.back_image);
		back.setOnClickListener(this);
	}

	protected void getNewData() {
		if (null != mLoginDetail) {
			new LiftListTask(this, input_info, mCurrentPage, mCurrentRows, R.string.loading, new IResultListener<LiftList>() {
				@Override
				public void onSuccess(LiftList result) {	
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = result.getRows();
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(SearchActivity.this, R.string.no_data);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(SearchActivity.this, R.string.network_unavailable);
				}
			}).execute();
		}
	}

	
	class TextWatcher_s implements TextWatcher {

		@Override
		public void afterTextChanged(Editable edit) {
			
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int before, int count) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int count, int after) {
			if (et_search.getText().toString() != null && !et_search.getText().toString().equals("")) {
				list.clear();
				input_info = et_search.getText().toString();
//				getNewData();
			} else {
				list.clear();
				if (null != adapter) {
					adapter.onDateChange(list, listChild);
				}
			}
		}
	}
	
	@Override  
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {  
        switch(actionId){  
        case EditorInfo.IME_ACTION_SEARCH:  
            getNewData();
            break;  
        }  
        return true;
    }  
	
	protected void showList() {
		if (adapter == null && null != list && list.size() > 0) {
			mPullToRefreshLoadView = (PullToRefreshLoadView) findViewById(R.id.list_search);
			mPullToRefreshLoadView.setOnRefreshLoadListener(SearchActivity.this);
			mExpandableListView = new PullableExpandableListView(SearchActivity.this);
			mExpandableListView.setGroupIndicator(null);
			prepareListData();
			list_search.setRefreshLoadView(SearchActivity.this, mExpandableListView);
			adapter = new ExpandableListAdapter(SearchActivity.this, list, listChild);
			mExpandableListView.setAdapter(adapter);
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
		} else if (adapter != null && null != list && list.size() > 0) {
			prepareListData();
			adapter.onDateChange(list, listChild);
		}
		
	}

	@Override
	public void onRefresh(PullToRefreshLoadView pullToRefreshLayout, int state) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 搜到的数据
	 * 
	 * @author: pyyang
	 * @date 创建时间：2015年7月3日 下午4:19:35
	 */
	public void prepareListData() {
		listChild = new HashMap<LiftListDetail, List<Map<String, Object>>>();
		// child 数据
		List<Map<String, Object>> list_student = new ArrayList<Map<String, Object>>();
		Map<String, Object> student = new HashMap<String, Object>();
		student.put("weixiu", "维保详情");
		student.put("lift", "电梯详情");
		student.put("settingstag", "设置标签");
		list_student.add(student);
		for (int i = 0; null != list && i < list.size(); i++) {
			listChild.put(list.get(i), list_student);
		}
	}

	@Override
	public void onLoadMore(PullToRefreshLoadView pullToRefreshLayout, int state) {
		++mCurrentPage;
		LogUtils.d(LOG_TAG, "onLoadMore", "mCurrentPage:" + mCurrentPage);
		if (null != mLoginDetail) {
			new LiftListTask(this, input_info, mCurrentPage, mCurrentRows, R.string.loading, new IResultListener<LiftList>() {
				@Override
				public void onSuccess(LiftList result) {
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = result.getRows();
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(SearchActivity.this, R.string.no_data);
						mCurrentPage--;
					}
					mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.SUCCEED);
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(SearchActivity.this, R.string.network_unavailable);
					mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.FAIL);
				}
			}).execute();
		}
	}

	@Override
	public void onLoadScrollFinish(PullToRefreshLoadView pullToRefreshLayout,
			int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_image:
			PageTurnUtil.goBack(this);
			break;
		case R.id.search_image:
			getNewData();
			break;
		default:
			break;
		}
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
