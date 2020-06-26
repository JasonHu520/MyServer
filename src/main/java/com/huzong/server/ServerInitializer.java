package com.huzong.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.stream.ChunkedFile;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final AtomicInteger connectNum ;
    public ServerInitializer( AtomicInteger connectNum ){
        this.connectNum = connectNum;
    }
    @Override
    protected void initChannel(SocketChannel ch) {
        //向管道加入处理器

        //得到管道
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("http-decoder", new HttpRequestDecoder());
        pipeline.addLast("http-aggregator", new HttpObjectAggregator(Integer.MAX_VALUE));
        pipeline.addLast("http-encoder", new HttpResponseEncoder());
        ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
        pipeline.addLast("compressor", new HttpContentCompressor());
        //2. 增加一个自定义的handler
        pipeline.addLast("MyTestHttpServerHandler", new HttpFileHandler(connectNum));
    }
}
