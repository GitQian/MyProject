package com.chinacnit.elevatorguard.mobile.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.chinacnit.elevatorguard.mobile.R;

/**
 * 自定义dialog
 * @author ssu 
 * @date 2015-6-16 下午6:11:45
 */
public class MyCustomDialog extends Dialog {
	// 定义回调事件，用于dialog的点击事件
	public interface OnCustomDialogListener {
		public void confirm();
		public void cancle();
	}

	private String name;
	private OnCustomDialogListener customDialogListener;
	EditText etName;

	public MyCustomDialog(Context context, OnCustomDialogListener customDialogListener) {
		super(context);
		this.customDialogListener = customDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brake_nfc_dialog);
		// 设置标题
//		etName = (EditText) findViewById(R.id.edit);
//		Button clickBtn = (Button) findViewById(R.id.clickbtn);
//		clickBtn.setOnClickListener(clickListener);
	}

	private View.OnClickListener clickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
//			customDialogListener.back(String.valueOf(etName.getText()));
			MyCustomDialog.this.dismiss();
		}
	};

}
