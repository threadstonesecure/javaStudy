package dlt.study.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Created by denglt on 16/10/20.
 */
@ChannelHandler.Sharable
public class MyStringEncoder extends MessageToByteEncoder<CharSequence> {

    private final Charset charset;
    public MyStringEncoder() {
        this(Charset.defaultCharset());
    }

    /**
     * Creates a new instance with the specified character set.
     */
    public MyStringEncoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        this.charset = charset;
    }
    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, ByteBuf out) throws Exception {
        //ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), charset);
        out.writeBytes(msg.toString().getBytes(charset));

    }
}
