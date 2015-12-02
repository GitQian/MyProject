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
public class NetCommonActivity extends BaseActivity implements NetListener{
	private ProDialog mProDialog;
	private ArrayList<Request> mRequestList=new ArrayList<Request>();
	private boolean mShowToast=true;
	private boolean mDismissPro=true;
	
	public void setmShowToast(boolean mShowToast) {
		this.mShowToast = mShowToast;
	}

	public void setmDismissPro(boolean mDismissPro) {
		this.mDismissPro = mDismissPro;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mProDialog=new ProDialog(this);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		for(Request request:mRequestList){
			request.setCancel(true);
		}
		super.onDestroy();
	}
	@Override
	public void netResultSuccess(String result, Request request) {
		// TODO Auto-generated method stub
		request.setCancel(true);
		mRequestList.remove(request);
		if(mDismissPro){
			proDismiss();
		}
	}
	@Override
	public void netResultFailed(String result, Request request) {
		// TODO Auto-generated method stub
		request.setCancel(true);
		mRequestList.remove(request);
		if(mShowToast){
			Tool.toastShow(this, result);
		}
		proDismiss();
	}
	public void proDismiss(){
		if(mProDialog!=null&&mProDialog.isShowing()){
			mProDialog.dismiss();
		}
	}
	public void proShow(){
		if(mProDialog==null){
			mProDialog=new ProDialog(this);
		}
		mProDialog.show();
	}
	
	/**
	 * 访问网络,不加载进度条
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
	
	public void doFinish(String str){
		if(str!=null){
			Tool.toastShow(this, str);
		}
		finish();
	}
	public void setShowToast(boolean showToast){
		this.mShowToast=showToast;
	}

	public ProDialog getProDialog() {
		return mProDialog;
	}
}
