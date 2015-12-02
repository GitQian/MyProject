package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.WeibaoRecordDetailAdapter;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoDetail;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoRecordBean;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.WeiBaoDetailTask;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView.OnRefreshLoadListener;
import com.chinacnit.elevatorguard.mobile.ui.view.PullableListView;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.ztkj.tool.Tool;

/**
 * 维保记录详情 界面
 * @author liucheng  liucheng187@qq.com
 */
public class WeibaoRecordDetailActivity extends BaseActivity implements OnClickListener, OnRefreshLoadListener, OnItemClickListener {
	private WeibaoRecordDetailActivity mActivity;
	private LinearLayout back_ll;
	private TextView back_textView;
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	private WeibaoRecordDetailAdapter mAdapter;
	private PullToRefreshLoadView mPullToRefreshLoadView;
	private PullableListView mListView;
	
	/** 维保开始时间 */
	private TextView mTvWeibaoStartDate;
	/** 维保结束日期 */
	private TextView mTvWeibaoEndDate;
	/** 显示维保人员列表的第一个维保人员名称 */
	private TextView mTvWeibaoName;
	/** 计划id */
	private long planId;
	private WeiBaoDetail mBean;
	private WeiBaoRecordBean mWeiBaoRecordBean;
	
	public WeibaoRecordDetailActivity() {
		mActivity = this;
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				showData();
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mWeiBaoRecordBean  = (WeiBaoRecordBean) getIntent().getBundleExtra(Bundle.class.getName()).getSerializable(WeiBaoRecordBean.class.getName());
		planId  = mWeiBaoRecordBean.getPlanId();
		
		setContentView(R.layout.weibao_record_detail);
		initView();
		initData();
		setData();
	}

	private void initData() {
		mTvWeibaoStartDate.setText(mWeiBaoRecordBean.getMaintenanceTime());  // 
		mTvWeibaoEndDate.setText("");  //他妈的，，有时候 给两个字段有时候一个字段。
		mTvWeibaoName.setText(mWeiBaoRecordBean.getUserAlias());		
	}

	private void initView() {
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_textView = (TextView) findViewById(R.id.back_textview);
		back_textView.setText("维保记录详情");
		mTvWeibaoStartDate = (TextView) findViewById(R.id.wb_startdate);
		mTvWeibaoEndDate = (TextView) findViewById(R.id.wb_enddate);
		findViewById(R.id.wb_name_rl_layout).setOnClickListener(this);
		mTvWeibaoName = (TextView) findViewById(R.id.wb_name);
	}
	
	private void setData() {
		if (null != mLoginDetail && planId != 0) {
			
			Log.e(getClass().getSimpleName(), "planId:"+planId);
			new WeiBaoDetailTask(this, R.string.loading, planId, new IResultListener<WeiBaoDetail>() {
				
				@Override
				public void onSuccess(WeiBaoDetail result) {
					if (null != result) {
						Log.e("====", "返回的WeiBaoDetail："+result);
						mBean = result;
						mHandler.sendEmptyMessage(0);
					} else { 
						com.ztkj.tool.ToastUtil.getInstance(mActivity).showMsg("服务器返回数据无效", 1);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(WeibaoRecordDetailActivity.this, R.string.network_unavailable);
				}
			}).execute();
		}
	}
	
	private void showData() {
		mTvWeibaoStartDate.setText(mBean.getMaintainBeginDtm());
		mTvWeibaoEndDate.setText(mBean.getMaintainEndDtm());
		
		//如果开始维保日期和结束维保日期为同一天，那么  隐藏 分隔符号和结束维保日期。
		if(mBean.getMaintainBeginDtm()!=null && mBean.getMaintainEndDtm()!=null && mBean.getMaintainBeginDtm().equals(mBean.getMaintainEndDtm())){
			findViewById(R.id.weibao_detail_separator).setVisibility(View.INVISIBLE);
			mTvWeibaoEndDate.setVisibility(View.INVISIBLE);
		}
		
		StringBuilder sb = new StringBuilder();
		if(mBean.getUserAlias()!=null && mBean.getUserAlias().size()>0){
			for (int i = 0; i < mBean.getUserAlias().size(); i++) {
				if(i!=mBean.getUserAlias().size()-1)
					sb.append(mBean.getUserAlias().get(i)+",");
				else
					sb.append(mBean.getUserAlias().get(i));
			}
		}
		
		mTvWeibaoName.setText(sb.toString());
		showList();
	}
	
	private void showList() {
		if (mAdapter == null && null != mBean && mBean.getMaintainItems() != null && mBean.getMaintainItems().size() > 0) {
			mPullToRefreshLoadView = (PullToRefreshLoadView) findViewById(R.id.weibao_detail_person_listview);
			mPullToRefreshLoadView.setOnRefreshLoadListener(WeibaoRecordDetailActivity.this);
			mListView = new PullableListView(WeibaoRecordDetailActivity.this, false, false);
			mPullToRefreshLoadView.setRefreshLoadView(WeibaoRecordDetailActivity.this, mListView); 
			mListView.setOnItemClickListener(this);
			mAdapter = new WeibaoRecordDetailAdapter( mBean.getMaintainItems());
			mListView.setAdapter(mAdapter);
		} else if (mAdapter != null && null != mBean && mBean.getMaintainItems() != null && mBean.getMaintainItems().size() > 0) {
			mAdapter.onDateChange(mBean.getMaintainItems());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.wb_name_rl_layout:
			if(Tool.isNullList(mBean.getUserAlias())){
				Tool.toastShow(mActivity, "暂无维保人");
				return;
			}else{
				for (String i : mBean.getUserAlias()) {
					System.out.println("name: "+i);
				}
			}
			
			Intent intent = new Intent(this, MaintenancePersonListActivity.class);
			intent.putStringArrayListExtra("personList", mBean.getUserAlias() != null && mBean.getUserAlias().size() > 0 ? (ArrayList<String>) mBean.getUserAlias() : null);
			startActivity(intent);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		WeiBaoItem weiBaoItem = (WeiBaoItem) parent.getAdapter().getItem(position);
		if(weiBaoItem.getStatusCode()!= WeiBaoItemStatus.NORMAL && weiBaoItem.getStatusCode()!=WeiBaoItemStatus.ABNORMAL){//除了“正常”和“异常”其他状态不可查看详情。
			return;
		}
		Bundle b = new Bundle();
		b.putBoolean("isShowSubBtn", false);
		b.putLong("planId", planId);
		b.putSerializable(WeiBaoItem.class.getName(), weiBaoItem);
		b.putSerializable(WeiBaoDetail.class.getName(), mBean);
		Tool.startActivity(mActivity, WeiboItemDetailActivity_4manager.class, b);
	}

	@Override
	public void onRefresh(PullToRefreshLoadView pullToRefreshLayout, int state) {
		
	}

	@Override
	public void onLoadMore(PullToRefreshLoadView pullToRefreshLayout, int state) {
	}

	@Override
	public void onLoadScrollFinish(PullToRefreshLoadView pullToRefreshLayout,	int state) {
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
