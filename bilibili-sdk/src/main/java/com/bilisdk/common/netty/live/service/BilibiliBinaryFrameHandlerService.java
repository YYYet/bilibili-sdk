/*
 * MIT License
 *
 * Copyright (c) 2023 OrdinaryRoad
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bilisdk.common.netty.live.service;


import com.bilisdk.common.cache.CacheSingleton;
import com.bilisdk.common.netty.live.constant.CmdEnum;
import com.bilisdk.common.netty.live.constant.ProtoverEnum;
import com.bilisdk.common.netty.live.listener.IBilibiliSendSmsReplyMsgListener;
import com.bilisdk.common.netty.live.msg.SendSmsReplyMsg;
import com.bilisdk.common.netty.live.netty.frame.factory.BilibiliWebSocketFrameFactory;
import com.bilisdk.common.netty.live.netty.handler.BilibiliBinaryFrameHandler;
import com.bilisdk.common.netty.live.netty.handler.BilibiliHandshakerHandler;

import com.bilisdk.common.threadPool.ThreadPool;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.Cache;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

/**
 * @author mjz
 * @date 2023/1/7
 */
@Slf4j
public class BilibiliBinaryFrameHandlerService {

    public void exec(int roomid, String webHook) {
//        Thread one = threadService.getOne(Wrappers.<Thread>lambdaQuery().eq(Thread::getRoomid, roomid));
//
//        if (one.getStatus() == 0){
//
//        }
//





        Cache<Long, EventLoopGroup> instance = CacheSingleton.SingletonEnum.SINGLETON.getInstance();

        if (instance.asMap().containsKey(roomid)){
            return;
        }

        EventLoopGroup workerGroup = new NioEventLoopGroup();
        instance.put((long) roomid, workerGroup);
        try {
            URI websocketURI = new URI("wss://broadcastlv.chat.bilibili.com:2245/sub");

            BilibiliHandshakerHandler bilibiliHandshakerHandler = new BilibiliHandshakerHandler(WebSocketClientHandshakerFactory.newHandshaker(
                    websocketURI, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));
            BilibiliBinaryFrameHandler bilibiliHandler = new BilibiliBinaryFrameHandler(new IBilibiliSendSmsReplyMsgListener() {
                @Override
                public void onDanmuMsg(SendSmsReplyMsg msg) {
                    JsonNode info = msg.getInfo();
                    JsonNode jsonNode1 = info.get(1);
                    String danmuText = jsonNode1.asText();
                    JsonNode jsonNode2 = info.get(2);
                    String uid = jsonNode2.get(0).asText();
                    String uname = jsonNode2.get(1).asText();
                    log.info("房间【"+roomid+"】"+"收到弹幕 {}({})：{}", uname, uid, danmuText);
                }

                @Override
                public void onSendGift(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    String action = data.get("action").asText();
                    String giftName = data.get("giftName").asText();
                    Integer num = data.get("num").asInt();
                    String uname = data.get("uname").asText();
                    Integer price = data.get("price").asInt();
                    log.info("房间【"+roomid+"】"+"收到礼物 {} {} {}x{}({})  {}", uname, action, giftName, num, price, data);
                }

                @Override
                public void onEnterRoom(SendSmsReplyMsg msg) {
                    log.info("房间【"+roomid+"】"+"普通用户进入直播间 {} {}", msg.getData().get("uname").asText(), msg);
                }

                @Override
                public void onEntryEffect(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    String copyWriting = data.get("copy_writing").asText();
                    log.info("房间【"+roomid+"】"+"入场效果 {} {}", copyWriting, data);
                }

                @Override
                public void onWatchedChange(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    int num = data.get("num").asInt();
                    String textSmall = data.get("text_small").asText();
                    String textLarge = data.get("text_large").asText();
                    log.debug("房间【"+roomid+"】"+"观看人数变化 {} {} {}", num, textSmall, textLarge);
                }

                @Override
                public void onClickLike(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    String uname = data.get("uname").asText();
                    String likeText = data.get("like_text").asText();
                    log.debug("房间【"+roomid+"】"+"为主播点赞 {} {}", uname, likeText);
                }

                @Override
                public void onClickUpdate(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    int clickCount = data.get("click_count").asInt();
                    log.debug("房间【"+roomid+"】"+"点赞数更新 {}", clickCount);
                }

                @Override
                public void onGuardBuy(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.debug("房间【"+roomid+"】"+"直播间开通信息 {}", data);
                }

                @Override
                public void onScopeChange(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.debug("房间【"+roomid+"】"+"双方分数变化 {}", data);
                }

                @Override
                public void onPkPre(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.debug("房间【"+roomid+"】"+"pk连接 {}", data.toString());
                }

                @Override
                public void onPkStart(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.debug("房间【"+roomid+"】"+"pk开始 {}", data.toString());
                }

                @Override
                public void onPkEnd(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.debug("房间【"+roomid+"】"+"pk结束 {}", data.toString());
                }

                @Override
                public void onCombo(SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.info("房间【"+roomid+"】"+"收到投喂连击 {}", data);
                }

                @Override
                public void onOtherSendSmsReplyMsg(CmdEnum cmd, SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.info("房间【"+roomid+"】"+"其他消息 {} {}", cmd, data);
                }

                @Override
                public void onUnknownCmd(String cmdString, SendSmsReplyMsg msg) {
                    JsonNode data = msg.getData();
                    log.info("房间【"+roomid+"】"+"未知cmd {} {}", cmdString, data);
                }
            });

            //进行握手
            log.info("房间【"+roomid+"】"+"握手开始");
//            SslContext sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
            SslContext sslCtx = SslContextBuilder.forClient().build();

            Bootstrap bootstrap = new Bootstrap()
                    .group(workerGroup)
                    // 创建Channel
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    // Channel配置
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            // 责任链
                            ChannelPipeline pipeline = ch.pipeline();

                            //放到第一位 addFirst 支持wss链接服务端
                            pipeline.addFirst(sslCtx.newHandler(ch.alloc(), websocketURI.getHost(), websocketURI.getPort()));

                            // 添加一个http的编解码器
                            pipeline.addLast(new HttpClientCodec());
                            // 添加一个用于支持大数据流的支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 添加一个聚合器，这个聚合器主要是将HttpMessage聚合成FullHttpRequest/Response
                            pipeline.addLast(new HttpObjectAggregator(1024 * 64));

//                            pipeline.addLast(WebSocketClientCompressionHandler.INSTANCE);
//                            pipeline.addLast(new WebSocketServerProtocolHandler("/sub", null, true, 65536 * 10));

                            // 握手处理器
                            pipeline.addLast(bilibiliHandshakerHandler);
                            pipeline.addLast(bilibiliHandler);
                        }
                    });

            final Channel channel = bootstrap.connect(websocketURI.getHost(), websocketURI.getPort()).sync().channel();

            // 阻塞等待是否握手成功
            bilibiliHandshakerHandler.handshakeFuture().sync();

            // 5s内认证
            log.info("房间【"+roomid+"】"+"发送认证包");
            channel.writeAndFlush(
                    BilibiliWebSocketFrameFactory.getInstance(ProtoverEnum.NORMAL_ZLIB)
//                             7777
//                            .createAuth(545068)
//                            .createAuth(27625653)
                            .createAuth(roomid) // 没烦事
//                            .createAuth(27746349) //园子remake
//                            .createAuth(27695966)
//                            .createAuth(7396329)
            );
            instance.put((long) roomid, workerGroup);
            channel.closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}