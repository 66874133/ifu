package com.funnel.smarker.builder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.funnel.smarker.cache.SmarkerCache;

public class NextOneArticleDataBuilder implements IDataBuilder {

	private String db;
	private String articleId;

	public NextOneArticleDataBuilder(String db,String articleId) {
		this.db = db;
		this.articleId = articleId;
	}

	public void build(Map<String, Object> datas) {

		List<String> ids = SmarkerCache.getInstance(db).getArticleIds();

		int index = ids.indexOf(articleId) + 1;

		Document doc = null;
		if (index > 0 && index < ids.size()) {
			doc = SmarkerCache.getInstance(db).getArticle(ids.get(index));
		} else {
			doc = SmarkerCache.getInstance(db).getArticle(ids.get(0));
		}

		Iterator<String> ita = doc.keySet().iterator();
		while (ita.hasNext()) {

			String key = ita.next();

			Object o = doc.get(key);

			datas.put("next." + key, (String) o.toString());

		}

	}

}
