package com.platform.util.wechat;

public class Configure {
	private static String key = "TIANYUANshangchengwxd157147d4c79";

	//小程序ID	
	private static String appID = "wxd157147d4c79c241";
	//商户号
	private static String mch_id = "1496750182";
	//
	private static String secret = "3c7d3a4d16bec6044da29e9349780b16";

	public static String getSecret() {
		return secret;
	}

	public static void setSecret(String secret) {
		Configure.secret = secret;
	}

	public static String getKey() {
		return key;
	}

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static String getAppID() {
		return appID;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static String getMch_id() {
		return mch_id;
	}

	public static void setMch_id(String mch_id) {
		Configure.mch_id = mch_id;
	}

}
