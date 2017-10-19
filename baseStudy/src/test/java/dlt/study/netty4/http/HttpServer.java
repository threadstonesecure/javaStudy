package dlt.study.netty4.http;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * Created by denglt on 2016/3/29.
 */
public class HttpServer {
    private static Log log = LogFactory.getLog(HttpServer.class);

    private Channel channel;

    public void run(String ip, int port) throws Exception {

        final EventLoopGroup bossGroup = new NioEventLoopGroup();
        final EventLoopGroup workerGroup = new NioEventLoopGroup();
        int businessThreadPoolSize = 10;
        String name = SystemPropertyUtil.get("os.name").toLowerCase(Locale.UK)
                .trim();
        log.debug("os.name:" + name);
        final MultithreadEventExecutorGroup businessExecutor;
        if (name.startsWith("linux"))
            businessExecutor = new EpollEventLoopGroup(businessThreadPoolSize,
                    new DefaultThreadFactory("HdpServerHandler"));
        else
            businessExecutor = new DefaultEventExecutorGroup(
                    businessThreadPoolSize, new DefaultThreadFactory(
                    "HdpServerHandler")
            );
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.RCVBUF_ALLOCATOR,
                            AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR,
                            PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR,
                            AdaptiveRecvByteBufAllocator.DEFAULT /* 容量动态调整的接收缓冲区分配器 */)
                    .childOption(ChannelOption.ALLOCATOR,
                            PooledByteBufAllocator.DEFAULT /* 内存池 */)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {

                            ch.pipeline().addLast("HttpRequestDecoder", new HttpRequestDecoder());// HttpDecoder在每个Http消息中会生成多个消息对象（HttpRequest、HttpContent、LastHttpContent）
                            ch.pipeline().addLast("HttpObjectAggregator", new HttpObjectAggregator(1024 * 1024 * 1024)); // 将多个消息转换为单一的FullHttpRequest、FullHttpResponse
                            ch.pipeline().addLast("ChunkedWriteHandler", new ChunkedWriteHandler());// 支持异步发送大的码流（如大文件传输），但不占用过多的内存，防止发生Java内存溢出

                            ch.pipeline().addLast("HttpResponseEncoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("HttpRequestHandler", new HttpRequestHandler());

                        }
                    });
            ChannelFuture f;
            if (StringUtils.isEmpty(ip)) {
                f = b.bind(port).sync();
            } else {
                f = b.bind(ip, port).sync();
            }
            channel = f.channel();

            channel.closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            businessExecutor.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8081;
        HttpServer httpServer = new HttpServer();
        httpServer.run(null, port);

    }
}

class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static Log log = LogFactory.getLog(HttpRequestHandler.class);

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("Disconnected from HttpServer !" + ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                FullHttpRequest request) throws Exception {
        HttpUtils.analysisRequest(ctx,request);
        sendText(ctx, "收到ok");
    }



    private void sendText(ChannelHandlerContext ctx, String msg) {
        HttpUtils.sendRespText(ctx, msg);
    }

    private void sendError(ChannelHandlerContext ctx, int code, String msg) {
        HttpUtils.sendRespError(ctx, code, msg);
    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        HttpUtils.sendRespError(ctx, status);
    }
}

class Test {
    Integer isAge;
    Boolean isSex;
    Boolean sex;
    boolean good;

    public Integer getIsAge() {
        return isAge;
    }

    public void setIsAge(Integer isAge) {
        this.isAge = isAge;
    }

    public Boolean getIsSex() {
        return isSex;
    }

    public void setIsSex(Boolean isSex) {
        this.isSex = isSex;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public boolean isGood() {
        return good;
    }

    public void setGood(boolean good) {
        this.good = good;
    }
}