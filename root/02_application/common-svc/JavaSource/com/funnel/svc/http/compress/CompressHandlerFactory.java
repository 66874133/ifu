package com.funnel.svc.http.compress;

public class CompressHandlerFactory {

	/**
	 * 获取对应的压缩处理器
	 * 
	 * @param encoding
	 * @return
	 */
	public static CompressHandler getCompressHandler(String encoding) {

		if (encoding.toLowerCase().indexOf("gzip") > -1) {
			return new GzipCompressHandler();
		} else if (encoding.toLowerCase().indexOf("deflate") > -1) {
			return new DeflateCompressHandler();
		} else if (encoding.equalsIgnoreCase("zip")) {
			return new ZipCompressHandler();
		} else {
			return null;
		}
	}
}
