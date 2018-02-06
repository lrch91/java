package com.mg.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mg.dao.AppealDao;
import com.mg.dao.CatcoinChangeDao;
import com.mg.dao.GoodsDao;
import com.mg.dao.MarketingCenterDao;
import com.mg.dao.OrderDao;
import com.mg.dao.OrderGoodsDao;
import com.mg.dao.UserDao;
import com.mg.dao.UserLoginSessionDao;
import com.mg.entity.FDCatcoinChange;
import com.mg.entity.FDGoods;
import com.mg.entity.FDMarketingCenter;
import com.mg.entity.FDOrder;
import com.mg.entity.FDOrderGoods;
import com.mg.entity.FDSystemParam;
import com.mg.entity.FDUser;
import com.mg.entity.WxBack;
import com.mg.util.ConUtil;
import com.mg.util.MD5;
import com.mg.util.Status;
import com.mg.util.Utils;
import com.mg.util.Alipay.AlipayNotify;
import com.mg.util.Alipay.RSA;

@Service
@Transactional 
public class OrderServiceImpl implements OrderService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private UserLoginSessionDao userLoginSessionDao;
	@Autowired
	private OrderDao orderDao;
	@Autowired
	private OrderGoodsDao orderGoodsDao;
	@Autowired
	private AppealDao appealDao;
	@Autowired
	private GoodsDao goodsDao;
	@Autowired
	private CatcoinChangeDao catcoinChangeDao;
	@Autowired
	private MarketingCenterDao marketingCenterDao;
	@Autowired
	private ConUtil conUtil;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	public String addOrder(String goodsId, Double sell_count, String acceptName, String acceptTel, String city, String area, String street, String address, String userRemark, String userId){
		try{
			FDGoods goods = goodsDao.findById(goodsId);
			if(goods==null)
				return "未找到商品信息";
			if(!goods.getStatus().equals(Status.GOODS_STATUS_ON))
				return "不能订购未上架产品";
			FDOrder order = new FDOrder();
			order.setId(UUID.randomUUID().toString());
			order.setBelongMerchant(goods.getBelong_merchant());
			order.setUserId(userId);
			order.setOrderNumber(Utils.getOrderNumber(orderDao));
			order.setAcceptName(acceptName);
			order.setAcceptTel(acceptTel);
			order.setCity(city);
			order.setArea(area);
			order.setAddress(address);
			order.setStreet(street);
			order.setUserRemark(userRemark);
			order.setStatus(Status.ORDER_STATUS_ORDERED);
			order.setSubmitDate(new Date());
			order.setUserFrontState(Status.ORDER_USERFRONTSTATE_SHOW);
			order.setUserRemark(userRemark);
			
			//订单绑定销售中心ID
			ArrayList<FDMarketingCenter> mcList = (ArrayList<FDMarketingCenter>) marketingCenterDao.findAll();
			for(FDMarketingCenter mc:mcList){
				JSONArray array1 = JSONObject.parseArray(mc.getDistrict());
				for(int i=0;i<array1.size();i++){
					JSONObject json1 = array1.getJSONObject(i);
					JSONArray array2 = (JSONArray) json1.get("sub_area");
					for(int j=0;j<array2.size();j++){
						JSONObject json2 = array2.getJSONObject(j);
						Set<String> set = json2.keySet();
					     for(String key : set){
					    	 if(key.equals(area)){
					    		 order.setBelongMarketingCenter(mc.getId());
					    		 break;
					    	 }
					     }
					}
				}
			}
			//订单绑定销售中心ID
			
			FDOrderGoods og = new FDOrderGoods();
			og.setId(UUID.randomUUID().toString());
			og.setGoods_number(goods.getGoods_number());
			og.setGoods_name(goods.getGoods_name());
			og.setGoods_id(goods.getId());
			og.setOrder_id(order.getId());
			og.setReference_price(goods.getReference_price());
			og.setSell_count(sell_count);
			og.setGoods_images(goods.getImagePaths());
			
			orderGoodsDao.add(og);
			orderDao.add(order);
			return order.getId();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getMemberOrderList(String userId, String status, Integer page, Integer pageSize){
		String sql = " select o.*,(select og1.GOODS_NAME from fd_order_goods og1 where og1.ORDER_ID=o.ID) as GOODS_NAME,"
				+ "(select og1.SELL_COUNT from fd_order_goods og1 where og1.ORDER_ID=o.ID) as SELL_COUNT,"
				+ "(select og1.REFERENCE_PRICE from fd_order_goods og1 where og1.ORDER_ID=o.ID) as REFERENCE_PRICE,"
				+ "(select og1.GOODS_IMAGES from fd_order_goods og1 where og1.ORDER_ID=o.ID) as GOODS_IMAGES ";
		String where = " from fd_order o where o.USER_ID='"+userId+"' and o.USER_FRONT_STATE ="+Status.ORDER_USERFRONTSTATE_SHOW+" ";
		if(!Utils.isEmpty(status)){
			if(status.equals("BEFOREPAY")){
				where += " and (o.STATUS ='"+Status.ORDER_STATUS_ORDERED+"' or o.STATUS ='"+Status.ORDER_STATUS_PRICED+"' ) ";
			}else{
				where += " and o.STATUS ='"+status+"' ";
			}
		}
		JSONObject json = conUtil.getRows(sql+where+" order by o.SUBMIT_DATE desc limit "+(page-1)*pageSize+","+pageSize+" ");
		String countSql = " select count(*) as COUNT ";
		json.put("total", ((Long)(conUtil.getData(countSql+where)).get(0).get("COUNT")).intValue());
		return json.toJSONString();
	}
	
	@Override
	public String getMemberOrderDetail(String orderId, String userId){
		String sql = " select o.*,(select og1.GOODS_NAME from fd_order_goods og1 where og1.ORDER_ID=o.ID) as GOODS_NAME,"
				+ "(select og1.SELL_COUNT from fd_order_goods og1 where og1.ORDER_ID=o.ID) as SELL_COUNT,"
				+ "(select og1.REFERENCE_PRICE from fd_order_goods og1 where og1.ORDER_ID=o.ID) as REFERENCE_PRICE,"
				+ "(select og1.GOODS_IMAGES from fd_order_goods og1 where og1.ORDER_ID=o.ID) as GOODS_IMAGES, "
				+ "(select a1.NAME from fd_area a1 where a1.ID=o.CITY) as CITY_NAME, "
				+ "(select a2.NAME from fd_area a2 where a2.ID=o.AREA) as AREA_NAME, "
				+ "(select a3.NAME from fd_area a3 where a3.ID=o.STREET) as STREET_NAME ";
		String where = " from fd_order o where o.USER_ID='"+userId+"' and o.ID='"+orderId+"' and o.USER_FRONT_STATE ="+Status.ORDER_USERFRONTSTATE_SHOW+" ";
		List<Map<String,Object>> list = conUtil.getData(sql+where);
		if(list==null)
			return Utils.getErrorStatusJson("获取数据失败").toJSONString();
		return Utils.getSuccessEntityJson(list.get(0)).toJSONString();
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	public String memberCancelOrder(String orderId, String userId){
		FDOrder order = orderDao.findOne(" where id='"+orderId+"' and userId='"+userId+"' ");
		if(order==null){
			return "未找到订单信息";
		}
		if(!(order.getStatus().equals(Status.ORDER_STATUS_ORDERED)||order.getStatus().equals(Status.ORDER_STATUS_PRICED))){
			return "当前订单状态不可取消";
		}
		order.setStatus(Status.ORDER_STATUS_CANCEL);
		order.setCancelDate(new Date());
		return null;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	public String deductOrderPrice(String orderId, String userId, Integer deductSum, FDSystemParam param){
		FDOrder order = orderDao.findOne(" where id='"+orderId+"' and userId='"+userId+"' ");
		if(order==null){
			return "未找到订单信息";
		}
		FDUser user = userDao.findById(userId);
		if(user==null){
			return "未找到用户信息";
		}
		if(!Status.SYSTEMPARAM_ISCATCOINDEDUCTOPEN_YES.equals(param.getIsCatcoinDeductOpen())){
			return "暂不可用猫币抵扣";
		}
		if(!order.getStatus().equals(Status.ORDER_STATUS_PRICED)){
			return "当前订单状态不可抵扣";
		}
		if(deductSum>order.getTotalPrice()){
			return "抵扣金额高于限制";
		}
		Integer needCatcoin = deductSum*param.getCatcoinDeductRate();
		if(needCatcoin>user.getCatcoin()){
			return "抵扣所需猫币大于现有猫币数量";
		}
		order.setDeductPrice(0.0+deductSum);
		order.setPayPrice(order.getTotalPrice()-deductSum);
		orderDao.update(order);
		
		Integer beforeCatcoin = user.getCatcoin();
		user.setCatcoin(beforeCatcoin-needCatcoin);
		userDao.update(user);
		
		FDCatcoinChange cc = new FDCatcoinChange();
		cc.setId(UUID.randomUUID().toString());
		cc.setChangeSum(needCatcoin);
		cc.setChangeType(Status.CATCOINCHANGE_CHANGETYPE_CATCOINDEDUCT);
		cc.setChangeDate(new Date());
		cc.setNowCoin(beforeCatcoin-needCatcoin);
		catcoinChangeDao.add(cc);
		return null;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	public String get_alipay_sign(String orderId, String userId, Map<String, Object> map, HttpServletResponse response){
		try{
			String total_fee="";
			FDOrder order = orderDao.findOne(" where id='"+orderId+"' and userId='"+userId+"'  ");
			if(order==null){
				return Utils.getErrorStatusJson("支付订单不存在").toJSONString();
			}
			String subject = "支付"+order.getOrderNumber()+"订单-富岛农友";
			String body = "支付"+order.getOrderNumber()+"订单-富岛农友";
			if(!Status.ORDER_STATUS_PRICED.equals(order.getStatus())){
				return Utils.getErrorStatusJson("非待付款订单").toJSONString();
			}
			Double money = order.getPayPrice();
			if(money!=null && money>0){
				total_fee = money.toString();
			}else{
				return Utils.getErrorStatusJson("支付金额异常").toJSONString();
			}
			String notify_url=map.get("ORDER_NOTIFY_URL").toString();
			String orderSpec="partner=\""+map.get("PARTNER").toString()+"\""
					+ "&seller_id=\""+map.get("SELLER_ID").toString()+"\""
					+ "&out_trade_no=\""+order.getOrderNumber()+"\""
					+ "&subject=\""+subject+"\""
					+ "&body=\""+body+"\""
					+ "&total_fee=\""+total_fee+"\""
					+ "&notify_url=\""+notify_url+"\""
					+ "&service=\"mobile.securitypay.pay\""
					+ "&payment_type=\"1\""
					+ "&_input_charset=\"utf-8\""
					+ "&it_b_pay=\"30m\"";
			String sign = RSA.returnSign(orderSpec, map.get("PACSK8_PRIVATE_KEY").toString());
			sign = new String(sign.getBytes("US-ASCII"),"UTF-8");
			response.setContentType("text/plain; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(orderSpec + "&sign=\"" + sign + "\"&sign_type=\"RSA\"");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return Utils.getErrorStatusJson("系统异常").toJSONString();
		}
	}
	
	@Override
	public String alipay_order_notify(HttpServletRequest request){
		try{
			//获取支付宝POST过来反馈信息
			Map<String,String> params = new HashMap<String,String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
				params.put(name, valueStr);
			}
			
			//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			//商户订单号	String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//支付宝交易号	String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

			//交易状态
			String trade_status = new String(request.getParameter("trade_status")!=null?request.getParameter("trade_status"):"");
			System.out.println("order_trade_status:"+trade_status);
			if(trade_status.equals("TRADE_FINISHED")||trade_status.equals("TRADE_SUCCESS")){
				if(AlipayNotify.verify(params)){
					//修改订单状态
					String orderNumber = params.get("out_trade_no").toString();
					FDOrder order = orderDao.findOne(" where orderNumber='"+orderNumber+"' ");
					if(order!=null && order.getStatus().equals(Status.ORDER_STATUS_PRICED)){
						order.setStatus(Status.ORDER_STATUS_PAYED);
						order.setPayWay("alipay");
						order.setPayWaySN(params.get("trade_no").toString());
						order.setPayDate(new Date());
						orderDao.update(order);
					}
				}
			}
			return Utils.getSuccessStatusJson("订单状态已修改").toJSONString();
		}catch(Exception e){
			e.printStackTrace();
			return Utils.getErrorStatusJson("异步回调接口异常").toJSONString();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getWXPrepayId(String orderId, String userId, String spbill_create_ip, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		try {
			String total_fee="";
			FDOrder order = orderDao.findOne(" where id='"+orderId+"' and userId='"+userId+"'  ");
			if(order==null){
				json.put("retcode",-1);
				json.put("retmsg","支付信息不存在");
				return json.toJSONString();
			}
			String nonce_str = Utils.MD5(Utils.getRandom(100000000));
			String body = "支付"+order.getOrderNumber()+"订单-富岛农友";
			Double money = order.getPayPrice();
			if(money!=null && money>0){
				Integer fee = (int) Math.rint(money*100);
				total_fee = fee.toString();
			}else{
				json.put("retcode",-1);
				json.put("retmsg","支付金额异常");
				return json.toJSONString();
			}
			String notify_url=map.get("ORDER_NOTIFY_URL").toString();
			String sign_str="appid="+map.get("APP_ID")
					+ "&body="+body
					+ "&mch_id="+map.get("MCH_ID")
					+ "&nonce_str="+nonce_str
					+ "&notify_url="+notify_url
					+ "&out_trade_no="+order.getOrderNumber()
					+ "&spbill_create_ip="+spbill_create_ip
					+ "&total_fee="+total_fee
					+ "&trade_type=APP"
					+ "&key="+map.get("APP_KEY");
			String sign = MD5.getMessageDigest(sign_str.getBytes("UTF-8")).toUpperCase();
			String str="<xml>"
					+ "<appid>"+map.get("APP_ID")+"</appid>"
					+ "<body>"+body+"</body>"
					+ "<mch_id>"+map.get("MCH_ID")+"</mch_id>"
					+ "<nonce_str>"+nonce_str+"</nonce_str>"
					+ "<notify_url>"+notify_url+"</notify_url>"
					+ "<out_trade_no>"+order.getOrderNumber()+"</out_trade_no>"
					+ "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"
					+ "<total_fee>"+total_fee+"</total_fee>"
					+ "<trade_type>APP</trade_type>"
					+ "<sign>"+sign+"</sign>"
					+ "</xml>";
			InputStream input = new ByteArrayInputStream(str.getBytes("UTF-8"));
		    PostMethod post = new PostMethod("https://api.mch.weixin.qq.com/pay/unifiedorder");
		    // 设置请求的内容直接从文件中读取
		    post.setRequestBody(input);
		    // 指定请求内容的类型
		    post.setRequestHeader("Content-type", "text/xml; charset=utf-8");
		    HttpClient httpclient = new HttpClient(); 
			int result = httpclient.executeMethod(post); 
			if(result!=200){
				json.put("retcode",-1);
				json.put("retmsg","网络异常");
				return json.toJSONString();
			}
			String re = post.getResponseBodyAsString();
			String re_str = Utils.turnCodeWhenGet(re, request);
			SAXReader sax = new SAXReader();
			InputStream in = new ByteArrayInputStream(re_str.getBytes());
			Document dc = sax.read(in);
			Element root = dc.getRootElement();
			String return_code = root.element("return_code").getTextTrim();
			String return_msg = root.element("return_msg").getTextTrim();
			if(return_code.equals("SUCCESS") && return_msg.equals("OK")){
				Date date = new Date();
				String tem = String.valueOf(date.getTime()/1000);
				String sec_str = "appid="+map.get("APP_ID")
						+"&noncestr="+nonce_str
						+"&package=Sign=WXPay"
						+"&partnerid="+map.get("MCH_ID")
						+"&prepayid="+root.element("prepay_id").getTextTrim()
						+"&timestamp="+tem
						+"&key="+map.get("APP_KEY");
				String sec_sign = MD5.getMessageDigest(sec_str.getBytes("UTF-8")).toUpperCase();
				/*json.put("status", "success");
				json.put("msg", "数据返回成功");
				json.put("appid", map.get("APP_ID"));
				json.put("partner_id", map.get("MCH_ID"));
				json.put("prepay_id", root.element("prepay_id").getTextTrim());
				json.put("nonce_str", nonce_str);
				json.put("time_stamp", tem);
				json.put("sign", sec_sign);*/
				
				json.put("retcode",0);
				json.put("retmsg","OK");
				json.put("appid",map.get("APP_ID"));
				json.put("noncestr", nonce_str);
				json.put("package", "Sign=WXPay");
				json.put("partnerid", map.get("MCH_ID"));
				json.put("prepayid", root.element("prepay_id").getTextTrim());
				json.put("timestamp", tem);
				json.put("sign", sec_sign);
				return json.toJSONString();
			}else{
				json.put("retcode",-1);
				json.put("retmsg","数据返回失败");
				return json.toJSONString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			json.put("retcode",-1);
			json.put("retmsg","获取微信支付预付款ID异常");
			return json.toJSONString();
		}
	}
	@Override
	public WxBack wx_pay_notify(HttpServletRequest request, Map<String, Object> map){
		WxBack wxBack = new WxBack();
		try {
			ServletInputStream in = request.getInputStream();
			int size = request.getContentLength(); 
			byte[] bdata = new byte[size];
			in.read(bdata);
			SAXReader sax = new SAXReader();
			InputStream input = new ByteArrayInputStream(bdata);
			Document dc = sax.read(input);
			Element root = dc.getRootElement();
			
			if(root.element("result_code").getTextTrim().equals("SUCCESS")){
				//验证签名是否有效
				root.element("transaction_id").getTextTrim();
				
				String str="appid="+root.element("appid").getTextTrim()
						+ "&bank_type="+root.element("bank_type").getTextTrim()
						+ "&cash_fee="+root.element("cash_fee").getTextTrim()
						+ "&fee_type="+root.element("fee_type").getTextTrim()
						+ "&is_subscribe="+root.element("is_subscribe").getTextTrim()
						+ "&mch_id="+root.element("mch_id").getTextTrim()
						+ "&nonce_str="+root.element("nonce_str").getTextTrim()
						+ "&openid="+root.element("openid").getTextTrim()
						+ "&out_trade_no="+root.element("out_trade_no").getTextTrim()
						+ "&result_code="+root.element("result_code").getTextTrim()
						+ "&return_code="+root.element("return_code").getTextTrim()
						+ "&time_end="+root.element("time_end").getTextTrim()
						+ "&total_fee="+root.element("total_fee").getTextTrim()
						+ "&trade_type="+root.element("trade_type").getTextTrim()
						+ "&transaction_id="+root.element("transaction_id").getTextTrim()
						+ "&key="+map.get("APP_KEY");
				
				String sign = MD5.getMessageDigest(str.getBytes("UTF-8")).toUpperCase();
				if(!sign.equals(root.element("sign").getTextTrim())){
					wxBack.setReturn_code("FAIL");
					wxBack.setReturn_msg("");
					return wxBack;
				}
				//修改订单状态
				String orderNumber = root.element("out_trade_no").getTextTrim();
				FDOrder order = orderDao.findOne(" where orderNumber='"+orderNumber+"' ");
				if(order!=null && order.getStatus().equals(Status.ORDER_STATUS_PRICED)){
					order.setStatus(Status.ORDER_STATUS_PAYED);
					order.setPayWay("wx_pay");
					order.setPayWaySN(root.element("transaction_id").getTextTrim());
					order.setPayDate(new Date());
					orderDao.update(order);
					
					wxBack.setReturn_code("SUCCESS");
					wxBack.setReturn_msg("OK");
					
				}
				wxBack.setReturn_code("FAIL");
				wxBack.setReturn_msg("");
			}else{
				wxBack.setReturn_code("FAIL");
				wxBack.setReturn_msg("");
			}
		} catch (Exception e) {
			wxBack.setReturn_code("FAIL");
			wxBack.setReturn_msg("");
			e.printStackTrace();
		}
		return wxBack;
	}
	
	
	//================================================
	@Override
	@Cacheable(value = "alipayConifgCache")
	public Map<String,Object> getAlipayConifg(){
		String sql = " select * from fd_alipay_config ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) json.get("rows");
		return list.get(0);
	}
	
	@Override
	@Cacheable(value = "wxpayConifgCache")
	public Map<String,Object> getWxpayConifg(){
		String sql = " select * from fd_wxpay_config ";
		JSONObject json = new JSONObject();
		json = conUtil.getRows(sql);
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> list = (List<Map<String, Object>>) json.get("rows");
		return list.get(0);
	}
	
}
