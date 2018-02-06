package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDApplyFertilize;
import com.mg.entity.FDApplySoiltest;
import com.mg.entity.FDFertilizeEvaluate;
import com.mg.entity.FDSoiltestEvaluate;
import com.mg.service.CommonService;
import com.mg.service.ParamService;
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
	@Autowired
	private ParamService paramService;
	
	@ResponseBody
	@RequestMapping(value="/applyFertilize")
	public String applyFertilize(FDApplyFertilize af,HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		af.setUserId(ua.getId());
		String r = serveService.applyFertilize(af, paramService.getParamCache());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("申请成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/applyFertilizeDetail")
	public String applyFertilizeDetail(String applyFertilizeId,HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return serveService.applyFertilizeDetail(ua.getId(), applyFertilizeId);
	}
	
	@ResponseBody
	@RequestMapping(value="/applyFertilizeList")
	public String applyFertilizeList(String status, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return serveService.applyFertilizeList(ua.getId(), status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/addFertilizeEvaluate")
	public String addFertilizeEvaluate(FDFertilizeEvaluate fe, HttpServletRequest request){
		if(Utils.isEmpty(fe.getApplyFertilizeId()))
			return Utils.getErrorStatusJson("请提交配肥申请ID").toJSONString();
		if(Utils.isEmpty(fe.getContent()))
			return Utils.getErrorStatusJson("请填写评价内容").toJSONString();
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		fe.setUserId(ua.getId());
		String r = serveService.addFertilizeEvaluate(fe);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("评价成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/applySoiltest")
	public String applySoiltest(FDApplySoiltest as,HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		as.setUserId(ua.getId());
		String r = serveService.applySoiltest(as, paramService.getParamCache());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("申请成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/applySoiltestDetail")
	public String applySoiltestDetail(String applySoiltestId,HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return serveService.applySoiltestDetail(ua.getId(), applySoiltestId);
	}
	
	@ResponseBody
	@RequestMapping(value="/applySoiltestList")
	public String applySoiltestList(String status, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return serveService.applySoiltestList(ua.getId(), status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/addSoiltestEvaluate")
	public String addSoiltestEvaluate(FDSoiltestEvaluate se, HttpServletRequest request){
		if(Utils.isEmpty(se.getApplySoiltestId()))
			return Utils.getErrorStatusJson("请提交测土申请ID").toJSONString();
		if(Utils.isEmpty(se.getContent()))
			return Utils.getErrorStatusJson("请填写评价内容").toJSONString();
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		se.setUserId(ua.getId());
		String r = serveService.addSoiltestEvaluate(se);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("评价成功").toJSONString();
	}
	
	
}
