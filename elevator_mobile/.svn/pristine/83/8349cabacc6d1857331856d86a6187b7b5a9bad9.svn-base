package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
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

/**
 * 维保人员列表Activity
 * 
 * @author ssu
 * @date 2015-6-12 下午6:44:58
 */
public class MaintenancePersonListActivity extends NetInCommonActivity	implements OnClickListener {

	/** 维保人员列表 */
	private List<String> list;

	private ArrayList<EmployeeBean> listBean = new ArrayList<EmployeeBean>();
	private LayoutInflater inflater;
	private MyAdapter mAdapter;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_weibao_works);
		initView();
		list = getIntent().getStringArrayListExtra("personList");
		if(list == null|| list.size()==0){
			Tool.toastShow(mActivity, "该维保项暂无维保人员");
			findViewById(R.id.fl_coontent).setVisibility(View.GONE);
			return;
		}
		
		accept();
	}

	private void initView() {
		TextView back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText("维保人员");
		findViewById(R.id.back_ll).setOnClickListener(this);

		inflater = LayoutInflater.from(this);
		mListView = (ListView) findViewById(R.id.viewContent);
		mAdapter = new MyAdapter();
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back_ll:
			finish();
			break;

		}
	}

	@Override
	public void imgRefreshClick() {
		accept();
	}

	private void accept() {
		LoginDetail loginDetail = ConfigSettings.getLoginDetail();
		if (loginDetail == null || loginDetail.getUid() == 0) {
			doFinish("用户信息不完整");
			return;
		}

		RequestManagerEmployee requestMe = new RequestManagerEmployee();
		requestMe.setUserId(loginDetail.getUid() + "");
		getData(new Request(requestMe,
				"/apps/biz/liftMaintainContentController/userManage"));

	}

	@Override
	public void netResultSuccess(String result, Request request) {
		super.netResultSuccess(result, request);
		try {
			JSONObject jo = new JSONObject(result);
			Gson gson = new Gson();
			listBean = gson.fromJson(jo.getJSONArray("object").toString(),new TypeToken<ArrayList<EmployeeBean>>() {}.getType());
			
			//过滤只需要显示  当前 维保项下的员工，不用显示所有的员工。
			if(listBean!=null && listBean.size()>0){
				Iterator<EmployeeBean> iterator = listBean.iterator();
				while (iterator.hasNext()) {
					EmployeeBean bean = iterator.next();
					if(!list.contains(bean.getRealName())){
						iterator.remove();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (listBean != null) {
			mAdapter.notifyDataSetChanged();
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listBean == null ? 0 : listBean.size();
		}

		@Override
		public EmployeeBean getItem(int position) {
			// TODO Auto-generated method stub
			return listBean.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			EmployeeBean bean = getItem(position);
			H h = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_manage_employee,
						null);
				h = new H();
				h.tv1 = (TextView) convertView.findViewById(R.id.tv1);
				h.tv2 = (TextView) convertView.findViewById(R.id.tv2);
				h.tv3 = (TextView) convertView.findViewById(R.id.tv3);
				convertView.setTag(h);
				;
			} else {
				h = (H) convertView.getTag();
			}
			h.tv1.setText(Tool.StringNull(bean.getDepartmentName(), "暂无"));
			h.tv2.setText(Tool.StringNull(bean.getRealName(), "暂无"));
			h.tv3.setText(Tool.StringNull(bean.getUserMobile(), "暂无"));

			return convertView;
		}

	}

	class H {
		TextView tv1, tv2, tv3;
	}

}
