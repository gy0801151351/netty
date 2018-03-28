package com.teddy.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	private String toString(ByteBuf msg) {
		StringBuilder sb = new StringBuilder(msg.capacity());
		int readerIndex = msg.readerIndex();
		while (msg.isReadable()) {
			sb.append(msg.readCharSequence(1, CharsetUtil.UTF_8));
		}
		msg.readerIndex(readerIndex);
		return sb.toString();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		System.out.println("Client received: " + toString(msg));
//		char c = 'M';
//		ByteBuf msg2 = msg.duplicate();
////		char c1 = (char) msg.readByte();
//		msg2.writeChar(c);
////		System.out.println(c1);
//		System.out.println("Client received: " + toString(msg));
//		msg2.writerIndex(1);
//		msg2.writeChar(c);
//		System.out.println("Client received: " + toString(msg));
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
