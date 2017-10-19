package dlt.study.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.Date;
import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder {

	/*
	 * @Override protected void decode(ChannelHandlerContext ctx, ByteBuf in,
	 * List<Object> out) throws Exception { if (in.readableBytes()<3){ return; }
	 * out.add(in.readBytes(4)); }
	 */
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,
			List<Object> out) throws Exception {
		if (in.readableBytes() < 3) {
			return;
		}
		ByteBuf m = in.readBytes(4);
		long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
		Date date = new Date(currentTimeMillis);
		out.add(date);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("TimeDecoder.channelReadComplete");
		super.channelReadComplete(ctx);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("TimeDecoder.exceptionCaught");
		cause.printStackTrace();
		// ctx.close();
		super.exceptionCaught(ctx, cause);
	}
}
