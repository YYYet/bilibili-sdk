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
            name = "loginModel",
            abbrev = 'l',
            help = "登录模式",
            category = "参数",
            defaultValue = "tv"
    )
    public String loginModel;

    @Option(
            name = "roomId",
            abbrev = 'r',
            help = "房间Id",
            category = "参数",
            defaultValue = ""
    )
    public String roomId;

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

}