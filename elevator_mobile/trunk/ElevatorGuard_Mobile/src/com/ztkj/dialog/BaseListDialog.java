package com.ztkj.dialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

import com.chinacnit.elevatorguard.mobile.R;
import com.ztkj.adapter.ZTKJBaseAdapter;

/**
 * 适配listView的dialog；要求布局文件必须具有id为listViewForDialog,内置ZTKJBaseAdapter
 *所有的数据设置都应该在adapter里面 
 * @author hzx
 * 
 */
public abstract class BaseListDialog extends BaseDialog {

	private ZTKJBaseAdapter adapter;
	private AbsListView listView;
	private ListDialogCallBack listDialogCallBack;
	protected LayoutInflater inflate;
	/**
	 * 上次点击的位置,默认是0
	 */
	private int oldClickPosition;

	public interface ListDialogCallBack extends Dialogcallback{
		public boolean itemClick(int position, Object item);
	}
  
	public BaseListDialog(Context context) {
		super(context);
		inflate=LayoutInflater.from(context);
		listView=(AbsListView)getDialog().findViewById(R.id.listView);
		if(listView==null){
			listView=(AbsListView)getDialog().findViewById(R.id.listViewForDialog);
		}
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// TODO Auto-generated method stub
				oldClickPosition=position;
				if(itemClick(position)){
					return ;
				}
				if (listDialogCallBack != null) {
					Object obj = null;
					if (adapter != null && adapter.getItems() != null
							&& position >= 0
							&& position < adapter.getCount()) {

						obj = adapter.getItem(position);
					}

					if(listDialogCallBack.itemClick(position, obj)){
						dismiss();
					}
				}
			}
		});

		adapter = initAdapter();
		
		if(initClass()){
			((GridView)listView).setAdapter(adapter);
		}else{
			((ListView)listView).setAdapter(adapter);
		}
	}

	/**
	 * 返回一个Adapter给父类,子类不用写一个ZTKJBaseAdapter 全局变量，调用getAdapter（）即可
	 * 
	 * @return
	 */
	protected abstract ZTKJBaseAdapter initAdapter();
	/**
	 * false表示为ListView ，true GridView
	 * @return
	 */
	protected abstract boolean initClass();
	public ZTKJBaseAdapter getAdapter() {
		return adapter;
	}

	public void setListDialogCallBack(ListDialogCallBack listDialogCallBack) {
		this.listDialogCallBack = listDialogCallBack;
	}
	/**
	 * 返回上次点击的位置
	 * @return
	 */
	public int getOldPosition(){
		return oldClickPosition;
	}
	/**
	 * @param position listView点击的item位置
	 * @return 如果没有实现父类的ListDialogCallBack接口那么尽量返回true
	 */
	public boolean itemClick(int position){
		return false;
	}
}
