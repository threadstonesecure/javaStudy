package com.yuntai.hdp.server;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.access.service.UpdataHandler;
import com.yuntai.hdp.future.FutureResultManager;
import com.yuntai.hdp.server.handler.HospitalLogonHandler;
import com.yuntai.hdp.server.net.ConnectionHandler;
import com.yuntai.hdp.server.ssl.SSLEngineFactory;
import com.yuntai.hdp.server.updata.UpdataHandlerManager;
import com.yuntai.hdp.server.updata.dynamic.DiscoveryUpdataHandler;
import com.yuntai.netty.DefaultNettyContext;
import com.yuntai.util.HdpHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLEngine;
import java.util.ArrayList;
import java.util.List;

/**
 * Hdp的服务端
 *
 * @author denglt
 */
public final class HdpServer {

    public static Log log = LogFactory.getLog(HdpServer.class);


    public static final String HDPSERVER_NOTICE_PREFIX ="$$Hdp Server Notice$$";


    private UpdataHandlerManager updataHandler;
    private DiscoveryUpdataHandler discoveryUpdataHandler;

    private Channel channel;
    private boolean ssl = true;

    private String monitor_ip;
    private int monitor_port = 8089;

    // 数据处理结果管理
    public static FutureResultManager<ResultPack> resultPackManager = new FutureResultManager<ResultPack>();

    // 接收应答结果管理
    public static FutureResultManager<Boolean> receiveAnswerManager = new FutureResultManager<Boolean>();

    // 业务处理线程数量
    private int businessThreadPoolSize = 100;

    private MultithreadEventExecutorGroup businessExecutor;

    public HdpServer() {

    }

    /**
     * 配置 UpdataHandler的管理者
     *
     * @param updataHandler
     */
    public HdpServer updataHandler(UpdataHandlerManager updataHandler) {
        if (updataHandler == null) {
            throw new NullPointerException("updataHandler");
        }
        this.updataHandler = updataHandler;
        return this;
    }

    public HdpServer discoveryUpdataHandler(
            DiscoveryUpdataHandler discoveryUpdataHandler) {
        if (discoveryUpdataHandler == null) {
            throw new NullPointerException("discoveryUpdataHandler");
        }
        this.discoveryUpdataHandler = discoveryUpdataHandler;
        return this;
    }

    /**
     * 配置监控ip和port
     *
     * @param ip
     * @param port
     * @return
     */
    public HdpServer monitor(String ip, int port) {
        this.monitor_ip = ip;
        this.monitor_port = port;
        return this;
    }

    /**
     * 是否使用SSL
     *
     * @param b
     * @return
     */
    public HdpServer ssl(boolean b) {
        if (channel != null) {
            throw new RuntimeException("HdpServer have been started!");
        }
        ssl = b;
        return this;
    }

    public HdpServer businessThreadPoolSize(int corePoolSize) {
        this.businessThreadPoolSize = corePoolSize;
        return this;
    }

    public void run(int port) throws Exception {
        this.run(null, port);
    }

