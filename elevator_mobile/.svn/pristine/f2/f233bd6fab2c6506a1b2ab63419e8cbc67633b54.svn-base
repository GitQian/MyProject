package com.chinacnit.elevatorguard.mobile.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.adapter.MyEnumAdapterFactory;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.bean.ResponseMaintenanceAlert;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.ui.activity.MaintenanceTaskHomeActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.MaintenanceTaskHomeActivity.OnMyPageChangeListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.ztkj.base.business.NetCommonFragment;
import com.ztkj.componet.JGoodListView;
import com.ztkj.componet.JGoodListView.IXListViewListener;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestMaintenanceAlert;
import com.ztkj.data.request.RequestWeixiuJieGuoFankui;
import com.ztkj.dialog.DialogBottom_Alrt;
import com.ztkj.dialog.DialogBottom_Alrt.DialogPictureSelMCallback;
import com.ztkj.tool.Tool;
/**
 * 维保 警报……
 * @author liucheng  liucheng187@qq.com  item_maintenance_alert.xml
 */
public class MaintenanceAlertFragment extends NetCommonFragment  implements IXListViewListener,OnMyPageChangeListener{
	private int position = -1;
	private boolean isLoad; //是否ui初始化加载完了。   用次数判断。
	
	private View rootView;
	private MaintenanceTaskHomeActivity mActivity;
	private final int CODE_REFERESH = 0X0001;
	private final int CODE_LOADMORE = 0x0002;
	private final int CODE_FANGKUI = 0x0003;
	int mPage = 1;
	JGoodListView mListView;
	private MyAdapter mAdapter;
	List<ResponseMaintenanceAlert> mData = new ArrayList<ResponseMaintenanceAlert>();
	
	private LoginDetail mLoginDetail = ConfigSettings.getLoginDetail();
	DialogBottom_Alrt dialog;
	private ResponseMaintenanceAlert mClickBean; //点击选中的那个item
	private RequestWeixiuJieGuoFankui mRequestBody; //构建的body。
	
	public MaintenanceAlertFragment(int position) {
		this.position = position;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MaintenanceTaskHomeActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		if(rootView == null){
			rootView = View.inflate(mActivity, R.layout.fragment_maintenance_alerthome, null);
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
		mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mAdapter);
	}

