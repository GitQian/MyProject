package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.MyEnumAdapterFactory;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskList;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail.MaintenanceStatus;
import com.chinacnit.elevatorguard.mobile.bean.QueryMaintenanceTaskIsStart;
import com.chinacnit.elevatorguard.mobile.bean.ResponseMaintenanceAlert;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ztkj.base.business.NetCommonActivity;
import com.ztkj.componet.JGoodListView;
import com.ztkj.componet.JGoodListView.IXListViewListener;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestMaintenanceAlert;
import com.ztkj.data.request.RequestMaintenanceListBody;
import com.ztkj.data.request.RequestSign;
import com.ztkj.data.request.RequestStart;
import com.ztkj.data.request.RequestWeixiuJieGuoFankui;
import com.ztkj.data.response.MaintenanceBean;
import com.ztkj.dialog.DialogBottom_Alrt;
import com.ztkj.dialog.DialogExit;
import com.ztkj.dialog.DialogBottom_Alrt.DialogPictureSelMCallback;
import com.ztkj.dialog.DialogExit.DialogExitcallback;
import com.ztkj.dialog.PopuMantenanceFilter;
import com.ztkj.dialog.PopuMantenanceFilter.PopuItemClickListener;
import com.ztkj.tool.Tool;

public class MaintenanceTaskActivity extends NetCommonActivity implements OnItemClickListener, IXListViewListener{
	private RelativeLayout rela;
	private TextView tvTitle;
	/**
	 * 查询维保任务
	 */
	private final int ACCEPT_QUERY_TASK = 0X0001;
	/**
	 * 查询维保报警
	 */
	private final int ACCEPT_QUERY_ALERT = 0x0002;
	/**
	 * 查询任务是否开始
	 */
	private final int ACCEPT_TASK_START = 0x0003;
	/**
	 * 是否签到过
	 */
	private final int ACCEPT_SIGN=0x0004;
	/**
	 * 提交
	 */
	private final int ACCEPT_COMMIT=0x0005;
	private boolean isTaskComplete,isAlertComplete;
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	
	private ArrayList<MyBean> listBean,listOriginal,listTemp;
	private MyAdapter adapter;
	/**
	 * 0=全部，1=尚未维保，2=维保延期，3=故障报警
	 */
	private int mCurentType=0;
	private PopuMantenanceFilter mPopu;
	private MaintenanceTaskListDetail mItemTaskBean;
	private ResponseMaintenanceAlert mItemAlertBean;
	private DialogExit dialogExit;
	private DialogBottom_Alrt dialogAlert;
	private boolean startMode;
	protected RequestWeixiuJieGuoFankui mRequestBody;
	private JGoodListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintennance_task);
		initView();
		initDialog();
		accept(0);
	}
	private void initView(){
		tvTitle=(TextView)findViewById(R.id.tv_selectmycarbrand_title);
		rela=(RelativeLayout)findViewById(R.id.relaTitle);
		findViewById(R.id.tv_right).setOnClickListener(this);
		findViewById(R.id.rb_msg).setOnClickListener(this);
		findViewById(R.id.rb_settting).setOnClickListener(this);
		listView=(JGoodListView)findViewById(R.id.viewContent);
		listView.setPageSize(Integer.MAX_VALUE/2);
		listView.setPullLoadEnable(false);
		listView.setXListViewListener(this);
		adapter=new MyAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
	}
	private void initDialog(){
		mPopu=new PopuMantenanceFilter(this);
		mPopu.setPopuItemClickListener(new PopuItemClickListener() {
			
			@Override
			public void popuItemClick(int position,String str) {
				// TODO Auto-generated method stub
				tvTitle.setText(str);
				mCurentType=position;
				filterList();
				adapter.notifyDataSetChanged();
			}
		});
		dialogExit=new DialogExit(this);
		dialogExit.setDialogExitcallback(new DialogExitcallback() {
			
			@Override
			public void btnConfirm(int tag) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void btnCancel(int tag) {
				// TODO Auto-generated method stub
				
			}
		});
		dialogAlert = new DialogBottom_Alrt(this);
		dialogAlert.setDialogPictureSelMCallback(new DialogPictureSelMCallback() {
			
			@Override
			public void onDialogClick(int rid, String content) {
				mRequestBody = new RequestWeixiuJieGuoFankui();
				if(rid == R.id.btn_no){
					mRequestBody.setStatusCode(5);
				}else if (rid == R.id.btn_yes) {
					mRequestBody.setStatusCode(4);
				}
				switch (rid) {
				case R.id.btn_no:
				case R.id.btn_yes:
//					dialog.edt_content.getTag(key)
					dialogAlert.edt_content.setText("");
					mRequestBody.setUserId(""+mLoginDetail.getUid());
					mRequestBody.setMalfunctionId(mItemAlertBean.getMalfunctionId());
					mRequestBody.setRemark(content);
					mRequestBody.setUserName(mLoginDetail.getUserName());
					mRequestBody.setProcessingDtm(Tool.getTime());
					mRequestBody.setTaskId(mItemAlertBean.getTaskId());
					accept(ACCEPT_COMMIT);
				}
			}
		});
	}
	private void filterList(){
		
		 // 实体类;尚未维保=0; 维保完成 =1;维保延期 =2
		int type=0;
		switch (mCurentType) {
		case 0:
			type=-2;
			break;
		case 1:
			type=0;
			break;
		case 2:
			type=2;
			break;
		case 3:
			type=-1;
			break;
		default:
			break;
		}
		
		
		if(type==-2){
			listBean=listOriginal;
		}else{
			listBean=new ArrayList<MyBean>();
			for(int i=0;i<listOriginal.size();i++){
				if(listOriginal.get(i).type==type){
					listBean.add(listOriginal.get(i));
				}
			}
		}
		
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.rb_msg:
			PageTurnUtil.gotoActivity(this, MessageNetActivity.class, false);
			break;
		case R.id.rb_settting:
			PageTurnUtil.gotoActivity(this, SettingsActivity.class, false);
			break;
		case R.id.tv_right:
			if(Tool.isNullList(listOriginal)){
				Tool.toastShow(this, "暂无任务列表");
				return ;
			}
			mPopu.showAsDropDown(rela, mCurentType);
			break;
		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parentView, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		MyBean bean = (MyBean) parentView.getAdapter().getItem(position);
		switch (bean.type) {
		case -1://报警
			mItemAlertBean=bean.alertBean;
			dialogAlert.show();
			break;
		default://维保任务
			mItemTaskBean=bean.taskBean;
			accept(ACCEPT_TASK_START);
			break;
		}
		
		
	}
	private void accept(int state){
		switch (state) {
		case 0://查询维保任务报维保警及
			isTaskComplete=false;
			isAlertComplete=false;
			
			RequestMaintenanceAlert bodyAlert = new RequestMaintenanceAlert();
			bodyAlert.setPage(1);
			bodyAlert.setRows(Integer.MAX_VALUE/2);
			bodyAlert.setUserId(""+mLoginDetail.getUid());
			Request requestAlert = new Request(bodyAlert, "/apps/biz/liftMaintainContentController/queryMalfunctionTask", ACCEPT_QUERY_ALERT);
			getData(requestAlert);
			
			RequestMaintenanceListBody body = new RequestMaintenanceListBody();
			body.setPage(1);
			body.setRows(Integer.MAX_VALUE/2);
			Request request = new Request(body, "/apps/biz/liftMaintainContentController/queryLiftMaintainTask", ACCEPT_QUERY_TASK);
			getData(request);
			
			break;
		case ACCEPT_TASK_START://查询维保任务是否开始
			setmDismissPro(false);
			getData(new Request(new RequestStart(mItemTaskBean.getPlanId()+""), "/apps/biz/liftMaintainContentController/isPlanStarted",ACCEPT_TASK_START),true);
			break;
		case ACCEPT_SIGN:
			setmDismissPro(true);
			RequestSign rBody=new RequestSign();
			rBody.setPlanId(""+mItemTaskBean.getPlanId());
			getData(new Request(rBody, "/apps/biz/liftMaintainContentController/startLiftMaintainPlan",ACCEPT_SIGN),true);
			break;
		case ACCEPT_COMMIT:
			getData(new Request(mRequestBody, "/apps/biz/liftMaintainContentController/malfunctionResponses", ACCEPT_COMMIT, true), true);
			break;
		default:
			break;
		}
		
		
		
	}
	@Override
	public void netResultFailed(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultFailed(result, request);
		switch (request.getCode()) {
		case ACCEPT_QUERY_TASK:
		case ACCEPT_QUERY_ALERT:
			listView.onCompleted(1);
			break;

		default:
			break;
		}
		
	}
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultSuccess(result, request);
		switch (request.getCode()) {
		case ACCEPT_QUERY_TASK:
			dealQueryTask(result);
			break;
		case ACCEPT_QUERY_ALERT:
			dealQueryAlert(result);
			break;
		case ACCEPT_TASK_START:
			dealTaskStart(result);
			break;
		case ACCEPT_SIGN:
			dealSign(result);
			break;
		case ACCEPT_COMMIT:
			Tool.toastShow(this, "反馈成功");
			accept(0);
			break;
		default:
			break;
		}
		
		
		
	}
	
	private void dealSign(String result) {
		List<MaintenanceBean> mListBean=null;
		try {
			JSONObject jo=new JSONObject(result).getJSONObject("object");
			Gson gson =new Gson();
			mListBean=gson.fromJson(jo.getJSONArray("maintainContent").toString(),new TypeToken<ArrayList<MaintenanceBean>>() {}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Tool.isNullList(mListBean)){
			Tool.toastShow(mActivity, "暂无数据");
			return ;
		}
		
		boolean isSigned = false;
		//如果已经签到过的，不用弹出 SignActivity界面。
		for (MaintenanceBean bean : mListBean) {
			String strVal1 = bean.getStrVal1();
			if(strVal1!=null && strVal1.equals("LIFTMAINTAINSIGN") && bean.getStatusCode()!=null && bean.getStatusCode().equals("0")){
				isSigned = true;
				break;
			}
		}
		proDismiss();
		
		if(isSigned){//直接跳转。
			Log.v(TAG, "已经签到过了，直接跳转到下下个页面");
			Map<String, Serializable> map = new HashMap<String, Serializable>();
			map.put("planId", mItemTaskBean.getPlanId());
			if(startMode){//维保任务已开始
				PageTurnUtil.gotoActivity(mActivity, StartMaintenanceTaskDetailsActivity.class, false, map);
			}else{
				map.put("locationAddress", mItemTaskBean.getLocationAddress());
				map.put("maintainCycle", mItemTaskBean.getMaintainCycle());
				PageTurnUtil.gotoActivity(mActivity, StartMaintenanceActivity.class, false, map);
			}
		}else{//显示签到activity；
			Intent intent =new Intent(mActivity, SignActivity.class);
			intent.putExtra("planId",mItemTaskBean.getPlanId());
			intent.putExtra("startMode",startMode);
			intent.putExtra("locationAddress",mItemTaskBean.getLocationAddress());
			intent.putExtra("maintainCycle",mItemTaskBean.getMaintainCycle());
			startActivity(intent);
		}
	}
	private void dealTaskStart(String result) {
		// TODO Auto-generated method stub
		String strObj="";
		try {
			JSONObject jobj = new JSONObject(result);
			strObj = jobj.getString("object");
		} catch (Exception e) {
			// TODO: handle exception
		}
		Gson gson =new Gson();
		QueryMaintenanceTaskIsStart beanStart =gson.fromJson(strObj, QueryMaintenanceTaskIsStart.class);
		
		if (beanStart.getFlag()) { // 当前日期在计划维保时间段内
			//提前查询 该planid是否 已经签到过，如果已经签到过无需 弹出下一个签到界面。
			startMode = beanStart.getStartMode();
			accept(ACCEPT_SIGN);
			
		} else {
			proDismiss();
			dialogExit.setContent(getResources().getString(R.string.maintenance_task_no_start));
			dialogExit.show();
		}
		
		
		
	}
	private void dealQueryTask(String result) {
		// TODO Auto-generated method stub
		String strObj="";
		try {
			JSONObject jobj = new JSONObject(result);
			strObj = jobj.getString("object");
		} catch (Exception e) {
			// TODO: handle exception
		}
		GsonBuilder gb = new GsonBuilder(); 
		gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
		Gson gson = gb.create();
		MaintenanceTaskList bean = gson.fromJson(strObj, MaintenanceTaskList.class);
		
		if(isAlertComplete){
			
			if(bean!=null&&bean.getRows()!=null){
				for(int i=0;i<bean.getRows().size();i++){
					MyBean mBean=new MyBean();
					MaintenanceTaskListDetail task =bean.getRows().get(i);
					mBean.type=task.getStatusCode().getValue();
					mBean.taskBean=task;
					listTemp.add(mBean);
				}
			}
			
			listOriginal=listTemp;
			filterList();
			adapter.notifyDataSetChanged();
			listView.onCompleted(1);
			
		}else{
			listTemp=new ArrayList<MyBean>();
			if(bean!=null&&bean.getRows()!=null){
				for(int i=0;i<bean.getRows().size();i++){
					MyBean mBean=new MyBean();
					MaintenanceTaskListDetail task =bean.getRows().get(i);
					mBean.type=task.getStatusCode().getValue();
					mBean.taskBean=task;
					listTemp.add(mBean);
				}
			}
		}
		
		isTaskComplete=true;
	}
	
	private void dealQueryAlert(String result) {
		// TODO Auto-generated method stub
		String rows="";
		try{
			JSONObject jobj = new JSONObject(result);
			JSONObject object = jobj.getJSONObject("object");
			if(object.has("rows")){
				rows = object.getString("rows");
			}
		}catch(Exception e){
		}
		GsonBuilder gb = new GsonBuilder(); 
		gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
		Gson gson = gb.create();
		ArrayList<ResponseMaintenanceAlert> list= gson.fromJson(rows,  new TypeToken<List<ResponseMaintenanceAlert>>() {}.getType());
		
		
		if(isTaskComplete){
			ArrayList<MyBean> listX =new ArrayList<MyBean>();
			if(!Tool.isNullList(listTemp)){
				listX.addAll(listTemp);
			}
			
			listTemp=new ArrayList<MyBean>();
			
			if(!Tool.isNullList(list)){
				for(int i=0;i<list.size();i++){
					MyBean mBean=new MyBean();
					ResponseMaintenanceAlert alert =list.get(i);
					mBean.type=-1;
					mBean.alertBean=alert;
					listTemp.add(mBean);
				}
			}
			
			if(!Tool.isNullList(listX)){
				listTemp.addAll(listX);
			}
			
			listOriginal=listTemp;
			filterList();
			adapter.notifyDataSetChanged();
			listView.onCompleted(1);
			
		}else{
			listTemp=new ArrayList<MyBean>();
			if(!Tool.isNullList(list)){
				for(int i=0;i<list.size();i++){
					MyBean mBean=new MyBean();
					ResponseMaintenanceAlert alert =list.get(i);
					mBean.type=-1;
					mBean.alertBean=alert;
					listTemp.add(mBean);
				}
			}
		}
		
		isAlertComplete=true;
		
	}
	private class MyBean {
		/**
		 * -1=维保报警，其他=维保任务
		 */
		int type;
		MaintenanceTaskListDetail taskBean;
		ResponseMaintenanceAlert alertBean;
	}
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listBean==null?0:listBean.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listBean.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		@Override
		public int getItemViewType(int position) {
			// TODO Auto-generated method stub
			if(listBean.get(position).type==-1){
				return 0;
			}
			return 1;
		}
		@Override
		public int getViewTypeCount() {
			// TODO Auto-generated method stub
			return 2;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHoldTask holdTask=null;
			ViewHoldAlert holdAlert =null;
			if(convertView==null){
				//报警
				if(getItemViewType(position)==0){
					holdAlert = new ViewHoldAlert();
					convertView = View.inflate(mActivity, R.layout.item_maintenance_alert_listview	, null);
					holdAlert.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
					holdAlert.tv_elevatorname = (TextView) convertView.findViewById(R.id.tv_elevatorname);
					holdAlert.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
					holdAlert.tv_reson = (TextView) convertView.findViewById(R.id.tv_reson);
					convertView.setTag(holdAlert);
				}else{
					holdTask = new ViewHoldTask();
					convertView = View.inflate(parent.getContext(), R.layout.item_4_listview_maintenancetask	, null);
					holdTask.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
					holdTask.tv_elevatorname = (TextView) convertView.findViewById(R.id.tv_elevatorname);
					holdTask.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
					holdTask.tv_cycle = (TextView) convertView.findViewById(R.id.tv_cycle);
					holdTask.tv_maintenancestate = (TextView) convertView.findViewById(R.id.tv_maintenancestate);
					
					convertView.setTag(holdTask);
				}
			}else{
				if(getItemViewType(position)==0){
					holdAlert=(ViewHoldAlert)convertView.getTag();
				}else{
					holdTask=(ViewHoldTask)convertView.getTag();
				}
			}
			
			if(getItemViewType(position)==0){
				ResponseMaintenanceAlert bean =listBean.get(position).alertBean;
				holdAlert.tv_time.setText(bean.getCreateTime() +"");
				holdAlert.tv_elevatorname.setText(bean.getLiftName());
				holdAlert.tv_location.setText(bean.getLocationAddress());
				holdAlert.tv_reson.setText(bean.getMalfunctionType());
			}else{
				MaintenanceTaskListDetail bean =listBean.get(position).taskBean;
				holdTask.tv_time.setText(bean.getPlanBeginDate());
				holdTask.tv_elevatorname.setText(bean.getLiftName());
				holdTask.tv_location.setText(bean.getLocationAddress());
				holdTask.tv_cycle.setText(Cycle.getValueByKey(bean.getMaintainCycle()) != 0 ? parent.getContext().getResources().getString(Cycle.getValueByKey(bean.getMaintainCycle())) : "");
				holdTask.tv_maintenancestate.setText(bean.getStatusCode()==MaintenanceStatus.NOMAINTENANCE?"尚未维保":bean.getStatusCode()==MaintenanceStatus.MAINTENANCEEXTENSION?"维保延期":"维保完成");
				holdTask.tv_maintenancestate.setTextColor(bean.getStatusCode() == MaintenanceStatus.NOMAINTENANCE?0xff60B0FF:bean.getStatusCode() == MaintenanceStatus.MAINTENANCEEXTENSION?0xffFFAF37:Color.GREEN);
			}
			
			return convertView;
		}
		
	}
	class ViewHoldTask{
		TextView tv_time,tv_elevatorname,tv_location,tv_cycle,tv_maintenancestate;
	}
	class ViewHoldAlert{
		TextView tv_time,tv_elevatorname,tv_location,tv_reson;
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		accept(0);
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
}
