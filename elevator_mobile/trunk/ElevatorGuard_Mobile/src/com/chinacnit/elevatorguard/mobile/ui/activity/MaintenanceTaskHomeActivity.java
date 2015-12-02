package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.ui.fragment.MaintenanceAlertFragment;
import com.chinacnit.elevatorguard.mobile.ui.fragment.MaintenanceTaskListFragment;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.ztkj.base.business.BaseFragmentActivity;

/**
 * 维保任务 。。。。首页 for 维保工
 * @author liucheng  liucheng187@qq.com
 */
public class MaintenanceTaskHomeActivity extends BaseFragmentActivity implements OnClickListener{
	ViewPager viewpager;
	MyAdapter mAdapter;
	TextView tv_tab1,tv_tab2,tv_indicator;
	TextView tv_right;
	private int indicator_width;
	private DisplayMetrics metrics;
	private LinearLayout ll_maskpane; //蒙板
	
	//过滤显示类型。
	public static final int FILTER_TYPE_ALL = 0x0001;
	public static final int FILTER_TYPE_NOTMAINTENANCE = 0x0002;
	public static final int FILTER_TYPE_DELAY = 0x0003;
	public int filterType = FILTER_TYPE_ALL;
	
	private List<OnMyPageChangeListener> pageChangeListeners = new ArrayList<MaintenanceTaskHomeActivity.OnMyPageChangeListener>();
	
	/**
	 * viewpager的页面切换触发。
	 * @author liucheng  liucheng187@qq.com
	 */
	public static interface OnMyPageChangeListener{
		public  void onPageChanged(int position);
	}
	
	public void addOnPageChangeListener(OnMyPageChangeListener listener){
		if(listener==null) return;
		pageChangeListeners.add(listener);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintennancetaskhome);
		initView();
	}

	private void initView() {
		findViewById(R.id.rb_msg).setOnClickListener(this);
		findViewById(R.id.rb_settting).setOnClickListener(this);
		
		metrics = getResources().getDisplayMetrics();
		indicator_width = metrics.widthPixels/3;
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new MyAdapter(getSupportFragmentManager());
		viewpager.setAdapter(mAdapter);
		
		tv_tab1 = (TextView) findViewById(R.id.tv_tab1);
		tv_tab2 = (TextView) findViewById(R.id.tv_tab2);
		tv_tab2.setTextColor(getResources().getColor(R.color.red_melon));
		tv_tab2.setTextColor(getResources().getColor(android.R.color.black));
		
		tv_tab1.setOnClickListener(this);
		tv_tab2.setOnClickListener(this);
		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right.setOnClickListener(this);
		
		tv_indicator = (TextView) findViewById(R.id.tv_indicator);
		tv_indicator.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						LinearLayout.LayoutParams params = (LayoutParams) tv_indicator.getLayoutParams();
						params.width = indicator_width;
						params.leftMargin = (metrics.widthPixels/2 - params.width)/2;
						tv_indicator.setLayoutParams(params);
						tv_indicator.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					}
				});
		
		viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if(position == 0){
					tv_tab1.setTextColor(getResources().getColor(R.color.red_melon));
					tv_tab2.setTextColor(getResources().getColor(android.R.color.black));
					tv_right.setVisibility(View.VISIBLE);
				}else{
					tv_tab1.setTextColor(getResources().getColor(android.R.color.black));
					tv_tab2.setTextColor(getResources().getColor(R.color.red_melon));
					tv_right.setVisibility(View.GONE);
				}
				for (OnMyPageChangeListener listener : pageChangeListeners) {
					listener.onPageChanged(position);
				}
			}
			
			private int lastpostion;
			@Override
			public void onPageScrolled(int position, float positionOffset,int positionOffsetPixels) {
				if(state == 1 || state==2 ){//触摸拖动。。
					if(lastpostion != position){ //本页面稍微滑动就不动了的现象。
						lastpostion = position;
						return;
					}
					if(positionOffset == 0){ //在最后一个页面继续往左边滑动。。的手势。
						return;
					}
					LinearLayout.LayoutParams params = (LayoutParams) tv_indicator.getLayoutParams();
					params.width = indicator_width;
					params.leftMargin = (int) ((metrics.widthPixels/2 - params.width)/2 + metrics.widthPixels/2 * positionOffset);
					tv_indicator.setLayoutParams(params);
				}
			}
			
			private int state;
			@Override
			public void onPageScrollStateChanged(int state) {//1,2,3 分别代表开始拖拽、自由停止中、已停止。
				this.state = state;
			}
		});
		
		
		ll_maskpane = (LinearLayout) findViewById(R.id.ll_maskpane);
		ll_maskpane.setOnClickListener(this);
		RadioGroup rg_group = (RadioGroup) findViewById(R.id.rg_group);
		rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				ll_maskpane.setVisibility(View.INVISIBLE);
				switch (checkedId) {
				case R.id.rb_all:
					filterType = FILTER_TYPE_ALL;
					break;
				case R.id.rb_notmaintenance:
					filterType = FILTER_TYPE_NOTMAINTENANCE;
					break;
				case R.id.rb_delay:
					filterType = FILTER_TYPE_DELAY;
					break;
				}
				if(listener!=null){
					listener.onCheckedChanged(group, checkedId);
				}
			}
		});
		
		findViewById(R.id.v_mask).setOnClickListener(this);
		
	}
	
	private OnCheckedChangeListener listener;
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener){
		this.listener = listener;
	}
	
	
	private static class MyAdapter extends FragmentPagerAdapter{
		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position ) {
			Fragment fragment = null;
			if(position == 0){
				fragment = new MaintenanceTaskListFragment();
			}else{
				fragment = new MaintenanceAlertFragment(1);
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_tab1:
			viewpager.setCurrentItem(0);
			tv_right.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_tab2:
			viewpager.setCurrentItem(1);
			tv_right.setVisibility(View.INVISIBLE);
			break;
		case R.id.tv_right:// 条件筛选。
			if(ll_maskpane.getVisibility()==View.VISIBLE){
				tv_right.setText("筛选");
			}else{
				tv_right.setText("收起");
			}
			ll_maskpane.setVisibility(ll_maskpane.getVisibility()==View.VISIBLE?View.INVISIBLE:View.VISIBLE);
			break;
		case R.id.v_mask:
			ll_maskpane.setVisibility(View.INVISIBLE);
			tv_right.setText("筛选");
			break;
		case R.id.rb_msg:
			PageTurnUtil.gotoActivity(this, MessageNetActivity.class, false);
			break;
		case R.id.rb_settting:
			PageTurnUtil.gotoActivity(this, SettingsActivity.class, false);
			break;
		}
		
	}
}
