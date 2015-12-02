package com.ztkj.base.business;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseFragment extends Fragment implements OnClickListener{
	public String TAG = getClass().getSimpleName();
	/**
	 * 内存被清理时是否退出，默认不退出
	 */
	private boolean mMemoryCleanedExit;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		BaseFragmentActivity fragmentActivity=(BaseFragmentActivity) getActivity();
		mMemoryCleanedExit=fragmentActivity.isMemoryCleanedExit();
	}
	public boolean isMemoryCleanedExit() {
		return mMemoryCleanedExit;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	@Override
	public void onClick(View v)
	{
		// TODO Auto-generated method stub
		
	}
}
