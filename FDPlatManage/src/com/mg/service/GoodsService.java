package com.mg.service;

import java.util.List;

import com.mg.entity.FDCategory;
import com.mg.entity.FDGoods;




public interface GoodsService {

	String addGoods(FDGoods g, String adminId);
	String editGoods(FDGoods g, String adminId);
	FDGoods getGoodsDetail(String goodsId);
	String getGoodsList(String goodsName, String goodsNumber, String categoryId, String status, Integer page, Integer pageSize);
	String setGoodsUp(String goodsId, String adminId);
	String setGoodsDown(String goodsId, String adminId);
	String setGoodsDeleted(String goodsId, String adminId);
	
	String addCategory(FDCategory c, String adminId);
	String editCategory(FDCategory c, String adminId);
	String delCategory(String categoryId, String adminId);
	List<FDCategory> getAllCategories();
	String setCategoryTop(String categoryId);
}
