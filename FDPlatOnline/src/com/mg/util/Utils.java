package com.mg.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.AxisFault;
import org.apache.commons.codec.binary.Base64;
import org.tempuri.ServiceStub;
import org.tempuri.ServiceStub.SendMessage;
import org.tempuri.ServiceStub.SendMessageResponse;
import org.tempuri.ServiceStub.SendState;

import com.alibaba.fastjson.JSONObject;
import com.mg.dao.OrderDao;
import com.mg.entity.FDOrder;
import com.qiniu.api.auth.digest.Mac;
import com.qiniu.api.config.Config;
import com.qiniu.api.io.IoApi;
import com.qiniu.api.io.PutExtra;
import com.qiniu.api.io.PutRet;
import com.qiniu.api.rs.PutPolicy;

public final class Utils {

	private Utils() {
	}

	private static SimpleDateFormat sfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static DecimalFormat df = new DecimalFormat("0.00");
	
	private static final String ERROR_STRING = "错误";
	private static final String WARNING_STRING = "警告";
	private static final String INFO_STRING = "信息";
	public static Properties pps;
	public static Properties cpps;
	
	public static final String CLASS_PATH = Utils.class.getResource("/").getPath();

	public static void print(String type, String m) {
		System.out.println(sfm.format(new Date()) + " " + type + " " + m);
	}
	
	public static SimpleDateFormat getSimpleDateFormat(){
		return sfm;
	}
	
	public static String getCurrentTimeString() {
		return sfm.format(new Date());
	}
	
	public static SimpleDateFormat getTimestampFormat(){
		return timestampFormat;
	}
	
	public static String DoubleToString(Double d) {
		return df.format(d);
	}
	
	public static String getTimestampString(){
		return timestampFormat.format(new Date());
	}

	public static void info(String m) {
		print(INFO_STRING, m);
	}
	
	public static void error(String m){
		print(ERROR_STRING, m);
	}
	
	public static void warning(String m){
		print(WARNING_STRING, m);
	}
	
	public static void write(String m){
		System.out.println(m);
	}
	
	public static void log(Class<?> c, String m){
		System.out.println(c.getName() + " >> " + m);
	}
	
	/**
	 * 将字符串首字符变成小�?
	 * @param str 源字符串
	 * @return 新字符串
	 */
	public static String firsttoLowerCase(String str){
		if(str == null || "".equals(str))
			return str;
		char[] c = str.toCharArray();
		c[0] = str.substring(0, 1).toLowerCase().toCharArray()[0];
		return new String(c);
	}
	
	public static String firsttoUpperCase(String str){
		if(str == null || "".equals(str))
			return str;
		char[] c = str.toCharArray();
		c[0] = str.substring(0, 1).toUpperCase().toCharArray()[0];
		return new String(c);
	}
	
	public static boolean isEmpty(String s){
		return s == null ? true : ("".equals(s) ? true : false);
	}
	
	public static boolean isEmpty(List<?> ls){
		return ls == null ? true : (ls.size() == 0 ? true : false);
	}
	
	public static boolean isEmpty(Map<?,?> m){
		return m == null ? true : (m.size() == 0 ? true : false);
	}
	
	public static boolean isAvalidUrl(String url){
		if(isEmpty(url))
			return true;
		if("/".equals(url))
			return true;
		return url.startsWith("/") && !url.endsWith("/");
	}
	
	public static String getFileExtend(String path){
		if(Utils.isEmpty(path))
			return "";
		int index = path.lastIndexOf('.');
		if(index == -1)
			return "";
		return path.substring(index);		
	}
	
	public static JSONObject getErrorStatusJson(String m){
		JSONObject json = new JSONObject();
		json.put(Vars.JSON_STATUS, Vars.FAILURE);
		json.put(Vars.JSON_MSG, m);
		return json;
	}
	
	public static JSONObject getSuccessStatusJson(Pager pager){
		JSONObject json = new JSONObject();
		json.put("status","success");
		json.put("msg", pager!=null && pager.getItems()!=null ? "操作成功" : "暂无数据");
		json.put("rows", pager.getItems());
		json.put("total", pager.getTotal());
		return json;
	}
	
	public static JSONObject getSuccessStatusJson(String m){
		JSONObject json = new JSONObject();
		json.put(Vars.JSON_STATUS, "success");
		json.put(Vars.JSON_MSG, m);
		return json;
	}
	
	public static <T> JSONObject getSuccessEntityJson(T t){
		JSONObject json = new JSONObject();
		json.put("status","success");
		json.put("msg", t!=null ? "操作成功" : "暂无数据");
		json.put("e", t);
		return json;
	}

