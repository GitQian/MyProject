package com.chinacnit.elevatorguard.mobile.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;

/**
 * 关于我们Activity
 * 
 * @author ssu
 * @date 2015-6-25 下午5:48:09
 */
public class AboutUsAndUseClausesActivity extends BaseActivity implements OnClickListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(AboutUsAndUseClausesActivity.class.getSimpleName(), true);
	
	/** 顶部返回按钮 */
	private LinearLayout mLlBack;
	/** 顶部返回按钮文本 */
	private TextView mTvBackText;
	
	/** 展示内容WebView */
	private WebView mWvShowContent;
	/** 展示类型 */
	private String type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = getIntent().getStringExtra("type");
		setContentView(R.layout.activity_about_us);
		initView();
	}
	
	private void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.back_ll);
		mLlBack.setOnClickListener(this);
		mTvBackText = (TextView) findViewById(R.id.back_textview);
		mWvShowContent = (WebView) findViewById(R.id.webView);
		if (!TextUtils.isEmpty(type) && "about_us".equals(type)) {
			mTvBackText.setText(R.string.about_us);
			mWvShowContent.loadUrl("file:///android_asset/about_us_info.html");
        } else if (!TextUtils.isEmpty(type) && "terms_of_use".equals(type)) {
        	mTvBackText.setText(R.string.use_clauses);
        	mWvShowContent.loadUrl("file:///android_asset/terms_of_use_info.html");
        }
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll: // 返回
			PageTurnUtil.goBack(this);
			break;
		default:
			break;
		}
	}
}
