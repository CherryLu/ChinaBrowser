package com.chinabrowser.bean;

import com.chinabrowser.utils.LoginMode;

import java.io.Serializable;


public class UserKeeper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public boolean isLogin = false;
	public LoginMode mode = LoginMode.NONE;
	public String nickName;
	public String sex;
	public String headPhoto;
	public UserData userData;
}
