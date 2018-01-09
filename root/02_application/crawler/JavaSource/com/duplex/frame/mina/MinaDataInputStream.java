package com.duplex.frame.mina;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import com.duplex.frame.ByteArrayTransfer.Mode;
import com.duplex.frame.mina.util.NetUtils;

public class MinaDataInputStream implements DataInput {

	private DataInputStream dataInputStream;



	private Mode mode;

	public MinaDataInputStream(Mode mode, DataInputStream dataInputStream) {
		this.dataInputStream = dataInputStream;
		this.mode = mode;
	}
	
	public MinaDataInputStream(Mode mode, byte[] bs) {
		ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bs);
		DataInputStream dintput = new DataInputStream(arrayInputStream);
		this.dataInputStream = dintput;
		this.mode = mode;
	}

	public int readInt() throws IOException {

		int value = dataInputStream.readInt();

		switch (mode) {
		case UPPER_TO_LOW:
			value = NetUtils.netInt(value);
			break;

		default:
			break;
		}

		return value;
	}

	public long readLong() throws IOException {

		long value = dataInputStream.readLong();

		switch (mode) {
		case UPPER_TO_LOW:
			value = NetUtils.netLong(value);
			break;

		default:
			break;
		}

		return value;
	}

	public int read(byte b[], int off, int len) throws IOException {
		return dataInputStream.read(b, off, len);
	}

	public void readFully(byte[] b) throws IOException {
		dataInputStream.readFully(b);

	}

	public void readFully(byte[] b, int off, int len) throws IOException {
		dataInputStream.readFully(b, off, len);

	}

	public int skipBytes(int n) throws IOException {
		return dataInputStream.skipBytes(n);
	}

	public boolean readBoolean() throws IOException {

		return dataInputStream.readBoolean();
	}

	public byte readByte() throws IOException {

		return dataInputStream.readByte();
	}

	public int readUnsignedByte() throws IOException {

		return dataInputStream.readUnsignedByte();
	}

	public short readShort() throws IOException {
		return dataInputStream.readShort();
	}

	public int readUnsignedShort() throws IOException {
		return dataInputStream.readUnsignedShort();
	}

	public char readChar() throws IOException {
		return dataInputStream.readChar();
	}

	public float readFloat() throws IOException {
		return dataInputStream.readFloat();
	}

	public double readDouble() throws IOException {
		return dataInputStream.readDouble();
	}

	@Deprecated
	public String readLine() throws IOException {
		return dataInputStream.readLine();
	}

	public String readUTF() throws IOException {
		return dataInputStream.readUTF();
	}

}
