package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 开始维保Activity
 * 
 * @author ssu
 * @date 2015-5-26 下午2:29:20
 */
public class StartMaintenanceActivity extends Activity implements OnClickListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(StartMaintenanceActivity.class.getSimpleName(), true);
	private ImageView circle_one;
	private ImageView circle_two;
	private ImageView circle_three;
	private ImageView btn_start_maintenance;
	private Handler mHandler = new Handler();
	
	private LinearLayout back_ll;
	private TextView back_textview;
	
	/** 维保地址 */
	private TextView mTvMaintenanceAddress;
	/** 维保类型 */
	private TextView mTvMaintenanceType;
	
	/** 维保计划 id  */
	private long planId;
	/** 电梯地址 */
	private String locationAddress;
	/** 维保类型 */
	private Cycle maintainCycle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		planId = (Long) getIntent().getSerializableExtra("planId");
		locationAddress = (String) getIntent().getSerializableExtra("locationAddress");
		maintainCycle = (Cycle) getIntent().getSerializableExtra("maintainCycle");
		LogUtils.d(LOG_TAG, "onCreate", "planId:" + planId + ", locationAddress:" + locationAddress + ",maintainCycle:" + maintainCycle);
		setContentView(R.layout.activity_start_maintenance);
		initView();
	}

	private void initView() {
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.maintenance_task);
		
		mTvMaintenanceAddress = (TextView) findViewById(R.id.start_maintenance_address);
		mTvMaintenanceAddress.setText(locationAddress);
		mTvMaintenanceType = (TextView) findViewById(R.id.start_maintenance_type);
		mTvMaintenanceType.setText(Cycle.getValueByKey(maintainCycle) != 0 ? getResources().getString(Cycle.getValueByKey(maintainCycle)) : "");
		
		circle_one = (ImageView) findViewById(R.id.circle_one);
		circle_two = (ImageView) findViewById(R.id.circle_two);
		circle_three = (ImageView) findViewById(R.id.circle_three);
		btn_start_maintenance = (ImageView) findViewById(R.id.btn_start_maintenance);
		btn_start_maintenance.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.btn_start_maintenance:
			startCircularAnima();
			break;
		}
	}

	/**
	 * 开始第一个扩散圆形动画
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-26 下午2:24:28
	 */
	private void startCircleOneAnimat() {
		circle_one.setVisibility(View.VISIBLE);
		AnimationSet annularAnimat = getAnimAnnular(0.5f, 1.0f, 0.5f, 1.0f);
		annularAnimat.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				circle_one.setVisibility(View.GONE);
			}
		});
		circle_one.startAnimation(annularAnimat);
	}

	/**
	 * 开始第二个扩散圆形动画
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-26 下午2:24:58
	 */
	private void startCircleTwoAnimat() {
		circle_two.setVisibility(View.VISIBLE);
		AnimationSet annularAnimat = getAnimAnnular(0.5f, 1.1f, 0.5f, 1.1f);
		annularAnimat.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				circle_two.setVisibility(View.GONE);
			}
		});
		circle_two.startAnimation(annularAnimat);
	}

	/**
	 * 开始第三个扩散圆形动画
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-26 下午2:25:07
	 */
	private void startCircleThreeAnimat() {
		circle_three.setVisibility(View.VISIBLE);
		AnimationSet annularAnimat = getAnimAnnular(0.5f, 1.2f, 0.5f, 1.2f);
		annularAnimat.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				circle_three.setVisibility(View.GONE);
				Map<String, Serializable> map = new HashMap<String, Serializable>();
				map.put("planId", planId);
				PageTurnUtil.gotoActivity(StartMaintenanceActivity.this, StartMaintenanceTaskDetailsActivity.class, true, map);
			}
		});
		circle_three.startAnimation(annularAnimat);
	}

	/**
	 * 点击开始按钮圆形扩散动画
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-26 下午2:25:16
	 */
	private void startCircularAnima() {
		startCircleOneAnimat();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startCircleTwoAnimat();
			}
		}, 100);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startCircleThreeAnimat();
			}
		}, 200);
	}

	/**
	 * 获得扩散动画参数
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-26 下午2:25:53
	 */
	private AnimationSet getAnimAnnular(float fromX, float toX, float fromY,
			float toY) {
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(fromX, toX, fromY, toY,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		// 透明度变化
		animationSet.addAnimation(new AlphaAnimation(1.0f, 0.1f));
		animationSet.setDuration(400);
		sa.setDuration(500);
		sa.setFillAfter(true);
		sa.setRepeatCount(0);
		sa.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(sa);
		return animationSet;
	}
}
