package com.lsx.netty.server.timeserver.timeclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @Author LiShuoXin
 * @Date 2020/8/10 16:28
 * 客户端app
 */
public class TimeClientApp {

    public void run() throws Exception{
        NioEventLoopGroup workLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap clientBootstrap = new Bootstrap();
            clientBootstrap.group(workLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            channel.pipeline().addLast(new TimeClientHandler());
                        }
                    })
                    .option(ChannelOption.SO_KEEPALIVE,true);

            ChannelFuture channelFuture = clientBootstrap.connect("localhost", 8080).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            workLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new TimeClientApp().run();
    }
}
