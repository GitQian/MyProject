package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.StartMaintenanceTaskDetailItemListAdapter;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.StartMaintenanceTaskDetail;
import com.chinacnit.elevatorguard.mobile.bean.StartMaintenanceTaskItem;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.StartMaintenanceTask;
import com.chinacnit.elevatorguard.mobile.http.task.impl.SubmitMaintenanceItemTask;
import com.chinacnit.elevatorguard.mobile.http.task.impl.SubmitMaintenancePlanTask;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView;
import com.chinacnit.elevatorguard.mobile.ui.view.PullToRefreshLoadView.OnRefreshLoadListener;
import com.chinacnit.elevatorguard.mobile.ui.view.PullableListView;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.ztkj.tool.Tool;

/**
 * 开始维保任务详情页
 * @author ssu 
 * @date 2015-6-18 上午11:55:20
 */
public class StartMaintenanceTaskDetailsActivity extends BaseActivity implements OnClickListener, OnRefreshLoadListener, OnItemClickListener {
	private static final LogTag LOG_TAG = LogUtils.getLogTag(StartMaintenanceTaskDetailsActivity.class.getSimpleName(), true);
	private LinearLayout back_ll;
	private TextView back_textView;
	private LinearLayout mLlRightTop;
	private TextView mTvSubmit;
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	private UserInfo mUserInfo = ConfigSettings.getUserInfo();
	
	private NfcAdapter mNfcAdapter;
	private AlertDialog checkNFCDialog;
	private PendingIntent mPendingIntent;
	private NdefMessage mNdefPushMessage;
	/** 只提示一次没有NFC设备 */
	private boolean isAlarmNoNFC;
	/** 提示Dialog */
	private Dialog alertDialog;
	
	private StartMaintenanceTaskDetailItemListAdapter mAdapter;
	private PullToRefreshLoadView mPullToRefreshLoadView;
	private PullableListView mListView;
	
	private Button normal_btn, abnormal_btn;
	/** 计划开始时间 */
	private TextView mTvStartDate;
	/** 计划结束时间 */
	private TextView mTvEndDate;
	/** 维保人员名称 */
	private TextView mTvWeibaoName;
	
	/** 计划id */
	private long planId;
	
	private StartMaintenanceTaskDetail mResult;
	/** 维保任务项Map */
	private Map<String, StartMaintenanceTaskItem> mItemMap;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch(msg.what) {
			case 0:
				mResult = (StartMaintenanceTaskDetail) msg.obj;
				 List<StartMaintenanceTaskItem> list=mResult.getMaintainContent();
				 ArrayList<StartMaintenanceTaskItem> listxx=new ArrayList<StartMaintenanceTaskItem>();
				 for(int i=0;i<list.size();i++){
					if(!"LIFTMAINTAINSIGN".equals(list.get(i).getStrVal1())){
						listxx.add(list.get(i));
					}
				 }
				mResult.setMaintainContent(listxx); 
				showData();
				break;
			case 1:
				mResult.setMaintainContent((List<StartMaintenanceTaskItem>)msg.obj);
				showList();
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		planId = (Long) getIntent().getSerializableExtra("planId");
		setContentView(R.layout.weibao_record_detail);
		initView();
		setData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (mNfcAdapter != null) {
			if (!mNfcAdapter.isEnabled()) {
				showWirelessSettingsDialog();
			}
			mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
			mNfcAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if (mNfcAdapter != null) {
			mNfcAdapter.disableForegroundDispatch(this);
			mNfcAdapter.disableForegroundNdefPush(this);
		}
	}

	private void initView() {
		resolveIntent(getIntent());
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter != null) {
			mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
			mNdefPushMessage = new NdefMessage(new NdefRecord[] { newTextRecord(
					"Message from NFC Reader :-)", Locale.ENGLISH, true) });
		} else {
			if (!isAlarmNoNFC) {
				showMessage(R.string.error, R.string.no_nfc);
				isAlarmNoNFC = true;
			}
		}
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_textView = (TextView) findViewById(R.id.back_textview);
		back_textView.setText(R.string.maintenance_detail);
		mLlRightTop = (LinearLayout) findViewById(R.id.common_title_righttop_ll);
		mLlRightTop.setOnClickListener(this);
		mTvSubmit = (TextView) findViewById(R.id.common_title_submit_textview);
		mTvSubmit.setVisibility(View.VISIBLE);
		
		findViewById(R.id.weibao_detail_person_list_image).setVisibility(View.GONE);
		
		mTvStartDate = (TextView) findViewById(R.id.wb_startdate);
		mTvEndDate = (TextView) findViewById(R.id.wb_enddate);
		mTvWeibaoName = (TextView) findViewById(R.id.wb_name);
	}
	
