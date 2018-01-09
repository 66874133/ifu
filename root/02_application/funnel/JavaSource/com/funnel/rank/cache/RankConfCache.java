package com.funnel.rank.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.funnel.datasource.connection.MongoConnection;
import com.funnel.datasource.connection.MongoConnectionManager;
import com.funnel.rank.RankConfInfo;
import com.funnel.svc.cache.CacheBase;

/**
 * 榜单配置缓存
 * 
 * @author wanghua4
 */
public class RankConfCache extends CacheBase {
	private static final Log logger = LogFactory.getLog(RankConfCache.class);
	private static Map<String, RankConfInfo> caches = new HashMap<String, RankConfInfo>();

	public String getCacheCode() {
		return "goodsRankConfCache";
	}

	public String getCacheDataForView() {
		return caches.toString();
	}

	public void load() {
		loadCacheFromDb();
	}

	private void loadCacheFromDb() {
		logger.info("开始从数据库装载GoodsRankConfCache");
		List<RankConfInfo> goodsRankConfList = getDbCacheData();
		logger.info("goodsRankConfList=" + goodsRankConfList);
		Map<String, RankConfInfo> goodsRankConfMap = new HashMap<String, RankConfInfo>();
		for (RankConfInfo goodsRankConfInfo : goodsRankConfList) {
			goodsRankConfMap.put(goodsRankConfInfo.getRankCode(),
					goodsRankConfInfo);
		}
		caches = goodsRankConfMap;
		logger.info("从数据库装载GoodsRankConfCache完成");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RankConfInfo> getDbCacheData() {
		MongoConnection mongoConnection = MongoConnectionManager
				.getConnection();
		try {
			return mongoConnection.editor().select(
					this.getClass().getSimpleName(), null, this.getClass());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static RankConfInfo getRankConf(String rankCode) {
		if (caches.containsKey(rankCode)) {
			return caches.get(rankCode);
		}
		return null;
	}

	public static List<RankConfInfo> getRankConfByRankGroup(String rankGroup) {
		List<RankConfInfo> allRankConf = new ArrayList<RankConfInfo>();
		for (Map.Entry<String, RankConfInfo> entry : caches.entrySet()) {
			if (rankGroup.equals(entry.getValue().getRankGroup())) {
				allRankConf.add(entry.getValue());
			}
		}
		return allRankConf;
	}
}
