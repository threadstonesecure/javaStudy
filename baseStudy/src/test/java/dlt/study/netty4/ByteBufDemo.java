package dlt.study.netty4;

import io.netty.buffer.*;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.FixedRecvByteBufAllocator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.util.CharsetUtil;
import org.junit.Test;

import java.nio.ByteBuffer;

public class ByteBufDemo {


    @Test
    public void writeUTF() throws Exception {
        ByteBuf byteBuf = Unpooled.buffer(20);// Unpooled.buffer();
        System.out.println(byteBuf.refCnt());
        System.out.println(byteBuf.getClass());
        ByteBufOutputStream byteBufOutputStream = new ByteBufOutputStream(byteBuf);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 30; i++) {
            sb.append("a");
        }
        //sb.append("b");
        byteBufOutputStream.writeUTF(sb.toString()); // 该方法会在头部多写两个字节数据
        byteBufOutputStream.flush();
        byteBufOutputStream.close();
        System.out.println(byteBuf.array().length);
        System.out.println(new String(byteBuf.array()));
        ByteBuf byteBuf2 = Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
        System.out.println(byteBuf2.array().length);

        byte[] arrByte = sb.toString().getBytes(CharsetUtil.UTF_8);
        System.out.println(arrByte.length);
        byteBuf.release();
    }

    @Test
    public void string() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 30; i++) {
            sb.append("a");
        }

        ByteBuf byteBuf = Unpooled.copiedBuffer(sb.toString(), CharsetUtil.UTF_8);
        System.out.println(byteBuf.array().length);
        System.out.println(byteBuf.readableBytes());
        System.out.println(byteBuf.toString(CharsetUtil.UTF_8));


        byte[] arrByte = sb.toString().getBytes(CharsetUtil.UTF_8);
        System.out.println(arrByte.length);
    }

    @Test
    public void recvByteBufAllocator() throws Exception {
        RecvByteBufAllocator adaptiveRecvByteBufAllocator = new AdaptiveRecvByteBufAllocator(64, 1024, 65536);

        ByteBuf byteBuf = adaptiveRecvByteBufAllocator.newHandle().allocate(UnpooledByteBufAllocator.DEFAULT);
        System.out.println(byteBuf.getClass());
        System.out.println(byteBuf.writableBytes());
        int maxLength = byteBuf.writableBytes();
        for (int i = 0; i < maxLength; i++) {
            byteBuf.writeByte(1);
        }
        System.out.println(byteBuf.writableBytes());
        byteBuf.writeByte(1);
        System.out.println(byteBuf.writableBytes());
        System.out.println(byteBuf.readableBytes());
        System.out.println("===============================");

        RecvByteBufAllocator fixedRecvByteBufAllocator = new FixedRecvByteBufAllocator(1024);
        byteBuf = fixedRecvByteBufAllocator.newHandle().allocate(UnpooledByteBufAllocator.DEFAULT);
        System.out.println(byteBuf.getClass());
        System.out.println(byteBuf.writableBytes());
        maxLength = byteBuf.writableBytes();
        for (int i = 0; i < maxLength; i++) {
            byteBuf.writeByte(1);
        }
        System.out.println(byteBuf.writableBytes());
        byteBuf.writeByte(1); // 虽然其ByteBuf长度是固定，但当容量不足时，因Netty-ByteBuf本身的特性，还是会进行动态扩展的。
        System.out.println(byteBuf.writableBytes());
        System.out.println(byteBuf.readableBytes());
    }

    @Test
    public void nioByteBuffer(){
        RecvByteBufAllocator fixedRecvByteBufAllocator = new FixedRecvByteBufAllocator(1024);
        ByteBuf byteBuf = fixedRecvByteBufAllocator.newHandle().allocate(UnpooledByteBufAllocator.DEFAULT);
        System.out.println(byteBuf);
        System.out.println(byteBuf.writerIndex());
        System.out.println(byteBuf.writableBytes());
        ByteBuffer byteBuffer = byteBuf.nioBuffer(byteBuf.writerIndex(), byteBuf.writableBytes());
        System.out.println(byteBuffer);

        System.out.println(byteBuffer.position());
        System.out.println(byteBuffer.limit());
        System.out.println(byteBuffer.capacity());
        for (int i = 0; i < byteBuffer.limit(); i++) {
            byteBuffer.put((byte)1);
        }
        System.out.println(byteBuffer.position());

        byteBuffer.put((byte)1); // java.nio.BufferOverflowException

    }

    @Test
    public void refCnt() {
        ByteBuf content = Unpooled.directBuffer(255).writeZero(2550);
        System.out.println(content);
        System.out.println(content.refCnt()); // 1  // 引用计数 ByteBuf是引用计数对象
        ByteBuf content2 = content.duplicate();
        content2.readByte();
        System.out.println(content.readerIndex());
        System.out.println(content2.readerIndex());

        content2.retain();   //增加引用
        System.out.println(content2.readableBytes());
        System.out.println(content2.refCnt()); //2
        System.out.println(content.refCnt()); //2
        content.release();
        content.release();
        content.release();
    }
}
