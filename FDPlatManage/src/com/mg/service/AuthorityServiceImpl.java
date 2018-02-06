package com.mg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.AuthorityGroupDao;
import com.mg.dao.AuthorityRelationDao;
import com.mg.dao.ManagerInfoDao;
import com.mg.dao.OperateLogDao;
import com.mg.dao.UserDao;
import com.mg.dao.VManagerDao;
import com.mg.entity.FDAuthorityGroup;
import com.mg.entity.FDAuthorityRelation;
import com.mg.entity.FDManagerInfo;
import com.mg.entity.FDUser;
import com.mg.entity.VFDManager;
import com.mg.util.ConUtil;
import com.mg.util.Pager;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {
	@Autowired
	UserDao userDao;
	@Autowired
	ManagerInfoDao managerInfoDao;
	@Autowired
	VManagerDao vManagerDao;
	@Autowired
	AuthorityGroupDao authorityGroupDao;
	@Autowired
	AuthorityRelationDao authorityRelationDao;
	@Autowired
	OperateLogDao operateLogDao;
	@Autowired
	ConUtil conUtil;
	
	@Override
	public String addAuthorityGroup(String groupName, String remark, String[] menus, String adminId){
		FDAuthorityGroup ag = new FDAuthorityGroup();
		ag.setId(UUID.randomUUID().toString());
		ag.setGroupName(groupName);
		ag.setRemark(remark);
		ag.setCreateTime(new Date());
		
		String menusStr = "";
		for(String str:menus){
			if(menusStr.equals("")){
				menusStr = str;
			}else{
				menusStr += (","+str);
			}
		}
		FDAuthorityRelation ar = new FDAuthorityRelation();
		ar.setId(UUID.randomUUID().toString());
		ar.setAuthorityGroupId(ag.getId());
		ar.setMenu(menusStr);
		authorityRelationDao.add(ar);

		authorityGroupDao.add(ag);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "添加权限组", ag.getGroupName());
		return null;
	}
	
	@Override
	public String editAuthorityGroup(String groupId, String groupName, String remark, String[] menus, String adminId){
		FDAuthorityGroup ag = authorityGroupDao.findById(groupId);
		if(ag==null)
			return "未找到权限组信息";
		ag.setGroupName(groupName);
		ag.setRemark(remark);
		FDAuthorityRelation tempAr = authorityRelationDao.findOne(" where authorityGroupId='"+groupId+"'");
		String menusStr = "";
		for(String str:menus){
			if(menusStr.equals("")){
				menusStr = str;
			}else{
				menusStr += (","+str);
			}
		}
		tempAr.setMenu(menusStr);
		String tempName = ag.getGroupName();
		authorityRelationDao.update(tempAr);
		
		authorityGroupDao.update(ag);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "添加权限组", tempName);
		return null;
	}
	
	@Override
	public String delAuthorityGroup(String groupId, String adminId){
		FDAuthorityGroup ag = authorityGroupDao.findById(groupId);
		if(ag==null)
			return "未找到权限组信息";
		List<FDManagerInfo> mList = managerInfoDao.findBycondition(" where authorityGroupId='"+groupId+"'");
		if(mList!=null&&mList.size()>0){
			return "权限组已添加管理员数据，无法删除";
		}
		String tempName = ag.getGroupName();
		List<FDAuthorityRelation> arList = authorityRelationDao.findBycondition(" where authorityGroupId='"+groupId+"'");
		if(arList!=null&&arList.size()>0){
			for(FDAuthorityRelation ar:arList){
				authorityRelationDao.deleteById(ar.getId());
			}
		}
		authorityGroupDao.deleteById(ag.getId());
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "添加权限组", tempName);
		return null;
	}
	
	@Override
	public List<FDAuthorityGroup> getAuthorityGroupList(String groupName){
		String where = " where 1=1 ";
		if(!Utils.isEmpty(groupName)){
			where += " and groupName='"+groupName+"'";
		}
		List<FDAuthorityGroup> agList = authorityGroupDao.findBycondition(where);
		return agList;
	}
	
	@Override
	public FDAuthorityGroup getAuthorityGroupDetail(String groupId){
		FDAuthorityGroup ag = authorityGroupDao.findById(groupId);
		if(ag==null)
			return null;
		List<FDAuthorityRelation> arList = authorityRelationDao.findBycondition(" where authorityGroupId='"+groupId+"'");
		if(arList!=null&&arList.size()>0){
			ag.setAuthorityRelationList(arList);
		}
		return ag;
	}
	
	/*@Override
	public String addAuthorityRelation(String groupId, String[] menus){
		List<FDAuthorityRelation> arList = authorityRelationDao.findBycondition(" where authorityGroupId='"+groupId+"'");
		if(arList!=null&&arList.size()>0){
			for(FDAuthorityRelation tempAr:arList){
				authorityRelationDao.deleteById(tempAr.getId());
			}
		}
		for(String menu:menus){
			FDAuthorityRelation ar = new FDAuthorityRelation();
			ar.setId(UUID.randomUUID().toString());
			ar.setAuthorityGroupId(groupId);
			ar.setMenu(menu);
			authorityRelationDao.add(ar);
		}
		return null;
	}*/
	
	@Override
	public Pager userList(String loginName, String phone, Integer status, String startDate, String endDate, Integer page, Integer pageSize){
		String where = " where 1=1 ";
		if(!Utils.isEmpty(loginName))
			where += " and loginName='"+loginName+"' ";
		if(!Utils.isEmpty(phone))
			where += " and phone='"+phone+"' ";
		if(status!=null)
			where += " and status="+status;
		if(!Utils.isEmpty(startDate))
			where += " and registerDate>='"+startDate+" 00:00:00'";
		if(!Utils.isEmpty(endDate))
			where += " and registerDate<='"+endDate+" 23:59:59'";
		return userDao.findBycondition(page, pageSize, where);
	}
	
	@Override
	public Pager getManagerList(String loginName, String phone, Integer managerState, Integer userStatus, String startDate, String endDate, Integer page, Integer pageSize){
		String where = " where 1=1 ";
		if(!Utils.isEmpty(loginName))
			where += " and loginName='"+loginName+"' ";
		if(!Utils.isEmpty(phone))
			where += " and phone='"+phone+"' ";
		if(managerState!=null)
			where += " and managerState="+managerState;
		if(userStatus!=null)
			where += " and userStatus="+userStatus;
		if(!Utils.isEmpty(startDate))
			where += " and registerDate>='"+startDate+" 00:00:00'";
		if(!Utils.isEmpty(endDate))
			where += " and registerDate<='"+endDate+" 23:59:59'";
		return vManagerDao.findBycondition(page, pageSize, where);
	}
	@Override
	public VFDManager getManagerDetail(String userId){
		VFDManager m = vManagerDao.findOne(" where userId='"+userId+"'");
		return m;
	}
	
	@Override
	public String addManager(String userId, String department, String position, String number, String authorityGroupId, String belongMarketingCenter, String adminId){
		FDUser u = userDao.findById(userId);
		if(u==null)
			return "未找到用户信息";
		if(!Status.USER_NORMAL.equals(u.getStatus())){
			return "用户已禁用";
		}
		FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+userId+"' ");
		if(mi!=null){
			return "已添加管理员账号";
		}
		FDManagerInfo tempMi = new FDManagerInfo();
		tempMi.setId(UUID.randomUUID().toString());
		tempMi.setUserId(userId);
		tempMi.setDepartment(department);
		tempMi.setPosition(position);
		tempMi.setNumber(number);
		tempMi.setState(Status.MANANGER_STATE_NORMAL);
		tempMi.setIsSuper(Status.MANANGER_SUPERMANAGER_NO);
		if(!Utils.isEmpty(belongMarketingCenter)){
			tempMi.setBelongMarketingCenter(belongMarketingCenter);
		}
		tempMi.setCreateDate(new Date());
		FDAuthorityGroup ag = authorityGroupDao.findById(authorityGroupId);
		if(ag==null)
			return "未找到权限组信息";
		tempMi.setAuthorityGroupId(authorityGroupId);
		managerInfoDao.add(tempMi);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "添加管理员", u.getId());
		return null;
	}
	
	@Override
	public String editManager(String userId, String department, String position, String number, String authorityGroupId, Integer state, String belongMarketingCenter, String adminId){
		FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+userId+"' ");
		if(mi==null){
			return "未找到管理员账号";
		}
		if(mi.getIsSuper().equals(Status.MANANGER_SUPERMANAGER_YES)){
			return "无法操作超级管理员账号";
		}
		if(!Utils.isEmpty(belongMarketingCenter)){
			mi.setBelongMarketingCenter(belongMarketingCenter);
		}else{
			mi.setBelongMarketingCenter("");
		}
		mi.setDepartment(department);
		mi.setPosition(position);
		mi.setNumber(number);
		mi.setState(state);
		mi.setAuthorityGroupId(authorityGroupId);
		FDAuthorityGroup ag = authorityGroupDao.findById(authorityGroupId);
		if(ag==null)
			return "未找到权限组信息";
		managerInfoDao.update(mi);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "编辑管理员", userId);
		return null;
	}
	
	@Override
	public String delManager(String userId, String adminId){
		FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+userId+"' ");
		if(mi==null){
			return "未找到管理员账号";
		}
		if(mi.getIsSuper().equals(Status.MANANGER_SUPERMANAGER_YES)){
			return "无法操作超级管理员账号";
		}
		managerInfoDao.deleteById(mi.getId());
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "删除管理员", userId);
		return null;
	}
	
	@Override
	public String lockManager(String userId, String adminId){
		FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+userId+"' ");
		if(mi==null){
			return "未找到管理员账号";
		}
		if(mi.getIsSuper().equals(Status.MANANGER_SUPERMANAGER_YES)){
			return "无法操作超级管理员账号";
		}
		mi.setState(Status.MANANGER_STATE_LOCKED);
		managerInfoDao.update(mi);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "冻结管理员", userId);
		return null;
	}
	
	@Override
	public String unlockManager(String userId, String adminId){
		FDManagerInfo mi = managerInfoDao.findOne(" where userId='"+userId+"' ");
		if(mi==null){
			return "未找到管理员账号";
		}
		if(mi.getIsSuper().equals(Status.MANANGER_SUPERMANAGER_YES)){
			return "无法操作超级管理员账号";
		}
		mi.setState(Status.MANANGER_STATE_NORMAL);
		managerInfoDao.update(mi);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "解冻管理员", userId);
		return null;
	}
	
	@Override
	public String checkOperateLog(String loginStr, String operateType, String startDate, String endDate, Integer page, Integer pageSize){
		String sql = " select l.*,u.PHONE,u.LOGIN_NAME ";
		String countSql = " select count(*) as COUNT ";
		String where = " from fd_operate_log l left join fd_user u on u.ID=l.USER_ID where 1=1 ";
		if(!Utils.isEmpty(loginStr)){
			loginStr = loginStr.trim();
			FDUser u = userDao.findOne(" where phone= '"+loginStr+"' ");
			if(u==null||Utils.isEmpty(u.getId())){
				u = userDao.findOne(" where loginName= '"+loginStr+"' ");
			}
			if(u!=null&&!Utils.isEmpty(u.getId())){
				where += " and l.USER_ID = '"+u.getId()+"' ";
			}
		}
		if(!Utils.isEmpty(startDate)){
			where += " and l.OPERATE_DATE>= '"+startDate+" 00:00:00' ";
		}
		if(!Utils.isEmpty(endDate)){
			where += " and l.OPERATE_DATE<= '"+startDate+" 23:59:59' ";
		}
		if(!Utils.isEmpty(operateType)){
			where += " and l.OPERATE_TYPE ='"+operateType+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by l.OPERATE_DATE desc ");
		List<Map<String, Object>> list = conUtil.getData(countSql+where);
		json.put("total", ((Long)list.get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
}
