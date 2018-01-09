package com.duplex.crawler.core.config;

import java.io.Serializable;

import com.duplex.crawler.core.adjuster.TemplateCombineAdjuster;
import com.duplex.crawler.core.adjuster.UnitValueAdjuster;
import com.duplex.crawler.core.adjuster.UrlFieldAdjuster;
import com.duplex.crawler.core.adjuster.ValueUrlAdjuster;
import com.duplex.crawler.core.adjuster.ValueUrlProtocolAdjuster;
import com.duplex.crawler.core.adjuster.ValueUrlRelativePathAdjuster;
import com.duplex.crawler.core.builder.UrlContentPatternBuilder;
import com.duplex.crawler.core.builder.UrlFieldBuilder;
import com.duplex.crawler.core.builder.UrlJsonBuilder;
import com.duplex.crawler.core.builder.UrlLinkPatternBuilder;
import com.duplex.crawler.core.builder.UrlMultipleNumberBuilder;
import com.duplex.crawler.core.builder.UrlPageBuilder;
import com.duplex.crawler.core.builder.UrlPathBuilder;
import com.duplex.crawler.core.builder.UrlTempleBuilder;
import com.duplex.crawler.core.checker.CrawlerObjectFieldsChecker;
import com.duplex.crawler.core.starter.ConfigSeedsStarter;
import com.duplex.crawler.core.starter.IJobStarter;
import com.duplex.crawler.core.writer.AbstractWriterField;
import com.duplex.crawler.core.writer.ExsitWriterField;
import com.duplex.crawler.core.writer.JsonWriterField;
import com.duplex.crawler.core.writer.PatternWriterField;
import com.duplex.crawler.core.writer.SimpleWriterField;
import com.duplex.crawler.core.writer.StaticWriterField;
import com.duplex.crawler.core.writer.UrlPatternWriterField;
import com.duplex.crawler.core.writer.UrlWriterField;
import com.funnel.svc.util.FileUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CrawlerConf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9069647003123897079L;

	/**
	 * 商品被抓取时所在的商城标识
	 */
	private String source;

	/**
	 * 商品销售的商城标识
	 */
	private String mall;
	private String name;
	private String encode;

	/**
	 * 商品的标签，名称暂时不修改，此字段是以前用于商品唯一类目ID,现修改为商品的标签字段
	 */
	private String category;

	/**
	 * 商品的唯一类目ID
	 */
	private String classId;

	private IJobStarter starter;

	private CrawlerExtractor extractor;
	private CrawlerWriters writers;

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getMall() {
		return mall;
	}

	public void setMall(String mall) {
		this.mall = mall;
	}

	public IJobStarter getStarter() {
		return starter;
	}

	public void setStarter(IJobStarter starter) {
		this.starter = starter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public CrawlerWriters getWriters() {
		return writers;
	}

	public void setWriters(CrawlerWriters writers) {
		this.writers = writers;
	}

	public CrawlerExtractor getExtractor() {
		return extractor;
	}

	public void setExtractor(CrawlerExtractor extractor) {
		this.extractor = extractor;
	}

	public static CrawlerConf fromName(String name) {
		String content = FileUtil.getFileContent(name, "utf8");

		return fromXMLContent(content);
	}

	public static CrawlerConf fromXMLContent(String xml) {
		return (CrawlerConf) getXStream().fromXML(xml);
	}

	public static XStream getXStream() {

		XStream xs = new XStream(new DomDriver());
		xs.alias("crawler", CrawlerConf.class);
		xs.aliasAttribute(CrawlerConf.class, "source", "source");
		xs.aliasAttribute(CrawlerConf.class, "mall", "mall");
		xs.aliasAttribute(CrawlerConf.class, "name", "name");
		xs.aliasAttribute(CrawlerConf.class, "encode", "encode");
		xs.aliasAttribute(CrawlerConf.class, "category", "category");
		xs.aliasAttribute(CrawlerConf.class, "classId", "classId");
		xs.aliasAttribute(CrawlerConf.class, "starter", "starter");

		// starter
		xs.alias("seeds-starter", ConfigSeedsStarter.class);
		xs.aliasAttribute(ConfigSeedsStarter.class, "prefix", "prefix");
		// filter

		// extractor
		xs.alias("extractor", CrawlerExtractor.class);
		xs.addImplicitCollection(CrawlerExtractor.class, "extractFields");
		xs.alias("extractor-field", ExtractField.class);
		xs.aliasAttribute(ExtractField.class, "from", "from");
		xs.aliasAttribute(ExtractField.class, "to", "to");
		xs.aliasAttribute(ExtractField.class, "name", "name");
		xs.aliasField("builder", ExtractField.class, "urlBuilder");

		// url adjuster
		xs.alias("field-adjuster", UrlFieldAdjuster.class);
		xs.aliasAttribute(UrlFieldAdjuster.class, "slashs", "slashs");
		xs.aliasAttribute(UrlFieldAdjuster.class, "parameters", "parameters");

		xs.alias("template-combine-adjuster", TemplateCombineAdjuster.class);
		xs.aliasAttribute(TemplateCombineAdjuster.class, "template", "template");

		// url builder

		xs.alias("template-builder", UrlTempleBuilder.class);
		xs.aliasAttribute(UrlTempleBuilder.class, "template", "template");
		xs.addImplicitCollection(UrlTempleBuilder.class, "builders");

		xs.alias("multiple-number-builder", UrlMultipleNumberBuilder.class);
		xs.aliasAttribute(UrlMultipleNumberBuilder.class, "target", "target");

		xs.alias("page-builder", UrlPageBuilder.class);
		xs.aliasAttribute(UrlPageBuilder.class, "total", "total");
		xs.aliasAttribute(UrlPageBuilder.class, "size", "size");

		xs.alias("json-builder", UrlJsonBuilder.class);
		xs.aliasAttribute(UrlJsonBuilder.class, "parameter", "parameter");
		xs.aliasAttribute(UrlJsonBuilder.class, "pattern", "pattern");
		xs.aliasAttribute(UrlJsonBuilder.class, "path", "path");
		xs.aliasAttribute(UrlJsonBuilder.class, "matche", "matche");

		xs.alias("url-link-pattern-builder", UrlLinkPatternBuilder.class);
		xs.aliasAttribute(UrlLinkPatternBuilder.class, "pattern", "pattern");

		xs.alias("url-content-pattern-builder", UrlContentPatternBuilder.class);
		xs.aliasAttribute(UrlContentPatternBuilder.class, "pattern", "pattern");

		xs.alias("field-builder", UrlFieldBuilder.class);
		xs.aliasAttribute(UrlFieldBuilder.class, "field", "field");

		xs.alias("path-builder", UrlPathBuilder.class);
		xs.aliasAttribute(UrlPathBuilder.class, "path", "path");
		xs.aliasAttribute(UrlPathBuilder.class, "pattern", "pattern");
		xs.aliasAttribute(UrlPathBuilder.class, "order", "order");

		// writer
		xs.addImplicitCollection(CrawlerWriters.class, "writers");
		xs.alias("writer", CrawlerWriter.class);
		xs.aliasAttribute(CrawlerWriter.class, "urlType", "url-type");
		xs.aliasAttribute(CrawlerWriter.class, "format", "format");
		xs.aliasAttribute(CrawlerWriter.class, "written", "written");
		xs.aliasField("job-checker", CrawlerWriter.class, "jobChecker");
		xs.aliasField("task-checker", CrawlerWriter.class, "taskChecker");
		xs.aliasField("record-checker", CrawlerWriter.class, "recordChecker");

		// output

		// checker

		xs.alias("fields-checker", CrawlerObjectFieldsChecker.class);
		xs.aliasAttribute(CrawlerObjectFieldsChecker.class, "errorFields",
				"error");
		xs.aliasAttribute(CrawlerObjectFieldsChecker.class, "warnFields",
				"warning");

		// writer-field
		xs.aliasAttribute(AbstractWriterField.class, "multiple", "multiple");
		xs.aliasAttribute(AbstractWriterField.class, "job", "job");
		xs.aliasAttribute(AbstractWriterField.class, "name", "name");
		xs.aliasAttribute(AbstractWriterField.class, "append", "append");
		xs.aliasAttribute(AbstractWriterField.class, "handleUnicode",
				"handle-unicode");
		xs.aliasAttribute(AbstractWriterField.class, "handleDiscard",
				"handle-discard");

		xs.alias("url-adjuster", ValueUrlAdjuster.class);
		xs.aliasAttribute(ValueUrlAdjuster.class, "slashs", "slashs");
		xs.aliasAttribute(ValueUrlAdjuster.class, "parameters", "parameters");

		xs.alias("url-protocol-adjuster", ValueUrlProtocolAdjuster.class);
		xs.alias("url-relative-path-adjuster",
				ValueUrlRelativePathAdjuster.class);

		xs.alias("unit-adjuster", UnitValueAdjuster.class);

		xs.alias("simple-field", SimpleWriterField.class);
		xs.aliasAttribute(SimpleWriterField.class, "path", "path");
		xs.aliasAttribute(SimpleWriterField.class, "pattern", "pattern");

		xs.alias("url-field", UrlWriterField.class);
		xs.aliasAttribute(UrlWriterField.class, "pattern", "pattern");

		xs.alias("json-field", JsonWriterField.class);
		xs.aliasAttribute(JsonWriterField.class, "path", "path");
		xs.aliasAttribute(JsonWriterField.class, "parameter", "parameter");

		xs.alias("pattern-field", PatternWriterField.class);
		xs.aliasAttribute(PatternWriterField.class, "pattern", "pattern");

		xs.alias("url-pattern-field", UrlPatternWriterField.class);
		xs.aliasAttribute(UrlPatternWriterField.class, "pattern", "pattern");

		xs.alias("static-field", StaticWriterField.class);
		xs.aliasAttribute(StaticWriterField.class, "value", "value");

		xs.alias("existed-field", ExsitWriterField.class);
		xs.aliasAttribute(ExsitWriterField.class, "path", "path");
		xs.aliasAttribute(ExsitWriterField.class, "pattern", "pattern");
		xs.aliasAttribute(ExsitWriterField.class, "existValue", "existed-value");
		xs.aliasAttribute(ExsitWriterField.class, "notExistValue",
				"not-existed-value");

		return xs;
	}

	public static void main(String[] args) {
		CrawlerConf crawler1 = CrawlerConf
				.fromName("D:\\SVN\\Code\\server\\trunk\\goodsBaseCrawler\\crawler-conf\\guangdiu_hot.xml");

		ConfigSeedsStarter configSeedsStarter = new ConfigSeedsStarter();
		configSeedsStarter.setPrefix("11");
		crawler1.setStarter(configSeedsStarter);
		crawler1.setMall("1");
		System.out.println(CrawlerConf.getXStream().toXML(crawler1));
		JsonWriterField jsonWriterField = new JsonWriterField();
		jsonWriterField.setParameter("promotionPrice");
		crawler1.getWriters().getWriters().get(0)
				.addWriterField(jsonWriterField);
		// CrawlerConf crawler = CrawlerTest.getCrawlerConf();
		//
		// OutputStreamWriter fileWriter;
		// try {
		// fileWriter = new OutputStreamWriter(new FileOutputStream(
		// "D:/test.xml"), "utf-8");
		// //crawler.setClassId("2");
		// CrawlerConf.getXStream().toXML(crawler, fileWriter);
		//
		// fileWriter.flush();
		// fileWriter.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		System.out.println(CrawlerConf.getXStream().toXML(crawler1));
	}
}
