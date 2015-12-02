package com.ztkj.dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
public class DialogExit extends BaseDialog{
	private DialogExitcallback dialogcallback;
	private Button sure,cancel;
	private TextView tvContent;
	private int mTag;
	public DialogExit(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		tvContent = (TextView) getDialog().findViewById(R.id.tvContent);
		sure = (Button)  getDialog().findViewById(R.id.btnConfirm);
		cancel=(Button) getDialog().findViewById(R.id.btnCancel);
		
		sure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				dialogcallback.btnConfirm(mTag);
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
				dialogcallback.btnCancel(mTag);
			}
		});
		
		
	}

	public interface DialogExitcallback {
		public void btnConfirm(int tag);
		public void btnCancel(int tag);
	}

	public void setDialogExitcallback(DialogExitcallback dialogcallback) {
		this.dialogcallback = dialogcallback;
	}
	
	public void setContent(String content) {
		tvContent.setText(content);
	}
	public String getContent(){
		return tvContent.getText().toString();
	}
	public void setCanceText(String text){
		cancel.setText(text);
	}
	public void setConfirmText(String confirmText){
		sure.setText(confirmText);
	}
	public void setCancelGone(){
		cancel.setVisibility(View.GONE);
	}

	@Override
	public int initLayoutid() {
		// TODO Auto-generated method stub
		return R.layout.dialog_exit;
	}
	public void show(int tag) {
		// TODO Auto-generated method stub
		this.mTag=tag;
		super.show();
	}
}