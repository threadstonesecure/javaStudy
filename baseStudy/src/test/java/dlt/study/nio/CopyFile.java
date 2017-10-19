package dlt.study.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CopyFile {

    @Test
    public void copyFile() throws Exception {
        String infile = "C:\\copy.sql";
        String outfile = "C:\\copy.txt";
        // 获取源文件和目标文件的输入输出流
        FileInputStream fin = new FileInputStream(infile);
        FileOutputStream fout = new FileOutputStream(outfile);
        // 获取输入输出通道
        FileChannel fcin = fin.getChannel();
        FileChannel fcout = fout.getChannel();
        // 创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        while (true) {
            // clear方法重设缓冲区，使它可以接受读入的数据
            buffer.clear();
            // 从输入通道中将数据读到缓冲区
            int r = fcin.read(buffer);
            // read方法返回读取的字节数，可能为零，如果该通道已到达流的末尾，则返回-1
            if (r == -1) {
                break;
            }
            // flip方法让缓冲区可以将新读入的数据写入另一个通道
            buffer.flip();
            // 从输出通道中将数据写入缓冲区
            fcout.write(buffer);
            buffer.compact();
        }
        fcin.close();
        fcout.close();
    }

    @Test
    public void transferFile() throws Exception {
        String infile = "C:\\copy.sql";
        String outfile = "C:\\copy.txt";
        // 获取源文件和目标文件的输入输出流
        try (FileChannel fcin = FileChannel.open(Paths.get(infile), StandardOpenOption.READ);
             FileChannel fcout = FileChannel.open(Paths.get(outfile), StandardOpenOption.CREATE, StandardOpenOption.WRITE);) {
            fcout.transferFrom(fcin, 0, fcin.size());
            //fcin.transferTo(0, fcin.size(), fcout);
        }
    }
}