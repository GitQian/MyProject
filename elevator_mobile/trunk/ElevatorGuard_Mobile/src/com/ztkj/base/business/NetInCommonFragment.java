package com.ztkj.base.business;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.ztkj.data.request.Request;

public abstract class NetInCommonFragment extends NetCommonFragment{
	public ImageView imgRefresh;
	public TextView tvNoMsg;
	public LinearLayout linePro;
	public View viewContent;
	/**
	 * 是否使用netFrame
	 */
	private boolean useNetFrame=true;
	/**
	 * 是否使用netFrame
	 */
	public void setUseNetFrame(boolean bool){
		useNetFrame=bool;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initProFrame();
	}
	private void initProFrame() {
		if(imgRefresh==null||tvNoMsg==null||linePro==null){
			imgRefresh=(ImageView)getView().findViewById(R.id.imgRefresh);
			tvNoMsg=(TextView)getView().findViewById(R.id.tvNoMsg);
			linePro=(LinearLayout)getView().findViewById(R.id.linePro);
			viewContent=getView().findViewById(R.id.viewContent);
			imgRefresh.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					imgClick();
				}
			});
		}
	}
	public void imgClick(){
		linePro.setVisibility(View.VISIBLE);
		imgRefresh.setVisibility(View.INVISIBLE);
		tvNoMsg.setVisibility(View.INVISIBLE);
		imgRefreshClick();
	}
	
	public abstract void imgRefreshClick();
	
	@Override
	public void netResultFailed(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultFailed(result, request);
		if(useNetFrame){
			dealNetFailedView();
		}
	}
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultSuccess(result, request);
		if(useNetFrame){
			dealNetSucView();
		}
	}
	/**
	 * 处理成功显示
	 */
	public void dealNetSucView(){
		linePro.setVisibility(View.INVISIBLE);
		imgRefresh.setVisibility(View.INVISIBLE);
		tvNoMsg.setVisibility(View.INVISIBLE);
		if(viewContent!=null){
			viewContent.setVisibility(View.VISIBLE);
		}
	}
	/**
	 * 处理失败显示
	 */
	public void dealNetFailedView(){
		linePro.setVisibility(View.INVISIBLE);
		imgRefresh.setVisibility(View.VISIBLE);
		tvNoMsg.setVisibility(View.INVISIBLE);
		if(viewContent!=null){
			viewContent.setVisibility(View.INVISIBLE);
		}
	}
	@Override
	public void getData(Request requestBean) {
		// TODO Auto-generated method stub
		if(useNetFrame){
			initProFrame();
			linePro.setVisibility(View.VISIBLE);
			imgRefresh.setVisibility(View.INVISIBLE);
			tvNoMsg.setVisibility(View.INVISIBLE);
			if(viewContent!=null){
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
}
