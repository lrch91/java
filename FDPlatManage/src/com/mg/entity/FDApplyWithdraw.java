package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_apply_withdraw")
public class FDApplyWithdraw {
	private String id;
	private Integer version;
	private String proposerId;//PROPOSER_ID;申请人ID(对应着用户表的ID)
	private Date applyDate;//APPLY_DATE;申请时间
	private Double withdrawSum;
	private String withdrawWay;//WITHDRAW_WAY;提现渠道
	private String withdrawWayNumber;//WITHDRAW_WAY_NUMBER;提现渠道账号
	private String withdrawWayName;//WITHDRAW_WAY_NAME;提现渠道账号人名
	private Integer applyStatus;//APPLY_STATUS;申请状态(未审核 wait_verify，已通过 pass，未通过unpass,已完成 finished)
	private String verifierId;//VERIFIER_ID;审核人ID(对应着用户表的ID)
	private Date verifyDate;//VERIFY_DATE;审核时间
	private String transferNumber;//TRANSFER_NUMBER;审核通过管理员转账流水号
	private Date transferDate;//TRANSFER_DATE;转账时间
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
	
	@Column(name="PROPOSER_ID")
	public String getProposerId() {
		return proposerId;
	}
	public void setProposerId(String proposerId) {
		this.proposerId = proposerId;
	}
	
	@Column(name="APPLY_DATE")
	public Date getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}
	
	@Column(name="WITHDRAW_WAY_NUMBER")
	public String getWithdrawWayNumber() {
		return withdrawWayNumber;
	}
	public void setWithdrawWayNumber(String withdrawWayNumber) {
		this.withdrawWayNumber = withdrawWayNumber;
	}
	
	@Column(name="APPLY_STATUS")
	public Integer getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}
	
	@Column(name="VERIFIER_ID")
	public String getVerifierId() {
		return verifierId;
	}
	public void setVerifierId(String verifierId) {
		this.verifierId = verifierId;
	}
	
	@Column(name="VERIFY_DATE")
	public Date getVerifyDate() {
		return verifyDate;
	}
	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}
	
	@Column(name="TRANSFER_NUMBER")
	public String getTransferNumber() {
		return transferNumber;
	}
	public void setTransferNumber(String transferNumber) {
		this.transferNumber = transferNumber;
	}
	
	@Column(name="TRANSFER_DATE")
	public Date getTransferDate() {
		return transferDate;
	}
	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}
	
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="WITHDRAW_SUM")
	public Double getWithdrawSum() {
		return withdrawSum;
	}
	public void setWithdrawSum(Double withdrawSum) {
		this.withdrawSum = withdrawSum;
	}
	
	@Column(name="WITHDRAW_WAY")
	public String getWithdrawWay() {
		return withdrawWay;
	}
	public void setWithdrawWay(String withdrawWay) {
		this.withdrawWay = withdrawWay;
	}
	
	@Column(name="WITHDRAW_WAY_NAME")
	public String getWithdrawWayName() {
		return withdrawWayName;
	}
	public void setWithdrawWayName(String withdrawWayName) {
		this.withdrawWayName = withdrawWayName;
	}
	
}
