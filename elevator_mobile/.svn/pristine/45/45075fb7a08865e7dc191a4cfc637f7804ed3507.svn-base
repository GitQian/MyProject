package com.ztkj.data.request;


import com.google.gson.Gson;

/**
 * 请求实体父类，提交的json参数
 *
 */
public class RequestBaseBody {
	private Object mInsertObj;
	//此处本来可以遍历插入对象的key，个人觉得比较麻烦就直接用字符串拼接的形式搞起
	public String toJsonString(){
		Gson gson=new Gson();
		if(mInsertObj==null){
			return gson.toJson(this);
		}
		Object obj=mInsertObj;
		mInsertObj=null;
		String body=gson.toJson(this);
		String bodyInsert=gson.toJson(obj);
		StringBuffer sb=new StringBuffer();
		sb.append(body.substring(0, body.length()-1)).append(",").append(bodyInsert.substring(1));
		return sb.toString();
	}
	public RequestBaseBody insertJson(Object obj){
		this.mInsertObj=obj;
		return this;
	}
}
