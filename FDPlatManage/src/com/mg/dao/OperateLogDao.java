package com.mg.dao;

import com.mg.entity.FDOperateLog;

public interface OperateLogDao extends PublicDao<FDOperateLog>{
	
	void addOperateLog(String adminId, String operateType, String operateObject);
	void addOperateLog(String adminId, String operateType, String operateObject, String operateNo);
	
}
