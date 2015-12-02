package com.chinacnit.elevatorguard.core.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

public class LoadingDialog extends Dialog {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(LoadingDialog.class.getSimpleName(), true);
	private Context context;
	private String loadingText;
	private TextView loading_text;
	private int resLayout = R.layout.dialog_loading_1;

	public LoadingDialog(Context context) {
		super(context);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public LoadingDialog(Context context, int theme, int loadingTextId) {
		super(context, theme);
		this.context = context;
		this.loadingText = context.getResources().getString(loadingTextId);
	}

	public int getResLayout() {
		return this.resLayout;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		createLoadingDialog();
	}

	public void createLoadingDialog() {
		if (this.resLayout != 0) {
			setContentView(this.resLayout);
			if (this.resLayout == R.layout.dialog_loading_1) {
				this.loading_text = ((TextView) findViewById(R.id.tipTextView));
				this.loading_text.setText(this.loadingText);
			}
		}
	}
	
	/**
	 * 当窗口焦点改变时调用
	 */
	public void onWindowFocusChanged(boolean hasFocus) {
		ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
		// 获取ImageView上的动画背景
		AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
		// 开始动画
		spinner.start();
	}

	public void setMessage(int resId) {
		this.loadingText = this.context.getResources().getString(resId);
	}

	public void setMessage(String messageText) {
		this.loadingText = messageText;
	}

	public void setResLayout(int layoutId) {
		this.resLayout = layoutId;
	}
}
