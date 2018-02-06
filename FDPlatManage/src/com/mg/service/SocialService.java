package com.mg.service;

import javax.servlet.http.HttpServletRequest;

import com.mg.entity.FDForumCategory;
import com.mg.entity.FDMobAccount;
import com.mg.entity.FDSystemParam;



public interface SocialService {
	String addForumCategory(FDForumCategory fc, String adminId);
	String editForumCategory(FDForumCategory fc, String adminId);
	String getForumCategoryList();
	String delForumCategory(String forumCategoryId, String adminId);
	String setForumCategoryTop(String forumCategoryId);
	
	String getForumPostList(String nameTel, String categoryId, Integer page, Integer pageSize);
	String getForumPostDetail(String forumPostId);
	String delForumPost(String forumPostId, String adminId);
	String setForumPostTop(String forumPostId);
	String cancelForumPostTop(String forumPostId);
	String getForumCommentList(String postId, Integer page, Integer pageSize);
	String delForumComment(String forumCommentId);
	
	String addChatRoom(FDMobAccount ma, HttpServletRequest request, String adminId);
	FDMobAccount chatRoomDetail(String id);
	String editChatRoom(FDMobAccount ma, HttpServletRequest request);
	String delChatRoom(String id, HttpServletRequest request, String adminId);
	String getChatRoomList(String type, Integer page, Integer pageSize);
	
	FDSystemParam getServiceTel();
	String updateServiceTel(String tel, HttpServletRequest request, String adminId);

}
