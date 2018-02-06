package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDAdv;
import com.mg.service.CommonService;
import com.mg.service.OperationService;
import com.mg.util.Utils;


@Controller
@RequestMapping("/adv")
public class AdvController {

	@Autowired
	private OperationService operationService;
	@Autowired
	private CommonService commonService;
	
	@ResponseBody
	@RequestMapping(value="/addAdv")
	public String addAdv(FDAdv adv, HttpServletRequest request){
		if(!Utils.isEmpty(adv.getTitle())){
			adv.setTitle(Utils.turnCodeWhenGet(adv.getTitle(), request));
		}
		if(!Utils.isEmpty(adv.getLink())){
			adv.setLink(Utils.turnCodeWhenGet(adv.getLink(), request));
		}
		String r = operationService.addAdv(adv, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("添加广告成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/advDetail")
	public String advDetail(String advId,HttpServletRequest request){
		if(Utils.isEmpty(advId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		FDAdv adv = operationService.advDetail(advId);
		if(adv==null)
			return Utils.getErrorStatusJson("获取信息失败").toJSONString();
		return Utils.getSuccessEntityJson(adv).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/editAdv")
	public String editAdv(FDAdv adv, HttpServletRequest request){
		if(Utils.isEmpty(adv.getId()))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		if(!Utils.isEmpty(adv.getTitle())){
			adv.setTitle(Utils.turnCodeWhenGet(adv.getTitle(), request));
		}
		if(!Utils.isEmpty(adv.getLink())){
			adv.setLink(Utils.turnCodeWhenGet(adv.getLink(), request));
		}
		String r = operationService.editAdv(adv, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑广告成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getAdvList")
	public String getAdvList(String type, String status, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return operationService.getAdvList(type, status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/delAdv")
	public String delAdv(String advId, HttpServletRequest request){
		if(Utils.isEmpty(advId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = operationService.delAdv(advId, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除广告成功").toJSONString();
	}
	
//	@ResponseBody
//	@RequestMapping(value="/setAdvTop")
//	public String setAdvTop(String advId, HttpServletRequest request){
//		if(Utils.isEmpty(advId))
//			return Utils.getErrorStatusJson("ID为空").toJSONString();
//		String r = operationService.setAdvTop(advId, request);
//		if(r!=null)
//			return Utils.getErrorStatusJson(r).toJSONString();
//		return Utils.getSuccessStatusJson("广告置顶成功").toJSONString();
//	}
}
