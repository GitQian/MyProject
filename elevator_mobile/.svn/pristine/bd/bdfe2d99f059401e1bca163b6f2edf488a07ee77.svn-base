package com.ztkj.base.business;

import java.util.ArrayList;

import com.ztkj.data.request.Request;
import com.ztkj.dialog.ProDialog;
import com.ztkj.net.NetListener;
import com.ztkj.net.NetTask;
import com.ztkj.tool.Tool;

import android.os.Bundle;
/**
 * 继承此类访问网络会回调netResultSuccess、netResultFailed方法<br>
 * <font color='red'>在回调方法中尽量写上super.netResultSuccess以及super.netResultFailed()方法</font><br>
 * 如果不想在返回失败中弹出toast则调用setShowToast（false）即可
 * 
 * @author hzx
 *
 */
public class NetCommonFragment extends BaseFragment implements NetListener{
	private ProDialog mProDialog;
	private ArrayList<Request> mRequestList=new ArrayList<Request>();
	private boolean mShowToast=true;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}
	/**
	 * 访问网络,默认不加载进度条
	 */
	public void getData(Request requestBean){
		NetTask netTask = new NetTask(this);
		netTask.execute(requestBean);
		mRequestList.add(requestBean);
	}
	/**
	 * 访问网络,是否加载进度条
	 */
	public void getData(Request request,boolean isShowPro){
		if(isShowPro){
			proShow();
		}
		getData(request);
	}
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		request.setCancel(true);
		mRequestList.remove(request);
		proDismiss();
	}
	@Override
	public void netResultFailed(String result, Request request) {
		// TODO Auto-generated method stub
		request.setCancel(true);
		mRequestList.remove(request);
		if(mShowToast){
			Tool.toastShow(getActivity(), result);
		}
		proDismiss();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		for(Request request:mRequestList){
			request.setCancel(true);
		}
	}
	public void proShow(){
		if(mProDialog==null){
			mProDialog=new ProDialog(getActivity());
		}
		mProDialog.show();
	}
	/**
	 * 进度条消失
	 */
	public void proDismiss(){
		if(mProDialog!=null&&mProDialog.isShowing()){
			mProDialog.dismiss();
		}
	}
}
