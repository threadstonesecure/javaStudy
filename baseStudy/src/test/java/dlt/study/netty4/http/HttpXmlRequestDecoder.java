package dlt.study.netty4.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Created by denglt on 2016/4/8.
 */
public class HttpXmlRequestDecoder extends MessageToMessageDecoder<FullHttpRequest> {
    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (!msg.getDecoderResult().isSuccess()) {
            HttpUtils.sendRespError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        HttpXmlRequest request = new HttpXmlRequest(msg, XmlHelper.parseXml(msg.content().toString(CharsetUtil.UTF_8)));
        out.add(request);

    }
}
