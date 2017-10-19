package dlt.study.netty;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;

@Deprecated
public class MyObjDecoder implements ChannelUpstreamHandler {

	@Override
	public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
			throws Exception {
		if (e instanceof MessageEvent) {
			MessageEvent mEvent = (MessageEvent) e;
			if (!(mEvent.getMessage() instanceof ChannelBuffer)) {
				ctx.sendUpstream(e);
				return;
			}
			ChannelBuffer buffer = (ChannelBuffer) mEvent.getMessage();
			int prefixLength = MyObjEncoder.getPrefixLength();
			byte[] prefix_bs = new byte[prefixLength];
			buffer.getBytes(0, prefix_bs);
			if (MyObjEncoder.isPrefix(prefix_bs)) {
				ChannelBuffer temp = ChannelBuffers.buffer(buffer.capacity()-prefixLength);
				buffer.getBytes(prefixLength, temp);
				ByteArrayInputStream input = new ByteArrayInputStream(
						temp.array());
				ObjectInputStream ois = new ObjectInputStream(input);
				Object obj = ois.readObject();
				Channels.fireMessageReceived(e.getChannel(), obj);  // 该方法会再次进入handleUpstream方法
			} else
				ctx.sendUpstream(e);

		} else
			ctx.sendUpstream(e);
	}
}
