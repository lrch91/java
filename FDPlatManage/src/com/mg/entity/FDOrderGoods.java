package com.mg.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name="fd_order_goods")
public class FDOrderGoods {
	private String id;
	private Integer version;
	private String order_id;
	private String goods_id;
	private String goods_name;
	private String goods_number;
	private String brand;
	private Double reference_price;  //参考价
	private Double sell_count;      //售出数量
	private String express_number;
	private String express_type;
	private String delivery_date;
	private String goods_images;
	private Double weight;
	private String mark;
	
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
	
	@Column(name="ORDER_ID")
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	
	@Column(name="GOODS_ID")
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
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
	
	@Column(name="BRAND")
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	@Column(name="SELL_COUNT")
	public Double getSell_count() {
		return sell_count;
	}
	public void setSell_count(Double sell_count) {
		this.sell_count = sell_count;
	}
	
	@Column(name="EXPRESS_NUMBER")
	public String getExpress_number() {
		return express_number;
	}
	public void setExpress_number(String express_number) {
		this.express_number = express_number;
	}
	
	@Column(name="EXPRESS_TYPE")
	public String getExpress_type() {
		return express_type;
	}
	public void setExpress_type(String express_type) {
		this.express_type = express_type;
	}
	
	@Column(name="DELIVERY_DATE")
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	
	@Column(name="GOODS_IMAGES")
	public String getGoods_images() {
		return goods_images;
	}
	public void setGoods_images(String goods_images) {
		this.goods_images = goods_images;
	}
	
	@Column(name="WEIGHT")
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	@Column(name="MARK")
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	@Column(name="REFERENCE_PRICE")
	public Double getReference_price() {
		return reference_price;
	}
	public void setReference_price(Double reference_price) {
		this.reference_price = reference_price;
	}
	
	
}
