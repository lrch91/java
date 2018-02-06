package com.mg.service;

import javax.servlet.http.HttpServletRequest;

import com.mg.vo.UserInfoVo;


public interface UserService {
	String userList(String loginName, String phone, Integer memberLevel, Integer status, String startDate, String endDate, Integer page, Integer pageSize);
	UserInfoVo userDetail(String userId);
	String verifyUserApply(String userId);
	String lockUser(String userId, String adminId);
	String unlockUser(String userId, String adminId);
	String memberRequestList(String loginName, String phone, Integer page, Integer pageSize);
	String requestMemberLevel(String userId, Integer level, String adminId);
	String requestMemberOut(String userId, String adminId);
	String passLevelRequest(String requestId, String adminId);
	String rejectLevelRequest(String requestId, String adminId);
	String editMemberClass(Integer level, String name, Integer value, String font_color, String portrait, HttpServletRequest request, String adminId);
	String delMemberClass(Integer level, HttpServletRequest request, String adminId);
	String getClassList();
	String editUserGrade(Integer level, String name, Integer value, String font_color, String portrait, HttpServletRequest request, String adminId);
	String delUserGrade(Integer level, HttpServletRequest request, String adminId);
	String getGradeList();
	
}