	private void initView() {
		dialog = new DialogBottom_Alrt(mActivity);
		dialog.setDialogPictureSelMCallback(new DialogPictureSelMCallback() {
			
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
					dialog.edt_content.setText("");
					mRequestBody.setUserId(""+mLoginDetail.getUid());
					mRequestBody.setMalfunctionId(mClickBean.getMalfunctionId());
					mRequestBody.setRemark(content);
					mRequestBody.setUserName(mLoginDetail.getUserName());
					mRequestBody.setProcessingDtm(Tool.getTime());
					mRequestBody.setTaskId(mClickBean.getTaskId());
					
					Request request  = new Request(mRequestBody, "/apps/biz/liftMaintainContentController/malfunctionResponses", CODE_FANGKUI, true);
					getData(request, true);
				}
			}
		});
		mActivity.addOnPageChangeListener(this);
		mListView = (JGoodListView) rootView.findViewById(R.id.listview);
		mListView.setPageSize(Integer.MAX_VALUE);
		mListView.setXListViewListener(this); 
		
	}
	
	private void accept(int code){
		if(!isLoad) {
			Log.e(TAG, "UI预加载。。。不执行网络请求");
			return;
		}
		
		if(mLoginDetail == null){
			Log.e(TAG, "lalig uile .....");
			return;
		}
		switch (code) {
		case CODE_REFERESH: //刷新 和 加载更多。
		case CODE_LOADMORE:
			RequestMaintenanceAlert body = new RequestMaintenanceAlert();
			body.setPage(mPage);
			body.setRows(mListView.getPageSize());
			body.setUserId(""+mLoginDetail.getUid());
			Request request = new Request(body, "/apps/biz/liftMaintainContentController/queryMalfunctionTask", code);
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
	
	@Override
	public void netResultSuccess(String result, Request request) {
		super.netResultSuccess(result, request);
		Log.i(TAG, "列表呢result: "+result);
		List<ResponseMaintenanceAlert> data = new ArrayList<ResponseMaintenanceAlert>();
		if(request.getCode() == CODE_REFERESH || request.getCode() == CODE_LOADMORE){
			try{
				JSONObject jobj = new JSONObject(result);
				JSONObject object = jobj.getJSONObject("object");
				
				String rows = object.getString("rows");
				if(rows != null){
					GsonBuilder gb = new GsonBuilder(); 
					gb.registerTypeAdapterFactory(new MyEnumAdapterFactory());
					Gson gson = gb.create();
					List<ResponseMaintenanceAlert> sbbb = gson.fromJson(rows,  new TypeToken<List<ResponseMaintenanceAlert>>() {}.getType());
					
					if(sbbb!=null){
						for (ResponseMaintenanceAlert b : sbbb) {
							System.out.println("解析出："+b);
						}
					}
					
					if(request.getCode() == CODE_REFERESH){
						mData.clear();
					}
					if(sbbb!=null){
						data.addAll(sbbb);
						mData.addAll(data);
					}
				}else{ //没数据。
					if(request.getCode() == CODE_REFERESH){
						mData.clear();
					}
					mData.addAll(data);
				}
				
			}catch(Exception e){
				e.printStackTrace();
				mPage = --mPage <=0?1:mPage;
			}
			
			
			mAdapter.notifyDataSetChanged();
			mListView.onCompleted(mPage);
			
		}else if (request.getCode() == CODE_FANGKUI) {
			Tool.toastShow(mActivity, "已回复");
			mListView.setFreshing();
		}
	}
	
	@Override
	public void onRefresh() {
		mPage=1;
		accept(CODE_REFERESH);
	}

	@Override
	public void onLoadMore() {
		mPage++;
		accept(CODE_LOADMORE);
	}
	
	private  class MyAdapter extends BaseAdapter implements OnItemClickListener{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData == null ? 0 : mData.size();
		}

		@Override
		public ResponseMaintenanceAlert getItem(int position) {
			// TODO Auto-generated method stub
			return mData.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(mActivity == null){
				mActivity = (MaintenanceTaskHomeActivity) parent.getContext();
			}
			ResponseMaintenanceAlert bean = getItem(position);
			Log.e(TAG, "====bean:"+bean);
			ViewHold holder;
			if(convertView == null){
				holder = new ViewHold();
				convertView = View.inflate(mActivity, R.layout.item_maintenance_alert_listview	, null);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.tv_elevatorname = (TextView) convertView.findViewById(R.id.tv_elevatorname);
				holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
				holder.tv_reson = (TextView) convertView.findViewById(R.id.tv_reson);
				
				convertView.setTag(holder);
			}
			holder = (ViewHold) convertView.getTag();
			holder.tv_time.setText(bean.getCreateTime() +"");
			holder.tv_elevatorname.setText(bean.getLiftName());
			holder.tv_location.setText(bean.getLocationAddress());
			holder.tv_reson.setText(bean.getMalfunctionType());
			
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,	long id) {
			
			mClickBean = (ResponseMaintenanceAlert) parent.getAdapter().getItem(position);
//			String tag = (String) dialog.edt_content.getTag(position);
//			dialog.edt_content.setText(tag);
			dialog.show();
			
		}
	}
	
	private static class ViewHold{
		TextView tv_time,tv_elevatorname,tv_location,tv_reson;
	}
	
	@Override
	public void onPageChanged(int position) {
		if(position == this.position && !isLoad){
			isLoad = true;
			accept(CODE_REFERESH);
		}
	}

}
