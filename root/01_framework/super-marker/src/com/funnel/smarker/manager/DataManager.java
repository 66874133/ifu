package com.funnel.smarker.manager;

import java.util.Iterator;

import org.bson.Document;

import com.cpkf.yyjd.tools.data.mongo.MongoHandler;

public class DataManager {

	
	public void addArticle()
	{
		MongoHandler handler = new MongoHandler();

		Iterator<Document> ita = handler
				.getCollection("sites_localhost", "article").find().iterator();
	}
}
