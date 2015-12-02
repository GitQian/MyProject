package com.chinacnit.elevatorguard.mobile.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.ui.view.LoadingView;

public class DialogUtil {

	/**
	 * 自定义的加载Dialog
	 * 
	 * @param context
	 * @param msg
	 *            文字显示
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
		LinearLayout layout = (LinearLayout) v
				.findViewById(R.id.dialog_loading_view);// 加载布局
		TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
		tipTextView.setText(msg);// 设置加载信息
		Dialog loadingDialog = new Dialog(context, R.style.MyDialogStyle);// 创建自定义样式dialog
		loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
		loadingDialog.setCanceledOnTouchOutside(true); // 点击加载框以外的区域
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		loadingDialog.show();
		return loadingDialog;
	}

	/**
	 * 自定义的加载Dialog
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-14 上午10:33:30
	 */
	public static ProgressDialog createLoadingDialog1(Context context, String text) {
		final ProgressDialog dlg = new ProgressDialog(context);
		dlg.show();
		dlg.setContentView(R.layout.view_custom_dialog1);
		LinearLayout root = (LinearLayout) dlg.findViewById(R.id.progressDialog);
		root.setGravity(android.view.Gravity.CENTER);
		LoadingView mLoadView = new LoadingView(context);
		mLoadView.setDrawableResId(R.drawable.dialog_loading_img);
		root.addView(mLoadView);
		TextView alert = new TextView(context);
		alert.setGravity(android.view.Gravity.CENTER);
		alert.setText(text);
		root.addView(alert);
		return dlg;
	}

	/**
	 * 自定义的对话Dialog
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-5-13 下午4:47:00
	 */
	public static void createAlertDialog(Context context, String msg) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setMessage(msg);
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

}
