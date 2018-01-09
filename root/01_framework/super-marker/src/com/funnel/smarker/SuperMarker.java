package com.funnel.smarker;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.funnel.smarker.builder.IDataBuilder;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class SuperMarker {

	private Configuration cfg; 

	private List<IDataBuilder> builders = new ArrayList<IDataBuilder>();

	private Template t;
	
	public static final String DEFAULT_FTL_DIR = "supermarker/ftl";

	/**
	 * 
	 * @param ftlDirectory
	 *            FreeMarker的模版文件夹位置
	 * @param fileName
	 *            模版文件名
	 */
	public SuperMarker(String ftlDirectory, String fileName) {

		try {

			init(ftlDirectory);
			t = cfg.getTemplate(fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param ftlDirectory
	 *            FreeMarker的模版文件夹位置
	 * @throws Exception
	 */
	private void init(String ftlDirectory) throws Exception {
		cfg = new Configuration(Configuration.VERSION_2_3_0);
		cfg.setDirectoryForTemplateLoading(new File(ftlDirectory));
	}

	public void make() throws Exception {

		make(System.out);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void make(OutputStream outputStream) throws Exception {

		Map map = new HashMap();

		for (IDataBuilder b : builders) {
			b.build(map);
		}

		t.process(map, new OutputStreamWriter(outputStream));
	}

	public List<IDataBuilder> getBuilders() {
		return builders;
	}

	public void setBuilders(List<IDataBuilder> builders) {
		this.builders = builders;
	}

}
