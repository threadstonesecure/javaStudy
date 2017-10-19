package dlt.study.google.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denglt on 2016/3/24.
 */
public class TestSubscribeReqProto {
	private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray();
	}

	private static SubscribeReqProto.SubscribeReq decode(byte[] body)
			throws InvalidProtocolBufferException {
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}

	private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq
				.newBuilder();
		builder.setSubReqID(1);
		builder.setUserName("denglt");
		builder.setProductName("dzy;dwx");
		List<String> address = new ArrayList<>();
		address.add("guangzhou");
		address.add("hangzhou");
		builder.addAllAddress(address);
		return builder.build();
	}

	public static void main(String[] args)
			throws InvalidProtocolBufferException {
		SubscribeReqProto.SubscribeReq req = createSubscribeReq();
		System.out.println("Before encode :" + req.toString());

		SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
		System.out.println("After decode:" + req.toString());
		System.out.println("Assert equal : -->" + req2.equals(req));

        SubscribeReqProto.SubscribeReq defaultInstance =  SubscribeReqProto.SubscribeReq.getDefaultInstance();
        System.out.println("defaultInstance :" +  defaultInstance);
        MessageLite type = defaultInstance.getDefaultInstanceForType();  //  == SubscribeReqProto.SubscribeReq.getDefaultInstance
        System.out.println("type:" + type);
        Object  r = type.getParserForType().parseFrom(req.toByteArray());
        System.out.println("type parseFrom:" + r);
         r=  type.newBuilderForType().mergeFrom(req.toByteArray()).build();
        System.out.println("type build:" + r);
	}
}
