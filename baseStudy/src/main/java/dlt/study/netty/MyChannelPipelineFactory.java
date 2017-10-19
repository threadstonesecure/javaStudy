package dlt.study.netty;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

class MyChannelPipelineFactory implements ChannelPipelineFactory {

	private List<ChannelHandler> channelHandlers = new ArrayList<ChannelHandler>();

	public MyChannelPipelineFactory(ChannelHandler... chs) {

		for (int i = 0; i < chs.length; i++) {
			channelHandlers.add(chs[i]);
		}
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {

		return Channels.pipeline(channelHandlers
				.toArray(new ChannelHandler[0]));

	
	}
}