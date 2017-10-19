package dlt.study.netty4.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import io.netty.util.internal.SystemPropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by denglt on 2016/4/11.
 */
public class WebSocketServer {
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
                            ch.pipeline().addLast("http-codec",new HttpServerCodec());
                            ch.pipeline().addLast("HttpObjectAggregator", new HttpObjectAggregator(1024 * 1024 * 1024));
                            ch.pipeline().addLast("ChunkedWriteHandler", new ChunkedWriteHandler());
                            ch.pipeline().addLast("handler",new WebSocketServerHandler());
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
        WebSocketServer socketServer = new WebSocketServer();
        socketServer.run(null,port);
    }
}

class WebSocketServerHandler extends  SimpleChannelInboundHandler<Object>{
    private static Log log = LogFactory.getLog(WebSocketServerHandler.class);

    private  WebSocketServerHandshaker handshaker;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){  // 传统的http接入
            handleHttpRequest(ctx,(FullHttpRequest)msg);
        }else if( msg instanceof WebSocketFrame){
            handleWebSocketFrame(ctx,(WebSocketFrame)msg);
        }
    }


    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request){
        HttpUtils.analysisRequest(ctx,request);
        if (!"websocket".equals(request.headers().get("Upgrade"))){ // 非webSocket握手请求
            HttpUtils.sendRespText(ctx, "收到ok");
            //HttpUtils.sendRespError(ctx,HttpResponseStatus.BAD_REQUEST);
            return;
        }

        //进行握手
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory("ws://localhost:8081/websocket",null,false);
        handshaker  = wsFactory.newHandshaker(request);
        if (handshaker == null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),request); // add newWebsocketDecoder ,newWebSocketEncoder
        }
    }

    private  void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame){

        if (frame instanceof CloseWebSocketFrame){
            log.info("close WebSocketFrame");
            handshaker.close(ctx.channel(),(CloseWebSocketFrame)frame.retain());
            return;
        }

        if (frame instanceof PingWebSocketFrame){
            ctx.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (!(frame instanceof  TextWebSocketFrame)){
            log.info("WebSocketFrame:" + frame);
            throw new UnsupportedOperationException(frame.getClass().getName() + " types not supported");
        }

        String request = ((TextWebSocketFrame)frame).text();
        log.info("收到消息：" + request);
        ctx.channel().writeAndFlush(new TextWebSocketFrame(request+",欢迎使用Netty WebSocket服务，现在时刻:" + new Date().toString()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}