package com.mg.service;

import com.mg.entity.FDSystemParam;


public interface ParamService {
	FDSystemParam getParamCache();
//	FDSystemParam getSystemParam();
	String evictParamCache();
}
