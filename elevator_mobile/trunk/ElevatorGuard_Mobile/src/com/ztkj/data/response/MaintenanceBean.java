package com.ztkj.data.response;

import android.os.Parcel;
import android.os.Parcelable;

public class MaintenanceBean implements Parcelable{
//	 "uid":123,// Long   0 表示没有被维保过
//     "tagInfo":"", // String 标签号
//     "maintainItem":"",// String 维保项
//     "statusCode":0, // Integer维保状态 -1 未扫描;0正常;1异常
//    "comments":"" //String  备注
	private String uid;
	private String statusCode;  //-1 未扫描;0正常;1异常
	private String maintainItem;
	private String lastUpdateDtm;
	private String tagInfo;
	private String strVal1;
	private String comments;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMaintainItem() {
		return maintainItem;
	}
	public void setMaintainItem(String maintainItem) {
		this.maintainItem = maintainItem;
	}
	public String getLastUpdateDtm() {
		return lastUpdateDtm;
	}
	public void setLastUpdateDtm(String lastUpdateDtm) {
		this.lastUpdateDtm = lastUpdateDtm;
	}
	public String getTagInfo() {
		return tagInfo;
	}
	public void setTagInfo(String tagInfo) {
		this.tagInfo = tagInfo;
	}
	public String getStrVal1() {
		return strVal1;
	}
	public void setStrVal1(String strVal1) {
		this.strVal1 = strVal1;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(uid);
		dest.writeString(statusCode);
		dest.writeString(maintainItem);
		dest.writeString(lastUpdateDtm);
		dest.writeString(tagInfo);
		dest.writeString(strVal1);
		dest.writeString(comments);
	}
	public final static Parcelable.Creator<MaintenanceBean> CREATOR =new Creator<MaintenanceBean>() {
		
		@Override
		public MaintenanceBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new MaintenanceBean[size];
		}
		
		@Override
		public MaintenanceBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new MaintenanceBean(source);
		}
	};
	
	
	public MaintenanceBean(){
		
	}
	private MaintenanceBean(Parcel parcel){
		uid=parcel.readString();
		statusCode=parcel.readString();
		maintainItem=parcel.readString();
		lastUpdateDtm=parcel.readString();
		tagInfo=parcel.readString();
		strVal1=parcel.readString();
		comments=parcel.readString();
	}
	
}
