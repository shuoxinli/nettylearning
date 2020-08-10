package com.lsx.netty.server.timeserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author LiShuoXin
 * @Date 2020/8/10 16:06
 * 服务端处理器
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 获取池化的内存块
        ByteBuf timeBuf = ctx.alloc().buffer();
        // 写入服务端的时间
        timeBuf.writeBytes(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).getBytes());

        final ChannelFuture channelFuture = ctx.writeAndFlush(timeBuf);
        // 添加事件，GenericFutureListener是非阻塞的，在I/O操作完成的时候，能够获得通知
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            @Override
            public void operationComplete(Future<? super Void> future) throws Exception {
                assert channelFuture == future;
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
