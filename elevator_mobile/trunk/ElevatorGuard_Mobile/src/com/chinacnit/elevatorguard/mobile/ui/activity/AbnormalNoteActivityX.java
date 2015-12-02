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
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.SubmitMaintenanceItemTask;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;

/**
 * 异常备注 界面
 * 
 * @author pyyang
 * @date 创建时间：2015年5月26日 下午4:24:10
 * 
 */
public class AbnormalNoteActivityX extends BaseActivity implements
		OnClickListener {
	private LinearLayout back_ll;
	private TextView back_text;
	/** 提交异常备注按钮 */
	private Button mBtnSubmit;
	/** 显示异常备注信息的EditText */
	private EditText mEtShowText;
	
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isShowSubBtn = (Boolean) getIntent().getSerializableExtra("isShowSubBtn");
		if (isShowSubBtn) {
			planId = (Long) getIntent().getSerializableExtra("planId");
			tagId = (String) getIntent().getSerializableExtra("tagId");
			statusCode = (WeiBaoItemStatus) getIntent().getSerializableExtra("statusCode");
		}
		comments = (String) getIntent().getSerializableExtra("comments");
		setContentView(R.layout.abnormal_note_x);
		initView();
	}

	private void initView() {
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_text = (TextView) findViewById(R.id.back_textview);
		back_text.setText(R.string.abnormal_note);
		mEtShowText = (EditText) findViewById(R.id.abnormal_note_show_text);
		mEtShowText.setText(comments);
		mBtnSubmit = (Button) findViewById(R.id.abnormal_note_submit_btn);
		if (!isShowSubBtn) {
			mBtnSubmit.setVisibility(View.GONE);
			mEtShowText.setFocusable(false);
		} else {
			mBtnSubmit.setVisibility(View.VISIBLE);
			mBtnSubmit.setOnClickListener(this);
			mEtShowText.setHint(R.string.input_abnomal_text);
			mEtShowText.setFocusable(true);
		}
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
		default:
			break;
		}
	}
	
	private void submitAbnormalText() {
		final String text = mEtShowText.getText().toString();
		new SubmitMaintenanceItemTask(AbnormalNoteActivityX.this, R.string.loading, planId, tagId, mUserInfo.getUserMobile(), text, WeiBaoItemStatus.ABNORMAL, new IResultListener<Object>() {

			@Override
			public void onSuccess(Object result) {
				CommonToast.showToast(AbnormalNoteActivityX.this, R.string.opeartion_success);
				Intent intent = new Intent();
				intent.putExtra("comments", text);
				intent.putExtra("status", WeiBaoItemStatus.ABNORMAL);
				intent.putExtra("tagId", tagId);
				setResult(RESULT_OK, intent);
				finish();
			}

			@Override
			public void onError(String errMsg) { 
				CommonToast.showToast(AbnormalNoteActivityX.this, R.string.opeartion_fail);
			}
			
		}).execute();
	}
}
