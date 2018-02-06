package com.mg.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.AdvDao;
import com.mg.dao.ArticleDao;
import com.mg.dao.MobAccountDao;
import com.mg.dao.NoticeDao;
import com.mg.dao.OperateLogDao;
import com.mg.entity.FDAdv;
import com.mg.entity.FDArticle;
import com.mg.entity.FDMobAccount;
import com.mg.entity.FDNotice;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional 
public class OperationServiceImpl implements OperationService {

	@Autowired
	private ConUtil conUtil;
	@Autowired
	private AdvDao advDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private MobAccountDao mobAccountDao;
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private OperateLogDao operateLogDao;
	
	@Override
	public String addAdv(FDAdv adv, HttpServletRequest request){
		adv.setId(UUID.randomUUID().toString());
		adv.setCreateDate(new Date());
		adv.setSortDate(new Date());
		adv.setStatus(Status.ADV_STATUS_ON);
		advDao.add(adv);
		return Utils.evictFrontCache(request, "evictAdvCache");
	}
	
	@Override
	public FDAdv advDetail(String advId){
		FDAdv adv = advDao.findById(advId);
		return adv;
	}
	
	@Override
	public String editAdv(FDAdv adv, HttpServletRequest request){
		FDAdv temp = advDao.findById(adv.getId());
		temp.setImagePath(adv.getImagePath());
		temp.setLink(adv.getLink());
		temp.setName(adv.getName());
		temp.setTitle(adv.getTitle());
		temp.setType(adv.getType());
		temp.setSubType(adv.getSubType());
		advDao.update(temp);
		return Utils.evictFrontCache(request, "evictAdvCache");
	}
	
