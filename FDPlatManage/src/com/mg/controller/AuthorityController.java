package com.mg.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDAuthorityGroup;
import com.mg.entity.VFDManager;
import com.mg.service.AuthorityService;
import com.mg.service.CommonService;
import com.mg.service.OrderService;
import com.mg.util.Pager;
import com.mg.util.Status;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/authority")
public class AuthorityController {
	@Autowired
	private AuthorityService authorityService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private CommonService commonService;
	
	@ResponseBody
	@RequestMapping("/addAuthorityGroup")
	public String addAuthorityGroup(String groupName, String remark,  String[] menus, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		groupName = Utils.turnCodeWhenGet(groupName, request);
		remark = Utils.turnCodeWhenGet(remark, request);
		if(Utils.isEmpty(groupName))
			return Utils.getErrorStatusJson("权限组名称为空").toJSONString();
		if(Utils.isEmpty(remark))
			remark = "";
		String r = authorityService.addAuthorityGroup(groupName, remark, menus, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("添加成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/editAuthorityGroup")
	public String editAuthorityGroup(String groupId, String groupName, String remark, String[] menus, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		groupName = Utils.turnCodeWhenGet(groupName, request);
		remark = Utils.turnCodeWhenGet(remark, request);
		if(Utils.isEmpty(groupId))
			return Utils.getErrorStatusJson("权限组ID为空").toJSONString();
		if(Utils.isEmpty(groupName))
			return Utils.getErrorStatusJson("权限组名称为空").toJSONString();
		if(Utils.isEmpty(remark))
			remark = "";
		String r = authorityService.editAuthorityGroup(groupId, groupName, remark, menus, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("添加成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/delAuthorityGroup")
	public String delAuthorityGroup(String groupId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(groupId))
			return Utils.getErrorStatusJson("权限组ID为空").toJSONString();
		String r = authorityService.delAuthorityGroup(groupId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("添加成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getAuthorityGroupList")
	public String getAuthorityGroupList(String groupName){
		List<FDAuthorityGroup> agList = authorityService.getAuthorityGroupList(groupName);
		return Utils.getSuccessEntityJson(agList).toJSONString();
	}
	@ResponseBody
	@RequestMapping("/getAuthorityGroupDetail")
	public String getAuthorityGroupDetail(String groupId){
		if(Utils.isEmpty(groupId)){
			return Utils.getErrorStatusJson("权限组ID为空").toJSONString();
		}
		FDAuthorityGroup ag = authorityService.getAuthorityGroupDetail(groupId);
		if(ag==null)
			return Utils.getErrorStatusJson("获取数据失败").toJSONString();
		return Utils.getSuccessEntityJson(ag).toJSONString();
	}
	
	/*@ResponseBody
	@RequestMapping("/addAuthorityRelation")
	public String addAuthorityRelation(String groupId, String[] menus, HttpServletRequest request){
		if(Utils.isEmpty(groupId))
			return Utils.getErrorStatusJson("权限组ID为空").toJSONString();
		if(menus==null||menus.length<1)
			return Utils.getErrorStatusJson("菜单为空").toJSONString();
		String r = authorityService.addAuthorityRelation(groupId, menus);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("添加成功").toJSONString();
	}*/
	
	@ResponseBody
	@RequestMapping("/userList")
	public String userList(String loginName, String phone, Integer status, String startDate, String endDate, Integer page, Integer pageSize, HttpServletRequest request){
		loginName = Utils.turnCodeWhenGet(loginName, request);
		Pager p = authorityService.userList(loginName, phone, status, startDate, endDate, page, pageSize);
		return Utils.getSuccessStatusJson(p).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getManagerList")
	public String getManagerList(String loginName, String phone, Integer managerState, Integer userStatus, String startDate, String endDate, Integer page, Integer pageSize, HttpServletRequest request){
		Pager p = authorityService.getManagerList(loginName, phone, managerState, userStatus, startDate, endDate, page, pageSize);
		return Utils.getSuccessStatusJson(p).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getManagerDetail")
	public String getManagerDetail(String userId){
		if(Utils.isEmpty(userId))
			return Utils.getErrorStatusJson("用户ID为空").toJSONString();
		VFDManager m = authorityService.getManagerDetail(userId);
		if(m==null)
			return Utils.getErrorStatusJson("未找到管理员信息").toJSONString();
		return Utils.getSuccessEntityJson(m).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/addManager")
	public String addManager(String userId, String department, String position, String number, String authorityGroupId,String belongMarketingCenter, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(userId))
			return Utils.getErrorStatusJson("用户ID为空").toJSONString();
		if(Utils.isEmpty(department))
			department = "";
		if(Utils.isEmpty(position)){
			position = "";
		}
		if(Utils.isEmpty(number)){
			number = "";
		}
		if(Utils.isEmpty(authorityGroupId)){
			authorityGroupId = "";
		}
		String r = authorityService.addManager(userId, department, position, number, authorityGroupId, belongMarketingCenter, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("添加成功").toJSONString();
	}
	
	
	@ResponseBody
	@RequestMapping("/editManager")
	public String editManager(String userId, String department, String position, String number, String authorityGroupId, Integer state, String belongMarketingCenter, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(userId))
			return Utils.getErrorStatusJson("用户ID为空").toJSONString();
		if(Utils.isEmpty(department))
			department = "";
		if(Utils.isEmpty(position)){
			position = "";
		}
		if(Utils.isEmpty(number)){
			number = "";
		}
		if(Utils.isEmpty(authorityGroupId)){
			authorityGroupId = "";
		}
		if(state==null){
			state = Status.MANANGER_STATE_NORMAL;
		}
		String r = authorityService.editManager(userId, department, position, number, authorityGroupId, state, belongMarketingCenter, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("编辑成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/delManager")
	public String delManager(String userId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(userId))
			return Utils.getErrorStatusJson("用户ID为空").toJSONString();
		String r = authorityService.delManager(userId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("删除成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/lockManager")
	public String lockManager(String userId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(userId))
			return Utils.getErrorStatusJson("用户ID为空").toJSONString();
		String r = authorityService.lockManager(userId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("禁用账号成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/unlockManager")
	public String unlockManager(String userId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(userId))
			return Utils.getErrorStatusJson("用户ID为空").toJSONString();
		String r = authorityService.unlockManager(userId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("解锁账号成功").toJSONString();
	}

	@ResponseBody
	@RequestMapping("/checkOperateLog")
	public String checkOperateLog(String loginStr, String operateType, String startDate, String endDate, Integer page, Integer pageSize, HttpServletRequest request){
		loginStr = Utils.turnCodeWhenGet(loginStr, request);
		return authorityService.checkOperateLog(loginStr, operateType, startDate, endDate, page, pageSize);
	}
}
