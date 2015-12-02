package com.chinacnit.elevatorguard.mobile.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.MaintenanceTaskAdapter;
import com.chinacnit.elevatorguard.mobile.adapter.MyEnumAdapterFactory;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskList;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail.MaintenanceStatus;
import com.chinacnit.elevatorguard.mobile.bean.QueryMaintenanceTaskIsStart;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.http.task.IResultListener;
import com.chinacnit.elevatorguard.mobile.http.task.impl.QueryMaintenanceTaskIsStartTask;
import com.chinacnit.elevatorguard.mobile.ui.activity.MaintenanceTaskHomeActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.SignActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.StartMaintenanceActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.StartMaintenanceTaskDetailsActivity;
import com.chinacnit.elevatorguard.mobile.util.CommonToast;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ztkj.base.business.NetCommonFragment;
import com.ztkj.componet.JGoodListView;
import com.ztkj.componet.JGoodListView.IXListViewListener;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestMaintenanceListBody;
import com.ztkj.data.request.RequestSign;
import com.ztkj.data.response.MaintenanceBean;
import com.ztkj.tool.Tool;

public class MaintenanceTaskListFragment extends NetCommonFragment  implements IXListViewListener,OnItemClickListener,OnCheckedChangeListener{
	private View rootView;
	private MaintenanceTaskHomeActivity mActivity;
	private final int CODE_REFERESH = 0X0001;
	private final int CODE_LOADMORE = 0x0002;
	private final int ACCEPT_QUERY = 0x0003;
	int mPage = 1;
	JGoodListView mListView;
	List<MaintenanceTaskListDetail> mData = new ArrayList<MaintenanceTaskListDetail>();
	private MaintenanceTaskAdapter mAdapter;
	private MaintenanceTaskListDetail mItemBean;
	
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	private boolean startMode;
	
