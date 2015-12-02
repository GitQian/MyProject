package com.ztkj.tool;


public class TempAll {
	private static TempAll temp;
	private TempAll() {

	}
	public static TempAll getTempAll() {
		if (temp == null) {
			temp = new TempAll();
		}
		return temp;
	}
	public static TempAll getTemp()
	{
		return temp;
	}
	public static void setTemp(TempAll temp)
	{
		TempAll.temp = temp;
	}
}
