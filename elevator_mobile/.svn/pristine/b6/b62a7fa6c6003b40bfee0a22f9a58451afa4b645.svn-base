package com.ztkj.dialog;

import com.chinacnit.elevatorguard.mobile.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;


public class DialogBottom_Alrt extends BaseDialog implements OnClickListener {
	private DialogPictureSelMCallback callback;
	public EditText edt_content;

	public interface DialogPictureSelMCallback {
		public void onDialogClick(int rid, String content);
	}

	public void setDialogPictureSelMCallback(DialogPictureSelMCallback callback) {
		this.callback = callback;
	}

	public DialogBottom_Alrt(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		Window win = getDialog().getWindow();
		win.setWindowAnimations(R.style.bottomAnimDialog);
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(lp);
		getDialog().findViewById(R.id.btn_no).setOnClickListener(this);
		getDialog().findViewById(R.id.btn_yes).setOnClickListener(this);
		edt_content = (EditText) getDialog().findViewById(R.id.edt_content);
		
		
	}

	@Override
	public int initLayoutid() {
		// TODO Auto-generated method stub
		return R.layout.dialog__bottom;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_no:
		case R.id.btn_yes:
			dismiss();
			callback.onDialogClick(v.getId(), edt_content.getText().toString());
			break;
		}
	}


}
