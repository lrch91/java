package com.mg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="vfd_manager")
public class VFDManager {
	private String id;
	private String userId;//管理员ID
	private String department;//所属部门
	private String position;//职位
	private String number;//编号
	private String authorityGroupId;//所属权限组ID
	private Integer managerState;//类型：正常normal/封号locked
	private String isSuper;//是否为超级管理员 (0,1)
	private String belongMarketingCenter; //所属营销中心ID
	private String createDate;
	
	private String loginName;
	private String phone;
	private String trueName;
	private String catcoin;
	private String integral;
	private String userStatus;
	private String registerDate;
	private String groupName;
	private FDAuthorityRelation authorityRelation;
	
	@Id
	@Column(name="ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name="DEPARTMENT")
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	
	@Column(name="POSITION")
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	@Column(name="NUMBER")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Column(name="AUTHORITY_GROUP_ID")
	public String getAuthorityGroupId() {
		return authorityGroupId;
	}
	public void setAuthorityGroupId(String authorityGroupId) {
		this.authorityGroupId = authorityGroupId;
	}
	
	@Column(name="IS_SUPER")
	public String getIsSuper() {
		return isSuper;
	}
	public void setIsSuper(String isSuper) {
		this.isSuper = isSuper;
	}
	
	@Column(name="LOGIN_NAME")
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	
	@Column(name="TRUE_NAME")
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	
	@Column(name="GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name="MANAGER_STATE")
	public Integer getManagerState() {
		return managerState;
	}
	public void setManagerState(Integer managerState) {
		this.managerState = managerState;
	}
	
	@Column(name="PHONE")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name="CREATE_DATE")
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="BELONG_MARKETING_CENTER")
	public String getBelongMarketingCenter() {
		return belongMarketingCenter;
	}
	public void setBelongMarketingCenter(String belongMarketingCenter) {
		this.belongMarketingCenter = belongMarketingCenter;
	}
	
	@Column(name="CATCOIN")
	public String getCatcoin() {
		return catcoin;
	}
	public void setCatcoin(String catcoin) {
		this.catcoin = catcoin;
	}
	
	@Column(name="INTEGRAL")
	public String getIntegral() {
		return integral;
	}
	public void setIntegral(String integral) {
		this.integral = integral;
	}
	
	@Column(name="USER_STATUS")
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	
	@Column(name="REGISTER_DATE")
	public String getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}
	
	@Transient
	public FDAuthorityRelation getAuthorityRelation() {
		return authorityRelation;
	}
	public void setAuthorityRelation(FDAuthorityRelation authorityRelation) {
		this.authorityRelation = authorityRelation;
	}
	
}
