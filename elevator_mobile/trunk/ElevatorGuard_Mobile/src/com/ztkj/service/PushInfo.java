package com.ztkj.service;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.ui.activity.MessageActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.MessageNetActivity;
/**
 * key=title<br>
 * key=content<br>
 *
 */
public class PushInfo extends Activity implements OnClickListener {
	private TextView tvInfo;
	private Vibrator mVibrator;
	private MediaPlayer player;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_push_info);
		init();
	}

	public void init() {
		tvInfo = (TextView) findViewById(R.id.tvContent);
		findViewById(R.id.tvClose).setOnClickListener(this);
		findViewById(R.id.tvCheck).setOnClickListener(this);

		AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		// 最后一个参数传0，为了不让系统音量进度条显示，如果要显示系统音量进度条则传AudioManager.STREAM_MUSIC
		am.setStreamVolume(AudioManager.STREAM_MUSIC,
				am.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
		mVibrator = (Vibrator) getApplication().getSystemService(
				Service.VIBRATOR_SERVICE);
		String curMusic = Settings.System.getString(getContentResolver(),
				Settings.System.NOTIFICATION_SOUND);
		player = new MediaPlayer();
		try {
			player.setDataSource(curMusic);
			player.prepare();
		} catch (Exception e) {
			// TODO: handle exception
		}

		setData();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent);
		setData();
	}

	public void setData() {
		mVibrator.vibrate(500);
		player.start();
		String title = getIntent().getStringExtra("title");
		String content = getIntent().getStringExtra("content");
		tvInfo.setText("标题 : "+title + "\n内容 : " + content);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvClose:
			finish();
			break;
		case R.id.tvCheck:
			Intent intent = new Intent(this, MessageNetActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_translucent_enter,R.anim.activity_translucent_exit);
			finish();
			break;
		default:
			break;
		}
	}
}
