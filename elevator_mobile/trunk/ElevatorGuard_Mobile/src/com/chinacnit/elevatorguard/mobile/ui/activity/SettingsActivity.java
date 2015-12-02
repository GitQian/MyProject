package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;

/**
 * 设置界面
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月2日 下午4:09:29
 */
public class SettingsActivity extends BaseActivity implements OnClickListener {
	private static SettingsActivity sInstance;
	private UserInfo mUserInfo = ConfigSettings.getUserInfo();
	/** 顶部返回按钮 */
	private LinearLayout mLlBack;
	/** 顶部返回按钮文本 */
	private TextView mTvBackText;
	private RelativeLayout title_right_rl;

	/** 用户姓名 */
	private TextView mTvUserName;
	/** 用户手机号 */
	private TextView mTvUserMobile;
	/** 个人信息 */
	private RelativeLayout mPeronalInfo;
	/** 关于我们 */
	private RelativeLayout mAboutUs;
	/** 帐号安全 */
	private RelativeLayout mSecurityAccount;
	/** 版本信息 */
	private RelativeLayout mVersionInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		sInstance = this;
		initView();
	}
	
	public static SettingsActivity getInstance() {
		return sInstance;
	}

	private void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.back_ll);
		mLlBack.setOnClickListener(this);
		mTvBackText = (TextView) findViewById(R.id.back_textview);
		mTvBackText.setText(R.string.settings);
		
		title_right_rl = (RelativeLayout) findViewById(R.id.wb_time_rl);
		title_right_rl.setVisibility(View.GONE);

		mTvUserName = (TextView) findViewById(R.id.settings_personal_name_textview);
		mTvUserName.setText(mUserInfo.getUserAlias());
		mTvUserMobile = (TextView) findViewById(R.id.setings_personal_mobile_textview);
		mTvUserMobile.setText(mUserInfo.getUserMobile());

		mPeronalInfo = (RelativeLayout) findViewById(R.id.settings_personal);
		mPeronalInfo.setOnClickListener(this);
		mAboutUs = (RelativeLayout) findViewById(R.id.settings_about_us_rl);
		mAboutUs.setOnClickListener(this);
		mSecurityAccount = (RelativeLayout) findViewById(R.id.settings_use_clauses);
		mSecurityAccount.setOnClickListener(this);
		mVersionInfo = (RelativeLayout) findViewById(R.id.settings_version_info);
		mVersionInfo.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll: // 返回
			PageTurnUtil.goBack(this);
			break;
		case R.id.settings_personal: // 个人信息
			PageTurnUtil.gotoActivity(SettingsActivity.this, PersonalInfoActivity.class, false);
			break;
		case R.id.settings_about_us_rl: // 关于我们
			Map<String, Serializable> map = new HashMap<String, Serializable>();
			map.put("type", "about_us");
			PageTurnUtil.gotoActivity(this, AboutUsAndUseClausesActivity.class, false, map);
			break;
		case R.id.settings_use_clauses: // 用户条款
			Map<String, Serializable> map1 = new HashMap<String, Serializable>();
			map1.put("type", "terms_of_use");
			PageTurnUtil.gotoActivity(this, AboutUsAndUseClausesActivity.class, false, map1);
			break;
		case R.id.settings_version_info: // 版本信息
			PageTurnUtil.gotoActivity(this, VersionInfoActivity.class, false);
			break;
		default:
			break;
		}
	}
}
