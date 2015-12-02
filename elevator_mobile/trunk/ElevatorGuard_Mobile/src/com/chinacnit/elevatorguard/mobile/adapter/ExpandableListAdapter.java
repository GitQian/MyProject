package com.chinacnit.elevatorguard.mobile.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.bean.LiftListDetail;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.RoleType;
import com.chinacnit.elevatorguard.mobile.bean.UserInfo.UserType;
import com.chinacnit.elevatorguard.mobile.config.ConfigSettings;
import com.chinacnit.elevatorguard.mobile.ui.activity.LiftDetailsActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.SettingsTagActivity;
import com.chinacnit.elevatorguard.mobile.ui.activity.WeiBaoRecordActivity;
import com.chinacnit.elevatorguard.mobile.util.LogUtils;
import com.chinacnit.elevatorguard.mobile.util.LogUtils.LogTag;

/**
 * 列表Adapter
 * 
 * @author ssu
 * @date 2015-5-26 下午3:18:13
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

	private static final LogTag LOG_TAG = LogUtils.getLogTag(ExpandableListAdapter.class.getSimpleName(), true);

	private Context _context;
	private List<LiftListDetail> _listDataHeader; // 存放Header的数据
	private Map<LiftListDetail, List<Map<String, Object>>> _listDataChild;// 存放Header的数据和Child的数据

	// 构造方法，需要传递三个参数，1.上下文对象；2，Header数据3.Child数据
	public ExpandableListAdapter(Context context,
			List<LiftListDetail> listDataHeader,
			Map<LiftListDetail, List<Map<String, Object>>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	public void onDateChange(List<LiftListDetail> listDataHeader,
			Map<LiftListDetail, List<Map<String, Object>>> listChildData) {
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		this.notifyDataSetChanged();
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		if (null != this._listDataChild) {
			return this._listDataChild.get(
					this._listDataHeader.get(groupPosition))
					.get(childPosititon);
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 构建child的视图
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ChildViewHolder();
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.child_item, null);
			viewHolder.btn_liftDetail = (Button) convertView.findViewById(R.id.lift_details_button);
			viewHolder.btn_weibaoDetail = (Button) convertView.findViewById(R.id.weixiu_details_button);
			viewHolder.btn_setTag = (Button) convertView.findViewById(R.id.settings_tag_button);
			viewHolder.btn_setTagParent = (RelativeLayout) convertView.findViewById(R.id.settings_tag_button_rl);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ChildViewHolder) convertView.getTag();
		}
		UserInfo userInfo = ConfigSettings.getUserInfo();
		if (userInfo.getCompanyType() == RoleType.MAINTENANCECOMPANY) {
			if (userInfo.getUserType() == UserType.ADMINISTRATOR || userInfo.getUserType() == UserType.ADMINANDWORKERS) {
				viewHolder.btn_setTagParent.setVisibility(View.VISIBLE);
			}
		}
		viewHolder.btn_weibaoDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtils.d(LOG_TAG, "btn_weibaoDetail", _listDataHeader.get(groupPosition).getLiftName());
				Intent intent = new Intent(_context, WeiBaoRecordActivity.class);
				intent.putExtra("id", _listDataHeader.get(groupPosition).getLiftId());
				_context.startActivity(intent);
			}
		});
		viewHolder.btn_liftDetail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtils.d(LOG_TAG, "btn_liftDetail", _listDataHeader.get(groupPosition).getLiftName());
				Intent intent = new Intent(_context, LiftDetailsActivity.class);
				intent.putExtra("liftId", _listDataHeader.get(groupPosition).getLiftId());
				_context.startActivity(intent);
			}
		});
		viewHolder.btn_setTag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogUtils.d(LOG_TAG, "btn_setTag", _listDataHeader.get(groupPosition).getLiftName());
				Intent intent = new Intent(_context, SettingsTagActivity.class);
				intent.putExtra("liftId", _listDataHeader.get(groupPosition).getLiftId());
				_context.startActivity(intent);
			}
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (null != this._listDataChild && this._listDataChild.size() > 0 && null != this._listDataHeader && this._listDataHeader.size() > 0) {
			List<Map<String, Object>> temp = this._listDataChild.get(this._listDataHeader.get(groupPosition));
			if (null != temp && temp.size() > 0) {
				return temp.size();
			}
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (null != this._listDataHeader) {
			return this._listDataHeader.get(groupPosition);
		}
		return null;
	}

	@Override
	public int getGroupCount() {
		if (null != this._listDataHeader) {
			return this._listDataHeader.size();
		}
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 构建Header的View
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		LiftListDetail lld = _listDataHeader.get(groupPosition);
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
			holder = new ViewHolder();
			holder.lift_name = (TextView) convertView.findViewById(R.id.lift_ListHeader);
			holder.lift_area = (TextView) convertView.findViewById(R.id.lift_area_ListHeader);
			holder.lift_date = (TextView) convertView.findViewById(R.id.lift_date_ListHeader);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.lift_name.setText(lld.getLiftName().toString());
		holder.lift_area.setText(lld.getLocationAddress());
		holder.lift_date.setText(lld.getMaxMaintainEndDtm().toString());
		return convertView;
	}

	static class ViewHolder {
		TextView lift_name;
		TextView lift_area;
		TextView lift_date;
	}

	class ChildViewHolder {
		Button btn_weibaoDetail;
		Button btn_liftDetail;
		Button btn_setTag;
		RelativeLayout btn_setTagParent;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
