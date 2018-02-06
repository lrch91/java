package com.mg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mg.service.CommonService;
import com.mg.service.OperationService;


@Controller
@RequestMapping("/statistic")
//统计
public class StatisticController {

	@Autowired
	private OperationService operationService;
	@Autowired
	private CommonService commonService;
	
	//用户统计 测土统计 配肥统计 销售统计
}
