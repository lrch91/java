package com.mg.service;

import javax.servlet.http.HttpServletRequest;

import com.mg.entity.FDFertilizeEvaluate;
import com.mg.entity.FDSoiltestEvaluate;
import com.mg.entity.FDSystemParam;


public interface ServeService {
	String handleFertilize(String applyFertilizeId, String adminId);
	String applyFertilizeDetail(String applyFertilizeId);
	String applyFertilizeList(String nameTel, String applier, String fertilizeCategory, String status, Integer page, Integer pageSize);
	String addFertilizeReply(String evaluateId, FDFertilizeEvaluate fe, String adminId);
	String delFertilizeReply(String evaluateId, String adminId);
	
	String handleSoiltest(String applySoiltestId, String name, String adminId);
	String applySoiltestDetail(String applySoiltestId);
	String applySoiltestList(String nameTel, String applier, String handler, String status, Integer page, Integer pageSize);
	String addSoiltestReply(String evaluateId, FDSoiltestEvaluate se, String adminId);
	String delSoiltestReply(String evaluateId, String adminId);
	
	FDSystemParam getSystemParam();
	String updateMinFSValue(Double minFertilizeNum, Double minSoiltestAcreage, HttpServletRequest request, String adminId);
}
