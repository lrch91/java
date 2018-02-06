package com.mg.service;

import com.mg.entity.FDForumPost;




public interface SocialService {
	String getMemberForumPostlist(String userId, String categoryId, Integer page, Integer pageSize);
	String getMemberForumPostDetail(String forumPostId, String userId);
	String addMemberForumPost(FDForumPost fp);
	String editMemberForumPost(FDForumPost fp);
	String delMemberForumPost(String forumPostId, String userId);
	String getForumCommentList(String postId, Integer page, Integer pageSize);
}
