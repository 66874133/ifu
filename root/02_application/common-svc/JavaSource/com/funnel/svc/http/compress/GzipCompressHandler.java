package com.funnel.svc.http.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

public class GzipCompressHandler implements CompressHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public byte[] handle(byte[] responseBody) throws NotInGZIPFormatException,
			IOException {
		byte[] ret = null;
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		InputStream is = new ByteArrayInputStream(responseBody);
		GZIPInputStream gzin = new GZIPInputStream(is);
		try {

			byte[] bs = new byte[1024 * 50];
			int len = 0;
			while ((len = gzin.read(bs)) != -1) {
				byteOutputStream.write(bs, 0, len);
			}
			ret = byteOutputStream.toByteArray();
			byteOutputStream.close();
			return ret;

		} catch (IOException e) {
			if (null != e.getLocalizedMessage()
					&& e.getLocalizedMessage().equalsIgnoreCase(
							"Not in GZIP format")) {
				throw new NotInGZIPFormatException();
			} else {
				return byteOutputStream.toByteArray();
			}
		} finally {
			try {
				byteOutputStream.close();
				is.close();
				gzin.close();
			} catch (IOException e) {
				logger.error("close byteOutputStream error", e);
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
