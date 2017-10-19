package dlt.study.voice;

import com.taobao.api.internal.tmc.Message;
import com.taobao.api.internal.tmc.MessageHandler;
import com.taobao.api.internal.tmc.MessageStatus;

/**
 * Created by denglt on 2016/3/8.
 */
public class AlibabaVoiceResultMonitor implements MessageHandler {

	@Override
	public void onMessage(Message message, MessageStatus messageStatus)
			throws Exception {
		try {
			String topic = message.getTopic();
			String content = message.getContent();
			System.out.println("消息回执主题:" + topic);
			System.out.println("消息回执内容:" + content);
		} catch (Exception ex) {
            ex.printStackTrace();
            messageStatus.fail();   // 消息处理失败回滚，服务端需要重发
		}
	}
}
