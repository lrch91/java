package com.mg.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mg.entity.WxBack;
import com.mg.util.Pager;



public interface TradeService {
	Pager getIntegralList(String userId, Integer page, Integer pageSize);
	Pager getCatcoinList(String userId, Integer page, Integer pageSize);
	String get_alipay_sign(Integer rechargeSum, String userId, Map<String, Object> map, HttpServletResponse response);
	String alipay_order_notify(HttpServletRequest request);
	String getWXPrepayId(Integer rechargeSum, String userId, String spbill_create_ip, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response);
	WxBack wx_pay_notify(HttpServletRequest request, Map<String, Object> map);
	
}
