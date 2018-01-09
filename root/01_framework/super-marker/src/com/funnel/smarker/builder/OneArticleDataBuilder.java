package com.funnel.smarker.builder;

import java.util.Iterator;
import java.util.Map;

import org.bson.Document;

import com.funnel.smarker.cache.SmarkerCache;

public class OneArticleDataBuilder implements IDataBuilder {

	private String db;
	private String articleId;

	public OneArticleDataBuilder(String db, String articleId) {
		this.db = db;
		this.articleId = articleId;
	}

	public void build(Map<String, Object> datas) {

		Document doc = SmarkerCache.getInstance(db).getArticle(articleId);

		Iterator<String> ita = doc.keySet().iterator();
		while (ita.hasNext()) {

			String key = ita.next();

			Object o = doc.get(key);

			datas.put(key, o);

		}

	}

}
