package dlt.study.netty4.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.CharsetUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Created by denglt on 2016/4/8.
 */
public class HttpUtils {

    private static Log log = LogFactory.getLog(HttpUtils.class);

    public static void sendRespText(ChannelHandlerContext ctx, String msg) {
        sendResponse(ctx, Unpooled.copiedBuffer(msg + "\r\n", CharsetUtil.UTF_8));

    }

    public static void sendResponse(ChannelHandlerContext ctx, ByteBuf content) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    public static void sendRespError(ChannelHandlerContext ctx, int code, String msg) {
        HttpResponseStatus status = new HttpResponseStatus(code, msg);
        sendRespError(ctx, status);
    }

    public static void sendRespError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                                                                Unpooled.copiedBuffer("Failure:" + status.toString() + "\r\n", CharsetUtil.UTF_8)
                                                                );
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);  //关闭连接
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }


    public static void analysisRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.getDecoderResult().isSuccess()) {
            log.error("Http 解码失败：", request.getDecoderResult().cause());
            sendRespError(ctx, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        if (!(request.getMethod().equals(HttpMethod.GET) || request.getMethod()
                .equals(HttpMethod.POST))) {
            sendRespError(ctx, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }
        HttpHeaders headers = request.headers();
        log.info("===========  Request Header ==================");
        List<Map.Entry<String, String>> entries = headers.entries();
        for (Map.Entry<String, String> header : entries) {
            log.info(header.getKey() + ":" + header.getValue());
        }
        log.info("http url:" + request.getUri());

        // log.info("FullHttpRequest:" + request.getClass());
        QueryStringDecoder decoderQuery = new QueryStringDecoder(
                request.getUri());
        log.info("=========== Uri Parameters ==================");
        Map<String, List<String>> uriParams = decoderQuery.parameters();

        for (Map.Entry<String, List<String>> entry : uriParams.entrySet()) {
            log.info(entry.getKey() + ":" + entry.getValue());
        }


        ByteBuf buf = request.content();
        String content = buf.toString(io.netty.util.CharsetUtil.UTF_8);
        log.info("content:" + content);
        log.info("=========== Post Body ======================");
        if (buf.isReadable()) {
            try {
                HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(
                        request);
                while (postDecoder.hasNext()) {
                    HttpUtils.printData(postDecoder.next());
                }
            } catch (Exception ex) {
                sendRespError(ctx, 99999, "Decode Post Body Fail!");
                return;
            }
        }

    }


    private static void printData(InterfaceHttpData data) throws Exception {
        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
            Attribute attribute = (Attribute) data;
            String key = attribute.getName();
            String value = attribute.getValue();
            log.info(key + ":" + value);
        }

        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
            log.info("========= File Upload =========================");
            FileUpload fileUpload = (FileUpload) data;
            log.info("filename:" + fileUpload.getFilename());
            byte[] filecontent = fileUpload.get();
            log.info("filesize:" + filecontent.length + " byte");
            File file = fileUpload.getFile();
            log.info("filepath:" + file.getPath());
            log.info("filelength:" + file.length());
        }

        if (data.getHttpDataType() == InterfaceHttpData.HttpDataType.InternalAttribute) {
            //
        }
    }
}
