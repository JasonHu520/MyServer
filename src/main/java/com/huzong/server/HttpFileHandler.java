package com.huzong.server;



/**
 * 接收HTTP文件上传的处理器
 */

import com.huzong.serlet.MyServlet;
import com.huzong.serlet.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;

import java.util.concurrent.atomic.AtomicInteger;

public class HttpFileHandler extends SimpleChannelInboundHandler<HttpObject>{
    private FullHttpRequest request;
    private AtomicInteger connectNum;

    public HttpFileHandler(AtomicInteger connectNum){
        this.connectNum = connectNum;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        // TODO Auto-generated method stub

        if (msg instanceof FullHttpRequest) {

            this.request = (FullHttpRequest) msg;
            Response response = new Response(ctx);
            HttpMethod method = this.request.method();
            if (HttpMethod.GET.equals(method)) {
                System.out.println("Get方法");
                MyServlet.getInstance().doGet(this.request,response);
            }
            if (HttpMethod.POST.equals(method)){
                System.out.println("Post方法");
                MyServlet.getInstance().doPost(this.request,response);

            }
        }else if (msg instanceof HttpContent) {
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {

        super.channelRegistered(ctx);
        connectNum.getAndIncrement();
        System.err.println("current connected :" + connectNum.get());
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        connectNum.getAndDecrement();
        System.err.println("current connected :" + connectNum.get());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            System.out.println("产生了一些错误");
        }
    }
}
