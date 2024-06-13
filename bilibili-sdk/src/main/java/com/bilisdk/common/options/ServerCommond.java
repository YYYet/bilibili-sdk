package com.bilisdk.common.options;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.json.JSONUtil;
import com.bilisdk.common.exception.NotTurnedOnException;
import com.bilisdk.common.util.CommonUtil;
import com.bilisdk.service.tv.entity.resp.medalInfo.MedalList;
import com.bilisdk.service.tv.entity.resp.qrcodeInfo.QRcodeInfoResp;
import com.bilisdk.service.tv.entity.resp.verifyqrcodeinfo.VerifyQRcodeInfoResp;
import com.bilisdk.service.tv.sdk.TvLiveSdk;
import com.bilisdk.service.tv.sdk.TvLoginSdk;
import com.bilisdk.service.web.sdk.WebChargeSdk;
import com.bilisdk.service.web.sdk.WebLoginSdk;
import com.dtflys.forest.utils.URLEncoder;
import com.google.devtools.common.options.OptionsParser;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.*;

public class ServerCommond {
    static  String[] UUIDS = new String[]{UUID.randomUUID().toString(), UUID.randomUUID().toString()};
    public static void checkAction(OptionsParser parser, ServerOptions options){
        try {


            if (options.login) {
                String loginModel = options.model;
                if (loginModel.equals("tv")){
                    System.out.println("开始tv登录");
                    TvLoginSdk.startTvLogin();
                }
                if (loginModel.equals("web")){
                    System.out.println("开始web登录");
                    WebLoginSdk.startWebLogin();
                }
            }

            if (options.charge) {
                startChrage(parser, options);
            }
            if (options.live) {
                startLive(parser, options);
            }
            if (options.giveLike) {
                startGiveLike(parser, options);
            }
            if (options.sendDanMu) {
                startSendDanMu(parser, options);
            }
            if (options.shareRoom) {
                startShareRoom(parser, options);
            }
        }catch (Exception e){
//            printUsage(parser);
        }
    }

    public static void startChrage(OptionsParser parser, ServerOptions options) throws Exception {

        System.out.println("充电开始");
        long uid = Long.parseLong(options.uid);
        String model = options.model;
        WebChargeSdk webChargeSdk = new WebChargeSdk();
        ArrayList<HashMap> hashMaps = CommonUtil.readCookieOrToken(model.equalsIgnoreCase("tv"));
        for (HashMap line : hashMaps) {
            HashMap dedeUserID = JSONUtil.parseObj(line.get("DedeUserID")).toBean(HashMap.class);
            System.out.printf("用户 【 %s 】 领取b币券 \n\r", dedeUserID.get("value"), uid);
            boolean isReciveBpCard = webChargeSdk.reciveBpCard(JSONUtil.toJsonStr(line));

            if (isReciveBpCard){
                System.out.printf("用户 【 %s 】 为 用户 【 %s 】 充电 \n\r", dedeUserID.get("value"), uid);
                webChargeSdk.AutoChargeUser(JSONUtil.toJsonStr(line), uid);
            }
            System.out.println(" ");
        }

    }

    public static void startShareRoom(OptionsParser parser, ServerOptions options) throws Exception {
        checkException(parser, options);

        String accessToken = options.accessToken;
        Long roomId = Long.parseLong(options.roomId);

        TvLiveSdk tvLiveSdk = new TvLiveSdk();
        boolean status = tvLiveSdk.shareRoom(accessToken, roomId);
        System.out.printf("直播间 【 %s 】 弹幕发送状态: %s \n\r", roomId, status);
    }

    public static void startSendDanMu(OptionsParser parser, ServerOptions options) throws Exception {
        checkException(parser, options);

        String accessToken = options.accessToken;
        String danMu = options.danMu;
        Long roomId = Long.parseLong(options.roomId);

        TvLiveSdk tvLiveSdk = new TvLiveSdk();
        boolean status = tvLiveSdk.sendDanMuKu(accessToken, roomId, danMu);
        System.out.printf("直播间 【 %s 】 弹幕发送状态: %s \n\r", roomId, status);
    }

