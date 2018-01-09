package com.funnel.smarker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.funnel.smarker.builder.ArticlesDataBuilder;
import com.funnel.smarker.builder.HomeDataBuilder;
import com.funnel.smarker.builder.IDataBuilder;
import com.funnel.smarker.builder.CategoriesDataBuilder;

public class HomeMarker implements IMarker {

	private static final String FTL_FILE_NAME = "home.ftl";

	public static void main(String[] args) throws Exception {
		IMarker homeMarker = new HomeMarker();
		homeMarker.make(SuperMarker.DEFAULT_FTL_DIR,
				"D:\\tools\\nginx-1.11.5\\site\\water","sites_localhost");
	}

	@Override
	public void make(String ftlDirectory, String siteRoot,String db) {

		List<IDataBuilder> builders = new ArrayList<IDataBuilder>();
		builders.add(new CategoriesDataBuilder(db));
		builders.add(new HomeDataBuilder(db));
		builders.add(new ArticlesDataBuilder(db));
		SuperMarker hf = new SuperMarker(ftlDirectory, FTL_FILE_NAME);
		hf.setBuilders(builders);

		try {
			hf.make(new FileOutputStream(siteRoot + "/index.html"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
