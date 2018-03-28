package com.teddy.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

	protected final int port;

	public EchoServer(int port) {
		super();
		this.port = port;
	}
	
	public void start() throws Exception {
		ChannelHandler[] handlers = new ChannelHandler[] {
				new EchoServerOutboundHandler1(),
				new EchoServerInboundHandler1(),
				new EchoServerOutboundHandler2(),
				new EchoServerInboundHandler2(),
				new EchoServerOutboundHandler3(),
				new EchoServerInboundHandler3(),
		};
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)
			 .channel(NioServerSocketChannel.class)
			 .localAddress(this.port)
			 .childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(handlers);
				}
			});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		int port = 29000;
		new EchoServer(port).start();
		
	}
}
