package com.huzong.serlet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class Response {
    private final ChannelHandlerContext ctx;

    public Response(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    //传输文本
    public void write(String out) throws Exception {
        try {
            if (out == null || out.length() == 0) {
                return;
            }
            // 设置 http协议及请求头信息
            FullHttpResponse r = new DefaultFullHttpResponse(
                    // 设置http版本为1.1
                    HTTP_1_1,
                    // 设置响应状态码
                    OK,
                    // 将输出值写出 编码为UTF-8
                    Unpooled.wrappedBuffer(out.getBytes(StandardCharsets.UTF_8)));
            // 设置连接类型 为 JSON
            r.headers().set(CONTENT_TYPE, "text/json");
            // 设置请求头长度
            r.headers().set(CONTENT_LANGUAGE, r.content().readableBytes());
            // 设置超时时间为5000ms
            r.headers().set(EXPIRES, 5000);
            // 当前是否支持长连接
            if (HttpUtil.isKeepAlive(r)) {
                // 设置连接内容为长连接
                r.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            }
            ctx.writeAndFlush(r);
        } finally {
            ctx.close();
        }
    }

    public void write(File file) throws Exception {
        if (!file.isFile()) {
            System.out.println("不是文件！");
            return;
        }
        ByteBuf imgBuf = getImage(file);
        response(ctx,"image/jpeg",imgBuf,HttpResponseStatus.OK,file.length());

    }

    /**
     *
     * @param ctx 上下文
     * @param type 类型
     * @param byteBuf
     * @param status
     */
    // TODO: 2020/5/24 这里需要好好体会
    private void response(ChannelHandlerContext ctx, String type, ByteBuf byteBuf, HttpResponseStatus status,long len) {
        FullHttpResponse httpResponse = new DefaultFullHttpResponse
                (HttpVersion.HTTP_1_1,status,byteBuf);
        httpResponse.headers().
                set(HttpHeaderNames.CONTENT_TYPE,type).set(HttpHeaderNames.CONTENT_LENGTH,len);
        ctx.writeAndFlush(httpResponse).
                addListener(ChannelFutureListener.CLOSE);
    }
    private ByteBuf getImage(File file) {
        ByteBuf byteBuf = Unpooled.buffer();
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            int len;
            byte[] buf = new byte[1024];
            while ((len = fileInputStream.read(buf)) != -1){
                byteBuf.writeBytes(buf,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuf;
    }
}
