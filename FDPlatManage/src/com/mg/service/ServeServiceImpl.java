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
import com.mg.dao.ApplyFertilizeDao;
import com.mg.dao.ApplySoiltestDao;
import com.mg.dao.AreaDao;
import com.mg.dao.FertilizeEvaluateDao;
import com.mg.dao.OperateLogDao;
import com.mg.dao.ParamDao;
import com.mg.dao.SoiltestEvaluateDao;
import com.mg.dao.UserDao;
import com.mg.entity.FDApplyFertilize;
import com.mg.entity.FDApplySoiltest;
import com.mg.entity.FDFertilizeEvaluate;
import com.mg.entity.FDSoiltestEvaluate;
import com.mg.entity.FDSystemParam;
import com.mg.entity.FDUser;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional
public class ServeServiceImpl implements ServeService {

	@Autowired
	ApplyFertilizeDao applyFertilizeDao;
	@Autowired
	ApplySoiltestDao applySoiltestDao;
	@Autowired
	FertilizeEvaluateDao fertilizeEvaluateDao;
	@Autowired
	SoiltestEvaluateDao soiltestEvaluateDao;
	@Autowired
	AreaDao areaDao;
	@Autowired
	UserDao userDao;
	@Autowired
	ParamDao paramDao;
	@Autowired
	OperateLogDao operateLogDao;
	@Autowired
	ConUtil conUtil;
	
