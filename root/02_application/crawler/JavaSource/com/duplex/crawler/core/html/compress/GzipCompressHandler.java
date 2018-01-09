package com.duplex.crawler.core.html.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipCompressHandler implements CompressHandler {

	public byte[] handle(byte[] responseBody) throws NotInGZIPFormatException {
		byte[] ret = null;
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		try {
			InputStream is = new ByteArrayInputStream(responseBody);

			GZIPInputStream gzin = new GZIPInputStream(is);

			byte[] bs = new byte[1024 * 50];
			int len = 0;
			while ((len = gzin.read(bs)) != -1) {
				byteOutputStream.write(bs, 0, len);
			}
			ret = byteOutputStream.toByteArray();
			byteOutputStream.close();
			return ret;

		} catch (IOException e) {

			// 不是GZIP格式返回原字符
			if (null != e.getLocalizedMessage()
					&& e.getLocalizedMessage().equalsIgnoreCase(
							"Not in GZIP format")) {
				throw new NotInGZIPFormatException();
			} else {
				return byteOutputStream.toByteArray();
			}
		}

	}

	class NotInGZIPFormatException extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}

}
