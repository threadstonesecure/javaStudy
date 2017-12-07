package com.yuntai.hdp.client;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultKind;
import com.yuntai.hdp.access.ResultPack;
import com.yuntai.hdp.client.net.ReconnectHandler;
import com.yuntai.hdp.client.ssl.SSLEngineFactory;
import com.yuntai.hdp.future.FutureResult;
import com.yuntai.hdp.future.FutureResultManager;
import com.yuntai.netty.DefaultNettyContext;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.net.ssl.SSLEngine;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

/**
 * @author denglt@hundsun.com
 * @Description HDP客户端通讯类
 * @CopyRight: 版权归Hundsun 所有
 */

public final class HdpClient implements ReconnectHandler.Connector<HdpClient> {


    private static final String HDPSERVER_NOTICE_PREFIX = "$$Hdp Server Notice$$";


    private static Log log = LogFactory.getLog(HdpClient.class);

    private Channel channel;
    private String hosId;
    private String clientId = HdpHelper.getUUID();

    private InetSocketAddress remoteAddress;
    private EventLoopGroup workerGroup;
    private MultithreadEventExecutorGroup businessExecutor;
    private Receiver receiver;// 异步下发数据接收器
    private SynAccessHospital synAccessHospital; // 同步访问hs数据
    // Sleep 5 seconds before a reconnection attempt.
    private int reconnectDelay = 5;

    // 业务处理线程数量
    private int businessThreadPoolSize = 20;

    // 等待接收数据应答时长(单位秒)
    private int waitAnswerTime = 3;

    private boolean ssl = true;

    private String accessToken = null;

    private boolean isLogon = false;

    // 数据处理结果管理
    public static FutureResultManager<ResultPack> resultPackManager = new FutureResultManager<ResultPack>();

    // 接收应答结果管理
    public static FutureResultManager<Boolean> receiveAnswerManager = new FutureResultManager<Boolean>();

    public HdpClient() {

    }

