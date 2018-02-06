package com.mg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.OperateLogDao;
import com.mg.dao.ParamDao;
import com.mg.dao.PhotoSpotsDao;
import com.mg.entity.FDPhotoSpots;
import com.mg.entity.FDSystemParam;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional
public class TradeServiceImpl implements TradeService {
	@Autowired
	private  ConUtil conUtil;
	@Autowired
	private  ParamDao paramDao;
	@Autowired
	private PhotoSpotsDao photoSpotsDao;
	@Autowired
	private OperateLogDao operateLogDao;
	
	@Override
	public String getIntegralList(String nameTel, String changeType, Integer page, Integer pageSize){
		String sql = " select ic.*,(select u1.LOGIN_NAME from fd_user u1 where u1.ID=ic.USER_ID) LOGIN_NAME,(select u1.PHONE from fd_user u1 where u1.ID=ic.USER_ID) PHONE from fd_integral_change ic ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(nameTel)){
			String userSql = " select u.ID from fd_user u where u.LOGIN_NAME='"+nameTel+"' or u.PHONE='"+nameTel+"' ";
			List<Map<String,Object>> userList = conUtil.getData(userSql);
			if(userList!=null&&userList.size()>0){
				String userId = (String) userList.get(0).get("ID");
				where += " and ic.USER_ID='"+userId+"' ";
			}
		}
		if(!Utils.isEmpty(changeType)){
			where += " and ic.CHANGE_TYPE='"+changeType+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by ic.CHANGE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		List<Map<String,Object>> countList = conUtil.getData(" select count(*) as COUNT from fd_integral_change ic "+where);
		json.put("total", ((Long)countList.get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getCatcoinList(String nameTel, String changeType, Integer page, Integer pageSize){
		String sql = " select cc.*,(select u1.LOGIN_NAME from fd_user u1 where u1.ID=cc.USER_ID) LOGIN_NAME,(select u1.PHONE from fd_user u1 where u1.ID=cc.USER_ID) PHONE from fd_catcoin_change cc ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(nameTel)){
			String userSql = " select u.ID from fd_user u where u.LOGIN_NAME='"+nameTel+"' or u.PHONE='"+nameTel+"' ";
			List<Map<String,Object>> userList = conUtil.getData(userSql);
			if(userList!=null&&userList.size()>0){
				String userId = (String) userList.get(0).get("ID");
				where += " and cc.USER_ID='"+userId+"' ";
			}
		}
		if(!Utils.isEmpty(changeType)){
			where += " and cc.CHANGE_TYPE='"+changeType+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by cc.CHANGE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		List<Map<String,Object>> countList = conUtil.getData(" select count(*) as COUNT from fd_catcoin_change cc "+where);
		json.put("total", ((Long)countList.get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public FDSystemParam getSystemParam(){
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		return sp;
	}
	
	@Override
	public String updateParam(FDSystemParam sp, HttpServletRequest request, String adminId){
		FDSystemParam sp1 = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		sp1.setCompleteCrop(sp.getCompleteCrop());
		sp1.setCompleteGender(sp.getCompleteGender());
		sp1.setCompleteName(sp.getCompleteName());
		sp1.setConsumeIntegralRate(sp.getConsumeIntegralRate());
		sp1.setIntegralToCatcoinRate(sp.getIntegralToCatcoinRate());
		sp1.setCatcoinDeductRate(sp.getCatcoinDeductRate());
		sp1.setIsCatcoinDeductOpen(sp.getIsCatcoinDeductOpen());
		sp1.setRechargeCatcoinRate(sp.getRechargeCatcoinRate());
		sp1.setCompleteDetailAddress(sp.getCompleteDetailAddress());
		sp1.setSignIn(sp.getSignIn());
		paramDao.update(sp1);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "设定兑换比例");
		return Utils.evictFrontCache(request, "param/evictParamCache");
	}
	
	//=====================================================================
	
	@Override
	public String editPhotoSpots(String id, String imagePath, Integer integralValue, String[] spots, HttpServletRequest request, String adminId){
		if(Utils.isEmpty(id)){
			FDPhotoSpots ps = new FDPhotoSpots();
			ps.setId(UUID.randomUUID().toString());
			ps.setImagePaths(imagePath);
			ps.setIntegralValue(integralValue);
			if(spots.length>=1)
				ps.setSpot1(spots[0]);
			if(spots.length>=2)
				ps.setSpot2(spots[1]);
			if(spots.length>=3)
				ps.setSpot3(spots[2]);
			if(spots.length>=4)
				ps.setSpot4(spots[3]);
			if(spots.length>=5)
				ps.setSpot5(spots[4]);
			if(spots.length>=6)
				ps.setSpot6(spots[5]);
			if(spots.length>=7)
				ps.setSpot7(spots[6]);
			if(spots.length>=8)
				ps.setSpot8(spots[7]);
			if(spots.length>=9)
				ps.setSpot9(spots[8]);
			if(spots.length>=10)
				ps.setSpot10(spots[9]);
			ps.setCreateDate(new Date());
			ps.setSortDate(new Date());
			photoSpotsDao.add(ps);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GAME, "添加游戏");
		}else{
			FDPhotoSpots ps = photoSpotsDao.findById(id);
			if(ps==null)
				return "未找到记录";
			ps.setImagePaths(imagePath);
			ps.setIntegralValue(integralValue);
			if(spots.length>=1)
				ps.setSpot1(spots[0]);
			if(spots.length>=2)
				ps.setSpot2(spots[1]);
			if(spots.length>=3)
				ps.setSpot3(spots[2]);
			if(spots.length>=4)
				ps.setSpot4(spots[3]);
			if(spots.length>=5)
				ps.setSpot5(spots[4]);
			if(spots.length>=6)
				ps.setSpot6(spots[5]);
			if(spots.length>=7)
				ps.setSpot7(spots[6]);
			if(spots.length>=8)
				ps.setSpot8(spots[7]);
			if(spots.length>=9)
				ps.setSpot9(spots[8]);
			if(spots.length>=10)
				ps.setSpot10(spots[9]);
			photoSpotsDao.update(ps);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GAME, "编辑游戏");
		}
       return Utils.evictFrontCache(request, "evictGameCache");
	}
	
	@Override
	public List<FDPhotoSpots> getPhotoSpotsList(){
		return photoSpotsDao.findBycondition(" order by sortDate desc ");
	}
	
	@Override
	public FDPhotoSpots getPhotoSpotsDetail(String photoSpotsId){
		return photoSpotsDao.findById(photoSpotsId);
	}
	
	@Override
	public String delPhotoSpots(String photoSpotsId, HttpServletRequest request, String adminId){
		FDPhotoSpots ps = photoSpotsDao.findById(photoSpotsId);
		if(ps==null)
			return "未找到记录";
		photoSpotsDao.deleteById(ps.getId());
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GAME, "删除游戏");
		return Utils.evictFrontCache(request, "evictGameCache");
	}
	
	@Override
	public String setPhotoSpotsTop(String photoSpotsId, HttpServletRequest request){
		FDPhotoSpots ps = photoSpotsDao.findById(photoSpotsId);
		if(ps==null)
			return "未找到记录";
		ps.setSortDate(new Date());
		photoSpotsDao.update(ps);
		return Utils.evictFrontCache(request, "evictGameCache");
	}
	
	//运费说明=========================================
	@Override
	public String updateSetFreightFeeDesc(String desc, HttpServletRequest request, String adminId){
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		sp.setFreightFeeDesc(desc);
		paramDao.update(sp);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "编辑运费说明");
		return Utils.evictFrontCache(request, "param/evictParamCache");
	}
	
}
