package com.funnel.smarker;

import java.util.ArrayList;
import java.util.List;

import com.funnel.smarker.cache.SmarkerCache;

public class SiteMarker implements IMarker {

	private List<IMarker> markers = new ArrayList<IMarker>();

	public void addMarker(IMarker marker) {
		markers.add(marker);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String siteRoot = "D:\\tools\\nginx-1.11.5\\site\\water";
		String ftlDir = SuperMarker.DEFAULT_FTL_DIR;

		String db = "sites_localhost";
		if (args.length > 0) {
			siteRoot = args[0];
		}

		if (args.length > 1) {
			ftlDir = args[1];
		}

		if (args.length > 2) {
			db = args[2];
		}

		SiteMarker marker = new SiteMarker();
		marker.addMarker(new HomeMarker());

		List<String> list = SmarkerCache.getInstance(db).getCategoryNames();

		for (String s : list) {
			marker.addMarker(new CategoryMarker(s));
		}

		List<String> list2 = SmarkerCache.getInstance(db).getArticleIds();
		for (String id : list2) {
			try {
				marker.addMarker(new ArticleMarker(id));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		marker.make(ftlDir, siteRoot, db);
	}

	@Override
	public void make(String ftlDirectory, String siteRoot, String db) {

		for (IMarker marker : markers) {
			try {
				marker.make(ftlDirectory, siteRoot, db);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

	}

}
