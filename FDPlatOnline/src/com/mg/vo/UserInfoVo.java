package com.mg.vo;

import java.util.Date;

public class UserInfoVo {
	
	//用户类型 普通用户common  商家merchant  管理员 manager  超级管理员 superManager
	private String id;//用户ID 要求：随机自动生成9位数字，开头不能为0
	private String loginName;//用户名 要求：4~25位汉字、数字或字母，字母不区分大小写，不能为纯数字
	private String phone;//手机
	private Integer catcoin;//猫币
	private Integer integral;//账户积分
	private Integer historyIntegral;//历史积分
	private Double historyBalance;//历史订单总额
	private Integer isMember; //是否为会员
	private Integer memberLevel;//会员等级
	private String memberLevelName;
	private Integer userLevel;//用户等级
	private String userLevelName;
	private String nickName;//昵称
	private String trueName;//真实姓名
	private String city;//城市
	private String cityName;//城市
	private String area;//区或县
	private String areaName;//区或县
	private String street;//街道或镇
	private String streetName;//街道或镇
	private String detailAddress;//详细地址
	private String gender;
	private String myCrops;//种植作物
	private String status;//账户状态  类型：正常normal/封号locked
	private Date registerDate;//注册时间
	private String imagePath;//头像  要求：仅支持JPG、JPEG、PNG格式（4M以下）
	private String verifyState;//实名认证状态  0或空:未认证  1:已认证
	private Date verifyDate;//实名认证时间
	private Date becomeMemberDate;//成为会员时间
	private Integer integralName;
	private Integer integralGender;
	private Integer integralDetailAddress;
	private Integer integralCrop;
	private Integer isSignIn;
	private Integer signInIntegral; 
	
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public String getVerifyState() {
		return verifyState;
	}
	public void setVerifyState(String verifyState) {
		this.verifyState = verifyState;
	}
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
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
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getCatcoin() {
		return catcoin;
	}
	public void setCatcoin(Integer catcoin) {
		this.catcoin = catcoin;
	}
	public Integer getHistoryIntegral() {
		return historyIntegral;
	}
	public void setHistoryIntegral(Integer historyIntegral) {
		this.historyIntegral = historyIntegral;
	}
	public Double getHistoryBalance() {
		return historyBalance;
	}
	public void setHistoryBalance(Double historyBalance) {
		this.historyBalance = historyBalance;
	}
	public Integer getIntegralName() {
		return integralName;
	}
	public void setIntegralName(Integer integralName) {
		this.integralName = integralName;
	}
	
	public Integer getIntegralGender() {
		return integralGender;
	}
	public void setIntegralGender(Integer integralGender) {
		this.integralGender = integralGender;
	}
	
	public Integer getIntegralDetailAddress() {
		return integralDetailAddress;
	}
	public void setIntegralDetailAddress(Integer integralDetailAddress) {
		this.integralDetailAddress = integralDetailAddress;
	}
	
	public Integer getIntegralCrop() {
		return integralCrop;
	}
	public void setIntegralCrop(Integer integralCrop) {
		this.integralCrop = integralCrop;
	}
	
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public Integer getIsSignIn() {
		return isSignIn;
	}
	public void setIsSignIn(Integer isSignIn) {
		this.isSignIn = isSignIn;
	}
	public String getMyCrops() {
		return myCrops;
	}
	public void setMyCrops(String myCrops) {
		this.myCrops = myCrops;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public Integer getIsMember() {
		return isMember;
	}
	public void setIsMember(Integer isMember) {
		this.isMember = isMember;
	}
	public String getMemberLevelName() {
		return memberLevelName;
	}
	public void setMemberLevelName(String memberLevelName) {
		this.memberLevelName = memberLevelName;
	}
	public Integer getMemberLevel() {
		return memberLevel;
	}
	public void setMemberLevel(Integer memberLevel) {
		this.memberLevel = memberLevel;
	}
	public Integer getUserLevel() {
		return userLevel;
	}
	public void setUserLevel(Integer userLevel) {
		this.userLevel = userLevel;
	}
	public String getUserLevelName() {
		return userLevelName;
	}
	public void setUserLevelName(String userLevelName) {
		this.userLevelName = userLevelName;
	}
	public Date getBecomeMemberDate() {
		return becomeMemberDate;
	}
	public void setBecomeMemberDate(Date becomeMemberDate) {
		this.becomeMemberDate = becomeMemberDate;
	}
	public Integer getSignInIntegral() {
		return signInIntegral;
	}
	public void setSignInIntegral(Integer signInIntegral) {
		this.signInIntegral = signInIntegral;
	}
	
}
