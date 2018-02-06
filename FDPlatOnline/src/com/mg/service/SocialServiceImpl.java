package com.mg.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.ForumPostDao;
import com.mg.dao.UserDao;
import com.mg.entity.FDForumPost;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional 
public class SocialServiceImpl implements SocialService {

	@Autowired
	UserDao userDao;
	@Autowired
	ForumPostDao forumPostDao;
	@Autowired
	ConUtil conUtil;
	
	@Override
	public String getMemberForumPostlist(String userId, String categoryId, Integer page, Integer pageSize){
		String sql = " select fp.*,(select u.PHONE from fd_user u where u.ID=fp.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fp.USER_ID) LOGIN_NAME "
				+ " ,(select COUNT(*) from fd_forum_comment fc where fc.POST_ID=fp.ID and fc.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+") COMMENT_NUM from fd_forum_post fp ";
		String where = " where fp.USER_ID='"+userId+"' and fp.IS_SHOW="+Status.FORUMPOST_ISSHOW_YES+" ";
		if(!Utils.isEmpty(categoryId)){
			where += " and fp.CATEGORY_ID='"+categoryId+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by fp.CREATE_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		String countSql = " select count(*) COUNT from fd_forum_post fp ";
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getMemberForumPostDetail(String forumPostId, String userId){
		return conUtil.getRows(" select fp.*,(select u.phone from fd_user u where u.ID=fp.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fp.USER_ID) LOGIN_NAME "
				+ ",(select COUNT(*) from fd_forum_comment fc where fc.POST_ID=fp.ID and fc.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+") COMMENT_NUM from fd_forum_post fp where fp.ID='"+forumPostId+"' and fp.USER_ID='"+userId+"' ").toJSONString();
	}
	
	@Override
	public String addMemberForumPost(FDForumPost fp){
		fp.setId(UUID.randomUUID().toString());
		fp.setCreateDate(new Date());
		fp.setIsShow(Status.FORUMPOST_ISSHOW_YES);
		fp.setReadCount(0);
		forumPostDao.add(fp);
		return null;
	}
	
	@Override
	public String editMemberForumPost(FDForumPost fp){
		FDForumPost fp1 = forumPostDao.findOne(" where id='"+fp.getId()+"' and userId='"+fp.getUserId()+"' ");
		if(fp1==null)
			return "未获取到帖子信息";
		fp1.setCategoryId(fp.getCategoryId());
		fp1.setContent(fp.getContent());
		fp1.setTitle(fp.getTitle());
		fp1.setImagePaths(fp.getImagePaths());
		forumPostDao.update(fp1);
		return null;
	}
	
	@Override
	public String delMemberForumPost(String forumPostId, String userId){
		FDForumPost fp = forumPostDao.findOne(" where id='"+forumPostId+"' and userId='"+userId+"' ");
		if(fp==null)
			return "未获取到帖子信息";
		fp.setIsShow(Status.FORUMPOST_ISSHOW_NO);
		forumPostDao.update(fp);
		return null;
	}
	
	@Override
	public String getForumCommentList(String postId, Integer page, Integer pageSize){
		String sql = " select fc.*,(select u.phone from fd_user u where u.ID=fc.USER_ID) PHONE,(select u.LOGIN_NAME from fd_user u where u.ID=fc.USER_ID) LOGIN_NAME"
				+ " ,(select u1.phone from fd_user u1 where u1.ID=fc.TO_USER_ID) TO_PHONE,(select u1.LOGIN_NAME from fd_user u1 where u1.ID=fc.TO_USER_ID) TO_LOGIN_NAME from fd_forum_comment fc ";
		String where = " where fc.POST_ID='"+postId+"' and fc.IS_SHOW="+Status.FORUMCOMMENT_ISSHOW_YES+" ";
//		JSONObject json = conUtil.getRows(sql+where+" order by fc.CREATE_DATE asc limit "+(page-1)*pageSize+","+pageSize);
		JSONObject json = conUtil.getRows(sql+where+" order by fc.CREATE_DATE asc ");
		String countSql = " select count(*) COUNT from fd_forum_comment fc ";
		json.put("total", ((Long)conUtil.getData(countSql+where).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
}
