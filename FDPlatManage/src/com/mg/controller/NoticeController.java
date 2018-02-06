package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDNotice;
import com.mg.service.CommonService;
import com.mg.service.OperationService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;


@Controller
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private OperationService operationService;
	@Autowired
	private CommonService commonService;
	
	@ResponseBody
	@RequestMapping(value="/addSystemNotice")
	public String addSystemNotice(String title, String content, String endDate, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(title))
			return Utils.getErrorStatusJson("通知标题为空").toJSONString();
		if(Utils.isEmpty(content))
			return Utils.getErrorStatusJson("通知内容为空").toJSONString();
		title = Utils.turnCodeWhenGet(title, request);
		content = Utils.turnCodeWhenGet(content, request);
		String r=operationService.addSystemNotice(title, content, endDate, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("添加成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/systemNoticeDetail")
	public String systemNoticeDetail(String id,HttpServletRequest request){
		if(Utils.isEmpty(id))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		FDNotice notice = operationService.systemNoticeDetail(id);
		if(notice==null)
			return Utils.getErrorStatusJson("获取信息失败").toJSONString();
		return Utils.getSuccessEntityJson(notice).toJSONString();
	}
	
	/*@ResponseBody
	@RequestMapping(value="/editSystemNotice")
	public String editSystemNotice(String id, String title, String content, String endDate, HttpServletRequest request){
		if(Utils.isEmpty(id))
			return Utils.getErrorStatusJson("通知ID为空").toJSONString();
		if(Utils.isEmpty(title))
			return Utils.getErrorStatusJson("通知标题为空").toJSONString();
		if(Utils.isEmpty(content))
			return Utils.getErrorStatusJson("通知内容为空").toJSONString();
		title = Utils.turnCodeWhenGet(title, request);
		content = Utils.turnCodeWhenGet(content, request);
		String r=operationService.editSystemNotice(id, title, content, endDate);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑成功").toJSONString();
	}*/
	
	/*@ResponseBody
	@RequestMapping(value="/delSystemNotice")
	public String delSystemNotice(String id, HttpServletRequest request){
		if(Utils.isEmpty(id))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = operationService.delSystemNotice(id);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除成功").toJSONString();
	}*/
	
	@ResponseBody
	@RequestMapping(value="/getSystemNoticeList")
	public String getSystemNoticeList(Integer page, Integer pageSize){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return operationService.getSystemNoticeList(page, pageSize);
	}
	
}
