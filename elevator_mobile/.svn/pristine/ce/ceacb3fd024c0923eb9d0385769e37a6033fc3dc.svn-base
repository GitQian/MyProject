package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.MyEnumAdapterFactory;
import com.chinacnit.elevatorguard.mobile.adapter.SettingsTagAdapter;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.TagList;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ztkj.base.business.NetCommonActivity;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestLockLift;
import com.ztkj.data.request.RequestSettingTagList;
import com.ztkj.tool.Tool;

/**
 * 设置标签
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月3日 下午5:14:43
 */
public class SettingsTagActivity extends NetCommonActivity implements OnClickListener, OnItemClickListener {
	public static final int CODE_UNLOCK = 0x0000;
	public static final int CODE_LOCK = 0x0001;
	public static final int CODE_REFRESH = 0x0003;
	private ListView mListView;
	private LinearLayout back;
	private TextView back_textview;
	private SettingsTagAdapter mAdapter;
	private List<TagListDetail> list = new ArrayList<TagListDetail>();
	private long mLiftid;
	private TextView tvLock;
	private boolean isLock=true;
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	private int mPosition = 0; //默认选中第一个位置。 从0开始
	private TextView tv_half_month,tv_half_jidu,tv_half_year,tv_year;
	private View tv_half_month_indicator,tv_half_jidu_indicator,tv_half_year_indicator,tv_year_indicator;
	
