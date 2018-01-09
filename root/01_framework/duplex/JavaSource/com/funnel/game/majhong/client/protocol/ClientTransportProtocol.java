package com.funnel.game.majhong.client.protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

public class ClientTransportProtocol {

	/**
	 * 请求结果
	 */
	private int ret;

	/**
	 * 请求结果
	 */
	private int status;

	private long version;

	private String data;

	public int getRet() {
		return ret;
	}

	public void setRet(int ret) {
		this.ret = ret;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "ClientTransportProtocol [ret=" + ret + ", status=" + status
				+ ", version=" + version + ", data=" + data + "]";
	}

	public static ClientTransportProtocol toProtocolObject(byte[] bs)
			throws Exception {

		ByteArrayInputStream byteInt2 = new ByteArrayInputStream(bs);

		DataInputStream dintput = new DataInputStream(byteInt2);

		int ret = dintput.readInt();

		int status = dintput.readInt();

		long version = dintput.readLong();

		int size = dintput.readInt();

		byte[] data = new byte[size];
		dintput.read(data, 0, size);
		ClientTransportProtocol t = new ClientTransportProtocol();

		t.setRet(ret);
		t.setStatus(status);
		t.setVersion(version);
		t.setData(new String(data, "utf-8"));
		return t;

	}

	
}
