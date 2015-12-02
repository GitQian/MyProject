package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.Lift;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.LiftTask;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
/**
 * 电梯详情 界面
 * 
 * @author pyyang
 * @date 创建时间：2015年5月26日 下午3:53:55
 *
 */
public class LiftDetailsActivity extends BaseActivity implements OnClickListener {
	private LinearLayout back_ll;
	private TextView back_textview;
	private long liftId;
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	
	private Map<String, Object> mLift;
	private TextView mWeiBaoCompany;
	private TextView mWuYeCompany;
	private TextView mBrandTextView;
	private TextView mFactoryDateTextView;
	private TextView tvPosition;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				show();
				break;
			}
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		liftId = getIntent().getLongExtra("liftId", 0);
		setContentView(R.layout.lift_details);
		initView();
		setData();
	}
	
	private void initView() {
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.lift_detail);
	}
	
	private void setData() {
		if (null != mLoginDetail) {
			new LiftTask(this, liftId, R.string.loading, new IResultListener<Lift>() {
				@Override
				public void onSuccess(Lift result) {
					if (result != null) {
						mLift = new HashMap<String, Object>();
						mLift.put("mWeiBao",result.getMaintainCompanyName());
						mLift.put("mWuYe",result.getPropertyCompanyName());
						mLift.put("mBrand",result.getLiftBrandName());
						mLift.put("mFactoryDate",result.getFactoryDate());
						mLift.put("locationAddress",result.getLocationAddress());
						mHandler.sendEmptyMessage(0);
					} else {
						CommonToast.showToast(LiftDetailsActivity.this, R.string.http_parse_error);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(LiftDetailsActivity.this, R.string.network_unavailable);
				}
			}).execute();
		}
	}
	
	protected void show() {
		mWeiBaoCompany = (TextView)findViewById(R.id.wb_company);
		mWeiBaoCompany.setText(mLift.get("mWeiBao").toString());
		mWuYeCompany = (TextView) findViewById(R.id.wy_company);
		mWuYeCompany.setText(mLift.get("mWuYe").toString());
		mBrandTextView = (TextView) findViewById(R.id.brand);
		mBrandTextView.setText(mLift.get("mBrand").toString());
		mFactoryDateTextView = (TextView) findViewById(R.id.date_in_produced);
		mFactoryDateTextView.setText(mLift.get("mFactoryDate").toString());
		tvPosition = (TextView) findViewById(R.id.tvPosition);
		tvPosition.setText(mLift.get("locationAddress").toString());
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
}
