package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_operate_log")
public class FDOperateLog {
	private String id;
	private Integer version;
	private String userId;//用户ID
	private Date operateDate;//操作时间
	private String operateType;//操作类型
	private String operateObject;//操作内容
	private String operateNo;//订单操作专用
	
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

	@Column(name="OPERATE_DATE")
	public Date getOperateDate() {
		return operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	@Column(name="OPERATE_TYPE")
	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	@Column(name="OPERATE_OBJECT")
	public String getOperateObject() {
		return operateObject;
	}

	public void setOperateObject(String operateObject) {
		this.operateObject = operateObject;
	}

	@Column(name="OPERATE_NO")
	public String getOperateNo() {
		return operateNo;
	}

	public void setOperateNo(String operateNo) {
		this.operateNo = operateNo;
	}
	
}
