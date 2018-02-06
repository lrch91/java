package com.mg.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mg.dao.GoodsDao;
import com.mg.dao.IntegralChangeDao;
import com.mg.dao.MarketingCenterDao;
import com.mg.dao.OperateLogDao;
import com.mg.dao.OrderDao;
import com.mg.dao.OrderGoodsDao;
import com.mg.dao.ParamDao;
import com.mg.dao.UserDao;
import com.mg.dao.VManagerDao;
import com.mg.entity.FDGoods;
import com.mg.entity.FDIntegralChange;
import com.mg.entity.FDMarketingCenter;
import com.mg.entity.FDOrder;
import com.mg.entity.FDOrderGoods;
import com.mg.entity.FDSystemParam;
import com.mg.entity.FDUser;
import com.mg.entity.VFDManager;
import com.mg.util.ConUtil;
import com.mg.util.Status;
import com.mg.util.Utils;

@Service
@Transactional 
public class OrderServiceImpl implements OrderService {

	@Autowired
	private ConUtil conUtil;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private ParamDao paramDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private IntegralChangeDao integralChangeDao;
	@Autowired
	private MarketingCenterDao	marketingCenterDao;
	@Autowired
	private VManagerDao	vManagerDao;
	@Autowired
	private OperateLogDao operateLogDao;
	
	@Override
	public String getOrderList(String nameTel, String orderNumber, String status, String startDate, String endDate, Integer page, Integer pageSize, String adminId){
		VFDManager m = vManagerDao.findOne(" where userId='"+adminId+"'");
		String sql = " select o.*,(select u1.LOGIN_NAME from fd_user u1 where u1.ID=o.USER_ID) LOGIN_NAME,(select u1.PHONE from fd_user u1 where u1.ID=o.USER_ID) PHONE,"
				+ " (select mc.NAME from fd_marketing_center mc where mc.ID=o.BELONG_MARKETING_CENTER) MARKETING_CENTER_NAME from fd_order o ";
		String where = " where 1=1 ";
		if(!Utils.isEmpty(nameTel)){
			String userSql = " select u.ID from fd_user u where u.LOGIN_NAME='"+nameTel+"' or u.PHONE='"+nameTel+"' ";
			List<Map<String,Object>> userList = conUtil.getData(userSql);
			if(userList!=null&&userList.size()>0){
				String userId = (String) userList.get(0).get("ID");
				where += " and o.USER_ID='"+userId+"' ";
			}
		}
		if(!Utils.isEmpty(orderNumber)){
			where += " and o.ORDER_NUMBER='"+orderNumber+"' ";
		}
		if(!Utils.isEmpty(status)){
			where += " and o.STATUS='"+status+"' ";
		}
		if(!Utils.isEmpty(m.getBelongMarketingCenter())){
			where += " and o.BELONG_MARKETING_CENTER='"+m.getBelongMarketingCenter()+"' ";
		}
		if(!Utils.isEmpty(startDate)){
			where += " and o.SUBMIT_DATE>= '"+startDate+" 00:00:00'";
		}
		if(!Utils.isEmpty(startDate)){
			where += " and o.SUBMIT_DATE<= '"+startDate+" 23:59:59'";
		}
		JSONObject json = conUtil.getRows(sql+where+" order by o.SUBMIT_DATE desc limit "+(page-1)*pageSize+","+pageSize);
		List<Map<String,Object>> countList = conUtil.getData(" select count(*) as COUNT from fd_order o "+where);
		json.put("total", ((Long)countList.get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getOrderDetalil(String orderId){
		String sql = " select o.*,og.goods_id,og.goods_name,og.goods_number,og.brand,og.reference_price,og.sell_count,"
				 + " og.express_number,og.express_type,og.delivery_date,og.goods_images,og.weight,og.mark, "
				 + " (select a1.NAME from fd_area a1 where a1.ID=o.CITY) as CITY_NAME, "
				 + " (select a2.NAME from fd_area a2 where a2.ID=o.AREA) as ARES_NAME, "
				 + " (select a3.NAME from fd_area a3 where a3.ID=o.STREET) as STREET_NAME "
				 + " from fd_order o left join fd_order_goods og on o.ID=og.ORDER_ID where o.ID='"+orderId+"'";
		JSONObject json = conUtil.getRows(sql);
		return json.toJSONString();
	}
	
	@Override
	public String changeOrderPrice(String orderId, Double newPrice, String adminId){
		FDOrder order = orderDao.findById(orderId);
		if(order==null)
			return "未找到订单信息";
		if(!Status.ORDER_STATUS_ORDERED.equals(order.getStatus())){
			return "当前状态不可改价";
		}
		order.setStatus(Status.ORDER_STATUS_PRICED);
		order.setTotalPrice(newPrice);
		order.setPayPrice(newPrice);
		orderDao.update(order);
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_ORDER, "修改订单价格", order.getOrderNumber());
		return null;
	}
	
	@Override //接受并完成订单
	public String acceptOrder(String orderId, String adminId){
		FDOrder order = orderDao.findById(orderId);
		if(order==null)
			return "未找到订单信息";
		if(!Status.ORDER_STATUS_PAYED.equals(order.getStatus())){
			return "当前状态不可受理";
		}
		order.setStatus(Status.ORDER_STATUS_ACCEPTED);
		FDOrderGoods orderGoods = orderGoodsDao.findOne(" where order_id='"+order.getId()+"' ");
		if(orderGoods==null)
			return "未找到订单商品信息";
		FDGoods goods = goodsDao.findById(orderGoods.getGoods_id());
		if(goods==null)
			return "未找到商品信息";
		goods.setSalesVolume(goods.getSalesVolume()+orderGoods.getSell_count());
		goods.setInventory(goods.getInventory()-orderGoods.getSell_count());
		goodsDao.update(goods);
		
		orderDao.update(order);
		
		FDSystemParam sp = paramDao.findById(Status.SYSTEMPARAM_DEFAULT_ID);
		FDUser user = userDao.findById(order.getUserId());
		if(user==null)
			return "未找到用户信息";
		Integer beforeIntegral = user.getIntegral();
		Integer beforeHistoryIntegral = user.getHistoryIntegral();
		user.setIntegral(beforeIntegral + order.getPayPrice().intValue()*sp.getConsumeIntegralRate());
		user.setHistoryIntegral(beforeHistoryIntegral + order.getPayPrice().intValue()*sp.getConsumeIntegralRate());
		user.setHistoryBalance(user.getHistoryBalance()+order.getPayPrice());
		userDao.update(user);
		
		FDIntegralChange ic = new FDIntegralChange();
		ic.setId(UUID.randomUUID().toString());
		ic.setChangeDate(new Date());
		ic.setAttachId(orderId);
		ic.setChangeSum(order.getPayPrice().intValue()*sp.getConsumeIntegralRate());
		ic.setChangeType(Status.INTEGRALCHANGE_CHANGETYPE_ORDERCONSUMEADD);
		ic.setNowIntegral(beforeIntegral + order.getPayPrice().intValue()*sp.getConsumeIntegralRate());
		ic.setUserId(order.getUserId());
		integralChangeDao.add(ic);
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_ORDER, "受理订单", order.getOrderNumber());
		return null;
	}
	
	@Override
	public String cancelOrder(String orderId, String remark, String adminId){
		FDOrder order = orderDao.findById(orderId);
		if(order==null)
			return "未找到订单信息";
		if(!(Status.ORDER_STATUS_ORDERED.equals(order.getStatus())||Status.ORDER_STATUS_PRICED.equals(order.getStatus()))){
			return "当前状态不可取消";
		}
		order.setAdminCancelRemark(remark);
		order.setStatus(Status.ORDER_STATUS_CANCEL);
		orderDao.update(order);
		
		operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_ORDER, "取消订单", order.getOrderNumber());
		return null;
	}
	
