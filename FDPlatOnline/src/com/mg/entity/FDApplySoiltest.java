package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name = "fd_apply_soiltest")
public class FDApplySoiltest {
	private String id;
	private Integer version;
	private String userId;
	private String trueName; //申请人姓名
	private String phone;
	private String city;
	private String area;
	private String street;
	private String detailAddress;
	private Double acreage;  //面积
	
	
	private String lastCropName; //上茬作物
	private String lastOutput;  //上茬产量
	private String baseFertilizer; //基肥
	private String baseFertilizerSum; //基肥用量
	private String addFertilizer; //追肥
	private String addFertilizerSum; //追肥用量
	private String nextCropName; //下茬作物
	private String nextOutput;  //下茬产量
	private Integer soil_n;  //土壤速效氮
	private Integer soil_p;  //土壤速效磷
	private Integer soil_k;  //土壤速效钾
	private Integer soil_om;  //有机质含量
	private Integer soil_ph;  //土壤酸碱度
	private Integer soil_salt;  //土壤盐含量
	
	private String userRemark;
	private String adminRemark;
	private Date createDate;
	private Date handleDate;
	private Date endDate;
	private String status;  //applied 申请中, handled 已受理
	private String handlerName;
	
	private String cityName;
	private String areaName;
	private String streetName;

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
	
	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "ADMIN_REMARK")
	public String getAdminRemark() {
		return adminRemark;
	}

	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "STATUS")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "TRUE_NAME")
	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	@Column(name="PHONE")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	@Column(name="DETAIL_ADDRESS")
	public String getDetailAddress() {
		return detailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}

	@Column(name="ACREAGE")
	public Double getAcreage() {
		return acreage;
	}

	public void setAcreage(Double acreage) {
		this.acreage = acreage;
	}

	@Column(name="USER_REMARK")
	public String getUserRemark() {
		return userRemark;
	}

	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}

	@Column(name="END_DATE")
	public Date getEndDate() {
		return endDate;
	}
	
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name="LAST_CROP_NAME")
	public String getLastCropName() {
		return lastCropName;
	}

	public void setLastCropName(String lastCropName) {
		this.lastCropName = lastCropName;
	}

	@Column(name="LAST_OUTPUT")
	public String getLastOutput() {
		return lastOutput;
	}

	public void setLastOutput(String lastOutput) {
		this.lastOutput = lastOutput;
	}

	@Column(name="BASE_FERTILIZER")
	public String getBaseFertilizer() {
		return baseFertilizer;
	}

	public void setBaseFertilizer(String baseFertilizer) {
		this.baseFertilizer = baseFertilizer;
	}

	@Column(name="BASE_FERTILIZER_SUM")
	public String getBaseFertilizerSum() {
		return baseFertilizerSum;
	}

	public void setBaseFertilizerSum(String baseFertilizerSum) {
		this.baseFertilizerSum = baseFertilizerSum;
	}

	@Column(name="ADD_FERTILIZER")
	public String getAddFertilizer() {
		return addFertilizer;
	}

	public void setAddFertilizer(String addFertilizer) {
		this.addFertilizer = addFertilizer;
	}

	@Column(name="ADD_FERTILIZER_SUM")
	public String getAddFertilizerSum() {
		return addFertilizerSum;
	}

	public void setAddFertilizerSum(String addFertilizerSum) {
		this.addFertilizerSum = addFertilizerSum;
	}

	@Column(name="NEXT_CROP_NAME")
	public String getNextCropName() {
		return nextCropName;
	}

	public void setNextCropName(String nextCropName) {
		this.nextCropName = nextCropName;
	}

	@Column(name="NEXT_OUTPUT")
	public String getNextOutput() {
		return nextOutput;
	}

	public void setNextOutput(String nextOutput) {
		this.nextOutput = nextOutput;
	}

	@Column(name="SOIL_N")
	public Integer getSoil_n() {
		return soil_n;
	}

	public void setSoil_n(Integer soil_n) {
		this.soil_n = soil_n;
	}

	@Column(name="SOIL_P")
	public Integer getSoil_p() {
		return soil_p;
	}

	public void setSoil_p(Integer soil_p) {
		this.soil_p = soil_p;
	}

	@Column(name="SOIL_K")
	public Integer getSoil_k() {
		return soil_k;
	}

	public void setSoil_k(Integer soil_k) {
		this.soil_k = soil_k;
	}

	@Column(name="SOIL_PH")
	public Integer getSoil_ph() {
		return soil_ph;
	}

	public void setSoil_ph(Integer soil_ph) {
		this.soil_ph = soil_ph;
	}

	@Column(name="SOIL_SALT")
	public Integer getSoil_salt() {
		return soil_salt;
	}

	public void setSoil_salt(Integer soil_salt) {
		this.soil_salt = soil_salt;
	}

	@Column(name="SOIL_OM")
	public Integer getSoil_om() {
		return soil_om;
	}

	public void setSoil_om(Integer soil_om) {
		this.soil_om = soil_om;
	}
	
	@Column(name="HANDLER_NAME")
	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	
	@Column(name="HANDLE_DATE")
	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}
	
	@Column(name="STREET")
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	@Transient
	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	@Transient
	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	@Transient
	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
}
