package com.ztkj.adapter;

import android.content.Context;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * 根据网上的一些资料整合过来的Adapter父类，方便ListView或GridView使用
 * @author hzx
 *
 */
public abstract class ZTKJBaseAdapter extends BaseAdapter {
    public static final int INVALID_ID = -1;
    protected Context mContext;
    private int nextStableId = 0;
    protected ArrayList<Object> mItems = new ArrayList<Object>();
    private HashMap<Object, Integer> mIdMap = new HashMap<Object, Integer>();
    /**
     * 无数据源之后可调用set方法设置
     * @param items
     */
    public ZTKJBaseAdapter(Context context) {
    	mContext = context;
    }
    /**
     * 
     * @param context
     * @param items 数据源
     */
    public ZTKJBaseAdapter(Context context, List<?> items) {
        mContext = context;
        init(items);
    }
    private void init(List<?> items) {
    	if(items!=null){
    		addAllStableId(items);
    		this.mItems.addAll(items);
    	}
    }
    protected void addAllStableId(List<?> items) {
        for (Object item : items) {
            addStableId(item);
        }
    }
    /**
     * 此方法表明每个item的id唯一
     *
     * @return
     */
    @Override
    public final boolean hasStableIds() {
        return true;
    }

    protected void addStableId(Object item) {
        mIdMap.put(item, nextStableId++);
    }
    @Override
    public final long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        Object item = getItem(position);
        return mIdMap.get(item);
    }

    protected void clearStableIdMap() {
        mIdMap.clear();
    }

    /**
     *调用remove时必须调用的方法
     * @param item
     */
    protected void removeStableID(Object item) {
        mIdMap.remove(item);
    }
    
    
    public void set(List<?> items) {
        clear();
        init(items);
        notifyDataSetChanged();
    }
    /**
     * 清空元素
     */
    public void clear() {
        clearStableIdMap();
        mItems.clear();
        notifyDataSetChanged();
    }
    /**
     * 添加元素
     * @param item
     */
    public void add(Object item) {
        addStableId(item);
        mItems.add(item);
        notifyDataSetChanged();
    }
    /**
     * 指定位置添加元素
     * @param position
     * @param item
     */
    public void add(int position, Object item) {
        addStableId(item);
        mItems.add(position, item);
        notifyDataSetChanged();
    }
    /**
     * 在原有的基础上添加一个集合
     * @param items
     */
    public void add(List<?> items) {
        addAllStableId(items);
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }
    /**
     * 删除一个元素
     * @param item
     */
    public void remove(Object item) {
        mItems.remove(item);
        removeStableID(item);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }
    /**
     * 得到这个Adapter里面的所有元素
     * @return
     */
    public List<Object> getItems() {
        return mItems;
    }
    /**
     * 获取context
     * @return
     */
    protected Context getContext() {
        return mContext;
    }

}
