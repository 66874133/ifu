package com.funnel.svc.cache;

public interface Cache {
	// 鏁版嵁鍔犺浇鏂瑰紡锛屼粠鏁版嵁搴撳姞杞芥暟鎹?
	public static final String LOAD_SOURCE_DB = "db";
	// 鏁版嵁鍔犺浇鏂瑰紡锛屼粠杩滅▼鏈嶅姟鍔犺浇鏁版嵁
	public static final String LOAD_SOURCE_REMOTE = "remoteSvc";

	/**
	 * 缂撳瓨璇嗗埆code
	 * 
	 * @return
	 */
	public String getCacheCode();

	/**
	 * 鍔犺浇缂撳瓨鏁版嵁
	 */
	public void load();

	/**
	 * 鑾峰彇缂撳瓨涓墍鏈夋暟鎹?,渚涙煡鐪嬩娇鐢?
	 * 
	 * @return
	 */
	public String getCacheDataForView();

	/**
	 * 鑾峰彇鏁版嵁搴撲腑鎵?鏈夌紦瀛樻暟鎹?
	 * 
	 * @return
	 */
	public Object getDbCacheData();

	/**
	 * 灏嗘暟鎹坊鍔犲埌缂撳瓨涓?
	 * 
	 * @param cacheData
	 */
	public  void addToCache(Object cacheData);

	/**
	 * 灏嗘暟鎹寔涔呭寲
	 * 
	 * @param cacheData
	 */
	public  void saveCacheData(Object cacheData);

	/**
	 * 鎸佷箙鍖栫紦瀛樹腑鐨勬墍鏈夋暟鎹?
	 */
	public void saveAllCache();

}
