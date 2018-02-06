package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_manager_info")
public class FDManagerInfo {
	private String id;
	private Integer version;
	private String userId;//管理员ID
	private String department;//所属部门
	private String position;//职位
	private String number;//编号
	private String authorityGroupId;//所属权限组ID
	private Integer state;// 类型：正常normal/封号locked
	private Integer isSuper;//是否为超级管理员
	private String belongMarketingCenter; //所属营销中心ID
	private Date createDate;
	
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
	
	@Column(name="STATE")
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
	@Column(name="IS_SUPER")
	public Integer getIsSuper() {
		return isSuper;
	}
	public void setIsSuper(Integer isSuper) {
		this.isSuper = isSuper;
	}
	
	@Column(name="CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="BELONG_MARKETING_CENTER")
	public String getBelongMarketingCenter() {
		return belongMarketingCenter;
	}
	public void setBelongMarketingCenter(String belongMarketingCenter) {
		this.belongMarketingCenter = belongMarketingCenter;
	}
}
