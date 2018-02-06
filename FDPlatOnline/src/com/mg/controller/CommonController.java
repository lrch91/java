package com.mg.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mg.entity.FDArea;
import com.mg.entity.FDUser;
import com.mg.service.CommonService;
import com.mg.service.ParamService;
import com.mg.service.PublicService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;
import com.mg.vo.UserInfoVo;


@Controller
public class CommonController {

	@Autowired
	private CommonService commonService;
	@Autowired
	private ParamService paramService;
	@Autowired
	private PublicService publicService;
	
	@ResponseBody
	@RequestMapping(value="/reigster")
	public String reigster(String msgCode, String phone, String loginName, String password, String city, String area, String street, HttpServletRequest request, HttpServletResponse response){
//		loginName = Utils.turnCodeWhenGet(loginName, request);
		if(Utils.isEmpty(msgCode))
			return Utils.getErrorStatusJson("请输入短信验证码").toJSONString();
		if(Utils.isEmpty(phone))
			return Utils.getErrorStatusJson("请输入手机号").toJSONString();
//		if(Utils.isEmpty(loginName))
//			return Utils.getErrorStatusJson("请输入用户名").toJSONString();
//		if(!loginName.equals(loginName.trim()))
//			return Utils.getErrorStatusJson("用户名不能包含空格").toJSONString();
		if(Utils.isEmpty(password))
			return Utils.getErrorStatusJson("请输入密码").toJSONString();
		if(!password.trim().equals(password)){
			return "密码不能包含空格";
		}
		if(Utils.isEmpty(city)||Utils.isEmpty(area)||Utils.isEmpty(street))
			return Utils.getErrorStatusJson("请完善地址").toJSONString();
		String r = commonService.register(msgCode, phone, password, city, area, street, request, response);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("注册成功").toJSONString();
	}
	
	
	@ResponseBody
	@RequestMapping(value="/login")
	public String userLogin(String loginStr,String password,String imgCode, String ip, HttpServletRequest request, HttpServletResponse response){
		loginStr = Utils.turnCodeWhenGet(loginStr, request);
		if(Utils.isEmpty(loginStr))
			return Utils.getErrorStatusJson("请输入用户名或手机号").toJSONString();
		if(Utils.isEmpty(password))
			return Utils.getErrorStatusJson("请输入登录密码").toJSONString();
		/*if(Utils.isEmpty(imgCode))
			return Utils.getErrorStatusJson("请输入图形验证码").toJSONString();*/
		if(Utils.isEmpty(ip))
			return Utils.getErrorStatusJson("获取IP地址失败").toJSONString();
		String r = commonService.login(loginStr,password, imgCode, ip, request, response);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("登录成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/logout")
	public String userLogout(HttpServletRequest request,HttpServletResponse response){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		String r = commonService.logout(request, response);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("退出成功").toJSONString();
	}
	
	@RequestMapping("/imgcode")
	public void getImgCode(HttpServletRequest request, HttpServletResponse response){
		commonService.getImgCode(request, response);
	}
	
	@ResponseBody
	@RequestMapping("/messageCode")
	public String getMessageCode(String phone,String imgCode,HttpServletRequest request, HttpServletResponse response){
		if(Utils.isEmpty(phone))
			return Utils.getErrorStatusJson("请输入手机号").toJSONString();
		if(Utils.isEmpty(imgCode))
			return Utils.getErrorStatusJson("请输入图形验证码").toJSONString();
		String r = commonService.getMessageCode( phone, imgCode, request,  response);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("发送短信验证码成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getqiniutoken")
	public String getQiNiuToken(){
		JSONObject json = new JSONObject();
		json.put("status", "success");
		json.put("msg", "获取成功");
		json.put("token",Utils.getqiniutoken());
		return json.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/bindPhone")
	public String bindPhone(String msgCode,HttpServletRequest request,HttpServletResponse response){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		if(Utils.isEmpty(msgCode))
			return Utils.getErrorStatusJson("请输入短信验证码").toJSONString();
		String r = commonService.bindPhone(msgCode, request, response);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("绑定手机成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updatePwdByPhone")
	public String updatePwdByPhone(String msgCode,String phone,String password, HttpServletRequest request,HttpServletResponse response){
		if(Utils.isEmpty(msgCode))
			return Utils.getErrorStatusJson("请输入短信验证码").toJSONString();
		if(Utils.isEmpty(phone))
			return Utils.getErrorStatusJson("请输入手机号").toJSONString();
		if(Utils.isEmpty(password))
			return Utils.getErrorStatusJson("请输入新密码").toJSONString();
		String r = commonService.updatePwdByPhone(msgCode,phone, password, request, response);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改密码成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updatePaypwdByPhone")
	public String updatePaypwdByPhone(String msgCode,String phone,String paypwd, HttpServletRequest request,HttpServletResponse response){
		if(Utils.isEmpty(msgCode))
			return Utils.getErrorStatusJson("请输入短信验证码").toJSONString();
		if(Utils.isEmpty(phone))
			return Utils.getErrorStatusJson("请输入手机号").toJSONString();
		if(Utils.isEmpty(paypwd))
			return Utils.getErrorStatusJson("请输入新支付密码").toJSONString();
		String r = commonService.updatePaypwdByPhone(msgCode,phone, paypwd, request, response);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改支付密码成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updatePwdByPwd")
	public String updatePwdByPwd(String oldpwd,String newpwd,HttpServletRequest request){
		if(Utils.isEmpty(oldpwd))
			return "请输入旧密码";
		if(Utils.isEmpty(newpwd))
			return "请输入新密码";
		String r = commonService.updatePwdByPwd(oldpwd, newpwd, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改密码成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updateUserName")
	public String updateUserName(HttpServletRequest request,String userName){
		userName = Utils.turnCodeWhenGet(userName, request);
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		if(Utils.isEmpty(userName))
			return "用户名为空";
		if(!userName.equals(userName.trim()))
			return "用户名不要加空格";
		String r = commonService.updateUserName(request, userName);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改用户名成功").toJSONString();
	}
	
	
	@ResponseBody
	@RequestMapping("/findUserInfo")
	public String findUserInfo(HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		UserInfoVo ui = commonService.findUserInfo(request, publicService.getClassSet(), publicService.getGradeSet());
		if(ui==null)
			return Utils.getErrorStatusJson("获取信息失败").toJSONString();
		return Utils.getSuccessEntityJson(ui).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updateUserInfo")
	public String updateUserInfo(FDUser u, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		u.setId(ua.getId());
		String r = commonService.updateUserInfo(u, paramService.getParamCache());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("修改信息成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updatePhoneCheckImgCode")
	public String updatemobilestemcode(HttpServletRequest request,HttpServletResponse response,String imgCode){
		if(Utils.isEmpty(imgCode))
			return Utils.getErrorStatusJson("请输入图片验证码").toJSONString();
		String r = commonService.updatemobilestemcode(request,response,imgCode);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("获取短信验证码成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updatePhoneStepOne")
	public String updateMobileStepOne(HttpServletRequest request,HttpServletResponse response,String msgCode){
		if(Utils.isEmpty(msgCode))
			return Utils.getErrorStatusJson("请输入短信验证码").toJSONString();
		String r = commonService.updateMobileStepOne(request,response,msgCode);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("操作成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updatePhoneSteptTwo")
	public String updateMobileStepTwo(HttpServletRequest request,HttpServletResponse response,String msgCode,String newPhone){
		if(Utils.isEmpty(msgCode))
			return Utils.getErrorStatusJson("请输入短信验证码").toJSONString();
		if(Utils.isEmpty(newPhone))
			return Utils.getErrorStatusJson("请输入新手机号").toJSONString();
		String r = commonService.updateMobileStepTwo(request,response,msgCode,newPhone);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("手机号修改成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/userAuth")
	public String userAuth(HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("信息获取失败").toJSONString();
		return Utils.getSuccessEntityJson(ua).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getArea")
	public String getArea(HttpServletRequest request){
		ArrayList<FDArea> aList = commonService.getArea();
		return Utils.getSuccessEntityJson(aList).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getLoginNameByPhones")
	public String getLoginNameByPhones(HttpServletRequest request, @RequestParam(value = "phones[]") String[] phones){
		if(phones==null||phones.length==0)
			return Utils.getErrorStatusJson("手机号为空").toJSONString();
		HashMap<String, HashMap<String, String>> map = (HashMap<String, HashMap<String, String>>)commonService.getLoginNameByPhones(phones, publicService.getClassSet());
		return Utils.getSuccessEntityJson(map).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getMobNameByAccounts")
	public String getMobNameByAccounts(HttpServletRequest request, @RequestParam(value = "accounts[]") String[] accounts){
		if(accounts==null||accounts.length==0)
			return Utils.getErrorStatusJson("客服账号为空").toJSONString();
		HashMap<String, String> map = (HashMap<String, String>) commonService.getMobNameByAccounts(accounts);
		return Utils.getSuccessEntityJson(map).toJSONString();
	}
	
}
