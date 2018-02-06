package com.mg.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mg.entity.FDCategory;
import com.mg.entity.FDGoods;
import com.mg.service.CommonService;
import com.mg.service.GoodsService;
import com.mg.service.TradeService;
import com.mg.util.Utils;
import com.mg.vo.UserAuthVo;

@Controller
@RequestMapping("/goods")
public class GoodsController {
	@Autowired
	CommonService commonService;
	@Autowired
	GoodsService goodsService;
	@Autowired
	TradeService tradeService;
	
	//商品管理
	@ResponseBody
	@RequestMapping("/addGoods")
	public String addGoods(FDGoods g, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(g.getGoods_name())){
			Utils.getErrorStatusJson("商品名称为空").toJSONString();
		}
		g.setGoods_name(Utils.turnCodeWhenGet(g.getGoods_name(), request));
		
		if(!Utils.isEmpty(g.getGoods_number())){
			g.setGoods_number(Utils.turnCodeWhenGet(g.getGoods_number(), request));
		}
		if(Utils.isEmpty(g.getCategory_id())){
			return Utils.getErrorStatusJson("商品分类为空").toJSONString();
		}
		if(g.getReference_price()==null){
			return Utils.getErrorStatusJson("商品价格为空").toJSONString();
		}
		if(Utils.isEmpty(g.getBrief())){
			return Utils.getErrorStatusJson("商品简介为空").toJSONString();
		}
		if(Utils.isEmpty(g.getImagePaths())){
			return Utils.getErrorStatusJson("商品图片为空").toJSONString();
		}
		if(Utils.isEmpty(g.getDetail())){
			return Utils.getErrorStatusJson("商品详情为空").toJSONString();
		}
		String r = goodsService.addGoods(g, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("添加商品成功").toJSONString();
	}
	
	//商品管理
	@ResponseBody
	@RequestMapping("/editGoods")
	public String editGoods(FDGoods g, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(g.getId())){
			Utils.getErrorStatusJson("ID为空").toJSONString();
		}
		if(Utils.isEmpty(g.getGoods_name())){
			Utils.getErrorStatusJson("商品名称为空").toJSONString();
		}
		g.setGoods_name(Utils.turnCodeWhenGet(g.getGoods_name(), request));
		
		if(!Utils.isEmpty(g.getGoods_number())){
			g.setGoods_number(Utils.turnCodeWhenGet(g.getGoods_number(), request));
		}
		if(Utils.isEmpty(g.getCategory_id())){
			return Utils.getErrorStatusJson("商品分类为空").toJSONString();
		}
		if(g.getReference_price()==null){
			return Utils.getErrorStatusJson("商品价格为空").toJSONString();
		}
		if(Utils.isEmpty(g.getBrief())){
			return Utils.getErrorStatusJson("商品简介为空").toJSONString();
		}
		if(Utils.isEmpty(g.getImagePaths())){
			return Utils.getErrorStatusJson("商品图片为空").toJSONString();
		}
		if(Utils.isEmpty(g.getDetail())){
			return Utils.getErrorStatusJson("商品详情为空").toJSONString();
		}
		String r = goodsService.editGoods(g, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑商品成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getGoodsDetail")
	public String getGoodsDetail(String goodsId){
		if(Utils.isEmpty(goodsId))
			return Utils.getErrorStatusJson("商品ID为空").toJSONString();
		FDGoods goods = goodsService.getGoodsDetail(goodsId);
		if(goods==null)
			return Utils.getErrorStatusJson("获取商品信息失败").toJSONString();
		return Utils.getSuccessEntityJson(goods).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getGoodsList")
	public String getGoodsList(String goodsName, String goodsNumber, String categoryId, String status, Integer page, Integer pageSize, HttpServletRequest request){
		if(page==null||page<1)
			page =1;
		if(pageSize==null||pageSize<1)
			pageSize = 15;
		goodsName = Utils.turnCodeWhenGet(goodsName, request);
		goodsNumber = Utils.turnCodeWhenGet(goodsNumber, request);
		return goodsService.getGoodsList(goodsName, goodsNumber, categoryId, status, page, pageSize);
	}
	
	@ResponseBody
	@RequestMapping("/setGoodsUp")
	public String setGoodsUp(String goodsId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(goodsId))
			Utils.getErrorStatusJson("商品ID为空").toJSONString();
		String r = goodsService.setGoodsUp(goodsId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("商品上架成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/setGoodsDown")
	public String setGoodsDown(String goodsId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(goodsId))
			Utils.getErrorStatusJson("商品ID为空").toJSONString();
		String r = goodsService.setGoodsDown(goodsId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("商品下架成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/setGoodsDeleted")
	public String setGoodsDeleted(String goodsId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(goodsId))
			Utils.getErrorStatusJson("商品ID为空").toJSONString();
		String r = goodsService.setGoodsDeleted(goodsId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("商品删除成功").toJSONString();
	}
	
	//分类管理
	//商品管理
	@ResponseBody
	@RequestMapping("/addCategory")
	public String addCategory(FDCategory c, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(c.getName())){
			Utils.getErrorStatusJson("分类名称为空").toJSONString();
		}
		c.setName(Utils.turnCodeWhenGet(c.getName(), request));
		String r = goodsService.addCategory(c, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("添加分类成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/editCategory")
	public String editCategory(FDCategory c, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(c.getId())){
			Utils.getErrorStatusJson("分类ID为空").toJSONString();
		}
		if(Utils.isEmpty(c.getName())){
			Utils.getErrorStatusJson("分类名称为空").toJSONString();
		}
		c.setName(Utils.turnCodeWhenGet(c.getName(), request));
		String r = goodsService.editCategory(c, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("编辑分类成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/delCategory")
	public String delCategory(String categoryId, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(categoryId)){
			Utils.getErrorStatusJson("分类ID为空").toJSONString();
		}
		String r = goodsService.delCategory(categoryId, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("删除分类成功").toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/getAllCategories")
	public String getAllCategories(){
		List<FDCategory> list = goodsService.getAllCategories();
		if(list==null)
			return Utils.getErrorStatusJson("获取分类失败").toJSONString();
		return Utils.getSuccessEntityJson(list).toJSONString();
	}
	
	@ResponseBody
	@RequestMapping("/setCategoryTop")
	public String setCategoryTop(String categoryId){
		String r = goodsService.setCategoryTop(categoryId);
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("置顶分类成功").toJSONString();
	}
	
	//运费描述====================================
	@ResponseBody
	@RequestMapping("/updateSetFreightFeeDesc")
	public String updateSetFreightFeeDesc(String desc, HttpServletRequest request){
		UserAuthVo ua = commonService.userAuth(request);
		if(Utils.isEmpty(desc))
			return Utils.getErrorStatusJson("运费描述为空").toJSONString();
		desc = Utils.turnCodeWhenGet(desc, request);
		String r = tradeService.updateSetFreightFeeDesc(desc, request, ua.getId());
		if(r!=null)
			return Utils.getErrorStatusJson(r).toJSONString();
		return Utils.getSuccessStatusJson("修改成功").toJSONString();
	}
	
}
