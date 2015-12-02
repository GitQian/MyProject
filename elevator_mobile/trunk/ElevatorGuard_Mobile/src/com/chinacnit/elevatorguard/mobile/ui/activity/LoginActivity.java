package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.IOException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.RoleType;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.UserType;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.config.PreferencesStore;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.GetUserCompanyTypeTask;
import com.chinacnit.elevatorguard.mobile.http.task.impl.UserLoginTask;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.Helper;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.UserCacheUtil;
import com.ztkj.componet.LinearKeyBoard;
import com.ztkj.componet.LinearKeyBoard.onKybdsChangeListener;
import com.ztkj.db.DbOperater;
import com.ztkj.tool.SharedPreferenceTool;
import com.ztkj.tool.Tool;

/**
 * 登录Activity
 * 
 * @author ssu
 * @date 2015-5-20 下午5:34:51
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private static final LogTag LOG_TAG = LogUtils.getLogTag(LoginActivity.class.getSimpleName(), true);
	// 用户名
	private EditText mEtUserName;
	// 用户密码
	private EditText mEtUserPwd;
	// 登录
	private Button mBtnSubmit;
	private RelativeLayout relaTitle;
	private ImageView imgSmall,imgBig;
	private Animation bottomIn,alphaIn,alphaOut;
	private Handler handler =new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				if(relaTitle.getVisibility()==View.VISIBLE){
					relaTitle.setVisibility(View.INVISIBLE);
					relaTitle.startAnimation(alphaOut);
					imgBig.setVisibility(View.VISIBLE);
					startBigAnim();
				}
				break;
			case 1:
				if(relaTitle.getVisibility()!=View.VISIBLE){
					relaTitle.setVisibility(View.VISIBLE);
					relaTitle.startAnimation(alphaIn);
					imgBig.setVisibility(View.GONE);
					imgSmall.startAnimation(bottomIn);
				}
				break;
			default:
				break;
			}
			
			return false;
		}
	});
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initView();
		initAnimation();
	}
	
	private void initView() {
		mEtUserName = (EditText) findViewById(R.id.login_username_edittext);
		mEtUserPwd = (EditText) findViewById(R.id.login_userpwd_edittext);
		imgBig=(ImageView)findViewById(R.id.imgBig);
		imgSmall=(ImageView)findViewById(R.id.imgSmall);
		relaTitle=(RelativeLayout)findViewById(R.id.relaTitle);
		findViewById(R.id.login_submit_btn).setOnClickListener(this);
		LinearKeyBoard linearKeyBoard=(LinearKeyBoard)findViewById(R.id.lineAll);
		linearKeyBoard.setOnkbdStateListener(new onKybdsChangeListener() {
			
			@Override
			public void onKeyBoardStateChange(boolean isShow) {
				// TODO Auto-generated method stub
				if(isShow){
					handler.sendEmptyMessage(1);
				}else{
					handler.sendEmptyMessage(0);
				}
			}
		});
	}
	private void initAnimation(){
		bottomIn=AnimationUtils.loadAnimation(this, R.anim.bottom_in_1000);
		alphaIn=AnimationUtils.loadAnimation(this, R.anim.alpha_in_500);
		alphaOut=AnimationUtils.loadAnimation(this, R.anim.alpha_out_500);
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_submit_btn:
//			testLogin();
			doLogin();
//			getUserCompanyType();
			break;
		}
	}
	private void startBigAnim(){
		TranslateAnimation translateAnimation = new TranslateAnimation(-relaTitle.getWidth()/2-imgBig.getWidth(),0, -imgBig.getTop()-imgBig.getHeight(), 0);
		ScaleAnimation scaleAnimation =new ScaleAnimation(0.25f, 1f, 0.25f, 1f,1f,1f);
		translateAnimation.setInterpolator(new OvershootInterpolator(0.5f));
		
		AnimationSet setBig = new AnimationSet(false);
		setBig.addAnimation(translateAnimation);
		setBig.addAnimation(scaleAnimation);
		setBig.setDuration(500);
		
		imgBig.startAnimation(setBig);
	}
//	private void testLogin() {
//		String userName = mEtUserName.getText().toString().trim();
//		String userPwd = mEtUserPwd.getText().toString().trim();
//		if (isVaild(userName, null)) {
//			UserInfo userInfo = new UserInfo();
//			if (userName.equals("test1")) {
//				userInfo.setCompanyType(RoleType.QUALITY);
//			} else if (userName.equals("test2")) {
//				userInfo.setCompanyType(RoleType.MAINTENANCECOMPANY);
//			} else if (userName.equals("test3")) {
//				userInfo.setCompanyType(RoleType.MAINTENANCEWORKER);
//			} else if (userName.equals("test4")) {
//				userInfo.setCompanyType(RoleType.PROPERTYCOMPANY);
//			}
//			try {
////				UserCacheUtil.saveCachedUser(userInfo);
////				ConfigSettings.saveLoginDetail(userInfo);
//				PreferencesStore.getInstance().putBooleanToKey(PreferencesStore.KEY_IS_CACHED_USER, true);
//				CommonToast.showToast(LoginActivity.this, R.string.login_success);
//				PageTurnUtil.gotoActivity(LoginActivity.this, MainActivity.class, true);
//			} catch (Exception e) {
//				LogUtils.e(LOG_TAG, "testLogin", e);
//				CommonToast.showToast(this, "登录失败!");
//			}
//		}
//	}

	/**
	 * 登录
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-21 下午1:57:40
	 */
	private void doLogin() {
		String userName = mEtUserName.getText().toString().trim();
		final String userPwd = mEtUserPwd.getText().toString().trim();
		String mac = Helper.getMacAddress();
		if (isVaild(userName, userPwd)) {
			new UserLoginTask(this, R.string.loading, userName, userPwd, mac,
					new IResultListener<LoginDetail>() {
						@Override
						public void onSuccess(LoginDetail result) {
							if (null != result && result.isLogin()) {
								DbOperater.reset();
								SharedPreferenceTool.putUid(result.getUid());
								SharedPreferenceTool.putPwd(userPwd);
								SharedPreferenceTool.putLoginsState(1);
								Tool.startMyService(LoginActivity.this);
								getUserCompanyType(result);
							} else {
								CommonToast.showToast(LoginActivity.this, R.string.login_fail);
							}
						}
						
						@Override
						public void onError(String errMsg) {
							LogUtils.e(LOG_TAG, "onError", "登录失败:" + errMsg);
							CommonToast.showToast(LoginActivity.this, getResources().getString(R.string.login_fail) + errMsg); 
						}
					}).execute();
		}
	}
	
	/**
	 * 获取用户信息
	 * @param
	 * @author: ssu
	 * @date: 2015-6-4 下午4:28:21
	 */
	private void getUserCompanyType(final LoginDetail mLoginDetail) {
		if (mLoginDetail != null) {
			new GetUserCompanyTypeTask(this, R.string.loading, mLoginDetail, new IResultListener<UserInfo>() {

				@Override
				public void onSuccess(UserInfo result) {
					Log.e("==-===dddd", "result:"+result);
					
					if (result != null && result.getCompanyType() == RoleType.MAINTENANCECOMPANY && result.getUserType() == UserType.MAINTENANCEWORKERS) {
						try{
							UserCacheUtil.saveLoginDetail(mLoginDetail);
							UserCacheUtil.saveUserInfo(result);
							ConfigSettings.saveLoginDetail(mLoginDetail);
							ConfigSettings.saveUserInfo(result);
							PreferencesStore.getInstance().putBooleanToKey(PreferencesStore.KEY_IS_CACHED_USER, true);
							CommonToast.showToast(LoginActivity.this, R.string.login_success);
							// 维保工。
							Tool.startActivity(LoginActivity.this, MaintenanceTaskActivity.class);
							finish();
						}catch(Exception e){ 
							e.printStackTrace();
						}
						
					} else if (result != null && result.getCompanyType() != RoleType.NOROLE) {
						// 保存登录状态
						try {
							UserCacheUtil.saveLoginDetail(mLoginDetail);
							UserCacheUtil.saveUserInfo(result);
							ConfigSettings.saveLoginDetail(mLoginDetail);
							ConfigSettings.saveUserInfo(result);
							PreferencesStore.getInstance().putBooleanToKey(PreferencesStore.KEY_IS_CACHED_USER, true);
							CommonToast.showToast(LoginActivity.this, R.string.login_success);
							LogUtils.d(LOG_TAG, "onSuccess", "userName:" + mLoginDetail.getUserName());
							LogUtils.d(LOG_TAG, "onSuccess", "uId:" + mLoginDetail.getUid());
							LogUtils.d(LOG_TAG, "onSuccess", "sId:" + mLoginDetail.getSid());
							LogUtils.d(LOG_TAG, "onSuccess", "userRole:" + result.getCompanyType());
							
							Tool.startActivityClearTop(LoginActivity.this, MainActivity.class);
							finish();
//							PageTurnUtil.gotoActivity(LoginActivity.this, MainActivity.class, true);
						} catch (IOException e) {
							LogUtils.e(LOG_TAG, "doLogin", "login error.");
						}
					} else {
						CommonToast.showToast(LoginActivity.this, R.string.login_fail);
					}
				}

				@Override
				public void onError(String errMsg) {
					LogUtils.e(LOG_TAG, "onError", "登录失败:" + errMsg);
					CommonToast.showToast(LoginActivity.this, R.string.login_fail); 
				}
			}).execute();
		}
	}

	private boolean isVaild(String userId, String userPwd) {
		if (TextUtils.isEmpty(userId)) {
			CommonToast.showToast(this, R.string.userid_empty_alarm);
			return false;
		}
		if (TextUtils.isEmpty(userPwd)) {
			CommonToast.showToast(this, R.string.pwd_empty_alarm);
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		LogUtils.e(LOG_TAG, "onKeyDown", "execute onKeyDown method");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			System.exit(0);
		}
		return true;
	}
}
