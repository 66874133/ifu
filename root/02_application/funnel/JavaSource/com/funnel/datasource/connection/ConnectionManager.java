package com.funnel.datasource.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionManager {

	// 创建连接池的数据源对象
	// 指定的是从c3p0-config.xml配置文件中选择那个链配置进行连接
	// 读取c3p0-config.xml name为mysql
	// private static ComboPooledDataSource cpds = new ComboPooledDataSource(
	// "oracle");
	private static ComboPooledDataSource cpds = null;

	public static Connection getConnection() {

		try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static Connection newConnection(String jdbcDriver, String jdbcUrl,
			String user, String pwd) {
		Connection conn = null;
		try {
			Class.forName(jdbcDriver);
			conn = DriverManager.getConnection(jdbcUrl, user, pwd);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String SQL = "select * from GOODS_RANK_NINE_NINE";
		Connection conn = ConnectionManager.getConnection();
		try {
			List<Map<String, Object>> result = new QueryRunner().query(conn,
					SQL, new MapListHandler());

			System.out.println("result=" + result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
