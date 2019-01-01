package io.github.alphahelixdev.helius.netty;

import com.google.gson.JsonElement;
import io.github.alphahelixdev.helius.Helius;
import io.github.alphahelixdev.helius.netty.client.EchoClient;
import io.github.alphahelixdev.helius.netty.server.EchoServer;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Consumer;

public class ServerConnector {
	
	private EchoServer server;
	private EchoClient client;
	
	public ServerConnector(int ownPort, String host, int port) {
		if(ownPort == port) {
			Helius.getLogger().warning("Can't connect to own server!");
			return;
		}
		
		this.setOwnServer(new EchoServer(ownPort));
		if(!host.isEmpty())
			this.setClient(new EchoClient(host, port));
		
		ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
		
		executor.execute(() -> {
			try {
				this.getOwnServer().start();
			} catch(Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	public EchoServer getOwnServer() {
		return this.server;
	}
	
	public ServerConnector setOwnServer(EchoServer server) {
		this.server = server;
		return this;
	}
	
	public void makeRequest(String request, Consumer<JsonElement> callback) {
		this.getClient().request(request, callback);
	}
	
	public EchoClient getClient() {
		return this.client;
	}
	
	public void addRequestProcessor(String request, RequestProcessor reprocessor) {
		EchoServer.addRequestProcessor(request, reprocessor);
	}
	
	public ServerConnector setClient(EchoClient client) {
		this.client = client;
		return this;
	}
}
