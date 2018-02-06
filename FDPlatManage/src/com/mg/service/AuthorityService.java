package com.mg.service;

import java.util.List;

import com.mg.entity.FDAuthorityGroup;
import com.mg.entity.VFDManager;
import com.mg.util.Pager;


public interface AuthorityService {
	String addAuthorityGroup(String groupName, String remark, String[] menus, String adminId);
	String editAuthorityGroup(String groupId, String groupName, String remark, String[] menus, String adminId);
	String delAuthorityGroup(String groupId, String adminId);
	List<FDAuthorityGroup> getAuthorityGroupList(String groupName);
	FDAuthorityGroup getAuthorityGroupDetail(String groupId);
//	String addAuthorityRelation(String groupId, String[] menus);
	Pager userList(String loginName, String phone, Integer status, String startDate, String endDate, Integer page, Integer pageSize);
	Pager getManagerList(String loginName, String phone, Integer managerState, Integer userStatus, String startDate, String endDate, Integer page, Integer pageSize);
	VFDManager getManagerDetail(String userId);
	String addManager(String userId, String department, String position, String number, String authorityGroupId, String belongMarketingCenter, String adminId);
	String editManager(String userId, String department, String position, String number, String authorityGroupId, Integer state, String belongMarketingCenter, String adminId);
	String delManager(String userId, String adminId);
	String lockManager(String userId, String adminId);
	String unlockManager(String userId, String adminId);
	String checkOperateLog(String loginStr, String operateType, String startDate, String endDate, Integer page, Integer pageSize);
}
