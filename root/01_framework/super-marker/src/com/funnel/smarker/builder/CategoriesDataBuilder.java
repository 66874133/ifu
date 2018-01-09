package com.funnel.smarker.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.cpkf.yyjd.tools.data.mongo.MongoHandler;

public class CategoriesDataBuilder implements IDataBuilder {

	private String db;

	public CategoriesDataBuilder(String db) {
		this.db = db;
	}

	public void build(Map<String, Object> datas) {
		MongoHandler handler = new MongoHandler();

		Iterator<Document> it = handler
				.getCollection(db, "category").find().iterator();

		List<Document> list = new ArrayList<Document>();
		List<Document> tags = new ArrayList<Document>();
		while (it.hasNext()) {
			Document doc = it.next();

			if (doc.getString("type").equals("cat")) {
				list.add(doc);
			} else {
				tags.add(doc);
			}

		}
		datas.put("categories", list);
		datas.put("tags", tags);
	}
}
