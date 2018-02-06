package com.mg.vo;

public class UserAuthVo {
	
	//用户类型 普通用户common  商家merchant  管理员 manager  超级管理员 superManager
	private String id;//用户ID 要求：随机自动生成9位数字，开头不能为0
	private String loginName;//用户名 要求：4~25位汉字、数字或字母，字母不区分大小写，不能为纯数字
	private String phone;//手机
	private String trueName;//真实姓名
	private String common; // YES NO
	private String merchant;  // YES NO
	private String manager; // YES NO
	private String superManager;  // YES NO
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getCommon() {
		return common;
	}
	public void setCommon(String common) {
		this.common = common;
	}
	public String getMerchant() {
		return merchant;
	}
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getSuperManager() {
		return superManager;
	}
	public void setSuperManager(String superManager) {
		this.superManager = superManager;
	}
}
