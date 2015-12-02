package com.ztkj.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.chinacnit.elevatorguard.mobile.R;


/**
 * 	内置了一个dialog对象，设置了dialog的样式、动画、回调接口;
 *  内置一个View（可以是ImageView、TextView、Button等）用来关闭对话框，如果要用到此View必须给定id为btnClose，不用就不要写
 * @author hzx
 *
 */
public abstract class BaseDialog {

	private Context mContext;
	private Dialog dialog;
	private boolean isKeyBackDismiss=true;
	/**
	 * init the dialog
	 * 
	 * @return
	 */
	public BaseDialog(Context context) {
		this.mContext = context;
		init(-1);
	}
	
	public BaseDialog(Context context,int style){
		this.mContext = context;
		init(style);
	}
	
	private void init(int style){
		dialog = new Dialog(mContext, style!=-1?style:R.style.translucentDialogStyle);
		Window window = dialog.getWindow();
		window.setWindowAnimations(R.style.exitAnimDialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(initLayoutid());
		dialog.setCanceledOnTouchOutside(true);
		dialog.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode == KeyEvent.KEYCODE_BACK
						&& event.getAction() == KeyEvent.ACTION_UP
						&& event.getRepeatCount() == 0) {
					dealBack();
					return true;
				}
				return false;
			}
		});
		View vClose=dialog.findViewById(R.id.btnClose);
		View vCancel=dialog.findViewById(R.id.btnCancel);
		setClickToClose(vClose);
		setClickToClose(vCancel);
	}
	private void setClickToClose(View v){
		if(v!=null){
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});
		}
	}
	/**
	 * 初始化布局
	 * @return
	 */
	public abstract int initLayoutid();
	/**
	 * 返回内置对话框，如果需要修改样式可以调用此方法
	 * @return
	 */
	public Dialog getDialog(){
		return dialog;
	}
	public Context getContext(){
		return mContext;
	}
	/**
	 * 设置是否在其它地方点击对话框消失,默认是true
	 * @param bool
	 */
	public void setCanceledOnTouchOutside(boolean bool){
		dialog.setCanceledOnTouchOutside(bool);
	}
	public void show() {
		dialog.show();
	}

	public void hide() {
		dialog.hide();
	}
	public void dismiss() {
		dialog.dismiss();
	}
	public boolean isShowing() {
		if (dialog.isShowing()) {
			return true;
		}
		return false;
	}
	public void dealBack(){
		if(isKeyBackDismiss){
			dismiss();
		}
	}

	public boolean isKeyBackDismiss() {
		return isKeyBackDismiss;
	}

	public void setKeyBackDismiss(boolean isKeyBackDismiss) {
		this.isKeyBackDismiss = isKeyBackDismiss;
	}
	
}