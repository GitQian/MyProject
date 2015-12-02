package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.MyEnumAdapterFactory;
import com.chinacnit.elevatorguard.mobile.adapter.WeibaoRecordAdapter;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoRecordBean;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoRecordList;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.ui.view.CustomDatePickerDialog;
import com.chinacnit.elevatorguard.mobile.ui.view.CustomDatePickerDialog.onDateListener;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ztkj.base.business.NetCommonActivity;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestWeiBaoRecord;
import com.ztkj.tool.Tool;
/**
 * 维保记录   Activity  显示 维保记录的List列表
 * @author liucheng  liucheng187@qq.com
 */
public class WeiBaoRecordActivity extends NetCommonActivity implements OnClickListener, OnItemClickListener {
	//维保记录适配器
	private WeibaoRecordAdapter mAdapter;
	//维保记录列表
	private List<WeiBaoRecordBean> mList = new ArrayList<WeiBaoRecordBean>();
	
	private ListView mListView;
	//返回按钮
	private LinearLayout back;
	/** 标题栏维保时间按钮 */
	private RelativeLayout title_right_rl;
	//返回按钮文本
	private TextView back_textview;
	
	private final Calendar c = Calendar.getInstance();
	//电梯Id
	private long mLiftid;
	
	/** 自定义时间选择控件 */
	private CustomDatePickerDialog mCustomDatePickerDialog;
	/** 输入的查询日期 */
	private String mInputDate;
	
	
	public static final int CODE_REFRESH = 0x0003;
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	private int mPosition = 0; //默认选中第一个位置。 从0开始
	private TextView tv_half_month,tv_half_jidu,tv_half_year,tv_year;
	private View tv_half_month_indicator,tv_half_jidu_indicator,tv_half_year_indicator,tv_year_indicator;
	private TextView[] tv_item = new TextView[4];
	private View[] tv_item_indicator = new View[4];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weibao_records);
		mLiftid = getIntent().getLongExtra("id", 0);
		initView();
	}
	

	/**
	 * 界面初始化
	 * 
	 * @author: pyyang
	 * @date 创建时间：2015年5月28日 上午11:34:26
	 */
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
		
		back = (LinearLayout) findViewById(R.id.back_ll);
		back.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.maintenance_records);
		title_right_rl = (RelativeLayout) findViewById(R.id.wb_time_rl);
//		title_right_rl.setVisibility(View.VISIBLE);
//		date_textview = (TextView) findViewById(R.id.wb_time_rl_textview);
		mCustomDatePickerDialog = new CustomDatePickerDialog(WeiBaoRecordActivity.this, Calendar.getInstance());
		mCustomDatePickerDialog.addDateListener(new onDateListener() {
			
			@Override
			public void dateFinish(Calendar c) {
//				Toast.makeText(getApplicationContext(), DateFormat.format("yyy-MM-dd", c), Toast.LENGTH_SHORT).show();
				mInputDate = DateFormat.format("yyyy-MM-dd", c).toString();
				accept(CODE_REFRESH);
			}
		});
		title_right_rl.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mCustomDatePickerDialog.show();
			}
		});
		
		mAdapter = new WeibaoRecordAdapter(mList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		selectPosition(mPosition);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		accept(CODE_REFRESH);
	}
	
	private void accept(int state){
		if (state == CODE_REFRESH) {
			if (null == mLoginDetail) {
				return;
			}
			RequestWeiBaoRecord body = new RequestWeiBaoRecord();
			body.setLiftId(mLiftid);
			getData(new Request(body, "/apps/biz/liftMaintainContentController/findLiftMaintainPlanByLiftId/", CODE_REFRESH), true);
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
		
		List<WeiBaoRecordBean> list_0 = new ArrayList<WeiBaoRecordBean>();
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
		
		for (WeiBaoRecordBean i : mList) {
			if(i.getMaintainCycle() == slctCycle){
				list_0.add(i);
			}
		}
		
		WeibaoRecordAdapter adapter = (WeibaoRecordAdapter) mListView.getAdapter();
		if(adapter == null ||(!is0Select&&mPosition==0)||(!is1Select&&mPosition==1)||(!is2Select&&mPosition==2)||(!is3Select&&mPosition==3)){
			mAdapter = new WeibaoRecordAdapter( list_0);
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
			List<WeiBaoRecordBean> existList = adapter.getList();
			existList.clear();
			existList.addAll(list_0);
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		WeiBaoRecordBean bean = (WeiBaoRecordBean) parent.getAdapter().getItem(position);
//		Map<String, Serializable> param = new HashMap<String, Serializable>();
//		param.put("planId", bean.getPlanId());
//		PageTurnUtil.gotoActivity(this, WeibaoRecordDetailActivity.class, false, param);
		
		Bundle b = new Bundle();
		b.putSerializable(WeiBaoRecordBean.class.getName(), bean);
		Tool.startActivity(mActivity, WeibaoRecordDetailActivity.class, b);
	}

	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
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
	
	@Override
	public void netResultSuccess(String result, Request request) {
		super.netResultSuccess(result, request);
		switch (request.getCode()) {
		case CODE_REFRESH:
			try {
				Log.i(TAG, "===========code====CODE_REFRESH");
				JSONObject jobj = new JSONObject(result);
				String objStr = jobj.getString("object");
				GsonBuilder gb = new GsonBuilder(); 
				gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
				Gson gson = gb.create();
				WeiBaoRecordList bean = gson.fromJson(objStr, WeiBaoRecordList.class);
				if(bean!=null && bean.getRows()!=null && bean.getRows().size() > 0){
					mList.clear();
					mList.addAll(bean.getRows());
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
}
