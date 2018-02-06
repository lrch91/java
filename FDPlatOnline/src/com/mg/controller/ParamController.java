package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDSystemParam;
import com.mg.service.CommonService;
import com.mg.service.ParamService;
import com.mg.util.Utils;

@Controller
@RequestMapping("/param")
public class ParamController {
	@Autowired
	CommonService commonService;
	@Autowired
	ParamService paramService;
	
	@ResponseBody
	@RequestMapping("/getParamCache")
	public String getParamCache(HttpServletRequest request){
		/*UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();*/
		FDSystemParam sp = paramService.getParamCache();
		if(sp==null)
			return Utils.getErrorStatusJson("获取系统参数出错").toJSONString();
		return Utils.getSuccessEntityJson(sp).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/evictParamCache")
	public String evictParamCache(HttpServletRequest request){
		String r = paramService.evictParamCache();
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("清除缓存成功").toJSONString();
	}
	
}
