package io.github.alphahelixdev.helius.netty.client;

import com.google.gson.JsonParser;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.Objects;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	
	private ChannelHandlerContext ctx;
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.setCtx(ctx);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object s) {
		String[] sentDataAndJson = ((String) s).split("-::=::-");
		String data = sentDataAndJson[0];
		String json = sentDataAndJson[1];
		
		if(EchoClient.getRequests().containsKey(data))
			EchoClient.getRequests().get(data).accept(new JsonParser().parse(json));
	}
	
	public void requestData(String data) {
		if(this.getCtx() != null) {
			try {
				ChannelFuture f = this.getCtx().writeAndFlush(Unpooled.copiedBuffer(data, CharsetUtil.UTF_8)).sync();
				
				if(!f.isSuccess())
					try {
						throw f.cause();
					} catch(Throwable throwable) {
						throwable.printStackTrace();
					}
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public ChannelHandlerContext getCtx() {
		return this.ctx;
	}
	
	public EchoClientHandler setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
		return this;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.getCtx());
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		EchoClientHandler that = (EchoClientHandler) o;
		return Objects.equals(this.getCtx(), that.getCtx());
	}
	
	@Override
	public String toString() {
		return "EchoClientHandler{" +
				"                            ctx=" + this.ctx +
				'}';
	}
}
