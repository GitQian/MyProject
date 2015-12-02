package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail;
import com.chinacnit.elevatorguard.mobile.bean.MaintenanceTaskListDetail.MaintenanceStatus;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail.Cycle;

/**
 * @deprecated 已废弃使用， 新的样式 用  MaintenanceTaskAdapter.java
 * 维保任务列表页Adapter
 * 
 * @author ssu
 * @date 2015-6-2 下午8:41:36
 */
public class MaintenanceTaskListAdaper extends BaseAdapter {
	private LayoutInflater inflater;
	private List<MaintenanceTaskListDetail> list;
	private Context mContext;

	public MaintenanceTaskListAdaper(Context context, List<MaintenanceTaskListDetail> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void onDateChange(List<MaintenanceTaskListDetail> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (null != list && list.size() > 0) {
			return list.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MaintenanceTaskListDetail bean = list.get(position);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.activity_maintenance_task_list_item, null);
			holder.location_address_textview = (TextView) convertView.findViewById(R.id.maintenance_task_list_item_location_address);
			holder.status_textview = (TextView) convertView.findViewById(R.id.maintenance_task_list_item_status);
			holder.deadline = (TextView) convertView.findViewById(R.id.maintenance_task_list_item_deadline);
			holder.maintenance_task_list_item_bg = (LinearLayout) convertView.findViewById(R.id.maintenance_task_list_item_bg);
			holder.remark = (ImageView) convertView.findViewById(R.id.time_mark_imageview);
			holder.status_code_textview = (TextView) convertView.findViewById(R.id.maintenance_task_list_item_statusCode);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		holder.location_address_textview.setText(task.getLocationAddress());
		holder.location_address_textview.setText(bean.getLiftName()); //joymark
		holder.deadline.setText(bean.getPlanBeginDate());
		holder.status_code_textview.setText(MaintenanceStatus.getValueByKey(bean.getStatusCode()) != -1 ? mContext.getResources().getString(MaintenanceStatus.getValueByKey(bean.getStatusCode())) : "");
//		if (task.getStatusCode() == MaintenanceStatus.NOMAINTENANCE) {
//			holder.maintenance_task_list_item_bg.setBackgroundColor(android.R.color.holo_red_dark);
//		} else if (task.getStatusCode() == MaintenanceStatus.MAINTENANCEED) {
//			holder.maintenance_task_list_item_bg.setBackgroundColor(android.R.color.holo_green_dark);
//		} else if (task.getStatusCode() == MaintenanceStatus.MAINTENANCEEXTENSION) {
//			holder.maintenance_task_list_item_bg.setBackgroundColor(android.R.color.holo_orange_dark);
//		}
		holder.status_textview.setText(Cycle.getValueByKey(bean.getMaintainCycle()) != 0 ? mContext.getResources().getString(Cycle.getValueByKey(bean.getMaintainCycle())) : "");
		return convertView;
	}

	static class ViewHolder {
		TextView location_address_textview;
		TextView status_textview;
		TextView status_code_textview;
		TextView deadline;
		LinearLayout maintenance_task_list_item_bg;
		ImageView remark;
	}
}
