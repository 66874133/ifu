package com.funnel.svc.http.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DeflaterInputStream;

import org.apache.log4j.Logger;

public class DeflateCompressHandler implements CompressHandler {

	private Logger logger = Logger.getLogger(this.getClass());

	@Override
	public byte[] handle(byte[] responseBody) throws IOException {
		byte[] ret = null;
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

		InputStream is = new ByteArrayInputStream(responseBody);

		DeflaterInputStream dzin = new DeflaterInputStream(is);

		try {
			byte[] bs = new byte[1024 * 50];
			int len = 0;
			while ((len = dzin.read(bs)) != -1) {
				byteOutputStream.write(bs, 0, len);
			}

			ret = byteOutputStream.toByteArray();
			byteOutputStream.close();
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			byteOutputStream.close();
			is.close();
			dzin.close();
		}
		return ret;
	}

}
