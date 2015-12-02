package com.example.mydialog;
/**
 * name：dalong
 * date：2014/12/3
 * QQ：982242260
 */
import com.example.mydialog.MyDialog.DialogItemClickListener;
import com.example.mydialog.MyDialog.OnAlertViewClickListener;
import com.example.mydialog.MyDialog.OnTimeAlertViewClickListener;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private Button btnOpen1, btnOpen2, btnOpen3, btnOpen4,btnOpen5,btnOpen6;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnOpen1 = (Button) this.findViewById(R.id.btnOpen1);
		btnOpen2 = (Button) this.findViewById(R.id.btnOpen2);
		btnOpen3 = (Button) this.findViewById(R.id.btnOpen3);
		btnOpen4 = (Button) this.findViewById(R.id.btnOpen4);
		btnOpen5 = (Button) this.findViewById(R.id.btnOpen5);
		btnOpen6 = (Button) this.findViewById(R.id.btnOpen6);
		btnOpen1.setOnClickListener(this);
		btnOpen2.setOnClickListener(this);
		btnOpen3.setOnClickListener(this);
		btnOpen4.setOnClickListener(this);
		btnOpen5.setOnClickListener(this);
		btnOpen6.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOpen1:
			MyDialog.showAlertView(this, 0, null, "删除成功", "确   定", null, new OnAlertViewClickListener() {
				@Override
				public void confirm(String result) {}
				@Override
				public void cancel() {
					Toast.makeText(MainActivity.this, "删除成功", 1).show();
				}
			});
			break;
		case R.id.btnOpen2:
			MyDialog.showAlertView(this, R.drawable.dialog_icon, "删除成功", null, "确   定", null, new OnAlertViewClickListener() {
				@Override
				public void confirm(String result) {}
				@Override
				public void cancel() {
					Toast.makeText(MainActivity.this, "删除成功", 1).show();
				}
			});
			break;
		case R.id.btnOpen3:
			MyDialog.showAlertView(this, R.drawable.dialog_icon, "删除成功", null, "取消", new String[]{"确认"}, new OnAlertViewClickListener() {
				@SuppressLint("ShowToast")
				@Override
				public void confirm(String result) {
					if(result.equals("确认")){
						Toast.makeText(MainActivity.this, result, 1).show();
					}
				}
				@SuppressLint("ShowToast")
				@Override
				public void cancel() {
					Toast.makeText(MainActivity.this, "取消", 1).show();
				}
			});
			break;
		case R.id.btnOpen4:
			MyDialog.showTimeAlertDialog(this, 0, "删除成功", "秒后自动跳转页面或", "手动跳转", 5, new OnTimeAlertViewClickListener() {
				@Override
				public void confirm() {
					Toast.makeText(MainActivity.this, "删除成功", 1).show();
				}
			});
			break;
		case R.id.btnOpen5:
			MyDialog.ShowDialog(this, "", new String[]{"男"}, new DialogItemClickListener() {
				@Override
				public void confirm(String result) {
					Toast.makeText(MainActivity.this,result, 1).show();
				}
			});
			break;
		case R.id.btnOpen6:
			MyDialog.ShowDialog(this, "请选择性别", new String[]{"男","女"}, new DialogItemClickListener() {
				@Override
				public void confirm(String result) {
					Toast.makeText(MainActivity.this,result, 1).show();
				}
			});
			break;
		}

	}


}
