package com.chinabrowser.utils;

/**
 * 这个是临时解决方案，统一不再域名转ip 因为需要改的地方较多 时间短此处统一替换了
 * 
 * @author malin
 *
 */
public class InetAddress2 {

	private String str = "";

	private InetAddress2(String str) {
		this.str = str;
	}

	public static InetAddress2 getByName(String str) {
		return new InetAddress2(str);
	}

	public String getHostAddress() {
		return str;
	}
}