    public HdpClient(String host, int port) {
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
    public HdpClient remoteAddress(String inetHost, int inetPort) {
        remoteAddress = new InetSocketAddress(inetHost, inetPort);
        return this;
    }

    /**
     * 是否使用SSL
     *
     * @param b
     * @return
     */
    public HdpClient ssl(boolean b) {
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
    public HdpClient hosId(String hosId) {
        if (hosId == null || hosId.length() == 0) {
            throw new NullPointerException("hosId");
        }
        this.hosId = hosId;
        return this;
    }

    /**
     * 设置前置机accessToken
     *
     * @param accessToken
     * @return
     */
    public HdpClient accessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    /**
     * 设置重连接失败时间间隔，单位为秒
     *
     * @param reconnectDelay
     * @return
     */
    public HdpClient reconnectDelay(int reconnectDelay) {
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
    public HdpClient businessThreadPoolSize(int corePoolSize) {
        if (corePoolSize > businessThreadPoolSize)
            this.businessThreadPoolSize = corePoolSize;
        return this;
    }

    /**
     * 设置等待接收数据应答时长(单位秒)
     *
     * @param timeout
     * @return
     */
    public HdpClient waitAnswerTime(int timeout) {
        this.waitAnswerTime = timeout;
        return this;
    }

    /**
     * 设置数据接收者
     *
     * @param receiver
     * @return
     */
    public HdpClient receiver(Receiver receiver) {
        if (receiver == null) {
            throw new NullPointerException("receiver is null");
        }
        this.receiver = receiver;
        return this;
    }

    /**
     * 设置同步数据对接处理器
     *
     * @param synAccessHospital
     * @return
     */
    public HdpClient synAccessHospital(SynAccessHospital synAccessHospital) {
        if (synAccessHospital == null) {
            throw new NullPointerException("synAccessHospital is null");
        }

        this.synAccessHospital = synAccessHospital;
        return this;
    }

    /**
     * 连接HDP Server
     *
     * @return
     */
    public HdpClient connect() {

        log.info("HDP Client is Starting...");
        isLogon = false;
        try {
            if (workerGroup == null) {
                workerGroup = DefaultNettyContext.get().getWorkerEventLoopGroup();
            }
            if (businessExecutor == null) {
                businessExecutor = new DefaultEventExecutorGroup(businessThreadPoolSize, new DefaultThreadFactory("HdpClientHandler"));
            }
            Bootstrap b = new Bootstrap();
            b.group(workerGroup)
                    .channel(DefaultNettyContext.get().getChannelClass())
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                    .remoteAddress(remoteAddress)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {

                            if (ssl) {
                                SSLEngine sslEngine = SSLEngineFactory.createEngine();
                                ch.pipeline().addLast(new SslHandler(sslEngine));
                            }

                            ch.pipeline().addLast(
                                    new ReadTimeoutHandler(15),
                                    new ReconnectHandler(HdpClient.this, HdpClient.this.reconnectDelay),
                                    new ObjectDecoder(10485760, ClassResolvers.cacheDisabled(HdpClient.class.getClassLoader())),
                                    new ObjectEncoder());
                            ch.pipeline().addLast(new HeartBeatReqHandler(1),
                                    new HospitalLogonHandler(),
                                    new BusinessHandler());
                        }
                    });

            ChannelFuture f = b.connect().awaitUninterruptibly();
            channel = f.channel();
            if (f.isSuccess()) {
                log.info(String.format("HDP Client  successfully connect to the HDP Server on[%s:%d] in %s mode.",
                        remoteAddress.getHostName(), remoteAddress.getPort(), (ssl ? "SSL" : " Non-Secure")
                ));

            } else {
                log.error("HDP Client  can not connect to the HDP Server. Reason:", f.cause());
            }

        } catch (Exception e) {// 理论上应该没有该异常发生
            log.error("连接HdpServer发生意外：", e);
            log.info("After Sleeping for: " + reconnectDelay + "s , reconnect to HdpServer");

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

    private boolean checkLogon() {
        if (!isLogon) {
            log.error("前置机注册登录失败,请检查网络配置和使用的AccessToken是否正确!");
            return false;
        }
        return true;
    }

    private boolean _synSend(RequestPack request) {
        if (!checkLogon()) return false;
        FutureResult<Boolean> result = HdpClient.receiveAnswerManager
                .newFutureResult(request.getHdpSeqno());

        ChannelFuture f = channel.writeAndFlush(request);
        f.awaitUninterruptibly();
        if (f.isSuccess()) {
            Boolean b = result.get(waitAnswerTime);
            return b != null;
        } else {
            HdpClient.receiveAnswerManager.remove(request.getHdpSeqno());
            log.error("发送数据失败：", f.cause());
            return false;
        }

    }

    /**
     * 同步发送数据包
     * 被sendData替代 @see com.yuntai.hdp.client.HdpClient#sendData(RequestPack request, int timeout)
     *
     * @param request
     * @return
     * @throws InterruptedException
     */
    @Deprecated
    public boolean synSendData(RequestPack request) throws InterruptedException {
        fillData(request);
        request.setCallMode(0);
        return _synSend(request);
    }

    /**
     * 异步发送数据包
     * 被sendData替代 @see com.yuntai.hdp.client.HdpClient#sendData(RequestPack request, int timeout)
     *
     * @param request
     * @return
     */
    @Deprecated
    public ResultFuture<Boolean> asynSendData(final RequestPack request) {
        if (!checkLogon()) {
            return new ResultFuture<Boolean>() {
                @Override
                public Boolean get() {
                    return false;
                }
            };
        }
        fillData(request);
        request.setCallMode(0);
        final FutureResult<Boolean> result = HdpClient.receiveAnswerManager.newFutureResult(request.getHdpSeqno());
        final ChannelFuture f = channel.writeAndFlush(request);
        return new ResultFuture<Boolean>() {
            @Override
            public Boolean get() {
                f.awaitUninterruptibly();
                if (f.isSuccess()) {
                    Boolean b = result.get(waitAnswerTime);
                    return b != null;
                } else {
                    HdpClient.receiveAnswerManager
                            .remove(request.getHdpSeqno());
                    log.error("发送数据失败：", f.cause());
                    return false;
                }
            }

        };
    }

    /**
     * 同步获取请求结果
     *
     * @param request
     * @param timeout 单位秒
     * @return
     */
    public ResultPack sendData(RequestPack request, int timeout) {
        fillData(request);
        request.setCallMode(1);

        log.info(String.format("===>收到前置机同步对接云服务请求:%s", request.toKeyString()));
        log.debug(String.format("请求内容：%s", request));
        if (StringUtils.isEmpty(request.getCmd())) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_MISS_KEYFIELD.getKind());
            resultPack.setMsg(ResultKind.ERROR_MISS_KEYFIELD.getMessage("cmd"));
            log.error(String.format("===>请求失败,数据错误:%s", resultPack.toKeyString()));
            return resultPack;
        }

        if (StringUtils.isEmpty(request.getBody())) {
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_MISS_KEYFIELD.getKind());
            resultPack.setMsg(ResultKind.ERROR_MISS_KEYFIELD.getMessage("body"));
            log.error(String.format("===>请求失败,数据错误:%s", resultPack.toKeyString()));
            return resultPack;
        }

        FutureResult<ResultPack> futureResult = HdpClient.resultPackManager.newFutureResult(request.getHdpSeqno());

        if (!_synSend(request)) {
            HdpClient.resultPackManager.remove(request.getHdpSeqno());
            ResultPack resultPack = HdpHelper.newResult(request);
            resultPack.setKind(ResultKind.ERROR_NET.getKind());
            resultPack.setMsg(ResultKind.ERROR_NET.getMessage("") + "/连接HdpServer失败");
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

        log.debug(String.format("云服务对接返回内容：%s", resultPack));
        log.info(String.format("===>云服务对接完成,对接返回内容:%s", resultPack.toKeyString()));
        return resultPack;
    }

    /**
     *  异步获取请求结果
     * @param request
     * @param timeout
     * @return
     */
    public Future<ResultPack> sendDataAsync(final RequestPack request, final int timeout){
        FutureTask<ResultPack> task = new FutureTask<>(new Callable<ResultPack>() {
            @Override
            public ResultPack call() throws Exception {
                return sendData(request,timeout);
            }
        });
        this.businessExecutor.execute(task);
        return task;
    }

    /**
     * 仅并发测试使用
     *
     * @param data
     * @return
     */
    @Deprecated
    public ResultFuture<Boolean> asynSendData(String data) {
        final ChannelFuture f = channel.writeAndFlush(data);
        return new ResultFuture<Boolean>() {
            @Override
            public Boolean get() {
                f.awaitUninterruptibly();
                if (!f.isSuccess()) {
                    log.error("发送请求到HdpServer异常：", f.cause());
                }
                return f.isSuccess();
            }

        };
    }

    private void receiveRequestPack(RequestPack request) {
        if (receiver != null) {
            receiver.receive(request);
        } else {
            log.error("HdpClient 's receiver is null!");
        }
    }

    private void receiveResutlPack(ResultPack result) {
        if (receiver != null) {
            receiver.receive(result);
        } else {
            log.error("HdpClient 's receiver is null!");
        }
    }

    private ResultPack getHospitalReult(RequestPack request) {
        log.info(String.format("===>HdpClient开始请求对接:%s", request.toKeyString()));
        log.debug(String.format("请求内容：%s", request));
        if (synAccessHospital == null) {
            ResultPack result = HdpHelper.newResult(request);
            result.setKind(ResultKind.ERROR_NOEXISTS_ACCESSHOSP.getKind());
            result.setMsg(ResultKind.ERROR_NOEXISTS_ACCESSHOSP.getMessage(request.getHosId()));
            log.error("HdpClient 's synAccessHospital is null");
            return result;
        }
        try {
            long startTime = System.currentTimeMillis();
            ResultPack result = synAccessHospital.getHospitalResult(request);
            long timeLong = System.currentTimeMillis() - startTime;
            log.info(String.format("处理业务[%s]耗时[%f]秒", request.getCmd(), timeLong / 1000f));
            result.setSeqno(request.getSeqno());
            result.setHosId(request.getHosId());
            result.setCmd(request.getCmd());
            result.setHdpSeqno(request.getHdpSeqno());
            result.setCallMode(request.getCallMode());
            result.setHdpOther("businessTime=" + timeLong);
            if (result.getReturnTime() <= 0) {
                result.setReturnTime(System.currentTimeMillis());
            }

            log.debug(String.format("对接返回内容：%s", result));
            log.info(String.format("===>请求对接完成:%s", result.toKeyString()));
            return result;
        } catch (Exception ex) {
            log.error("医院对接服务发生异常", ex);
            ResultPack result = HdpHelper.newResult(request);
            result.setKind(ResultKind.ERROR_ACCESS_HOSPITAL.getKind());
            result.setMsg(ResultKind.ERROR_ACCESS_HOSPITAL.getMessage(request.getHosId()) + "\n 异常信息：" + ex.getMessage());
            return result;
        }
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
    private class HospitalLogonHandler extends ChannelInboundHandlerAdapter {
        private static final String CMD_REG_HOSPITAL = "$$Register Hospital$$";
        private static final String REG_HOSPITAL_OK = "$$Register Hospital OK$$";

        @Override
        public void channelActive(final ChannelHandlerContext ctx)
                throws Exception {
            if (!StringUtils.isEmpty(hosId)) {
                RequestPack data = new RequestPack();
                data.setClientId(clientId);
                data.setHosId(hosId);
                data.setCmd(CMD_REG_HOSPITAL);
                data.setBody(accessToken);
                data.setSendTime(System.currentTimeMillis());
                ctx.writeAndFlush(data);
            }else {
                log.error("没有配置hosId，不能进行通讯！");
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg)
                throws Exception {
            if (msg.equals(REG_HOSPITAL_OK)) {
                isLogon = true;
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
        private final long MIN_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(1);
        private final long intervalNanos;
        private volatile ScheduledFuture<?> heartBeat;
        private volatile int state; // 0 - none, 1 - Initialized, 2 - Destroyed;
        public static final String HEART_BEAT = "heartbeat";

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
                log.debug("收到服务端心跳");
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

                    log.debug("发送心跳");
                    ctx.writeAndFlush(HEART_BEAT);
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

            if (!msg.equals(HeartBeatReqHandler.HEART_BEAT))
                log.info(String.format("Receive [%s] from HdpServer", msg));

            if (msg instanceof RequestPack) {
                RequestPack request = (RequestPack) msg;
                ctx.writeAndFlush("ReceiveAnswer:" + request.getHdpSeqno());
            }

            if (msg instanceof String) {
                final String msg42 = (String) msg;
                if (msg42.startsWith("ReceiveAnswer:")) { // 接收到数据应答
                    String hdpSeqno = msg42.split(":")[1];
                    HdpClient.receiveAnswerManager.setResult(hdpSeqno, new Boolean(true));
                }
            } else {
                TimeOutTask task = null;
                if (msg instanceof RequestPack) {
                    task = new RequestPackTask(ctx, (RequestPack) msg, 30000);
                } else if (msg instanceof ResultPack) {
                    task = new ResultPackTask((ResultPack) msg, 30000);
                }
                if (task == null) {
                    log.error("Data format is error！ Data Content：" + msg);
                } else {
                    businessExecutor.execute(task);
                }
            }
            // super.channelRead(ctx, msg);
        }
    }

    private abstract class TimeOutTask<T> implements Runnable {
        private T data;
        private long startTime;
        private long timeoutMillis;

        public TimeOutTask(T data, long timeoutMillis) {
            this.data = data;
            startTime = System.currentTimeMillis();
            this.timeoutMillis = timeoutMillis;
        }

        @Override
        public void run() {
            if ((System.currentTimeMillis() - startTime) > timeoutMillis) {
                log.error(String.format("[%s][%s] wait timeout!", this.getClass().getSimpleName(),data));
                return;
            }
            deal(data);
        }

        abstract void deal(T data);
    }

    /**
     * 转化收到的HdpServer请求RequestPack（包括同步与异步）为可执行Task
     */
    private class RequestPackTask extends TimeOutTask<RequestPack> {
        private ChannelHandlerContext ctx;
        private long timeoutMillis;

        private RequestPackTask(ChannelHandlerContext ctx, RequestPack data,
                                long timeoutMillis) {
            super(data, timeoutMillis);
            this.ctx = ctx;
            this.timeoutMillis = timeoutMillis;
        }

        @Override
        void deal(RequestPack request) {
            if (request.getCallMode() == 1) { // 同步请求
                ResultPack result = HdpClient.this.getHospitalReult(request); // 请求医院数据
                TimeOutTask task = new LoopSendTask(ctx, result, timeoutMillis);
                businessExecutor.execute(task);

            } else { // 异步请求
                HdpClient.this.receiveRequestPack(request);
            }
        }
    }


    /**
     * 包装收到的HdpServer处理结果ResultPack为执行Task
     */
    private class ResultPackTask extends TimeOutTask<ResultPack> {
        private ResultPackTask(ResultPack data, long timeoutMillis) {
            super(data, timeoutMillis);
        }

        @Override
        void deal(ResultPack resultPack) {
            if (resultPack.getCallMode() == 1) { // 同步结果处理
                HdpClient.resultPackManager.setResult(resultPack.getHdpSeqno(), resultPack);
            } else {
                // 异步结果处理
                HdpClient.this.receiveResutlPack(resultPack);
            }
        }
    }


    /**
     * 循环发送数据
     */
    private class LoopSendTask extends TimeOutTask<Object> {
        private ChannelHandlerContext ctx;

        private LoopSendTask(ChannelHandlerContext ctx, Object data, long timeoutMillis) {
            super(data, timeoutMillis);
            this.ctx = ctx;
        }

        @Override
        void deal(Object data) {
            try {
                String hdpSeqno = BeanUtils.getProperty(data, "hdpSeqno");
                log.info(String.format("Sending Data[hdpSeqno=%s] to HdpServer.", hdpSeqno));
            }catch(Exception e){
                log.info("Sending Data to HdpServer.");
            }
            ChannelFuture f = ctx == null ? channel.writeAndFlush(data) : ctx.writeAndFlush(data);
            f.awaitUninterruptibly();
            if (!f.isSuccess()) {
                ctx = null;
                log.error("返回对接数据到HdpServer异常：", f.cause());
                log.info(String.format("进入重发队列,[%d]秒后将进行重发", reconnectDelay));
                businessExecutor.schedule(this, reconnectDelay, TimeUnit.SECONDS);
            }
        }
    }
}
