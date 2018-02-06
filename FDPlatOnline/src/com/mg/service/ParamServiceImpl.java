package com.mg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mg.dao.ParamDao;
import com.mg.dao.UserDao;
import com.mg.entity.FDSystemParam;
import com.mg.util.Status;

@Service
@Transactional 
public class ParamServiceImpl implements ParamService {

	@Autowired
	UserDao userDao;
	@Autowired
	ParamDao paramDao;
	
	@Override
	@Cacheable(value="systemParamCache")
	public FDSystemParam getParamCache(){
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		return sp;
	}
	
	@Override
	@CacheEvict(value="systemParamCache", allEntries=true)
	public String evictParamCache(){
		return null;
	}
	
}
