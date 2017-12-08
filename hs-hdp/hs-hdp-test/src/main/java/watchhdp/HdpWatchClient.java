package watchhdp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.HdpHelper;
import com.yuntai.hdp.client.Receiver;
import com.yuntai.hdp.client.net.ReconnectHandler;
import com.yuntai.hdp.client.ssl.SSLEngineFactory;
import com.yuntai.hdp.future.FutureResult;
import com.yuntai.hdp.future.FutureResultManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.FutureListener;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author denglt@hundsun.com
 * @Description HDP客户端通讯类
 * @CopyRight: 版权归Hundsun 所有
 */

public final class HdpWatchClient implements
        ReconnectHandler.Connector<HdpWatchClient> {

    private static final String CMD_REG_HOSPITAL = "$$Register Hospital$$";
    private static final String REG_HOSPITAL_OK = "$$Register Hospital OK$$";


    private static Log log = LogFactory.getLog(HdpWatchClient.class);

    private Channel channel;
    private String hosId;
    private String clientId = HdpHelper.getUUID();

    private InetSocketAddress remoteAddress;
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private DefaultEventExecutorGroup businessExecutor;
    private Receiver receiver;// 异步下发数据接收器

    // Sleep 5 seconds before a reconnection attempt.
    private int reconnectDelay = 5;

    // 业务处理线程数量
    private int businessThreadPoolSize = 20;

    // 等待接收数据应答时长(单位秒)
    private int waitAnswerTime = 3;

    private boolean ssl = true;

    private String accessToken = null;

    private DataHandler dataHandler = null;

    // 数据处理结果管理
    public static FutureResultManager<ResultPack> resultPackManager = new FutureResultManager<ResultPack>();

    // 接收应答结果管理
    public static FutureResultManager<Boolean> receiveAnswerManager = new FutureResultManager<Boolean>();

    public HdpWatchClient() {

    }

    public HdpWatchClient(String host, int port) {
        this();
        remoteAddress = new InetSocketAddress(host, port);
    }

    /**
     * 设置服务器的ip和port
     *
     * @param inetHost
     * @param inetPort
     * @return
     */
    public HdpWatchClient remoteAddress(String inetHost, int inetPort) {
        remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }

    /**
     * 是否使用SSL
     *
     * @param b
     * @return
     */
    public HdpWatchClient ssl(boolean b) {
        if (channel != null && channel.isActive()) {
            throw new RuntimeException("HdpClient have been started!");
        }
        ssl = b;
        return this;
    }

    /**
     * 设置医院id
     *
     * @param hosId
     * @return
     */
    public HdpWatchClient hosId(String hosId) {
        if (hosId == null || hosId.length() == 0) {
            throw new NullPointerException("hosId");
        }
        this.hosId = hosId;
        return this;
    }

    public HdpWatchClient accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * 设置重连接失败时间间隔，单位为秒
     *
     * @param reconnectDelay
     * @return
     */
    public HdpWatchClient reconnectDelay(int reconnectDelay) {
        if (reconnectDelay <= 5 && reconnectDelay > 0)
            this.reconnectDelay = reconnectDelay;
        return this;
    }

    /**
     * 设置业务处理线程池大小
     *
     * @param corePoolSize
     * @return
     */
    public HdpWatchClient businessThreadPoolSize(int corePoolSize) {
        // if (corePoolSize > businessThreadPoolSize)
        this.businessThreadPoolSize = corePoolSize;
        return this;
    }

    /**
     * 设置等待接收数据应答时长(单位秒)
     *
     * @param timeout
     * @return
     */
    public HdpWatchClient waitAnswerTime(int timeout) {
        this.waitAnswerTime = timeout;
        return this;
    }


    public HdpWatchClient dataHandler(DataHandler dataHandler) {
        this.dataHandler = dataHandler;
        return this;
    }


    /**
     * 连接HDP Server
     *
     * @return
     */
    public HdpWatchClient connect() {

        log.info("HDP Client is Starting...");
        try {
            if (businessExecutor == null) {
                businessExecutor = new DefaultEventExecutorGroup(
                        businessThreadPoolSize, new DefaultThreadFactory(
                        "HdpClientHandler"));
            }
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR,
                            AdaptiveRecvByteBufAllocator.DEFAULT)
                    .remoteAddress(remoteAddress)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {

                            if (ssl) {
                                SSLEngine sslEngine = SSLEngineFactory
                                        .createEngine();
                                ch.pipeline()
                                        .addLast(new SslHandler(sslEngine));
                            }

                            ch.pipeline()
                                    .addLast(
                                            new ReadTimeoutHandler(60),
                                            new ReconnectHandler(
                                                    HdpWatchClient.this,
                                                    HdpWatchClient.this.reconnectDelay),
                                            new ObjectDecoder(
                                                    ClassResolvers
                                                            .cacheDisabled(HdpWatchClient.class
                                                                    .getClassLoader())),
                                            new ObjectEncoder());
                            ch.pipeline().addLast(new HeartBeatReqHandler(1),
                                    new HospitalLogonHanhler(),
                                    new BusinessHandler());
                        }
                    });

            ChannelFuture f = b.connect().awaitUninterruptibly();
            channel = f.channel();
            if (f.isSuccess()) {
                log.info(String
                        .format("HDP Client  successfully connect to the HDP Server on[%s:%d] in %s mode.",
                                remoteAddress.getHostName(), remoteAddress
                                        .getPort(), (ssl ? "SSL"
                                        : " Non-Secure")));

            } else {
                log.error(
                        "HDP Client  can not connect to the HDP Server. Reason:",
                        f.cause());
            }

        } catch (Exception e) {// 理论上应该没有该异常发生
            log.error("连接HdpServer发生意外：", e);
            log.info("After Sleeping for: " + reconnectDelay
                    + "s , reconnect to HdpServer");

            workerGroup.schedule(new Runnable() {
                @Override
                public void run() {
                    connect();
                }
            }, reconnectDelay, TimeUnit.SECONDS);
        }

        return this;
    }

    public boolean isActive() {
        return channel.isActive();
    }

    /**
     * 关闭HdpClient
     *
     * @throws InterruptedException
     */
    public void close() throws InterruptedException {
        try {
            channel.close().sync();
            log.info("HDP Client Close.");
        } finally {
            workerGroup.shutdownGracefully();
            businessExecutor.shutdownGracefully();
        }
    }

    private boolean _synSend(RequestPack request) {
        FutureResult<Boolean> result = HdpWatchClient.receiveAnswerManager
                .newFutureResult(request.getHdpSeqno());
        ChannelFuture f = channel.writeAndFlush(request);
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

            }
        });
        f.awaitUninterruptibly();
        if (f.isSuccess()) {
            Boolean b = result.get(waitAnswerTime);
            return b != null;
        } else {
            HdpWatchClient.receiveAnswerManager.remove(request.getHdpSeqno());
            log.error("发送数据失败：", f.cause());
            return false;
        }

    }

    /**
     * 同步发送请求
     *
     * @param request
     * @param timeout 单位秒
     * @return
     */
    public ResultPack sendData(RequestPack request, int timeout) {
        fillData(request);
        request.setCallMode(1);
        if (StringUtils.isEmpty(request.getCmd())) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_MISS_KEYFIELD.getKind());
            resultPack.setMsg(ResultKind.ERROR_MISS_KEYFIELD.getMessage("cmd"));
            log.error(String.format("===>请求失败,数据错误:%s",
                    resultPack.toKeyString()));
            return resultPack;
        }

        if (StringUtils.isEmpty(request.getBody())) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_MISS_KEYFIELD.getKind());
            resultPack
                    .setMsg(ResultKind.ERROR_MISS_KEYFIELD.getMessage("body"));
            log.error(String.format("===>请求失败,数据错误:%s",
                    resultPack.toKeyString()));
            return resultPack;
        }

        FutureResult<ResultPack> futureResult = HdpWatchClient.resultPackManager
                .newFutureResult(request.getHdpSeqno());

        if (!_synSend(request)) {
            HdpWatchClient.resultPackManager.remove(request.getHdpSeqno());
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_NET.getKind());
            resultPack.setMsg(ResultKind.ERROR_NET.getMessage("")
                    + "/连接HdpServer失败");
            log.error(String.format("===>请求对接失败:%s", resultPack.toKeyString()));
            return resultPack;
        }
        ResultPack resultPack = futureResult.get(timeout);
        if (resultPack == null) {
            resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_TIMEOUT.getKind());
            resultPack.setMsg(ResultKind.ERROR_TIMEOUT.getMessage(timeout + ""));
            log.error(String.format("对接HdpServer超时：%s", resultPack));
            return resultPack;
        }
        return resultPack;
    }


    private boolean _bombData(RequestPack request) {
        FutureResult<Boolean> result = HdpWatchClient.receiveAnswerManager
                .newFutureResult(request.getHdpSeqno());
        ChannelFuture f = channel.writeAndFlush(request);
        f.awaitUninterruptibly();
        if (f.isSuccess()) {
            return true;
        } else {
            HdpWatchClient.receiveAnswerManager.remove(request.getHdpSeqno());
            log.error("发送数据失败：", f.cause());
            return false;
        }

    }
    public void bombData(RequestPack request){
        fillData(request);
        request.setCallMode(1);
        _bombData(request);
    }
    private void fillData(RequestPack data) {
        if (StringUtils.isEmpty(data.getClientId())) {
            data.setClientId(clientId);
        }
        if (StringUtils.isEmpty(data.getHosId())) {
            data.setHosId(hosId);
        }

        if (data.getSendTime() <= 0) {
            data.setSendTime(System.currentTimeMillis());
        }
        data.setHdpSeqno(HdpHelper.getUUID());
    }


    /**
     * 注册hosId处理器
     */
    private class HospitalLogonHanhler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelActive(final ChannelHandlerContext ctx)
                throws Exception {
            if (!StringUtils.isEmpty(hosId)) {
                RequestPack data = new RequestPack();
                data.setClientId(clientId);
                data.setHosId(hosId);
                data.setCmd(CMD_REG_HOSPITAL);
                data.setBody(String.format("{accessToken :\"%s\" , isLog : false , isMonitor : true }", accessToken));
                data.setSendTime(System.currentTimeMillis());
                ctx.writeAndFlush(data);
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            if (msg.equals(REG_HOSPITAL_OK)) {
                log.info("HDP Client register hospital successfully.");
                return;
            }
            super.channelRead(ctx, msg);
        }
    }

    /**
     * 心跳请求处理器
     */
    private class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {
        private static final String HEART_BEAT = "heartbeat";
        private final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
        private final long intervalNanos;
        private volatile ScheduledFuture<?> heartBeat;
        private volatile int state; // 0 - none, 1 - Initialized, 2 - Destroyed;

        public HeartBeatReqHandler(int intervalSeconds) {
            if (intervalSeconds < 0)
                intervalNanos = 0;
            else
                intervalNanos = Math.max(
                        TimeUnit.SECONDS.toNanos(intervalSeconds),
                        MIN_TIMEOUT_NANOS);
        }

        @Override
        public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
            if (ctx.channel().isActive() && ctx.channel().isRegistered()) {
                initialize(ctx);
            } else {

            }
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
            destroy();
        }

        @Override
        public void channelRegistered(ChannelHandlerContext ctx)
                throws Exception {
            if (ctx.channel().isActive()) {
                initialize(ctx);
            }
            super.channelRegistered(ctx);
        }

        @Override
        public void channelActive(final ChannelHandlerContext ctx)
                throws Exception {
            initialize(ctx);
            super.channelActive(ctx);
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            destroy();
            super.channelInactive(ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            if (msg.equals(HEART_BEAT)) {
                log.info("收到服务端心跳");
                return;
            }
            super.channelRead(ctx, msg);
        }

        private void initialize(final ChannelHandlerContext ctx) {
            switch (state) {
                case 1:
                case 2:
                    return;
            }
            state = 1;
            heartBeat = ctx.executor().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    if (!ctx.channel().isOpen()) {
                        return;
                    }

                    //log.info("发送心跳");
                    ChannelFuture channelFuture = ctx.writeAndFlush(HEART_BEAT);
                    System.out.println(channelFuture.getClass()); // io.netty.channel.DefaultChannelPromise  see: ChannelOutboundHandler.void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception;
                    channelFuture.addListener(new ChannelFutureListener() {
                        @Override
                        public void operationComplete(ChannelFuture future) throws Exception {
                            //
                        }
                    })
                    channelFuture.awaitUninterruptibly();
                    if (channelFuture.isSuccess()) {
                        log.info("发送心跳");
                    } else {
                        log.error("发送心跳失败：", channelFuture.cause());
                    }
                }
            }, 0, intervalNanos, TimeUnit.NANOSECONDS);
        }

        private void destroy() {
            state = 2;

            if (heartBeat != null) {
                heartBeat.cancel(false);
                heartBeat = null;
            }
        }
    }

    /**
     * 业务处理器
     */
    private class BusinessHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelRead(final ChannelHandlerContext ctx,
                                final Object msg) throws Exception {
/*			if (!msg.equals(HeartBeatReqHandler.HEART_BEAT))
                log.info(String.format("Receive [%s] from HdpServer", msg));*/

            if (msg instanceof RequestPack) {
                RequestPack request = (RequestPack) msg;
                ctx.writeAndFlush("ReceiveAnswer:" + request.getHdpSeqno());
            }

            if (msg instanceof String) {
                final String msg42 = (String) msg;
                if (msg42.startsWith("ReceiveAnswer:")) { // 接收到数据应答
                    String hdpSeqno = msg42.split(":")[1];
                    HdpWatchClient.receiveAnswerManager.setResult(hdpSeqno, new Boolean(true));
                }
            } else {
                HdpWatchClient.Task task = null;
                if (msg instanceof RequestPack) {
                    task = new HdpWatchClient.RequestPackTask(ctx, (RequestPack) msg, 30000);
                } else if (msg instanceof ResultPack) {
                    task = new HdpWatchClient.ResultPackTask(ctx, (ResultPack) msg, 30000);
                }
                if (task == null) {
                    log.error("Data format is error！ Data Content：" + msg);
                } else {
                    // log.info("execute task");
                    businessExecutor.execute(task);
                }
            }

        }
    }

    private abstract class Task<T> implements Runnable {
        private T data;
        private long startTime;
        private long timeoutMillis;
        protected ChannelHandlerContext ctx;

        public Task(ChannelHandlerContext ctx, T data, long timeoutMillis) {
            this.data = data;
            startTime = System.currentTimeMillis();
            this.timeoutMillis = timeoutMillis;
            this.ctx = ctx;
        }

        @Override
        public void run() {
            if ((System.currentTimeMillis() - startTime) > timeoutMillis) {
                log.error(String.format("Task[%s] wait timeout!", data));
                return;
            }
            deal(data);
        }

        abstract void deal(T data);
    }

    /**
     * 收到的HdpServer请求RequestPack，包括同步与异步
     */
    private class RequestPackTask extends Task<RequestPack> {

        private RequestPackTask(ChannelHandlerContext ctx, RequestPack data,
                                long timeoutMillis) {
            super(ctx, data, timeoutMillis);
        }

        @Override
        void deal(RequestPack request) {
            ResultPack result = HdpWatchClient.this.dataHandler.dealData(request);
            if (result == null) return;
            ChannelFuture f = ctx.writeAndFlush(result);
            f.awaitUninterruptibly();
            if (!f.isSuccess()) {
                log.error("返回对接数据到HdpServer异常：", f.cause());
            }
        }
    }

    /**
     * 收到HdpServer处理结果ResultPack
     */
    private class ResultPackTask extends Task<ResultPack> {
        private ResultPackTask(ChannelHandlerContext ctx, ResultPack data,
                               long timeoutMillis) {
            super(ctx, data, timeoutMillis);
        }

        @Override
        void deal(ResultPack resultPack) {
            if (HdpWatchClient.resultPackManager.contain(resultPack.getHdpSeqno())) {
                HdpWatchClient.resultPackManager.setResult(resultPack.getHdpSeqno(), resultPack);
            } else
                HdpWatchClient.this.dataHandler.dealData(resultPack);
        }
    }
}