	private TextView[] tv_item = new TextView[4];
	private View[] tv_item_indicator = new View[4];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_tag);
		mLiftid = (Long) getIntent().getSerializableExtra("liftId");
		initView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		accept(CODE_REFRESH);
	}
	
	private void initView() {
		tv_half_month = (TextView) findViewById(R.id.tv_half_month);
		tv_half_jidu = (TextView) findViewById(R.id.tv_half_jidu);
		tv_half_year = (TextView) findViewById(R.id.tv_half_year);
		tv_year = (TextView) findViewById(R.id.tv_year);
		tv_item[0] = tv_half_month;
		tv_item[1] = tv_half_jidu;
		tv_item[2] = tv_half_year;
		tv_item[3] = tv_year;
		tv_half_month_indicator = (View) findViewById(R.id.tv_half_month_indicator);
		tv_half_jidu_indicator = (View) findViewById(R.id.tv_half_jidu_indicator);
		tv_half_year_indicator = (View) findViewById(R.id.tv_half_year_indicator);
		tv_year_indicator = (View) findViewById(R.id.tv_year_indicator);
		tv_item_indicator[0] = tv_half_month_indicator;
		tv_item_indicator[1] = tv_half_jidu_indicator;
		tv_item_indicator[2] = tv_half_year_indicator;
		tv_item_indicator[3] = tv_year_indicator;
		tv_half_month.setOnClickListener(this);
		tv_half_jidu.setOnClickListener(this);
		tv_half_year.setOnClickListener(this);
		tv_year.setOnClickListener(this);
		
		mListView = (ListView) findViewById(R.id.listview);
		tvLock=(TextView)findViewById(R.id.tvLock);
		back = (LinearLayout) findViewById(R.id.back_ll);
		back.setOnClickListener(this);
		tvLock.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.settings_tag);
		
		mListView.setOnItemClickListener(this);
		selectPosition(mPosition);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		case R.id.tvLock:
			if(isLock){
				accept(CODE_UNLOCK);
			}else{
				accept(CODE_LOCK);
			}
			break;
		case R.id.tv_half_month:
			selectPosition(0);
			break;
		case R.id.tv_half_jidu:
			selectPosition(1);
			break;
		case R.id.tv_half_year:
			selectPosition(2);
			break;
		case R.id.tv_year:
			selectPosition(3);
			break;
		}
	}
	
	
	private boolean is0Select; //该界面是否曾经 进入过。
	private boolean is1Select;
	private boolean is2Select;
	private boolean is3Select;
	/**
	 * 选中位置 position
	 * @param position
	 */
	private void selectPosition(int position){
		mPosition = position;
		for (int i = 0; i < tv_item.length; i++) {
			if(i == mPosition){
				tv_item[i].setTextColor(getResources().getColor(R.color.red_melon));
				tv_item_indicator[i].setVisibility(View.VISIBLE);
			}else{
				tv_item[i].setTextColor(0xff555555);
				tv_item_indicator[i].setVisibility(View.INVISIBLE);
			}
		}
		
		List<TagListDetail> list_0 = new ArrayList<TagListDetail>();
		Cycle slctCycle = Cycle.HALFMONTH;
		switch (mPosition) {
		case 0:
			slctCycle = Cycle.HALFMONTH;
			break;
		case 1: //
			slctCycle = Cycle.QUARTER;
			break;
		case 2:
			slctCycle = Cycle.HALFYEAR;
			break;
		case 3:
			slctCycle = Cycle.YEAR;
			break;
		}
		
		for (TagListDetail i : list) {
			if(slctCycle==Cycle.HALFMONTH&&i.getIntVal1()==Cycle.SIGN ){
				list_0.add(i);
				continue;
			}
			
			if(i.getIntVal1() == slctCycle){
				list_0.add(i);
			}
		}
		
		SettingsTagAdapter adapter = (SettingsTagAdapter) mListView.getAdapter();
		if(adapter == null ||(!is0Select&&mPosition==0)||(!is1Select&&mPosition==1)||(!is2Select&&mPosition==2)||(!is3Select&&mPosition==3)){
			mAdapter = new SettingsTagAdapter(mActivity, list_0);
			mListView.setAdapter(mAdapter);
			if(mPosition==0){
				is0Select = true;
			}else if (mPosition==1) {
				is1Select = true;
			}else if (mPosition==2) {
				is2Select = true;
			}else if (mPosition == 3) {
				is3Select = true;
			}
		}else{
			List<TagListDetail> existList = adapter.getList();
			existList.clear();
			existList.addAll(list_0);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	private void accept(int state){
		if(state == CODE_UNLOCK || state==CODE_LOCK){
			RequestLockLift requestLock=new RequestLockLift();
			requestLock.setLiftId(mLiftid+"");
			switch (state) {
			case CODE_UNLOCK://点击解锁
				requestLock.setFinish("0");
				getData(new Request(requestLock, "/apps/biz/liftMaintainContentController/bindFinished",0), true);
				break;
			case CODE_LOCK://点击锁定
				requestLock.setFinish("1");
				getData(new Request(requestLock, "/apps/biz/liftMaintainContentController/bindFinished",1), true);
				break;
			}
		}else if (state == CODE_REFRESH) {
			if (null == mLoginDetail) {
				return;
			}
			RequestSettingTagList body = new RequestSettingTagList();
			body.setLiftId(mLiftid);
			getData(new Request(body, "/apps/biz/liftMaintainContentController/findLiftMaintainContents", CODE_REFRESH), true);
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int arg2, long arg3) {
		if(isLock){
			Tool.toastShow(this, "请先点击右上角按钮进行设置");
			return ;
		}
		
		TagListDetail bean = (TagListDetail) parent.getAdapter().getItem(arg2);
		
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("liftId", mLiftid);
		map.put("keyValue", bean.getKeyValue());
		map.put("maintainItem", bean.getKeyCname());
		map.put("maintainClaim", bean.getStrVal1());
		map.put("tagId", bean.getTagInfo());
		PageTurnUtil.gotoActivity(mActivity, ComponentActivity.class, false, map);
	}
	
	@Override
	public void netResultSuccess(String result, Request request) {
		super.netResultSuccess(result, request);
		switch (request.getCode()) {
		case CODE_UNLOCK:
		case CODE_LOCK:
			Log.i(TAG, "===========code====request.getCode()："+request.getCode());
			setLockChanged(request.getCode());
			break;
		case CODE_REFRESH:
			try {
				Log.i(TAG, "===========code====CODE_REFRESH");
				JSONObject jobj = new JSONObject(result);
				String objStr = jobj.getString("object");
				GsonBuilder gb = new GsonBuilder(); 
				gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
				Gson gson = gb.create();
				TagList bean = gson.fromJson(objStr, TagList.class);
				if(bean!=null && bean.getRows()!=null && bean.getRows().size() > 0){
					tvLock.setVisibility(View.VISIBLE);
					list.clear();
					list.addAll(bean.getRows());
					if(!Tool.isNullList(bean.getGroups())&&"1".equals(bean.getGroups().get(0).getFinish())){
						setLockChanged(CODE_LOCK);
					}else{
						setLockChanged(CODE_UNLOCK);
					}
					for (TagListDetail i : list) {
						System.out.println("obj:"+i);
					}
					selectPosition(mPosition); //会自动notify的。
				}else{
					Tool.toastShow(mActivity, getResources().getString(R.string.no_data));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	@Override
	public void netResultFailed(String result, Request request) {
		super.netResultFailed(result, request);
		Log.v(TAG, "==netResultFailed=---result:"+result);
	}
	
	/**
	 * 0表示未完成，1=完成
	 * @param state
	 */
	private void setLockChanged(int state){
		switch (state) {
		case CODE_UNLOCK:
			tvLock.setText("完成");
			isLock=false;
			break;
		case CODE_LOCK:
			tvLock.setText("开始");
			isLock=true;
			break;
		}
	}
}
