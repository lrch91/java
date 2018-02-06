
package com.mg.service;

import java.util.List;
import java.util.Map;

import com.mg.entity.FDArticle;
import com.mg.entity.FDCategory;
import com.mg.entity.FDForumComment;
import com.mg.entity.FDGoods;




public interface PublicService {
	String query_adv_list(String type, Integer page, Integer pageSize);
	String evictAdvCache();
	
	String query_goods_list(String categoryId, String queryStr, Integer page, Integer pageSize);
	List<FDCategory> query_goods_category();
	FDGoods query_goods_detail(String goodsId);
	
	String query_article_list(String type, Integer page, Integer pageSize);
	String evictArticleCache();
	FDArticle query_article_detail(String articleId);
	
	String getGameList();
	String evictGameCache();
	String passGame(String userId, String photoSpotsId);
	
	List<Map<String,Object>> getClassSet();
	String evictClassSetCache();
	
	List<Map<String,Object>> getGradeSet();
	String evictGradeSetCache();
	
	String getFertilizeEvaluateList(Integer page, Integer pageSize);
	String getSoiltestEvaluateList(Integer page, Integer pageSize);
	
	String getForumCategoryList();
	String getForumPostList(String categoryId, Integer page, Integer pageSize);
	String getForumCommentList(String postId, Integer page, Integer pageSize);
	String getForumPostDetail(String forumPostId);
	String addForumComment(FDForumComment fc);
	
	String getMobAccountList(String type);
	String evictMobAccountCache(String type);
	
	String getNoticeData(String userId);
	String getTypeNoticeList(String userId, String type);
	String getNoticeDetail(String noticeId, String userId);
	
	String addEvaluatePraise(String id);
	
	String getUserNameAndClass(String tel, List<Map<String, Object>> csList);
	
	String getAllClassSets();
}
