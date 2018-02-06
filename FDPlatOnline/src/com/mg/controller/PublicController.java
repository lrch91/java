package com.mg.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.CacheManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDArticle;
import com.mg.entity.FDCategory;
import com.mg.entity.FDForumComment;
import com.mg.entity.FDGoods;
import com.mg.service.CommonService;
import com.mg.service.PublicService;
import com.mg.util.Pager;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
public class PublicController {
	
	@Autowired
	private PublicService publicService;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CacheManager cacheManager;
	
	@ResponseBody
	@RequestMapping(value="/query_adv_list")
	public String query_adv_list(String type, Integer page, Integer pageSize){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return publicService.query_adv_list(type, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/evictAdvCache")
	public String evictAdvCache(){
		String r = publicService.evictAdvCache();
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("清除广告缓存成功").toJSONString();
	} 
	
	//===============================================
	@ResponseBody
	@RequestMapping(value="/query_goods_list")
	public String query_goods_list(String categoryId, String queryStr, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		queryStr = Utils.turnCodeWhenGet(queryStr, request);
		return publicService.query_goods_list(categoryId, queryStr, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/query_goods_detail")
	public String query_goods_detail(String goodsId, HttpServletRequest request){
		FDGoods goods =  publicService.query_goods_detail(goodsId);
		if(goods==null)
			return Utils.getErrorStatusJson("获取商品信息失败").toJSONString();
		return Utils.getSuccessEntityJson(goods).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/query_goods_category")
	public String query_goods_category(){
		List<FDCategory> list = publicService.query_goods_category();
		return Utils.getSuccessEntityJson(list).toJSONString();
	}
	
	//==========================================
	@ResponseBody
	@RequestMapping(value="/query_article_list")
	public String query_article_list(String type, Integer page, Integer pageSize, HttpServletRequest request){
		type = Utils.turnCodeWhenGet(type, request);
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return publicService.query_article_list(type, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/evictArticleCache")
	public String evictArticleCache(){
		String r = publicService.evictArticleCache();
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("清除文章缓存成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/query_article_detail")
	public String query_article_detail(String articleId, HttpServletRequest request){
		FDArticle article =  publicService.query_article_detail(articleId);
		if(article==null)
			return Utils.getErrorStatusJson("获取文章信息失败").toJSONString();
		return Utils.getSuccessEntityJson(article).toJSONString();
	}
	
	//=======================
	@ResponseBody
	@RequestMapping("/getGameList")
	public String getGameList(HttpServletRequest request){
		return publicService.getGameList();
	} 
	
	@ResponseBody
	@RequestMapping("/evictGameCache")
	public String evictGameCache(){
		String r = publicService.evictGameCache();
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("清除游戏缓存成功").toJSONString();
	} 
	
	@ResponseBody
	@RequestMapping("/passGame")
	public String passGame(String photoSpotsId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return publicService.passGame(ua.getId(), photoSpotsId);
	} 
	
	//=======================
	@ResponseBody
	@RequestMapping("/evictClassSetCache")
	public String evictClassSetCache(){
		String r = publicService.evictClassSetCache();
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("清除会员等级信息缓存成功").toJSONString();
	} 
	
	@ResponseBody
	@RequestMapping("/evictGradeSetCache")
	public String evictGradeSetCache(){
		String r = publicService.evictGradeSetCache();
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("清除用户等级信息缓存成功").toJSONString();
	}
	
	//=======================
	@ResponseBody
	@RequestMapping("/getFertilizeEvaluateList")
	public String getFertilizeEvaluateList(Integer page, Integer pageSize){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return publicService.getFertilizeEvaluateList(page, pageSize);
	} 
	
	@ResponseBody
	@RequestMapping("/getSoiltestEvaluateList")
	public String getSoiltestEvaluateList(Integer page, Integer pageSize){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return publicService.getSoiltestEvaluateList(page, pageSize);
	} 
	
	//=============================
	@ResponseBody
	@RequestMapping("/getForumCategoryList")
	public String getForumCategoryList(){
		return publicService.getForumCategoryList();
	}
	
	@ResponseBody
	@RequestMapping("/getForumPostList")
	public String getForumPostList(String categoryId, Integer page, Integer pageSize){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = Pager.PAGE_SIZE;
		return publicService.getForumPostList(categoryId, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/getForumCommentList")
	public String getForumCommentList(String postId, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		return publicService.getForumCommentList(postId, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/getForumPostDetail")
	public String getForumPostDetail(String forumPostId){
		return publicService.getForumPostDetail(forumPostId);
	}
	
	@ResponseBody
	@RequestMapping(value="/addForumComment")
	public String addForumComment(FDForumComment fc, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		fc.setUserId(ua.getId());
		if(Utils.isEmpty(fc.getPostId()))
			return Utils.getErrorStatusJson("帖子ID为空").toJSONString();
		if(Utils.isEmpty(fc.getContent()))
			return Utils.getErrorStatusJson("回复内容为空").toJSONString();
		fc.setContent(Utils.turnCodeWhenGet(fc.getContent(), request));
		if(Utils.isEmpty(fc.getToUserId()))
			fc.setToUserId("");
		String r =  publicService.addForumComment(fc);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("回复成功").toJSONString();
	}
	
	//======================================
	@ResponseBody
	@RequestMapping("/getMobAccountList")
	public String getMobAccountList(String type, HttpServletRequest request){
		return publicService.getMobAccountList(type);
	} 
	
	@ResponseBody
	@RequestMapping("/evictMobAccountCache")
	public String evictMobAccountCache(String type){
		String r = publicService.evictMobAccountCache(type);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("清除客服数据缓存成功").toJSONString();
	} 
	
	//=======================================
	@ResponseBody
	@RequestMapping("/getNoticeData")
	public String getNoticeData(HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return publicService.getNoticeData(ua.getId());
	} 
	
	@ResponseBody
	@RequestMapping("/getTypeNoticeList")
	public String getTypeNoticeList(String type, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return publicService.getTypeNoticeList(ua.getId(), type);
	}
	
	@ResponseBody
	@RequestMapping("/getNoticeDetail")
	public String getNoticeDetail(String noticeId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return publicService.getNoticeDetail(noticeId, ua.getId());
	} 
	
	@ResponseBody
	@RequestMapping("/addEvaluatePraise")
	public  String addEvaluatePraise(String id){
		String r = publicService.addEvaluatePraise(id);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("点赞成功").toJSONString();
	} 
	
	@ResponseBody
	@RequestMapping(value="/getUserNameAndClass")
	public String getUserNameAndClass(String phone){
		if(Utils.isEmpty(phone))
			return Utils.getErrorStatusJson("手机号为空").toJSONString();
		return publicService.getUserNameAndClass(phone, publicService.getClassSet());
	}
	
	//获取会员所有等级信息==============
	@ResponseBody
	@RequestMapping(value="/getAllClassSets")
	public String getAllClassSets(){
		return publicService.getAllClassSets();
	}
	
}
