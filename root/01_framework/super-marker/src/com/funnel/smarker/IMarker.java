package com.funnel.smarker;

public interface IMarker {

	/**
	 * 生成站点文件
	 * 
	 * @param ftlDirectory
	 *            FreeMarker的模版文件夹位置
	 * @param siteRoot
	 *            输出站点根目录
	 * @throws Exception
	 */
	public void make(String ftlDirectory, String siteRoot,String db) throws Exception;
}
