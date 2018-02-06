package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_order")
public class FDOrder {
	private String id;
	private Integer version;
	private String belongMerchant;
	private String userId;
	private String orderNumber; 	//订单号
	private String status;			//已订购(ordered),已改价(priced)，已付款(payed)，已受理(accepted), 已取消(cancel)
	private Date submitDate;		//提交日期
	private Date priceDate;			//改价日期
	private Date payDate;			//支付日期
	private Date acceptDate;		//受理日期
	private Date deliveryDate;		//发货日期
	private Date cancelDate;		//取消日期
	private Date completeDate;		//完成日期
	private String userRemark;     //用户下单备注
	private String adminRemark;    //管理员备注
	private Integer userFrontState;		//用户端是否显示订单(逻辑删除)
	private String adminCancelRemark; 
	private String userCancelRemark;
	private String payWay;			  //支付方式 :alipay 支付宝, balance 余额
	private String payWaySN;	  	  //流水号
	private Double totalPrice;		  //修改后实际总价
	private Double deductPrice;		  //猫币抵扣金额
	private Double payPrice;		  //扣除猫币抵扣实际总价
	private String catcoinChangeId;	  //猫币抵扣记录ID
	
	private String acceptName;        //收货人姓名
	private String acceptTel;        //收货人电话
	private String city;
	private String area;
	private String street;
	private String address;
	private String belongMarketingCenter; //所属营销中心ID
	
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
	
	@Column(name="BELONG_MERCHANT")
	public String getBelongMerchant() {
		return belongMerchant;
	}
	public void setBelongMerchant(String belongMerchant) {
		this.belongMerchant = belongMerchant;
	}
	
	@Column(name="USER_ID")
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	@Column(name="ORDER_NUMBER")
	public String getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="SUBMIT_DATE")
	public Date getSubmitDate() {
		return submitDate;
	}
	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}
	
	@Column(name="PAY_DATE")
	public Date getPayDate() {
		return payDate;
	}
	public void setPayDate(Date payDate) {
		this.payDate = payDate;
	}
	
	@Column(name="CANCEL_DATE")
	public Date getCancelDate() {
		return cancelDate;
	}
	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}
	
	@Column(name="COMPLETE_DATE")
	public Date getCompleteDate() {
		return completeDate;
	}
	public void setCompleteDate(Date completeDate) {
		this.completeDate = completeDate;
	}
	
	@Column(name="USER_REMARK")
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	
	@Column(name="ADMIN_REMARK")
	public String getAdminRemark() {
		return adminRemark;
	}
	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}
	
	@Column(name="USER_FRONT_STATE")
	public Integer getUserFrontState() {
		return userFrontState;
	}
	public void setUserFrontState(Integer userFrontState) {
		this.userFrontState = userFrontState;
	}
	
	@Column(name="ADMIN_CANCEL_REMARK")
	public String getAdminCancelRemark() {
		return adminCancelRemark;
	}
	public void setAdminCancelRemark(String adminCancelRemark) {
		this.adminCancelRemark = adminCancelRemark;
	}
	
	@Column(name="USER_CANCEL_REMARK")
	public String getUserCancelRemark() {
		return userCancelRemark;
	}
	public void setUserCancelRemark(String userCancelRemark) {
		this.userCancelRemark = userCancelRemark;
	}
	
	@Column(name="PAY_WAY")
	public String getPayWay() {
		return payWay;
	}
	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}
	
	@Column(name="PAY_WAY_SN")
	public String getPayWaySN() {
		return payWaySN;
	}
	public void setPayWaySN(String payWaySN) {
		this.payWaySN = payWaySN;
	}
	
	@Column(name="ACCEPT_DATE")
	public Date getAcceptDate() {
		return acceptDate;
	}
	public void setAcceptDate(Date acceptDate) {
		this.acceptDate = acceptDate;
	}
	
	@Column(name="DELIVERY_DATE")
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	@Column(name="PRICE_DATE")
	public Date getPriceDate() {
		return priceDate;
	}
	public void setPriceDate(Date priceDate) {
		this.priceDate = priceDate;
	}
	
	@Column(name="ACCEPT_NAME")
	public String getAcceptName() {
		return acceptName;
	}
	public void setAcceptName(String acceptName) {
		this.acceptName = acceptName;
	}
	
	@Column(name="ACCEPT_TEL")
	public String getAcceptTel() {
		return acceptTel;
	}
	public void setAcceptTel(String acceptTel) {
		this.acceptTel = acceptTel;
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
	
	@Column(name="ADDRESS")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	@Column(name="TOTAL_PRICE")
	public Double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}
	
	@Column(name="PAY_PRICE")
	public Double getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}
	
	@Column(name="CATCOIN_CHANGE_ID")
	public String getCatcoinChangeId() {
		return catcoinChangeId;
	}
	public void setCatcoinChangeId(String catcoinChangeId) {
		this.catcoinChangeId = catcoinChangeId;
	}
	
	@Column(name="DEDUCT_PRICE")
	public Double getDeductPrice() {
		return deductPrice;
	}
	public void setDeductPrice(Double deductPrice) {
		this.deductPrice = deductPrice;
	}
	
	@Column(name="BELONG_MARKETING_CENTER")
	public String getBelongMarketingCenter() {
		return belongMarketingCenter;
	}
	public void setBelongMarketingCenter(String belongMarketingCenter) {
		this.belongMarketingCenter = belongMarketingCenter;
	}
	
}
