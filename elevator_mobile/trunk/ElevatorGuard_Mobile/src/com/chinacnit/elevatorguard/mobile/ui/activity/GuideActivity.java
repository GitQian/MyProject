package com.chinacnit.elevatorguard.mobile.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.ui.view.GuideScrollView;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;

/**
 * 引导页
 * 
 * @author ssu
 * @date 2015-5-15 下午6:23:19
 */
public class GuideActivity extends BaseActivity {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(GuideActivity.class.getSimpleName(), true);
	private FrameLayout mFrameLayout;
	private LinearLayout.LayoutParams param;
	private GuideScrollView scrollView;
	private LayoutInflater inflater;
	private Button mBtnEntryMain;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		setupView();
	}

	private void setupView() {
		scrollView = (GuideScrollView) findViewById(R.id.custom_view_guide);
		param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		inflater = this.getLayoutInflater();
		View addView1 = inflater.inflate(R.layout.activity_guide_first, null);
		mFrameLayout = new FrameLayout(this);
		mFrameLayout.addView(addView1, param);
		scrollView.addView(mFrameLayout);
		
		View addView2 = inflater.inflate(R.layout.activity_guide_two, null);
		mFrameLayout = new FrameLayout(this);
		mFrameLayout.addView(addView2, param);
		scrollView.addView(mFrameLayout);
		
		View addView3 = inflater.inflate(R.layout.activity_guide_three, null);
		mBtnEntryMain = (Button) addView3.findViewById(R.id.btn_entry_main);
		mBtnEntryMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GoToMainActivity();
			}
		});
		mFrameLayout = new FrameLayout(this);
		mFrameLayout.addView(addView3, param);
		scrollView.addView(mFrameLayout);
		
		scrollView.setPageListener(new GuideScrollView.PageListener() {
			@Override
			public void page(int page) {
				LogUtils.d(LOG_TAG, "setupView", "page:" + page);
			}
		});
	}
	
	/**
     * 进入主界面
     */
    private void GoToMainActivity() {
    	PageTurnUtil.gotoActivity(GuideActivity.this, LoginActivity.class, true);
    }
}
