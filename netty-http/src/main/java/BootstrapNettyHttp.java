

import com.teddy.http.handler.HttpServletHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.codec.http.HttpServerKeepAliveHandler;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import io.netty.handler.codec.http.cors.CorsHandler;

public class BootstrapNettyHttp {

	public static void main(String[] args) {
		int port = 19100;
		ServerBootstrap boot = new ServerBootstrap();
		EventLoopGroup listenGroup = new NioEventLoopGroup();
		EventLoopGroup processGroup = new NioEventLoopGroup();
		try {
			ChannelFuture future = boot.group(listenGroup, processGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
						  .addLast(new HttpServerCodec(1 * 1024 * 1024 * 1024, 1 * 1024 * 1024 * 1024, 1 * 1024 * 1024))
						  .addLast(new HttpObjectAggregator(1 * 1024 * 1024 * 1024))
						  .addLast(new HttpServerExpectContinueHandler())
						  .addLast(new CorsHandler(CorsConfigBuilder.forAnyOrigin().build()))
						  .addLast(new HttpServerKeepAliveHandler())
						  .addLast(new HttpServletHandler());
					}
					
				})
				.bind(port)
				.sync();
			future.channel()
				  .closeFuture()
				  .sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				listenGroup.shutdownGracefully().sync();
			} catch (Exception e) {
			}
			try {
				processGroup.shutdownGracefully().sync();
			} catch (Exception e) {
			}
		}
			
	}
}
