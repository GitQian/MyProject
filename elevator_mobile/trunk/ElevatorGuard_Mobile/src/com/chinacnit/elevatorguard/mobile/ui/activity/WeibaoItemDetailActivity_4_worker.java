package com.chinacnit.elevatorguard.mobile.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.StartMaintenanceTaskDetail;
import com.chinacnit.elevatorguard.mobile.bean.StartMaintenanceTaskItem;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.ztkj.base.business.BaseActivity;

/**
 * 维保的Listview的item点进来的页面。  给 维保人员权限用户的界面
 * @author liucheng  liucheng187@qq.com
 */
public class WeibaoItemDetailActivity_4_worker extends BaseActivity{
	private TextView tv_name,tv_itemname,tv_time,tv_state;
	private EditText edt_comment;
	private UserInfo mUserInfo = ConfigSettings.getUserInfo();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weiboitemdetail);
		initView();
		initData();
		
	}

	private void initData() {
		tv_name.setText(mUserInfo.getRealName());
		
		Bundle b = getIntent().getBundleExtra(Bundle.class.getName());
		StartMaintenanceTaskItem item = (StartMaintenanceTaskItem)b.getSerializable(StartMaintenanceTaskItem.class.getName());
		StartMaintenanceTaskDetail mResult = (StartMaintenanceTaskDetail) b.getSerializable(StartMaintenanceTaskDetail.class.getName());
		
		tv_time.setText(mResult.getPlanBeginDate()+"~"+mResult.getPlanEndDate());
		tv_itemname.setText(item.getMaintainItem());
		tv_state.setText(item.getStatusCode()==WeiBaoItemStatus.ABNORMAL?"异常":item.getStatusCode()==WeiBaoItemStatus.NORMAL?"正常":"尚未维保");
		edt_comment.setText(item.getComments());
	}
	
	private void initView() {
		findViewById(R.id.back_ll).setOnClickListener(this);
		TextView back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText("维保项详情");
		
		edt_comment = (EditText) findViewById(R.id.edt_comment);
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_itemname = (TextView) findViewById(R.id.tv_itemname);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_state = (TextView) findViewById(R.id.tv_state);
		
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_ll:
			finish();
			break;

		default:
			break;
		}
	}
}
