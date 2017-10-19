package dlt.study.netty4.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import io.netty.util.NetUtil;

import java.util.List;

/**
 * Created by denglt on 2016/4/1.
 */
public class HttpXmlRequestEncoder extends MessageToMessageEncoder<HttpXmlRequest> {


    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg, List<Object> out) throws Exception {
        Object body = msg.getBody();
        ByteBuf content = Unpooled.copiedBuffer(XmlHelper.toXml(body), CharsetUtil.UTF_8);
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", content);
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaders.Names.HOST, NetUtil.LOCALHOST.getHostAddress());
            headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            headers.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP + "," + HttpHeaders.Values.DEFLATE);
            //headers.set(HttpHeaders.Names.ACCEPT_CHARSET,"");
            //headers.set(HttpHeaders.Names.USER_AGENT,"");
            headers.set(HttpHeaders.Names.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        }
        HttpHeaders.setContentLength(request, content.readableBytes());
        out.add(request);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}
