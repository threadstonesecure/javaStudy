package dlt.study.marshalling;

import org.jboss.marshalling.*;

import java.nio.ByteBuffer;

/**
 * Created by denglt on 2016/11/9.
 */
public class MarshallingDemo {
    public static void main(String[] args) throws Exception {

        MarshallerFactory marshallerFactory =  Marshalling.getProvidedMarshallerFactory("serial");
        MarshallingConfiguration marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(marshallingConfiguration);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteOutput byteOutput = Marshalling.createByteOutput(buffer);
        marshaller.start(byteOutput);
        marshaller.writeLong(System.currentTimeMillis());
        marshaller.writeObject("this is marshalling");
        marshaller.finish();
        marshaller.close();

        MarshallingConfiguration marshallingConfiguration2 = new MarshallingConfiguration();
        marshallingConfiguration2.setVersion(5);
        Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(marshallingConfiguration2);
        buffer.flip(); // 必须
        ByteInput byteInput =  Marshalling.createByteInput(buffer);
        unmarshaller.start(byteInput);
        long time = unmarshaller.readLong();
        Object o = unmarshaller.readObject();
        unmarshaller.finish();
        unmarshaller.close();
        System.out.println(o);
    }
}
