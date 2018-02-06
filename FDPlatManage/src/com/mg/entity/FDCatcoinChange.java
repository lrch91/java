package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_catcoin_change")
public class FDCatcoinChange {
	private String id;
	private Integer version;
	private String userId;//(对应着用户表的ID)
	private String changeType;//猫币变动类型(充值 订单支付 订单完成打款 订单取消退款 提现)
	private String attachId;//(关联充值、提现、交易表主键)
	private Integer changeSum;//DEAL_TYPE;  
	private Date changeDate;//DEAL_TYPE; 
	private Integer nowCoin;
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
	public Integer getChangeSum() {
		return changeSum;
	}
	public void setChangeSum(Integer changeSum) {
		this.changeSum = changeSum;
	}
	
	@Column(name="CHANGE_DATE")
	public Date getChangeDate() {
		return changeDate;
	}
	public void setChangeDate(Date changeDate) {
		this.changeDate = changeDate;
	}
	
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="NOW_COIN")
	public Integer getNowCoin() {
		return nowCoin;
	}
	public void setNowCoin(Integer nowCoin) {
		this.nowCoin = nowCoin;
	}
	
}
