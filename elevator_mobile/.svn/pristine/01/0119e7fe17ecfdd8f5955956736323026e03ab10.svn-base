package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.core.http.exception.HttpApiException;
import com.chinacnit.elevatorguard.core.http.exception.NetworkUnavailableException;
import com.chinacnit.elevatorguard.core.net.DownLoaderThread;
import com.chinacnit.elevatorguard.core.net.FetcherListener;
import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.application.ElevatorGuardApplication;
import com.chinacnit.elevatorguard.mobile.bean.CheckVersionIsUpdate;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.config.Constants;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.CheckVersionIsUpdateTask;
import com.chinacnit.elevatorguard.mobile.ui.view.CommonProgressDialog;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.chinacnit.elevatorguard.mobile.util.VersionUtil;

/**
 * 版本信息Activity
 * 
 * @author ssu
 * @date 2015-6-25 下午1:40:06
 */
public class VersionInfoActivity extends BaseActivity implements OnClickListener {

	private static final LogTag LOG_TAG = LogUtils.getLogTag(VersionInfoActivity.class.getSimpleName(), true);

	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();

	/** 顶部返回按钮 */
	private LinearLayout mLlBack;
	/** 顶部返回按钮文本 */
	private TextView mTvBackText;

	/** 显示的版本号 */
	private TextView mTvVersionCode;
	/** 檢查更新 */
	private Button mBtnCheckUpdate;
	
	// 下载apk线程
    private DownLoaderThread downloaderThread = null;
    private ProgressDialog progressDialog = null;
    private CommonProgressDialog commonProgressDialog = null;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version_info);
		initView();
	}

	private void initView() {
		mLlBack = (LinearLayout) findViewById(R.id.back_ll);
		mLlBack.setOnClickListener(this);
		mTvBackText = (TextView) findViewById(R.id.back_textview);
		mTvBackText.setText(R.string.version_info);

		mTvVersionCode = (TextView) findViewById(R.id.version_info_version_text);
		String currentVersionName = VersionUtil
				.getCurrentVersionName(VersionInfoActivity.this);
		mTvVersionCode.setText(currentVersionName);

		mBtnCheckUpdate = (Button) findViewById(R.id.version_info_check_update_btn);
		mBtnCheckUpdate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.version_info_check_update_btn: // 检查更新
			checkVersion();
			break;
		default:
			break;
		}
	}

	/**
	 * 检查是否有版本更新
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-6-30 下午2:10:49
	 */
	private void checkVersion() {
		if (null != mLoginDetail) {
			new CheckVersionIsUpdateTask(this, R.string.loading, 1,
					new IResultListener<CheckVersionIsUpdate>() {

						@Override
						public void onSuccess(CheckVersionIsUpdate result) {
							if (null != result && !TextUtils.isEmpty(result.getLatestVersion())) {
								// 获取服务器返回版本号中的数字字符串
								char[] number = result.getLatestVersion().toCharArray();
								String tempNewVersion = "";
								for (int i = 0; i < number.length; i++) {
									if (("0123456789").indexOf(number[i] + "") != -1) {
										tempNewVersion += number[i];
									}
								}
								char[] number1 = VersionUtil.getCurrentVersionName(VersionInfoActivity.this)
										.toCharArray();
								String tempCurrentVersion = "";
								for (int i = 0; i < number1.length; i++) {
									if (("0123456789").indexOf(number1[i] + "") != -1) {
										tempCurrentVersion += number1[i];
									}
								}

								int newVersion = 0;
								int currentVersion = 0;
								try {
									newVersion = Integer.parseInt(tempNewVersion);
									currentVersion = Integer.parseInt(tempCurrentVersion);
								} catch (Exception e) {
									LogUtils.e(LOG_TAG, "checkVersion", e);
								}

								if (newVersion <= currentVersion) {
									CommonToast.showToast(VersionInfoActivity.this, R.string.new_version);
								} else {
									updataVersion(result.getLatestVersion());
								}
							}
						}

						@Override
						public void onError(String errMsg) {
							CommonToast.showToast(VersionInfoActivity.this,
									R.string.network_unavailable);
						}
					}).execute();
		}
	}

	/**
	 * 更新APK版本
	 * 
	 * @param
	 * @author: ssu
	 * @date: 2015-6-30 下午4:21:08
	 */
	private void updataVersion(final String newVersion) {
		// 需要更新
		final AlertDialog groupDialog = new AlertDialog.Builder(VersionInfoActivity.this).create();
		View view = LayoutInflater.from(VersionInfoActivity.this).inflate(R.layout.lg_alert_dialog, null);
		groupDialog.setView(view);
//		TextView title = (TextView) view.findViewById(R.id.tvLgAlertDialogTitle);
//		title.setText(String.format(getString(R.string.version_update_alarm_title), newVersion));
		TextView updateMessage = (TextView) view.findViewById(R.id.tvLgAlertDialogMsg);
		updateMessage.setText(String.format(getString(R.string.version_update_alarm_title), newVersion.substring(0, newVersion.lastIndexOf("."))));
		TextView cancel = (TextView) view.findViewById(R.id.btnLgAlertDialogNegative);
		TextView ok = (TextView) view.findViewById(R.id.btnLgAlertDialogPositive);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				groupDialog.dismiss();
			}
		});
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startDownApk(newVersion);
				groupDialog.dismiss();
			}
		});
		groupDialog.show();
	}

	/**
	 * 下载最新APK
	 * @param
	 * @author: ssu
	 * @date: 2015-6-30 下午6:57:16
	 */
	private void startDownApk(final String apkName) {
		final String apkDir = Constants.APPLICATION_SDCARD_PATH + "/apk";
		// 清除目录
        try {
            File dir = new File(apkDir);
            if (dir.exists()) {
                FileUtils.cleanDirectory(new File(apkDir));
            }
        } catch (IOException e) {
        	LogUtils.e(LOG_TAG, "startDownApk", e);
        }
        downloaderThread = new DownLoaderThread();
        downloaderThread.setLocalDir(apkDir);
        downloaderThread.setLocalFileNameList(new String[] {apkName});
        try {
			downloaderThread.setRemoteUriList(new String[] {((ElevatorGuardApplication) getApplication()).getApi().downloadNewVersionApk(1)});
		} catch (HttpApiException e) {
			LogUtils.e(LOG_TAG, "startDownApk", e);
		} catch (NetworkUnavailableException e) {
			LogUtils.e(LOG_TAG, "startDownApk", e);
		}
        downloaderThread.setListener(new FetcherListener() {

            @Override
            public void onStart(String fileName, int fileLength) {
                handler.obtainMessage(DownLoaderThread.DOWNLOAD_STARTED, fileLength, 0, fileName).sendToTarget();
            }

            @Override
            public void onReceive(int currentIndex, int allIndex, int total, int received) {
                handler.obtainMessage(DownLoaderThread.DOWNLOADING, received, 0).sendToTarget();
            }

            @Override
            public void onOneFinish(File apkFile, int currentIndex, int allIndex) throws Exception {}

            @Override
            public void onInterrupt() {}

            @Override
            public void onError() {
                handler.obtainMessage(DownLoaderThread.ERROR, getString(R.string.download_error)).sendToTarget();
            }

            @Override
            public void onAllDone() {
                handler.obtainMessage(DownLoaderThread.COMPLETE, getString(R.string.download_sucess)).sendToTarget();
                autoInstallApk(new File(apkDir + File.separator + apkName));
            }
        });
        downloaderThread.start();
	}
	
	private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DownLoaderThread.DOWNLOAD_STARTED:
                    if (msg.obj != null && msg.obj instanceof String) {
                        int maxValue = msg.arg1;
                        String fileName = (String) msg.obj;
                        dismissCurrentProgressDialog();

                        progressDialog = new ProgressDialog(VersionInfoActivity.this);
                        progressDialog.setTitle(fileName);
                        progressDialog.setMessage(getResources().getString(R.string.downloading));  
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        progressDialog.setProgress(0);
                        progressDialog.setMax(maxValue);

                        Message newMsg = Message.obtain(this, DownLoaderThread.CANCELED, getString(R.string.download_cancel));
                        progressDialog.setCancelMessage(newMsg);
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                        
//                        commonProgressDialog = new CommonProgressDialog(VersionInfoActivity.this);
//                        commonProgressDialog.setTitle(fileName);
//                        commonProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                        commonProgressDialog.setProgress(0);
//                        commonProgressDialog.setMax(maxValue / 1024);
//                        
//                        Message newMsg = Message.obtain(this, DownLoaderThread.CANCELED, getString(R.string.download_cancel));
//                        commonProgressDialog.setCancelMessage(newMsg);
//                        commonProgressDialog.setCancelable(true);
//                        commonProgressDialog.show();
                    }
                    break;
                case DownLoaderThread.DOWNLOADING:
                    // Update the progress bar
                    if (progressDialog != null) {
                        int currentProgress = msg.arg1;
                        if (msg.obj != null && msg.obj instanceof String) {
                            progressDialog.setMessage((String) msg.obj);
                        } else {
                            progressDialog.setProgress(currentProgress);
                        }
                    }
