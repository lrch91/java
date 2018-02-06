package com.mg.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.CatcoinChangeDao;
import com.mg.dao.IntegralChangeDao;
import com.mg.dao.SignInRecordDao;
import com.mg.dao.UserDao;
import com.mg.entity.FDCatcoinChange;
import com.mg.entity.FDIntegralChange;
import com.mg.entity.FDSignInRecord;
import com.mg.entity.FDSystemParam;
import com.mg.entity.FDUser;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional 
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private SignInRecordDao signInRecordDao;
	@Autowired
	private CatcoinChangeDao catcoinChangeDao;
	@Autowired
	private IntegralChangeDao integralChangeDao;
	
	@Override
	public String signIn(String userId, FDSystemParam sp){
		try{
			FDSignInRecord sir = signInRecordDao.findOne(" where userId='"+userId+"' and signDate='"+Utils.getCurrentTimeString().substring(0,10)+"' ");
			if(sir!=null)
				return Utils.getErrorStatusJson("系统异常").toJSONString();
			FDSignInRecord ir = new FDSignInRecord();
			ir.setId(UUID.randomUUID().toString());
			ir.setUserId(userId);
			ir.setCreateDate(new Date());
			ir.setSignDate(Utils.getCurrentTimeString().substring(0,10));
			signInRecordDao.add(ir);
			
			FDUser u = userDao.findById(userId);
			u.setIntegral(u.getIntegral()+sp.getSignIn());
			u.setHistoryIntegral(u.getHistoryIntegral()+sp.getSignIn());
			userDao.update(u);

			FDIntegralChange ic1 = new FDIntegralChange();
			ic1.setId(UUID.randomUUID().toString());
			ic1.setUserId(userId);
			ic1.setChangeDate(new Date());
			ic1.setAttachId(ir.getId());
			ic1.setChangeSum(sp.getSignIn());
			ic1.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_SIGNIN);
			ic1.setNowIntegral(u.getIntegral());
			integralChangeDao.add(ic1);
			return Utils.getSuccessEntityJson(ic1.getChangeSum()).toJSONString();
		}catch(Exception e){
			e.printStackTrace();
			return Utils.getErrorStatusJson("系统异常").toJSONString();
		}
	}
	
	@Override
	public JSONObject integralToCatcoinInfo(String userId, FDSystemParam sp){
		FDUser u = userDao.findById(userId);
		JSONObject json = new JSONObject();
		json.put("integralToCatcoinRate", sp.getIntegralToCatcoinRate());
		json.put("nowIntegral", u.getIntegral());
		json.put("canGetCatcoin", u.getIntegral()/sp.getIntegralToCatcoinRate());
		json.put("msg", "获取信息成功");
		json.put("status", "SUCCESS");
		return json;
	}
	
	@Override
	public String integralToCatcoin(String userId, Integer num, FDSystemParam sp){
		FDUser u = userDao.findById(userId);
		if(u.getStatus().equals(Status.USER_LOCKED)){
			return "用户已禁用，无法操作";
		}
		Integer rate = sp.getIntegralToCatcoinRate();
		if(u.getIntegral()<(rate*num)){
			return "当前积分少于兑换所需数量";
		}
		Integer nowIntegral = u.getIntegral()-rate*num;
		Integer nowCatcoin = u.getCatcoin()+num;
		u.setIntegral(nowIntegral);
		u.setCatcoin(nowCatcoin);
		userDao.update(u);
		
		FDCatcoinChange cc = new FDCatcoinChange();
		cc.setId(UUID.randomUUID().toString());
		cc.setUserId(userId);
		cc.setChangeSum(num);
		cc.setNowCoin(nowCatcoin);
		cc.setChangeType(Status.CATCOINCHANGE_CHANGETYPE_INTEGRALTOCATCOIN);
		cc.setChangeDate(new Date());
		catcoinChangeDao.add(cc);
		
		FDIntegralChange ic = new FDIntegralChange();
		ic.setId(UUID.randomUUID().toString());
		ic.setUserId(userId);
		ic.setChangeSum(-rate*num);
		ic.setAttachId(cc.getId());
		ic.setNowIntegral(nowIntegral);
		ic.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_INTEGRALTOCATCOIN);
		ic.setChangeDate(new Date());
		integralChangeDao.add(ic);
		return null;
	}
	
	@Override
	public String donateCatcoin(String userId, String accepter, Integer num){
		FDUser from = userDao.findById(userId);
		FDUser to = userDao.findOne(" where phone='"+accepter+"' ");
		if(to==null)
			return "未找到受转增用户";
		if(from.getCatcoin()<num)
			return "转赠猫币金额大于账户猫币余额";
		Integer fromBeforeCatcoin = from.getCatcoin();
		Integer toBeforeCatcoin = to.getCatcoin();
		from.setCatcoin(fromBeforeCatcoin-num);
		to.setCatcoin(toBeforeCatcoin+num);
		userDao.update(from);
		userDao.update(to);
		
		FDCatcoinChange cc1 = new FDCatcoinChange();
		cc1.setId(UUID.randomUUID().toString());
		cc1.setUserId(from.getId());
		cc1.setChangeDate(new Date());
		cc1.setChangeSum(num);
		cc1.setChangeType(Status.CATCOINCHANGE_CHANGETYPE_DONATEFROM);
		cc1.setNowCoin(fromBeforeCatcoin-num);
		catcoinChangeDao.add(cc1);
		
		FDCatcoinChange cc2 = new FDCatcoinChange();
		cc2.setId(UUID.randomUUID().toString());
		cc2.setUserId(to.getId());
		cc2.setChangeDate(new Date());
		cc2.setChangeSum(num);
		cc2.setChangeType(Status.CATCOINCHANGE_CHANGETYPE_DONATETO);
		cc2.setNowCoin(toBeforeCatcoin+num);
		catcoinChangeDao.add(cc2);
		
		return null;
	}
	
}
