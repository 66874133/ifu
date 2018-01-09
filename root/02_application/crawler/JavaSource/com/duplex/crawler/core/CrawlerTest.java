package com.duplex.crawler.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.duplex.crawler.CrawlerConstant;
import com.duplex.crawler.core.adjuster.UnitValueAdjuster;
import com.duplex.crawler.core.adjuster.ValueUrlProtocolAdjuster;
import com.duplex.crawler.core.builder.IUrlBuilder;
import com.duplex.crawler.core.builder.UrlMultipleNumberBuilder;
import com.duplex.crawler.core.builder.UrlPathBuilder;
import com.duplex.crawler.core.builder.UrlTempleBuilder;
import com.duplex.crawler.core.config.CrawlerConf;
import com.duplex.crawler.core.config.CrawlerExtractor;
import com.duplex.crawler.core.config.CrawlerWriter;
import com.duplex.crawler.core.config.CrawlerWriters;
import com.duplex.crawler.core.config.ExtractField;
import com.duplex.crawler.core.starter.ConfigSeedsStarter;
import com.duplex.crawler.core.writer.PatternWriterField;
import com.duplex.crawler.core.writer.SimpleWriterField;
import com.duplex.crawler.core.writer.UrlPatternWriterField;

public class CrawlerTest {

	protected final static Log logger = LogFactory.getLog(CrawlerTest.class);

	public static CrawlerConf getCrawlerConf() {
		CrawlerConf conf = new CrawlerConf();
		conf.setName("test");
		conf.setEncode("GBK");
		conf.setCategory("adult");
		ConfigSeedsStarter starter = new ConfigSeedsStarter();
		starter.addSeed("https://list.tmall.com/search_product.htm?cat=53340007&sort=new&s=0");
		conf.setSource(CrawlerConstant.SOURCE_CODE_TMALL);
		conf.setStarter(starter);

		CrawlerExtractor extractor = new CrawlerExtractor();

		ExtractField field0 = new ExtractField();
		field0.setName("url");
		UrlTempleBuilder urlTempleBuilder = new UrlTempleBuilder();
		urlTempleBuilder
				.setTemplate("https://list.tmall.com/search_product.htm?cat=53340007&sort=new&s=$1");
		List<IUrlBuilder> builders = new ArrayList<IUrlBuilder>();
		UrlMultipleNumberBuilder multipleNumberBuilder = new UrlMultipleNumberBuilder();
		multipleNumberBuilder.setTarget("60*1-3");
		builders.add(multipleNumberBuilder);
		urlTempleBuilder.setBuilders(builders);

		field0.setUrlBuilder(urlTempleBuilder);
		field0.setFrom(CrawlerObject.URLTYPE_SEED);
		field0.setTo(CrawlerObject.URLTYPE_LIST);

		ExtractField field = new ExtractField();
		field.setName("price");
		UrlPathBuilder urlPathBuilder = new UrlPathBuilder();
		urlPathBuilder
				.setPath("div[@id='J_ItemList']/div/div/p[@class='productPrice']");
		urlPathBuilder.setPattern("&yen;(\\d+\\.?\\d*)");
		field.setUrlBuilder(urlPathBuilder);
		field.setFrom(CrawlerObject.URLTYPE_LIST);
		field.setTo(CrawlerObject.URLTYPE_ITEM);

		ExtractField field2 = new ExtractField();
		field2.setName("url");
		UrlPathBuilder urlPathBuilder2 = new UrlPathBuilder();
		urlPathBuilder2
				.setPath("div[@id='J_ItemList']/div/div/p[@class='productTitle']/a/@href");
		urlPathBuilder2.setPattern("(.*?)&");
		field2.setUrlBuilder(urlPathBuilder2);
		field2.setFrom(CrawlerObject.URLTYPE_LIST);
		field2.setTo(CrawlerObject.URLTYPE_ITEM);
		field2.setAdjuster(new ValueUrlProtocolAdjuster());

		ExtractField field3 = new ExtractField();
		field3.setName("volume_month");
		UrlPathBuilder urlPathBuilder3 = new UrlPathBuilder();
		urlPathBuilder3
				.setPath("div[@id='J_ItemList']/div/div/p[@class='productStatus']/span/em");
		urlPathBuilder3.setPattern("(.*)笔");
		field3.setUrlBuilder(urlPathBuilder3);
		field3.setFrom(CrawlerObject.URLTYPE_LIST);
		field3.setTo(CrawlerObject.URLTYPE_ITEM);
		field3.setAdjuster(new UnitValueAdjuster());

		ExtractField field4 = new ExtractField();
		field4.setName("comment_count");
		UrlPathBuilder urlPathBuilder4 = new UrlPathBuilder();
		urlPathBuilder4
				.setPath("div[@id='J_ItemList']/div/div/p[@class='productStatus']/span/a");
		field4.setUrlBuilder(urlPathBuilder4);
		field4.setFrom(CrawlerObject.URLTYPE_LIST);
		field4.setTo(CrawlerObject.URLTYPE_ITEM);
		field4.setAdjuster(new UnitValueAdjuster());

		ExtractField field5 = new ExtractField();
		field5.setName("name");
		UrlPathBuilder urlPathBuilder5 = new UrlPathBuilder();
		urlPathBuilder5
				.setPath("div[@id='J_ItemList']/div/div/p[@class='productTitle']");
		field5.setUrlBuilder(urlPathBuilder5);
		field5.setFrom(CrawlerObject.URLTYPE_LIST);
		field5.setTo(CrawlerObject.URLTYPE_ITEM);

		extractor.addExtractField(field0);
		extractor.addExtractField(field);
		extractor.addExtractField(field2);
		extractor.addExtractField(field3);
		extractor.addExtractField(field4);
		extractor.addExtractField(field5);

		conf.setExtractor(extractor);

		CrawlerWriters writers = new CrawlerWriters();
		CrawlerWriter writer = new CrawlerWriter();

		PatternWriterField writerField = new PatternWriterField();
		writerField.setName("oriPrice");
		writerField.setPattern("reservePrice\":\"(.*?)\",");

		SimpleWriterField writerField2 = new SimpleWriterField();
		writerField2.setName("pic");
		writerField2.setPath("img[@id='J_ImgBooth']/@src");
		writerField2.setAdjuster(new ValueUrlProtocolAdjuster());

		UrlPatternWriterField writerField3 = new UrlPatternWriterField();
		writerField3.setName("sourceGoodId");
		writerField3.setPattern("[&\\?]id=(\\d*)&?");

		PatternWriterField writerField4 = new PatternWriterField();
		writerField4.setName("catId");
		writerField4.setPattern("rootCatId\":\"(\\d*?)\",");

		writer.addWriterField(writerField);
		writer.addWriterField(writerField2);
		writer.addWriterField(writerField3);
		writer.addWriterField(writerField4);
		writer.setUrlType(CrawlerObject.URLTYPE_ITEM);
		writers.addCrawlerWriter(writer);
		conf.setWriters(writers);

		return conf;
	}

	public static void main(String[] args) throws Exception {

		CrawlerConf conf = getCrawlerConf();

	}

}