    /**
     * 在指定ip和端口上启动HdpServer，同时在监控的ip和port上启动监控
     *
     * @param ip
     * @param port
     * @throws Exception
     */
    public void run(String ip, int port) throws Exception {
        if (ssl) {
            log.info("HdpServer in SSL mode.");
        } else {
            log.info("HdpServer in Non-Secure mode.");
        }
        final EventLoopGroup bossGroup = DefaultNettyContext.get().getBossEventLoopGroup();
        final EventLoopGroup workerGroup = DefaultNettyContext.get().getWorkerEventLoopGroup();
        businessExecutor = new DefaultEventExecutorGroup(businessThreadPoolSize, new DefaultThreadFactory("HdpServerHandler"));
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                    businessExecutor.shutdownGracefully();
                    if (System.getProperty("hdp.restart") != null && System.getProperty("hdp.restart").equals("1"))
                        HdpHelper.start();
                }catch (Exception e){
                    log.error(e);
                }
            }
        }, "HdpShutdownHook"));

        try {
            Class<? extends ServerChannel> serverChannelClass = DefaultNettyContext.get().getServerChannelClass();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(serverChannelClass)  //NioServerSocketChannel.class)
                    .option(EpollChannelOption.SO_REUSEPORT, true)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .option(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.ALLOCATOR, DefaultNettyContext.get().getAllocator())  //   PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR,AdaptiveRecvByteBufAllocator.DEFAULT /* 容量动态调整的接收缓冲区分配器 */)
                    .childOption(ChannelOption.ALLOCATOR,DefaultNettyContext.get().getAllocator() /* 内存池 */)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        private final ObjectEncoder objectEncoder = new ObjectEncoder();
                        private final ConnectionHandler connectionHandler = new ConnectionHandler();
                        private final HeartBeatRespHandler heartBeatRespHandler = new HeartBeatRespHandler();

                        //private final LoggingHandler loggingHandler = new LoggingHandler("HandlerLog", LogLevel.INFO);
                        private final HospitalReceiveAnswerHandler hospitalReceiveAnswerHandler = new HospitalReceiveAnswerHandler();
                        private final HospitalRequestHandler hospitalRequestHandler = new HospitalRequestHandler();
                        private final HospitalResultHandler  hospitalResultHandler = new HospitalResultHandler();
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            if (ssl) {
                                SSLEngine sslEngine = SSLEngineFactory.createEngine();
                                ch.pipeline().addLast(new SslHandler(sslEngine));

                            }
                            ch.pipeline().addLast(connectionHandler,
                                    new ReadTimeoutHandler(15),
                                    new ObjectDecoder(10485760, ClassResolvers.cacheDisabled(HdpServer.class.getClassLoader())),
                                    objectEncoder);

                            ch.pipeline().addLast(heartBeatRespHandler,
                                    new HospitalLogonHandler(),
                                    hospitalReceiveAnswerHandler,
                                    hospitalRequestHandler,
                                    hospitalResultHandler);

                        }
                    });
            ChannelFuture f;
            if (StringUtils.isEmpty(ip)) {
                f = b.bind(port).sync();
            } else {
                f = b.bind(ip, port).sync();
            }
            channel = f.channel();
            log.info("Hdp Server start on port " + port);
            Channel monitorChannel = startStringCmdMonitor(bossGroup, workerGroup, serverChannelClass);
            channel.closeFuture().sync();
            monitorChannel.close();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            businessExecutor.shutdownGracefully();
        }
    }

    private Channel startStringCmdMonitor(EventLoopGroup bossGroup,
                                          EventLoopGroup workerGroup,
                                          Class<? extends ServerChannel> serverChannelClass) throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(serverChannelClass)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    private final StringCmdHandler stringCmdHandler = new StringCmdHandler();
                    private final StringDecoder stringDecoder = new StringDecoder(CharsetUtil.UTF_8);
                    private final StringEncoder stringEncoder = new StringEncoder(CharsetUtil.UTF_8);

                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new LineBasedFrameDecoder(100), stringDecoder, stringEncoder);
                        ch.pipeline().addLast(stringCmdHandler);
                    }
                }).option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        ChannelFuture f;

        if (StringUtils.isEmpty(monitor_ip)) {
            f = b.bind(monitor_port).sync();
        } else {
            f = b.bind(monitor_ip, monitor_port).sync();
        }
        return f.channel();
    }

    public void close() {
        channel.close();
    }

    public UpdataHandler getUpdataHandler() {
        return updataHandler;
    }

    public void setUpdataHandler(UpdataHandlerManager updataHandler) {
        this.updataHandler = updataHandler;
    }

    /**
     * 心跳响应处理器
     */
    @ChannelHandler.Sharable
    private class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
        private static final String HEART_BEAT = "heartbeat";
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            if (msg.equals(HEART_BEAT))
                ctx.writeAndFlush(HEART_BEAT); // 发送应答心跳
            else
                ctx.fireChannelRead(msg);
        }
    }



    /**
     * 前置机接收应答处理器，即前置机接收到请求后的应答信息处理，确保数据一定发送成功
     */
    @ChannelHandler.Sharable
    private class HospitalReceiveAnswerHandler extends SimpleChannelInboundHandler<String> {

        @Override
        public boolean acceptInboundMessage(Object msg) throws Exception {
            boolean isAccept = super.acceptInboundMessage(msg);
            if (isAccept) {
                String msg42 = (String) msg;
                isAccept = msg42.startsWith("ReceiveAnswer:");
            }
            return isAccept;
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, final String msg) throws Exception {
            businessExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    String hdpSeqno = msg.split(":")[1];
                    HdpServer.receiveAnswerManager.setResult(hdpSeqno, new Boolean(true));
                }
            });
        }
    }

    /**
     * 医院前置机请求处理器，即收到前置机推送数据
     */
    @ChannelHandler.Sharable
    private class HospitalRequestHandler extends SimpleChannelInboundHandler<RequestPack> {

        @Override
        protected void channelRead0(final ChannelHandlerContext ctx, final RequestPack request) throws Exception {
            ctx.writeAndFlush("ReceiveAnswer:" + request.getHdpSeqno());  //发送数据接收应答消息
            businessExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    UpdataHandler handler = HdpServer.this.updataHandler;
                    if (handler != null) {
                        ResultPack result = handler.process(request);
                        result.setSeqno(request.getSeqno());
                        result.setHosId(request.getHosId());
                        result.setHdpSeqno(request.getHdpSeqno());
                        result.setCallMode(request.getCallMode());
                        if (result != null) {
                            ChannelFuture f = ctx.channel().writeAndFlush(result);
                            f.awaitUninterruptibly();
                            if (!f.isSuccess()) {
                                log.error("返回对接数据到前置机异常:", f.cause());
                            }
                        }
                    } else {
                        log.warn("HdpSever's updataHandler is null !");
                    }
                }
            });
        }
    }

    /**
     * 医院前置机对接结果处理器，即收到前置机对接结果
     */
    @ChannelHandler.Sharable
    private class HospitalResultHandler extends SimpleChannelInboundHandler<ResultPack> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, final ResultPack resultPack) throws Exception {
            businessExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    HdpServer.resultPackManager.setResult(resultPack.getHdpSeqno(), resultPack);
                }
            });
        }
    }


    @ChannelHandler.Sharable
    private class StringCmdHandler extends SimpleChannelInboundHandler<String> {
        private final String supportCmds = "Suport Commands{hosp|updatahandler|quit}";

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String cmd) throws Exception {
            cmd = cmd.trim();
            List<String> monitorInfo = new ArrayList<String>();
            switch (cmd) {
                case "hosp":
                    monitorInfo.add("Hospital Info:");
                    monitorInfo.addAll(HospitalManager.getHospitalInfo());
                    break;
                case "updatahandler":
                    monitorInfo.add("dubbo registry info:");
                    monitorInfo.addAll(discoveryUpdataHandler.getRegistryInfo());
                    monitorInfo.add("dubbo updatahandler info:");
                    monitorInfo.addAll(discoveryUpdataHandler.getUpdataHandlerInfo());
                    break;
                case "help":
                    monitorInfo.add(supportCmds);
                    break;
                case "quit":
                    ctx.close();
                    break;
                default:
                    String info = String.format("Command[%s] is not support!", cmd);
                    monitorInfo.add(info);
                    monitorInfo.add(supportCmds);
            }
            if (monitorInfo == null || monitorInfo.size() == 0) {
                ctx.writeAndFlush("no info!\n");
            } else {
                StringBuffer sb = new StringBuffer();
                for (String s : monitorInfo)
                    sb.append(s + "\n");
                ctx.writeAndFlush(sb);
            }
        }

    }

}
