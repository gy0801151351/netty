package com.teddy.http.handler;

import java.text.SimpleDateFormat;

import com.alibaba.fastjson.JSONObject;
import com.rzd.framework.kernel.KernelWeb.Utility;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpServletHandler extends SimpleChannelInboundHandler<FullHttpMessage> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpMessage msg) throws Exception {
		System.out.println("---------------------------------------------------------------");
		System.out.println("channelRead0: " + msg.toString());
		HttpRequest req = (HttpRequest) msg;
		String uri = req.uri();
		DefaultFullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		res.headers().add(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
		JSONObject obj = new JSONObject(true);
		obj.put("uri", uri);
		obj.put("access_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Utility.getCurrentTimestamp()));
		res.content().writeCharSequence(obj.toJSONString(), CharsetUtil.UTF_8);
		ctx.write(res);
	}


	
}
