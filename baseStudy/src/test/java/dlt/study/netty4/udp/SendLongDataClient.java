package dlt.study.netty4.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * Created by denglt on 2017/5/27.
 */
public class SendLongDataClient {

    static final int PORT = Integer.parseInt(System.getProperty("port", "7686"));

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    //.option(ChannelOption.RCVBUF_ALLOCATOR,new FixedRecvByteBufAllocator(1024*2))  //DefaultDatagramChannelConfig 中 DEFAULT_RCVBUF_ALLOCATOR = new FixedRecvByteBufAllocator(2048);
                    //.option(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT)
                    .handler(new QuoteOfTheMomentClientHandler());

            final Channel ch = b.bind(0).sync().channel();
            StringBuffer sb = new StringBuffer();
            int maxLength = 9216 ;// 最大发送数据：9216 ？？？； 内网建议：1472 ；Internet建议：576 ;
            for (int i = 0; i < maxLength; i++) {
                if ((i == 0) || (i == maxLength - 1)) {
                    sb.append('b');
                } else
                    sb.append('a');
            }
            System.out.println(sb.length());
            ByteBuf byteBuf = Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
            ChannelConfig channelConfig =  ch.config();

            System.out.println(channelConfig.getClass()); //io.netty.channel.socket.nio.NioDatagramChannelConfig
            ByteBufAllocator byteBufAllocator = channelConfig.getOption(ChannelOption.ALLOCATOR);

            RecvByteBufAllocator recvByteBufAllocator = channelConfig.getOption(ChannelOption.RCVBUF_ALLOCATOR);
            System.out.println(byteBufAllocator.getClass());
            System.out.println(recvByteBufAllocator.getClass()); //io.netty.buffer.UnpooledByteBufAllocator
            ByteBuf byteBuf1 = recvByteBufAllocator.newHandle().allocate(byteBufAllocator);
            System.out.println(byteBuf1.writableBytes());
            ch.writeAndFlush(new DatagramPacket(
                    byteBuf,
                    new InetSocketAddress("127.0.0.1", PORT)));
            if (!ch.closeFuture().await(500000)) {
                System.err.println("QOTM request timed out.");
            }
        } finally {
            group.shutdownGracefully();
        }
    }
}
