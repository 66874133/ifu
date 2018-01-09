package com.discover.crawler.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.duplex.crawler.CrawlerConstant;
import com.duplex.crawler.ExcuteDepend;
import com.duplex.crawler.ExcuteDependConfig;
import com.duplex.crawler.IExecuteObject;
import com.duplex.crawler.JobContext;
import com.duplex.crawler.bean.GoodsImgTextInfo;
import com.duplex.crawler.bean.GoodsInfo;
import com.duplex.crawler.client.DependClientJob;
import com.duplex.crawler.core.CrawlerObject;
import com.duplex.crawler.core.CrawlerTest;
import com.duplex.crawler.core.config.CrawlerConf;
import com.funnel.svc.SvcContext;
import com.funnel.svc.comon.AsyncService;
import com.funnel.svc.util.ApplicationUtil;
import com.funnel.svc.util.DateUtil;
import com.funnel.svc.util.JsonUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class GeneralCrawler extends AsyncService {

	@Override
	public String getSvcCode() {
		return "generalCrawler";
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

	public void crawler(Map<String, Object> reciveData) {
		logger.info("开始抓取 reciveData=" + reciveData);
		String confStr = (String) reciveData.get("crawlerConf");

		confStr = confStr.replace(" ", "+");

		CrawlerConf conf = getCrawlerConf(confStr);

		String dependFilePath = this.getClass().getClassLoader()
				.getResource("depend.xml").getPath();

		logger.info("dependFilePath = " + dependFilePath);

		List<IExecuteObject> newList = null;
		JobContext jobContext = new JobContext("test");
		ExcuteDepend depend = ExcuteDependConfig.fromFile(dependFilePath)
				.getExcuteDepend();

		jobContext.setDepend(depend);
		jobContext.putParam("crawler-conf", conf);
		DependClientJob clientJob = new DependClientJob(jobContext);

		clientJob.start(conf.getStarter());

		while (true) {
			if (clientJob.getJobStatus().isFinished()) {

				newList = clientJob.getSuccess();
				logger.info("抓取完成 newList size=" + newList.size());
				clientJob.stop();
				break;
			} else {
				logger.info("等待抓取完成10s后检查抓取状态...");
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

		saveAsGoodsInfos(newList, reciveData, conf);

		logger.info("抓取完成");
	}

	private CrawlerConf getCrawlerConf(String encoded) {
		byte[] bs = Base64.decode(encoded);
		String content = new String(bs);
		logger.info("conf content=" + content);
		return CrawlerConf.fromXMLContent(content);
	}

	/**
	 * 按base接收数据接口的要求对格式进行适配
	 * 
	 * @param newList
	 * @param reciveData
	 * @param conf
	 */
	private void saveAsGoodsInfos(List<IExecuteObject> newList,
			Map<String, Object> reciveData, CrawlerConf conf) {
		long t = System.currentTimeMillis();

		String dateStr = DateUtil.getSimpleDateStr(new Date());
		List<GoodsInfo> goodsInfos = new ArrayList<GoodsInfo>();

		for (IExecuteObject object : newList) {

			CrawlerObject crawlerObject = (CrawlerObject) object;

			if (CrawlerObject.URLTYPE_ITEM.equals(crawlerObject.getUrlType())) {

				GoodsInfo goodsInfo = new GoodsInfo();
				List<GoodsImgTextInfo> goodsImgTextInfos = new ArrayList<GoodsImgTextInfo>();
				goodsInfo.setGoodsImgTextInfos(goodsImgTextInfos);

				goodsInfo.setPriceUnitCode(1);
				goodsInfo.setSourceMallCode(conf.getMall());
				goodsInfo.setSourceCode(conf.getSource());
				goodsInfo.setSourceCategory(conf.getCategory());
				goodsInfo.setClassId(conf.getClassId());
				goodsInfo.setDateTime(dateStr);

				buildGoodsDetail(crawlerObject, goodsInfo);
				buildGoodsGuide(crawlerObject, goodsInfo);
				goodsInfos.add(goodsInfo);
			}
		}

		// for (GoodsInfo g : goodsInfos) {
		// ApplicationUtil.getGoodsRedisService()
		// .lpush("goods:crawler:req", g);
		// }

		ApplicationUtil.getGoodsRedisService().publish("goods:crawler:sub",
				goodsInfos);

		long t1 = System.currentTimeMillis();
		logger.info("抓取天猫商品,url:" + conf.getStarter().getExecuteObjects()
				+ "商品完成,总数:" + goodsInfos.size() + " 耗时：" + (t1 - t)
				+ " 回传服务端耗时:" + (System.currentTimeMillis() - t1));
	}

	/**
	 * 拼装商品详细信息
	 * 
	 * @param crawlerObject
	 * @param goodsInfo
	 */
	private void buildGoodsDetail(CrawlerObject crawlerObject,
			GoodsInfo goodsInfo) {
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
			goodsInfo.setSaleNum(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_VOLUME_MONTH)[0]);
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
			goodsInfo.setImgUrl(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_PICTURE)[0]);
		} else {
			logger.warn("找不到图片信息  goodsInfo=" + goodsInfo);
		}

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
			goodsInfo.setPcSourceDetailPageUrl(crawlerObject.getParameter()
					.getValues(CrawlerField.FIELD_SOURCE_URL)[0]);
		} else {
			logger.warn("找不到商品详情页面信息  goodsInfo=" + goodsInfo);
		}

		goodsInfo.setSourceGoodsId(crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_SOURCE_GOOD_ID)[0]);

		if (null != crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_COMMENT_COUNT)
				&& crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_COMMENT_COUNT).length > 0) {
			goodsInfo.setCommentCount(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_COMMENT_COUNT)[0]);
		}

		if (null != crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_ORIGINAL_PRICE)
				&& crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_ORIGINAL_PRICE).length > 0) {
			goodsInfo.setOriPrice(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_ORIGINAL_PRICE)[0]);
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
			goodsInfo.setIndexInPage(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_INDEX_IN_PAGE)[0]);
		} else {
			logger.warn("找不到商品在页面中的编号  goodsInfo=" + goodsInfo);
		}
	}

	/**
	 * 拼装商品引导页
	 * 
	 * @param crawlerObject
	 * @param goodsInfo
	 */
	private void buildGoodsGuide(CrawlerObject crawlerObject,
			GoodsInfo goodsInfo) {
		if (null != crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_COUPON)
				&& crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_COUPON).length > 0) {
			goodsInfo.setCoupon(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_COUPON)[0]);
		} else {
			logger.debug("找不到商品优惠信息  goodsInfo=" + goodsInfo);
		}

		if (null != crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_COUPON_LINK)
				&& crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_COUPON_LINK).length > 0) {
			goodsInfo.setCouponLink(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_COUPON_LINK)[0]);
		} else {
			logger.debug("找不到商品优惠信息链接  goodsInfo=" + goodsInfo);
		}

		if (null != crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_DETAIL_TITILE)
				&& crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_DETAIL_TITILE).length > 0) {
			GoodsImgTextInfo goodsImgTextInfo = new GoodsImgTextInfo();
			goodsImgTextInfo.setTextContent(crawlerObject.getParameter()
					.getValues(CrawlerField.FIELD_DETAIL_TITILE)[0]);
			goodsImgTextInfo.setType(GoodsImgTextInfo.TYPE_TEXT);
			goodsImgTextInfo.setOrder("1");
			goodsInfo.getGoodsImgTextInfos().add(goodsImgTextInfo);
		} else {
			logger.warn("找不到商品详情页标题  goodsInfo=" + goodsInfo);
		}

		if (null != crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_DETAIL_ABSTRACT)
				&& crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_DETAIL_ABSTRACT).length > 0) {
			GoodsImgTextInfo goodsImgTextInfo = new GoodsImgTextInfo();
			goodsImgTextInfo.setTextContent(crawlerObject.getParameter()
					.getValues(CrawlerField.FIELD_DETAIL_ABSTRACT)[0]);
			goodsImgTextInfo.setType(GoodsImgTextInfo.TYPE_TEXT);
			goodsImgTextInfo.setOrder("2");

			if (null != goodsInfo.getCoupon()
					&& !"".equals(goodsInfo.getCoupon())
					&& null != goodsInfo.getCouponLink()
					&& !"".equals(goodsInfo.getCouponLink())) {
				String[] coupons = goodsInfo.getCoupon().split(",");
				String[] couponLinks = goodsInfo.getCouponLink().split(",");
				for (int i = 0; i < coupons.length; i++) {
					if (goodsImgTextInfo.getTextContent().contains(coupons[i])
							&& i < couponLinks.length) {
						String linkStr = "<a style=\"color:f25536\" href=\""
								+ couponLinks[i] + "\">" + coupons[i] + "</a>";
						goodsImgTextInfo.setTextContent(goodsImgTextInfo
								.getTextContent().replace(coupons[i], linkStr));
					}
				}
			}
			goodsInfo.getGoodsImgTextInfos().add(goodsImgTextInfo);
		} else {
			logger.warn("找不到商品详情页摘要  goodsInfo=" + goodsInfo);
		}

		if (null != crawlerObject.getParameter().getValues(
				CrawlerField.FIELD_DETAIL_PIC)
				&& crawlerObject.getParameter().getValues(
						CrawlerField.FIELD_DETAIL_PIC).length > 0) {
			GoodsImgTextInfo goodsImgTextInfo = new GoodsImgTextInfo();
			goodsImgTextInfo.setImgUrl(crawlerObject.getParameter().getValues(
					CrawlerField.FIELD_DETAIL_PIC)[0]);
			goodsImgTextInfo.setType(GoodsImgTextInfo.TYPE_IMG);
			goodsImgTextInfo.setOrder("3");
			goodsInfo.getGoodsImgTextInfos().add(goodsImgTextInfo);
		} else {
			logger.warn("找不到商品详情页图片  goodsInfo=" + goodsInfo);
		}
	}

	public static void main(String[] args) {
		Map<String, Object> reciveData = new HashMap<String, Object>();
		new GeneralCrawler().crawler(reciveData);

		System.out.println(JSONObject.fromObject(CrawlerTest.getCrawlerConf())
				.toString());
	}
}
