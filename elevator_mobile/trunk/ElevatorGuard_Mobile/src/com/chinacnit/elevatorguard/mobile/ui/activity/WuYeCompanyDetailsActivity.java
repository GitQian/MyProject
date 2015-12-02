package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.CompanyDetail;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.CompanyDetailTask;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;

/**
 * 物业公司详情
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月1日 下午5:01:53
 */
public class WuYeCompanyDetailsActivity extends BaseActivity implements
		OnClickListener {
	private RelativeLayout title_right_rl;
	private TextView back_textview;
	/** 公司地址 */
	private TextView mTvCompanyAddress;
	/** 注册时间 */
	private TextView mTvRegDate;
	/** 电梯数量 */
	private TextView mTvLiftCount;
	/** 公司ID */
	private long companyId;
	/** 当前用户登录详情 */
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	private CompanyDetail mCompanyDetail;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				showCompanyDetail();
				break;
			}
		};
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_details);
		companyId = (Long) getIntent().getSerializableExtra("companyId");
		initView();
		setData();
	}

	private void initView() {
		findViewById(R.id.back_ll).setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.wuye_company_details);
		title_right_rl = (RelativeLayout) findViewById(R.id.wb_time_rl);
		title_right_rl.setVisibility(View.GONE);
		
		mTvCompanyAddress = (TextView) findViewById(R.id.company_details_address);
		mTvRegDate = (TextView) findViewById(R.id.company_details_regtime);
		mTvLiftCount = (TextView) findViewById(R.id.company_details_sum);
	}
	
	private void setData() {
		if (mLoginDetail != null && companyId != 0) {
			new CompanyDetailTask(this, companyId, 2, R.string.loading, new IResultListener<CompanyDetail>() {
				
				@Override
				public void onSuccess(CompanyDetail result) {
					if (null != result) {
						mCompanyDetail = result;
						mHandler.sendEmptyMessage(0);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(WuYeCompanyDetailsActivity.this, R.string.network_unavailable);
				}
			}).execute();
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
	
	private void showCompanyDetail() {
		try {
			mTvCompanyAddress.setText(mCompanyDetail.getAddress());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String regDate = mCompanyDetail.getDateTime();
			Date date = sdf.parse(regDate);
			sdf.applyPattern("yyyy-MM-dd");
			mTvRegDate.setText(sdf.format(date));
			mTvLiftCount.setText(String.valueOf(mCompanyDetail.getCount()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
