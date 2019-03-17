package com.ddnet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.Element;
@Slf4j
public class DB {
	static  String DRIVER;
	static String DATABASE;
	static  String USER;
	static  String PASSWORD;
	static{
		Document doc =	Dom4Jutil.getDocument("/pandora/ddnet/DAO.xml");
		Element root = doc.getRootElement();
		DRIVER = root.element("driver").getTextTrim();
		DATABASE = root.element("database").getTextTrim()+"?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8";
		USER = root.element("user").getTextTrim();
		PASSWORD = root.element("password").getTextTrim();
	}
	public static Connection getConn() {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			
			conn = DriverManager.getConnection(DATABASE,USER, PASSWORD);
		} catch (Exception e) {
			log.info("error in getConn:{}",e);
		} 
		return conn;
	}
	
	public static Statement createStmt(Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			log.info("error in createStmt:{}",e);
		}
		return stmt;
	}
	
	public static ResultSet executeQuery(Statement stmt, String sql) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			log.info("error in executeQuery:{}",e);
		}
		return rs;
	}
	
	public static int executeUpdate(Connection conn, String sql) {
		int ret = 0;
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			ret = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			log.info("error in executeUpdate:{}",e);
		} finally {
			close(stmt);
		}
		return ret;
	}
	
	public static PreparedStatement prepareStmt(Connection conn, String sql) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
		} catch (SQLException e) {
			log.info("error in prepareStmt:{}",e);
		}
		return pstmt;
	}
	
	public static PreparedStatement prepareStmt(Connection conn, String sql, int autoGeneratedKeys) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql, autoGeneratedKeys);
		} catch (SQLException e) {
			log.info("error in prepareStmt:{}",e);
		}
		return pstmt;
	}
	
	public static void close(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.info("error in close:{}",e);
			}
			conn = null;
		}
	}
	
	public static void close(Statement stmt) {
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				log.info("error in close:{}",e);
			}
			stmt = null;
		}
	}
	
	public static void close(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				log.info("error in close:{}",e);
			}
			rs = null;
		}
	}
}
