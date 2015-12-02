package com.chinacnit.elevatorguard.mobile.ui.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.ui.view.CustomDatePicker.ChangingListener;

/**
 * 自定义时间选择器
 * @author ssu 
 * @date 2015-7-9 下午12:25:35
 */
public class CustomDatePickerDialog extends Dialog {
	private TextView tv_sure;
	private TextView tv_cancel;
	private onDateListener listener;
	private Calendar c = Calendar.getInstance();
	private CustomDatePicker cdp;

	public CustomDatePickerDialog(Context context, Calendar c) {
		super(context, R.style.CustomDialog);
		this.c = c;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_datepicker_dialog);
		getWindow().setBackgroundDrawable(new BitmapDrawable());
		setCanceledOnTouchOutside(true);
		final TextView tv_dialog_content = (TextView) findViewById(R.id.tv_dialog_content);
		tv_sure = (TextView) findViewById(R.id.tv_sure);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		cdp = (CustomDatePicker) findViewById(R.id.cdp);
		cdp.setDate(c);
		SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy年MM月dd日 E");
		String string = sdfFrom.format(c.getTime());
		tv_dialog_content.setText(string);
		cdp.addChangingListener(new ChangingListener() {

			@Override
			public void onChange(Calendar c1) {
				c = c1;
				SimpleDateFormat sdfFrom = new SimpleDateFormat("yyyy年MM月dd日 E");
				String string = sdfFrom.format(c.getTime());
				tv_dialog_content.setText(string);
			}
		});
		
		View.OnClickListener clickListener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v == tv_sure) {
					if (listener != null) {
						listener.dateFinish(c);
					}
				}
				dismiss();
			}
		};
		tv_sure.setOnClickListener(clickListener);
		tv_cancel.setOnClickListener(clickListener);
	}

	/**
	 * 设置限制日期，设置后，不能选择设置的开始日期以前的日期
	 * 
	 * @param c
	 */
	public void setNowData(Calendar c) {
		cdp.setNowData(c);
	}

	/**
	 * 设置点击确认的事件
	 * 
	 * @param listener
	 */
	public void addDateListener(onDateListener listener) {
		this.listener = listener;
	}

	public interface onDateListener {
		void dateFinish(Calendar c);
	}

}
