package com.mg.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.alibaba.fastjson.JSONObject;

@SuppressWarnings({ "rawtypes" })
//未使用预编译
public class ConnectionUtil {
	private static String url="";
	private static String user="";
	private static String password="";
	private static String driverName="";
	
	static {
		Properties pps = new Properties();
		try {
			pps.load(new FileInputStream(ConnectionUtil.class.getResource("../../../dbconfig.properties").getPath()));
			Enumeration enum1 = pps.propertyNames();
			while(enum1.hasMoreElements()) {
	              String strKey = (String) enum1.nextElement();
	              if("url".equals(strKey)){
	            	 url = pps.getProperty(strKey);
	              }else if("driverClassName".equals(strKey)){
	            	  driverName = pps.getProperty(strKey);
	              }else if("username".equals(strKey)){
	            	  user = pps.getProperty(strKey);
	              }else if("password".equals(strKey)){
	            	  password = pps.getProperty(strKey);
	              }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException,ClassNotFoundException {
		Class.forName(driverName);
		return DriverManager.getConnection(url, user, password);
	}
	
	private static void closeConnection(Connection con,Statement st,ResultSet re){
		if(re!=null){
			try {
				re.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(st!=null){
			try {
				st.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(con!=null){
			try {
				con.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static JSONObject getRows(String sql){
		JSONObject json = new JSONObject();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = getConnection();
			st = con.createStatement();
			rs = st.executeQuery(sql);
			List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
			Map<String, Object> item = new HashMap<String, Object>();
			ResultSetMetaData m = rs.getMetaData();
			int cols = m.getColumnCount();
			while (rs.next()) {
				item = new HashMap<String, Object>();
				for (int i = 1; i <= cols; i++){
					Object value = rs.getObject(i);
					if(value == null)
						value = "";
					item.put(m.getColumnName(i).toUpperCase(), value);
				}
				rows.add(item);
			}
			json.put("rows", rows!=null && rows.size()>0?rows:null);
			json.put("msg", "数据返回成功");
			json.put("status","success");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			json.put("msg", "系统异常");
			json.put("status","failure");
		} catch (SQLException e) {
			e.printStackTrace();
			json.put("msg", "系统异常");
			json.put("status","failure");
		}finally{
			closeConnection(con, st, rs);
		}
		return json;
	}
}
