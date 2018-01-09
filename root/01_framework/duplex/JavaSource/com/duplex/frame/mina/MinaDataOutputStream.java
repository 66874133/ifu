package com.duplex.frame.mina;

import java.io.DataOutput;
import java.io.IOException;

import com.duplex.frame.ByteArrayTransfer.Mode;
import com.funnel.game.majhong.server.protocol.NetUtils;

public class MinaDataOutputStream implements DataOutput{

	private DataOutput dataOutput;
	private Mode mode;
	public MinaDataOutputStream(Mode mode,DataOutput dataOutput) {
		this.dataOutput = dataOutput;
		this.mode = mode;
	}
	

	public void write(int b) throws IOException {
		
		
	}

	public void write(byte[] b) throws IOException {
		dataOutput.write(b);
		
	}

	public void write(byte[] b, int off, int len) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeBoolean(boolean v) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeByte(int v) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeShort(int v) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeChar(int v) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeInt(int v) throws IOException {

		switch (mode) {
		case UPPER_TO_LOW:
			v = NetUtils.netInt(v);
			break;
		case LOW_TO_UPPER:
			v = NetUtils.localInt(v);
			break;
		default:
			break;
		}
		
		dataOutput.writeInt(v);
		
	}

	public void writeLong(long v) throws IOException {
		
		
		
		switch (mode) {
		case UPPER_TO_LOW:
			v = NetUtils.netLong(v);
			break;
		case LOW_TO_UPPER:
			v = NetUtils.localLong(v);
			break;
		default:
			break;
		}
		
		dataOutput.writeLong(v);
		
	}

	public void writeFloat(float v) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeDouble(double v) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeBytes(String s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeChars(String s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeUTF(String s) throws IOException {
		// TODO Auto-generated method stub
		
	}

	

}
