package com.chinacnit.elevatorguard.mobile.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import asmack.bean.BaseResultInfoVo;
import asmack.utils.ConnectionUtil;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.config.PreferencesStore;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.chinacnit.elevatorguard.mobile.util.UserCacheUtil;
import com.ztkj.service.YTZTService;
import com.ztkj.tool.SharedPreferenceTool;

/**
 * 个人信息
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月3日 上午9:41:10
 */
public class PersonalInfoActivity extends BaseActivity implements OnClickListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(PersonalInfoActivity.class.getSimpleName(), true);
	/** 返回按钮 */
	private LinearLayout back;
	/** 返回按钮文本 */
	private TextView back_textview;
	/** 顶部编辑按钮 */
	private Button editor_btn;
	
	/** 用户名 */
	private TextView mTvUserName;
	/** 用户昵称 */
	private TextView mTvUserNickname;
	/** 用户性别 */
	private TextView mTvUserSex;
	/** 用户手机号 */
	private TextView mTvUserMobile;
	/** 用户生日 */
	private TextView mTvUserBirthday;
	/** 退出登录按钮 */
	private Button mRlLoginOut;
	/** 当前登录用户信息 */
	private UserInfo mUserInfo = ConfigSettings.getUserInfo();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.persona_info);
		initView();
	}

	private void initView() {
		back = (LinearLayout) findViewById(R.id.back_ll);
		back.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.personal_information);
		editor_btn = (Button) findViewById(R.id.search_button);
		editor_btn.setVisibility(View.VISIBLE);
		editor_btn.setBackgroundResource(R.drawable.editor);
		
		mTvUserName = (TextView) findViewById(R.id.person_info_username);
		mTvUserName.setText(mUserInfo.getUserAlias());
		mTvUserNickname = (TextView) findViewById(R.id.person_info_nickname);
		mTvUserNickname.setText(mUserInfo.getRealName());
		mTvUserSex = (TextView) findViewById(R.id.person_info_sex);
		if ("M".equalsIgnoreCase(mUserInfo.getUserGender())) {
			mTvUserSex.setText(R.string.gender_male);
		} else {
			mTvUserSex.setText(R.string.gender_female);
		}
		mTvUserMobile = (TextView) findViewById(R.id.person_info_usermobile);
		mTvUserMobile.setText(mUserInfo.getUserMobile());
		mTvUserBirthday = (TextView) findViewById(R.id.person_info_userbirthday);
		mTvUserBirthday.setText(mUserInfo.getBirthday());
		
		mRlLoginOut = (Button) findViewById(R.id.persona_info_loginout_btn);
		mRlLoginOut.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.persona_info_loginout_btn:
			LogUtils.d(LOG_TAG, "onClick", "do loginout");
			YTZTService.isConn=false;
			new Thread("t_logout"){
				public void run() {
					//Log.e("======", "准备执行注销");
					BaseResultInfoVo v=ConnectionUtil.getInstance(PersonalInfoActivity.this).loginOut();
					//Log.e("======", v.getMsg());
				};
				
			}.start();
			SharedPreferenceTool.putLoginsState(0);
			doLoginOut();
			break;
		default:
			break;
		}
	}

	/**
	 * 退出登录
	 * @param
	 * @author: ssu
	 * @date: 2015-6-19 下午4:38:06
	 */
	private void doLoginOut() {
		UserCacheUtil.clearLoginDetail();
		UserCacheUtil.clearUserInfo();
		ConfigSettings.saveLoginDetail(null);
		ConfigSettings.saveUserInfo(null);
		PreferencesStore.getInstance().putBooleanToKey(PreferencesStore.KEY_IS_CACHED_USER, false);
		if(MainActivity.getInstance()!=null){
			MainActivity.getInstance().finish();
		}
		if(SettingsActivity.getInstance()!=null){
			SettingsActivity.getInstance().finish();
		}
		PageTurnUtil.gotoActivity(PersonalInfoActivity.this, LoginActivity.class, true);
	}
}
