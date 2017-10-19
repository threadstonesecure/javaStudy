package dlt.study.google.protobuf;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import io.netty.buffer.ByteBufOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by denglt on 2016/3/25.
 */
public class TestBase128Varint {

    public static void main(String[] args) throws IOException{
        int i = 300;
        ByteArrayOutputStream boutput = new ByteArrayOutputStream();
        DataOutputStream doutput = new DataOutputStream(boutput);
        doutput.writeInt(i);
        byte[] buf = boutput.toByteArray();
        System.out.println("byte length:" + buf.length);
        boutput.close();
        doutput.close();


        boutput = new ByteArrayOutputStream();
        CodedOutputStream headerOut =
                CodedOutputStream.newInstance(boutput);
        headerOut.writeRawVarint32(i);
        headerOut.flush();
        buf = boutput.toByteArray();  //0xAC02
        System.out.println("byte length:" + buf.length);

        i = CodedInputStream.newInstance(buf).readRawVarint32();
        System.out.println(i);
    }
}
