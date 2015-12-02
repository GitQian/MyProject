package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.ResponseMaintenanceAlert;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MaintenanceAlertAdapter extends BaseAdapter{
	private List<ResponseMaintenanceAlert> mAlertList;
	public MaintenanceAlertAdapter( List<ResponseMaintenanceAlert> alertList){
		this.mAlertList=alertList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mAlertList==null?0:mAlertList.size();
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
		ResponseMaintenanceAlert bean = mAlertList.get(position);
		ViewHold holder;
		if(convertView == null){
			holder = new ViewHold();
			convertView = View.inflate(parent.getContext(), R.layout.item_maintenance_alert_listview, null);
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
	private static class ViewHold{
		TextView tv_time,tv_elevatorname,tv_location,tv_reson;
	}

}
