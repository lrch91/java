package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDSystemParam;
import com.mg.service.CommonService;
import com.mg.service.TradeService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/trade")
public class TradeController {
	@Autowired
	private TradeService tradeService;
	@Autowired
	private CommonService commonService;
	
	//积分 猫币列表
	@ResponseBody
	@RequestMapping("/getIntegralList")
	public String getIntegralList(String nameTel, String changeType, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		if(!Utils.isEmpty(nameTel))
			nameTel = Utils.turnCodeWhenGet(nameTel, request);
		return tradeService.getIntegralList(nameTel, changeType, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/getCatcoinList")
	public String getCatcoinList(String nameTel, String changeType, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		if(!Utils.isEmpty(nameTel))
			nameTel = Utils.turnCodeWhenGet(nameTel, request);
		return tradeService.getCatcoinList(nameTel, changeType, page, pageSize);
	}
	
	//========================
	
	@ResponseBody
	@RequestMapping("/getSystemParam")
	public String getSystemParam(HttpServletRequest request){
		FDSystemParam sp = tradeService.getSystemParam();
		if(sp==null)
			return Utils.getErrorStatusJson("获取系统参数出错").toJSONString();
		return Utils.getSuccessEntityJson(sp).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/updateParam")
	public String updateParam(FDSystemParam sp, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(sp.getCompleteCrop()==null||sp.getCompleteDetailAddress()==null||sp.getCompleteGender()==null||sp.getCompleteName()==null||sp.getConsumeIntegralRate()==null||sp.getIntegralToCatcoinRate()==null||sp.getRechargeCatcoinRate()==null||sp.getSignIn()==null)
			return Utils.getErrorStatusJson("提交信息不完整").toJSONString();
		String r = tradeService.updateParam(sp, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改成功").toJSONString();
	}
	
}
