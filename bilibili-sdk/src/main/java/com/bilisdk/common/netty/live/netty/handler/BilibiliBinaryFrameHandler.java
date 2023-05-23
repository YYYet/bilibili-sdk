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

package com.bilisdk.common.netty.live.netty.handler;

import com.bilisdk.common.netty.live.constant.CmdEnum;
import com.bilisdk.common.netty.live.constant.ProtoverEnum;
import com.bilisdk.common.netty.live.listener.IBilibiliSendSmsReplyMsgListener;
import com.bilisdk.common.netty.live.msg.SendSmsReplyMsg;
import com.bilisdk.common.netty.live.msg.base.BaseBilibiliMsg;
import com.bilisdk.common.netty.live.netty.frame.factory.BilibiliWebSocketFrameFactory;
import com.bilisdk.common.netty.live.util.BilibiliCodecUtil;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.ChannelInputShutdownReadComplete;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.ssl.SslCloseCompletionEvent;
import io.netty.handler.ssl.SslHandshakeCompletionEvent;
import io.netty.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author mjz
 * @date 2023/1/4
 */
@Slf4j
public class BilibiliBinaryFrameHandler extends SimpleChannelInboundHandler<BinaryWebSocketFrame> {

    /**
     * 客户端发送心跳包
     */
    private ScheduledFuture<?> scheduledFuture = null;
    private final IBilibiliSendSmsReplyMsgListener listener;

    public BilibiliBinaryFrameHandler(IBilibiliSendSmsReplyMsgListener listener) {
        this.listener = listener;
    }

    protected void channelRead0(ChannelHandlerContext ctx, BinaryWebSocketFrame message) throws Exception {
        ByteBuf byteBuf = message.content();
        List<BaseBilibiliMsg> msgList = BilibiliCodecUtil.decode(byteBuf);
        for (BaseBilibiliMsg msg : msgList) {
            if (msg instanceof SendSmsReplyMsg sendSmsReplyMsg) {
                CmdEnum cmd = sendSmsReplyMsg.getCmdEnum();
                if (cmd == null) {
                    listener.onUnknownCmd(sendSmsReplyMsg.getCmd(), sendSmsReplyMsg);
                    return;
                }
                // log.debug("收到 {} 消息 {}", cmd, msg);
                switch (cmd) {
                    case DANMU_MSG -> listener.onDanmuMsg(sendSmsReplyMsg);
                    case SEND_GIFT -> listener.onSendGift(sendSmsReplyMsg);
                    case INTERACT_WORD -> listener.onEnterRoom(sendSmsReplyMsg);
                    case ENTRY_EFFECT -> listener.onEntryEffect(sendSmsReplyMsg);
                    case WATCHED_CHANGE -> listener.onWatchedChange(sendSmsReplyMsg);
                    case LIKE_INFO_V3_CLICK -> listener.onClickLike(sendSmsReplyMsg);
                    case LIKE_INFO_V3_UPDATE -> listener.onClickUpdate(sendSmsReplyMsg);
                    case PK_BATTLE_PRE -> listener.onPkPre(sendSmsReplyMsg);
                    case PK_BATTLE_PRE_NEW -> listener.onPkPre(sendSmsReplyMsg);
                    case PK_BATTLE_PROCESS-> listener.onScopeChange(sendSmsReplyMsg);
                    case PK_BATTLE_PROCESS_NEW -> listener.onScopeChange(sendSmsReplyMsg);
                    case PK_BATTLE_START -> listener.onPkStart(sendSmsReplyMsg);
                    case PK_BATTLE_START_NEW -> listener.onPkStart(sendSmsReplyMsg);
                    case PK_BATTLE_END -> listener.onPkEnd(sendSmsReplyMsg);
                    case PK_BATTLE_PUNISH_END -> listener.onPkEnd(sendSmsReplyMsg);
                    case COMBO_SEND -> listener.onCombo(sendSmsReplyMsg);
                    case GUARD_BUY -> listener.onGuardBuy(sendSmsReplyMsg);
                    case HOT_RANK_CHANGED_V2 -> {
                        // TODO 主播实时活动排名变化
                    }
                    case ONLINE_RANK_COUNT -> {
                        // TODO 高能榜数量更新
                    }
                    case ROOM_REAL_TIME_MESSAGE_UPDATE -> {
                        // TODO 主播粉丝信息更新
                    }
                    case STOP_LIVE_ROOM_LIST -> {
                        // TODO 停止的直播间信息
                    }
                    case ONLINE_RANK_V2 -> {
                        // TODO 高能用户排行榜 更新
                    }
                    default -> {
                        listener.onOtherSendSmsReplyMsg(cmd, sendSmsReplyMsg);
                    }
                }
            }
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.error("userEventTriggered {}", evt.getClass());
        if (evt instanceof ChannelInputShutdownReadComplete) {
            // TODO
        } else if (evt instanceof SslHandshakeCompletionEvent) {
            if (null != scheduledFuture && !scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
            scheduledFuture = ctx.executor().scheduleAtFixedRate(() -> {
                ctx.writeAndFlush(
                        BilibiliWebSocketFrameFactory.getInstance(ProtoverEnum.NORMAL_ZLIB)
                                .createHeartbeat()
                );
                log.info("发送心跳包");
            }, 15, 30, TimeUnit.SECONDS);
        } else if (evt instanceof SslCloseCompletionEvent) {
            if (null != scheduledFuture && !scheduledFuture.isCancelled()) {
                scheduledFuture.cancel(true);
                scheduledFuture = null;
            }
        } else {
            log.error("待处理 {}", evt.getClass());
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause.getCause() instanceof UnrecognizedPropertyException) {
            log.error("缺少字段：{}", cause.getMessage());
        } else {
            super.exceptionCaught(ctx, cause);
        }
    }
}
