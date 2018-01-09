package com.duplex.crawler.core.html.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

public class ZipCompressHandler implements CompressHandler {

	public byte[] handle(byte[] responseBody) throws IOException {
		byte[] ret = null;
		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();

		InputStream is = new ByteArrayInputStream(responseBody);

		ZipInputStream zin = new ZipInputStream(is);

		byte[] bs = new byte[1024 * 50];
		int len = 0;
		while ((len = zin.read(bs)) != -1) {
			byteOutputStream.write(bs, 0, len);
		}

		ret = byteOutputStream.toByteArray();
		byteOutputStream.close();
		return ret;
	}

}
