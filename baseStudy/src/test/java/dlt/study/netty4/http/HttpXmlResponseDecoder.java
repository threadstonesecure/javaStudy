package dlt.study.netty4.http;

import com.sun.tools.internal.ws.util.xml.XmlUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Created by denglt on 2016/4/8.
 */
public class HttpXmlResponseDecoder extends MessageToMessageDecoder<FullHttpResponse> {

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) throws Exception {
        if (!msg.getDecoderResult().isSuccess()) {
            System.out.println(msg.getDecoderResult().cause());
            return;
        }

        HttpXmlResponse response = new HttpXmlResponse(msg, XmlHelper.parseXml(msg.content().toString(CharsetUtil.UTF_8)));
        out.add(response);
    }
}
