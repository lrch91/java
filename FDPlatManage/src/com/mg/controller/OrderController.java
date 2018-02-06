package com.mg.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDMarketingCenter;
import com.mg.service.CommonService;
import com.mg.service.OrderService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	CommonService commonService;
	@Autowired
	OrderService orderService;
	
	@ResponseBody
	@RequestMapping("/getOrderList")
	public String getOrderList(String nameTel, String orderNumber, String status, String startDate, String endDate, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		if(!Utils.isEmpty(nameTel))
			nameTel = Utils.turnCodeWhenGet(nameTel, request);
		UserAuthVo ua = commonService.userAuth(request);
		return orderService.getOrderList(nameTel, orderNumber, status, startDate, endDate, page, pageSize, ua.getId());
	}
	
	@ResponseBody
	@RequestMapping("/getOrderDetalil")
	public String getOrderDetalil(String orderId){
		if(Utils.isEmpty(orderId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		return orderService.getOrderDetalil(orderId);
	}
	
	@ResponseBody
	@RequestMapping("/changeOrderPrice")
	public String changeOrderPrice(String orderId, Double newPrice, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(orderId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		if(newPrice==null||newPrice<0.00000001)
			return Utils.getErrorStatusJson("请重新输入改价价格").toJSONString();
		String r = orderService.changeOrderPrice(orderId, newPrice, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("改价成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/acceptOrder")
	public String acceptOrder(String orderId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(orderId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = orderService.acceptOrder(orderId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("受理成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/cancelOrder")
	public String cancelOrder(String orderId, String remark, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(orderId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		if(Utils.isEmpty(remark))
			return Utils.getErrorStatusJson("订单取消原因为空").toJSONString();
		String r = orderService.cancelOrder(orderId, remark, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("取消成功").toJSONString();
	}
	
//	@ResponseBody
//	@RequestMapping("/delOrder")
//	public String delOrder(String orderId){
//		if(Utils.isEmpty(orderId))
//			return Utils.getErrorStatusJson("ID为空").toJSONString();
//		String r = orderService.delOrder(orderId);
//		if(r!=null)
//			return Utils.getErrorStatusJson(r).toJSONString();
//		return Utils.getSuccessStatusJson("删除成功").toJSONString();
//	}
	
	@ResponseBody
	@RequestMapping("/getMarketingCenterList")
	public String getMarketingCenterList(HttpServletRequest request){
		ArrayList<FDMarketingCenter> mcList = (ArrayList<FDMarketingCenter>) orderService.getMarketingCenterList();
		if(mcList==null||mcList.size()==0){
			return Utils.getErrorStatusJson("获取数据失败").toJSONString();
		}
		return Utils.getSuccessEntityJson(mcList).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getMarketingCenterDetail")
	public String getMarketingCenterDetail(HttpServletRequest request, String marketId){
		if(Utils.isEmpty(marketId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		FDMarketingCenter mc = orderService.getMarketingCenterDetail(marketId);
		return Utils.getSuccessEntityJson(mc).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/editMarketingCenter")
	public String editMarketingCenter(FDMarketingCenter mc, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(mc.getDistrict()))
			return Utils.getErrorStatusJson("地区为空 ").toJSONString();
		if(Utils.isEmpty(mc.getName()))
			return Utils.getErrorStatusJson("名称为空").toJSONString();
		mc.setDistrict(Utils.turnCodeWhenGet(mc.getDistrict(), request));
		mc.setName(Utils.turnCodeWhenGet(mc.getName(), request));
		String r = orderService.editMarketingCenter(mc, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑成功").toJSONString();
	}
	
}