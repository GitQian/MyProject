package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.TagListDetail;

/**
 * 设置标签列表适配器
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月1日 下午3:08:04
 */
public class SettingsTagAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<TagListDetail> list;

	
	public List<TagListDetail> getList() {
		return list;
	}

	public void setList(List<TagListDetail> list) {
		this.list = list;
	}

	public SettingsTagAdapter(Context context, List<TagListDetail> list) {
		this.inflater = LayoutInflater.from(context);
		this.list = list;
	}

	public void onDateChange(List<TagListDetail> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public TagListDetail getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TagListDetail tagListDetail = list.get(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.settings_tag_item, null);
			holder.tagName = (TextView) convertView.findViewById(R.id.settings_tag_tagname_textview);
//			holder.tagCardId = (TextView) convertView.findViewById(R.id.settings_tag_item_cardid_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tagName.setText(tagListDetail.getKeyCname());
//		holder.tagCardId.setText(TextUtils.isEmpty(tagListDetail.getTagInfo()) == true ? "" : tagListDetail.getTagInfo());
		return convertView;
	}

	static class ViewHolder {
		private TextView tagName;
		private TextView tagCardId;
	}
}
