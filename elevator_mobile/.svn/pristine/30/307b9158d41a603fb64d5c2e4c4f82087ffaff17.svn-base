package com.chinacnit.elevatorguard.mobile.ui.activity;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.RoleType;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.UserType;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.config.PreferencesStore;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.chinacnit.elevatorguard.mobile.util.UserCacheUtil;
import com.chinacnit.elevatorguard.mobile.util.VersionUtil;
import com.ztkj.tool.Tool;

/**
 * 闪屏页
 * 
 * @author ssu
 * @date 2015-5-13 下午6:17:50
 */
public class SplashActivity extends BaseActivity {
	private Handler mHandler = new Handler();
	/** 判断是否第一次安装 */
	private boolean isFirstInstall;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
//		setBottomTextType();
		selectActivity();
	}
	
	/**
	 * 设置闪屏页最下方的字体的风格
	 * @param
	 * @author: ssu
	 * @date: 2015-5-21 上午11:18:52
	 */
	private void setBottomTextType() {
		AssetManager am = getAssets();
		Typeface tf = Typeface.createFromAsset(am, "fonts/MSYHBD.TTF");
//		mSplashBottomText.setTypeface(tf);
	}
	
	/**
	 * 延时跳转Activity
	 * @param
	 * @author: ssu
	 * @date: 2015-5-20 下午4:43:30
	 */
	private void selectActivity() {

		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				skipActivity();
			}
		}, ConfigSettings.SPLASH_DURATION);

	}
	
	/**
	 * 跳转Activity
	 * @param
	 * @author: ssu
	 * @date: 2015-5-21 上午11:23:09
	 */
	private void skipActivity() {
		isFirstInstall = PreferencesStore.getInstance().getIsFirstInstall();
		if (isFirstInstall) {
			PreferencesStore.getInstance().saveIsFirstInstall(false);
			PageTurnUtil.gotoActivity(SplashActivity.this, GuideActivity.class,
					true);
		} else {
			String currentVersionName = VersionUtil
					.getCurrentVersionName(SplashActivity.this);
			String oldVersionName = PreferencesStore.getInstance()
					.getVersionName();
			if (!TextUtils.isEmpty(oldVersionName)) {
				if (currentVersionName != null
						&& !currentVersionName.equals(oldVersionName)) {
					// 覆盖安装老版本，清空登录记录
					UserCacheUtil.clearLoginDetail();
					UserCacheUtil.clearUserInfo();
					ConfigSettings.saveLoginDetail(null);
					ConfigSettings.saveUserInfo(null);
				}
			}
			PreferencesStore.getInstance().saveVersionName(currentVersionName);
			LoginDetail loginDetail = ConfigSettings.getLoginDetail();
			UserInfo userInfo = ConfigSettings.getUserInfo();
			if (null == loginDetail || null == userInfo) {
				PageTurnUtil.gotoActivity(SplashActivity.this,LoginActivity.class, true);
			} else if (loginDetail.isLogin() && null != userInfo) {
				if (userInfo.getCompanyType() == RoleType.MAINTENANCECOMPANY&& userInfo.getUserType() == UserType.MAINTENANCEWORKERS) {
					// 维保工。
					PageTurnUtil.gotoActivity(SplashActivity.this,MaintenanceTaskActivity.class,true);
					finish();
				}else{
					 PageTurnUtil.gotoActivity(SplashActivity.this,MainActivity.class, true);
				}
			}
		}
	}
}
