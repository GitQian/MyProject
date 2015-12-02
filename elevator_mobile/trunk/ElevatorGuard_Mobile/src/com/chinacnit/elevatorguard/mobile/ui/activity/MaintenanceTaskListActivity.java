package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.MaintenanceTaskListAdaper;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskList;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail.MaintenanceStatus;
import com.chinacnit.elevatorguard.mobile.bean.QueryMaintenanceTaskIsStart;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.MaintenanceTaskListTask;
import com.chinacnit.elevatorguard.mobile.http.task.impl.QueryMaintenanceBeanTask;
import com.chinacnit.elevatorguard.mobile.http.task.impl.QueryMaintenanceTaskIsStartTask;
import com.chinacnit.elevatorguard.mobile.ui.view.CustomDatePickerDialog;
import com.chinacnit.elevatorguard.mobile.ui.view.CustomDatePickerDialog.onDateListener;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView.OnRefreshLoadListener;
import com.chinacnit.elevatorguard.mobile.ui.view.PullableListView;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ztkj.base.business.NetCommonActivity;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestSign;
import com.ztkj.data.response.MaintenanceBean;
import com.ztkj.tool.Tool;

/**
 * 维保任务列表Activity
 * @deprecated
 * @author ssu
 * @date 2015-6-2 下午8:41:02
 */
public class MaintenanceTaskListActivity extends NetCommonActivity implements
		OnItemClickListener, OnClickListener, OnRefreshLoadListener {
	
	private final int ACCEPT_QUERY=0;
	private List<MaintenanceBean> mListBean;
	private MaintenanceTaskListDetail mBean ; //点击了的那个 item对应的planid。
	private  boolean startMode; //
	
	private MaintenanceTaskListActivity mActivity;
	private static final LogTag LOG_TAG = LogUtils.getLogTag(MaintenanceTaskListActivity.class.getSimpleName(), true);

	private MaintenanceTaskListAdaper mMaintenanceTaskListAdapter;
	private List<MaintenanceTaskListDetail> list = new ArrayList<MaintenanceTaskListDetail>();

	private PullToRefreshLoadView mPullToRefreshLoadView;
	private PullableListView mListView;

	/** 返回按钮 */
	private LinearLayout mLlBack;
	/** 返回文本 */
	private TextView mTvBack;
	/** 标题栏维保时间按钮 */
	private RelativeLayout title_right_rl;
	
	/** 当前页数 */
	private int mCurrentPage = 1;

	/** 每页显示的行数 */
	private int mCurrentRows = 10;
	
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	private AlertDialog mDialog;
	
	/** 自定义时间选择控件 */
	private CustomDatePickerDialog mCustomDatePickerDialog;
	/** 输入的查询日期 */
	private String mInputDate;
	
	public MaintenanceTaskListActivity() {
		mActivity = this;
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				list.addAll((List<MaintenanceTaskListDetail>)msg.obj);
				showList(list);
				break;
			case 1:
				list.clear();
				List<MaintenanceTaskListDetail> tempList = (List<MaintenanceTaskListDetail>) msg.obj;
				if (null != tempList && tempList.size() > 0) {
					list.addAll(tempList);
				}
				showList(list);
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintenance_task_list);
		initView();
		setData();
	}

	private void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.back_ll);
		mLlBack.setOnClickListener(this);
		mTvBack = (TextView) findViewById(R.id.back_textview);
		mTvBack.setText(R.string.maintenance_task);
		title_right_rl = (RelativeLayout) findViewById(R.id.wb_time_rl);
