package com.funnel.smarker;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.funnel.smarker.builder.ArticlesDataBuilder;
import com.funnel.smarker.builder.CategoriesDataBuilder;
import com.funnel.smarker.builder.HomeDataBuilder;
import com.funnel.smarker.builder.IDataBuilder;
import com.funnel.smarker.builder.NextOneArticleDataBuilder;
import com.funnel.smarker.builder.OneArticleDataBuilder;
import com.funnel.smarker.builder.PreviousOneArticleDataBuilder;

public class ArticleMarker implements IMarker {

	private String articleId;

	private static final String FTL_FILE_NAME = "article.ftl";

	public ArticleMarker(String articleId) {
		this.articleId = articleId;
	}

	public void make(String ftlDirectory, String siteRoot,String db) throws Exception {

		List<IDataBuilder> builders = new ArrayList<IDataBuilder>();
		builders.add(new OneArticleDataBuilder(db,articleId));
		builders.add(new CategoriesDataBuilder(db));
		builders.add(new HomeDataBuilder(db));
		builders.add(new ArticlesDataBuilder(db));
		builders.add(new PreviousOneArticleDataBuilder(db,articleId));
		builders.add(new NextOneArticleDataBuilder(db,articleId));
		SuperMarker hf = new SuperMarker(ftlDirectory, FTL_FILE_NAME);
		hf.setBuilders(builders);

		String path = siteRoot + "/article/";

		File file2 = new File(path);
		if (!file2.exists()) {
			file2.mkdirs();
		}

		hf.make(new FileOutputStream(siteRoot + "/article/" + articleId
				+ ".html"));
	}

	public static void main(String[] args) throws Exception {
		IMarker articleMarker = new ArticleMarker("58245d140cb001f41beab5e4");
		articleMarker.make(SuperMarker.DEFAULT_FTL_DIR,
				"D:\\tools\\nginx-1.11.5\\site\\water","sites_localhost");

	}
}
