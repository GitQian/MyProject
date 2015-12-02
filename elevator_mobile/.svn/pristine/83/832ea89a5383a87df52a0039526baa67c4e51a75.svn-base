package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.CompanyListDetail;

/**
 * 公司列表适配器
 * @author ssu 
 * @date 2015-6-4 下午7:51:30
 */
public class CompanyAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<CompanyListDetail> list;

	public CompanyAdapter(Context context, List<CompanyListDetail> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void onDateChange(List<CompanyListDetail> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
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
		CompanyListDetail pmc_info = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.company_item, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.company_icon);
			holder.name = (TextView) convertView.findViewById(R.id.company_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.name.setText(pmc_info.getCompanyName());
		holder.icon.setImageResource(R.drawable.logo);
		return convertView;
	}

	static class ViewHolder {
		TextView name;
		ImageView icon;
	}
}