	private List<MaintenanceBean> mListBean;
	

	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MaintenanceTaskHomeActivity) activity;
		mActivity.setOnCheckedChangeListener(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(rootView == null){
			rootView = View.inflate(mActivity, R.layout.fragment_maintenance_taskhome, null);
			initView();
			initdata();
		}
		ViewGroup v = (ViewGroup) rootView.getParent();
		if(v!=null ){
			v.removeView(rootView);
		}
		
		return rootView;
	}
	
	
	private void initdata() {
		mAdapter = new MaintenanceTaskAdapter(mData);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	private void initView() {
		mListView = (JGoodListView) rootView.findViewById(R.id.listview);
		mListView.setPageSize(Integer.MAX_VALUE);
		mListView.setXListViewListener(this); 
	}
	private void accept(int code){
		if(mLoginDetail == null){
			Log.e(TAG, "lalig uile .....");
			return;
		}
		switch (code) {
		case ACCEPT_QUERY:
			RequestSign rBody=new RequestSign();
			rBody.setPlanId(mItemBean.getPlanId()+"");
			getData(new Request(rBody, "/apps/biz/liftMaintainContentController/startLiftMaintainPlan",ACCEPT_QUERY),true);
			break;

		default: //刷新 和 加载更多。
			RequestMaintenanceListBody body = new RequestMaintenanceListBody();
			body.setPage(mPage);
			body.setRows(mListView.getPageSize());
			Request request = new Request(body, "/apps/biz/liftMaintainContentController/queryLiftMaintainTask", code);
			getData(request);
			break;
		}
		
	}
	
	@Override
	public void netResultFailed(String result, Request request) {
		super.netResultFailed(result, request);
		mPage = --mPage <=0?1:mPage; //无论是刷新失败还是加载更多发生失败。 没关系。
		Log.e(TAG, "失败了。。。result:"+result);
		mListView.onCompleted(mPage);
	}
	
	
//	public Type getParseType() {
//		return new TypeToken<MaintenanceTaskList>() {}.getType();
//	}
	
	@Override
	public void netResultSuccess(String result, Request request) {
		super.netResultSuccess(result, request);
		Log.i(TAG, "列表呢result: "+result);
		if(request.getCode() == CODE_REFERESH || request.getCode() == CODE_LOADMORE){
			try{
				JSONObject jobj = new JSONObject(result);
				String object = jobj.getString("object");
				GsonBuilder gb = new GsonBuilder(); 
				gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
				Gson gson = gb.create();
				MaintenanceTaskList bean = gson.fromJson(object, MaintenanceTaskList.class);
				
//				for (MaintenanceTaskListDetail maintenanceTaskListDetail : bean.getRows()) {
//					System.out.println("解析出："+maintenanceTaskListDetail);
//				}
				if(request.getCode() == CODE_REFERESH){
					mData.clear();
				}
				mData.addAll(bean.getRows());
			}catch(Exception e){
				e.printStackTrace();
				mPage -- ;
			}
			
			if(mActivity.filterType == MaintenanceTaskHomeActivity.FILTER_TYPE_ALL){
				mListView.setAdapter(new MaintenanceTaskAdapter(mData) );
			}else if (mActivity.filterType == MaintenanceTaskHomeActivity.FILTER_TYPE_NOTMAINTENANCE) {
				List<MaintenanceTaskListDetail> list = new ArrayList<MaintenanceTaskListDetail>();
				for (MaintenanceTaskListDetail bean : mData) {
					if(bean.getStatusCode() == MaintenanceStatus.NOMAINTENANCE){
						list.add(bean);
					}
				}
				mListView.setAdapter(new MaintenanceTaskAdapter(list) );
			}else if (mActivity.filterType == MaintenanceTaskHomeActivity.FILTER_TYPE_DELAY) {
				List<MaintenanceTaskListDetail> list = new ArrayList<MaintenanceTaskListDetail>();
				for (MaintenanceTaskListDetail bean : mData) {
					if(bean.getStatusCode() == MaintenanceStatus.MAINTENANCEEXTENSION){
						list.add(bean);
					}
				}
				mListView.setAdapter(new MaintenanceTaskAdapter(list) );
			}
			mListView.onCompleted(mPage);
			
		} else if(request.getCode() == ACCEPT_QUERY){
			dealQuery(result);
		}
	}

	@Override
	public void onRefresh() {
		mPage = 1;
		accept(CODE_REFERESH);
	}

	@Override
	public void onLoadMore() {
		mPage++;
		accept(CODE_LOADMORE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
		mItemBean = (MaintenanceTaskListDetail) parent.getAdapter().getItem(position);
		
		queryMaintenanceIsStart(mItemBean);
		
	}
	
	/**
	 * 查询维保任务是否开始
	 * @param
	 * @author: ssu
	 * @date: 2015-6-17 下午7:41:29
	 */
	private void queryMaintenanceIsStart(final MaintenanceTaskListDetail taskDetail) {
		if (null != mLoginDetail) {
			new QueryMaintenanceTaskIsStartTask(mActivity, R.string.loading, taskDetail.getPlanId(), new IResultListener<QueryMaintenanceTaskIsStart>() {

				@Override
				public void onSuccess(QueryMaintenanceTaskIsStart result) {
					if (null != result) {
						
						if (result.getFlag()) { // 当前日期在计划维保时间段内
							//提前查询 该planid是否 已经签到过，如果已经签到过无需 弹出下一个签到界面。
							startMode = result.getStartMode();
							accept(ACCEPT_QUERY);
							
							
							
						} else {
							showMessage(R.string.alarm, R.string.maintenance_task_no_start);
						}
						
					} else {
						CommonToast.showToast(mActivity, R.string.no_data);
					}
				}

				@Override
				public void onError(String errMsg) {
					CommonToast.showToast(mActivity, R.string.network_unavailable);
				}
				
			}).execute();
		}
	}
	
	
	AlertDialog mDialog;
	/**
	 * 显示对话框内容
	 * @param
	 * @author: ssu
	 * @date: 2015-6-9 下午3:43:49
	 */
	private void showMessage(int title, int message) {
		mDialog = new AlertDialog.Builder(mActivity).setNeutralButton("Ok", new android.content.DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDialog.dismiss();
			}
		}).create();
		mDialog.setTitle(title);
		mDialog.setMessage(getText(message));
		mDialog.setCancelable(false);
		mDialog.show();
	}
	
	private void dealQuery(String result){
		try {
			Log.e(TAG	, "result："+result);
			
			JSONObject jo=new JSONObject(result).getJSONObject("object");
			Gson gson =new Gson();
			mListBean=gson.fromJson(jo.getJSONArray("maintainContent").toString(),new TypeToken<ArrayList<MaintenanceBean>>() {}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(Tool.isNullList(mListBean)){
			proDismiss();
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
			dealCommit();  
		}else{//显示签到activity；
			Intent intent =new Intent(mActivity, SignActivity.class);
			intent.putExtra("planId",mItemBean.getPlanId());
			intent.putExtra("startMode",startMode);
			intent.putExtra("locationAddress",mItemBean.getLocationAddress());
			intent.putExtra("maintainCycle",mItemBean.getMaintainCycle());
			startActivity(intent);
		}
	}
	
	private void dealCommit(){
		Map<String, Serializable> map = new HashMap<String, Serializable>();
		map.put("planId", mItemBean.getPlanId());
		if(startMode){//维保任务已开始
			PageTurnUtil.gotoActivity(mActivity, StartMaintenanceTaskDetailsActivity.class, false, map);
		}else{
			map.put("locationAddress", mItemBean.getLocationAddress());
			map.put("maintainCycle", mItemBean.getMaintainCycle());
			PageTurnUtil.gotoActivity(mActivity, StartMaintenanceActivity.class, false, map);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		List<MaintenanceTaskListDetail> list = new ArrayList<MaintenanceTaskListDetail>();
		switch (checkedId) {
		case R.id.rb_all:
			list.addAll(mData);
			break;
		case R.id.rb_notmaintenance:
			Iterator<MaintenanceTaskListDetail> iterator = mData.iterator();
			while(iterator.hasNext()){
				MaintenanceTaskListDetail bean = iterator.next();
				if(bean.getStatusCode() == MaintenanceStatus.NOMAINTENANCE){
					list.add(bean);
				}
			}
			break;
		case R.id.rb_delay:
			Iterator<MaintenanceTaskListDetail> iterator2 = mData.iterator();
			while(iterator2.hasNext()){
				MaintenanceTaskListDetail bean = iterator2.next();
				if(bean.getStatusCode() == MaintenanceStatus.MAINTENANCEEXTENSION)
					list.add(bean);
			}
			break;
		}
		
		mListView.setAdapter(new MaintenanceTaskAdapter(list) );
		
	}


	
}
