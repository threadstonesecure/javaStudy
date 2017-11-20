package dlt.utils.dubbo.cache;

import dlt.utils.ByteUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.marshalling.*;
import org.springframework.core.NestedIOException;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.Deserializer;
import org.springframework.core.serializer.Serializer;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.*;

/**
 * Created by denglt on 2016/11/9.
 */
public class TTLRedisSerializer implements RedisSerializer<Object> {

    private static final Log log = LogFactory.getLog(TTLRedisSerializer.class);
    private static final MarshallerFactory marshallerFactory;
    private static final MarshallingConfiguration marshallingConfiguration;

    public static final Object EXPIRED_OBJ = new Object();

    static {
        marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        if (marshallerFactory == null) {
            log.error(" no matching factory was found ");
        }
        marshallingConfiguration = new MarshallingConfiguration();
        marshallingConfiguration.setVersion(5);

    }

    private int expiry = 0;  // 单位秒，0 永远不过去

    public TTLRedisSerializer(int expiry) {
        this.expiry = expiry;
    }


    private Converter<Object, byte[]> serializer = new SerializingConverter(new MarshallingSerializationDelegate());
    private Converter<byte[], Object> deserializer = new DeserializingConverter(new MarshallingSerializationDelegate());

    @Override
    public Object deserialize(byte[] bytes) {
        if (isEmpty(bytes)) {
            return null;
        }
        try {
            return deserializer.convert(bytes);
        } catch (Exception ex) {
            throw new SerializationException("Cannot deserialize", ex);
        }
    }

    @Override
    public byte[] serialize(Object object) {
        if (object == null) {
            return null;
        }
        try {
            return serializer.convert(object);
        } catch (Exception ex) {
            throw new SerializationException("Cannot serialize", ex);
        }
    }

    private boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

    private class MarshallingSerializationDelegate implements Serializer<Object>, Deserializer<Object> {
        @Override
        public void serialize(Object o, OutputStream outputStream) throws IOException {

            Marshaller marshaller = marshallerFactory.createMarshaller(marshallingConfiguration);
            ByteOutput byteOutput = Marshalling.createByteOutput(outputStream);
            try {
                marshaller.start(byteOutput);
                marshaller.writeInt(0); // 第一个字节作为数据格式，为以后兼容用
                if (expiry == 0)
                    marshaller.writeLong(0);
                else
                    marshaller.writeLong(System.currentTimeMillis() + expiry * 1000);
                marshaller.writeObject(o);
            } finally {
                marshaller.finish();
                marshaller.close();
            }

        }

        @Override
        public Object deserialize(InputStream inputStream) throws IOException {
            Unmarshaller unmarshaller = marshallerFactory.createUnmarshaller(marshallingConfiguration);
            try {
                ByteInput byteInput = Marshalling.createByteInput(inputStream);
                unmarshaller.start(byteInput);
                unmarshaller.readInt();
                long expiryTime = unmarshaller.readLong();
                if (expiryTime != 0 && System.currentTimeMillis() > expiryTime) {
                    return EXPIRED_OBJ;
                }
                Object o = unmarshaller.readObject();
                return o;
            } catch (ClassNotFoundException ex) {
                throw new NestedIOException("Failed to deserialize object type", ex);
            } finally {
                unmarshaller.finish();
                unmarshaller.close();
            }

        }
    }


    public static void main2(String[] args) throws Exception {
        TTLRedisSerializer serializer = new TTLRedisSerializer(0);

        MarshallingSerializationDelegate marshallingSerializationDelegate = serializer.new MarshallingSerializationDelegate();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
        marshallingSerializationDelegate.serialize(NullValue.INSTANCE, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Object o = marshallingSerializationDelegate.deserialize(inputStream);
        System.out.println(o);
        System.out.println(o.getClass());
        System.out.println(NullValue.INSTANCE == o);
        System.out.println(NullValue.INSTANCE.getClass() == o.getClass());
        System.out.println(NullValue.INSTANCE.getClass().equals(o.getClass()));
        System.out.println(NullValue.INSTANCE.equals(o));
        System.out.println(((NullValue) o).serialVersionUID);
    }

    public static void main(String[] args) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(256);
        Marshaller marshaller = marshallerFactory.createMarshaller(marshallingConfiguration);
        ByteOutput byteOutput = Marshalling.createByteOutput(outputStream);
        try {
            marshaller.start(byteOutput);
            marshaller.writeInt(1); // 第一个字节作为数据格式，为以后兼容用
            marshaller.writeLong(System.currentTimeMillis() + 5 * 1000);
        } finally {
            marshaller.finish();
            marshaller.close();
        }

        char[] chars = ByteUtils.encodeHex(outputStream.toByteArray());
        for (int i = 0; i < chars.length; i++) {
            System.out.print("\\x" + chars[i] + chars[++i]);
        }
        //\xac\xed\x00\x05\x00\x00\x00\x01\x00\x00\x01\x5f\xd8\x04\xff\x1f
        //\xac\xed\x00\x05\x00\x00\x00\x00\x00\x00\x01Xt\xda\xbf\x11t\x00
    }
}


