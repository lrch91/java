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
@Table(name="fd_authority_group")
public class FDAuthorityGroup {
	private String id;
	private Integer version;
	private String groupName;
	private Date createTime;
	private String remark;
	
	private List<FDAuthorityRelation> authorityRelationList;
	
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
	
	@Column(name="GROUP_NAME")
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="REMARK")
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	public List<FDAuthorityRelation> getAuthorityRelationList() {
		return authorityRelationList;
	}

	public void setAuthorityRelationList(List<FDAuthorityRelation> authorityRelationList) {
		this.authorityRelationList = authorityRelationList;
	}

}
