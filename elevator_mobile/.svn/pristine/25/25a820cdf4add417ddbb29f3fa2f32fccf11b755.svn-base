package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.LoginDetail;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ztkj.base.business.NetInCommonActivity;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestManagerEmployee;
import com.ztkj.data.response.EmployeeBean;
import com.ztkj.tool.Tool;

public class ManagerEmployee extends NetInCommonActivity{
	private ArrayList<EmployeeBean> listBean;
	private LayoutInflater inflater;
	private MyAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage_employee);
		initView();
		accept();
		
	}
	private void initView(){
		TextView back_textview=(TextView)findViewById(R.id.back_textview);
		back_textview.setText("员工管理");
		findViewById(R.id.back_ll).setOnClickListener(this);
		
		inflater=LayoutInflater.from(this);
		ListView listView=(ListView)findViewById(R.id.viewContent);
		adapter =new MyAdapter();
		listView.setAdapter(adapter);
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_ll:
			finish();
			break;

		default:
			break;
		}
	}
	
	
	
	@Override
	public void imgRefreshClick() {
		// TODO Auto-generated method stub
		accept();
	}
	private void accept(){
		LoginDetail loginDetail = ConfigSettings.getLoginDetail();
		if(loginDetail==null||loginDetail.getUid()==0){
			doFinish("用户信息不完整");
			return ;
		}
		
		RequestManagerEmployee requestMe=new RequestManagerEmployee();
		requestMe.setUserId(loginDetail.getUid()+"");
		getData(new Request(requestMe, "/apps/biz/liftMaintainContentController/userManage"));	
		
	}
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultSuccess(result, request);
		try {
			JSONObject jo=new JSONObject(result);
			Gson gson =new Gson();
			listBean=gson.fromJson(jo.getJSONArray("object").toString(),new TypeToken<ArrayList<EmployeeBean>>() {}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(listBean!=null){
			adapter.notifyDataSetChanged();
		}
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
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			H h=null;
			if(convertView==null){
				convertView=inflater.inflate(R.layout.item_manage_employee, null);
				h=new H();
				h.tv1=(TextView)convertView.findViewById(R.id.tv1);
				h.tv2=(TextView)convertView.findViewById(R.id.tv2);
				h.tv3=(TextView)convertView.findViewById(R.id.tv3);
				convertView.setTag(h);;
			}else{
				h=(H)convertView.getTag();
			}
			EmployeeBean bean =listBean.get(position);
			h.tv1.setText(Tool.StringNull(bean.getDepartmentName(), "暂无"));
			h.tv2.setText(Tool.StringNull(bean.getRealName(), "暂无"));
			h.tv3.setText(Tool.StringNull(bean.getUserMobile(), "暂无"));
			
			return convertView;
		}
		
	}
	class H {
		TextView tv1,tv2,tv3;
	}
}
