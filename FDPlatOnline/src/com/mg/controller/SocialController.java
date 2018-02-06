package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDForumPost;
import com.mg.service.CommonService;
import com.mg.service.SocialService;
import com.mg.util.Pager;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;


@Controller
@RequestMapping(value="/social")
public class SocialController {

	@Autowired
	private CommonService commonService;
	@Autowired
	private SocialService socialService;
	
	@ResponseBody
	@RequestMapping(value="/getMemberForumPostlist")
	public String getMemberForumPostlist(String categoryId, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = Pager.PAGE_SIZE;
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return socialService.getMemberForumPostlist(ua.getId(), categoryId, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping(value="/getMemberForumPostDetail")
	public String getMemberForumPostDetail(String forumPostId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		return socialService.getMemberForumPostDetail(forumPostId, ua.getId());
	}
	
	@ResponseBody
	@RequestMapping(value="/addMemberForumPost")
	public String addMemberForumPost(FDForumPost fp, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		fp.setUserId(ua.getId());
		if(Utils.isEmpty(fp.getTitle()))
			return Utils.getErrorStatusJson("标题为空").toJSONString();
		fp.setTitle(Utils.turnCodeWhenGet(fp.getTitle(), request));
		if(Utils.isEmpty(fp.getContent()))
			return Utils.getErrorStatusJson("内容为空").toJSONString();
		fp.setContent(Utils.turnCodeWhenGet(fp.getContent(), request));
		if(Utils.isEmpty(fp.getCategoryId()))
			return Utils.getErrorStatusJson("分类为空").toJSONString();
		String r =  socialService.addMemberForumPost(fp);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("发布帖子成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/editMemberForumPost")
	public String editMemberForumPost(FDForumPost fp, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		fp.setUserId(ua.getId());
		if(Utils.isEmpty(fp.getId()))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		if(Utils.isEmpty(fp.getTitle()))
			return Utils.getErrorStatusJson("标题为空").toJSONString();
		fp.setTitle(Utils.turnCodeWhenGet(fp.getTitle(), request));
		if(Utils.isEmpty(fp.getContent()))
			return Utils.getErrorStatusJson("内容为空").toJSONString();
		fp.setContent(Utils.turnCodeWhenGet(fp.getContent(), request));
		if(Utils.isEmpty(fp.getCategoryId()))
			return Utils.getErrorStatusJson("分类为空").toJSONString();
		String r =  socialService.editMemberForumPost(fp);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("编辑帖子成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/delMemberForumPost")
	public String delMemberForumPost(String forumPostId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(ua==null)
			return Utils.getErrorStatusJson("unLogin").toJSONString();
		if(Utils.isEmpty(forumPostId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r =  socialService.delMemberForumPost(forumPostId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessEntityJson("删除帖子成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getForumCommentList")
	public String getForumCommentList(String postId, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		return socialService.getForumCommentList(postId, page, pageSize);
	}
	
}
