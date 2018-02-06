package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDArticle;
import com.mg.service.CommonService;
import com.mg.service.OperationService;
import com.mg.util.Utils;


@Controller
@RequestMapping("/article")
public class ArticleController {

	@Autowired
	private OperationService operationService;
	@Autowired
	private CommonService commonService;
	
	@ResponseBody
	@RequestMapping(value="/addArticle")
	public String addArticle(FDArticle article, HttpServletRequest request){
		if(!Utils.isEmpty(article.getTitle()))
			article.setTitle(Utils.turnCodeWhenGet(article.getTitle(), request));
		if(!Utils.isEmpty(article.getContent()))
			article.setContent(Utils.turnCodeWhenGet(article.getContent(), request));
		if(!Utils.isEmpty(article.getType()))
			article.setType(Utils.turnCodeWhenGet(article.getType(), request));
		String r = operationService.addArticle(article, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("添加文章成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/articleDetail")
	public String articleDetail(String articleId,HttpServletRequest request){
		if(Utils.isEmpty(articleId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		FDArticle article = operationService.articleDetail(articleId);
		if(article==null)
			return Utils.getErrorStatusJson("获取信息失败").toJSONString();
		return Utils.getSuccessEntityJson(article).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/editArticle")
	public String editArticle(FDArticle article, HttpServletRequest request){
		if(Utils.isEmpty(article.getId()))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		if(!Utils.isEmpty(article.getTitle()))
			article.setTitle(Utils.turnCodeWhenGet(article.getTitle(), request));
		if(!Utils.isEmpty(article.getContent()))
			article.setContent(Utils.turnCodeWhenGet(article.getContent(), request));
		if(!Utils.isEmpty(article.getType()))
			article.setType(Utils.turnCodeWhenGet(article.getType(), request));
		String r = operationService.editArticle(article, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑文章成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getArticleList")
	public String getArticleList(String type, String status, Integer page, Integer pageSize, HttpServletRequest request){
		type = Utils.turnCodeWhenGet(type, request);
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return operationService.getArticleList(type, status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/delArticle")
	public String delArticle(String articleId, HttpServletRequest request){
		if(Utils.isEmpty(articleId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = operationService.delArticle(articleId, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除文章成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/setArticleTop")
	public String setArticleTop(String articleId, HttpServletRequest request){
		if(Utils.isEmpty(articleId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = operationService.setArticleTop(articleId, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("文章置顶成功").toJSONString();
	}
	
}
