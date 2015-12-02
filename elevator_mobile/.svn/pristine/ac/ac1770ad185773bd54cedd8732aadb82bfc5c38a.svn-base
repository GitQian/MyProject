package com.ztkj.data.request;

import com.ztkj.tool.Base64;
import com.ztkj.tool.Tool;

public class RequestFile extends RequestBaseBody{
	private String file;
	public RequestFile(String file) {
		// TODO Auto-generated constructor stub
		this.file=file;
	}
	public String getFile() {
		return file;
	}
	public void setFile(String file) {
		this.file = file;
	}
	@Override
	public String toJsonString() {
		// TODO Auto-generated method stub
		file=Base64.encode(Tool.decodeBitmap(file));
		return super.toJsonString();
	}
}
