package com.discover.crawler.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.duplex.crawler.CrawlerConstant;
import com.duplex.crawler.ExcuteDepend;
import com.duplex.crawler.ExcuteDependConfig;
import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobContext;
import com.duplex.crawler.bean.GoodsImgTextInfo;
import com.duplex.crawler.bean.GoodsInfo;
import com.duplex.crawler.client.DependClientJob;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.config.CrawlerConf;
import com.duplex.crawler.core.starter.ConfigSeedsStarter;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.SyncService;
import com.funnel.svc.util.ApplicationUtil;
import com.funnel.svc.util.JsonUtil;

public class GeneralDetailCrawler extends SyncService {

	@Override
	public String getSvcCode() {
		return "generalDetailCrawler";
	}

	@Override
	public String getSvcDesc() {
		return "商品爬虫";
	}

	@Override
	public void process(SvcContext context) {
		Map<String, Object> reciveData = context.getRequestData();
		Object crawlerParam = reciveData.get(CrawlerConstant.CRAWLER_PARAMS);
		Map<String, Object> crawlerParams = JsonUtil.toJSONObject(crawlerParam);
		crawler(crawlerParams);
	}

	public Map<String, Object> crawler(Map<String, Object> reciveData) {
		logger.info("开始抓取 reciveData=" + reciveData);
		// String confStr = (String) reciveData.get("crawlerConf");
		String seed = (String) reciveData.get("seed");
		String sourceCategory = (String) reciveData.get("sourceCategory");
		String confXml = (String) reciveData.get("confXml");
		// confStr = confStr.replace(" ", "+");

		CrawlerConf conf = getCrawlerConf(confXml, seed, sourceCategory);

		String dependFilePath = this.getClass().getClassLoader()
				.getResource("depend.xml").getPath();

		logger.info("dependFilePath = " + dependFilePath);
		logger.info("conf content=" + CrawlerConf.getXStream().toXML(conf));

		List<IExecuteObject> newList = null;
		JobContext jobContext = new JobContext("test");
		ExcuteDepend depend = ExcuteDependConfig.fromFile(dependFilePath)
				.getExcuteDepend();

		jobContext.setDepend(depend);
		jobContext.putParam("crawler-conf", conf);
		DependClientJob clientJob = new DependClientJob(jobContext, 1);

		clientJob.start(conf.getStarter());

		while (true) {
			if (clientJob.getJobStatus().isFinished()) {

				newList = clientJob.getSuccess();
				logger.info("抓取完成 newList size=" + newList.size());
				clientJob.stop();
				break;
			} else {
				logger.info("等待抓取完成3s后检查抓取状态...");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		logger.info("抓取完成");
		return saveAsGoodsInfos(newList, reciveData, conf);

	}

	private CrawlerConf getCrawlerConf(String confXml, String itemUrl,
			String sourceCategory) {
		// String xmlFile =
		// "D:\\SVN\\Code\\server\\trunk\\goodsBaseCrawler\\crawler-conf\\"
		// + confXml;
		String xmlFile = this.getClass().getClassLoader().getResource(confXml)
				.getPath();

		logger.info("使用该配置文件进行抓取,路径xmlFile = " + xmlFile);

		CrawlerConf conf = CrawlerConf.fromName(xmlFile);

		if (null != sourceCategory && !"".equals(sourceCategory.trim())) {
			logger.info("指定商品的标签sourceCategory=" + sourceCategory);
			conf.setCategory(sourceCategory);
		} else {
			logger.info("未指定商品的标签");
			conf.setCategory("");
		}

		ConfigSeedsStarter configSeedsStarter = (ConfigSeedsStarter) conf
				.getStarter();
		configSeedsStarter.addSeed(itemUrl);
		return conf;
	}

	/**
	 * 按base接收数据接口的要求对格式进行适配
	 * 
	 * @param newList
	 * @param reciveData
	 * @param conf
	 */
	private Map<String, Object> saveAsGoodsInfos(List<IExecuteObject> newList,
			Map<String, Object> reciveData, CrawlerConf conf) {
		long t = System.currentTimeMillis();

		String imageLink = "";
		if (reciveData.containsKey("imageLink")) {
			imageLink = (String) reciveData.get("imageLink");
		}

		List<GoodsInfo> goodsInfos = new ArrayList<GoodsInfo>();

		for (IExecuteObject object : newList) {

			CrawlerObject crawlerObject = (CrawlerObject) object;

			if (CrawlerObject.URLTYPE_ITEM.equals(crawlerObject.getUrlType())) {

				GoodsInfo goodsInfo = new GoodsInfo();
				List<GoodsImgTextInfo> goodsImgTextInfos = new ArrayList<GoodsImgTextInfo>();
				goodsInfo.setGoodsImgTextInfos(goodsImgTextInfos);

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_PRICE)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_PRICE).length > 0) {
					goodsInfo.setPrice(crawlerObject.getParameter().getValues(
							CrawlerField.FIELD_PRICE)[0]);
				} else {
					logger.warn("没有商品价格,丢弃改商品->" + crawlerObject.getParameter());
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_VOLUME_MONTH)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_VOLUME_MONTH).length > 0) {
					goodsInfo.setSaleNum(crawlerObject.getParameter()
							.getValues(CrawlerField.FIELD_VOLUME_MONTH)[0]);
				}

