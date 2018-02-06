package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity//用户表
@Table(name="fd_user")
public class FDUser {
	//用户类型 普通用户 common  管理员  manager  超级管理员 superManager
	private String id;//用户ID 要求：随机自动生成9位数字，开头不能为0
	private Integer version;
	private String loginName;//用户名 要求：4~25位汉字、数字或字母，字母不区分大小写，不能为纯数字
	private String phone;//手机
	private String password;//密码 要求：6~20位数字和字母混合组合
	private Integer catcoin;//猫币
	private Integer integral;//账户积分
	private Integer historyIntegral;//历史积分(用于用户等级,直接根据历史积分计算出用户等级)
	private Double historyBalance;//历史订单总额(用于会员等级，手动设置会员等级后，历史消费金额清空后再继续累计)
	private Integer isMember; //是否为会员
	private Integer manualMemberLevel;//手动设置会员等级
	private String trueName;//真实姓名
	private String gender;//性别
	private String birthday; //出生年月
	private String idCard;//身份证号
	private String city;//城市
	private String area;//区或县
	private String street;//街道或镇
	private String detailAddress;//详细地址
	private String myCrops;//种植作物
	private String email;//邮箱
	private Date registerDate;//注册时间
	private String imagePath;//头像  要求：仅支持JPG、JPEG、PNG格式（4M以下）
	private String alipay;//支付宝账号
	private String payPwd;//支付密码
	private String status;//账户状态  类型：正常normal/封号locked
	private String verifyState;//实名认证状态  0或空:未认证  1:已认证
	private Date verifyDate;//实名认证时间
	private Date becomeMemberDate;//成为会员时间
	private Integer integralName;
	private Integer integralGender;
	private Integer integralDetailAddress;
	private Integer integralCrop;
	private String salt;
	
	@Id
	@Column(name="ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Version
	@Column(name="VERSION")
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name="LOGIN_NAME")
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="PHONE")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="INTEGRAL")
	public Integer getIntegral() {
		return integral;
	}
	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	
	@Column(name="IS_MEMBER")
	public Integer getIsMember() {
		return isMember;
	}
	public void setIsMember(Integer isMember) {
		this.isMember = isMember;
	}

	@Column(name="MANUAL_MEMBER_LEVEL")
	public Integer getManualMemberLevel() {
		return manualMemberLevel;
	}
	public void setManualMemberLevel(Integer manualMemberLevel) {
		this.manualMemberLevel = manualMemberLevel;
	}
	
	@Column(name="TRUE_NAME")
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	@Column(name="GENDER")
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	@Column(name="BIRTHDAY")
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	@Column(name="ID_CARD")
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	@Column(name="EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name="REGISTER_DATE")
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	@Column(name="IMAGE_PATH")
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Column(name="ALIPAY")
	public String getAlipay() {
		return alipay;
	}
	public void setAlipay(String alipay) {
		this.alipay = alipay;
	}
	
	@Column(name="PAY_PWD")
	public String getPayPwd() {
		return payPwd;
	}
	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="VERIFY_STATE")
	public String getVerifyState() {
		return verifyState;
	}
	public void setVerifyState(String verifyState) {
		this.verifyState = verifyState;
	}
	
	@Column(name="VERIFY_DATE")
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	
	@Column(name="SALT")
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Column(name="CATCOIN")
	public Integer getCatcoin() {
		return catcoin;
	}
	public void setCatcoin(Integer catcoin) {
		this.catcoin = catcoin;
	}
	
	@Column(name="HISTORY_INTEGRAL")
	public Integer getHistoryIntegral() {
		return historyIntegral;
	}
	public void setHistoryIntegral(Integer historyIntegral) {
		this.historyIntegral = historyIntegral;
	}
	
	@Column(name="HISTORY_BALANCE")
	public Double getHistoryBalance() {
		return historyBalance;
	}
	public void setHistoryBalance(Double historyBalance) {
		this.historyBalance = historyBalance;
	}
	
	@Column(name="CITY")
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	@Column(name="AREA")
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	@Column(name="STREET")
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	
	@Column(name="DETAIL_ADDRESS")
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	
	@Column(name="MY_CROPS")
	public String getMyCrops() {
		return myCrops;
	}
	public void setMyCrops(String myCrops) {
		this.myCrops = myCrops;
	}
	
	@Column(name="INTEGRAL_NAME")
	public Integer getIntegralName() {
		return integralName;
	}
	public void setIntegralName(Integer integralName) {
		this.integralName = integralName;
	}
	
	@Column(name="INTEGRAL_GENDER")
	public Integer getIntegralGender() {
		return integralGender;
	}
	public void setIntegralGender(Integer integralGender) {
		this.integralGender = integralGender;
	}
	
	@Column(name="INTEGRAL_DETAIL_ADDRESS")
	public Integer getIntegralDetailAddress() {
		return integralDetailAddress;
	}
	public void setIntegralDetailAddress(Integer integralDetailAddress) {
		this.integralDetailAddress = integralDetailAddress;
	}
	
	@Column(name="INTEGRAL_CROP")
	public Integer getIntegralCrop() {
		return integralCrop;
	}
	public void setIntegralCrop(Integer integralCrop) {
		this.integralCrop = integralCrop;
	}
	
	@Column(name="BECOME_MEMBER_DATE")
	public Date getBecomeMemberDate() {
		return becomeMemberDate;
	}
	public void setBecomeMemberDate(Date becomeMemberDate) {
		this.becomeMemberDate = becomeMemberDate;
	}
	
}
