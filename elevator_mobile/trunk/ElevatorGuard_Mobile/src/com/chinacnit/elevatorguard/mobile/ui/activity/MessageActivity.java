package com.chinacnit.elevatorguard.mobile.ui.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chinacnit.elevatorguard.mobile.R;
import com.chinacnit.elevatorguard.mobile.util.PageTurnUtil;
import com.ztkj.db.DbOperater;
import com.ztkj.db.bean.MessageBean;

/**
 * 信息中心
 * 
 * @author: pyyang
 * @date 创建时间：2015年6月8日 下午6:27:33
 */
public class MessageActivity extends BaseActivity implements OnClickListener {
	// 返回按钮
	private LinearLayout back_ll;
	// 返回按钮文本
	private TextView back_textview;
	private ArrayList<MessageBean> listBean ;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		initView();
		DbOperater.getInstance().updateMessageReaderState();
	}

	private void initView() {
		back_ll = (LinearLayout) findViewById(R.id.back_ll);
		back_ll.setOnClickListener(this);
		back_textview = (TextView) findViewById(R.id.back_textview);
		back_textview.setText(R.string.info_center);
		ListView listView =(ListView)findViewById(R.id.listView);
		listBean=DbOperater.getInstance().queryMessage();
		listView.setAdapter(new BaseAdapter() {
			
			@Override
			public View getView(int position, View contentView, ViewGroup parent) {
				// TODO Auto-generated method stub
				ViewHolder viewHolder = null;
				if (contentView == null) {
					contentView = LayoutInflater.from(MessageActivity.this).inflate(R.layout.item_msg, null);
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
						viewHolder.imgNew.startAnimation(AnimationUtils.loadAnimation(MessageActivity.this, R.anim.shake_y));viewHolder.imgNew.setVisibility(View.VISIBLE);
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
		});
		
		
	}
	class ViewHolder {
		TextView tvTitle;
		TextView tvContent;
		TextView tvTime;
		ImageView imgNew;
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

}
