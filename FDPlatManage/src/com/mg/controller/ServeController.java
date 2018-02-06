package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDFertilizeEvaluate;
import com.mg.entity.FDSoiltestEvaluate;
import com.mg.entity.FDSystemParam;
import com.mg.service.CommonService;
import com.mg.service.ServeService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;


@Controller
@RequestMapping("/serve")
public class ServeController {

	@Autowired
	private ServeService serveService;
	@Autowired
	private CommonService commonService;
	
	@ResponseBody
	@RequestMapping(value="/handleFertilize")
	public String handleFertilize(String applyFertilizeId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		String r = serveService.handleFertilize(applyFertilizeId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("受理成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/applyFertilizeDetail")
	public String applyFertilizeDetail(String applyFertilizeId,HttpServletRequest request){
		return serveService.applyFertilizeDetail(applyFertilizeId);
	}
	
	@ResponseBody
	@RequestMapping(value="/applyFertilizeList")
	public String applyFertilizeList(String nameTel, String applier, String fertilizeCategory, String status, Integer page, Integer pageSize, HttpServletRequest request){
		nameTel = Utils.turnCodeWhenGet(nameTel, request);
		applier = Utils.turnCodeWhenGet(applier, request);
		fertilizeCategory = Utils.turnCodeWhenGet(fertilizeCategory, request);
		return serveService.applyFertilizeList(nameTel, applier, fertilizeCategory,  status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/addFertilizeReply")
	public String addFertilizeReply(String evaluateId, FDFertilizeEvaluate fe, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(evaluateId))
			return Utils.getErrorStatusJson("请提交评论ID").toJSONString();
		if(Utils.isEmpty(fe.getContent()))
			return Utils.getErrorStatusJson("请填写回复内容").toJSONString();
		fe.setContent(Utils.turnCodeWhenGet(fe.getContent(), request));
		String r = serveService.addFertilizeReply(evaluateId, fe, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("评价成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/delFertilizeReply")
	public String delFertilizeReply(String evaluateId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(evaluateId))
			return Utils.getErrorStatusJson("请提交评论ID").toJSONString();
		String r = serveService.delFertilizeReply(evaluateId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除评论成功").toJSONString();
	}
	
	//==================================================
	
	@ResponseBody
	@RequestMapping(value="/handleSoiltest")
	public String handleSoiltest(String applySoiltestId, String name,HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		name = Utils.turnCodeWhenGet(name, request);
		String r = serveService.handleSoiltest(applySoiltestId, name, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("受理成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/applySoiltestDetail")
	public String applySoiltestDetail(String applySoiltestId,HttpServletRequest request){
		return serveService.applySoiltestDetail(applySoiltestId);
	}
	
	@ResponseBody
	@RequestMapping(value="/applySoiltestList")
	public String applySoiltestList(String nameTel, String status, String applier, String handler, Integer page, Integer pageSize, HttpServletRequest request){
		nameTel = Utils.turnCodeWhenGet(nameTel, request);
		applier = Utils.turnCodeWhenGet(applier, request);
		handler = Utils.turnCodeWhenGet(handler, request);
		return serveService.applySoiltestList(nameTel, applier, handler, status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/addSoiltestReply")
	public String addSoiltestReply(String evaluateId, FDSoiltestEvaluate se, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(evaluateId))
			return Utils.getErrorStatusJson("请提交评论ID").toJSONString();
		if(Utils.isEmpty(se.getContent()))
			return Utils.getErrorStatusJson("请填写回复内容").toJSONString();
		se.setContent(Utils.turnCodeWhenGet(se.getContent(), request));
		String r = serveService.addSoiltestReply(evaluateId, se, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("评价成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/delSoiltestReply")
	public String delSoiltestReply(String evaluateId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(evaluateId))
			return Utils.getErrorStatusJson("请提交评论ID").toJSONString();
		String r = serveService.delSoiltestReply(evaluateId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除评论成功").toJSONString();
	}
	
	//测土配肥最低值====================================================================
	@ResponseBody
	@RequestMapping("/getSystemParam")
	public String getSystemParam(HttpServletRequest request){
		FDSystemParam sp = serveService.getSystemParam();
		if(sp==null)
			return Utils.getErrorStatusJson("获取系统参数出错").toJSONString();
		return Utils.getSuccessEntityJson(sp).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updateMinFSValue")
	public String updateMinFSValue(Double minFertilizeNum, Double minSoiltestAcreage, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(minFertilizeNum==null||minFertilizeNum<0)
			return Utils.getErrorStatusJson("配肥最低值为空").toJSONString();
		if(minSoiltestAcreage==null||minSoiltestAcreage<0)
			return Utils.getErrorStatusJson("测土最低值为空").toJSONString();
		String r = serveService.updateMinFSValue(minFertilizeNum, minSoiltestAcreage, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改成功").toJSONString();
	}
}
