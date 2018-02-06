package com.mg.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONObject;

@Component
public class ConUtil {
	
	@Autowired
	DruidDataSource dataSource = null;
	
	private Connection getConnection() throws SQLException,ClassNotFoundException {
		return dataSource.getConnection();
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
	
	public JSONObject getRows(String sql){
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
	
	public List<Map<String, Object>> getData(String sql){
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
			if(rows==null||rows.size()<1){
				return null;
			}else{
				return rows;
			}
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}finally{
			closeConnection(con, st, rs);
		}
	}
}
