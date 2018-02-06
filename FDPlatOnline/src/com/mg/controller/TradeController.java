package com.mg.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.WxBack;
import com.mg.service.CommonService;
import com.mg.service.OrderService;
import com.mg.service.TradeService;
import com.mg.util.Pager;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/trade")
public class TradeController {
	
	@Autowired
	CommonService commonService;
	@Autowired
	TradeService tradeService;
	@Autowired
	OrderService orderService;
	
	@ResponseBody
	@RequestMapping("/getIntegralList")
	public String getIntegralList(Integer page, Integer pageSize, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		Pager p = tradeService.getIntegralList(ua.getId(), page, pageSize);
		if(p==null)
			return Utils.getErrorStatusJson("获取列表失败").toJSONString();
		return Utils.getSuccessStatusJson(p).toJSONString();
	} 
	
	@ResponseBody
	@RequestMapping("/getCatcoinList")
	public String getCatcoinList(Integer page, Integer pageSize, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		Pager p = tradeService.getCatcoinList(ua.getId(), page, pageSize);
		if(p==null)
			return Utils.getErrorStatusJson("获取列表失败").toJSONString();
		return Utils.getSuccessStatusJson(p).toJSONString();
	}
	
	/*@ResponseBody
	@RequestMapping("/getUserRechargeList")
	public String getUserRechargeList(Integer page, Integer pageSize, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		Pager p = tradeService.getUserRechargeList(ua.getId(), page, pageSize);
		if(p==null)
			return Utils.getErrorStatusJson("获取列表失败").toJSONString();
		return Utils.getSuccessStatusJson(p).toJSONString();
	}*/
	
	//支付宝获取签名
	@ResponseBody
	@RequestMapping("get_alipay_sign")
	public String get_alipay_sign(Integer rechargeSum, HttpServletRequest request, HttpServletResponse response){
		if(rechargeSum==null || rechargeSum<1){
			return Utils.getErrorStatusJson("充值金额过小").toJSONString();
		}
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return tradeService.get_alipay_sign(rechargeSum, ua.getId(), orderService.getAlipayConifg(), response);
	}
	
	//支付宝异步回调信息处理
	@ResponseBody
	@RequestMapping("alipay_order_notify")
	public String alipay_order_notify(HttpServletRequest request){
		return tradeService.alipay_order_notify(request);
	}
	
	//微信获取预付款ID
	@ResponseBody
	@RequestMapping("getWXPrepayId")
	public String getWXPrepayId(Integer rechargeSum,String spbill_create_ip, HttpServletRequest request, HttpServletResponse response){
		if(rechargeSum==null || rechargeSum<1){
			return Utils.getErrorStatusJson("充值金额过小").toJSONString();
		}
		if(spbill_create_ip==null || spbill_create_ip.equals("")){
			return Utils.getErrorStatusJson("IP地址为空").toJSONString();
		}
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return tradeService.getWXPrepayId(rechargeSum, ua.getId(), spbill_create_ip, orderService.getWxpayConifg(), request, response);
	}
	
	//微信异步回调信息处理
	@ResponseBody
	@RequestMapping("wxpay_order_notify")
	public WxBack wx_pay_notify(HttpServletRequest request){
		return tradeService.wx_pay_notify(request, orderService.getWxpayConifg());
	}
}
