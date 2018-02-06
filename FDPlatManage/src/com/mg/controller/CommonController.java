package com.mg.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDArea;
import com.mg.service.CommonService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;
import com.mg.vo.UserInfoVo;


@Controller
@RequestMapping("/common")
public class CommonController {

	@Autowired
	private CommonService commonService;
	
	@ResponseBody
	@RequestMapping(value="/login")
	public String userLogin(String loginStr,String password,String imgCode, String ip, HttpServletRequest request, HttpServletResponse response){
		System.out.println("login-start-----------");
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
	
	@ResponseBody
	@RequestMapping("/findUserInfo")
	public String findUserInfo(HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		if(!"YES".equals(ua.getManager())){
			return Utils.getErrorStatusJson("unManagerLogin").toJSONString();
		}
		UserInfoVo ui = commonService.findUserInfo(request);
		if(ui==null)
			return Utils.getErrorStatusJson("获取信息失败").toJSONString();
		return Utils.getSuccessEntityJson(ui).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getArea")
	public String getArea(HttpServletRequest request){
		ArrayList<FDArea> aList = commonService.getArea();
		return Utils.getSuccessEntityJson(aList).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/test")
	public String test(){
//		try{
			String r = commonService.test();
			if(r!=null)
				return Utils.getErrorStatusJson(r).toJSONString();
			return Utils.getSuccessStatusJson("成功").toJSONString();
//		}catch(Exception e){
//			e.printStackTrace();
//			return Utils.getErrorStatusJson("系统异常").toJSONString();
//		}
		
	}
}
