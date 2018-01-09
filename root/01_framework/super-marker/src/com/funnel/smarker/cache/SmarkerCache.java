package com.funnel.smarker.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.cpkf.yyjd.tools.data.mongo.MongoHandler;
import com.mongodb.BasicDBObject;

public class SmarkerCache {

	private Map<String, Document> idToArticle = new HashMap<String, Document>();

	private Map<String, Document> aliasToDocument = new HashMap<String, Document>();

	private List<String> articleIds = new ArrayList<String>();
	private List<String> categoryNames = new ArrayList<String>();
	private MongoHandler handler = new MongoHandler();

	private static Map<String, SmarkerCache> map = new HashMap<String, SmarkerCache>();;

	private SmarkerCache(String db) {
		initCache(db);
	}

	public static SmarkerCache getInstance(String db) {
		if (!map.containsKey(db)) {
			map.put(db, new SmarkerCache(db));
		}
		return map.get(db);
	}

	public List<String> getCategoryNames() {

		return categoryNames;
	}

	public List<String> getArticleIds() {

		return articleIds;
	}

	public Document getArticle(String id) {

		return idToArticle.get(id);
	}

	public Document getCategory(String alias) {

		return aliasToDocument.get(alias);

	}

	public void initCache(String db) {
		cacheCategories(db);
		cacheArticles(db);
	}

	private void cacheArticles(String db) {
		Iterator<Document> ita = handler.getCollection(db, "article").find()
				.iterator();

		while (ita.hasNext()) {
			Document article = ita.next();

			String strs = article.getString("category");

			String[] cats = strs.split(",");

			List<Document> categories = new ArrayList<Document>();
			for (String s : cats) {
				Document category = aliasToDocument.get(s);
				categories.add(category);
			}

			article.put("article.categories", categories);

			idToArticle.put(article.get("_id").toString(), article);

		}

		articleIds.addAll(idToArticle.keySet());
	}

	private void cacheCategories(String db) {

		Iterator<Document> it = handler.getCollection(db, "category").find()
				.iterator();

		while (it.hasNext()) {

			Document doc = it.next();

			Iterator<String> data = doc.keySet().iterator();
			while (data.hasNext()) {
				String key = data.next();
				if ("alias".equalsIgnoreCase(key)) {
					Object o = doc.get(key);
					aliasToDocument.put((String) o, doc);
					categoryNames.add((String) o);
					break;
				}

			}

		}

	}
}