//		title_right_rl.setVisibility(View.VISIBLE);
		mCustomDatePickerDialog = new CustomDatePickerDialog(MaintenanceTaskListActivity.this, Calendar.getInstance());
		mCustomDatePickerDialog.addDateListener(new onDateListener() {
			
			@Override
			public void dateFinish(Calendar c) {
				mInputDate = DateFormat.format("yyyy-MM-dd", c).toString();
				mHandler.sendEmptyMessage(1);
				setData();
			}
		});
		title_right_rl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mCustomDatePickerDialog.show();
			}
		});
	}
	
	/**
	 * 加载维保任务列表
	 * @param
	 * @author: ssu
	 * @date: 2015-6-11 下午8:27:44
	 */
	private void setData() {
		if (null != mLoginDetail) {
			new MaintenanceTaskListTask(this, mInputDate, mCurrentPage, mCurrentRows, null, "planEndDate", "desc", R.string.loading, new IResultListener<MaintenanceTaskList>() {
				
				@Override
				public void onSuccess(MaintenanceTaskList result) {
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						List<MaintenanceTaskListDetail> tempList = result.getRows();
						Message msg = new Message();
						msg.what = 0;
						msg.obj = tempList;
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.no_data);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.network_unavailable);
				}
			}).execute();
		}
	}

	/**
	 * 展示维保记录数据
	 * 
	 * @author: pyyang
	 * @date 创建时间：2015年5月28日 上午11:35:27
	 */
	private void showList(List<MaintenanceTaskListDetail> list) {
		if (mMaintenanceTaskListAdapter == null && null != list && list.size() > 0) {
			mPullToRefreshLoadView = (PullToRefreshLoadView) findViewById(R.id.maintenance_task_list_listview);
			mPullToRefreshLoadView.setOnRefreshLoadListener(MaintenanceTaskListActivity.this);
			mListView = new PullableListView(MaintenanceTaskListActivity.this, true, true);
			mListView.setDivider(null);
			mPullToRefreshLoadView.setRefreshLoadView(MaintenanceTaskListActivity.this, mListView);
			mListView.setOnItemClickListener(this);
			mMaintenanceTaskListAdapter = new MaintenanceTaskListAdaper(MaintenanceTaskListActivity.this, list);
			mListView.setAdapter(mMaintenanceTaskListAdapter);
		} else if (mMaintenanceTaskListAdapter != null && null != list && list.size() > 0) {
			mMaintenanceTaskListAdapter.onDateChange(list);
		} else if (mMaintenanceTaskListAdapter != null) {
			mMaintenanceTaskListAdapter.onDateChange(null);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		mBean = (MaintenanceTaskListDetail) arg0.getAdapter().getItem(arg2);
		
		queryMaintenanceIsStart(list.get(arg2));
	}
	
	/**
	 * 查询维保任务是否开始
	 * @param
	 * @author: ssu
	 * @date: 2015-6-17 下午7:41:29
	 */
	private void queryMaintenanceIsStart(final MaintenanceTaskListDetail taskDetail) {
		if (null != mLoginDetail) {
			new QueryMaintenanceTaskIsStartTask(this, R.string.loading, taskDetail.getPlanId(), new IResultListener<QueryMaintenanceTaskIsStart>() {

				@Override
				public void onSuccess(QueryMaintenanceTaskIsStart result) {
					if (null != result) {
						
						if (result.getFlag()) { // 当前日期在计划维保时间段内
							//提前查询 该planid是否 已经签到过，如果已经签到过无需 弹出下一个签到界面。
							startMode = result.getStartMode();
							accept(ACCEPT_QUERY);
							
						/*							
							Intent intent =new Intent(MaintenanceTaskListActivity.this, SignActivity.class);
							intent.putExtra("planId",taskDetail.getPlanId());
							intent.putExtra("startMode",result.getStartMode());
							intent.putExtra("locationAddress",taskDetail.getLocationAddress());
							intent.putExtra("maintainCycle",taskDetail.getMaintainCycle());
							startActivity(intent);
						*/
						
	//						Map<String, Serializable> map = new HashMap<String, Serializable>();
	//						if (result.getStartMode()) { //维保任务已开始
	//							map.put("planId", taskDetail.getPlanId());
	//							PageTurnUtil.gotoActivity(MaintenanceTaskListActivity.this, StartMaintenanceTaskDetailsActivity.class, false, map);
	//						} else { //维保任务未开始
	//							map.put("planId", taskDetail.getPlanId());
	//							map.put("locationAddress", taskDetail.getLocationAddress());
	//							map.put("maintainCycle", taskDetail.getMaintainCycle());
	//							PageTurnUtil.gotoActivity(MaintenanceTaskListActivity.this, StartMaintenanceActivity.class, false, map);
	//						}
							
							
						} else {
							showMessage(R.string.alarm, R.string.maintenance_task_no_start);
						}
						
					} else {
						CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.no_data);
					}
				}

				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.network_unavailable);
				}
				
			}).execute();
		}
	}
	
	/**
	 * 显示对话框内容
	 * @param
	 * @author: ssu
	 * @date: 2015-6-9 下午3:43:49
	 */
	private void showMessage(int title, int message) {
		mDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDialog.dismiss();
			}
		}).create();
		mDialog.setTitle(title);
		mDialog.setMessage(getText(message));
		mDialog.setCancelable(false);
		mDialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		default:
			break;
		}
	}

	/**
	 * 开始刷新
	 * 
	 * @author pyyang
	 */
	@Override
	public void onRefresh(PullToRefreshLoadView pullToRefreshLayout, int state) {
		mCurrentPage = 1;
		LogUtils.d(LOG_TAG, "onRefresh", "mCurrentPage:" + mCurrentPage);
		LogUtils.d(LOG_TAG, "onRefresh", "state:" + state);
		if (null != mLoginDetail) {
			new MaintenanceTaskListTask(this, mInputDate, mCurrentPage, mCurrentRows, null, "planEndDate", "desc", R.string.loading, new IResultListener<MaintenanceTaskList>() {
				
				@Override
				public void onSuccess(MaintenanceTaskList result) {
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						List<MaintenanceTaskListDetail> tempList = result.getRows();
						Message msg = new Message();
						msg.what = 1;
						msg.obj = tempList;
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.no_data);
					}
					mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.SUCCEED);
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.network_unavailable);
					mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.FAIL);
				}
			}).execute();
		}
	}

	@Override
	public void onLoadMore(PullToRefreshLoadView pullToRefreshLayout, int state) {
		mCurrentPage = 1;
		LogUtils.d(LOG_TAG, "onLoadMore", "mCurrentPage:" + mCurrentPage);
		LogUtils.d(LOG_TAG, "onLoadMore", "state:" + state);
		if (null != mLoginDetail) {
			String singDate = list.get(list.size() - 1).getPlanBeginDate();
			if ("尚未维保".equals(singDate)) {
				singDate = null;
				++mCurrentPage;
			}
			new MaintenanceTaskListTask(this, mInputDate, mCurrentPage, mCurrentRows, singDate, "planEndDate", "desc", R.string.loading, new IResultListener<MaintenanceTaskList>() {
				
				@Override
				public void onSuccess(MaintenanceTaskList result) {
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						List<MaintenanceTaskListDetail> tempList = result.getRows();
						Message msg = new Message();
						msg.what = 0;
						msg.obj = tempList;
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.no_data);
						--mCurrentPage;
					}
					mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.SUCCEED);
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(MaintenanceTaskListActivity.this, R.string.network_unavailable);
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
	protected void onDestroy() {
		if (null != mPullToRefreshLoadView) {
			mPullToRefreshLoadView.destory();
			mPullToRefreshLoadView = null;
		}
		super.onDestroy();
	}
	
	@Override
	public void netResultFailed(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultFailed(result, request);
	}
	
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultSuccess(result, request);
		
		switch (request.getCode()) {
		case ACCEPT_QUERY:
			dealQuery(result);
			break;
		}
		
	}
	private void dealQuery(String result){
		try {
			Log.e(TAG	, "result："+result);
			
			JSONObject jo=new JSONObject(result).getJSONObject("object");
			Gson gson =new Gson();
			mListBean=gson.fromJson(jo.getJSONArray("maintainContent").toString(),new TypeToken<ArrayList<MaintenanceBean>>() {}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(Tool.isNullList(mListBean)){
			proDismiss();
			Tool.toastShow(this, "暂无数据");
			return ;
		}
		
		boolean isSigned = false;
		//如果已经签到过的，不用弹出 SignActivity界面。
		for (MaintenanceBean bean : mListBean) {
			String strVal1 = bean.getStrVal1();
			if(strVal1!=null && strVal1.equals("LIFTMAINTAINSIGN") && bean.getStatusCode()!=null && bean.getStatusCode().equals("0")){
				isSigned = true;
				break;
			}
		}
		proDismiss();
		
		if(isSigned){//直接跳转。
			Log.v(TAG, "已经签到过了，直接跳转到下下个页面");
			dealCommit();  
		}else{//显示签到activity；
			Intent intent =new Intent(MaintenanceTaskListActivity.this, SignActivity.class);
			intent.putExtra("planId",mBean.getPlanId());
			intent.putExtra("startMode",startMode);
			intent.putExtra("locationAddress",mBean.getLocationAddress());
			intent.putExtra("maintainCycle",mBean.getMaintainCycle());
			startActivity(intent);
		}
	}
	
	private void dealCommit(){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("planId", mBean.getPlanId());
		if(startMode){//维保任务已开始
			PageTurnUtil.gotoActivity(this, StartMaintenanceTaskDetailsActivity.class, false, map);
		}else{
			map.put("locationAddress", mBean.getLocationAddress());
			map.put("maintainCycle", mBean.getMaintainCycle());
			PageTurnUtil.gotoActivity(this, StartMaintenanceActivity.class, false, map);
		}
	}
	
	private void accept(int state){
		RequestSign rBody=new RequestSign();
		switch (state) {
		case ACCEPT_QUERY:
			rBody.setPlanId(mBean.getPlanId()+"");
			setmDismissPro(false);
			getData(new Request(rBody, "/apps/biz/liftMaintainContentController/startLiftMaintainPlan",ACCEPT_QUERY),true);
			break;
		}
		
	}

}