				goodsInfo.setName(crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_NAME)[0]);
				goodsInfo.setPcUrl(crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_URL)[0]);
				goodsInfo.setMobileUrl(crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_URL)[0]);

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_PICTURE)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_PICTURE).length > 0) {

					// 如果LMS指定了图片，则使用指定的图片
					if (null != imageLink && !"".equals(imageLink)) {

						goodsInfo.setImgUrl(imageLink);
						logger.warn("强制替换图片信息  goodsInfo=" + goodsInfo);
					} else {
						goodsInfo.setImgUrl(crawlerObject.getParameter()
								.getValues(CrawlerField.FIELD_PICTURE)[0]);
					}

				} else {
					logger.warn("找不到图片信息  goodsInfo=" + goodsInfo);
				}

				goodsInfo.setType(GoodsInfo.TYPE_MANUAL);
				goodsInfo.setPriceUnitCode(1);
				goodsInfo.setSourceMallCode(conf.getMall());
				goodsInfo.setSourceCode(conf.getSource());
				goodsInfo.setSourceCategory(conf.getCategory());
				goodsInfo.setClassId(conf.getClassId());
				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_CATEGORY_ID)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_CATEGORY_ID).length > 0) {
					goodsInfo.setCid(crawlerObject.getParameter().getValues(
							CrawlerField.FIELD_CATEGORY_ID)[0]);
				} else {
					logger.warn("找不到分类信息  goodsInfo=" + goodsInfo);
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_SOURCE_URL)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_SOURCE_URL).length > 0) {
					goodsInfo.setPcSourceDetailPageUrl(crawlerObject
							.getParameter().getValues(
									CrawlerField.FIELD_SOURCE_URL)[0]);
				} else {
					logger.warn("找不到商品详情页面信息  goodsInfo=" + goodsInfo);
				}

				goodsInfo.setSourceGoodsId(crawlerObject.getParameter()
						.getValues(CrawlerField.FIELD_SOURCE_GOOD_ID)[0]);

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_COMMENT_COUNT)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_COMMENT_COUNT).length > 0) {
					goodsInfo.setCommentCount(crawlerObject.getParameter()
							.getValues(CrawlerField.FIELD_COMMENT_COUNT)[0]);
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_ORIGINAL_PRICE)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_ORIGINAL_PRICE).length > 0) {
					goodsInfo.setOriPrice(crawlerObject.getParameter()
							.getValues(CrawlerField.FIELD_ORIGINAL_PRICE)[0]);
				} else {
					logger.warn("找不到商品原价信息  goodsInfo=" + goodsInfo);
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_PAGE)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_PAGE).length > 0) {
					goodsInfo.setPage(crawlerObject.getParameter().getValues(
							CrawlerField.FIELD_PAGE)[0]);
				} else {
					logger.warn("找不到商品的页面编号  goodsInfo=" + goodsInfo);
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_INDEX_IN_PAGE)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_INDEX_IN_PAGE).length > 0) {
					goodsInfo.setIndexInPage(crawlerObject.getParameter()
							.getValues(CrawlerField.FIELD_INDEX_IN_PAGE)[0]);
				} else {
					logger.warn("找不到商品在页面中的编号  goodsInfo=" + goodsInfo);
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_DETAIL_TITILE)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_DETAIL_TITILE).length > 0) {
					GoodsImgTextInfo goodsImgTextInfo = new GoodsImgTextInfo();
					goodsImgTextInfo.setTextContent(crawlerObject
							.getParameter().getValues(
									CrawlerField.FIELD_DETAIL_TITILE)[0]);
					goodsImgTextInfo.setType(GoodsImgTextInfo.TYPE_TEXT);
					goodsImgTextInfo.setOrder("1");
					goodsImgTextInfos.add(goodsImgTextInfo);
				} else {
					logger.warn("找不到商品详情页标题  goodsInfo=" + goodsInfo);
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_DETAIL_ABSTRACT)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_DETAIL_ABSTRACT).length > 0) {
					GoodsImgTextInfo goodsImgTextInfo = new GoodsImgTextInfo();
					goodsImgTextInfo.setTextContent(crawlerObject
							.getParameter().getValues(
									CrawlerField.FIELD_DETAIL_ABSTRACT)[0]);
					goodsImgTextInfo.setType(GoodsImgTextInfo.TYPE_TEXT);
					goodsImgTextInfo.setOrder("2");
					goodsImgTextInfos.add(goodsImgTextInfo);
				} else {
					logger.warn("找不到商品详情页摘要  goodsInfo=" + goodsInfo);
				}

				if (null != crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_DETAIL_PIC)
						&& crawlerObject.getParameter().getValues(
								CrawlerField.FIELD_DETAIL_PIC).length > 0) {
					GoodsImgTextInfo goodsImgTextInfo = new GoodsImgTextInfo();
					goodsImgTextInfo.setImgUrl(crawlerObject.getParameter()
							.getValues(CrawlerField.FIELD_DETAIL_PIC)[0]);
					goodsImgTextInfo.setType(GoodsImgTextInfo.TYPE_IMG);
					goodsImgTextInfo.setOrder("3");
					goodsImgTextInfos.add(goodsImgTextInfo);
				} else {
					logger.warn("找不到商品详情页图片  goodsInfo=" + goodsInfo);
				}

				goodsInfos.add(goodsInfo);
			}
		}

		// 执行下一步，目前的处理是存储已经抓取到的商品数据
		String callbackSvcCode = reciveData.get(
				CrawlerConstant.PARAMS_CALLBACK_SVC_CODE).toString();

		Map<String, Object> respGoodsInfoMap = new HashMap<String, Object>();
		if (goodsInfos.size() > 0) {

			respGoodsInfoMap.put("dataSource", 1);
			respGoodsInfoMap.put("dataType", 1);
			respGoodsInfoMap.put("datas", goodsInfos.get(0));

			ApplicationUtil.getGoodsRedisService().publish("goods:crawler:sub",
					goodsInfos);

		}

		long t1 = System.currentTimeMillis();
		logger.info("抓取天猫商品,url:" + conf.getStarter().getExecuteObjects()
				+ "商品完成,总数:" + goodsInfos.size() + " 耗时：" + (t1 - t)
				+ " 回传服务端耗时:" + (System.currentTimeMillis() - t1));
		return respGoodsInfoMap;
	}

}
