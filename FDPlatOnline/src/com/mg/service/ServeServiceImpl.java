package com.mg.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.ApplyFertilizeDao;
import com.mg.dao.ApplySoiltestDao;
import com.mg.dao.AreaDao;
import com.mg.dao.FertilizeEvaluateDao;
import com.mg.dao.SoiltestEvaluateDao;
import com.mg.entity.FDApplyFertilize;
import com.mg.entity.FDApplySoiltest;
import com.mg.entity.FDFertilizeEvaluate;
import com.mg.entity.FDSoiltestEvaluate;
import com.mg.entity.FDSystemParam;
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
	ConUtil conUtil;
	
	@Override
	public String applyFertilize(FDApplyFertilize af, FDSystemParam sp){
		if(af.getFertilizeNum()<sp.getMinFertilizeNum())
			return "低于最低配肥数量";
		af.setId(UUID.randomUUID().toString());
		af.setStatus(Status.APPLYFERTILIZE_STATUS_APPLIED);
		af.setCreateDate(new Date());
		applyFertilizeDao.add(af);
		return null;
	}
	
	@Override
	public String applyFertilizeDetail(String userId, String applyFertilizeId){
		String sql = " select af.*, (select a1.NAME from fd_area a1 where a1.ID=af.CITY) CITY_NAME, "
				+ " (select a2.NAME from fd_area a2 where a2.ID=af.AREA) AREA_NAME, "
				+ " (select a3.NAME from fd_area a3 where a3.ID=af.STREET) STREET_NAME, "
				+ " (select count(*) from fd_fertilize_evaluate fe where fe.APPLY_FERTILIZE_ID=af.ID and fe.TYPE="+Status.EVALUATE_TYPE_USER+") EVALUATE_COUNT, "
				+ " (select fe1.CONTENT from fd_fertilize_evaluate fe1 where fe1.APPLY_FERTILIZE_ID=af.ID and fe1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_CONTENT, "
				+ " (select fe1.CREATE_DATE from fd_fertilize_evaluate fe1 where fe1.APPLY_FERTILIZE_ID=af.ID and fe1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_DATE, "
				+ " (select fe2.CONTENT from fd_fertilize_evaluate fe2 where fe2.APPLY_FERTILIZE_ID=af.ID and fe2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_CONTENT, "
				+ " (select fe2.CREATE_DATE from fd_fertilize_evaluate fe2 where fe2.APPLY_FERTILIZE_ID=af.ID and fe2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_DATE "
				+ " from fd_apply_fertilize af where af.USER_ID='"+userId+"' and af.ID='"+applyFertilizeId+"' ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql);
		return json.toJSONString();
	}
	
	@Override
	public String applyFertilizeList(String userId, String status, Integer page, Integer pageSize){
		JSONObject json = new JSONObject();
		String sql = " select af.*,(select count(*) from fd_fertilize_evaluate fe where fe.APPLY_FERTILIZE_ID=af.ID and fe.TYPE="+Status.EVALUATE_TYPE_USER+") EVALUATE_COUNT "
				+ " from fd_apply_fertilize af ";
		String where = " where af.USER_ID='"+userId+"' ";
		if(!Utils.isEmpty(status)){
			where += " and af.STATUS='"+status+"' ";
		}
		json = conUtil.getRows(sql+where+" order by af.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		String countSql = " select count(*) COUNT from fd_apply_fertilize af ";
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String addFertilizeEvaluate(FDFertilizeEvaluate fe){
		FDApplyFertilize af = applyFertilizeDao.findOne(" where id='"+fe.getApplyFertilizeId()+"' and userId='"+fe.getUserId()+"' ");
		if(af==null)
			return "未找到配肥申请记录";
		if(!Status.APPLYFERTILIZE_STATUS_HANDLED.equals(af.getStatus()))
			return "当前状态不可评价";
		FDFertilizeEvaluate temp = fertilizeEvaluateDao.findOne(" where applyFertilizeId='"+fe.getApplyFertilizeId()+"' and userId='"+fe.getUserId()+"' and type="+Status.EVALUATE_TYPE_USER);
		if(temp!=null)
			return "已添加过评价";
		fe.setId(UUID.randomUUID().toString());
		fe.setStarNum(0);
		fe.setType(Status.EVALUATE_TYPE_USER);
		fe.setIsShow(Status.EVALUATE_ISSHOW_YES);
		fe.setCreateDate(new Date());
		fertilizeEvaluateDao.add(fe);
		return null;
	}
	
	@Override
	public String applySoiltest(FDApplySoiltest as, FDSystemParam sp){
		if(as.getAcreage()<sp.getMinSoiltestAcreage())
			return "低于最低测土面积";
		as.setId(UUID.randomUUID().toString());
		as.setStatus(Status.APPLYSOILTEST_STATUS_APPLIED);
		as.setCreateDate(new Date());
		applySoiltestDao.add(as);
		return null;
	}
	
	@Override
	public String applySoiltestDetail(String userId, String applySoiltestId){
		String sql = " select os.*,(select a1.NAME from fd_area a1 where a1.ID=os.CITY) CITY_NAME, "
				+ " (select a2.NAME from fd_area a2 where a2.ID=os.AREA) AREA_NAME, "
				+ " (select a3.NAME from fd_area a3 where a3.ID=os.STREET) STREET_NAME, "
				+ " (select count(*) from fd_soiltest_evaluate se where se.APPLY_SOILTEST_ID=os.ID and se.TYPE="+Status.EVALUATE_TYPE_USER+") EVALUATE_COUNT, "
				+ " (select se1.CONTENT from fd_soiltest_evaluate se1 where se1.APPLY_SOILTEST_ID=os.ID and se1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_CONTENT, "
				+ " (select se1.CREATE_DATE from fd_soiltest_evaluate se1 where se1.APPLY_SOILTEST_ID=os.ID and se1.TYPE="+Status.EVALUATE_TYPE_USER+" limit 0,1) EVALUATE_DATE, "
				+ " (select se2.CONTENT from fd_soiltest_evaluate se2 where se2.APPLY_SOILTEST_ID=os.ID and se2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_CONTENT, "
				+ " (select se2.CREATE_DATE from fd_soiltest_evaluate se2 where se2.APPLY_SOILTEST_ID=os.ID and se2.TYPE="+Status.EVALUATE_TYPE_ADMIN+" limit 0,1) REPLY_DATE "
				+ " from fd_apply_soiltest os where os.USER_ID='"+userId+"' and os.ID='"+applySoiltestId+"' ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql);
		return json.toJSONString();
	}
	
	@Override
	public String applySoiltestList(String userId, String status, Integer page, Integer pageSize){
		JSONObject json = new JSONObject();
		String sql = " select os.*,(select count(*) from fd_soiltest_evaluate se where se.APPLY_SOILTEST_ID=os.ID and se.TYPE="+Status.EVALUATE_TYPE_USER+") EVALUATE_COUNT "
				+ " from fd_apply_soiltest os ";
		String where = " where os.USER_ID='"+userId+"' ";
		if(!Utils.isEmpty(status)){
			where += " and os.STATUS='"+status+"' ";
		}
		json = conUtil.getRows(sql+where+" order by os.CREATE_DATE limit "+(page-1)*pageSize+","+pageSize);
		String countSql = " select count(*) COUNT from fd_apply_soiltest os ";
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String addSoiltestEvaluate(FDSoiltestEvaluate se){
		FDApplySoiltest as = applySoiltestDao.findOne(" where id='"+se.getApplySoiltestId()+"' and userId='"+se.getUserId()+"' ");
		if(as==null)
			return "未找到测土申请记录";
		if(!Status.APPLYFERTILIZE_STATUS_HANDLED.equals(as.getStatus()))
			return "当前状态不可评价";
		FDSoiltestEvaluate temp = soiltestEvaluateDao.findOne(" where applySoiltestId='"+se.getApplySoiltestId()+"' and userId='"+se.getUserId()+"' and type="+Status.EVALUATE_TYPE_USER);
		if(temp!=null)
			return "已添加过评价";
		se.setId(UUID.randomUUID().toString());
		se.setType(Status.EVALUATE_TYPE_USER);
		se.setStarNum(0);
		se.setIsShow(Status.EVALUATE_ISSHOW_YES);
		se.setCreateDate(new Date());
		soiltestEvaluateDao.add(se);
		return null;
	}
	
}
