package com.mg.service;

import javax.servlet.http.HttpServletRequest;

import com.mg.entity.FDAdv;
import com.mg.entity.FDArticle;
import com.mg.entity.FDMobAccount;
import com.mg.entity.FDNotice;


public interface OperationService {
	String addAdv(FDAdv adv, HttpServletRequest request);
	FDAdv advDetail(String advId);
	String editAdv(FDAdv adv, HttpServletRequest request);
	String getAdvList(String type, String status, Integer page, Integer pageSize);
	String delAdv(String advId, HttpServletRequest request);
	String setAdvTop(String advId, HttpServletRequest request);
	
	String addArticle(FDArticle article, HttpServletRequest request);
	FDArticle articleDetail(String articleId);
	String editArticle(FDArticle article, HttpServletRequest request);
	String getArticleList(String type, String status, Integer page, Integer pageSize);
	String delArticle(String articleId, HttpServletRequest request);
	String setArticleTop(String articleId, HttpServletRequest request);
	
	String addCustomService(FDMobAccount ma, HttpServletRequest request);
	FDMobAccount customServiceDetail(String id);
	String editCustomService(FDMobAccount ma, HttpServletRequest request);
	String delCustomService(String id, HttpServletRequest request);
	String getCumstomServiceList(String type, Integer page, Integer pageSize);
	
	String addSystemNotice(String title, String content, String endDate, String adminId);
	FDNotice systemNoticeDetail(String id);
	String editSystemNotice(String id, String title, String content, String endDate);
	String delSystemNotice(String id);
	String getSystemNoticeList(Integer page, Integer pageSize);

}
