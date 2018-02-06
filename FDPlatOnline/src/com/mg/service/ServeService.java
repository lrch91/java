package com.mg.service;

import com.mg.entity.FDApplyFertilize;
import com.mg.entity.FDApplySoiltest;
import com.mg.entity.FDFertilizeEvaluate;
import com.mg.entity.FDSoiltestEvaluate;
import com.mg.entity.FDSystemParam;


public interface ServeService {
	String applyFertilize(FDApplyFertilize af, FDSystemParam sp);
	String applyFertilizeDetail(String userId, String applyFertilizeId);
	String applyFertilizeList(String userId, String status, Integer page, Integer pageSize);
	String addFertilizeEvaluate(FDFertilizeEvaluate fe);
	String applySoiltest(FDApplySoiltest as, FDSystemParam sp);
	String applySoiltestDetail(String userId, String applySoiltestId);
	String applySoiltestList(String userId, String status, Integer page, Integer pageSize);
	String addSoiltestEvaluate(FDSoiltestEvaluate se);
}
