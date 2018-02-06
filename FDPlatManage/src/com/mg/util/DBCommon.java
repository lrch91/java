package com.mg.util;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public final class DBCommon {

	private static Properties dbConfig = new Properties();

	static {
		loadCommonDbConfig();
	}

	/**
	 * 读取 XML 中SQL的配置
	 */
	public static void loadCommonDbConfig() {
		try {
			dbConfig.clear();
			dbConfig.load(new FileInputStream(DBCommon.class.getResource("../../../dbconfig.properties").getPath()));
			Class.forName(dbConfig.getProperty("driverClassName"));
		} catch (Exception e) {
			e.printStackTrace();
			Utils.log(DBCommon.class, "读取公用数据层配置失败。");
		}
	}

	public static Connection getConnection() throws SQLException {
		if (dbConfig == null)
			return null;
		String url = dbConfig.getProperty("url");
		String uid = dbConfig.getProperty("username");
		String pwd = dbConfig.getProperty("password");
		return DriverManager.getConnection(url, uid, pwd);
	}

	@SuppressWarnings("resource")
	public static Pager query(String sql) throws SQLException {
		Connection c = null;
		Statement e = null;
		ResultSet r = null;
		try {
			c = getConnection();
			e = c.createStatement();
			r = e.executeQuery(sql);
			ResultSetMetaData m = r.getMetaData();
			int len = m.getColumnCount();
			List<Map<String,Object>> ls = new ArrayList<Map<String,Object>>();
			while(r.next()){
				Map<String,Object> item = new HashMap<String,Object>();
				for(int i=1;i<=len;i++)
					item.put(m.getColumnName(i).toLowerCase(), r.getObject(i));
				ls.add(item);
			}
			int rows = 0;
			if(e.getMoreResults()){
				r = e.getResultSet();
				r.next();
				rows = r.getInt(1);
			}
			Pager p = new Pager();
			p.setItems(ls);
			p.setTotal(rows);
			return p;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (r != null)
				r.close();
			if (e != null)
				e.close();
			if (c != null)
				c.close();
		}
	}
	
	public static Map<String,Object> querySingle(String sql) throws SQLException {

		Connection c = null;
		Statement e = null;
		ResultSet r = null;
		try {
			
			c = getConnection();
			e = c.createStatement();
			r = e.executeQuery(sql);
			ResultSetMetaData m = r.getMetaData();
			if(!r.next())
				return null;
			int len = m.getColumnCount();
			Map<String,Object> item = new HashMap<String,Object>();
			for(int i=1;i<=len;i++)
				item.put(m.getColumnName(i), r.getObject(i));
			return item;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (r != null)
				r.close();
			if (e != null)
				e.close();
			if (c != null)
				c.close();
		}
	}
	
	public static int executeBatch(String[] sqls) throws SQLException{
		Connection c = null;
		Statement e = null;
		boolean success = false;
		try {			
			c = getConnection();
			e = c.createStatement();
			int rc = 0;
			for(String sql : sqls)
				rc = e.executeUpdate(sql);
			success = true;
			return rc;
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		} finally {
			if (e != null)
				e.close();
			if (c != null){
				if(success)
					c.commit();
				else
					c.rollback();
				c.setAutoCommit(true);
				c.close();
			}
		}
	}
	
	
}
