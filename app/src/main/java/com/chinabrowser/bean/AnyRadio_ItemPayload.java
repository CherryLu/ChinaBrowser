/****************************
文件名:AnyRadio_ItemPayload.java

创建时间:2013-04-12
所在包:com.chinamobile.cloudapp.bean
作者:邢贝贝
说明:音频包数组，用于音频前端缓冲
 ****************************/
package com.chinabrowser.bean;


public class AnyRadio_ItemPayload {
	public byte[] data = null;
	public int errorcode = 0;// 直播错误的数据类型，0：为没有任何错误
	public int codeformat = 0;// 直播的数据格式类型
	public waveFormatEx wf = null;
	public long timeStamp = 0;// 时间戳
	public boolean lastBlock = false;
	public int rangeSize = -1; // 文件总长度

	public int getLen() {
		if (data != null)
			return data.length;
		return 0;
	}
}
