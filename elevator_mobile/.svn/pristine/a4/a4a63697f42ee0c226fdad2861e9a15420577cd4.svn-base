package com.ztkj.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.RadioButton;

import com.chinacnit.elevatorguard.mobile.R;

public class PopuMantenanceFilter extends BasePopu{
	private final String[] strs={"全部任务","尚未维保","维保延期","故障报警"};
	private MyAdapter adapter;
	private int mPosition;
	private PopuItemClickListener popuItemClickListener;
	private GridView gridView;
	public interface PopuItemClickListener{
		public void popuItemClick(int position,String str);
	}
	public void setPopuItemClickListener(PopuItemClickListener popuItemClickListener){
		this.popuItemClickListener=popuItemClickListener;
	}
	public PopuMantenanceFilter(Context context) {
		super(context,-1,-1);
		initView();
	}
	private void initView(){
		gridView=(GridView)getpopuContentView().findViewById(R.id.gridView);
		adapter =new MyAdapter();
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				mPosition=arg2;
				dismiss();
				popuItemClickListener.popuItemClick(arg2,strs[arg2]);
			}
		});
		getpopuContentView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	public void showAsDropDown(View v,int position) {
		// TODO Auto-generated method stub
		this.mPosition=position;
		adapter.notifyDataSetChanged();
		super.showAsDropDown(v);
	}
	
	@Override
	public int initLayoutid() {
		// TODO Auto-generated method stub
		return R.layout.popu_maintenance_filter;
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 4;
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
			// TODO Auto-generated method stub
			convertView=getLayoutInflater().inflate(R.layout.item_popu_maintenance_filter, null);
			RadioButton radioButton=(RadioButton)convertView.findViewById(R.id.radioButton);
			radioButton.setText(strs[position]);
			if(mPosition==position){
				radioButton.setChecked(true);
			}else{
				radioButton.setChecked(false);
			}
			return convertView;
		}
		
	}
	

}
