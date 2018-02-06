package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_user_recharge")
public class FDUserRecharge {
	private String id;
	private Integer version;
	private String rechargeUserId;
	private String payWay;
	private String payWayNumber; //流水号
	private Date rechargeDate;
	private Date verifyDate;
	private Integer rechargeSum;
	private String rechargeType;
	private String securedDealId;
	private Integer status;//待审核wait_verify ,  充值成功 success ,充值失败 failure
	private String remark;//充值申请不通过原因
	
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
	
	@Column(name="RECHARGE_USER_ID")
	public String getRechargeUserId() {
		return rechargeUserId;
	}
	public void setRechargeUserId(String rechargeUserId) {
		this.rechargeUserId = rechargeUserId;
	}
	
	@Column(name="PAY_WAY")
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	
	@Column(name="PAY_WAY_NUMBER")
	public String getPayWayNumber() {
		return payWayNumber;
	}
	public void setPayWayNumber(String payWayNumber) {
		this.payWayNumber = payWayNumber;
	}
	
	@Column(name="RECHARGE_DATE")
	public Date getRechargeDate() {
		return rechargeDate;
	}
	public void setRechargeDate(Date rechargeDate) {
		this.rechargeDate = rechargeDate;
	}
	
	@Column(name="VERIFY_DATE")
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	
	@Column(name="RECHARGE_SUM")
	public Integer getRechargeSum() {
		return rechargeSum;
	}
	public void setRechargeSum(Integer rechargeSum) {
		this.rechargeSum = rechargeSum;
	}
	
	@Column(name="RECHARGE_TYPE")
	public String getRechargeType() {
		return rechargeType;
	}
	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}
	
	@Column(name="SECURE_DDEAL_ID")
	public String getSecuredDealId() {
		return securedDealId;
	}
	public void setSecuredDealId(String securedDealId) {
		this.securedDealId = securedDealId;
	}
	
	@Column(name="STATUS")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
