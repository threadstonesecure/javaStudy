package dlt.study.netty;

import static org.jboss.netty.channel.Channels.write;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;

/**
 * 编码和解码合二为一 （合并 MyObjDecoder，MyObjEncoder），实现双向传递object。
 * 本类支持ChannelBuffer对象，处理方法是不进行编码和解码。
 * 
 * 
 * 由于NIO的机制，发送和接收的并不是一一对应的（及发送的次数和接收的次数不匹配，往往接收的次数要少与发送次数，但数据流是完整的，不会丢失数据）,
 * 解码的handleUpstream()事件收的字节有可能不是一个完整的Object字节（可能多了，也可能少了），这儿需要特殊处理。
 * 处理起来比较复杂。netty自带的ObjectDecoder类是可以处理该种情况的(由基类LengthFieldBasedFrameDecoder实现)。 本类不支持这种情况。
 * 
 * @author dlt
 *
 */


public class DecoderAndEncoderHandler implements ChannelDownstreamHandler,
		ChannelUpstreamHandler {

	private static final String PREFIX_ENCODER = "$$$$$";
	
	private  int getPrefixLength() {
		return PREFIX_ENCODER.getBytes().length;
	}

	private  boolean isPrefix(byte[] prefix) {
		byte[] bs = PREFIX_ENCODER.getBytes();
		if (bs.length != prefix.length)
			return false;
		for (int i = 0; i < bs.length; i++) {
			if (bs[i] != prefix[i])
				return false;
		}
		return true;
	}
	@Override // read //解码
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof MessageEvent) {
			MessageEvent mEvent = (MessageEvent) e;
			if (!(mEvent.getMessage() instanceof ChannelBuffer)) {
				ctx.sendUpstream(e);
				return;
			}
			ChannelBuffer buffer = (ChannelBuffer) mEvent.getMessage();
			int prefixLength = getPrefixLength();
			byte[] prefix_bs = new byte[prefixLength];
			buffer.getBytes(0, prefix_bs);
			if (isPrefix(prefix_bs)) {
				ChannelBuffer temp = ChannelBuffers.buffer(buffer.capacity()-prefixLength);
				buffer.getBytes(prefixLength, temp);
				ByteArrayInputStream input = new ByteArrayInputStream(
						temp.array());
				ObjectInputStream ois = new ObjectInputStream(input);
				Object obj = ois.readObject();
				//Channels.fireMessageReceived(e.getChannel(), obj);  // 该方法会再次进入第一个handleUpstream方法
				Channels.fireMessageReceived(ctx, obj, ((MessageEvent) e).getRemoteAddress());//进入下一个handleUpstream
			} else
				ctx.sendUpstream(e);

		} else
			ctx.sendUpstream(e);

	}

	@Override  //wirte //编码
    public void handleDownstream(
            ChannelHandlerContext ctx, ChannelEvent evt) throws Exception {
        if (!(evt instanceof MessageEvent)) {
            ctx.sendDownstream(evt);
            return;
        }

        MessageEvent e = (MessageEvent) evt;
        Object originalMessage = e.getMessage();
        Object encodedMessage = encode(ctx, e.getChannel(), originalMessage);
        if (originalMessage == encodedMessage) {
            ctx.sendDownstream(evt);
        } else if (encodedMessage != null) {
            write(ctx, e.getFuture(), encodedMessage, e.getRemoteAddress());//进入下一个handleDownstream
        }
    }

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
}
