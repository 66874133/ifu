package com.funnel.smarker.builder;

import java.util.List;
import java.util.Map;

import com.cpkf.yyjd.tools.data.DataRecord;
import com.cpkf.yyjd.tools.data.mongo.MongoHandler;
import com.cpkf.yyjd.tools.sql.SelectSQL;

public class HomeDataBuilder implements IDataBuilder {

	private String db;
	
	public HomeDataBuilder(String db) {
		this.db = db;
	}
	public void build(Map<String, Object> datas) {
		MongoHandler handler = new MongoHandler();

		SelectSQL selectSQL = new SelectSQL("site");
		selectSQL.addWhereCondition("sub", "www");
		List<DataRecord> dataRecords = handler.selectList(db,
				selectSQL);

		if (dataRecords.size() > 0) {
			DataRecord dataRecord = dataRecords.get(0);

			String[] fields = dataRecord.getAllFields();

			for (int i = 0; i < fields.length; i++) {
				datas.put(fields[i], dataRecord.getFieldValue(fields[i]));
			}
		}

	}
}
