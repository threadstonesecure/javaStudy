package dlt.study.netty4.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by denglt on 2016/4/14.
 * <p>
 * UDP  可以理解为仅仅对端口的监听，不分服务端和客户端
 * java      75408 denglt  252u     IPv6 0x30d93aa0bc3e6b11        0t0      UDP *:50064
 * java      75477 denglt  252u     IPv6 0x30d93aa0bc3e3471        0t0      UDP *:7686
 */
/**
 UDP  可以理解为仅仅对端口的监听，不分服务端和客户端
 java      75408 denglt  252u     IPv6 0x30d93aa0bc3e6b11        0t0      UDP *:50064
 java      75477 denglt  252u     IPv6 0x30d93aa0bc3e3471        0t0      UDP *:7686
 */

/**
 从TCP与UDP的区别讲起：
 网络数据经过路由器,如果数据很小,没有超过路由器的封包大小,就会直接直接经过路由器到达下一个路由器,一层一层最终到达目的地
 如果数据很大,这里指一个发送,超过了路由器的封包大小,那么路由器就会把这个数据包进行拆分,比如拆分成A B C三个包,这三个包都没有超过路由器的封包大小,到达下一个路由器的时候,TCP与UDP的区别就来了：
   1、TCP收到A的时候,会resp通知源路由器,A到达,B C包依然如此,如果由于网络的各种原因，目的路由收到了A C,B没有收到，TCP会要求源路由把B包重新发一次，直到ABC包目的路由都接受到了，那么目的路由把ABC包重新组成起始包，继续往下一个路由发送，这就是TCP安全连接的由来，只要发送，我就能保证目的一定能收到（网络断开能检测到）
   2、UDP则不是这样，如果ABC包拆分之后，目的路由只收到AC，经过检测，B没有被收到，那么此包就会被当作不完整，直接被丢弃。由于UDP没有resp的通知过程，所以，UDP的传输效率要高一些，当然安全性也低一些
 由上面的这些可以得出结论：UDP是绝对不会被粘包，因为路由器收到的只会是完整数据才会继续下发，什么粘包处理完全没有必要
 */
public class QuoteOfTheMomentServer {
    private static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    //.option(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(1024 * 20))
                    .option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    // 会限制UDP接收到的数据大小，因为udp没有粘包问题， NioDatagramChannel.doReadMessages中把ByteBuf转换为了ByteBuffer(它不能自行扩展)。
                    // 而tcp／ip可以不断读取数据，没有大小限制，见 AbstractNioByteChannel中的NioByteUnsafe.read() 和 doReadBytes()方法
                    .handler(new QuoteOfTheMomentServerHandler());

            b.bind(PORT).sync().channel().closeFuture().await();
        } finally {
            group.shutdownGracefully();
        }
    }
}

class QuoteOfTheMomentServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    private static final Random random = new Random();

    // Quotes from Mohandas K. Gandhi:
    private static final String[] quotes = {
            "Where there is love there is life.",
            "First they ignore you, then they laugh at you, then they fight you, then you win.",
            "Be the change you want to see in the world.",
            "The weak can never forgive. Forgiveness is the attribute of the strong.",
    };

    private static String nextQuote() {
        int quoteId;
/*        synchronized (random) {
            quoteId = random.nextInt(quotes.length);
        }*/
        quoteId = ThreadLocalRandom.current().nextInt(quotes.length);
        return quotes[quoteId];
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel is active");
        super.channelActive(ctx);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        System.err.println("Receive:" + packet + "from " + packet.sender());
        System.out.println(ctx.channel());
        System.out.println(ctx.channel().pipeline());
        if (packet.content().hasArray()) {
            System.out.println("消息长度：" + packet.content().array().length);
        } else {
            System.out.println("消息长度：" + packet.content().readableBytes());
        }

        System.out.println("消息内容：" + packet.content().toString(CharsetUtil.UTF_8));


        if ("QOTM?".equals(packet.content().toString(CharsetUtil.UTF_8))) {
            ctx.write(new DatagramPacket(
                    Unpooled.copiedBuffer("QOTM: " + nextQuote(), CharsetUtil.UTF_8), packet.sender()));
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        // We don't close the channel because we can keep serving requests.
    }

    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx)
            throws Exception {
        System.out.println("channel is closed!");

        super.channelUnregistered(ctx);
    }
}