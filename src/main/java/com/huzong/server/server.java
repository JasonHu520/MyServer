package com.huzong.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.atomic.AtomicInteger;

public class server{
    /**
     * @connectNum 当前连接数
     */
    private final AtomicInteger connectNum =new AtomicInteger(0);
    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ServerInitializer(connectNum));

            ChannelFuture cf = serverBootstrap.bind(8080).sync();


            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) {
                    if (cf.isSuccess()) {
                        System.out.println("监听8080成功");
                    } else {
                        System.out.println("监听端口 8080 失败");
                    }
//                    if(cf.is)
                }
            });

            cf.channel().closeFuture().sync();

        }
        catch (InterruptedException e){
            throw new RuntimeException(e);
        }
        finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
