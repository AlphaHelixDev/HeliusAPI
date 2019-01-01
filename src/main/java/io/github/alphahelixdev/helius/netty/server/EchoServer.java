package io.github.alphahelixdev.helius.netty.server;

import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.netty.RequestProcessor;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EchoServer {
	
	private static final Map<String, RequestProcessor> REPROCESSOR_MAP = new HashMap<>();
	private final int port;
	private final EventLoopGroup workerGroup = new NioEventLoopGroup();
	private ChannelFuture f;
	
	public EchoServer(int port) {
		this.port = port;
	}
	
	public static void addRequestProcessor(String data, RequestProcessor requestProcessor) {
		EchoServer.getReprocessorMap().put(data, requestProcessor);
	}
	
	public static Map<String, RequestProcessor> getReprocessorMap() {
		return EchoServer.REPROCESSOR_MAP;
	}
	
	public static RequestProcessor process(String data) {
		return EchoServer.getReprocessorMap().get(data);
	}
	
	public void start() {
		ServerBootstrap b = new ServerBootstrap();
		
		b.group(this.getWorkerGroup())
				.channel(NioServerSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel socketChannel) {
						Helius.getLogger().info("New client connected! Onto (" + socketChannel.localAddress() + ")");
						
						socketChannel.pipeline().addLast(new StringEncoder()).addLast(new StringEncoder()).addLast(new EchoServerHandler());
					}
				});
		
		this.setF(b.bind(port));
	}
	
	public EventLoopGroup getWorkerGroup() {
		return this.workerGroup;
	}
	
	public void stop() {
		try {
			this.getF().channel().close().sync();
			this.getWorkerGroup().shutdownGracefully();
		} catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public ChannelFuture getF() {
		return this.f;
	}
	
	public EchoServer setF(ChannelFuture f) {
		this.f = f;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getPort(), this.getWorkerGroup(), this.getF());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		EchoServer that = (EchoServer) o;
		return this.getPort() == that.getPort() &&
				Objects.equals(this.getWorkerGroup(), that.getWorkerGroup()) &&
				Objects.equals(this.getF(), that.getF());
	}
	
	public int getPort() {
		return this.port;
	}
	
	@Override
	public String toString() {
		return "EchoServer{" +
				"                            port=" + this.port +
				",                             workerGroup=" + this.workerGroup +
				",                             f=" + this.f +
				'}';
	}
}
