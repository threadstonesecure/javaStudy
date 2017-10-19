package dlt.study.voice;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.internal.tmc.TmcClient;
import com.taobao.api.request.AlibabaAliqinFcTtsNumSinglecallRequest;
import com.taobao.api.response.AlibabaAliqinFcTtsNumSinglecallResponse;

/**
 * Created by denglt on 2016/3/7.
 */
public class AlibabaVoiceService {

	public static final String  url = "https://eco.taobao.com/router/rest";
    public static final String receiveUrl="ws://mc.api.taobao.com";
    public static final String appKey = "23263097";
    public static final String appSecret = "1fc4b3acd1d4fef099f4dda511150011";
	private String showNum = "4008899299";
	private String ttsCode = "TTS_4456212";

	public void sendVoiceCode(String verifyCode, String callNum)
			throws Exception {
		StringBuffer ttsParm = new StringBuffer();
		ttsParm.append("{\"code\":");
		ttsParm.append("\"");
		ttsParm.append(transform(verifyCode));
		ttsParm.append("\"");
		ttsParm.append("}");
		TaobaoClient client = new DefaultTaobaoClient(url, appKey, appSecret);
		AlibabaAliqinFcTtsNumSinglecallRequest request = new AlibabaAliqinFcTtsNumSinglecallRequest();

		request.setExtend(callNum + "@" + 1);
		request.setTtsParam(ttsParm.toString());
		request.setCalledNum(callNum);
		request.setCalledShowNum(showNum);
		request.setTtsCode(ttsCode);
		AlibabaAliqinFcTtsNumSinglecallResponse response ;
		response = client.execute(request);
        String errorCode = response.getErrorCode();
        System.out.println("ErrorCode="+errorCode);
		System.out.println(response.getBody());// //{"alibaba_aliqin_fc_tts_num_singlecall_response":{"result":{"err_code":"0","model":"100809653879^100150812679","success":true},"request_id":"r4lxw0t5561y"}}

	}

	public static void main(String[] args) throws Exception {
        TmcClient client = new TmcClient(AlibabaVoiceService.appKey, AlibabaVoiceService.appSecret);
        client.setMessageHandler(new AlibabaVoiceResultMonitor());
        client.connect(AlibabaVoiceService.receiveUrl, true);
		AlibabaVoiceService voiceService = new AlibabaVoiceService();
		voiceService.sendVoiceCode("123456", "13825101877");
        //voiceService.sendVoiceCode("小安子，大宝剑", "18621185413");
        System.in.read();

	}

    /** 输出结果
     2016-03-08 17:00:51,043 WARN  [main] com.taobao.top.link.logging.Log4jLogger.warn(Log4jLogger.java:80) - 23263097~default connected to tmc server: ws://mc.api.taobao.com
     2016-03-08 17:00:51,043 INFO  [WebSocket-Executor-1] com.taobao.top.link.logging.Log4jLogger.info(Log4jLogger.java:64) - sucessfully connect to endpoint#23263097~default, and got token#dc148c0c-0b8d-437a-85fc-398dd91f749f
     ErrorCode=null
     {"alibaba_aliqin_fc_tts_num_singlecall_response":{"result":{"err_code":"0","model":"100820395160^100151992745","success":true},"request_id":"13ozpnqfyxqxd"}}
     消息回执主题:alibaba_aliqin_FcCallCdr
     消息回执内容:{"status_code":"200000","duration":"12","biz_id":"100820395160^100151992745","end_time":"2016-03-08 17:01:01","status_msg":"","extend":"13825101877@1","start_time":"2016-03-08 17:00:49"}
     */

	/**
	 * 将验证码数字转化为中文
	 */
	private static String transform(String code) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < code.length(); i++) {
			char c = code.charAt(i);
			switch (c) {
			case '1':
				c = '一';
				break;
			case '2':
				c = '二';
				break;
			case '3':
				c = '三';
				break;
			case '4':
				c = '四';
				break;
			case '5':
				c = '五';
				break;
			case '6':
				c = '六';
				break;
			case '7':
				c = '七';
				break;
			case '8':
				c = '八';
				break;
			case '9':
				c = '九';
				break;
			case '0':
				c = '零';
				break;
			}
			buffer.append(c);
		}
		return buffer.toString();

	}

}
