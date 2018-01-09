package com.funnel.smarker.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.cpkf.yyjd.tools.data.mongo.MongoHandler;
import com.funnel.smarker.cache.SmarkerCache;
import com.mongodb.BasicDBObject;

public class ArticlesDataBuilder implements IDataBuilder {

	private String db;

	public ArticlesDataBuilder(String db) {
		this.db = db;
	}

	public void build(Map<String, Object> datas) {
		MongoHandler handler = new MongoHandler();

		Iterator<Document> ita = handler.getCollection(db, "article").find()
				.sort(new BasicDBObject("dateTime", -1)).iterator();

		List<Document> list = new ArrayList<Document>();
		while (ita.hasNext()) {
			Document doc = ita.next();

			Document article = SmarkerCache.getInstance(db).getArticle(
					doc.get("_id").toString());

			list.add(article);

		}
		datas.put("articles", list);

	}

}