	@Override
	public String getAdvList(String type, String status, Integer page, Integer pageSize){
		String sql = " select * from fd_adv a ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(type)){
			where += " and a.TYPE='"+type+"' ";
		}
		if(!Utils.isEmpty(status)){
			where += " and a.STATUS='"+status+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by a.SORT_DATE,a.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		
		String count_sql = " select count(*) as COUNT from fd_adv a ";
		JSONObject count_json = conUtil.getRows(count_sql+where);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) count_json.get("rows");
		if(rows!=null){
			json.put("total", rows.get(0).get("COUNT"));
		}else{
			json.put("total",0);
		}
		json.put("page", page);
		json.put("pageSize", pageSize);
		return json.toJSONString();
	}
	
	@Override
	public String delAdv(String advId, HttpServletRequest request){
		FDAdv adv = advDao.findById(advId);
		if(adv==null)
			return "未找到广告信息";
		advDao.deleteById(advId);
		return Utils.evictFrontCache(request, "evictAdvCache");
	}
	
	@Override
	public String setAdvTop(String advId, HttpServletRequest request){
		FDAdv adv = advDao.findById(advId);
		if(adv==null)
			return "未找到广告信息";
		adv.setSortDate(new Date());
		advDao.update(adv);
		return Utils.evictFrontCache(request, "evictAdvCache");
	}
	
	//==========================================
	
	@Override
	public String addArticle(FDArticle article, HttpServletRequest request){
		article.setId(UUID.randomUUID().toString());
		article.setCreateDate(new Date());
		article.setSortDate(new Date());
		article.setReadCount(0);
		article.setStatus(Status.ADV_STATUS_ON);
		articleDao.add(article);
		return Utils.evictFrontCache(request, "evictArticleCache");
	}
	
	@Override
	public FDArticle articleDetail(String articleId){
		FDArticle article = articleDao.findById(articleId);
		return article;
	}
	
	@Override
	public String editArticle(FDArticle article, HttpServletRequest request){
		FDArticle temp = articleDao.findById(article.getId());
		temp.setTitle(article.getTitle());
		temp.setType(article.getType());
		temp.setContent(article.getContent());
		temp.setImagePath(article.getImagePath());
		articleDao.update(temp);
		return Utils.evictFrontCache(request, "evictArticleCache");
	}
	
	@Override
	public String getArticleList(String type, String status, Integer page, Integer pageSize){
		String sql = " select * from fd_article a ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(type)){
			where += " and a.TYPE='"+type+"' ";
		}
		if(!Utils.isEmpty(status)){
			where += " and a.STATUS='"+status+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by a.SORT_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		
		String count_sql = " select count(*) as COUNT from fd_article a ";
		JSONObject count_json = conUtil.getRows(count_sql+where);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) count_json.get("rows");
		if(rows!=null){
			json.put("total", rows.get(0).get("COUNT"));
		}else{
			json.put("total",0);
		}
		json.put("page", page);
		json.put("pageSize", pageSize);
		return json.toJSONString();
	}
	
	@Override
	public String delArticle(String articleId, HttpServletRequest request){
		FDArticle article = articleDao.findById(articleId);
		if(article==null)
			return "未找到文章信息";
		articleDao.deleteById(articleId);
		return Utils.evictFrontCache(request, "evictArticleCache");
	}
	
	@Override
	public String setArticleTop(String articleId, HttpServletRequest request){
		FDArticle article = articleDao.findById(articleId);
		if(article==null)
			return "未找到广告信息";
		article.setSortDate(new Date());
		articleDao.update(article);
		return Utils.evictFrontCache(request, "evictArticleCache");
	}
	
	//=================================================================================	
	@Override
	public String addCustomService(FDMobAccount ma, HttpServletRequest request){
		ma.setId(UUID.randomUUID().toString());
		ma.setCreateDate(new Date());
		ma.setSortDate(new Date());
		mobAccountDao.add(ma);
		return Utils.evictFrontCache(request, "evictMobAccountCache?type="+ma.getType());
	}
	
	@Override
	public FDMobAccount customServiceDetail(String id){
		FDMobAccount ma = mobAccountDao.findById(id);
		return ma;
	}
	
	@Override
	public String editCustomService(FDMobAccount ma, HttpServletRequest request){
		FDMobAccount temp = mobAccountDao.findById(ma.getId());
		temp.setAccount(ma.getAccount());
		temp.setName(temp.getName());
		temp.setType(ma.getType());
		return Utils.evictFrontCache(request, "evictMobAccountCache?type="+ma.getType());
	}
	
	@Override
	public String delCustomService(String id, HttpServletRequest request){
		FDMobAccount ma = mobAccountDao.findById(id);
		if(ma==null)
			return "未找到客服信息";
		String type = ma.getType();
		mobAccountDao.deleteById(id);
		return Utils.evictFrontCache(request, "evictMobAccountCache?type="+type);
	}
	
	@Override
	public String getCumstomServiceList(String type, Integer page, Integer pageSize){
		String sql = " select * from fd_mob_account a ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(type)){
			where += " and a.TYPE='"+type+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by a.SORT_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		String count_sql = " select count(*) as COUNT from fd_mob_account a ";
		JSONObject count_json = conUtil.getRows(count_sql+where);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) count_json.get("rows");
		if(rows!=null){
			json.put("total", rows.get(0).get("COUNT"));
		}else{
			json.put("total",0);
		}
		json.put("page", page);
		json.put("pageSize", pageSize);
		return json.toJSONString();
	}
	
	//==============================================================================
	@Override
	public String addSystemNotice(String title, String content, String endDate, String adminId){
		try {
			FDNotice n = new FDNotice();
			n.setId(UUID.randomUUID().toString());
			n.setTitle(title);
			n.setContent(content);
			n.setCreateDate(new Date());
			n.setEndDate(Utils.getSimpleDateFormat().parse(endDate+" 23:59:59"));
			n.setType(Status.NOTICE_TYPE_SYSTEM);
			noticeDao.add(n);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SYSTEM, "添加通知", n.getTitle());
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return "系统异常";
		}
	}
	
	@Override
	public FDNotice systemNoticeDetail(String id){
		FDNotice n = noticeDao.findById(id);
		return n;
	}
	
	@Override
	public String editSystemNotice(String id, String title, String content, String endDate){
		try {
			FDNotice n = noticeDao.findById(id);
			if(n==null)
				return "未找到通知信息";
			if(!Status.NOTICE_TYPE_SYSTEM.equals(n.getType()))
				return "非系统通知";
			n.setTitle(title);
			n.setContent(content);
			n.setCreateDate(new Date());
			n.setEndDate(Utils.getSimpleDateFormat().parse(endDate+" 23:59:59"));
			noticeDao.update(n);
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return "系统异常";
		}
	}
	
	@Override
	public String delSystemNotice(String id){
		FDNotice n = noticeDao.findById(id);
		if(n==null)
			return "未找到通知信息";
		if(Status.NOTICE_TYPE_SYSTEM.equals(n.getType()))
			return "非系统通知";
		noticeDao.deleteById(n.getId());
		return null;
	}
	
	@Override
	public String getSystemNoticeList(Integer page, Integer pageSize){
		String sql = " select * from fd_notice n ";
		JSONObject json = conUtil.getRows(sql+" order by n.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		String count_sql = " select count(*) as COUNT from fd_notice n ";
		JSONObject count_json = conUtil.getRows(count_sql);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> rows = (List<Map<String, Object>>) count_json.get("rows");
		if(rows!=null){
			json.put("total", rows.get(0).get("COUNT"));
		}else{
			json.put("total",0);
		}
		json.put("page", page);
		json.put("pageSize", pageSize);
		return json.toJSONString();
	}
	
}
