package com.ztkj.dialog;


import android.content.Context;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;



public class ProDialog extends BaseDialog{
	private TextView tvShow;

	public ProDialog(Context context) {
		super(context);
		setCanceledOnTouchOutside(false);
		setKeyBackDismiss(false);
		// TODO Auto-generated constructor stub
		tvShow = (TextView) getDialog().findViewById(R.id.tv_show);
		tvShow.setText("正在检测请稍后...");
	}
	@Override
	public int initLayoutid() {
		// TODO Auto-generated method stub
		return R.layout.dialog_pro;
	}
	public void setTvShow(String str) {
		tvShow.setText(str);
	}
	
	/**获取对话框上的文字*/
	public TextView getTvShow(){
		return tvShow;
	}
	@Override
	public void setKeyBackDismiss(boolean isKeyBackDismiss) {
		// TODO Auto-generated method stub
		super.setKeyBackDismiss(isKeyBackDismiss);
	}
}