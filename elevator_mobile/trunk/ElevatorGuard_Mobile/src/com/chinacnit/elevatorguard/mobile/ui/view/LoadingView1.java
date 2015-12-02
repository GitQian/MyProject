package com.chinacnit.elevatorguard.mobile.ui.view;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.chinacnit.elevatorguard.mobile.R;

/**
 * 自定义进度加载框
 * 
 * @author ssu
 * @date 2015-5-13 下午3:54:59
 */
public class LoadingView1 extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new Builder(getActivity());
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.view_custom_dialog, null);
		builder.setView(view);
		return builder.create();
	}
}
