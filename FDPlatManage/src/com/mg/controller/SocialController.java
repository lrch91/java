package com.mg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDForumCategory;
import com.mg.entity.FDMobAccount;
import com.mg.entity.FDSystemParam;
import com.mg.service.CommonService;
import com.mg.service.OperationService;
import com.mg.service.SocialService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/social")
public class SocialController {
	@Autowired
	SocialService socialService;
	@Autowired
	OperationService operationService;
	@Autowired
	CommonService commonService;
	
	//贴吧分类
	@ResponseBody
	@RequestMapping("/addForumCategory")
	public String addForumCategory(FDForumCategory fc, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(fc.getName()))
			return Utils.getErrorStatusJson("分类名称为空").toJSONString();
		fc.setName(Utils.turnCodeWhenGet(fc.getName(), request));
		String r = socialService.addForumCategory(fc, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson("添加分类失败").toJSONString();
		return Utils.getSuccessStatusJson("添加分类成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/editForumCategory")
	public String editForumCategory(FDForumCategory fc, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(fc.getId()))
			return Utils.getErrorStatusJson("分类ID为空").toJSONString();
		fc.setName(Utils.turnCodeWhenGet(fc.getName(), request));
		String r = socialService.editForumCategory(fc, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson("修改分类失败").toJSONString();
		return Utils.getSuccessStatusJson("修改分类成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getForumCategoryList")
	public String getForumCategoryList(){
		return socialService.getForumCategoryList();
	}
	
	@ResponseBody
	@RequestMapping("/delForumCategory")
	public String delForumCategory(String forumCategoryId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(forumCategoryId))
			return Utils.getErrorStatusJson("分类ID为空").toJSONString();
		String r = socialService.delForumCategory(forumCategoryId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson("删除分类失败").toJSONString();
		return Utils.getSuccessStatusJson("删除分类成功").toJSONString();
	}
	
//	@ResponseBody
//	@RequestMapping("/setForumCategoryTop")
//	public String setForumCategoryTop(String forumCategoryId){
//		if(Utils.isEmpty(forumCategoryId))
//			return Utils.getErrorStatusJson("分类ID为空").toJSONString();
//		String r = socialService.setForumCategoryTop(forumCategoryId);
//		if(r!=null)
//			return Utils.getErrorStatusJson("分类置顶失败").toJSONString();
//		return Utils.getSuccessStatusJson("分类置顶成功").toJSONString();
//	}
	
	//帖子
	@ResponseBody
	@RequestMapping("/getForumPostList")
	public String getForumPostList(String nameTel, String categoryId, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page = 1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		nameTel = Utils.turnCodeWhenGet(nameTel, request);
		return socialService.getForumPostList(nameTel, categoryId, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/getForumPostDetail")
	public String getForumPostDetail(String forumPostId){
		return socialService.getForumPostDetail(forumPostId);
	}
	
	@ResponseBody
	@RequestMapping("/delForumPost")
	public String delForumPost(String forumPostId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(forumPostId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = socialService.delForumPost(forumPostId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson("删除帖子失败").toJSONString();
		return Utils.getSuccessStatusJson("删除帖子成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/setForumPostTop")
	public String setForumPostTop(String forumPostId){
		if(Utils.isEmpty(forumPostId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = socialService.setForumPostTop(forumPostId);
		if(r!=null)
			return Utils.getErrorStatusJson("帖子置顶失败").toJSONString();
		return Utils.getSuccessStatusJson("帖子成置顶功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/cancelForumPostTop")
	public String cancelForumPostTop(String forumPostId){
		if(Utils.isEmpty(forumPostId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = socialService.cancelForumPostTop(forumPostId);
		if(r!=null)
			return Utils.getErrorStatusJson("取消帖子置顶失败").toJSONString();
		return Utils.getSuccessStatusJson("取消帖子置顶成功").toJSONString();
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
	
	@ResponseBody
	@RequestMapping("/delForumComment")
	public String delForumComment(String forumCommentId){
		if(Utils.isEmpty(forumCommentId))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = socialService.delForumComment(forumCommentId);
		if(r!=null)
			return Utils.getErrorStatusJson("删除回复失败").toJSONString();
		return Utils.getSuccessStatusJson("删除回复成功").toJSONString();
	}
	
/*	//聊天室=======================================
	@ResponseBody
	@RequestMapping(value="/addChatRoom")
	public String addChatRoom(FDMobAccount ma, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(ma.getAccount()))
			return Utils.getErrorStatusJson("账号为空").toJSONString();
		if(Utils.isEmpty(ma.getName()))
			return Utils.getErrorStatusJson("名称为空").toJSONString();
		if(Utils.isEmpty(ma.getType()))
			return Utils.getErrorStatusJson("分类为空").toJSONString();
		ma.setAccount(Utils.turnCodeWhenGet(ma.getAccount(), request));
		ma.setName(Utils.turnCodeWhenGet(ma.getName(), request));
		String r = socialService.addChatRoom(ma, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除客服成功").toJSONString();
	}
	
//	@ResponseBody
//	@RequestMapping(value="/chatRoomDetail")
//	public String chatRoomDetail(String id){
//		if(Utils.isEmpty(id))
//			return Utils.getErrorStatusJson("ID为空").toJSONString();
//		FDMobAccount ma = socialService.chatRoomDetail(id);
//		return Utils.getSuccessEntityJson(ma).toJSONString();
//	}
	
//	@ResponseBody
//	@RequestMapping(value="/editChatRoom")
//	public String editChatRoom(FDMobAccount ma, HttpServletRequest request){
//		if(Utils.isEmpty(ma.getId()))
//			return Utils.getErrorStatusJson("ID为空").toJSONString();
//		if(Utils.isEmpty(ma.getAccount()))
//			return Utils.getErrorStatusJson("账号为空").toJSONString();
//		if(Utils.isEmpty(ma.getName()))
//			return Utils.getErrorStatusJson("名称为空").toJSONString();
//		if(Utils.isEmpty(ma.getType()))
//			return Utils.getErrorStatusJson("分类为空").toJSONString();
//		ma.setAccount(Utils.turnCodeWhenGet(ma.getAccount(), request));
//		ma.setName(Utils.turnCodeWhenGet(ma.getName(), request));
//		String r = socialService.editChatRoom(ma, request);
//		if(r!=null)
//			return Utils.getErrorStatusJson(r).toJSONString();
//		return Utils.getSuccessStatusJson("删除客服成功").toJSONString();
//	}
	
	@ResponseBody
	@RequestMapping(value="/delChatRoom")
	public String delChatRoom(String id, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(id))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = socialService.delChatRoom(id, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除聊天室成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getChatRoomList")
	public String getChatRoomList(String type, Integer page, Integer pageSize){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return socialService.getChatRoomList(type, page, pageSize);
	}*/
	
	//===================================
	//客服操作
	@ResponseBody
	@RequestMapping(value="/addCustomService")
	public String addCustomService(FDMobAccount ma, HttpServletRequest request){
		if(Utils.isEmpty(ma.getAccount()))
			return Utils.getErrorStatusJson("账号为空").toJSONString();
		if(Utils.isEmpty(ma.getName()))
			return Utils.getErrorStatusJson("名称为空").toJSONString();
		if(Utils.isEmpty(ma.getType()))
			return Utils.getErrorStatusJson("分类为空").toJSONString();
		ma.setAccount(Utils.turnCodeWhenGet(ma.getAccount(), request));
		ma.setName(Utils.turnCodeWhenGet(ma.getName(), request));
		String r=operationService.addCustomService(ma, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("添加成功").toJSONString();
	}
	
	/*@ResponseBody
	@RequestMapping(value="/customServiceDetail")
	public String customServiceDetail(String id){
		if(Utils.isEmpty(id))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		FDMobAccount ma = operationService.customServiceDetail(id);
		return Utils.getSuccessEntityJson(ma).toJSONString();
	}*/
	
	/*@ResponseBody
	@RequestMapping(value="/editCustomService")
	public String editCustomService(FDMobAccount ma, HttpServletRequest request){
		if(Utils.isEmpty(ma.getId()))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		if(Utils.isEmpty(ma.getAccount()))
			return Utils.getErrorStatusJson("账号为空").toJSONString();
		if(Utils.isEmpty(ma.getName()))
			return Utils.getErrorStatusJson("名称为空").toJSONString();
		if(Utils.isEmpty(ma.getType()))
			return Utils.getErrorStatusJson("分类为空").toJSONString();
		ma.setAccount(Utils.turnCodeWhenGet(ma.getAccount(), request));
		ma.setName(Utils.turnCodeWhenGet(ma.getName(), request));
		String r=operationService.editCustomService(ma, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑成功").toJSONString();
	}*/
	
	@ResponseBody
	@RequestMapping(value="/delCustomService")
	public String delCustomService(String id, HttpServletRequest request){
		if(Utils.isEmpty(id))
			return Utils.getErrorStatusJson("ID为空").toJSONString();
		String r = operationService.delCustomService(id, request);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除客服成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/getCustomServiceList")
	public String getCumstomServiceList(String type, Integer page, Integer pageSize){
		if(page==null||page<1){
			page = 1;
		}
		if(pageSize==null||pageSize<1){
			pageSize = 15;
		}
		return operationService.getCumstomServiceList(type, page, pageSize);
	}
	
	//=客服电话=================================
	@ResponseBody
	@RequestMapping(value="/getServiceTel")
	public String getServiceTel(){
		FDSystemParam sp = socialService.getServiceTel();
		return Utils.getSuccessEntityJson(sp).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value="/updateServiceTel")
	public String getServiceTel(String tel, HttpServletRequest request){
		if(Utils.isEmpty(tel))
			return Utils.getErrorStatusJson("客服电话为空").toJSONString();
		UserAuthVo ua = commonService.userAuth(request);
		String r = socialService.updateServiceTel(tel, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改成功").toJSONString();
	}
}
