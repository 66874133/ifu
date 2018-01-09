package com.funnel.smarker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.funnel.smarker.builder.ArticlesGroupByCategoryDataBuilder;
import com.funnel.smarker.builder.CategoriesDataBuilder;
import com.funnel.smarker.builder.HomeDataBuilder;
import com.funnel.smarker.builder.IDataBuilder;

public class CategoryMarker implements IMarker {

	private String categoryName;

	private static final String FTL_FILE_NAME = "category.ftl";

	public CategoryMarker(String categoryName) {
		this.categoryName = categoryName;
	}

	public void make(String ftlDirectory, String siteRoot,String db) {

		List<IDataBuilder> builders = new ArrayList<IDataBuilder>();
		builders.add(new CategoriesDataBuilder(db));
		builders.add(new HomeDataBuilder(db));
		builders.add(new ArticlesGroupByCategoryDataBuilder(db,categoryName));
		SuperMarker hf = new SuperMarker(ftlDirectory, FTL_FILE_NAME);
		hf.setBuilders(builders);

		try {
			String path = siteRoot + "/category/" + categoryName;

			File file2 = new File(path);
			if (!file2.exists()) {
				file2.mkdirs();
			}
			hf.make(new FileOutputStream(path + "/index.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws Exception {
		IMarker categoryMarker = new CategoryMarker("xiaorong");
		categoryMarker.make(SuperMarker.DEFAULT_FTL_DIR,
				"D:\\tools\\nginx-1.11.5\\site\\water","sites_localhost");
	}
}
