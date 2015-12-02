package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.CompanyAdapter;
import com.chinacnit.elevatorguard.mobile.bean.CompanyList;
import com.chinacnit.elevatorguard.mobile.bean.CompanyListDetail.CompanyType;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.CompanyListDetail;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.CompanyListTask;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView.OnRefreshLoadListener;
import com.chinacnit.elevatorguard.mobile.ui.view.PullableListView;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 物业公司
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月1日 下午2:51:34
 */
public class WuYeCompanyActivity extends BaseActivity implements
		OnClickListener, OnRefreshLoadListener, OnItemClickListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(WuYeCompanyActivity.class.getSimpleName(), true);
	// 物业公司列表适配器
	private CompanyAdapter pmcAdapter;
	// 带上拉刷新ListView控制器
	private PullToRefreshLoadView mPullToRefreshLoadView;
	// 带上拉刷新ListView
	private PullableListView mListView;
	// 返回按钮
	private LinearLayout back;
	private RelativeLayout title_right_rl;
	// 返回按钮文本
	private TextView back_textview;

	// 当前页数
	private int mCurrentPage = 1;

	// 每页显示的行数
	private int mCurrentRows = 10;

	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	private List<CompanyListDetail> list;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			  switch (msg.what) {   
              case 0:   
            	  showList();
                  break;
			  }
		}
		
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company);
		initView();
		setData();
//		setData1();
//		showList(list);
	}
	
	/**
	 * 界面初始化
	 * 
	 * @author: pyyang
	 * @date 创建时间：2015年6月1日 下午4:01:55
	 */
	private void initView() {
		back = (LinearLayout) findViewById(R.id.back_ll);
		back.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.wuye_company);
		title_right_rl = (RelativeLayout) findViewById(R.id.wb_time_rl);
		title_right_rl.setVisibility(View.GONE);
	}

	private void setData() {
		if (null != mLoginDetail) {
			new CompanyListTask(WuYeCompanyActivity.this, CompanyType.PROPERTYCOMPANY, mCurrentPage, mCurrentRows,
					R.string.loading, new IResultListener<CompanyList>() {
				@Override
				public void onSuccess(CompanyList result) {
					if (result != null && result.getRows() != null && result.getRows().size() > 0) {
						list = result.getRows();
						mHandler.sendEmptyMessage(0);
					} else {
						CommonToast.showToast(WuYeCompanyActivity.this, R.string.no_data);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(WuYeCompanyActivity.this, R.string.network_unavailable);
				}
			}).execute();
		}
	}
	
	private void setData1() {
		list = new ArrayList<CompanyListDetail>();
		for(int i = 0;i<10;i++){
			CompanyListDetail pmc_info = new CompanyListDetail();
			pmc_info.setCompanyName("物业公司");
			list.add(pmc_info);
		}
	}
	
	private void showList() {
		if (pmcAdapter == null && null != list && list.size() > 0) {
			mPullToRefreshLoadView = (PullToRefreshLoadView) findViewById(R.id.company_listview);
			mPullToRefreshLoadView.setOnRefreshLoadListener(WuYeCompanyActivity.this);
			mListView = new PullableListView(WuYeCompanyActivity.this, false, true);
			mPullToRefreshLoadView.setRefreshLoadView(WuYeCompanyActivity.this, mListView); 
			mListView.setOnItemClickListener(this);
			pmcAdapter = new CompanyAdapter(WuYeCompanyActivity.this, list);
			mListView.setAdapter(pmcAdapter);
		} else if (pmcAdapter != null && null != list && list.size() > 0) {
			pmcAdapter.onDateChange(list);
		}
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

	@Override
	public void onRefresh(PullToRefreshLoadView pullToRefreshLayout, int state) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onLoadMore(PullToRefreshLoadView pullToRefreshLayout, int state) {
		++mCurrentPage;
		LogUtils.d(LOG_TAG, "onLoadMore", "mCurrentPage:" + mCurrentPage);
		if (null != mLoginDetail) {
			new CompanyListTask(this, CompanyType.PROPERTYCOMPANY, mCurrentPage, mCurrentRows,
					R.string.loading, new IResultListener<CompanyList>() {
						@Override
						public void onSuccess(CompanyList result) {
							if (result != null && result.getRows() != null && result.getRows().size() > 0) {
								if (list != null) {
									list.addAll(result.getRows());
								} else {
									list = result.getRows();
								}
								mHandler.sendEmptyMessage(0);
							} else {
								CommonToast.showToast(WuYeCompanyActivity.this, R.string.no_data);
								mCurrentPage--;
							}
							mPullToRefreshLoadView.loadmoreFinish(PullToRefreshLoadView.SUCCEED);
						}

						@Override
						public void onError(String errMsg) {
							CommonToast.showToast(WuYeCompanyActivity.this, R.string.network_unavailable);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Map<String, Serializable> parameters = new HashMap<String, Serializable>();
		parameters.put("companyId", list.get(arg2).getCompanyId());
		PageTurnUtil.gotoActivity(WuYeCompanyActivity.this, WuYeCompanyDetailsActivity.class, false, parameters);
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
