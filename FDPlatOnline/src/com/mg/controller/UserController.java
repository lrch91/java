package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.mg.service.CommonService;
import com.mg.service.ParamService;
import com.mg.service.UserService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;


@Controller
@RequestMapping(value="/user")
public class UserController {

	@Autowired
	private CommonService commonService;
	@Autowired
	private UserService userService;
	@Autowired
	private ParamService paramService;
	
	@ResponseBody
	@RequestMapping(value="/signIn")
	public String signIn(HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return userService.signIn(ua.getId(), paramService.getParamCache());
	}
	
	@ResponseBody
	@RequestMapping(value="/integralToCatcoinInfo")
	public String integralToCatcoinInfo(HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		JSONObject json = userService.integralToCatcoinInfo(ua.getId(), paramService.getParamCache());
		return json.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/integralToCatcoin")
	public String integralToCatcoin(Integer num, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		String r = userService.integralToCatcoin(ua.getId(), num, paramService.getParamCache());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("签到成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/donateCatcoin")
	public String donateCatcoin(String accepter, Integer num, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		String r = userService.donateCatcoin(ua.getId(), accepter, num);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("转赠成功").toJSONString();
	}
	
}
