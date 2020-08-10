package com.lsx.netty.server.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @Author LiShuoXin
 * @Date 2020/8/10 14:52
 * 输出打印服务端APP
 */
public class EchoServerApp {
    private int port;

    public EchoServerApp(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        // 创建并分配一个NioEventLoopGroup实例以进行事件的处理
        // bossLoopGroup为parentGroup,处理ServerChannel，例如处理与客户端建立连接
        // workLoopGroup为childGroup,处理Channel，例如处理读写数据
        NioEventLoopGroup bossLoopGroup = new NioEventLoopGroup();
        NioEventLoopGroup workLoopGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossLoopGroup, workLoopGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)  // tcp/ip协议listen函数中的backlog参数,等待队列的大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true);
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            //关闭所有channel，包括客户端的SocketChannel与服务器的Channel
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossLoopGroup.shutdownGracefully();
            workLoopGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new EchoServerApp(8080).run();
    }
}
