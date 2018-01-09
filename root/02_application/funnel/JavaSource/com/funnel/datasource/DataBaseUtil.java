package com.funnel.datasource;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.funnel.datasource.connection.ConnectionManager;
import com.funnel.datasource.connection.ObjectValueMapper;
import com.funnel.sys.SysParam;

public class DataBaseUtil {

	private static Connection conn = ConnectionManager.getConnection();

	private static ObjectValueMapper mapper = new ObjectValueMapper();

	public static List<?> select(String sqlId, Map<String, Object> map,
			Class<?> userCla) throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {

		List<Object> l = new ArrayList<Object>();
		List<Map<String, Object>> values = select(sqlId, map);
		for (Map<String, Object> m : values) {
			l.add(mapper.toObject(m, userCla));
		}

		return (List<?>) l;

	}

	public static List<Map<String, Object>> select(String sqlId,
			Map<String, Object> map) {

		List<Map<String, Object>> values = null;
		IbatisSql ibatisSql = IbatisConfUtil.getIbatisSql(sqlId);
		String SQL = ibatisSql.getSql();

		if (null != map) {
			Iterator<String> iterator = map.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				SQL = SQL.replace("#" + key + "#", "'"
						+ map.get(key).toString() + "'");
			}
		}

		try {

			values = new QueryRunner().query(conn, SQL, new MapListHandler());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	public static void delete(String sqlId, Map<String, Object> map) {

		IbatisSql ibatisSql = IbatisConfUtil.getIbatisSql(sqlId);
		String SQL = ibatisSql.getSql();
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			SQL = SQL.replace("#" + key + "#", map.get(key).toString());
		}
		try {

			new QueryRunner().query(conn, SQL, new MapListHandler());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static int update(String sqlId, Object o) {

		int values = -1;
		IbatisSql ibatisSql = IbatisConfUtil.getIbatisSql(sqlId);
		String SQL = ibatisSql.getSql();

		Map<String, Object> map = (Map<String, Object>) o;
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			SQL = SQL.replace("#" + key + "#", map.get(key).toString());
		}
		try {

			values = new QueryRunner().update(conn, SQL, new BeanListHandler(
					Class.forName(ibatisSql.getResultClass())), new Object[] {
					"JohnGao1", "123" });

		} catch (Exception e) {
			e.printStackTrace();
		}
		return values;
	}

	public static void insert(String sqlId, Object o) {

		IbatisSql ibatisSql = IbatisConfUtil.getIbatisSql(sqlId);
		String SQL = ibatisSql.getSql();

		Map<String, Object> map = (Map<String, Object>) o;
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			SQL = SQL.replace("#" + key + "#", map.get(key).toString());
		}
		try {

			if (null != ibatisSql.getResultClass()) {

				new QueryRunner().insert(
						conn,
						SQL,
						new BeanListHandler(Class.forName(ibatisSql
								.getResultClass())));

			} else {
				new QueryRunner().insert(conn, SQL, new MapListHandler());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void batchInsert(String sqlId, List<Object> list)
			throws IllegalArgumentException, IllegalAccessException {

		int[] result = null;
		IbatisSql ibatisSql = IbatisConfUtil.getIbatisSql(sqlId);
		String SQL = ibatisSql.getSql();
		String preparedStatement = getPreparedStatementSql(SQL);

		List<String> parameters = getPreparedStatementFields(SQL);

		String parameterClassName = "";
		if (null != ibatisSql.getParameterClass()
				&& !"".equals(ibatisSql.getParameterClass())) {
			parameterClassName = ibatisSql.getParameterClass();
		}

		List<String[]> l = new ArrayList<String[]>();
		String[][] data = null;
		if (list.size() > 0) {
			if (list.get(0) instanceof String[]) {
				data = list.toArray(new String[0][0]);

			} else {
				for (Object o : list) {

					String[] strs = toStringArray(o, parameterClassName,
							parameters);
					l.add(strs);
				}
				data = l.toArray(new String[0][0]);
			}
		}

		try {

			if (null != ibatisSql.getResultClass()
					&& !"".equals(ibatisSql.getResultClass())) {

				result = new QueryRunner().batch(conn, preparedStatement, data);

			} else {
				result = new QueryRunner().batch(conn, preparedStatement, data);
			}

			// DbUtils.commitAndClose(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("batch插入，返回相应结果数组" + Arrays.toString(result));
	}

	/**
	 * 将对象转换成String数组
	 * 
	 * @param o
	 * @param parameterClassName
	 * @param parameters
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static String[] toStringArray(Object o, String parameterClassName,
			List<String> parameters) throws IllegalArgumentException,
			IllegalAccessException {
		List<String> values = new ArrayList<String>();
		if ("".equals("java.lang.String")) {
			values.add(o.toString());
		} else {
			Map<String, String> map = mapper.toFieldValuesMap(o);

			for (String p : parameters) {
				values.add(map.get(p));
			}
		}

		return values.toArray(new String[0]);
	}

	/**
	 * 获取预处理语句中占位符，并按顺序排列
	 * 
	 * @param preparedStatement
	 * @return
	 */
	public static List<String> getPreparedStatementFields(
			String preparedStatement) {
		List<String> list = new ArrayList<String>();
		Pattern p = Pattern.compile("#(.*?)#");
		Matcher matcher = p.matcher(preparedStatement);

		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				if (matcher.start(i) > -1) {
					list.add(matcher.group(i).trim());
				}
			}
		}

		return list;
	}

	public static String getPreparedStatementSql(String preparedStatement) {

		return preparedStatement.replaceAll("#(.*?)#", "?");
	}

	public static void main(String[] args) throws Exception {
		SysParam sysParam = new SysParam();
		sysParam.setParamKey("aaa");
		sysParam.setParamType("aaa");
		sysParam.setParamValue("111");
		List<Object> list = new ArrayList<Object>();
		List<Object> list2 = new ArrayList<Object>();
		list2.add(sysParam);
		list.add(new String[] { "bbb", "bbb", "111" });
		list.add(new String[] { "bbb", "222", "111" });
		DataBaseUtil.batchInsert("cache.insertParam", list);
		DataBaseUtil.batchInsert("cache.insertParam", list2);

		System.out.println(DataBaseUtil.select("cache.querySysParams", null));

	}

}
