package com.mg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name="fd_goods")
public class FDGoods {
	private String id;
	private Integer version;
	private String belong_merchant;
	private String goods_name;
	private String goods_number;
	private String brief;		   //简介
	private Double reference_price;//起步价
	private Double weight;
	private String detail;        //商品详情
	private String status;        //上线 ON ,下架 OFF ,导入后等待编辑 wait_verify ,货源商商品待审核 wait_check,货源商商品审核未通过 unpass,进入回收站 deleted
	private String category_id;
	private String imagePaths;    //商品图片
	private Double inventory;
	private Double salesVolume;
	private String up_date;
	private String down_date;
	private String enter_date;
	private String free_post;     //free免邮,charge收费
	private String mark;		  //备注
	private String queryStr;      //搜索关键字
	private String priceShow;     //是否显示价格  YES/NO
	
	private String category_name;
	
	@Id
	@Column(name="ID")
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Version
    @Column(name = "VERSION")
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
	
	@Column(name="BELONG_MERCHANT")
	public String getBelong_merchant() {
		return belong_merchant;
	}
	public void setBelong_merchant(String belong_merchant) {
		this.belong_merchant = belong_merchant;
	}
	
	@Column(name="GOODS_NAME")
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	
	@Column(name="GOODS_NUMBER")
	public String getGoods_number() {
		return goods_number;
	}
	public void setGoods_number(String goods_number) {
		this.goods_number = goods_number;
	}
	
	@Column(name="REFERENCE_PRICE")
	public Double getReference_price() {
		return reference_price;
	}
	public void setReference_price(Double reference_price) {
		this.reference_price = reference_price;
	}
	
	@Column(name="WEIGHT")
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@Column(name="STATUS")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="UP_DATE")
	public String getUp_date() {
		return up_date;
	}
	public void setUp_date(String up_date) {
		this.up_date = up_date;
	}
	
	@Column(name="DOWN_DATE")
	public String getDown_date() {
		return down_date;
	}
	public void setDown_date(String down_date) {
		this.down_date = down_date;
	}
	
	@Column(name="ENTER_DATE")
	public String getEnter_date() {
		return enter_date;
	}
	public void setEnter_date(String enter_date) {
		this.enter_date = enter_date;
	}
	
	@Column(name="FREE_POST")
	public String getFree_post() {
		return free_post;
	}
	public void setFree_post(String free_post) {
		this.free_post = free_post;
	}
	
	@Column(name="CATEGORY_ID")
	public String getCategory_id() {
		return category_id;
	}
	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	
	@Column(name="MARK")
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	@Column(name="BRIEF")
	public String getBrief() {
		return brief;
	}
	public void setBrief(String brief) {
		this.brief = brief;
	}
	
	@Column(name="DETAIL")
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	
	@Column(name="IMAGE_PATHS")
	public String getImagePaths() {
		return imagePaths;
	}
	public void setImagePaths(String imagePaths) {
		this.imagePaths = imagePaths;
	}
	
	@Column(name="QUERY_STR")
	public String getQueryStr() {
		return queryStr;
	}
	public void setQueryStr(String queryStr) {
		this.queryStr = queryStr;
	}
	
	@Column(name="INVENTORY")
	public Double getInventory() {
		return inventory;
	}
	public void setInventory(Double inventory) {
		this.inventory = inventory;
	}
	
	@Column(name="SALES_VOLUME")
	public Double getSalesVolume() {
		return salesVolume;
	}
	public void setSalesVolume(Double salesVolume) {
		this.salesVolume = salesVolume;
	}
	
	@Column(name="PRICE_SHOW")
	public String getPriceShow() {
		return priceShow;
	}
	public void setPriceShow(String priceShow) {
		this.priceShow = priceShow;
	}
	
	@Transient
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	
}
