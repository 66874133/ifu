package com.funnel.game.majhong.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.duplex.frame.ByteArrayTransfer;
import com.duplex.frame.ByteArrayTransfer.Mode;
import com.duplex.frame.mina.MinaDataInputStream;
import com.duplex.frame.mina.MinaDataOutputStream;
import com.funnel.game.majhong.client.protocol.ClientTransportProtocol;

public class PureByteArrayCodecFactory implements ProtocolCodecFactory {
	private Logger logger = Logger.getLogger(this.getClass());

	private final DataEncoder encoder;
	private final ByteArrayDecoder decoder;

	private Mode mode;

	public PureByteArrayCodecFactory(Mode mode) {
		encoder = new DataEncoder();
		decoder = new ByteArrayDecoder();
		this.mode = mode;
	}

	public ProtocolDecoder getDecoder(IoSession session) throws Exception {
		return decoder;
	}

	public ProtocolEncoder getEncoder(IoSession session) throws Exception {
		return encoder;
	}

	class ByteArrayDecoder extends CumulativeProtocolDecoder {

		public boolean doDecode(IoSession session, IoBuffer in,
				ProtocolDecoderOutput out) throws Exception {

			if (in.remaining() > 0) {// 有数据时，读取4字节判断消息长度
				byte[] sizeBytes = new byte[4];
				in.mark();// 标记当前位置，以便reset

				in.get(sizeBytes);// 读取前4字节

				MinaDataInputStream dataInputStream = new MinaDataInputStream(
						mode, sizeBytes);

				int size = dataInputStream.readInt();
				// 如果消息内容的长度不够则直接返回true
				if (size > in.remaining()) {// 如果消息内容不够，则重置，相当于不读取size
					in.reset();
					return false;// 接收新数据，以拼凑成完整数据
				} else {
					byte[] bytes = new byte[size];
					in.get(bytes, 0, size);

					MinaDataInputStream dataInputStream2 = new MinaDataInputStream(
							mode, bytes);
					out.write(dataInputStream2);

				}
				if (in.remaining() > 0) {// 如果读取内容后还粘了包，就让父类再给俺
					// 一次，进行下一次解析
					return true;
				}
			}

			return false;// 处理成功，让父类进行接收下个包
		}

	}

	class DataEncoder extends ProtocolEncoderAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.mina.filter.codec.ProtocolEncoder#encode(org.apache.mina
		 * .core.session.IoSession, Java.lang.Object,
		 * org.apache.mina.filter.codec.ProtocolEncoderOutput)
		 */
		public void encode(IoSession session, Object message,
				ProtocolEncoderOutput out) throws Exception {
			logger.info("encode message=" + message);

			// IoBuffer buffer = (IoBuffer) message;
			//
			// int len = buffer.limit();
			//
			// byte[] bs = new byte[len];
			// buffer.get(bs);

			IoBuffer buf = IoBuffer.allocate(150).setAutoExpand(true);

			if (message instanceof ClientTransportProtocol) {
				ClientTransportProtocol clientTransportProtocol = (ClientTransportProtocol) message;

				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				DataOutputStream dataOutput = new DataOutputStream(
						byteArrayOutputStream);
				MinaDataOutputStream dataOutputStream = new MinaDataOutputStream(
						ByteArrayTransfer.reverse(mode), dataOutput);

				dataOutputStream.writeInt(clientTransportProtocol.getRet());
				dataOutputStream.writeInt(clientTransportProtocol.getStatus());
				dataOutputStream
						.writeLong(clientTransportProtocol.getVersion());
				byte[] bs = clientTransportProtocol.getData().getBytes("utf-8");
				dataOutputStream.writeInt(bs.length);
				dataOutputStream.write(bs);

				byte[] bs2 = byteArrayOutputStream.toByteArray();

				ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
				DataOutputStream dataOutput2 = new DataOutputStream(
						byteArrayOutputStream2);
				MinaDataOutputStream dataOutputStream2 = new MinaDataOutputStream(
						Mode.NONE, dataOutput2);

				dataOutputStream2.writeInt(bs2.length);
				dataOutputStream2.write(bs2);

			
				byte[] bb = byteArrayOutputStream2.toByteArray();
				buf.put(bb);
				buf.flip();
				out.write(buf);

			} else {
				logger.info("unknow message encode id=" + session.getId()
						+ " message=" + message);
			}
		}

	}
}
