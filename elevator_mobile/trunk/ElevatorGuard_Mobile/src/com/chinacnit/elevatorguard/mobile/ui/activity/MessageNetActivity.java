package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ztkj.base.business.NetCommonActivity;
import com.ztkj.componet.JGoodListView;
import com.ztkj.componet.JGoodListView.IXListViewListener;
import com.ztkj.data.request.Request;
import com.ztkj.data.request.RequestMsgNet;
import com.ztkj.data.response.MaintenanceBean;
import com.ztkj.db.DbOperater;
import com.ztkj.db.bean.MessageBean;
import com.ztkj.tool.SharedPreferenceTool;
import com.ztkj.tool.Tool;

/**
 * 信息中心
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月8日 下午6:27:33
 */
public class MessageNetActivity extends NetCommonActivity implements IXListViewListener {
	private JGoodListView listView;
	private MyAdapter adapter;
	// 返回按钮
	private LinearLayout back_ll;
	// 返回按钮文本
	private TextView back_textview;
	private ArrayList<MessageBean> listBean ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_net);
		initView();
		DbOperater.getInstance().updateMessageReaderState();
	}

	private void initView() {
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.info_center);
		listView =(JGoodListView)findViewById(R.id.listView);
		listView.setPageSize(Integer.MAX_VALUE/2);
		listView.setPullLoadEnable(false);
		listView.setXListViewListener(this);
		adapter =new MyAdapter();
		listView.setAdapter(adapter);
		
		
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_ll:
			PageTurnUtil.goBack(this);
			break;
		
		default:
			break;
		}
	}
	class MyAdapter extends BaseAdapter{
		@Override
		public View getView(int position, View contentView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder = null;
			if (contentView == null) {
				contentView = LayoutInflater.from(MessageNetActivity.this).inflate(R.layout.item_msg, null);
				viewHolder = new ViewHolder();
				viewHolder.tvTime = (TextView) contentView.findViewById(R.id.tvTime);
				viewHolder.tvTitle = (TextView) contentView.findViewById(R.id.tvTitle);
				viewHolder.tvContent = (TextView) contentView.findViewById(R.id.tvContent);
				viewHolder.imgNew=(ImageView)contentView.findViewById(R.id.imgNew);	
				contentView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) contentView.getTag();
			}
			MessageBean bean =listBean.get(position);
			viewHolder.tvContent.setText(bean.getContent()+"");
			viewHolder.tvTime.setText(bean.getSavetime()+"");
			viewHolder.tvTitle.setText(bean.getTitle()+"");
			
			if ("1".equals(bean.getReadState())) {
				if (viewHolder.imgNew.getVisibility() != View.VISIBLE) {
					viewHolder.imgNew.startAnimation(AnimationUtils.loadAnimation(MessageNetActivity.this, R.anim.shake_y));viewHolder.imgNew.setVisibility(View.VISIBLE);
				}
			} else {
				viewHolder.imgNew.clearAnimation();
				viewHolder.imgNew.setVisibility(View.GONE);
			}
			
			return contentView;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listBean==null?0:listBean.size();
		}
	}
	class ViewHolder {
		TextView tvTitle;
		TextView tvContent;
		TextView tvTime;
		ImageView imgNew;
	}
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		accept();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}
	private void accept(){
		getData(new Request(new RequestMsgNet(SharedPreferenceTool.getUid()+""), "/apps/biz/liftMaintainContentController/getMsg"));
	}
	@Override
	public void netResultFailed(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultFailed(result, request);
		listView.onCompleted(1);
	}
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		super.netResultSuccess(result, request);
		try {
			
			JSONArray jarray=new JSONObject(result).getJSONArray("object");
			Gson gson =new Gson();
			listBean=gson.fromJson(jarray.toString(),new TypeToken<ArrayList<MessageBean>>() {}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		if(!Tool.isNullList(listBean)){
			adapter.notifyDataSetChanged();
		}
		listView.onCompleted(1);
		
	}
}
