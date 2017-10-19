package dlt.study.netty;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

@Deprecated
public class MyObjEncoder extends OneToOneEncoder {

	private static final String PREFIX_ENCODER = "$$$$$";

	public MyObjEncoder() {

	}

	@Override
	protected Object encode(ChannelHandlerContext ctx, Channel channel,
			Object msg) throws Exception {
		if (msg instanceof ChannelBuffer) {
			return msg;
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(msg);
		oos.flush();
		oos.close();
		ChannelBuffer encoded = ChannelBuffers.dynamicBuffer();
		encoded.writeBytes(PREFIX_ENCODER.getBytes());
		encoded.writeBytes(out.toByteArray());
		return encoded;
	}

	public static int getPrefixLength() {
		return PREFIX_ENCODER.getBytes().length;
	}

	public static  boolean isPrefix(byte[] prefix) {
		byte[] bs = PREFIX_ENCODER.getBytes();
		if (bs.length != prefix.length)
			return false;
		for (int i = 0; i < bs.length; i++) {
			if (bs[i] != prefix[i])
				return false;
		}
		return true;
	}
}