	@Override
	public String handleFertilize(String applyFertilizeId, String adminId){
		FDApplyFertilize af = applyFertilizeDao.findById(applyFertilizeId);
		if(af==null)
			return "未找到测土申请信息";
		if(!af.getStatus().equals(Status.APPLYFERTILIZE_STATUS_APPLIED))
			return "非待处理测土申请";
		af.setStatus(Status.APPLYFERTILIZE_STATUS_HANDLED);
		af.setHandleDate(new Date());
		
		applyFertilizeDao.update(af);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SERVE, "受理配肥", af.getUserId());
		return null;
	}
	
	@Override
	public String applyFertilizeDetail(String applyFertilizeId){
		String sql = " select af.*, (select a1.NAME from fd_area a1 where a1.ID=af.CITY) CITY_NAME, "
				+ " (select a2.NAME from fd_area a2 where a2.ID=af.AREA) AREA_NAME, "
				+ " (select a3.NAME from fd_area a3 where a3.ID=af.STREET) STREET_NAME, "
				+ " (select count(*) from fd_fertilize_evaluate fe where fe.APPLY_FERTILIZE_ID=af.ID and fe.TYPE="+Status.EVALUATE_TYPE_USER+") EVALUATE_COUNT, "
				+ " (select fe1.ID from fd_fertilize_evaluate fe1 where fe1.APPLY_FERTILIZE_ID=af.ID and fe1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_ID, "
				+ " (select fe1.CONTENT from fd_fertilize_evaluate fe1 where fe1.APPLY_FERTILIZE_ID=af.ID and fe1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_CONTENT, "
				+ " (select fe1.IS_SHOW from fd_fertilize_evaluate fe1 where fe1.APPLY_FERTILIZE_ID=af.ID and fe1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_ISSHOW, "
				+ " (select fe1.CREATE_DATE from fd_fertilize_evaluate fe1 where fe1.APPLY_FERTILIZE_ID=af.ID and fe1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_DATE, "
				+ " (select fe2.ID from fd_fertilize_evaluate fe2 where fe2.APPLY_FERTILIZE_ID=af.ID and fe2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_ID, "
				+ " (select fe2.CONTENT from fd_fertilize_evaluate fe2 where fe2.APPLY_FERTILIZE_ID=af.ID and fe2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_CONTENT, "
				+ " (select fe2.CREATE_DATE from fd_fertilize_evaluate fe2 where fe2.APPLY_FERTILIZE_ID=af.ID and fe2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_DATE "
				+ " from fd_apply_fertilize af where af.ID='"+applyFertilizeId+"' ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql);
		return json.toJSONString();
	}
	
	@Override
	public String applyFertilizeList(String nameTel, String applier, String fertilizeCategory, String status, Integer page, Integer pageSize){
		String sql = " select af.*,(select a1.NAME from fd_area a1 where a1.ID=af.CITY) CITY_NAME, "
				+ " (select a2.NAME from fd_area a2 where a2.ID=af.AREA) AREA_NAME, "
				+ " (select a3.NAME from fd_area a3 where a3.ID=af.STREET) STREET_NAME ";
		String where = " from fd_apply_fertilize af where 1=1 ";
		FDUser user = userDao.findOne(" where loginName='"+nameTel+"' or phone='"+nameTel+"' ");
		if(user!=null){
			where = " and af.USER_ID='"+user.getId()+"' ";
		}
		if(!Utils.isEmpty(applier)){
			where += " and af.TRUE_NAME='"+applier+"' ";
		}
		if(!Utils.isEmpty(fertilizeCategory)){
			where += " and FERTILIZE_CATEGORY like '%"+fertilizeCategory+"%' ";
		}
		if(!Utils.isEmpty(status)){
			where += " and af.STATUS='"+status+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by af.CREATE_DATE desc ");
		List<Map<String,Object>> list = conUtil.getData(" select count(*) COUNT "+where);
		json.put("total", ((Long)list.get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String addFertilizeReply(String evaluateId, FDFertilizeEvaluate fe, String adminId){
		FDFertilizeEvaluate evaluate = fertilizeEvaluateDao.findById(evaluateId);
		if(evaluate==null)
			return "未找到评论信息";
		if(!evaluate.getType().equals(Status.EVALUATE_TYPE_USER))
			return "评论信息类型错误";
		FDApplyFertilize af = applyFertilizeDao.findById(evaluate.getApplyFertilizeId());
		if(af==null)
			return "未找到配肥申请记录";
		FDFertilizeEvaluate tempReply = fertilizeEvaluateDao.findOne(" where applyFertilizeId='"+evaluate.getApplyFertilizeId()+"' and type="+Status.EVALUATE_TYPE_ADMIN);
		if(tempReply!=null)
			return "已添加过回复";
		fe.setId(UUID.randomUUID().toString());
		fe.setApplyFertilizeId(evaluate.getApplyFertilizeId());
		fe.setCreateDate(new Date());
		fe.setStarNum(0);
		fe.setType(Status.EVALUATE_TYPE_ADMIN);
		fe.setUserId(evaluate.getUserId());
		fe.setIsShow(Status.EVALUATE_ISSHOW_YES);
		
		fertilizeEvaluateDao.add(fe);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SERVE, "回复配肥评论", fe.getUserId());
		return null;
	}
	
	@Override
	public String delFertilizeReply(String evaluateId, String adminId){
		FDFertilizeEvaluate fe = fertilizeEvaluateDao.findById(evaluateId);
		if(fe==null)
			return "获取评论信息失败";
		fe.setIsShow(Status.EVALUATE_ISSHOW_NO);
		fertilizeEvaluateDao.update(fe);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SERVE, "删除配肥评论", fe.getUserId());
		return null;
	}
	
	@Override
	public String handleSoiltest(String applySoiltestId, String name, String adminId){
		FDApplySoiltest as = applySoiltestDao.findById(applySoiltestId);
		if(as==null)
			return "未找到测土申请信息";
		if(!as.getStatus().equals(Status.APPLYSOILTEST_STATUS_APPLIED))
			return "非待处理测土申请";
		as.setHandlerName(name);
		as.setHandleDate(new Date());
		as.setStatus(Status.APPLYSOILTEST_STATUS_HANDLED);
		applySoiltestDao.update(as);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SERVE, "受理测土", as.getUserId());
		return null;
	}
	
	@Override
	public String applySoiltestDetail(String applySoiltestId){
		String sql = " select os.*,(select a1.NAME from fd_area a1 where a1.ID=os.CITY) CITY_NAME, "
				+ " (select a2.NAME from fd_area a2 where a2.ID=os.AREA) AREA_NAME, "
				+ " (select a3.NAME from fd_area a3 where a3.ID=os.STREET) STREET_NAME, "
				+ " (select count(*) from fd_soiltest_evaluate se where se.APPLY_SOILTEST_ID=os.ID and se.TYPE="+Status.EVALUATE_TYPE_USER+") EVALUATE_COUNT, "
				+ " (select se1.ID from fd_soiltest_evaluate se1 where se1.APPLY_SOILTEST_ID=os.ID and se1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_ID, "
				+ " (select se1.CONTENT from fd_soiltest_evaluate se1 where se1.APPLY_SOILTEST_ID=os.ID and se1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_CONTENT, "
				+ " (select se1.IS_SHOW from fd_soiltest_evaluate se1 where se1.APPLY_SOILTEST_ID=os.ID and se1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_ISSHOW, "
				+ " (select se1.CREATE_DATE from fd_soiltest_evaluate se1 where se1.APPLY_SOILTEST_ID=os.ID and se1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_DATE, "
				+ " (select se2.ID from fd_soiltest_evaluate se2 where se2.APPLY_SOILTEST_ID=os.ID and se2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_ID, "
				+ " (select se2.CONTENT from fd_soiltest_evaluate se2 where se2.APPLY_SOILTEST_ID=os.ID and se2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_CONTENT, "
				+ " (select se2.CREATE_DATE from fd_soiltest_evaluate se2 where se2.APPLY_SOILTEST_ID=os.ID and se2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_DATE "
				+ " from fd_apply_soiltest os where os.ID='"+applySoiltestId+"' ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql);
		return json.toJSONString();
	}
	
	@Override
	public String applySoiltestList(String nameTel, String applier, String handler, String status, Integer page, Integer pageSize){
		String sql = " select at.*,(select a1.NAME from fd_area a1 where a1.ID=at.CITY) CITY_NAME, "
				+ " (select a2.NAME from fd_area a2 where a2.ID=at.AREA) AREA_NAME, "
				+ " (select a3.NAME from fd_area a3 where a3.ID=at.STREET) STREET_NAME ";
		String where = " from fd_apply_soiltest at where 1=1 ";
		FDUser user = userDao.findOne(" where loginName='"+nameTel+"' or phone='"+nameTel+"' ");
		if(user!=null){
			where = " and at.USER_ID='"+user.getId()+"' ";
		}
		if(!Utils.isEmpty(applier)){
			where += " and at.TRUE_NAME='"+applier+"' ";
		}
		if(!Utils.isEmpty(handler)){
			where += " and at.HANDLER_NAME='"+handler+"' ";
		}
		if(!Utils.isEmpty(status)){
			where += " and at.STATUS='"+status+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by at.CREATE_DATE desc ");
		List<Map<String,Object>> list = conUtil.getData(" select count(*) COUNT "+where);
		json.put("total", ((Long)list.get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String addSoiltestReply(String evaluateId, FDSoiltestEvaluate se, String adminId){
		FDSoiltestEvaluate evaluate = soiltestEvaluateDao.findById(evaluateId);
		if(evaluate==null)
			return "未找到评论信息";
		if(!evaluate.getType().equals(Status.EVALUATE_TYPE_USER))
			return "评论信息类型错误";
		FDApplySoiltest af = applySoiltestDao.findById(evaluate.getApplySoiltestId());
		if(af==null)
			return "未找到测土申请记录";
		FDSoiltestEvaluate tempReply = soiltestEvaluateDao.findOne(" where applySoiltestId='"+evaluate.getApplySoiltestId()+"' and type="+Status.EVALUATE_TYPE_ADMIN);
		if(tempReply!=null)
			return "已添加过回复";
		se.setId(UUID.randomUUID().toString());
		se.setApplySoiltestId(evaluate.getApplySoiltestId());
		se.setStarNum(0);
		se.setCreateDate(new Date());
		se.setType(Status.EVALUATE_TYPE_ADMIN);
		se.setUserId(evaluate.getUserId());
		se.setIsShow(Status.EVALUATE_ISSHOW_YES);
		soiltestEvaluateDao.add(se);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SERVE, "回复测土评论", se.getUserId());
		return null;
	}
	
	@Override
	public String delSoiltestReply(String evaluateId, String adminId){
		FDSoiltestEvaluate se = soiltestEvaluateDao.findById(evaluateId);
		if(se==null)
			return "未获取到评论信息";
		se.setIsShow(Status.EVALUATE_ISSHOW_NO);
		soiltestEvaluateDao.update(se);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SERVE, "删除测土评论", se.getUserId());
		return null;
	}
	
	//测土配肥最低值====================================================================
	@Override
	public FDSystemParam getSystemParam(){
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		return sp;
	}
	
	@Override
	public String updateMinFSValue(Double minFertilizeNum, Double minSoiltestAcreage, HttpServletRequest request, String adminId){
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		sp.setMinFertilizeNum(minFertilizeNum);
		sp.setMinSoiltestAcreage(minSoiltestAcreage);
		paramDao.update(sp);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SERVE, "设定移动配肥条件");
		return Utils.evictFrontCache(request, "param/evictParamCache");
	}
	
}
