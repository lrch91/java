package com.mg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "fd_system_param")
public class FDSystemParam {
	private String id;
	private Integer version;
	private Integer integralToCatcoinRate; //积分兑换猫币比例
	private Integer catcoinDeductRate; //积分兑换猫币比例   1元兑换需要n个猫币
	private String isCatcoinDeductOpen; //是否开启积分兑换猫币
	private Integer consumeIntegralRate;   //消费积分比例   1元消费可得n个积分
	private Integer rechargeCatcoinRate;  //猫币充值比例   1元可以充值N个猫币
	private Integer completeName;  
	private Integer completeGender; 
	private Integer completeCrop;  
	private Integer completeDetailAddress;  
	private Integer signIn;  		//签到积分
	private String freightFeeDesc;  //运费描述
	private Double minSoiltestAcreage;  //测土最低面积
	private Double minFertilizeNum;  //配肥最少数量
	private String serviceTel;  //客服号码

	@Id
	@Column(name = "ID")
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

	@Column(name="CONSUME_INTEGRAL_RATE")
	public Integer getConsumeIntegralRate() {
		return consumeIntegralRate;
	}

	public void setConsumeIntegralRate(Integer consumeIntegralRate) {
		this.consumeIntegralRate = consumeIntegralRate;
	}

	@Column(name="RECHARGE_CATCOIN_RATE")
	public Integer getRechargeCatcoinRate() {
		return rechargeCatcoinRate;
	}

	public void setRechargeCatcoinRate(Integer rechargeCatcoinRate) {
		this.rechargeCatcoinRate = rechargeCatcoinRate;
	}

	@Column(name="COMPLETE_NAME")
	public Integer getCompleteName() {
		return completeName;
	}

	public void setCompleteName(Integer completeName) {
		this.completeName = completeName;
	}

	@Column(name="COMPLETE_GENDER")
	public Integer getCompleteGender() {
		return completeGender;
	}

	public void setCompleteGender(Integer completeGender) {
		this.completeGender = completeGender;
	}

	@Column(name="COMPLETE_CROP")
	public Integer getCompleteCrop() {
		return completeCrop;
	}

	public void setCompleteCrop(Integer completeCrop) {
		this.completeCrop = completeCrop;
	}

	@Column(name="SIGN_IN")
	public Integer getSignIn() {
		return signIn;
	}

	public void setSignIn(Integer signIn) {
		this.signIn = signIn;
	}

	@Column(name="INTEGRAl_TO_CATCOIN_RATE")
	public Integer getIntegralToCatcoinRate() {
		return integralToCatcoinRate;
	}

	public void setIntegralToCatcoinRate(Integer integralToCatcoinRate) {
		this.integralToCatcoinRate = integralToCatcoinRate;
	}

	@Column(name="COMPLETE_DETAIL_ADDRESS")
	public Integer getCompleteDetailAddress() {
		return completeDetailAddress;
	}

	public void setCompleteDetailAddress(Integer completeDetailAddress) {
		this.completeDetailAddress = completeDetailAddress;
	}

	@Column(name="CATCOIN_DEDUCT_RATE")
	public Integer getCatcoinDeductRate() {
		return catcoinDeductRate;
	}

	public void setCatcoinDeductRate(Integer catcoinDeductRate) {
		this.catcoinDeductRate = catcoinDeductRate;
	}

	@Column(name="IS_CATCOIN_DEDUCT_OPEN")
	public String getIsCatcoinDeductOpen() {
		return isCatcoinDeductOpen;
	}

	public void setIsCatcoinDeductOpen(String isCatcoinDeductOpen) {
		this.isCatcoinDeductOpen = isCatcoinDeductOpen;
	}

	@Column(name="FREIGHT_FEE_DESC")
	public String getFreightFeeDesc() {
		return freightFeeDesc;
	}

	public void setFreightFeeDesc(String freightFeeDesc) {
		this.freightFeeDesc = freightFeeDesc;
	}

	@Column(name="MIN_SOILTEST_ACREAGE")
	public Double getMinSoiltestAcreage() {
		return minSoiltestAcreage;
	}

	public void setMinSoiltestAcreage(Double minSoiltestAcreage) {
		this.minSoiltestAcreage = minSoiltestAcreage;
	}

	@Column(name="MIN_FERTILIZE_NUM")
	public Double getMinFertilizeNum() {
		return minFertilizeNum;
	}

	public void setMinFertilizeNum(Double minFertilizeNum) {
		this.minFertilizeNum = minFertilizeNum;
	}
	
	@Column(name="SERVICE_TEL")
	public String getServiceTel() {
		return serviceTel;
	}

	public void setServiceTel(String serviceTel) {
		this.serviceTel = serviceTel;
	}

}
