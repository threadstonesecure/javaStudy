package com.yuntai.hdp.client;

import java.net.Socket;
import java.util.UUID;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;

/**
 * 
 * @author denglt
 *
 */
public class HdpHelper {

	public static boolean isPortInUse(String host, int port) {
		boolean result = false;

		try {
			(new Socket(host, port)).close();
			result = true;
		} catch (Exception e) {
			// Could not connect.
			// e.printStackTrace();
		}
		return result;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}


    public static ResultPack newResult(RequestPack request){
        ResultPack resultPack = new ResultPack();
        resultPack.setSeqno(request.getSeqno());
        resultPack.setHosId(request.getHosId());
        resultPack.setCmd(request.getCmd());
        resultPack.setHdpSeqno(request.getHdpSeqno());
        resultPack.setCallMode(request.getCallMode());
        resultPack.setReturnTime(System.currentTimeMillis());
        return resultPack;
    }
}
