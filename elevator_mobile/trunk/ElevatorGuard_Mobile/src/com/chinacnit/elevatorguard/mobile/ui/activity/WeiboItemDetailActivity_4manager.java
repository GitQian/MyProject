package com.chinacnit.elevatorguard.mobile.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoDetail;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.SubmitMaintenanceItemTask;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
/**
 *  管理员的维保项详情页面的  某一条维保项点进来  到该界面  查看该维保项。
 * @author liucheng  liucheng187@qq.com
 */
public class WeiboItemDetailActivity_4manager extends BaseActivity implements OnClickListener {
	private TextView back_text;
	/** 提交异常备注按钮 */
	private Button mBtnSubmit;
	
	/** 是否显示异常备注提交按钮 */
	private boolean isShowSubBtn;
	/** 计划id */
	private long planId;
	/** 标签id */
	private String tagId;
	/** 状态(0 正常 ; 1 异常) */
	private WeiBaoItemStatus statusCode;
	/** 异常备注 */
	private String comments;
	/** 当前登录用户信息 */
	private UserInfo mUserInfo = ConfigSettings.getUserInfo();

	private TextView tv_name,tv_itemname,tv_time,tv_state;
	private EditText edt_comment;
	private WeiBaoDetail mWeibaoDetail;
	private WeiBaoItem mWeibaoItem;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Bundle b = getIntent().getBundleExtra(Bundle.class.getName());
		mWeibaoItem = (WeiBaoItem) b.getSerializable(WeiBaoItem.class.getName());
		mWeibaoDetail = (WeiBaoDetail) b.getSerializable(WeiBaoDetail.class.getName());
		isShowSubBtn = (Boolean) b.getBoolean("isShowSubBtn");
		
		setContentView(R.layout.abnormal_note);
		initView();
	}

	private void initView() {
		findViewById(R.id.back_ll).setOnClickListener(this);;
		back_text = (TextView) findViewById(R.id.back_textview);
		back_text.setText("维保项详情");
		edt_comment = (EditText) findViewById(R.id.edt_comment);
		edt_comment.setText(comments);
		mBtnSubmit = (Button) findViewById(R.id.abnormal_note_submit_btn);
		if (!isShowSubBtn) {
			mBtnSubmit.setVisibility(View.GONE);
			edt_comment.setFocusable(false);
		} else {
			mBtnSubmit.setVisibility(View.VISIBLE);
			mBtnSubmit.setOnClickListener(this);
			edt_comment.setHint(R.string.input_abnomal_text);
			edt_comment.setFocusable(true);
		}
		
		tv_name = (TextView) findViewById(R.id.tv_name);
		tv_itemname = (TextView) findViewById(R.id.tv_itemname);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_state = (TextView) findViewById(R.id.tv_state);
		
		
//		List<String> names = mWeibaoDetail.getUserAlias();
//		StringBuilder sb = new StringBuilder();
//		if(names!=null && names.size()>0){
//			for (int i = 0; i < names.size(); i++) {
//				if(i!=names.size()-1){
//					sb.append(names.get(i)+",");
//				}else{
//					sb.append(names.get(i));
//				}
//			}
//		}
//		tv_name.setText(sb.toString());
		tv_name.setText(mWeibaoItem.getMaintainUser());
		tv_itemname.setText(mWeibaoItem.getMaintainItem());
		tv_time.setText(mWeibaoItem.getLastUpdateDtm());
//		if(mWeibaoDetail.getMaintainBeginDtm()!=null && mWeibaoDetail.getMaintainEndDtm()!=null && mWeibaoDetail.getMaintainEndDtm().equals(mWeibaoDetail.getMaintainBeginDtm())){
//			tv_time.setText(mWeibaoDetail.getMaintainBeginDtm());
//		}else{
//			tv_time.setText(mWeibaoDetail.getMaintainBeginDtm()+" ~ "+mWeibaoDetail.getMaintainEndDtm());
//		}
		
		tv_state.setText(mWeibaoItem.getStatusCode()==WeiBaoItemStatus.ABNORMAL?"异常":mWeibaoItem.getStatusCode()==WeiBaoItemStatus.NORMAL?"正常":"尚未维保");
		edt_comment.setText(mWeibaoItem.getComments());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.abnormal_note_submit_btn:
			submitAbnormalText();
			break;
		}
	}
	
	private void submitAbnormalText() {
		final String text = edt_comment.getText().toString();
		new SubmitMaintenanceItemTask(WeiboItemDetailActivity_4manager.this, R.string.loading, planId, tagId, mUserInfo.getUserMobile(), text, WeiBaoItemStatus.ABNORMAL, new IResultListener<Object>() {

			@Override
			public void onSuccess(Object result) {
				CommonToast.showToast(WeiboItemDetailActivity_4manager.this, R.string.opeartion_success);
				Intent intent = new Intent();
				intent.putExtra("comments", text);
				intent.putExtra("status", WeiBaoItemStatus.ABNORMAL);
				intent.putExtra("tagId", tagId);
				setResult(RESULT_OK, intent);
				finish();
			}

			@Override
			public void onError(String errMsg) { 
				CommonToast.showToast(WeiboItemDetailActivity_4manager.this, R.string.opeartion_fail);
			}
			
		}).execute();
	}
}
