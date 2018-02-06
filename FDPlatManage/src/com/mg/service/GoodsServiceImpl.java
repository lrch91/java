package com.mg.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.CategoryDao;
import com.mg.dao.GoodsDao;
import com.mg.dao.OperateLogDao;
import com.mg.entity.FDCategory;
import com.mg.entity.FDGoods;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional 
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private ConUtil conUtil;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private OperateLogDao operateLogDao;
	
	@Override
	public String addGoods(FDGoods g, String adminId){
		g.setId(UUID.randomUUID().toString());
		g.setUp_date(Utils.getCurrentTimeString());
		g.setStatus(Status.GOODS_STATUS_ON);
		
		String queryStr = "";
		if(!Utils.isEmpty(g.getCategory_id())){
			FDCategory c = categoryDao.findById(g.getCategory_id());
			if(c!=null){
				queryStr += c.getName();
			}
		}
		if(!Utils.isEmpty(g.getGoods_name())){
			queryStr += g.getGoods_name();
		}
		if(!Utils.isEmpty(g.getBrief())){
			queryStr += g.getBrief();
		}
		if(!Utils.isEmpty(g.getGoods_number())){
			queryStr += g.getGoods_number();
		}
		g.setQueryStr(queryStr);
		goodsDao.add(g);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "发布商品", g.getGoods_name());
		return null;
	}
	
	@Override
	public String editGoods(FDGoods goods, String adminId){
		FDGoods g = goodsDao.findById(goods.getId());
		if(g==null)
			return "未找到商品信息";
		g.setBelong_merchant(goods.getBelong_merchant());
		g.setBrief(goods.getBrief());
		g.setCategory_id(goods.getCategory_id());
		g.setDetail(goods.getDetail());
		g.setDown_date(goods.getDown_date());
		g.setFree_post(goods.getFree_post());
		g.setGoods_name(goods.getGoods_name());
		g.setGoods_number(goods.getGoods_number());
		g.setImagePaths(goods.getImagePaths());
		g.setInventory(goods.getInventory());
		g.setMark(goods.getMark());
		g.setReference_price(goods.getReference_price());
		g.setWeight(goods.getWeight());
		g.setPriceShow(goods.getPriceShow());
		
		String queryStr = "";
		if(!Utils.isEmpty(g.getCategory_id())){
			FDCategory c = categoryDao.findById(g.getCategory_id());
			if(c!=null){
				queryStr += c.getName();
			}
		}
		if(!Utils.isEmpty(g.getGoods_name())){
			queryStr += g.getGoods_name();
		}
		if(!Utils.isEmpty(g.getBrief())){
			queryStr += g.getBrief();
		}
		if(!Utils.isEmpty(g.getGoods_number())){
			queryStr += g.getGoods_number();
		}
		g.setQueryStr(queryStr);
		goodsDao.update(g);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "编辑商品", g.getGoods_name());
		return null;
	}
	
	@Override
	public FDGoods getGoodsDetail(String goodsId){
		FDGoods goods = goodsDao.findById(goodsId);
		if(goods!=null&&goods.getCategory_id()!=null){
			FDCategory c = categoryDao.findById(goods.getCategory_id());
			if(c!=null){
				goods.setCategory_name(c.getName());
			}
		}
		return goods;
	}
	
	@Override
	public String getGoodsList(String goodsName, String goodsNumber, String categoryId, String status, Integer page, Integer pageSize){
		String sql = " select g.*,(select c1.NAME from fd_category c1 where c1.ID=g.CATEGORY_ID) as CATEGORY_NAME ";
		String where = " from fd_goods g where g.STATUS!='"+Status.GOODS_STATUS_DELETED+"' ";
		if(!Utils.isEmpty(goodsName)){
			where += " and g.GOODS_NAME='"+goodsName+"' ";
		}
		if(!Utils.isEmpty(goodsNumber)){
			where += " and g.GOODS_NUMBER='"+goodsNumber+"' ";
		}
		if(!Utils.isEmpty(categoryId)){
			where += " and (g.CATEGORY_ID='"+categoryId+"' or g.CATEGORY_ID in (select c.ID from fd_category c where c.PARENT_ID='"+categoryId+"') )";
		}
		if(!Utils.isEmpty(status)){
			where += " and g.STATUS='"+status+"' ";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by g.enter_date desc limit "+(page-1)*pageSize+","+pageSize+" ");
		String countSql = " select count(*) as COUNT ";
		json.put("total", ((Long)(conUtil.getData(countSql+where)).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String setGoodsUp(String goodsId, String adminId){
		FDGoods g = goodsDao.findById(goodsId);
		if(g==null)
			return "未找到商品信息";
		if(Status.GOODS_STATUS_DELETED.equals(g.getStatus()))
			return "商品已删除";
		if(Status.GOODS_STATUS_ON.equals(g.getStatus()))
			return "商品已上架";
		g.setStatus(Status.GOODS_STATUS_ON);
		g.setUp_date(Utils.getCurrentTimeString());
		goodsDao.update(g);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "上架商品", g.getGoods_name());
		return null;
	}
	
	@Override
	public String setGoodsDown(String goodsId, String adminId){
		FDGoods g = goodsDao.findById(goodsId);
		if(g==null)
			return "未找到商品信息";
		if(Status.GOODS_STATUS_DELETED.equals(g.getStatus()))
			return "商品已删除";
		if(Status.GOODS_STATUS_OFF.equals(g.getStatus()))
			return "商品已下架";
		g.setStatus(Status.GOODS_STATUS_OFF);
		g.setDown_date(Utils.getCurrentTimeString());
		goodsDao.update(g);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "下架商品", g.getGoods_name());
		return null;
	}
	
	@Override
	public String setGoodsDeleted(String goodsId, String adminId){
		FDGoods g = goodsDao.findById(goodsId);
		if(g==null)
			return "未找到商品信息";
		if(Status.GOODS_STATUS_DELETED.equals(g.getStatus()))
			return "商品已删除";
		g.setStatus(Status.GOODS_STATUS_DELETED);
		goodsDao.update(g);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "移除商品", g.getGoods_name());
		return null;
	}
	
	@Override
	public String addCategory(FDCategory c, String adminId){
		if(Utils.isEmpty(c.getParentId())){
			c.setId(UUID.randomUUID().toString());
			c.setParentId("root_node");
			c.setLevel(1);
			c.setSortDate(new Date());
			categoryDao.add(c);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "添加商品分类", c.getName());
			return null;
		}else{
			FDCategory tc = categoryDao.findById(c.getParentId());
			if(tc==null)
				return "未找到父级分类信息";
			c.setId(UUID.randomUUID().toString());
			c.setParentId(tc.getId());
			c.setLevel(tc.getLevel()+1);
			c.setSortDate(new Date());
			categoryDao.add(c);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "添加商品分类", c.getName());
			return null;
		}
	}
	
	@Override
	public String editCategory(FDCategory c, String adminId){
		FDCategory temp =categoryDao.findById(c.getId());
		if(temp==null)
			return "未找到分类信息";
		temp.setName(c.getName());
		temp.setImagePath(c.getImagePath());
		categoryDao.update(temp);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "编辑商品分类", c.getName());
		return null;
	}
	
	@Override
	public String delCategory(String categoryId, String adminId){
		FDCategory temp =categoryDao.findById(categoryId);
		if(temp==null)
			return "未找到分类信息";
		String c_name = temp.getName();
		categoryDao.deleteById(temp.getId());
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_GOODS, "编辑商品分类", c_name);
		return null;
	}
	
	@Override
	public List<FDCategory> getAllCategories(){
		List<FDCategory> list1 = categoryDao.findBycondition(" where level=1 order by sortDate desc ");
		if(list1!=null&&list1.size()>0){
			for(FDCategory temp1:list1){
				List<FDCategory> list2 = categoryDao.findBycondition(" where parentId='"+temp1.getId()+"' order by sortDate desc ");
				temp1.setChildList(list2);
			}
		}
		return list1;
	}
	
	@Override
	public String setCategoryTop(String categoryId){
		FDCategory c = categoryDao.findById(categoryId);
		if(c==null)
			return "未找到分类信息";
		c.setSortDate(new Date());
		categoryDao.update(c);
		return null;
	}
	
}
