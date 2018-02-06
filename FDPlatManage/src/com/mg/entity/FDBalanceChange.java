package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_balance_change")
public class FDBalanceChange {
	private String id;
	private Integer version;
	private String userId;//(对应着用户表的ID)
	private String changeType;//余额变动类型(充值 订单支付 订单完成打款 订单取消退款 提现)
	private String attachId;//(关联充值、提现、交易表主键)
	private Double changeSum;//DEAL_TYPE;  
	private Date changeDate;//DEAL_TYPE; 
	private Double balance;
	private String remark;//REMARK;备注
	
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
	
	@Column(name="CHANGE_TYPE")
	public String getChangeType() {
		return changeType;
	}
	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	
	@Column(name="ATTACH_ID")
	public String getAttachId() {
		return attachId;
	}
	public void setAttachId(String attachId) {
		this.attachId = attachId;
	}
	
	@Column(name="CHANGE_SUM")
	public Double getChangeSum() {
		return changeSum;
	}
	public void setChangeSum(Double changeSum) {
		this.changeSum = changeSum;
	}
	
	@Column(name="CHANGE_DATE")
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	@Column(name="BALANCE")
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
