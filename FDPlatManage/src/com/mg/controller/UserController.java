package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.service.CommonService;
import com.mg.service.UserService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;
import com.mg.vo.UserInfoVo;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	CommonService commonService;
	
	/**
	 * TODO 管理员后台用户列表
	 * 2016年7月28日
	 * @param loginName
	 * @param phone
	 * @param status
	 * @param startDate
	 * @param endDate
	 * @param page
	 * @param pageSize
	 * @return
	 * Pager
	 */
	@ResponseBody
	@RequestMapping("/userList")
	public String userList(String loginName, String phone, Integer memberLevel, Integer status, String startDate, String endDate, Integer page, Integer pageSize, HttpServletRequest request){
		loginName = Utils.turnCodeWhenGet(loginName, request);
		return userService.userList(loginName, phone, memberLevel, status, startDate, endDate, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/userDetail")
	public String userDetail(String userId){
		UserInfoVo ui = userService.userDetail(userId);
		if(ui==null)
			return Utils.getErrorStatusJson("获取用户信息失败").toJSONString();
		return Utils.getSuccessEntityJson(ui).toJSONString();
				
	}
	
	/**
	 * TODO  注册审核操作，暂不需要
	 * 2016年7月29日
	 * @param userId
	 * @return
	 * String
	 */
/*	@ResponseBody
	@RequestMapping("/verifyUserApply")
	public String verifyUserApply(String userId){
		String r = userService.verifyUserApply(userId);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("审核成功").toJSONString();
	}*/
	
	/**
	 * TODO 禁用用户账号
	 * 2016年7月28日
	 * @param userId
	 * @return
	 * String
	 */
	@ResponseBody
	@RequestMapping("/lockUser")
	public String lockUser(String userId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		String r = userService.lockUser(userId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("禁用账号成功").toJSONString();
	}
	
	/**
	 * TODO 解锁用户账号
	 * 2016年7月28日
	 * @param userId
	 * @return
	 * String
	 */
	@ResponseBody
	@RequestMapping("/unlockUser")
	public String unlockUser(String userId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		String r = userService.unlockUser(userId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("解锁账号成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/memberRequestList")
	public String memberRequestList(String loginName, String phone, Integer page, Integer pageSize, HttpServletRequest request){
		loginName = Utils.turnCodeWhenGet(loginName, request);
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return userService.memberRequestList(loginName, phone, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/requestMemberLevel")
	public String requestMemberLevel(String userId, Integer level, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		if(level==null||level<1)
			return Utils.getErrorStatusJson("设置等级为空").toJSONString();
		String r = userService.requestMemberLevel(userId, level, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("设定会员等级申请成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/requestMemberOut")
	public String requestMemberOut(String userId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		String r = userService.requestMemberOut(userId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("移出会员申请成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/passLevelRequest")
	public String passLevelRequest(String requestId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(requestId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = userService.passLevelRequest(requestId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("通过申请成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/rejectLevelRequest")
	public String rejectLevelRequest(String requestId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(requestId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = userService.rejectLevelRequest(requestId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("拒绝申请成功").toJSONString();
	}
	
	//会员等级信息操作======================
	@ResponseBody
	@RequestMapping("/editMemberClass")
	public String editMemberClass(Integer level, String name, Integer value, String font_color, String portrait, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(level==null||level<1)
			Utils.getErrorStatusJson("排序值过小").toJSONString();
		if(value==null||value<1)
			Utils.getErrorStatusJson("成长值过小").toJSONString();
		if(Utils.isEmpty(name))
			Utils.getErrorStatusJson("等级名称为空").toJSONString();
		if(Utils.isEmpty(font_color))
			Utils.getErrorStatusJson("字体颜色为空").toJSONString();
		if(Utils.isEmpty(portrait))
			Utils.getErrorStatusJson("图标为空").toJSONString();
		name = Utils.turnCodeWhenGet(name, request);
		font_color = Utils.turnCodeWhenGet(font_color, request);
		portrait = Utils.turnCodeWhenGet(portrait, request);
		String r = userService.editMemberClass(level, name, value, font_color, portrait, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑会员等级信息成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/delMemberClass")
	public String delMemberClass(Integer level, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(level==null||level<1)
			Utils.getErrorStatusJson("排序值过小").toJSONString();
		String r = userService.delMemberClass(level, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除会员等级成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getClassList")
	public String getClassList(){
		return userService.getClassList();
	}
	
	//用户等级信息操作======================
	@ResponseBody
	@RequestMapping("/editUserGrade")
	public String editUserGrade(Integer level, String name, Integer value, String font_color, String portrait, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(level==null||level<1)
			Utils.getErrorStatusJson("排序值过小").toJSONString();
		if(value==null||value<1)
			Utils.getErrorStatusJson("成长值过小").toJSONString();
		if(Utils.isEmpty(name))
			Utils.getErrorStatusJson("等级名称为空").toJSONString();
		if(Utils.isEmpty(font_color))
			Utils.getErrorStatusJson("字体颜色为空").toJSONString();
		if(Utils.isEmpty(portrait))
			Utils.getErrorStatusJson("图标为空").toJSONString();
		name = Utils.turnCodeWhenGet(name, request);
		font_color = Utils.turnCodeWhenGet(font_color, request);
		portrait = Utils.turnCodeWhenGet(portrait, request);
		String r = userService.editUserGrade(level, name, value, font_color, portrait, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑用户等级信息成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/delUserGrade")
	public String delUserGrade(Integer level, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(level==null||level<1)
			Utils.getErrorStatusJson("排序值过小").toJSONString();
		String r = userService.delUserGrade(level, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除用户等级成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getGradeList")
	public String getGradeList(){
		return userService.getGradeList();
	}
}
