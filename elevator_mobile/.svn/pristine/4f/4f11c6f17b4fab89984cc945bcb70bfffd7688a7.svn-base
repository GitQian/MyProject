package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.RoleType;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.UserType;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.config.PreferencesStore;
import com.chinacnit.elevatorguard.mobile.ui.view.ToastView;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.chinacnit.elevatorguard.mobile.util.UserCacheUtil;
import com.ztkj.tool.Tool;

/**
 * 主页面
 * 
 * @author ssu
 * @date 2015-5-18 上午11:39:49
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	private static MainActivity sInstance;
//	private SlidingMenu mLsetMenu;
	// 角色头像
	private ImageView mIvRoleImage;
	// 角色名称
	private TextView mTvRoleText;
	// 用户名称
	
	// 电梯管理
	private LinearLayout mRlElevatorManagement;
	// 数据统计
	private LinearLayout mRlDataStatistics;
	// 物业公司
	private LinearLayout mRlPropertyManagement;
	// 维保公司
	private LinearLayout mRlMaintenanceCompany;
	// 员工管理
	private LinearLayout mRlStaffManagement;
	// 维保任务
	private LinearLayout mRlMaintenanceTask;
    // 更多
	private LinearLayout mRlMore;
	
	// 电梯管理
	private ImageView mIvElevatorManagement;
	// 数据统计
	private ImageView mIvDataStatistics;
	// 物业公司
	private ImageView mIvPropertyManagement;
	// 维保公司
	private ImageView mIvMaintenanceCompany;
	// 员工管理
	private ImageView mIvStaffManagement;
	// 维保任务
	private ImageView mIvMaintenanceTask;
    // 更多
	private ImageView mIvMore;
	
	// 设置
	private RelativeLayout mRlSetting;
	// 消息
	private RelativeLayout mRlMessage;
	
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	private UserInfo mUserInfo = ConfigSettings.getUserInfo();

	private static boolean mBackKeyPressed = false;
	private static Boolean isQuit = false;
	private Timer timer = new Timer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sInstance = this;
		setContentView(R.layout.activity_main);
		initView();
		setRole();
	}

	private void initView() {
//		mLsetMenu = (SlidingMenu) findViewById(R.id.id_menu);
		mIvRoleImage = (ImageView) findViewById(R.id.main_top_role_imageview);
		mTvRoleText = (TextView) findViewById(R.id.main_top_role_textview);
		
		mRlElevatorManagement = (LinearLayout) findViewById(R.id.main_center_elevator_management_rl);
		mRlDataStatistics = (LinearLayout) findViewById(R.id.main_center_data_statistics_rl);
		mRlPropertyManagement = (LinearLayout) findViewById(R.id.main_center_property_management_rl);
		mRlMaintenanceCompany = (LinearLayout) findViewById(R.id.main_center_maintenance_company_rl);
		mRlStaffManagement = (LinearLayout) findViewById(R.id.main_center_staff_management_rl);
		mRlMaintenanceTask = (LinearLayout) findViewById(R.id.main_center_maintenance_task_rl);
		mRlMore = (LinearLayout) findViewById(R.id.main_center_more_rl);
		
		mRlElevatorManagement.setOnClickListener(this);
		mRlDataStatistics.setOnClickListener(this);
		mRlPropertyManagement.setOnClickListener(this);
		mRlMaintenanceCompany.setOnClickListener(this);
		mRlStaffManagement.setOnClickListener(this);
		mRlMaintenanceTask.setOnClickListener(this);
		mRlMore.setOnClickListener(this);
		
		
//		mIvElevatorManagement = (ImageView) findViewById(R.id.main_center_elevator_management_imageview);
//		mIvElevatorManagement.setOnClickListener(this);
//		mIvDataStatistics = (ImageView) findViewById(R.id.main_center_data_statistics_imageview);
//		mIvDataStatistics.setOnClickListener(this);
//		mIvPropertyManagement = (ImageView) findViewById(R.id.main_center_property_management_imageview);
//		mIvPropertyManagement.setOnClickListener(this);
//		mIvMaintenanceCompany = (ImageView) findViewById(R.id.main_center_maintenance_company_imageview);
//		mIvMaintenanceCompany.setOnClickListener(this);
//		mIvStaffManagement = (ImageView) findViewById(R.id.main_center_staff_management_imageview);
//		mIvStaffManagement.setOnClickListener(this);
//		mIvMaintenanceTask = (ImageView) findViewById(R.id.main_center_maintenance_task_imageview);
//		mIvMaintenanceTask.setOnClickListener(this);
//		mIvMore = (ImageView) findViewById(R.id.main_center_more_imageview);
//		mIvMore.setOnClickListener(this);
		
		findViewById(R.id.main_bottom_setting_rl).setOnClickListener(this);
		findViewById(R.id.main_bottom_message_rl).setOnClickListener(this);
	}
	
	/**
	 * 不同用户角色显示对应功能按钮
	 * @param
	 * @author: ssu
	 * @date: 2015-5-27 下午3:20:36
	 */
	private void setRole() {
		if (mLoginDetail == null) {
			return;
		}
		String userName=mLoginDetail.getUserName();
		if (mUserInfo.getCompanyType() == RoleType.QUALITY) { //质监局
			mIvRoleImage.setImageResource(R.drawable.main_top_role_icon_quality);
			mTvRoleText.setText(userName+" - "+getString(R.string.role_name_quality));
			
			mRlPropertyManagement.setVisibility(View.VISIBLE);
			mRlMaintenanceCompany.setVisibility(View.VISIBLE);
		} else if (mUserInfo.getCompanyType() == RoleType.MAINTENANCECOMPANY) { //维保公司
			if (mUserInfo.getUserType() == UserType.ADMINISTRATOR) { // 维保管理员
				mIvRoleImage.setImageResource(R.drawable.main_top_role_icon_maintenanceadmin);
				mTvRoleText.setText(userName+" - "+getString(R.string.role_name_maintenanceadmin));
				
				mRlPropertyManagement.setVisibility(View.VISIBLE);
				mRlStaffManagement.setVisibility(View.VISIBLE);
			} else if (mUserInfo.getUserType() == UserType.MAINTENANCEWORKERS) { //维保工
				mIvRoleImage.setImageResource(R.drawable.main_top_role_icon_maintenanceworkers);
				mTvRoleText.setText(userName+" - "+getString(R.string.role_name_maintenanceworkers));
				
				mRlPropertyManagement.setVisibility(View.VISIBLE);
				mRlMaintenanceTask.setVisibility(View.VISIBLE);
			} else if (mUserInfo.getUserType() == UserType.ADMINANDWORKERS) { //维保管理员和维保工
				mIvRoleImage.setImageResource(R.drawable.main_top_role_icon_maintenanceadmin);
				mTvRoleText.setText(userName+" - "+getString(R.string.role_name_maintenanceadmin));
				
				mRlPropertyManagement.setVisibility(View.VISIBLE);
				mRlStaffManagement.setVisibility(View.VISIBLE);
			}
			
		} else if (mUserInfo.getCompanyType() == RoleType.INSPECTORATE) { //监察局
			mIvRoleImage.setImageResource(R.drawable.main_top_role_icon_quality);
			mTvRoleText.setText(userName+" - "+getString(R.string.role_name_inspectorate));
			
			mRlPropertyManagement.setVisibility(View.VISIBLE);
			mRlMaintenanceCompany.setVisibility(View.VISIBLE);
		} else if (mUserInfo.getCompanyType() == RoleType.PROPERTYCOMPANY){ //物业公司
			mIvRoleImage.setImageResource(R.drawable.main_top_role_icon_propertycompany);
			mTvRoleText.setText(userName+" - "+getString(R.string.role_name_propertycompany));
			
			mRlPropertyManagement.setVisibility(View.GONE);
			mRlMaintenanceCompany.setVisibility(View.VISIBLE);
			mRlMore.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 获得MainActivity实例
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-12 下午4:56:54
	 */
	public static MainActivity getInstance() {
		return sInstance;
	}

	// public void toggleMenu(View view){
	// mLsetMenu.toggle();
	// }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_center_elevator_management_rl: // 电梯管理
			PageTurnUtil.gotoActivity(MainActivity.this, LiftListingActivity.class, false, null);
			break;
		case R.id.main_center_data_statistics_rl: // 数据统计
			break;
		case R.id.main_center_property_management_rl: // 物业公司 
			PageTurnUtil.gotoActivity(MainActivity.this, WuYeCompanyActivity.class, false);
			break;
		case R.id.main_center_maintenance_company_rl: // 维保公司
			PageTurnUtil.gotoActivity(MainActivity.this, WeiBaoCompanyActivity.class, false);
			break;
		case R.id.main_center_staff_management_rl: //员工管理
			Tool.startActivity(this, ManagerEmployee.class);
			break;
		case R.id.main_center_maintenance_task_rl: // 维保任务
			PageTurnUtil.gotoActivity(MainActivity.this, MaintenanceTaskListActivity.class, false);
			break;
		case R.id.main_center_more_rl: // 更多
			break;
		case R.id.main_bottom_setting_rl: //设置
			PageTurnUtil.gotoActivity(MainActivity.this, SettingsActivity.class, false);
			break;
		case R.id.main_bottom_message_rl: //消息
			PageTurnUtil.gotoActivity(MainActivity.this, MessageNetActivity.class, false);
			break;
		default:
			break;
		}
	}

	// 处理后一个activity返回来的数据
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			// Bundle b = data.getExtras();
			// String str = b.getString("listen");
			break;
		default:
			break;
		}
	}

	/**
	 * 设置按钮onClick
	 * 
	 */
	public void Setting_Click(View v) {
//		mLsetMenu.toggle();
	}

	/**
	 * messae按钮onClick
	 * 
	 */
	public void Message_Cilck(View v) {
//		mLsetMenu.toggle();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isQuit == false) {
				isQuit = true;
				Tool.toastShow(getBaseContext(), getString(R.string.press_again_to_exit));
				TimerTask task = null;
				task = new TimerTask() {
					@Override
					public void run() {
						isQuit = false;
					}
				};
				timer.schedule(task, 2 * 1000);
			} else {
				finish();
				System.exit(0);
			}
		}
		return true;
	}
}
