package watchhdp;

import com.yuntai.hdp.access.RequestPack;
import com.yuntai.hdp.access.ResultPack;
import dlt.utils.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by denglt on 2015/12/25.
 */
public class WatchHospital {
	private static Log log = LogFactory.getLog(WatchHospital.class);
	private static HdpWatchClient hdpWatchClient;
	public WatchHospital(String ip, int port ){
		init(ip,port,"_","98608d4679a28b719815ee03f7c404e0");
	}

	public WatchHospital(String ip, int port, String hosId, String accessToken){
		init(ip, port, "_" + hosId+"_",accessToken);
	}

	private void init (String ip, int port, String hosId, String accessToken) {
		 hdpWatchClient = new HdpWatchClient()
				.remoteAddress(ip, port).hosId(hosId).accessToken(accessToken)
				.reconnectDelay(10).ssl(true).dataHandler(new BadDataHandler()).businessThreadPoolSize(2).connect();

	}

	public static void main(String[] args) throws Exception {
        //120.55.66.16
        //120.26.224.231
        //121.40.182.17  -- 正式
		//WatchHospital watchHospital = new WatchHospital("121.40.182.17", 8088);
		WatchHospital watchHospital = new WatchHospital("121.40.182.17", 8088,"226","eece77d113a908a1bed958b3bbcc0c68");
		Thread.sleep(6000);
		RequestPack requestPack = new RequestPack();
		requestPack.setCmd("$$HDP_COMMAND$$");
		requestPack.setBody("{ hdpcmd : \"allInfo\"}");
		ResultPack resultPack = hdpWatchClient.sendData(requestPack,30);
		System.out.println(resultPack.getBody());
		System.in.read();
	}
}