    public static void startGiveLike(OptionsParser parser, ServerOptions options) throws Exception {
        checkException(parser, options);

        String accessToken = options.accessToken;
        Long roomId = Long.parseLong(options.roomId);

        TvLiveSdk tvLiveSdk = new TvLiveSdk();
        boolean status = tvLiveSdk.giveLike(accessToken, roomId);
        System.out.printf("直播间 【 %s 】 点赞状态: %s \n\r", roomId, status);

    }

    public static void printUsage(OptionsParser parser) {
        System.out.println("使用指令: java -jar bilisdk.jar OPTIONS");
        System.out.println(" 常用指令如下: ");
        System.out.println("  登录: java -jar bilisdk.jar --model [tv|web]");
        System.out.println("  直播间挂机: java -jar bilisdk.jar --accessToken {你的令牌} --roomId {房间号} [--liveTime {挂机时间(小时)}] --live [true]");
        System.out.println("  直播间点赞: java -jar bilisdk.jar --accessToken {你的令牌} --roomId {房间号} --giveLike [true]");
        System.out.println("  直播间分享: java -jar bilisdk.jar --accessToken {你的令牌} --roomId {房间号} --shareRoom [true]");
        System.out.println("  直播间弹幕: java -jar bilisdk.jar --accessToken {你的令牌} --roomId {房间号} [--danMu {弹幕内容}] --sendDanMu [true]");
        System.out.println(parser.describeOptions(Collections.<String, String>emptyMap(),
                OptionsParser.HelpVerbosity.LONG));
    }

    public static void startLive(OptionsParser parser, ServerOptions options) throws Exception {
        if (!options.live){
            throw new NotTurnedOnException("直播间挂机行为：false");
        }

        checkException(parser, options);

        String accessToken = options.accessToken;
        String roomId = options.roomId;

        TvLiveSdk tvLiveSdk = new TvLiveSdk();

        MedalList medal = tvLiveSdk.GetOneMedalInfoByRoomId(accessToken, roomId);
        Long longRoomId = Long.parseLong(roomId);
        String targetId = String.valueOf(medal.getMedal().getTarget_id());

        if (options.liveTime.equals("-1")){
            heartBeat(medal, tvLiveSdk, accessToken, longRoomId, targetId);
        }
        if (!options.liveTime.equals("-1")){

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future future = executor.submit(()->{
                try {
                    heartBeat(medal, tvLiveSdk, accessToken, longRoomId, targetId);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            try {
                future.get(Integer.parseInt(options.liveTime), TimeUnit.HOURS);
            } catch (TimeoutException e) {
                future.cancel(true);
            } finally {
//                service.shutdownNow();
                System.exit(0);
            }


        }
    }

    private static void heartBeat(MedalList medal, TvLiveSdk tvLiveSdk, String accessToken, Long longRoomId, String targetId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InterruptedException {
        while (true){
            boolean heartbeat = tvLiveSdk.Heartbeat(accessToken, longRoomId, UUIDS, targetId);
            if (!heartbeat){
                Thread.sleep(10 * 1000);
                System.out.printf("【 %s 】的直播间 【 %s 】 心跳包发送状态: %s 10秒后重试 \n\r", medal.getAnchor_info().getNick_name(), longRoomId, heartbeat);
            }
            if (heartbeat){
                System.out.printf("【 %s 】的直播间 【 %s 】 心跳包发送状态: %s \n\r", medal.getAnchor_info().getNick_name(), longRoomId, heartbeat);
                Thread.sleep(60 * 1000);
            }
        }
    }
    private static void checkException(OptionsParser parser, ServerOptions options) throws Exception {
        if (options.accessToken.isEmpty()){
            printUsage(parser);
            throw new Exception("accessToken为空");
        }
        if (options.roomId.isEmpty()){
            printUsage(parser);
            throw new Exception("roomId为空");
        }
    }

}
