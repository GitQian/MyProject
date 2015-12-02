package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.BindTagResult;
import com.chinacnit.elevatorguard.mobile.bean.BindTagResult.ResultStatus;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.BindTagTask;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;

/**
 * 绑定维保项标签Activity
 * 
 * @author pyyang
 * @date 创建时间：2015年5月26日 下午3:54:40
 * 
 */
public class ComponentActivity extends BaseActivity implements OnClickListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(ComponentActivity.class.getSimpleName(), true);
	private TextView title_tv;
	private TextView mTvSubmit;
	private LinearLayout back_btn;
	private LinearLayout mLlRightTop;
	/** 维保标准 */
	private TextView mTvMaintenanceStandards;
	/** 绑定标签按钮 */
	private RelativeLayout mRlBindingTag;
	/** 绑定标签对话框确认,取消按钮 */
	private Button confirm_btn, cancle_btn;
	/** 显示卡号的EditText */
	private EditText showTagId;
	/** 扫描成功之后显示的提示图片 */
	private ImageView scanSuccessImage;
	/** 点击确定按钮之后显示的标签ID */
	private TextView mTvShowTagId;
	/** 拍照上传按钮 */
	private RelativeLayout mRlPhotosUpload;
	/** 预览照片ImageView */
	private ImageView mIvPreviewPhoto;
	
	public static final int NONE = 0;
	/** 拍照 */
	public static final int PHOTOHRAPH = 1;
	/** 相册取照片 */
	public static final int PHOTOALBUM = 2;
	/** 缩放 */
	public static final int PHOTOZOOM = 3;
	/** 结果 */
	public static final int PHOTORESOULT = 4;
	
	public static final String IMAGE_UNSPECIFIED = "image/*";
	
	private NfcAdapter mAdapter;
	private AlertDialog checkNFCDialog;
	private PendingIntent mPendingIntent;
	private NdefMessage mNdefPushMessage;
	private Dialog alertOpenNFCDiaLog;
	
	/** 电梯id */
	private long liftId;
	/** 维保标识字段 */
	private String keyValue;
	/** 维保项对应的维保内容 */
	private String maintainItem;
	/** 维保要求 */
	private String maintainClaim;
	/** 维保周期标识 */
	private Cycle maintainCycle;
	/** 排序索引号 */
	private int sortIndex;
	/** 16进制NFC标签卡号 */
	private String hexTagId;
	/** 确定要保存的TagId */
	private String saveTagId;
	/** 只提示一次没有NFC设备 */
	private boolean isAlarmNoNFC;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		liftId = (Long) getIntent().getSerializableExtra("liftId");
		keyValue = (String) getIntent().getSerializableExtra("keyValue");
		maintainItem = (String) getIntent().getSerializableExtra("maintainItem");
		maintainClaim = (String) getIntent().getSerializableExtra("maintainClaim");
		saveTagId = (String) getIntent().getSerializableExtra("tagId");
		initView();
	}
	
	private void initView() {
		setContentView(R.layout.activity_brake);
		resolveIntent(getIntent());
		mAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mAdapter != null) {
			mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
					"Message from NFC Reader :-)", Locale.ENGLISH, true) });
		} else {
			if (!isAlarmNoNFC) {
				showMessage(R.string.error, R.string.no_nfc);
				isAlarmNoNFC = true;
			}
		}
		back_btn = (LinearLayout) findViewById(R.id.back_ll);
		back_btn.setOnClickListener(this);
		title_tv = (TextView) findViewById(R.id.back_textview);
		title_tv.setText(maintainItem);
		mLlRightTop = (LinearLayout) findViewById(R.id.common_title_righttop_ll);
		mLlRightTop.setOnClickListener(this);
		mTvSubmit = (TextView) findViewById(R.id.common_title_submit_textview);
		mTvSubmit.setVisibility(View.VISIBLE);
		mTvMaintenanceStandards = (TextView) findViewById(R.id.id_brake_maintenance_standard_text_content);
		mTvMaintenanceStandards.setText(maintainClaim);
		mTvShowTagId = (TextView) findViewById(R.id.brake_tag_id_show);
		if (!TextUtils.isEmpty(saveTagId)) {
			mTvShowTagId.setText(saveTagId);
			hexTagId = saveTagId;
		}
		mRlBindingTag = (RelativeLayout) findViewById(R.id.activity_brake_binding_tag);
		mRlBindingTag.setOnClickListener(this);
		mRlPhotosUpload = (RelativeLayout) findViewById(R.id.uploading_picture_rl);
		mIvPreviewPhoto = (ImageView) findViewById(R.id.brake_imageview);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mAdapter != null) {
			if (!mAdapter.isEnabled()) {
				showWirelessSettingsDialog();
			}
			mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
			mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.disableForegroundDispatch(this);
			mAdapter.disableForegroundNdefPush(this);
		}
	}
	
	/**
	 * 提示打开NFC对话框
	 * @param
	 * @author: ssu
	 * @date: 2015-6-9 下午3:43:23
	 */
	private void showWirelessSettingsDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.nfc_disabled);
		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
						startActivity(intent);
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						alertOpenNFCDiaLog.dismiss();
					}
				});
		alertOpenNFCDiaLog = builder.create();
		alertOpenNFCDiaLog.show();
		return;
	}
	
	/**
	 * 显示对话框内容
	 * @param
	 * @author: ssu
	 * @date: 2015-6-9 下午3:43:49
	 */
	private void showMessage(int title, int message) {
		checkNFCDialog = new AlertDialog.Builder(this).setNeutralButton("Ok", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				checkNFCDialog.dismiss();
			}
		}).create();
		checkNFCDialog.setTitle(title);
		checkNFCDialog.setMessage(getText(message));
		checkNFCDialog.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			// 设置文件保存路径这里放在跟目录下
			File picture = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
			startPhotoZoom(Uri.fromFile(picture));
		}
		if (data == null)
			return;
		// 读取相册缩放图片
		// if (requestCode == PHOTOZOOM) {
		// startPhotoZoom(data.getData());
		// }
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件
				mIvPreviewPhoto.setImageBitmap(photo);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 对所拍照图片进行裁剪处理
	 * 
	 * @author: pyyang
	 * @date 创建时间：2015年5月26日 下午3:55:38
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽
		intent.putExtra("outputX", 64);
		intent.putExtra("outputY", 64);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, PHOTORESOULT);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll: // 返回
			PageTurnUtil.goBack(this);
			break;
		case R.id.activity_brake_binding_tag: //绑定标签
			showScanNFCAndBluetoothDialog();
			break;
		case R.id.uploading_picture_rl: //拍照
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "temp.jpg")));
			startActivityForResult(intent, PHOTOHRAPH);
			break;
		case R.id.common_title_righttop_ll: //提交
			submitTagData();
			break;
		default:
			break;
		}
	}
	
	private Dialog mScanTagDialog;
	/**
	 * 弹出扫描
	 * @param
	 * @author: ssu
	 * @date: 2015-6-10 下午2:39:42
	 */
	private void showScanNFCAndBluetoothDialog() {
		mScanTagDialog = new Dialog(this, R.style.custom_dialog);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.brake_nfc_dialog, null);
		mScanTagDialog.setCancelable(false);
		mScanTagDialog.setContentView(view);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText("打开NFC，靠近标签");  
		scanSuccessImage = (ImageView) view.findViewById(R.id.brake_nfc_success_imageview);
		showTagId = (EditText) view.findViewById(R.id.brake_nfc_show_tagid_imageview);
		showTagId.setOnTouchListener(new OnTouchListener() {           
  
            public boolean onTouch(View v, MotionEvent event) {    
            	showTagId.setInputType(InputType.TYPE_NULL); // 关闭软键盘        
                return false;
            }
        }); 
		showTagId.setText("");
		confirm_btn = (Button) view.findViewById(R.id.confirm);
		confirm_btn.setText("确定");
		confirm_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tempId = showTagId.getText().toString();
				if (!TextUtils.isEmpty(tempId)) {
					hexTagId = tempId;
					mTvShowTagId.setText(hexTagId);
				}
				showTagId = null;
				mScanTagDialog.dismiss();
			}
		});
		cancle_btn = (Button) view.findViewById(R.id.cancle);
		cancle_btn.setText("取消");
		cancle_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mScanTagDialog.dismiss();
				showTagId = null;
			}
		});
		Display disp = getWindowManager().getDefaultDisplay();
		Window dialogWindow = mScanTagDialog.getWindow();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.x = 0;
		p.y = -200;
		p.height = (int) (disp.getHeight() * 0.37);
		p.width = (int) (disp.getWidth() * 0.9);
		dialogWindow.setAttributes(p);
		mScanTagDialog.show();
	}
	
	/**
	 * 验证TagId是否已经绑定其他维保项
	 * @param
	 * @author: ssu
	 * @date: 2015-7-2 下午8:59:09
	 */
	private void checkTagIdIsBind(String tagId) {
		new BindTagTask(this, R.string.loading, liftId, keyValue, tagId, "false", new IResultListener<BindTagResult>() {
			
			@Override
			public void onSuccess(BindTagResult result) {
				if (result.getStatus() == ResultStatus.TAGBINDEDITEM) {
					showMessage(R.string.error, R.string.tag_binded_item);
				} else {
					String tempId = showTagId.getText().toString();
					if (!TextUtils.isEmpty(tempId)) {
						hexTagId = tempId;
						mTvShowTagId.setText(hexTagId);
					}
					showTagId = null;
				}
			}
			
			@Override
			public void onError(String errMsg) {
				CommonToast.showToast(ComponentActivity.this, R.string.network_unavailable);
			}
		}).execute();
	}
	
	@Override
	protected void onStop() {
		if (null != alertOpenNFCDiaLog) {
			alertOpenNFCDiaLog.dismiss();
		}
		if (null != checkNFCDialog) {
			checkNFCDialog.dismiss();
		}
		if (null != mScanTagDialog) {
			mScanTagDialog.dismiss();
		}
		super.onStop();
	}
	
	/**
	 * 注册读取NFC数据
	 * @param
	 * @author: ssu
	 * @date: 2015-6-9 下午2:53:41
	 */
	private void resolveIntent(Intent intent) {
		String action = intent.getAction();
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage[] msgs = null;
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			} else {
				// Unknown tag type
				byte[] empty = new byte[0];
				byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
				Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
				if (!TextUtils.isEmpty(getTagIdByData(tag))) {
					byte[] payload = getTagIdByData(tag).getBytes();
					NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
					NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
					msgs = new NdefMessage[] { msg };
				}
			}
			LogUtils.d(LOG_TAG, "resolveIntent", "msgs:" + msgs);
		}
	}
	
	/**
	 * 获得Tag数据里面的ID
	 * @param
	 * @author: ssu
	 * @date: 2015-6-9 下午3:16:07
	 */
	private String getTagIdByData(Parcelable p) {
		Tag tag = (Tag) p;
		byte[] id = tag.getId();
		String tagId = null;
		if (null != showTagId) {
//			tagId = getHex(id);
			tagId = String.valueOf(getDec(id));
			LogUtils.d(LOG_TAG, "getTagIdByData", "tagId:" + tagId);
			showTagId.setText(tagId);
			scanSuccessImage.setVisibility(View.VISIBLE);
		}
		return tagId;
	}
	
	private String getHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = bytes.length - 1; i >= 0; --i) {
			int b = bytes[i] & 0xff;
			if (b < 0x10)
				sb.append('0');
			sb.append(Integer.toHexString(b));
			if (i > 0) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	private long getDec(byte[] bytes) {
		long result = 0;
		long factor = 1;
		for (int i = 0; i < bytes.length; ++i) {
			long value = bytes[i] & 0xffl;
			result += value * factor;
			factor *= 256l;
		}
		return result;
	}
	
	private NdefRecord newTextRecord(String text, Locale locale,
			boolean encodeInUtf8) {
		byte[] langBytes = locale.getLanguage().getBytes(
				Charset.forName("US-ASCII"));
		Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset
				.forName("UTF-16");
		byte[] textBytes = text.getBytes(utfEncoding);
		int utfBit = encodeInUtf8 ? 0 : (1 << 7);
		char status = (char) (utfBit + langBytes.length);
		byte[] data = new byte[1 + langBytes.length + textBytes.length];
		data[0] = (byte) status;
		System.arraycopy(langBytes, 0, data, 1, langBytes.length);
		System.arraycopy(textBytes, 0, data, 1 + langBytes.length,
				textBytes.length);
		return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT,
				new byte[0], data);
	}
	
	@Override
	public void onNewIntent(Intent intent) {
		LogUtils.d(LOG_TAG, "onNewIntent", "onNewIntent");
		setIntent(intent);
		resolveIntent(intent);
	} 
	
	/**
	 * 提交绑定的标签数据
	 * @param
	 * @author: ssu
	 * @date: 2015-6-11 上午10:33:57
	 */
	private void submitTagData() {
		if (TextUtils.isEmpty(hexTagId)) {
			CommonToast.showToast(this, R.string.no_bind_tag);
			return;
		}
		if (!TextUtils.isEmpty(saveTagId)) {
			if (saveTagId.equals(hexTagId)) {
				CommonToast.showToast(this, R.string.alarm_no_repeat_bind_tag);
				return;
			}
		}
//		if (!TextUtils.isEmpty(saveTagId)) { //如果之前已经绑定过标签
//			new AlertDialog.Builder(this).setTitle(R.string.cover_tag_alarm)
//		    .setIcon(android.R.drawable.ic_dialog_info)
//		    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//		  
//		        @Override
//		        public void onClick(DialogInterface dialog, int which) {
//		        	// 点击“确认”后的操作 
//		        	submitDataTask();
//		        }
//		    })
//		    .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
//		  
//		        @Override
//		        public void onClick(DialogInterface dialog, int which) {  
//		        	// 点击“返回”后的操作,这里不设置没有任何操作  
//		        	dialog.dismiss();
//		        }
//		    }).show();
//		} else { //没有绑定过标签
//			submitDataTask();
//		}
		submitDataTask("false");
	}
	
	private void submitDataTask(String cover) {
		new BindTagTask(ComponentActivity.this, R.string.loading, liftId, keyValue, hexTagId, cover, new IResultListener<BindTagResult>() {
			
			@Override
			public void onSuccess(BindTagResult result) { 
				if (result.getStatus() == ResultStatus.NONCOMPLIANCE) {
					CommonToast.showToast(ComponentActivity.this, R.string.maintenance_content_non_compliance);
				} else if (result.getStatus() == ResultStatus.SUCCESS) {
					saveTagId = hexTagId;
					CommonToast.showToast(ComponentActivity.this, R.string.bind_tag_success);
				} else if (result.getStatus() == ResultStatus.ITEMBINDEDTAG) {
//					CommonToast.showToast(ComponentActivity.this, R.string.item_binded_tag);
					new AlertDialog.Builder(ComponentActivity.this).setTitle(R.string.cover_tag_alarm)
				    .setIcon(android.R.drawable.ic_dialog_info)
				    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
				  
				        @Override
				        public void onClick(DialogInterface dialog, int which) {
				        	// 点击“确认”后的操作 
				        	submitDataTask("true");
				        }
				    })
				    .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
				  
				        @Override
				        public void onClick(DialogInterface dialog, int which) {  
				        	// 点击“返回”后的操作,这里不设置没有任何操作  
				        	dialog.dismiss();
				        }
				    }).show();
				} else if (result.getStatus() == ResultStatus.TAGBINDEDITEM) {
					CommonToast.showToast(ComponentActivity.this, R.string.tag_binded_item);
				}
			}
			
			@Override
			public void onError(String errMsg) {
				CommonToast.showToast(ComponentActivity.this, R.string.bind_tag_fail);
			}
		}).execute();
	}

}
