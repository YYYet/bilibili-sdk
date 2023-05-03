package com.bilisdk.common.options;

import com.bilisdk.common.exception.NotTurnedOnException;
import com.bilisdk.service.tv.entity.resp.medalInfo.MedalList;
import com.bilisdk.service.tv.sdk.LiveSdk;
import com.google.devtools.common.options.OptionsParser;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.*;

public class ServerCommond {
    static  String[] UUIDS = new String[]{UUID.randomUUID().toString(), UUID.randomUUID().toString()};
    public static void checkAction(OptionsParser parser, ServerOptions options){
        try {
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

    public static void startShareRoom(OptionsParser parser, ServerOptions options) throws Exception {
        checkException(parser, options);

        String accessToken = options.accessToken;
        Long roomId = Long.parseLong(options.roomId);

        LiveSdk liveSdk = new LiveSdk();
        boolean status = liveSdk.shareRoom(accessToken, roomId);
        System.out.printf("直播间 【 %s 】 弹幕发送状态: %s \n\r", roomId, status);
    }

    public static void startSendDanMu(OptionsParser parser, ServerOptions options) throws Exception {
        checkException(parser, options);

        String accessToken = options.accessToken;
        String danMu = options.danMu;
        Long roomId = Long.parseLong(options.roomId);

        LiveSdk liveSdk = new LiveSdk();
        boolean status = liveSdk.sendDanMuKu(accessToken, roomId, danMu);
        System.out.printf("直播间 【 %s 】 弹幕发送状态: %s \n\r", roomId, status);
    }

    public static void startGiveLike(OptionsParser parser, ServerOptions options) throws Exception {
        checkException(parser, options);

        String accessToken = options.accessToken;
        Long roomId = Long.parseLong(options.roomId);

        LiveSdk liveSdk = new LiveSdk();
        boolean status = liveSdk.giveLike(accessToken, roomId);
        System.out.printf("直播间 【 %s 】 点赞状态: %s \n\r", roomId, status);

    }

    public static void printUsage(OptionsParser parser) {
        System.out.println("使用指令: java -jar bilisdk.jar OPTIONS");
        System.out.println(" 常用指令如下: ");
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

        LiveSdk liveSdk = new LiveSdk();

        MedalList medal = liveSdk.GetOneMedalInfoByRoomId(accessToken, roomId);
        Long longRoomId = Long.parseLong(roomId);
        String targetId = String.valueOf(medal.getMedal().getTarget_id());

        if (options.liveTime.equals("-1")){
            heartBeat(medal, liveSdk, accessToken, longRoomId, targetId);
        }
        if (!options.liveTime.equals("-1")){

            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future future = executor.submit(()->{
                try {
                    heartBeat(medal, liveSdk, accessToken, longRoomId, targetId);
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

    private static void heartBeat(MedalList medal, LiveSdk liveSdk, String accessToken, Long longRoomId, String targetId) throws UnsupportedEncodingException, NoSuchAlgorithmException, InterruptedException {
        while (true){
            boolean heartbeat = liveSdk.Heartbeat(accessToken, longRoomId, UUIDS, targetId);
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
        if (options.roomId.isEmpty() ){
            printUsage(parser);
            throw new Exception("roomId为空");
        }
    }

}