	@Override
	public String delOrder(String orderId){
		FDOrder order = orderDao.findById(orderId);
		if(order==null)
			return "未找到订单信息";
		if(!Status.ORDER_STATUS_CANCEL.equals(order.getStatus())){
			return "非已取消订单不可删除";
		}
		FDOrderGoods orderGoods = orderGoodsDao.findOne(" where order_id='"+order.getId()+"' ");
		if(orderGoods!=null){
			orderGoodsDao.deleteById(orderGoods.getId());
		}
		orderDao.deleteById(order.getId());
		return null;
	}
	
	@Override
	public List<FDMarketingCenter> getMarketingCenterList(){
		ArrayList<FDMarketingCenter> mcList = (ArrayList<FDMarketingCenter>) marketingCenterDao.findBycondition(" order by createDate desc ");
		return mcList;
	}
	
	@Override
	public FDMarketingCenter getMarketingCenterDetail(String marketId){
		return marketingCenterDao.findById(marketId);
	}
	
	@Override
	public String editMarketingCenter(FDMarketingCenter mc, String adminId){
		if(Utils.isEmpty(mc.getId())){
			mc.setId(UUID.randomUUID().toString());
			mc.setCreateDate(new Date());
			if(!Utils.isEmpty(mc.getDistrict())){
				JSONArray array = JSONObject.parseArray(mc.getDistrict());
				if(array!=null&&array.size()>0){
					for(int i=0;i<array.size();i++){
						JSONObject obj = (JSONObject)array.get(i);
						JSONArray subArray = (JSONArray)obj.get("sub_area");
						if(subArray!=null&&subArray.size()>0){
							for(int j=0;j<subArray.size();j++){
								JSONObject subObject = (JSONObject) subArray.get(j);
								if(subObject!=null){
									for(String key:subObject.keySet()){
										List<FDMarketingCenter> mcList = marketingCenterDao.findBycondition(" where district like '%\""+key+"\"%' ");
										if(mcList!=null&&mcList.size()>0){
											return "地区重复添加:"+subObject.get(key);
										}
									}
								}
							}
						}
					}
				}
			}
			marketingCenterDao.add(mc);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_ORDER, "编辑营销中心", mc.getName());
		}else{
			FDMarketingCenter temp = marketingCenterDao.findById(mc.getId());
			temp.setDistrict(mc.getDistrict());
			temp.setName(mc.getName());
			if(!Utils.isEmpty(mc.getDistrict())){
				JSONArray array = JSONObject.parseArray(mc.getDistrict());
				if(array!=null&&array.size()>0){
					for(int i=0;i<array.size();i++){
						JSONObject obj = (JSONObject)array.get(i);
						JSONArray subArray = (JSONArray)obj.get("sub_area");
						if(subArray!=null&&subArray.size()>0){
							for(int j=0;j<subArray.size();j++){
								JSONObject subObject = (JSONObject) subArray.get(j);
								if(subObject!=null){
									for(String key:subObject.keySet()){
										List<FDMarketingCenter> mcList = marketingCenterDao.findBycondition(" where district like '%\""+key+"\"%' and id!='"+mc.getId()+"' ");
										if(mcList!=null&&mcList.size()>0){
											return "地区重复添加:"+subObject.get(key);
										}
									}
								}
							}
						}
					}
				}
			}
			marketingCenterDao.update(temp);
			operateLogDao.addOperateLog(adminId, Status.OPERATELOG_OPERATETYPE_ORDER, "编辑营销中心", mc.getName());
		}
		return null;
	}
	
}
