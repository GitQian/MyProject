package com.ztkj.base.business;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.ztkj.data.request.Request;

/**
 * 内嵌frame的布局，网络访问点返回键可以随时退出<br>
 * <font color='red'>需要在这个类里面加载进度条的可以调用getData(RequestBean requestBean,boolean
 * isShowPro) 这个方法<br>
 * 但是需要重写netResultFailed并将super.netResultFailed()删除</font>
 * 
 * @author hzx
 *
 */
public abstract class NetInCommonActivity extends NetCommonActivity {
	public ImageView imgRefresh;
	public TextView tvNoMsg;
	public LinearLayout linePro;
	public View viewContent;
	/**
	 * 是否使用netFrame
	 */
	private boolean useNetFrame = true;

	/**
	 * 是否使用netFrame
	 */
	public void setUseNetFrame(boolean bool) {
		useNetFrame = bool;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initProFrame();
	}

	private void initProFrame() {
		if (imgRefresh == null || tvNoMsg == null || linePro == null) {
			imgRefresh = (ImageView) findViewById(R.id.imgRefresh);
			tvNoMsg = (TextView) findViewById(R.id.tvNoMsg);
			linePro = (LinearLayout) findViewById(R.id.linePro);
			viewContent = findViewById(R.id.viewContent);
			imgRefresh.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					imgClick();
				}
			});
		}
	}

	@Override
	public void netResultFailed(String result, Request request) {
		super.netResultFailed(result, request);
		if (useNetFrame) {
			dealNetFailedView();
		}
	}

	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultSuccess(result, request);
		if (useNetFrame) {
			dealNetSucView();
		}
	}

	public void imgClick() {
		linePro.setVisibility(View.VISIBLE);
		imgRefresh.setVisibility(View.INVISIBLE);
		tvNoMsg.setVisibility(View.INVISIBLE);
		imgRefreshClick();
	}

	@Override
	public void getData(Request requestBean) {
		// TODO Auto-generated method stub
		if (useNetFrame) {
			initProFrame();
			linePro.setVisibility(View.VISIBLE);
			imgRefresh.setVisibility(View.INVISIBLE);
			tvNoMsg.setVisibility(View.INVISIBLE);
			if (viewContent != null) {
				viewContent.setVisibility(View.INVISIBLE);
			}
		}
		super.getData(requestBean);
	}

	@Override
	public void getData(Request requestBean, boolean isShowPro) {
		// TODO Auto-generated method stub
		setUseNetFrame(false);
		super.getData(requestBean, isShowPro);
	}

	/**
	 * 处理失败显示
	 */
	public void dealNetFailedView() {
		linePro.setVisibility(View.INVISIBLE);
		imgRefresh.setVisibility(View.VISIBLE);
		tvNoMsg.setVisibility(View.INVISIBLE);
		if (viewContent != null) {
			viewContent.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * 处理成功显示
	 */
	public void dealNetSucView() {
		linePro.setVisibility(View.INVISIBLE);
		imgRefresh.setVisibility(View.INVISIBLE);
		tvNoMsg.setVisibility(View.INVISIBLE);
		if (viewContent != null) {
			viewContent.setVisibility(View.VISIBLE);
		}
	}

	/** 点击刷新图片 */
	public abstract void imgRefreshClick();
}
