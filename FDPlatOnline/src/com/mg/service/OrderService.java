package com.mg.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mg.entity.FDSystemParam;
import com.mg.entity.WxBack;


public interface OrderService {
	String addOrder(String goodsId, Double sell_count, String acceptName, String acceptTel, String city, String area, String street, String address, String userRemark, String userId);
	String getMemberOrderList(String userId, String status, Integer page, Integer pageSize);
	String getMemberOrderDetail(String orderId, String userId);
	String memberCancelOrder(String orderId, String userId);
	String deductOrderPrice(String orderId, String userId, Integer deductSum, FDSystemParam pram);
	String get_alipay_sign(String orderId, String userId, Map<String, Object> map, HttpServletResponse response);
	String alipay_order_notify(HttpServletRequest request);
	String getWXPrepayId(String orderId, String userId, String spbill_create_ip, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response);
	WxBack wx_pay_notify(HttpServletRequest request, Map<String, Object> map);
	Map<String,Object> getAlipayConifg();
	Map<String,Object> getWxpayConifg();
}
