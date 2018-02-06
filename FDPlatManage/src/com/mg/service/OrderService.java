package com.mg.service;

import java.util.List;

import com.mg.entity.FDMarketingCenter;


public interface OrderService {
	String getOrderList(String nameTel, String orderNumber, String status, String startDate, String endDate, Integer page, Integer pageSize, String adminId);
	String getOrderDetalil(String orderId);
	String changeOrderPrice(String orderId, Double newPrice, String adminId);
	String acceptOrder(String orderId, String adminId);
	String cancelOrder(String orderId, String remark, String adminId);
	String delOrder(String orderId);
	List<FDMarketingCenter> getMarketingCenterList();
	FDMarketingCenter getMarketingCenterDetail(String marketId);
	String editMarketingCenter(FDMarketingCenter mc, String adminId);
}
