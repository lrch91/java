package com.mg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.AreaDao;
import com.mg.dao.ClassRequestDao;
import com.mg.dao.ClassSetDao;
import com.mg.dao.GradeSetDao;
import com.mg.dao.ManagerInfoDao;
import com.mg.dao.OperateLogDao;
import com.mg.dao.UserDao;
import com.mg.entity.FDArea;
import com.mg.entity.FDClassRequest;
import com.mg.entity.FDClassSet;
import com.mg.entity.FDGradeSet;
import com.mg.entity.FDUser;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;
import com.mg.vo.UserInfoVo;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	UserDao userDao;
	@Autowired
	AreaDao areaDao;
	@Autowired
	ClassSetDao classSetDao;
	@Autowired
	GradeSetDao gradeSetDao;
	@Autowired
	ClassRequestDao classRequestDao;
	@Autowired
	ManagerInfoDao managerInfoDao;
	@Autowired
	OperateLogDao operateLogDao;
	@Autowired
	ConUtil conUtil;
	
	@Override
	public String userList(String loginName, String phone, Integer memberLevel, Integer status, String startDate, String endDate, Integer page, Integer pageSize){
//		List<Map<String,Object>> list3 = conUtil.getData(" select count(*) COUNT from fd_grade_set where VALUE <="+u.getHistoryIntegral());
//		Integer userLevel = ((Long)list3.get(0).get("COUNT")).intValue();
//		ui.setUserLevel(userLevel);
//		List<Map<String,Object>> list4 = conUtil.getData(" select NAME from fd_grade_set where LEVEL="+userLevel);
		String where = " where 1=1 ";
		if(!Utils.isEmpty(loginName))
			where += " and u.LOGIN_NAME='"+loginName+"' ";
		if(!Utils.isEmpty(phone))
			where += " and u.PHONE='"+phone+"' ";
		if(memberLevel!=null&&memberLevel==0){
			where += " and u.IS_MEMBER="+Status.USER_ISMEMBER_NO;
		}else if(memberLevel!=null&&memberLevel>0){
			FDClassSet cs1 = classSetDao.findOne(" where level="+memberLevel);
			FDClassSet cs2 = classSetDao.findOne(" where level="+(memberLevel+1));
			where += " and u.IS_MEMBER=1 and (u.history_balance+(select cs.VALUE from fd_class_set cs where cs.LEVEL=u.MANUAL_MEMBER_LEVEL limit 0,1)>="+cs1.getValue()+")";
			if(cs2!=null&&cs2.getId()!=null){
				where += " and (u.history_balance+(select cs.VALUE from fd_class_set cs where cs.LEVEL=u.MANUAL_MEMBER_LEVEL limit 0,1)<"+cs2.getValue()+") ";
			}
		}
		if(status!=null)
			where += " and u.STATUS="+status;
		if(!Utils.isEmpty(startDate))
			where += " and u.REGISTER_DATE>='"+startDate+" 00:00:00'";
		if(!Utils.isEmpty(endDate))
			where += " and u.REGISTER_DATE<='"+endDate+" 23:59:59'";
		JSONObject json = conUtil.getRows(" select u.*,(select g1.NAME from fd_grade_set g1 where g1.LEVEL=(select count(*) from fd_grade_set g where u.HISTORY_INTEGRAL>=g.VALUE)) USER_GRADE_STR "
				+ " ,(select c1.NAME from fd_class_set c1 where c1.level=((select count(*) from fd_class_set c where (u.HISTORY_BALANCE+(select c2.value from fd_class_set c2 where c2.level=u.MANUAL_MEMBER_LEVEL))>=c.VALUE)*(u.IS_MEMBER))) MEMBER_CLASS_STR "
				+ "  from fd_user u "+where);
		Map<String,Object> countMap = (conUtil.getData(" select count(*) COUNT from fd_user u "+where)).get(0);
		json.put("total", ((Long)countMap.get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public UserInfoVo userDetail(String userId){
		FDUser u = userDao.findById(userId);
		UserInfoVo ui = new UserInfoVo();
		ui.setId(u.getId());
		ui.setCatcoin(u.getCatcoin());
		ui.setIntegral(u.getIntegral());
		ui.setHistoryIntegral(u.getHistoryIntegral());
		ui.setImagePath(u.getImagePath());
		ui.setLoginName(u.getLoginName());
		ui.setPhone(u.getPhone());
		ui.setRegisterDate(u.getRegisterDate());
		ui.setBecomeMemberDate(u.getBecomeMemberDate());
		ui.setStatus(u.getStatus());
//		ui.setTrueName(u.getTrueName());
		if(!Utils.isEmpty(u.getCity())){
			ui.setCity(u.getCity());
			FDArea city = areaDao.findById(ui.getCity());
			if(city!=null)
				ui.setCityName(city.getName());
		}
		if(!Utils.isEmpty(u.getArea())){
			ui.setArea(u.getArea());
			FDArea area = areaDao.findById(ui.getArea());
			if(area!=null)
				ui.setAreaName(area.getName());
		}
		if(!Utils.isEmpty(u.getStreet())){
			ui.setStreet(u.getStreet());
			FDArea street = areaDao.findById(ui.getStreet());
			if(street!=null)
				ui.setStreetName(street.getName());
		}
		ui.setDetailAddress(u.getDetailAddress());
		ui.setGender(u.getGender());
		ui.setMyCrops(u.getMyCrops());
		ui.setIsMember(u.getIsMember());
		if(Status.USER_ISMEMBER_YES.equals(u.getIsMember())){
			List<Map<String,Object>> list0 = conUtil.getData(" select VALUE from fd_class_set where LEVEL="+u.getManualMemberLevel());
			Double historyBalance = ((int)list0.get(0).get("VALUE"))+u.getHistoryBalance();
			List<Map<String,Object>> list1 = conUtil.getData(" select count(*) COUNT from fd_class_set where VALUE <="+historyBalance);
			Integer memberLevel = ((Long)list1.get(0).get("COUNT")).intValue();
			ui.setMemberLevel(memberLevel);
			List<Map<String,Object>> list2 = conUtil.getData(" select NAME from fd_class_set where LEVEL="+memberLevel);
			ui.setMemberLevelName((String)list2.get(0).get("NAME"));
		}
		List<Map<String,Object>> list3 = conUtil.getData(" select count(*) COUNT from fd_grade_set where VALUE <="+u.getHistoryIntegral());
		Integer userLevel = ((Long)list3.get(0).get("COUNT")).intValue();
		ui.setUserLevel(userLevel);
		List<Map<String,Object>> list4 = conUtil.getData(" select NAME from fd_grade_set where LEVEL="+userLevel);
		if(list4!=null&&list4.size()>0&&list4.get(0).get("NAME")!=null){
			ui.setUserLevelName((String)list4.get(0).get("NAME"));
		}
		return ui;
	}
	
	@Override
	public String verifyUserApply(String userId){
		FDUser u = userDao.findById(userId);
		if(u==null)
			return "未找到用户信息";
		if(!u.getVerifyState().equals(Status.USER_VERIFY_UNVERIFY))
			return "已审核过此注册申请信息";
		u.setVerifyState(Status.USER_VERIFY_VERIFIED);
		u.setStatus(Status.USER_NORMAL);
		userDao.update(u);
		return null;
	}
	
	@Override
	public String lockUser(String userId, String adminId){
		FDUser u = userDao.findById(userId);
		if(u==null)
			return "未找到用户信息";
		u.setStatus(Status.USER_LOCKED);
		userDao.update(u);
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "冻结用户", userId);
		return null;
	}
	
	@Override
	public String unlockUser(String userId, String adminId){
		FDUser u = userDao.findById(userId);
		if(u==null)
			return "未找到用户信息";
		u.setStatus(Status.USER_NORMAL);
		userDao.update(u);
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "解冻用户", userId);
		return null;
	}
	
	@Override
	public String memberRequestList(String loginName, String phone, Integer page, Integer pageSize){
		String sql = " select r.*, (select u.LOGIN_NAME from fd_user u where u.ID=r.USER_ID) LOGIN_NAME, "
				+ " (select u.PHONE from fd_user u where u.ID=r.USER_ID) PHONE, "
				+ " (select u.PHONE from fd_user u where u.ID=r.USER_ID) PHONE, "
				+ " (select s.NAME from fd_class_set s where s.LEVEL=r.LEVEL) CLASS_LEVEL_NAME, "
				+ " (select u1.LOGIN_NAME from fd_user u1 where u1.ID=r.ADMIN_ID) SUBMITER_NAME, "
				+ " (select u1.PHONE from fd_user u1 where u1.ID=r.ADMIN_ID) SUBMITER_PHONE "
				+ " from fd_class_request r ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(loginName)){
			FDUser u = userDao.findOne(" where loginName='"+loginName+"' ");
			if(u!=null)
				where+= " and r.USER_ID='"+u.getId()+"' ";
		}
		if(!Utils.isEmpty(phone)){
			FDUser u = userDao.findOne(" where phone='"+phone+"' ");
			if(u!=null)
				where+= " and r.USER_ID='"+u.getId()+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by r.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		String count_sql = " select count(*) as COUNT from fd_class_request r ";
		json.put("total", ((Long)conUtil.getData(count_sql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String requestMemberLevel(String userId, Integer level, String adminId){
		FDUser u = userDao.findById(userId);
		if(u==null)
			return "未找到用户信息";
		FDClassSet cs = classSetDao.findOne(" where level = "+level+" ");
		if(cs==null)
			return "设置的会员等级不正确";
		FDClassRequest cr = new FDClassRequest();
		cr.setId(UUID.randomUUID().toString());
		cr.setLevel(level);
		cr.setAdminId(adminId);
		cr.setStatus(Status.CLASSREQUEST_STATUS_WAITHANDLE);
		cr.setUserId(userId);
		cr.setCreateDate(new Date());
		classRequestDao.add(cr);
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "添加会员申请", userId);
		return null;
	}
	
	@Override
	public String requestMemberOut(String userId, String adminId){
		FDUser u = userDao.findById(userId);
		if(u==null)
			return "未找到用户信息";
		FDClassRequest cr = new FDClassRequest();
		cr.setId(UUID.randomUUID().toString());
		cr.setLevel(0);
		cr.setAdminId(adminId);
		cr.setStatus(Status.CLASSREQUEST_STATUS_WAITHANDLE);
		cr.setUserId(userId);
		cr.setCreateDate(new Date());
		classRequestDao.add(cr);
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "移除会员申请", userId);
		return null;
	}
	
	@Override
	public String passLevelRequest(String requestId, String adminId){
		FDClassRequest cr = classRequestDao.findById(requestId);
		if(cr==null)
			return "未找到申请信息";
		if(!cr.getStatus().equals(Status.CLASSREQUEST_STATUS_WAITHANDLE))
			return "非待处理申请";
		FDUser u = userDao.findById(cr.getUserId());
		if(u==null)
			return "未找到用户信息";
		if(cr.getLevel()==0){
			u.setIsMember(Status.USER_ISMEMBER_NO);
			u.setManualMemberLevel(0);
			u.setHistoryBalance(0.0);
			u.setBecomeMemberDate(null);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "审核会员等级设定通过", cr.getUserId());
		}else if(cr.getLevel()>0){
			FDClassSet cs = classSetDao.findOne(" where level = "+cr.getLevel()+" ");
			if(cs==null)
				return "设置的会员等级不正确";
			
			u.setIsMember(Status.USER_ISMEMBER_YES);
			u.setManualMemberLevel(cr.getLevel());
			u.setHistoryBalance(0.0);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "审核会员移除通过", cr.getUserId());
			u.setBecomeMemberDate(new Date());
		}
		userDao.update(u);
		
		cr.setStatus(Status.CLASSREQUEST_STATUS_PASS);
		cr.setHandleDate(new Date());
		classRequestDao.update(cr);
		return null;
	}
	
	@Override
	public String rejectLevelRequest(String requestId, String adminId){
		FDClassRequest cr = classRequestDao.findById(requestId);
		if(cr==null)
			return "未找到申请信息";
		cr.setStatus(Status.CLASSREQUEST_STATUS_REJECT);
		cr.setHandleDate(new Date());
		if(cr.getLevel()==0){
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "审核会员等级设定不通过", cr.getUserId());
		}else if(cr.getLevel()>0){
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "审核会员移除不通过", cr.getUserId());
		}
		
		classRequestDao.update(cr);
		return null;
	}
	
	//会员等级信息操作======================
	@Override
	public String editMemberClass(Integer level, String name, Integer value, String font_color, String portrait, HttpServletRequest request, String adminId){
		FDClassSet cs = classSetDao.findOne(" where level="+level);
		if(cs==null){
			if(level>1){
				FDClassSet before = classSetDao.findOne(" where level="+(level-1));
				if(before==null||before.getValue()==null)
					return "请先编辑完善上一级信息";
			}
			cs = new FDClassSet();
			cs.setId(UUID.randomUUID().toString());
			cs.setLevel(level);
			cs.setValue(value);
			cs.setName(name);
			cs.setFont_color(font_color);
			cs.setPortrait(portrait);
			
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "添加会员等级", cs.getName());
			classSetDao.add(cs);
			return Utils.evictFrontCache(request, "evictClassSetCache");
		}else{
			cs.setLevel(level);
			cs.setValue(value);
			cs.setName(name);
			cs.setFont_color(font_color);
			cs.setPortrait(portrait);
			
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "编辑会员等级", cs.getName());
			classSetDao.update(cs);
			return Utils.evictFrontCache(request, "evictClassSetCache");
		}
	}
	
	@Override
	public String delMemberClass(Integer level, HttpServletRequest request, String adminId){
		FDClassSet cs = classSetDao.findOne(" where level="+level+" ");
		if(cs==null)
			return "未找到会员等级信息";
		if(level>1){
			FDClassSet temp = classSetDao.findOne(" where level="+(level+1));
			if(temp!=null)
				return "请先删除后一级会员等级";
		}
		List<FDClassRequest> crList = classRequestDao.findBycondition(" where level="+level+" and status!='"+Status.CLASSREQUEST_STATUS_REJECT+"' ");
		if(crList!=null&&crList.size()>0)
			return "已有添加此会员等级的用户";
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "删除会员等级", cs.getName());
		classSetDao.deleteById(cs.getId());
		return Utils.evictFrontCache(request, "evictClassSetCache");
	}
	
	@Override
	public String getClassList(){
		JSONObject json = conUtil.getRows(" select * from fd_class_set order by level asc ");
		return json.toJSONString();
	}
	
	//用户等级信息操作======================
	@Override
	public String editUserGrade(Integer level, String name, Integer value, String font_color, String portrait, HttpServletRequest request, String adminId){
		FDGradeSet cs = gradeSetDao.findOne(" where level="+level);
		if(cs==null){
			if(level>1){
				FDGradeSet before = gradeSetDao.findOne(" where level="+(level-1));
				if(before==null||before.getValue()==null)
					return "请先编辑完善上一级信息";
			}
			cs = new FDGradeSet();
			cs.setId(UUID.randomUUID().toString());
			cs.setLevel(level);
			cs.setValue(value);
			cs.setName(name);
			cs.setFont_color(font_color);
			cs.setPortrait(portrait);
			
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "添加用户等级", cs.getName());
			gradeSetDao.add(cs);
			return Utils.evictFrontCache(request, "evictGradeSetCache");
		}else{
			cs.setLevel(level);
			cs.setValue(value);
			cs.setName(name);
			cs.setFont_color(font_color);
			cs.setPortrait(portrait);
			
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "编辑用户等级", cs.getName());
			gradeSetDao.update(cs);
			return Utils.evictFrontCache(request, "evictGradeSetCache");
		}
	}
	
	@Override
	public String delUserGrade(Integer level, HttpServletRequest request, String adminId){
		FDGradeSet cs = gradeSetDao.findOne(" where level="+level+" ");
		if(cs==null)
			return "未找到会员等级信息";
		if(level>1){
			FDGradeSet temp = gradeSetDao.findOne(" where level="+(level+1));
			if(temp!=null)
				return "请先删除后一级用户等级";
		}
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_USER, "删除用户等级", cs.getName());
		gradeSetDao.deleteById(cs.getId());
		return Utils.evictFrontCache(request, "evictGradeSetCache");
	}
	
	@Override
	public String getGradeList(){
		JSONObject json = conUtil.getRows(" select * from fd_grade_set order by level asc ");
		return json.toJSONString();
	}
}
