package dlt.study.netty4.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Created by denglt on 2016/4/8.
 */
public class HttpXmlResponseEndcoder extends MessageToMessageEncoder<HttpXmlResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
        Object body = msg.getResult();
        ByteBuf content = Unpooled.copiedBuffer(XmlHelper.toXml(body), CharsetUtil.UTF_8);
        FullHttpResponse response = msg.getResponse();
        if (response == null){
            response = new DefaultFullHttpResponse( HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        }else{
            response = new DefaultFullHttpResponse( msg.getResponse().getProtocolVersion(), msg.getResponse().getStatus(), content);
        }
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/xml; charset=UTF-8");
        HttpHeaders.setContentLength(response,content.readableBytes());
        out.add(response);
    }
}
