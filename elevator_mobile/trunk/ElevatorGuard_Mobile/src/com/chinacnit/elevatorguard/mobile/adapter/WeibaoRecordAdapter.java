package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoRecordBean;

/**
 * 维保记录Adapter
 * 
 * @author ssu
 * @date 2015-5-26 下午3:18:02
 */
public class WeibaoRecordAdapter extends BaseAdapter {
	private List<WeiBaoRecordBean> list;
	private Activity mActivity;

	
	public List<WeiBaoRecordBean> getList() {
		return list;
	}
	public void setList(List<WeiBaoRecordBean> list) {
		this.list = list;
	}
	public WeibaoRecordAdapter( List<WeiBaoRecordBean> list) {
		this.list = list;
	}
	public void onDateChange(List<WeiBaoRecordBean> list) {
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
	public WeiBaoRecordBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(mActivity == null){
			mActivity = (Activity) parent.getContext();
		}
		WeiBaoRecordBean bean = list.get(position);
		Log.v(getClass().getSimpleName(), "WeiBaoRecordBean:"+bean);
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mActivity,R.layout.item_metainence_record_4listview, null);
			holder.tvPlanTime = (TextView) convertView.findViewById(R.id.tvPlanTime);
			holder.tvMantenanceTime = (TextView) convertView.findViewById(R.id.tvMaintenanceTime);
			holder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvPlanTime.setText(bean.getPlanTime());
		holder.tvMantenanceTime.setText(bean.getMaintenanceTime());
		holder.tvStatus.setText(bean.getStatus());
		if("异常".equals(bean.getStatus())){
			holder.tvStatus.setTextColor(mActivity.getResources().getColor(R.color.red_melon));
		}else if("正常".equals(bean.getStatus())){
			holder.tvStatus.setTextColor(0xff16D27F);
		}else{
			//其他状态， 默认黑色else
			holder.tvStatus.setTextColor(0xff000000);
		}
		
		return convertView;
	}

	static class ViewHolder {
		TextView tvPlanTime;
		TextView tvMantenanceTime;
		TextView tvStatus;
	}
}
