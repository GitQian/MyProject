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
import com.chinacnit.elevatorguard.mobile.bean.StartMaintenanceTaskItem;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;

/**
 * 开始维保任务详情维保项Adapter
 * @author ssu 
 * @date 2015-6-18 上午11:07:26
 */
public class StartMaintenanceTaskDetailItemListAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<StartMaintenanceTaskItem>  list;
	private Context mContext;

	public StartMaintenanceTaskDetailItemListAdapter(Context context, List<StartMaintenanceTaskItem> list) {
		this.mContext = context;
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void onDateChange(List<StartMaintenanceTaskItem> list) {
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
		final StartMaintenanceTaskItem item = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.weibao_details_list_item, null);
			holder.mWeibaoItemName = (TextView) convertView.findViewById(R.id.weibao_detail_list_item_name);
			holder.mWeibaoItemStatus = (TextView) convertView.findViewById(R.id.weibao_detail_list_item_status);
			holder.mWeibaoItemImage = (ImageView) convertView.findViewById(R.id.weibao_detail_list_item_imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mWeibaoItemName.setText(item.getMaintainItem());
		if (item.getStatusCode() == WeiBaoItemStatus.NORMAL) {
			holder.mWeibaoItemImage.setVisibility(View.GONE);
			holder.mWeibaoItemStatus.setText(R.string.weibaoitem_normal);
			holder.mWeibaoItemStatus.setTextColor(mContext.getResources().getColor(R.color.black));
//			holder.mLlAbnormal.setOnClickListener(null);
		} else if (item.getStatusCode() == WeiBaoItemStatus.ABNORMAL) {
			holder.mWeibaoItemStatus.setTextColor(0xffcc0000);
			holder.mWeibaoItemStatus.setText(R.string.weibaoitem_abnormal);
			holder.mWeibaoItemImage.setVisibility(View.VISIBLE);
//			holder.mLlAbnormal.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					Intent intent = new Intent(mContext, WeiboItemDetailActivity_4manager.class);
//					intent.putExtra("isShowSubBtn", false);
//					intent.putExtra("comments", item.getComments());
//					mContext.startActivity(intent);
//				}
//			});
		} else if (item.getStatusCode() == WeiBaoItemStatus.NOSTATUS) {
			holder.mWeibaoItemStatus.setText("尚未维保");
		}
		return convertView;
	}

	static class ViewHolder {
		private TextView mWeibaoItemName;
		private TextView mWeibaoItemStatus;
		private ImageView mWeibaoItemImage;
	}
}
