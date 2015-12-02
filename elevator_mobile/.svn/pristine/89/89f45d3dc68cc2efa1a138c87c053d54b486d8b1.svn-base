package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ztkj.base.business.NetCommonActivity;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestSign;
import com.ztkj.data.response.MaintenanceBean;
import com.ztkj.dialog.DialogExit;
import com.ztkj.dialog.DialogExit.DialogExitcallback;
import com.ztkj.tool.Tool;

/**
 * 
 * 签到activity<br>  那个对话框
 * Intent key="planId" values=Long<br>
 * Intent key="startMode" values=boolean 为false的时候需要下面两个<br>
 * Intent key="locationAddress" values=String<br>
 * Intent key="maintainCycle" values=Serializable<br>
 * 
 */
public class SignActivity extends NetCommonActivity {
	private TextView tvResult;
	private NfcAdapter mNfcAdapter;
	private PendingIntent mPendingIntent;
	private DialogExit dialogExit;
	private String strTag;
	private long planId;
	private final int ACCEPT_QUERY=0;
	private final int ACCEPT_COMMIT=1;
	private ArrayList<MaintenanceBean> listBean;
	private boolean isMatch;
	private boolean isSign=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		initView();
		initDialog();
		initData();
	}

	private void initView() {
		Window win = getWindow();
		win.setWindowAnimations(R.style.bottomAnimDialog);
		win.getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams lp = win.getAttributes();
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		win.setAttributes(lp);
		findViewById(R.id.tvExit).setOnClickListener(this);
		findViewById(R.id.tvSign).setOnClickListener(this);
		tvResult = (TextView) findViewById(R.id.viewContent);
	}

	private void initDialog() {
		dialogExit = new DialogExit(this);
		dialogExit.setKeyBackDismiss(false);
		dialogExit.setCanceledOnTouchOutside(false);
		dialogExit.setDialogExitcallback(new DialogExitcallback() {

			@Override
			public void btnConfirm(int tag) {
				// TODO Auto-generated method stub
				
				if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
					startNFCSetting();
				}else{
					Intent intent = new Intent(Settings.ACTION_SETTINGS);
					startActivity(intent);
				}
			}

			@Override
			public void btnCancel(int tag) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void startNFCSetting(){
		Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
		startActivity(intent);
	}
	
	

	private void initData() {
		planId =getIntent().getLongExtra("planId",0);
		mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
		if (mNfcAdapter != null) {
			if (!mNfcAdapter.isEnabled()) {
				dialogExit.setContent("系统检测到您的手机未打开NFC功能,是否立即打开");
				dialogExit.show();
			}
			mPendingIntent = PendingIntent.getActivity(this, 0,
					new Intent(this, getClass())
							.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		} else {
			doFinish("您的手机不支持NFC功能");
		}
	}
	@Override  
    public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_DOWN && isOutOfBounds(this, event)) {  
            return true;  
        }  
        return super.onTouchEvent(event);  
    }  
  
    private boolean isOutOfBounds(Activity context, MotionEvent event) {  
        final int x = (int) event.getX();  
        final int y = (int) event.getY();  
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();  
        final View decorView = context.getWindow().getDecorView();  
        return (x < -slop) || (y < -slop)|| (x > (decorView.getWidth() + slop))|| (y > (decorView.getHeight() + slop));  
    }  
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (mNfcAdapter != null) {
			if (!mNfcAdapter.isEnabled()) {
				dialogExit.setContent("系统检测到您的手机未打开NFC功能,是否立即打开");
				dialogExit.show();
				return ;
			}
		} else {
			doFinish("您的手机不支持NFC功能");
		}
		
		mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		resolveIntent(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvExit:
			finish();
			break;
		case R.id.tvSign:
			if(isSign){
				Tool.toastShow(this, "正在请求中,请稍候");
				return ;
			}
			if(Tool.isNullString(strTag)){
				Tool.toastShow(this, "请先扫描NFC签到标签");
				return ;
			}
			
			if(isMatch){
				accept(ACCEPT_COMMIT);
			}else{
				accept(ACCEPT_QUERY);
			}
			break;
		default:
			break;
		}
	}

	
	private void resolveIntent(Intent intent) {
		Parcelable rawArray = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
		// TAG: Tech [android.nfc.tech.MifareUltralight, android.nfc.tech.NfcA,android.nfc.tech.Ndef, android.nfc.tech.NdefFormatable]
		if (rawArray != null) {
			Tag mNdefMsg = (Tag) rawArray;
			tvResult.setTextColor(0xff7CB600);
			String oldTag=strTag;
			strTag=getDec(mNdefMsg.getId())+"";
			
			Log.e("*****", strTag);
			
			tvResult.setText("扫描成功");
			if(!isSign){
				if(isMatch&&strTag.equals(oldTag)){
					accept(ACCEPT_COMMIT);
				}else{
					accept(ACCEPT_QUERY);
				}
			}else{
				Tool.toastShow(this, "正在请求网络,请稍候");
			}
		} else {
			tvResult.setTextColor(0xffff8833);
			tvResult.setText("扫描失败,请重试");
			Tool.toastShow(this, "扫描失败");
		}
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
	private void accept(int state){
		isSign=true;
		RequestSign rBody=new RequestSign();
		switch (state) {
		case ACCEPT_QUERY:
			rBody.setPlanId(planId+"");
			setmDismissPro(false);
			getData(new Request(rBody, "/apps/biz/liftMaintainContentController/startLiftMaintainPlan",ACCEPT_QUERY),true);
			break;
		case ACCEPT_COMMIT:
			UserInfo mUserInfo = ConfigSettings.getUserInfo();
			
			rBody.setPlanId(planId+"");
			rBody.setTagId(strTag);
			rBody.setMobile(mUserInfo.getUserMobile());
			rBody.setComments("");
			rBody.setStatusCode("0");
			setmDismissPro(true);
			getData(new Request(rBody, "/apps/biz/liftMaintainContentController/commitMaintainContent",ACCEPT_COMMIT),true);
			
			break;
		default:
			break;
		}
		
	}

	@Override
	public void netResultFailed(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultFailed(result, request);
		isSign=false;
	}
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultSuccess(result, request);
		
		switch (request.getCode()) {
		case ACCEPT_QUERY:
			dealQuery(result);
			break;
		case ACCEPT_COMMIT:
			dealCommit();
			break;
		default:
			break;
		}
		
	}
	private void dealQuery(String result){
		try {
			Log.e(TAG	, "result："+result);
			
			JSONObject jo=new JSONObject(result).getJSONObject("object");
			Gson gson =new Gson();
			listBean=gson.fromJson(jo.getJSONArray("maintainContent").toString(),new TypeToken<ArrayList<MaintenanceBean>>() {}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(Tool.isNullList(listBean)){
			isSign=false;
			proDismiss();
			Tool.toastShow(this, "暂无数据");
			return ;
		}
		
		
		for(int i=0;i<listBean.size();i++){
			if(strTag.trim().equals(listBean.get(i).getTagInfo().trim())&&"LIFTMAINTAINSIGN".equals(listBean.get(i).getStrVal1())){
				isMatch=true;
			}
		}
		
		if(isMatch){
			Tool.toastShow(this, "匹配成功,正在提交");
			accept(ACCEPT_COMMIT);
		}else{
			isSign=false;
			proDismiss();
			tvResult.setTextColor(0xffff8833);
			tvResult.setText("匹配失败,请重试");
			Tool.toastShow(this, "匹配失败,请重试");
		}
	}
	
	private void dealCommit(){
		Tool.toastShow(this, "签到成功");
		boolean startMode=getIntent().getBooleanExtra("startMode", false);
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("planId", planId);
		if(startMode){//维保任务已开始
			PageTurnUtil.gotoActivity(this, StartMaintenanceTaskDetailsActivity.class, false, map);
		}else{
			map.put("locationAddress", getIntent().getStringExtra("locationAddress"));
			map.put("maintainCycle", getIntent().getSerializableExtra("maintainCycle"));
			PageTurnUtil.gotoActivity(this, StartMaintenanceActivity.class, false, map);
		}
		finish();
	}
}
