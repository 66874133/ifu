package com.duplex.crawler.core.html.compress;

public interface CompressHandler {

	/**
	 * 进行解压缩
	 * 
	 * @param bs
	 * @return
	 * @throws Exception
	 */
	public byte[] handle(byte[] bs) throws Exception;
}
