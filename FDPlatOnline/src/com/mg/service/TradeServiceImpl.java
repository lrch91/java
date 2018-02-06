package com.mg.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.CatcoinChangeDao;
import com.mg.dao.IntegralChangeDao;
import com.mg.dao.UserDao;
import com.mg.dao.UserRechargeDao;
import com.mg.entity.FDUserRecharge;
import com.mg.entity.WxBack;
import com.mg.util.MD5;
import com.mg.util.Pager;
import com.mg.util.Status;
import com.mg.util.Utils;
import com.mg.util.Alipay.AlipayNotify;
import com.mg.util.Alipay.RSA;

@Service
@Transactional 
public class TradeServiceImpl implements TradeService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private ParamService paramService;
	@Autowired
	private IntegralChangeDao integralChangeDao;
	@Autowired
	private CatcoinChangeDao catcoinChangeDao;
	@Autowired
	private UserRechargeDao userRechargeDao;
	
	@Override
	public Pager getIntegralList(String userId, Integer page, Integer pageSize){
		return integralChangeDao.findBycondition(page, pageSize, " where userId='"+userId+"' order by changeDate desc");
	}
	
	@Override
	public Pager getCatcoinList(String userId, Integer page, Integer pageSize){
		return catcoinChangeDao.findBycondition(page, pageSize, " where userId='"+userId+"' order by changeDate desc");
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_COMMITTED)
	public String get_alipay_sign(Integer rechargeSum, String userId, Map<String, Object> map, HttpServletResponse response){
		try{
			String total_fee="";
			FDUserRecharge ur = new FDUserRecharge();
			ur.setId(UUID.randomUUID().toString());
			ur.setRechargeUserId(userId);
			ur.setPayWay("alipay");
			ur.setRechargeDate(new Date());
			ur.setRechargeSum(rechargeSum);
			ur.setRechargeType(Status.CATCOINCHANGE_CHANGETYPE_RECHARGECATCOIN);
			ur.setStatus(Status.USERRECHARGE_STATUS_WAITVERIFY);
			userRechargeDao.add(ur);
			
			String subject = "猫币充值"+ur.getId()+"订单-富岛农友";
			String body = "猫币充值"+ur.getId()+"订单-富岛农友";
//			Double money = 0.0+ur.getRechargeSum();
			Double money = 0.01;
			if(money!=null && money>0){
				total_fee = money.toString();
			}else{
				return Utils.getErrorStatusJson("支付金额异常").toJSONString();
			}
			String notify_url=map.get("ORDER_NOTIFY_URL").toString();
			String orderSpec="partner=\""+map.get("PARTNER").toString()+"\""
					+ "&seller_id=\""+map.get("SELLER_ID").toString()+"\""
					+ "&out_trade_no=\""+ur.getId().substring(0, 30)+"\""
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
					String userRechargeId = params.get("out_trade_no").toString();
					FDUserRecharge ur = userRechargeDao.findOne(" where id like '"+userRechargeId+"%' ");
					if(ur!=null && ur.getStatus().equals(Status.USERRECHARGE_STATUS_WAITVERIFY)){
						ur.setStatus(Status.USERRECHARGE_STATUS_SUCCESS);
						ur.setPayWayNumber(params.get("trade_no").toString());
						ur.setVerifyDate(new Date());
						userRechargeDao.update(ur);
					}
				}
			}
			return Utils.getSuccessStatusJson("充值订单状态已修改").toJSONString();
		}catch(Exception e){
			e.printStackTrace();
			return Utils.getErrorStatusJson("异步回调接口异常").toJSONString();
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getWXPrepayId(Integer rechargeSum, String userId, String spbill_create_ip, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response){
		JSONObject json = new JSONObject();
		try {
			String total_fee="";
			FDUserRecharge ur = new FDUserRecharge();
			ur.setId(UUID.randomUUID().toString());
			ur.setRechargeUserId(userId);
			ur.setPayWay("wxpay");
			ur.setRechargeDate(new Date());
			ur.setRechargeSum(rechargeSum);
			ur.setRechargeType(Status.CATCOINCHANGE_CHANGETYPE_RECHARGECATCOIN);
			ur.setStatus(Status.USERRECHARGE_STATUS_WAITVERIFY);
			userRechargeDao.add(ur);
			
			String body = "猫币充值"+ur.getId()+"订单-富岛农友";
//			Double money = 0.0+ur.getRechargeSum();
			Double money = 0.01;
			String nonce_str = Utils.MD5(Utils.getRandom(100000000));
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
					+ "&out_trade_no="+ur.getId().substring(0, 30)
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
					+ "<out_trade_no>"+ur.getId().substring(0, 30)+"</out_trade_no>"
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
				//修改订单状态
				String userRechargeId = root.element("out_trade_no").getTextTrim();
				FDUserRecharge ur = userRechargeDao.findOne(" where id like '"+userRechargeId+"%' ");
				if(ur!=null && ur.getStatus().equals(Status.USERRECHARGE_STATUS_WAITVERIFY)){
					ur.setStatus(Status.USERRECHARGE_STATUS_SUCCESS);
					ur.setPayWayNumber(root.element("transaction_id").getTextTrim());
					ur.setVerifyDate(new Date());
					userRechargeDao.update(ur);
					
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
	
	
}
