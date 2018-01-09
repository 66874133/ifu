package com.funnel.smarker.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.funnel.smarker.cache.SmarkerCache;

public class ArticlesGroupByCategoryDataBuilder implements IDataBuilder {

	private String db;
	private String categoryName;

	public ArticlesGroupByCategoryDataBuilder(String db,String categoryName) {
		this.db = db;
		this.categoryName = categoryName;
	}

	public void build(Map<String, Object> datas) {

		List<String> articleids = SmarkerCache.getInstance(db).getArticleIds();
		List<Document> list = new ArrayList<Document>();
		for (String id : articleids) {
			Document document = SmarkerCache.getInstance(db).getArticle(id);

			String catStrs = document.getString("category");
			List<String> catList = Arrays.asList(catStrs.split(","));

			if (catList.contains(categoryName)) {

				list.add(document);
			}
		}

		datas.put("category.articles", list);
		datas.put("current.category",
				SmarkerCache.getInstance(db).getCategory(categoryName));

	}

}
