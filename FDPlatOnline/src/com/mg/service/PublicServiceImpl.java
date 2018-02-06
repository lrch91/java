package com.mg.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.ArticleDao;
import com.mg.dao.CategoryDao;
import com.mg.dao.FertilizeEvaluateDao;
import com.mg.dao.ForumCommentDao;
import com.mg.dao.ForumPostDao;
import com.mg.dao.GoodsDao;
import com.mg.dao.IntegralChangeDao;
import com.mg.dao.NoticeDao;
import com.mg.dao.PhotoSpotsDao;
import com.mg.dao.SoiltestEvaluateDao;
import com.mg.dao.UserDao;
import com.mg.dao.UserLoginSessionDao;
import com.mg.entity.FDArticle;
import com.mg.entity.FDCategory;
import com.mg.entity.FDFertilizeEvaluate;
import com.mg.entity.FDForumComment;
import com.mg.entity.FDForumPost;
import com.mg.entity.FDGoods;
import com.mg.entity.FDIntegralChange;
import com.mg.entity.FDNotice;
import com.mg.entity.FDPhotoSpots;
import com.mg.entity.FDSoiltestEvaluate;
import com.mg.entity.FDUser;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional 
public class PublicServiceImpl implements PublicService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserLoginSessionDao userLoginSessionDao;
	@Autowired
	private CacheManager cacheManager;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private PhotoSpotsDao photoSpotsDao;
	@Autowired
	private IntegralChangeDao integralChangeDao;
	@Autowired
	private ForumPostDao forumPostDao;
	@Autowired
	private ForumCommentDao forumCommentDao;
	@Autowired
	private NoticeDao noticeDao;
	@Autowired
	private SoiltestEvaluateDao soiltestEvaluateDao;
	@Autowired
	private FertilizeEvaluateDao fertilizeEvaluateDao;
	@Autowired
	private ConUtil conUtil;
	
	@Override
	@Cacheable(value = "advCache", condition = "#page == 1")
	public String query_adv_list(String type, Integer page, Integer pageSize){
		try{
			String sql = "select * from fd_adv a where a.status='"+Status.ADV_STATUS_ON+"' and a.type='"+type+"' order by a.sort_date,a.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize;
			JSONObject json = conUtil.getRows(sql);
			String sql2 = " select count(*) as COUNT from fd_adv a where a.status='"+Status.ADV_STATUS_ON+"' and a.type='"+type+"' ";
			JSONObject count_json = conUtil.getRows(sql2);
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
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getErrorStatusJson("系统异常").toJSONString();
		}
	}
	
	@Override
	@CacheEvict(value="advCache", allEntries=true)
	public String evictAdvCache(){
		return null;
	}
	
	@Override
	public String query_goods_list(String categoryId, String queryStr, Integer page, Integer pageSize){
		String sql = " select g.* from fd_goods g left join fd_category c on g.category_id=c.id ";
		String where = " where g.status='"+Status.GOODS_STATUS_ON+"' ";
		if(!Utils.isEmpty(categoryId)){
			where += " and (g.category_id = '"+categoryId+"' or c.parent_id = '"+categoryId+"' ) ";
		}
		if(!Utils.isEmpty(queryStr)){
			where += " and g.query_str like '%"+queryStr+"%' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by g.up_date desc limit "+(page-1)*pageSize+","+pageSize);
		json.put("total", ((Long)conUtil.getData(" select count(*) as COUNT from fd_goods g left join fd_category c on g.category_id=c.id "+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public List<FDCategory> query_goods_category(){
		List<FDCategory> list1 = categoryDao.findBycondition(" where level=1 ");
		if(list1!=null&&list1.size()>0){
			for(FDCategory temp1:list1){
				List<FDCategory> list2 = categoryDao.findBycondition(" where parentId='"+temp1.getId()+"' ");
				temp1.setChildList(list2);
			}
		}
		return list1;
	}
	
	@Override
	public FDGoods query_goods_detail(String goodsId){
		FDGoods goods = goodsDao.findOne(" where id='"+goodsId+"' and status='"+Status.GOODS_STATUS_ON+"' ");
		return goods;
	}
	
	@Override
	@Cacheable(value = "articleCache", condition = "#page == 1")
	public String query_article_list(String type, Integer page, Integer pageSize){
		try{
			String sql = "select * from fd_article a where a.status='"+Status.ARTICLE_STATUS_ON+"' and a.type='"+type+"' order by a.sort_date desc limit "+(page-1)*pageSize+","+pageSize;
			JSONObject json = conUtil.getRows(sql);
			String sql2 = " select count(*) as COUNT from fd_article a where a.status='"+Status.ARTICLE_STATUS_ON+"' and a.type='"+type+"' ";
			JSONObject count_json = conUtil.getRows(sql2);
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
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getErrorStatusJson("系统异常").toJSONString();
		}
	}
	
	@Override
	@CacheEvict(value="articleCache", allEntries=true)
	public String evictArticleCache(){
		return null;
	}
	
	@Override
	public FDArticle query_article_detail(String articleId){
		FDArticle article = articleDao.findOne(" where id='"+articleId+"' and status='"+Status.ARTICLE_STATUS_ON+"' ");
		article.setReadCount(article.getReadCount()+1);
		articleDao.update(article);
		return article;
	}
	
	//找茬游戏========================================================
	@Override
	@Cacheable(value="gameCache")
	public String getGameList(){
		System.out.println("创建gameCache=========================");
		return conUtil.getRows(" select * from fd_photo_spots order by sort_date desc ").toJSONString();
	}
	
	@Override
	@CacheEvict(value="gameCache", allEntries=true)
	public String evictGameCache(){
		return null;
	}
	
	@Override
	public String passGame(String userId, String photoSpotsId){
		FDPhotoSpots ps = photoSpotsDao.findById(photoSpotsId);
		if(ps==null)
			return Utils.getErrorStatusJson("未找到关卡信息").toJSONString();
		FDUser user = userDao.findById(userId);
		List<FDIntegralChange> icList = integralChangeDao.findBycondition(" where userId='"+userId+"' and attachId='"+photoSpotsId+"' and changeType='"+Status.INTEGRALCHANGE_CHANGETYPE_PHOTOSPOTS+"' and remark='"+Utils.getCurrentTimeString().substring(0, 10)+"' ");
		if(icList==null||icList.size()<1){
			FDIntegralChange ic = new FDIntegralChange();
			ic.setId(UUID.randomUUID().toString());
			ic.setUserId(userId);
			ic.setChangeSum(ps.getIntegralValue());
			ic.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_PHOTOSPOTS);
			ic.setNowIntegral(user.getIntegral()+ps.getIntegralValue());
			ic.setAttachId(ps.getId());
			ic.setChangeDate(new Date());
			ic.setRemark(Utils.getCurrentTimeString().substring(0, 10));
			integralChangeDao.add(ic);
			
			user.setIntegral(user.getIntegral()+ps.getIntegralValue());
			user.setHistoryIntegral(user.getHistoryIntegral()+ps.getIntegralValue());
			userDao.update(user);
			return Utils.getSuccessEntityJson(ic.getChangeSum()).toJSONString();
		}else{
			return Utils.getErrorStatusJson("该关卡当日已通过，不再重复获得通关积分").toJSONString();
		}
	}
	
	//会员等级=========================================================
	@Override
	@Cacheable(value="classSetCache")
	public List<Map<String,Object>> getClassSet(){
		System.out.println("创建classSetCache=========================");
		JSONObject json = conUtil.getRows(" select * from fd_class_set order by level asc ");
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> csList = (List<Map<String,Object>>)json.get("rows");
		return csList;
	}
	
	@Override
	@CacheEvict(value="classSetCache", allEntries=true)
	public String evictClassSetCache(){
		return null;
	}
	
	//用户等级=========================================================
	@Override
	@Cacheable(value="gradeSetCache")
	public List<Map<String,Object>> getGradeSet(){
		System.out.println("创建gradeSetCache=========================");
		JSONObject json = conUtil.getRows(" select * from fd_grade_set order by level asc ");
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> csList = (List<Map<String,Object>>)json.get("rows");
		return csList;
	}
	
	@Override
	@CacheEvict(value="gradeSetCache", allEntries=true)
	public String evictGradeSetCache(){
		return null;
	}
	
	//=========================================================
	@Override
//	@Cacheable(value = "fertilizeEvaluateCache", condition = "#page == 1")
	public String getFertilizeEvaluateList(Integer page, Integer pageSize){
		String sql = " select fe.* from fd_fertilize_evaluate fe ";
		String where = " where fe.TYPE="+Status.EVALUATE_TYPE_USER+" and fe.IS_SHOW='"+Status.EVALUATE_ISSHOW_YES+"' ";
		String countSql = " select count(*) COUNT from fd_fertilize_evaluate fe ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql+where+"order by fe.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
//	@Cacheable(value = "soiltestEvaluateCache", condition = "#page == 1")
	public String getSoiltestEvaluateList(Integer page, Integer pageSize){
		String sql = " select se.* from fd_soiltest_evaluate se ";
		String where = " where se.TYPE="+Status.EVALUATE_TYPE_USER+" and se.IS_SHOW='"+Status.EVALUATE_ISSHOW_YES+"' ";
		String countSql = " select count(*) COUNT from fd_soiltest_evaluate se ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql+where+" order by se.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	//=================================================================
	@Override
	@Cacheable(value = "forumCategroyCache")
	public String getForumCategoryList(){
		return conUtil.getRows(" select * from fd_forum_category order by SORT_DATE desc ").toJSONString();
	}
	
	@Override
	@Cacheable(value = "forumPostCache", condition = "#page == 1")
	public String getForumPostList(String categoryId, Integer page, Integer pageSize){
		String sql = " select fp.*,(select u.phone from fd_user u where u.ID=fp.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fp.USER_ID) LOGIN_NAME "
				+ " ,(select COUNT(*) from fd_forum_comment fc where fc.POST_ID=fp.ID and fc.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+") COMMENT_NUM from fd_forum_post fp ";
		String where = " where fp.IS_SHOW="+Status.FORUMPOST_ISSHOW_YES+" ";
		if(!Utils.isEmpty(categoryId)){
			where += " and fp.CATEGORY_ID='"+categoryId+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by fp.SORT_DATE,fp.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		String countSql = " select count(*) COUNT from fd_forum_post fp ";
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getForumCommentList(String postId, Integer page, Integer pageSize){
		String sql = " select fc.*,(select u.phone from fd_user u where u.ID=fc.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fc.USER_ID) LOGIN_NAME "
				+ " ,(select u1.phone from fd_user u1 where u1.ID=fc.TO_USER_ID) TO_PHONE,(select u1.LOGIN_NAME from fd_user u1 where u1.ID=fc.TO_USER_ID) TO_LOGIN_NAME from fd_forum_comment fc ";
		String where = " where fc.POST_ID='"+postId+"' and fc.IS_SHOW="+Status.FORUMPOST_ISSHOW_YES+" ";
//		JSONObject json = conUtil.getRows(sql+where+" order by fc.CREATE_DATE asc limit "+(page-1)*pageSize+","+pageSize);
		JSONObject json = conUtil.getRows(sql+where+" order by fc.CREATE_DATE asc ");
		String countSql = " select count(*) COUNT from fd_forum_comment fc ";
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getForumPostDetail(String forumPostId){
		FDForumPost fp = forumPostDao.findById(forumPostId);
		fp.setReadCount(fp.getReadCount()+1);
		forumPostDao.update(fp);
		String sql = " select fp.*,(select u.phone from fd_user u where u.ID=fp.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fp.USER_ID) LOGIN_NAME "
				+ " ,(select COUNT(*) from fd_forum_comment fc where fc.POST_ID=fp.ID and fc.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+") COMMENT_NUM from fd_forum_post fp where fp.IS_SHOW="+Status.FORUMPOST_ISSHOW_YES+" and fp.ID='"+forumPostId+"' ";
		JSONObject json = conUtil.getRows(sql);
		return json.toJSONString();
	}
	
	@Override
	public String addForumComment(FDForumComment fc){
		FDForumPost fp = forumPostDao.findById(fc.getPostId());
		if(fp==null)
			return "未找到帖子信息";
		
		fc.setId(UUID.randomUUID().toString());
		fc.setCreateDate(new Date());
		fc.setIsShow(Status.FORUMCOMMENT_ISSHOW_YES);
		forumCommentDao.add(fc);
		
		FDNotice n = new FDNotice();
		n.setId(UUID.randomUUID().toString());
		if(!Utils.isEmpty(fc.getToUserId())){
			n.setUserId(fc.getToUserId());
		}else{
			n.setUserId(fp.getUserId());
		}
		n.setAttachId(fc.getId());
		n.setCreateDate(new Date());
		n.setType(Status.NOTICE_TYPE_FORUMCOMMENT);
		n.setStatus(Status.NOTICE_STATUS_UNREAD);
		noticeDao.add(n);
		return null;
	}
	
	//=========================================================
	@Override
	@Cacheable(value = "mobAccountCache")
	public String getMobAccountList(String type){
		String sql = " select * from fd_mob_account ";
		if(!Utils.isEmpty(type)){
			sql += " where type='"+type+"' ";
		}
		sql += " order by SORT_DATE desc  ";
		return conUtil.getRows(sql).toJSONString();
	}
	
	@Override
	@CacheEvict(value="mobAccountCache")
	public String evictMobAccountCache(String type){
		return null;
	}
	
	//==========================================================
	@Override
	public String getNoticeData(String userId){
		JSONObject json = new JSONObject();
		String sql1 = " select n.* from fd_notice n where n.TYPE='"+Status.NOTICE_TYPE_SYSTEM+"' and n.CREATE_DATE<='"+Utils.getCurrentTimeString()+"' and n.END_DATE>='"+Utils.getCurrentTimeString()+"' order by n.CREATE_DATE desc limit 0,1 ";
		String sql2 = " select count(*) COUNT from fd_notice where TYPE='"+Status.NOTICE_TYPE_SYSTEM+"' and CREATE_DATE<='"+Utils.getCurrentTimeString()+"' and END_DATE>='"+Utils.getCurrentTimeString()+"' ";
		String sql3 = " select n.*,(select u.PHONE from fd_forum_comment c2 left join fd_user u on u.ID=c2.USER_ID where c2.ID=n.ATTACH_ID ) SENDER_PHONE from fd_notice n where n.TYPE='"+Status.NOTICE_TYPE_FORUMCOMMENT+"' and n.STATUS='"+Status.NOTICE_STATUS_UNREAD+"' order by n.CREATE_DATE desc limit 0,1 ";
		String sql4 = " select count(*) COUNT from fd_notice where TYPE='"+Status.NOTICE_TYPE_FORUMCOMMENT+"' and STATUS='"+Status.NOTICE_STATUS_UNREAD+"' ";
		List<Map<String,Object>> data1 = conUtil.getData(sql1);
		if(data1!=null&&data1.size()>0){
			json.put("NOTICE_SYSTEM", data1.get(0));
		}else{
			json.put("NOTICE_SYSTEM", null);
		}
		json.put("NOTICE_SYSTEM_NUM", ((Long)conUtil.getData(sql2).get(0).get("COUNT")).intValue());
		List<Map<String,Object>> data3 = conUtil.getData(sql3);
		if(data3!=null&&data3.size()>0){
			json.put("NOTICE_FORUMCOMMENT", data3.get(0));
		}else{
			json.put("NOTICE_FORUMCOMMENT", null);
		}
		json.put("NOTICE_FORUMCOMMENT_NUM", ((Long)conUtil.getData(sql4).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getTypeNoticeList(String userId, String type){
		String sql = " select n.*,(select u.PHONE from fd_forum_comment c2 left join fd_user u on u.ID=c2.USER_ID where c2.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c2.ID=n.ATTACH_ID ) SENDER_PHONE, "
				+ " (select c1.CONTENT from fd_forum_comment c1 where c1.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c1.ID=n.ATTACH_ID ) COMMENT_CONTENT, "
				+ " (select p.TITLE from fd_forum_comment c left join fd_forum_post p on c.POST_ID=p.ID where c.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c.ID=n.ATTACH_ID ) POST_TITLE "
				+ " from fd_notice n ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(type)){
			where += " and n.TYPE='"+type+"' ";
		}
		if(Status.NOTICE_TYPE_SYSTEM.equals(type)){
			where += " and n.CREATE_DATE<='"+Utils.getCurrentTimeString()+"' and n.END_DATE>='"+Utils.getCurrentTimeString()+"' ";
		}else if(Status.NOTICE_TYPE_FORUMCOMMENT.equals(type)){
			where += " and n.USER_ID='"+userId+"' ";
		}
		String countSql = " select count(*) COUNT from fd_notice n ";
		JSONObject json = conUtil.getRows(sql+where+" order by n.CREATE_DATE desc  ");
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getNoticeDetail(String noticeId, String userId){
		FDNotice n = noticeDao.findOne(" where id='"+noticeId+"' and userId='"+userId+"' ");
		if(n!=null){
			n.setStatus(Status.NOTICE_STATUS_READED);
			noticeDao.update(n);
		}
		String sql = " select n.*,(select u.PHONE from fd_forum_comment c2 left join fd_user u on u.ID=c2.USER_ID where c2.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c2.ID=n.ATTACH_ID ) SENDER_PHONE, "
				+ " (select c1.CONTENT from fd_forum_comment c1 where c1.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c1.ID=n.ATTACH_ID ) COMMENT_CONTENT, "
				+ " (select c1.USER_ID from fd_forum_comment c1 where c1.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c1.ID=n.ATTACH_ID ) COMMENTER_ID, "
				+ " (select p.ID from fd_forum_comment c left join fd_forum_post p on c.POST_ID=p.ID where c.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c.ID=n.ATTACH_ID ) POST_ID, "
				+ " (select p.TITLE from fd_forum_comment c left join fd_forum_post p on c.POST_ID=p.ID where c.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" and c.ID=n.ATTACH_ID ) POST_TITLE "
//				+ " from fd_notice n where n.ID='"+noticeId+"' and (n.USER_ID='"+userId+"' or (n.CREATE_DATE<='"+Utils.getCurrentTimeString()+"' and n.END_DATE>='"+Utils.getCurrentTimeString()+"')) ";
				+ " from fd_notice n where n.ID='"+noticeId+"' ";
		return conUtil.getRows(sql).toJSONString();
	}
	
	//服务评价点赞======================================================
	@Override
	public String addEvaluatePraise(String id){
		FDSoiltestEvaluate se = soiltestEvaluateDao.findById(id);
		if(se!=null){
			se.setStarNum(se.getStarNum()+1);
		}
		FDFertilizeEvaluate fe = fertilizeEvaluateDao.findById(id);
		if(fe!=null){
			fe.setStarNum(fe.getStarNum()+1);
		}
		return null;
	}
	
	//用户等级及用户名
	@Override
	@Cacheable(value = "nameAndClassCache", key = "#phone")
	public String getUserNameAndClass(String phone, List<Map<String, Object>> csList){
		JSONObject json = new JSONObject();
		FDUser u = userDao.findOne(" where phone = '"+phone+"' ");
		if(u==null)
			return Utils.getErrorStatusJson("未找到用户信息").toJSONString();
		json.put("phone", phone);
		json.put("loginName", u.getLoginName()!=null?u.getLoginName():"");
		Integer memberLevelTag = 0;
		String memberLevelName = "";
		if(Status.USER_ISMEMBER_YES.equals(u.getIsMember())){
			if(csList!=null&&csList.size()>0){
				Double historyBalance = u.getHistoryBalance();
				for(int i=0;i<csList.size();i++){
					if((int)csList.get(i).get("LEVEL")==u.getManualMemberLevel()){
						historyBalance += (int)csList.get(i).get("VALUE");
					}
				}
				for(int i=0;i<csList.size();i++){
					if(historyBalance>=(int)csList.get(i).get("VALUE")){
						memberLevelTag++;
					}
				}
				json.put("memberLevel",memberLevelTag);
				for(int i=0;i<csList.size();i++){
					if(memberLevelTag==(int)csList.get(i).get("LEVEL")){
						memberLevelName = (String)csList.get(i).get("NAME");
					}
				}
				json.put("memberLevelName",memberLevelName);
			}
		}
		return json.toJSONString();
	}
	
	@Override
	public String getAllClassSets(){
		JSONObject json = conUtil.getRows(" select * from fd_class_set order by level desc ");
		return json.toJSONString();
	}
}
