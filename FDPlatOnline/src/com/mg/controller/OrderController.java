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
import com.mg.service.ParamService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	CommonService commonService;
	@Autowired
	OrderService orderService;
	@Autowired
	ParamService paramService;
	
	@ResponseBody
	@RequestMapping("/addOrder")
	public String addOrder(String goodsId, Double sell_count, String acceptName, String acceptTel, String city, String area, String street, String address, String userRemark, HttpServletRequest request){
		acceptName = Utils.turnCodeWhenGet(acceptName, request);
		address = Utils.turnCodeWhenGet(address, request);
		userRemark = Utils.turnCodeWhenGet(userRemark, request);
		if(Utils.isEmpty(goodsId))
			return Utils.getErrorStatusJson("商品ID为空").toJSONString();
		if(sell_count==null||sell_count<0)
			return Utils.getErrorStatusJson("数量为空").toJSONString();
		sell_count = Double.valueOf(Utils.DoubleToString(sell_count));
		if(Utils.isEmpty(acceptName))
			return Utils.getErrorStatusJson("收货人姓名为空").toJSONString();
		if(Utils.isEmpty(acceptTel))
			return Utils.getErrorStatusJson("手机号为空").toJSONString();
		if(Utils.isEmpty(street))
			return Utils.getErrorStatusJson("地址为空").toJSONString();
		if(Utils.isEmpty(address))
			return Utils.getErrorStatusJson("详细地址为空").toJSONString();
		if(Utils.isEmpty(userRemark))
			userRemark = "";
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		String r = orderService.addOrder(goodsId, sell_count, acceptName, acceptTel, city, area, street, address, userRemark, ua.getId());
		if(r==null)
			return Utils.getErrorStatusJson("下单失败").toJSONString();
		return Utils.getSuccessEntityJson(r).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getMemberOrderList")
	public String getMemberOrderList(String status, Integer page, Integer pageSize ,HttpServletRequest request){
		if(page==null||page<1)
			page =1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return orderService.getMemberOrderList(ua.getId(), status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/memberCancelOrder")
	public String memberCancelOrder(String orderId ,HttpServletRequest request){
		if(Utils.isEmpty(orderId))
			return Utils.getErrorStatusJson("订单ID为空").toJSONString();
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		String r = orderService.memberCancelOrder(orderId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("取消订单成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getMemberOrderDetail")
	public String getMemberOrderDetail(String orderId, HttpServletRequest request){
		if(Utils.isEmpty(orderId))
			return Utils.getErrorStatusJson("订单ID为空").toJSONString();
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return orderService.getMemberOrderDetail(orderId, ua.getId());
	}
	
	@ResponseBody
	@RequestMapping("/deductOrderPrice")
	public String deductOrderPrice(String orderId, Integer deductSum, HttpServletRequest request){
		if(Utils.isEmpty(orderId))
			return Utils.getErrorStatusJson("订单ID为空").toJSONString();
		if(deductSum==null||deductSum<1)
			return Utils.getErrorStatusJson("抵扣金额为空").toJSONString();
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		String r = orderService.deductOrderPrice(orderId, ua.getId(), deductSum, paramService.getParamCache() );
		if(r!=null)
			return Utils.getErrorStatusJson("抵扣操作失败").toJSONString();
		return Utils.getSuccessStatusJson("抵扣成功").toJSONString();
	}
	
	//支付宝获取签名
	@ResponseBody
	@RequestMapping("get_alipay_sign")
	public String get_alipay_sign(String orderId, HttpServletRequest request, HttpServletResponse response){
		if(orderId==null || orderId.equals("")){
			return Utils.getErrorStatusJson("订单号为空").toJSONString();
		}
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return orderService.get_alipay_sign(orderId, ua.getId(), orderService.getAlipayConifg(), response);
	}
	
	//支付宝异步回调信息处理
	@ResponseBody
	@RequestMapping("alipay_order_notify")
	public String alipay_order_notify(HttpServletRequest request){
		return orderService.alipay_order_notify(request);
	}
	
	//微信获取预付款ID
	@ResponseBody
	@RequestMapping("getWXPrepayId")
	public String getWXPrepayId(String orderId,String spbill_create_ip, HttpServletRequest request, HttpServletResponse response){
		if(orderId==null || orderId.equals("")){
			return Utils.getErrorStatusJson("订单号为空").toJSONString();
		}
		if(spbill_create_ip==null || spbill_create_ip.equals("")){
			return Utils.getErrorStatusJson("IP地址为空").toJSONString();
		}
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return orderService.getWXPrepayId(orderId, ua.getId(), spbill_create_ip, orderService.getWxpayConifg(),request, response);
	}
	
	//微信异步回调信息处理
	@ResponseBody
	@RequestMapping("wxpay_order_notify")
	public WxBack wx_pay_notify(HttpServletRequest request){
		return orderService.wx_pay_notify(request, orderService.getWxpayConifg());
	}
	
}