	private void setData() {
		if (null != mLoginDetail) {
			new StartMaintenanceTask(this, R.string.loading, planId, new IResultListener<StartMaintenanceTaskDetail>() {
				
				@Override
				public void onSuccess(StartMaintenanceTaskDetail result) {
					if (null != result) {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = result;
						mHandler.sendMessage(msg);
					} else {
						CommonToast.showToast(StartMaintenanceTaskDetailsActivity.this, R.string.no_data);
					}
				}
				
				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(StartMaintenanceTaskDetailsActivity.this, R.string.network_unavailable);
				}
			}).execute();
		}
	}
	
	private void showData() {
		mTvStartDate.setText(mResult.getPlanBeginDate());
		mTvEndDate.setText(mResult.getPlanEndDate());
		mTvWeibaoName.setText(mUserInfo.getRealName());
		showList();
	}
	
	private void showList() {
		if (mAdapter == null && null != mResult && mResult.getMaintainContent() != null && mResult.getMaintainContent().size() > 0) {
			mItemMap = new HashMap<String, StartMaintenanceTaskItem>();
			for (StartMaintenanceTaskItem item : mResult.getMaintainContent()) {
				mItemMap.put(item.getTagInfo(), item);
			}
			mPullToRefreshLoadView = (PullToRefreshLoadView) findViewById(R.id.weibao_detail_person_listview);
			mPullToRefreshLoadView.setOnRefreshLoadListener(StartMaintenanceTaskDetailsActivity.this);
			mListView = new PullableListView(StartMaintenanceTaskDetailsActivity.this, false, false);
			mPullToRefreshLoadView.setRefreshLoadView(StartMaintenanceTaskDetailsActivity.this, mListView); 
			
			
			mListView.setOnItemClickListener(this);
			mAdapter = new StartMaintenanceTaskDetailItemListAdapter(StartMaintenanceTaskDetailsActivity.this, mResult.getMaintainContent());
			mListView.setAdapter(mAdapter);
		} else if (mAdapter != null && null != mResult && mResult.getMaintainContent() != null && mResult.getMaintainContent().size() > 0) {
			mAdapter.onDateChange(mResult.getMaintainContent());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.common_title_righttop_ll: //提交维保计划
			submitMaintenancePlan();
			break;
		}
	}
	
	/**
	 * 提交维保计划
	 * @param
	 * @author: ssu
	 * @date: 2015-6-19 下午12:25:59
	 */
	private void submitMaintenancePlan() {
		boolean isDone = true;
		for (StartMaintenanceTaskItem item : mResult.getMaintainContent()) {
			if (item.getStatusCode() == WeiBaoItemStatus.NOSTATUS) {
				isDone = false;
				break;
			}
		}
		if (!isDone) { // 还有未完成的维保项
			showMessage(R.string.alarm, R.string.no_done_maintenance_item);
			return;
		}
		showSubmitMaintenancePlan();
	}
	
	long lastTime = System.currentTimeMillis();
	/**
	 * 获得Tag标签里面的ID
	 * @param
	 * @author: ssu
	 * @date: 2015-6-9 下午3:16:07
	 */
	private String getTagIdByData(Parcelable p) {
		String tagId = null;
		long currentTime = System.currentTimeMillis();
		if (Math.abs(currentTime - lastTime) > 1 * 1000) {
			Tag tag = (Tag) p;
			byte[] id = tag.getId();
			tagId = String.valueOf(getDec(id));
			if (!TextUtils.isEmpty(tagId)) {
				if (null != mItemMap && mItemMap.size() > 0) {
					StartMaintenanceTaskItem temp = mItemMap.get(tagId);
					if (null != temp) {
						if (temp.getStatusCode() == WeiBaoItemStatus.NOSTATUS) { // 维保项没有被维保过
							showScanTagDialog(temp.getMaintainItem(), temp.getTagInfo());
						} else { // 维保项被维保过
							if (temp.getUid() != mLoginDetail.getUid()) { // 如果不是当前登录用户维保
								showMessage(R.string.alarm, R.string.scan_tag_alarm_already);	
							} else {
								showCoverScanTagDialog(temp);
							}
						}
					} else {  // 如果没有对应的维保项
						LogUtils.d(LOG_TAG, "getTagIdByData", "no item.");
						showMessage(R.string.alarm, R.string.scan_tag_alarm_noitem);
					}
				}
			}
			lastTime = currentTime;
		}
		return tagId;
	}
	
	private Dialog mScanTagDialog;
	/**
	 * 点击机房弹出dialog
	 * 
	 * @author: pyyang
	 * @date 创建时间：2015年5月26日 下午3:17:52
	 */
	private void showScanTagDialog(String titleText, final String tagId) {
		if (null != mScanTagDialog) {
			mScanTagDialog.dismiss();
			mScanTagDialog = null;
		}
		mScanTagDialog = new Dialog(this, R.style.custom_dialog);
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.machine_room_custom_dialog, null);
		mScanTagDialog.setCancelable(true);
		mScanTagDialog.setContentView(view);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(titleText);
		normal_btn = (Button) view.findViewById(R.id.normal);
		normal_btn.setText("正常");
		normal_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new SubmitMaintenanceItemTask(StartMaintenanceTaskDetailsActivity.this, R.string.loading, planId, tagId, mUserInfo.getUserMobile(), null, WeiBaoItemStatus.NORMAL, new IResultListener<Object>() {

					@Override
					public void onSuccess(Object result) {
						CommonToast.showToast(StartMaintenanceTaskDetailsActivity.this, R.string.opeartion_success);
						updateMaintenanceItemStatus(WeiBaoItemStatus.NORMAL, null, tagId);
					}

					@Override
					public void onError(String errMsg) {
						CommonToast.showToast(StartMaintenanceTaskDetailsActivity.this, R.string.opeartion_fail);
					}
					
				}).execute();
				mScanTagDialog.dismiss();
			}
		});
		abnormal_btn = (Button) view.findViewById(R.id.abnormal);
		abnormal_btn.setText("异常");
		abnormal_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Map<String, Serializable> map = new HashMap<String, Serializable>();
				map.put("isShowSubBtn", true);
				map.put("planId", planId);
				map.put("tagId", tagId);
				map.put("statusCode", WeiBaoItemStatus.ABNORMAL);
				PageTurnUtil.gotoActivityForResult(StartMaintenanceTaskDetailsActivity.this, AbnormalNoteActivityX.class, false, map, 1);
				mScanTagDialog.dismiss();
			}
		});
		Window dialogWindow = mScanTagDialog.getWindow();
		WindowManager m = getWindowManager();
		Display disp = m.getDefaultDisplay();
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		p.x = 0;
		p.y = -200;
		p.height = (int) (disp.getHeight() * 0.37);
		p.width = (int) (disp.getWidth() * 0.9);
		dialogWindow.setAttributes(p);
		mScanTagDialog.show();
	}
	
	/**
	 * 更新维保项状态
	 * @param
	 * @author: ssu
	 * @date: 2015-6-18 下午9:01:44
	 */
	private void updateMaintenanceItemStatus(WeiBaoItemStatus status, String comments, String tagId) {
		StartMaintenanceTaskItem item = mItemMap.get(tagId);
		if (null != item) {
			item.setStatusCode(status);
			item.setComments(comments);
			item.setUid(mLoginDetail.getUid());
			List<StartMaintenanceTaskItem> tempItemList = new ArrayList<StartMaintenanceTaskItem>(); 
			tempItemList =  mResult.getMaintainContent();
			for (int i = 0; i < tempItemList.size(); i++) {
				StartMaintenanceTaskItem tempItem = tempItemList.get(i);
				if (item.getTagInfo().equals(tempItem.getTagInfo())) {
					tempItem = item;
					break;
				}
			}
			Message msg = new Message();
			msg.what = 1;
			msg.obj = tempItemList; 
			mHandler.sendMessage(msg);
		}
	}
	
	@Override
	protected void onStop() {
		if (null != alertDialog) {
			alertDialog.dismiss();
		}
		if (null != checkNFCDialog) {
			checkNFCDialog.dismiss();
		}
		if (null != mScanTagDialog) {
			mScanTagDialog.dismiss();
		}
		super.onStop();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		StartMaintenanceTaskItem item = (StartMaintenanceTaskItem) parent.getAdapter().getItem(position);
		Bundle bundle = new Bundle();
		bundle.putSerializable(StartMaintenanceTaskDetail.class.getName(), mResult);
		bundle.putSerializable(StartMaintenanceTaskItem.class.getName(), item);
		Tool.startActivity(this, WeibaoItemDetailActivity_4_worker.class, bundle);
//		
//		Intent intent = new Intent(this, WeiboItemDetailActivity_4manager.class);
//		intent.putExtra("isShowSubBtn", false);
//		intent.putExtra("comments", item.getComments());
//		startActivity(intent);
	}

	@Override
	public void onRefresh(PullToRefreshLoadView pullToRefreshLayout, int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadMore(PullToRefreshLoadView pullToRefreshLayout, int state) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadScrollFinish(PullToRefreshLoadView pullToRefreshLayout,
			int state) {
		// TODO Auto-generated method stub
		
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
				getTagIdByData(tag);
//				if (!TextUtils.isEmpty(getTagIdByData(tag))) {
//					byte[] payload = getTagIdByData(tag).getBytes();
//					NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
//					NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
//					msgs = new NdefMessage[] { msg };
//				}
			}
			LogUtils.d(LOG_TAG, "resolveIntent", "msgs:" + msgs);
		}
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
		checkNFCDialog.setCancelable(false);
		checkNFCDialog.setTitle(title);
		checkNFCDialog.setIcon(android.R.drawable.ic_dialog_info);
		checkNFCDialog.setMessage(getText(message));
		checkNFCDialog.show();
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
						alertDialog.dismiss();
					}   
				});
		builder.setNegativeButton(android.R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						alertDialog.dismiss();
					}
				});
		AlertDialog builder1 = builder.create();
		builder1.setIcon(android.R.drawable.ic_dialog_info);
		alertDialog = builder1;
		alertDialog.setCancelable(false);
		alertDialog.show();
	}
	
	/**
	 * 提交整个维保计划提示框
	 * @param
	 * @author: ssu
	 * @date: 2015-6-19 下午12:35:47
	 */
	private void showSubmitMaintenancePlan() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.submit_maintenance_plan_alarm);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						new SubmitMaintenancePlanTask(StartMaintenanceTaskDetailsActivity.this, R.string.loading, planId, new IResultListener<Object>() {

							@Override
							public void onSuccess(Object result) {
								CommonToast.showToast(StartMaintenanceTaskDetailsActivity.this, R.string.opeartion_success);
								alertDialog.dismiss();
							}

							@Override
							public void onError(String errMsg) {
								CommonToast.showToast(StartMaintenanceTaskDetailsActivity.this, R.string.opeartion_fail);
								alertDialog.dismiss();
							}
							
						}).execute();
					}
				});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						alertDialog.dismiss();
					}
				});
		AlertDialog builder1 = builder.create();
		builder1.setIcon(android.R.drawable.ic_dialog_info);
		alertDialog = builder1;
		alertDialog.setCancelable(false);
		alertDialog.show();
	}
	
	/**
	 * 显示覆盖标签提示框
	 * @param
	 * @author: ssu
	 * @date: 2015-6-18 下午5:40:21
	 */
	private void showCoverScanTagDialog(final StartMaintenanceTaskItem item) {
		if (null != alertDialog) {
			alertDialog.dismiss();
			alertDialog = null;
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.scan_tag_alarm_cover);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						showScanTagDialog(item.getMaintainItem(), item.getTagInfo());
						alertDialog.dismiss();
					}
				});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogInterface, int i) {
						alertDialog.dismiss();
					}
				});
		AlertDialog builder1 = builder.create();
		builder1.setIcon(android.R.drawable.ic_dialog_info);
		alertDialog = builder1;
		alertDialog.setCancelable(false);
		alertDialog.show();
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
	
	/**
	 * byte转换为十进制
	 * @param
	 * @author: ssu
	 * @date: 2015-6-23 下午3:41:11
	 */
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
	
	@Override
	public void onNewIntent(Intent intent) {
		LogUtils.d(LOG_TAG, "onNewIntent", "onNewIntent");
		setIntent(intent);
		resolveIntent(intent);
	} 
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			switch(requestCode) {
			case 1:
				updateMaintenanceItemStatus((WeiBaoItemStatus)intent.getSerializableExtra("status"), (String)intent.getSerializableExtra("comments"), (String)intent.getSerializableExtra("tagId"));
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		if (null != mPullToRefreshLoadView) {
			mPullToRefreshLoadView.destory();
			mPullToRefreshLoadView = null;
		}
		super.onDestroy();
	}
}
