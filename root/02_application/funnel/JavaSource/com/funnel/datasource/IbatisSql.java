package com.funnel.datasource;

public class IbatisSql {

	private String id;
	private String resultClass;
	private String parameterClass;
	private String sql;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getResultClass() {
		return resultClass;
	}

	public void setResultClass(String resultClass) {
		this.resultClass = resultClass;
	}

	public String getParameterClass() {
		return parameterClass;
	}

	public void setParameterClass(String parameterClass) {
		this.parameterClass = parameterClass;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		return "IbatisSql [id=" + id + ", resultClass=" + resultClass
				+ ", parameterClass=" + parameterClass + ", sql=" + sql + "]";
	}

}
