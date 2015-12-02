package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem;
import com.chinacnit.elevatorguard.mobile.bean.WeiBaoItem.WeiBaoItemStatus;

/**
 * 维保记录详情页面  对应的Adapter
 * 
 * @author ssu
 * @date 2015-6-12 下午7:14:37
 */
public class WeibaoRecordDetailAdapter extends BaseAdapter {
	private List<WeiBaoItem> mList;
	private Activity mActivity;

	public WeibaoRecordDetailAdapter(List<WeiBaoItem> list) {
		this.mList = list;
	}

	public void onDateChange(List<WeiBaoItem> list) {
		this.mList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(mActivity == null){
			mActivity  = (Activity) parent.getContext();
		}
		final WeiBaoItem weiBaoItem = mList.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(mActivity, R.layout.weibao_details_list_item, null);
			holder.mWeibaoItemName = (TextView) convertView.findViewById(R.id.weibao_detail_list_item_name);
			holder.mWeibaoItemStatus = (TextView) convertView.findViewById(R.id.weibao_detail_list_item_status);
			holder.mWeibaoItemImage = (ImageView) convertView.findViewById(R.id.weibao_detail_list_item_imageview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mWeibaoItemName.setText(weiBaoItem.getMaintainItem());
		if (weiBaoItem.getStatusCode() == WeiBaoItemStatus.NORMAL) {
			holder.mWeibaoItemStatus.setText(R.string.weibaoitem_normal);
		} else if (weiBaoItem.getStatusCode() == WeiBaoItemStatus.ABNORMAL) {
			holder.mWeibaoItemStatus.setTextColor(0xffcc0000);
			holder.mWeibaoItemStatus.setText(R.string.weibaoitem_abnormal);
		} else if (weiBaoItem.getStatusCode() == WeiBaoItemStatus.NOSTATUS) {
			holder.mWeibaoItemStatus.setText("尚未维保");
		}else{
			holder.mWeibaoItemStatus.setText("服务器数据返回异常");
		}
		
		if(weiBaoItem.getStatusCode() == WeiBaoItemStatus.NORMAL || weiBaoItem.getStatusCode() == WeiBaoItemStatus.ABNORMAL){//除了“正常”和“异常”其他状态不可查看详情。
			holder.mWeibaoItemImage.setVisibility(View.VISIBLE);
		}else{
			holder.mWeibaoItemImage.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}

	static class ViewHolder {
		private TextView mWeibaoItemName;
		private TextView mWeibaoItemStatus;
		private ImageView mWeibaoItemImage;
	}
}
