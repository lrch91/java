package com.mg.dao;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.mg.entity.FDOperateLog;

@Repository
public class OperateLogDaoImpl extends PublicDaoImpl<FDOperateLog> implements OperateLogDao {

	@Override
	public void addOperateLog(String adminId, String operateType, String operateObject){
		FDOperateLog log = new FDOperateLog();
		log.setId(UUID.randomUUID().toString());
		log.setUserId(adminId);
		log.setOperateDate(new Date());
		log.setOperateObject(operateObject);
		log.setOperateType(operateType);
		super.add(log);
	}
	
	@Override
	public void addOperateLog(String adminId, String operateType, String operateObject, String operateNo){
		FDOperateLog log = new FDOperateLog();
		log.setId(UUID.randomUUID().toString());
		log.setUserId(adminId);
		log.setOperateDate(new Date());
		log.setOperateObject(operateObject);
		log.setOperateType(operateType);
		log.setOperateNo(operateNo);
		super.add(log);
	}
}