	public static SimpleDateFormat getShortDateFormat() {
		return shortDateFormat;
	} 
	public static Map<String,String> uploadqiniu(InputStream file){
		Map<String,String> map = new HashMap<String,String>();
		try {
			Config.ACCESS_KEY = "_AkLMPv3Z7bTOqJSdGPyukbBX3UrCwX4bTrkBD9D";
			Config.SECRET_KEY = "Op0r1MqzZb7zUyXBP6bI2NxRPqKdjqgEAzYRsGbp";
			Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
			String bucketName = "ulishop";
			PutPolicy putPolicy = new PutPolicy(bucketName);
			String uptoken = putPolicy.token(mac);
	        PutExtra extra = new PutExtra();
	        PutRet ret = IoApi.Put(uptoken, null, file, extra);
	        if(ret!=null){
	        	map.put("key", ret.getKey());
	        	map.put("hash", ret.getHash());
	        }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	/**
	 * 获取七牛的TOKEN
	 * @return
	 */
	public static String getqiniutoken(){
		String uptoken = "";
		try {
			Config.ACCESS_KEY = "_AkLMPv3Z7bTOqJSdGPyukbBX3UrCwX4bTrkBD9D";
			Config.SECRET_KEY = "Op0r1MqzZb7zUyXBP6bI2NxRPqKdjqgEAzYRsGbp";
			Mac mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
			String bucketName = "ulishop";
			PutPolicy putPolicy = new PutPolicy(bucketName);
			uptoken = putPolicy.token(mac);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return uptoken;
	}
	
	public static String getID(){
		return UUID.randomUUID().toString();
	}
	
	public static void getProperties(){
		if(pps==null){
			pps = new Properties();
			try {
				pps.load(new FileInputStream(new File(Utils.class.getResource("/menu.properties").getPath())));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(cpps==null){
			cpps = new Properties();
			Set<Map.Entry<Object,Object>> entrySet=pps.entrySet();
            for(Iterator<Map.Entry<Object,Object>> it=entrySet.iterator();it.hasNext();){
               Map.Entry<Object,Object> me=it.next();
               String key=(String)me.getKey();
               String value=(String)me.getValue();
               cpps.setProperty(value, key);
           }
		}
	}
	
	public static String menuToUrl(String menuId){
		Utils.getProperties();
		return (String) cpps.getProperty(menuId);
	}
	
	public static String urlToMenu(String url){
		Utils.getProperties();
		return (String) pps.getProperty(url);
	}
	
	public static String turnCodeWhenGet(String in, HttpServletRequest request){
		return in;
	}
	
	/**
	 * 获取图片验证码
	 */
	private static Random random = new Random();
	private static int width = 68;// 图片宽
	private static int height = 38;// 图片高
	private static int lineSize = 40;// 干扰线数量
	private static int stringNum = 4;// 随机产生字符数量
	private static String randString = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";// 随机产生的字符串

	public static BufferedImage getImagCode(HttpServletRequest request,HttpServletResponse response) {
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_BGR);
		Graphics g = image.getGraphics();// 产生Image对象的Graphics对象,改对象可以在图像上进行各种绘制操作
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ROMAN_BASELINE, 20));
		g.setColor(getRandColor(110, 133));
		// 绘制干扰线
		for (int i = 0; i <= lineSize; i++) {
			drowLine(g);
		}
		// 绘制随机字符
		String randomString = "";
		for (int i = 0; i < stringNum; i++) {
			randomString = drowString(g, randomString, i);
		}
        Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
        	for(Cookie cookie : cookies){
                if(cookie.getName().equals("imgCode")){
                	cookie.setValue(getPassWord(randomString.toUpperCase(), "7480"));
                	cookie.setMaxAge(60*60*3);
        	        cookie.setPath("/");
                    response.addCookie(cookie);
                    g.dispose();
            		return image;
                }
            }
        }
        Cookie cookie = new Cookie("imgCode", getPassWord(randomString.toUpperCase(), "7480"));
        cookie.setMaxAge(60*60*3);
        cookie.setPath("/");
        response.addCookie(cookie);
		g.dispose();
		return image;
	}

	private static void drowLine(Graphics g) {
		int x = random.nextInt(width);
		int y = random.nextInt(height);
		int xl = random.nextInt(13);
		int yl = random.nextInt(15);
		g.drawLine(x, y, x + xl, y + yl);
	}

	private static String drowString(Graphics g, String randomString, int i) {
		g.setFont(getFont());
		g.setColor(new Color(random.nextInt(101), random.nextInt(111), random
				.nextInt(121)));
		String rand = String.valueOf(getRandomString(random.nextInt(randString
				.length())));
		randomString += rand;
		g.translate(random.nextInt(3), random.nextInt(3));
		g.drawString(rand, 13 * i + 6, 25);
		return randomString;
	}

	private static Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc - 16);
		int g = fc + random.nextInt(bc - fc - 14);
		int b = fc + random.nextInt(bc - fc - 18);
		return new Color(r, g, b);
	}

	private static Font getFont() {
		return new Font("Fixedsys", Font.CENTER_BASELINE, 20);
	}

	private static String getRandomString(int num) {
		return String.valueOf(randString.charAt(num));
	}
	
	public static String getCookieValue(HttpServletRequest request, String key){
		if(Utils.isEmpty(key)){
			return null;
		}
		Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
        	for(Cookie cookie : cookies){
                if(cookie.getName().equals(key)){
                    return cookie.getValue();
                }
            }
        }
        return null;
	}
	
	public static Cookie getCookie(HttpServletRequest request, String key){
		if(Utils.isEmpty(key)){
			return null;
		}
		Cookie[] cookies = request.getCookies();
        if (cookies!=null) {
        	for(Cookie cookie : cookies){
                if(cookie.getName().equals(key)){
                    return cookie;
                }
            }
        }
        return null;
	}
	
	public static void delCookie(HttpServletRequest request, HttpServletResponse response, String key){
		Cookie[] cookies = request.getCookies();
        if (null!=cookies) {
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(key)){
                    cookie.setValue(null);
                    cookie.setMaxAge(0);// 立即销毁cookie
                    cookie.setPath("/");
                    response.addCookie(cookie);
                    return;
                }
            }
        }
	}
	
	public static void resetCookie(HttpServletRequest request, HttpServletResponse response, String key, String value){
		if(!Utils.isEmpty(key)){
			Cookie[] cookies = request.getCookies();
	        if (cookies!=null) {
	        	for(Cookie cookie : cookies){
	                if(cookie.getName().equals(key)){
	                	cookie.setValue(value);
	                	cookie.setMaxAge(60*60*3);
	        	        cookie.setPath("/");
	                    response.addCookie(cookie);
	                    return;
	                }
	            }
	        }
	        Cookie cookie = new Cookie(key,value);
	        cookie.setMaxAge(60*60*3);
	        cookie.setPath("/");
            response.addCookie(cookie);
            return;
		}
	}
	
	//发送短信
	public static void sendmsg(String tel, String msgContent)
			throws NoSuchAlgorithmException {
		SendMessage sendMessage = new SendMessage();
		sendMessage.setId(300);
		sendMessage.setName("forulin");
		sendMessage.setPsw("8325794");
		sendMessage.setPhone(tel);
		sendMessage.setMessage(msgContent);
		sendMessage.setTimestamp(0);
		try {
			ServiceStub serviceStub = new ServiceStub();
			SendMessageResponse smr = serviceStub.sendMessage(sendMessage);
			SendState ss = smr.getSendMessageResult();
			System.out.println("id:" + ss.getId());
			System.out.println("state:" + ss.getState());
		} catch (AxisFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public static String MD5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("UTF-8"));
			StringBuffer buf = new StringBuffer();
			for (byte b : md.digest())
				buf.append(String.format("%02x",b&0xff));
			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getPassWord(String str,String salt){
		String md51 = MD5(str);
		String password = md51;
		if(salt!=null){
			password =  MD5(md51+salt);
		}
		return password;
	}
	
	public static String getOrderNumber(OrderDao orderDao){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String orderNumber = sdf.format(new Date())+(int)(Math.random()*10000);
		List<FDOrder> orderList = orderDao.findBycondition(" where orderNumber = '"+orderNumber+"'");
		if(orderList!=null&&orderList.size()>0){
			return getOrderNumber(orderDao);
		}
		return orderNumber;
	}
	
	public static String getRandom(Integer sum){
		Integer i = (int) (Math.random()*sum);
		i += sum;
		return (i.toString()).substring(1);
	}
	
	public static String base64_decode(String str){
        if(Utils.isEmpty(str))
        	return "";
        try {
			return new String(Base64.decodeBase64(str.getBytes("utf-8")), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static String getIpAddr(HttpServletRequest request) {     
	      String ip = request.getHeader("x-forwarded-for");     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("Proxy-Client-IP");     
	     }     
	      if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	         ip = request.getHeader("WL-Proxy-Client-IP");     
	      }     
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {     
	          ip = request.getRemoteAddr();     
	     }     
	     return ip;     
	}
}
