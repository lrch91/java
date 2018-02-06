package com.mg.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mg.entity.FDArea;
import com.mg.entity.FDSystemParam;
import com.mg.entity.FDUser;
import com.mg.vo.UserAuthVo;
import com.mg.vo.UserInfoVo;


public interface CommonService {
	String register(String msgCode,String phone,String password, String city, String area, String street, HttpServletRequest request, HttpServletResponse response);
	String login(String loginStr, String password, String imgCode, String ip, HttpServletRequest request, HttpServletResponse response);
	String logout(HttpServletRequest request,HttpServletResponse response);
	void getImgCode(HttpServletRequest request, HttpServletResponse response);
	String getMessageCode(String phone,String imgCode,HttpServletRequest request, HttpServletResponse response);
	String bindPhone(String msgCode,HttpServletRequest request,HttpServletResponse response);
	String updatePwdByPhone(String msgCode,String phone,String password, HttpServletRequest request,HttpServletResponse response);
	String updatePaypwdByPhone(String msgCode,String phone,String paypwd, HttpServletRequest request,HttpServletResponse response);
	String updatePwdByPwd(String oldpwd, String newpwd, HttpServletRequest request);
	String updateUserName(HttpServletRequest request,String userName);
	UserInfoVo findUserInfo(HttpServletRequest request, List<Map<String, Object>> csList, List<Map<String, Object>> gsList);
	String updateUserInfo(FDUser u, FDSystemParam sp);
	String updatemobilestemcode(HttpServletRequest request, HttpServletResponse response,String imgCode);
	String updateMobileStepOne(HttpServletRequest request, HttpServletResponse response,String msgCode);
	String updateMobileStepTwo(HttpServletRequest request, HttpServletResponse response,String msgCode,String newPhone);
	UserAuthVo userAuth(HttpServletRequest request);
	ArrayList<FDArea> getArea();
	HashMap<String, HashMap<String, String>> getLoginNameByPhones(String[] phones, List<Map<String, Object>> csList);
	HashMap<String, String> getMobNameByAccounts(String[] accounts);
}
