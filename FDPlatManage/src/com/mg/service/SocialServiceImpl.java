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
import com.mg.dao.CategoryDao;
import com.mg.dao.ForumCategoryDao;
import com.mg.dao.ForumCommentDao;
import com.mg.dao.ForumPostDao;
import com.mg.dao.GoodsDao;
import com.mg.dao.MobAccountDao;
import com.mg.dao.OperateLogDao;
import com.mg.dao.ParamDao;
import com.mg.entity.FDForumCategory;
import com.mg.entity.FDForumComment;
import com.mg.entity.FDForumPost;
import com.mg.entity.FDMobAccount;
import com.mg.entity.FDSystemParam;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional 
public class SocialServiceImpl implements SocialService {

	@Autowired
	private ConUtil conUtil;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private ParamDao paramDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private ForumCategoryDao forumCategoryDao;
	@Autowired
	private ForumPostDao forumPostDao;
	@Autowired
	private ForumCommentDao forumCommentDao;
	@Autowired
	private MobAccountDao mobAccountDao;
	@Autowired
	private OperateLogDao operateLogDao;
	
	@Override
	public String addForumCategory(FDForumCategory fc, String adminId){
		fc.setId(UUID.randomUUID().toString());
		fc.setCreateDate(new Date());
		fc.setSortDate(new Date());
		forumCategoryDao.add(fc);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SOCIAL, "添加贴吧板块", fc.getName());
		return null;
	}
	
	@Override
	public String editForumCategory(FDForumCategory fc, String adminId){
		FDForumCategory fc1 = forumCategoryDao.findById(fc.getId());
		if(fc1==null)
			return "未找到分类信息";
		fc1.setCreateDate(fc.getCreateDate());
		fc1.setName(fc.getName());
		forumCategoryDao.update(fc);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SOCIAL, "编辑贴吧板块", fc.getName());
		return null;
	}
	
	@Override
	public String getForumCategoryList(){
		return conUtil.getRows(" select * from fd_forum_category order by SORT_DATE desc ").toJSONString();
	}
	
	@Override
	public String delForumCategory(String forumCategoryId, String adminId){
		FDForumCategory fc = forumCategoryDao.findById(forumCategoryId);
		if(fc==null)
			return "未找到分类信息";
		List<FDForumPost> list = forumPostDao.findBycondition(" where categoryId='"+fc.getId()+"' ");
		if(list!=null&&list.size()>0){
			return "已有帖子添加此分类，不可删除";
		}
		String fc_name = fc.getName();
		forumCategoryDao.deleteById(forumCategoryId);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SOCIAL, "删除贴吧板块", fc_name);
		return null;
	}
	
	@Override
	public String setForumCategoryTop(String forumCategoryId){
		FDForumCategory fc = forumCategoryDao.findById(forumCategoryId);
		if(fc==null)
			return "未找到分类信息";
		fc.setSortDate(new Date());
		forumCategoryDao.update(fc);
		return null;
	}
	
	//======================================================
	@Override
	public String getForumPostList(String nameTel, String categoryId, Integer page, Integer pageSize){
		String sql = " select fp.*,(select u.phone from fd_user u where u.ID=fp.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fp.USER_ID) LOGIN_NAME "
				+ " ,(select COUNT(*) from fd_forum_comment fc where fc.POST_ID=fp.ID) COMMENT_NUM from fd_forum_post fp ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(nameTel)){
			String userSql = " select u.ID from fd_user u where u.LOGIN_NAME='"+nameTel+"' or u.PHONE='"+nameTel+"' ";
			List<Map<String,Object>> userList = conUtil.getData(userSql);
			if(userList!=null&&userList.size()>0){
				String userId = (String) userList.get(0).get("ID");
				where += " and fp.USER_ID='"+userId+"' ";
			}
		}
		if(!Utils.isEmpty(categoryId)){
			where += " and fp.CATEGORY_ID ='"+categoryId+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by fp.SORT_DATE,fp.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		String countSql = " select count(*) COUNT from fd_forum_post fp ";
		json.put("total", ((Long)conUtil.getData(countSql).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getForumPostDetail(String forumPostId){
		return conUtil.getRows(" select fp.*,(select u.phone from fd_user u where u.ID=fp.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fp.USER_ID) LOGIN_NAME "
				+ " ,(select COUNT(*) from fd_forum_comment fc where fc.POST_ID=fp.ID) COMMENT_NUM from fd_forum_post fp where fp.ID='"+forumPostId+"' ").toJSONString();
	}
	
	@Override
	public String delForumPost(String forumPostId, String adminId){
		FDForumPost fp = forumPostDao.findById(forumPostId);
		if(fp==null)
			return "获取帖子信息失败";
		fp.setIsShow(Status.FORUMPOST_ISSHOW_NO);
		String fp_title = fp.getTitle();
		forumPostDao.update(fp);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SOCIAL, "删除帖子", fp_title);
		return null;
	}
	
	@Override
	public String setForumPostTop(String forumPostId){
		FDForumPost fp = forumPostDao.findById(forumPostId);
		if(fp==null)
			return "获取帖子信息失败";
		fp.setSortDate(new Date());
		forumPostDao.update(fp);
		return null;
	}
	
	@Override
	public String cancelForumPostTop(String forumPostId){
		FDForumPost fp = forumPostDao.findById(forumPostId);
		if(fp==null)
			return "获取帖子信息失败";
		fp.setSortDate(null);
		forumPostDao.update(fp);
		return null;
	}
	
	@Override
	public String getForumCommentList(String postId, Integer page, Integer pageSize){
		String sql = " select fc.*,(select u.phone from fd_user u where u.ID=fc.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fc.USER_ID) LOGIN_NAME "
				+ " ,(select u1.phone from fd_user u1 where u1.ID=fc.TO_USER_ID) TO_PHONE,(select u1.LOGIN_NAME from fd_user u1 where u1.ID=fc.TO_USER_ID) TO_LOGIN_NAME from fd_forum_comment fc ";
		String where = " where fc.POST_ID='"+postId+"' ";
//		JSONObject json = conUtil.getRows(sql+where+" order by fc.CREATE_DATE asc limit "+(page-1)*pageSize+","+pageSize);
		JSONObject json = conUtil.getRows(sql+where+" order by fc.CREATE_DATE asc ");
		String countSql = " select count(*) COUNT from fd_forum_comment fc ";
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String delForumComment(String forumCommentId){
		FDForumComment fc = forumCommentDao.findById(forumCommentId);
		if(fc==null)
			return "获取帖子信息失败";
		fc.setIsShow(Status.FORUMPOST_ISSHOW_NO);
		forumCommentDao.update(fc);
		return null;
	}
	
	//聊天室====================================================================
	@Override
	public String addChatRoom(FDMobAccount ma, HttpServletRequest request, String adminId){
		ma.setId(UUID.randomUUID().toString());
		ma.setCreateDate(new Date());
		ma.setSortDate(new Date());
		mobAccountDao.add(ma);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SOCIAL, "添加聊天室", ma.getName());
		return Utils.evictFrontCache(request, "evictMobAccountCache?type="+ma.getType());
	}
	
	@Override
	public FDMobAccount chatRoomDetail(String id){
		FDMobAccount ma = mobAccountDao.findById(id);
		return ma;
	}
	
	@Override
	public String editChatRoom(FDMobAccount ma, HttpServletRequest request){
		FDMobAccount temp = mobAccountDao.findById(ma.getId());
		temp.setAccount(ma.getAccount());
		temp.setName(temp.getName());
		temp.setType(ma.getType());
		mobAccountDao.update(temp);
		return Utils.evictFrontCache(request, "evictMobAccountCache?type="+ma.getType());
	}
	
	@Override
	public String delChatRoom(String id, HttpServletRequest request, String adminId){
		FDMobAccount ma = mobAccountDao.findById(id);
		if(ma==null)
			return "未找到客服信息";
		String type = ma.getType();
		String ma_name = ma.getName();
		mobAccountDao.deleteById(id);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_SOCIAL, "删除聊天室", ma_name);
		return Utils.evictFrontCache(request, "evictMobAccountCache?type="+type);
	}
	
	@Override
	public String getChatRoomList(String type, Integer page, Integer pageSize){
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
	
	@Override
	public FDSystemParam getServiceTel(){
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		return sp;
	}
	
	@Override
	public String updateServiceTel(String tel, HttpServletRequest request, String adminId){
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		sp.setServiceTel(tel);
		paramDao.update(sp);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "编辑客服电话");
		return Utils.evictFrontCache(request, "param/evictParamCache");
	}
}
