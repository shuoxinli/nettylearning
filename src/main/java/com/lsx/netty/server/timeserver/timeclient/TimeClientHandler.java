package com.lsx.netty.server.timeserver.timeclient;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Author LiShuoXin
 * @Date 2020/8/10 16:28
 * 客户端处理器
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            ByteBuf byteBuf = (ByteBuf) msg;
            int length = byteBuf.readableBytes();
            byte[] buff = new byte[1024];        //转为byte数组
            byteBuf.readBytes(buff,0,length);
            System.out.println("current server time :" + new String(buff,0,length));
            ctx.close();
        }finally {
            // 释放对象msg的引用计数，到达0时会被释放或归还到创建它的对象池
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
