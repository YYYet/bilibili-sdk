package com.bilisdk.common.options;

import com.google.devtools.common.options.Option;
import com.google.devtools.common.options.OptionsBase;

import java.util.List;

/**
 * Command-line options definition for example server.
 */
public class ServerOptions extends OptionsBase {

    @Option(
            name = "accessToken",
            abbrev = 't',
            help = "令牌",
            category = "参数",
            defaultValue = ""
    )
    public String accessToken;

    @Option(
            name = "model",
            help = "登录模式",
            category = "参数",
            defaultValue = "tv"
    )
    public String model;

    @Option(
            name = "roomId",
            abbrev = 'r',
            help = "房间Id",
            category = "参数",
            defaultValue = ""
    )
    public String roomId;

    @Option(
            name = "uid",
            abbrev = 'u',
            help = "用户id",
            category = "参数",
            defaultValue = ""
    )
    public String uid;

    @Option(
            name = "danMu",
            abbrev = 'm',
            help = "弹幕内容",
            category = "参数",
            defaultValue = "O.o"
    )
    public String danMu;

    @Option(
            name = "live",
            help = "直播间挂机",
            category = "行为",
            defaultValue = "false"
    )
    public boolean live;

    @Option(
            name = "liveTime",
            help = "直播间挂机时长(小时)",
            category = "参数",
            defaultValue = "-1"
    )
    public String liveTime;

    @Option(
            name = "sendDanMu",
            help = "发送弹幕",
            category = "行为",
            defaultValue = "false"
    )
    public boolean sendDanMu;

    @Option(
            name = "giveLike",
            help = "点赞",
            category = "行为",
            defaultValue = "false"
    )
    public boolean giveLike;

    @Option(
            name = "shareRoom",
            help = "分享",
            category = "行为",
            defaultValue = "false"
    )
    public boolean shareRoom;

    @Option(
            name = "charge",
            help = "充电",
            category = "行为",
            defaultValue = "false"
    )
    public boolean charge;
    @Option(
            name = "login",
            help = "登录",
            category = "行为",
            defaultValue = "false"
    )
    public boolean login;

}
