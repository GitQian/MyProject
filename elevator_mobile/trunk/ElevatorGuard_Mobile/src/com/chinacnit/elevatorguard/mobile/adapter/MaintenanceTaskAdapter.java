package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail.MaintenanceStatus;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;

public class MaintenanceTaskAdapter extends BaseAdapter {
	private List<MaintenanceTaskListDetail> mData;
	
	
	public MaintenanceTaskAdapter(List<MaintenanceTaskListDetail> mData) {
		this.mData = mData; 
	}

	@Override
	public int getCount() {
		return mData == null?0:mData.size();
	}

	@Override
	public MaintenanceTaskListDetail getItem(int position) {
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
		MaintenanceTaskListDetail bean = getItem(position);
		ViewHold holder;
		if(convertView == null){
			holder = new ViewHold();
			convertView = View.inflate(parent.getContext(), R.layout.item_4_listview_maintenancetask	, null);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_elevatorname = (TextView) convertView.findViewById(R.id.tv_elevatorname);
			holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
			holder.tv_cycle = (TextView) convertView.findViewById(R.id.tv_cycle);
			holder.tv_maintenancestate = (TextView) convertView.findViewById(R.id.tv_maintenancestate);
			
			convertView.setTag(holder);
		}
		holder = (ViewHold) convertView.getTag();
		holder.tv_time.setText(bean.getPlanBeginDate());
		holder.tv_elevatorname.setText(bean.getLiftName());
		holder.tv_location.setText(bean.getLocationAddress());
		holder.tv_cycle.setText(Cycle.getValueByKey(bean.getMaintainCycle()) != 0 ? parent.getContext().getResources().getString(Cycle.getValueByKey(bean.getMaintainCycle())) : "");
		holder.tv_maintenancestate.setText(bean.getStatusCode()==MaintenanceStatus.NOMAINTENANCE?"尚未维保":bean.getStatusCode()==MaintenanceStatus.MAINTENANCEEXTENSION?"维保延期":"维保完成");
		holder.tv_maintenancestate.setTextColor(bean.getStatusCode() == MaintenanceStatus.NOMAINTENANCE?0xff60B0FF:bean.getStatusCode() == MaintenanceStatus.MAINTENANCEEXTENSION?0xffFFAF37:Color.GREEN);
		
//		convertView.setVisibility(View.GONE);   //行不通，会显示一块空白在那里。
//		if(mActivity.filterType == MaintenanceTaskHomeActivity.FILTER_TYPE_DELAY){//只显示 类型是 维保延迟的。
//			if(bean.getStatusCode() == MaintenanceStatus.MAINTENANCEEXTENSION)
//				convertView.setVisibility(View.VISIBLE);
//		}else if (mActivity.filterType == MaintenanceTaskHomeActivity.FILTER_TYPE_NOTMAINTENANCE) {
//			if(bean.getStatusCode() == MaintenanceStatus.NOMAINTENANCE)
//				convertView.setVisibility(View.VISIBLE);
//		}else {
//			convertView.setVisibility(View.VISIBLE);
//		}
		
//		convertView.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				notifyDataSetChanged();
//			}
//		}, 300);
		return convertView;
	}
	
	private static class ViewHold{
		TextView tv_time,tv_elevatorname,tv_location,tv_cycle,tv_maintenancestate;
	}


}
