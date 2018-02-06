package com.mg.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_photo_spots")
public class FDPhotoSpots {
	private String id;
	private Integer version;
	private String imagePaths;
	private Integer integralValue;
	private String spot1;
	private String spot2;
	private String spot3;
	private String spot4;
	private String spot5;
	private String spot6;
	private String spot7;
	private String spot8;
	private String spot9;
	private String spot10;
	private Date createDate;
	private Date sortDate;
	
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
	
	@Column(name="IMAGE_PATHS")
	public String getImagePaths() {
		return imagePaths;
	}
	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}
	
	@Column(name="SPOT1")
	public String getSpot1() {
		return spot1;
	}
	public void setSpot1(String spot1) {
		this.spot1 = spot1;
	}
	
	@Column(name="SPOT2")
	public String getSpot2() {
		return spot2;
	}
	public void setSpot2(String spot2) {
		this.spot2 = spot2;
	}
	
	@Column(name="SPOT3")
	public String getSpot3() {
		return spot3;
	}
	public void setSpot3(String spot3) {
		this.spot3 = spot3;
	}
	
	@Column(name="SPOT4")
	public String getSpot4() {
		return spot4;
	}
	public void setSpot4(String spot4) {
		this.spot4 = spot4;
	}
	
	@Column(name="SPOT5")
	public String getSpot5() {
		return spot5;
	}
	public void setSpot5(String spot5) {
		this.spot5 = spot5;
	}
	
	@Column(name="SPOT6")
	public String getSpot6() {
		return spot6;
	}
	public void setSpot6(String spot6) {
		this.spot6 = spot6;
	}
	
	@Column(name="SPOT7")
	public String getSpot7() {
		return spot7;
	}
	public void setSpot7(String spot7) {
		this.spot7 = spot7;
	}
	
	@Column(name="SPOT8")
	public String getSpot8() {
		return spot8;
	}
	public void setSpot8(String spot8) {
		this.spot8 = spot8;
	}
	
	@Column(name="SPOT9")
	public String getSpot9() {
		return spot9;
	}
	public void setSpot9(String spot9) {
		this.spot9 = spot9;
	}
	
	@Column(name="SPOT10")
	public String getSpot10() {
		return spot10;
	}
	public void setSpot10(String spot10) {
		this.spot10 = spot10;
	}
	
	@Column(name="CREATE_DATE")
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="SORT_DATE")
	public Date getSortDate() {
		return sortDate;
	}
	public void setSortDate(Date sortDate) {
		this.sortDate = sortDate;
	}
	
	@Column(name="INTEGRAL_VALUE")
	public Integer getIntegralValue() {
		return integralValue;
	}
	public void setIntegralValue(Integer integralValue) {
		this.integralValue = integralValue;
	}
	
}
