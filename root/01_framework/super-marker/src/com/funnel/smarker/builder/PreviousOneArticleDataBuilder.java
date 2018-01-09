package com.funnel.smarker.builder;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.funnel.smarker.cache.SmarkerCache;

public class PreviousOneArticleDataBuilder implements IDataBuilder {

	private String articleId;
	private String db;

	public PreviousOneArticleDataBuilder(String db, String articleId) {
		this.articleId = articleId;
		this.db = db;
	}

	public void build(Map<String, Object> datas) {

		List<String> ids = SmarkerCache.getInstance(db).getArticleIds();

		int index = ids.indexOf(articleId) - 1;

		Document doc = null;
		if (index > 0 && index < ids.size()) {
			doc = SmarkerCache.getInstance(db).getArticle(ids.get(index));
		} else {
			doc = SmarkerCache.getInstance(db).getArticle(
					ids.get(ids.size() - 1));
		}

		Iterator<String> ita = doc.keySet().iterator();
		while (ita.hasNext()) {

			String key = ita.next();

			Object o = doc.get(key);

			datas.put("pre." + key, (String) o.toString());

		}

	}

}