//                	if (commonProgressDialog != null) {
//                        int currentProgress = msg.arg1;
//                        if (msg.obj != null && msg.obj instanceof String) {
//                        	commonProgressDialog.setMessage((String) msg.obj);
//                        } else {
//                        	commonProgressDialog.setProgress(currentProgress);
//                        }
//                    }
                    break;

                case DownLoaderThread.ERROR:
                    if (msg.obj != null && msg.obj instanceof String) {
                        displayMessage((String) msg.obj);
                    }
                    dismissCurrentProgressDialog();
                    break;
                case DownLoaderThread.COMPLETE:
                    if (msg.obj != null && msg.obj instanceof String) {
                        displayMessage((String) msg.obj);
                    }
                    dismissCurrentProgressDialog();
                    break;
                case DownLoaderThread.CANCELED:
                    if (downloaderThread != null) {
                        downloaderThread.interrupt();
                    }
                    if (msg.obj != null && msg.obj instanceof String) {
                        displayMessage((String) msg.obj);
                    }
                    dismissCurrentProgressDialog();
                    break;
                default:
                    break;
            }
        }
    };
    
    public void dismissCurrentProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
            progressDialog.dismiss();
            progressDialog = null;
        }
//    	if (commonProgressDialog != null) {
//    		commonProgressDialog.hide();
//    		commonProgressDialog.dismiss();
//    		commonProgressDialog = null;
//    	}
    }
    
    public void displayMessage(String message) {
        if (message != null) {
            CommonToast.showToast(getApplicationContext(), message);
        }
    }
    
    private void autoInstallApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }
}
