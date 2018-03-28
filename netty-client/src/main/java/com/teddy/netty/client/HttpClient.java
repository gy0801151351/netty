package com.teddy.netty.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpClient {

	private final String host;
	private final int port;

	public HttpClient(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}
	
	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			 .channel(NioSocketChannel.class)
			 .remoteAddress(new InetSocketAddress(host, port))
			 .handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
					  .addLast(new HttpRequestDecoder())
					  .addLast(new HttpRequestEncoder())
					  .addLast(new HttpObjectAggregator(1 * 1024 * 1024 * 1024))
					  .addLast(new SimpleChannelInboundHandler<FullHttpMessage>() {

						@Override
						protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
							System.out.println(msg.content().toString());
						}

						@Override
						public void channelActive(ChannelHandlerContext ctx) throws Exception {
							DefaultFullHttpRequest req = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/hello");
							req.content().writeCharSequence("Hello World!", CharsetUtil.UTF_8);
							ctx.write(req);
							ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
						}
						
						
						
					});
				}
			});
			ChannelFuture f = b.connect().sync();
			f.channel().write("AAA");
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		String host = "127.0.0.1";
		int port = 19100;
		new HttpClient(host, port).start();
	}

}
