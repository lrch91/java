package com.mg.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
@Table(name="fd_category")
public class FDCategory {
	private String id;
	private Integer version;
	private String name;
	private String imagePath;
	private Integer level;
	private String parentId;
	private Date sortDate;
	
	private List<FDCategory> childList;
	
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
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="LEVEL")
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	@Column(name="PARENT_ID")
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="SORT_DATE")
	public Date getSortDate() {
		return sortDate;
	}
	public void setSortDate(Date sortDate) {
		this.sortDate = sortDate;
	}
	
	@Column(name="IMAGE_PATH")
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	
	@Transient
	public List<FDCategory> getChildList() {
		return childList;
	}
	public void setChildList(List<FDCategory> childList) {
		this.childList = childList;
	}
	
}
