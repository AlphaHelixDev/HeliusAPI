package io.github.whoisalphahelix.helius.netty.client;

import com.google.gson.JsonElement;
import io.github.whoisalphahelix.helius.Helius;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Getter
@EqualsAndHashCode
@ToString
public class EchoClient {
	
	//Request name -> CallBack
	private static final Map<String, Consumer<JsonElement>> REQUESTS = new ConcurrentHashMap<>();
	private final EchoClientHandler ech = new EchoClientHandler();
	
	public EchoClient(String host, int port) {
		EventLoopGroup worker = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		
		b.group(worker)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) {
						socketChannel.pipeline()
								.addLast(new StringDecoder())
								.addLast(new StringEncoder())
								.addLast(ech);
					}
				});
		
		Helius.getLogger().info("Registered a new Client for " + host + " on " + port);
		
		b.connect(host, port);
	}
	
	public void request(String sentData, Consumer<JsonElement> nettyCallback) {
		this.getEch().requestData(sentData);
		EchoClient.getRequests().put(sentData, nettyCallback);
	}
	
	public static Map<String, Consumer<JsonElement>> getRequests() {
		return EchoClient.REQUESTS;
	}
}
