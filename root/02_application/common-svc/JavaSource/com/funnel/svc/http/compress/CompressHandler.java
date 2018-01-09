package com.funnel.svc.http.compress;

public interface CompressHandler {


	public byte[] handle(byte[] bs) throws Exception;
}
