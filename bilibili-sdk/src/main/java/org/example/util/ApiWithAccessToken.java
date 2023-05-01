package org.example.util;

import com.alibaba.fastjson2.JSONObject;
import com.dtflys.forest.Forest;
import org.example.req.UserInfoResp;
import org.example.req.common.CommonReq;
import org.example.resp.common.Resp.CommonResp;
import org.example.resp.givelikeInfo.GiveLikeInfoResp;
import org.example.resp.heartbeatinfo.HeartBeat;
import org.example.resp.medalInfo.Data;
import org.example.resp.medalInfo.MedalInfoResp;
import org.example.resp.senddanmukuinfo.SendDanmuKuInfo;
import org.example.resp.shareroominfo.ShareRoomInfoResp;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.example.util.CommonUtil.randomString;


public class ApiWithAccessToken {
    // 获取当前穿戴的粉丝牌
    static final String GET_USERINFO_URL = "https://api.live.bilibili.com/xlive/app-ucenter/v1/user/get_user_info";
    // 获取所有粉丝牌
    static final String GET_MEDAL_URL = "https://api.live.bilibili.com/xlive/app-ucenter/v1/fansMedal/panel";
    // 直播间点赞
    static final String LIKE_INTERACT_URL = "https://api.live.bilibili.com/xlive/web-ucenter/v1/interact/likeInteract";
    // 分享直播间
    static final String SHARE_ROOM_URL = "https://api.live.bilibili.com/xlive/app-room/v1/index/TrigerInteract";
    // 发送弹幕
    static final String SEND_DANMUKU_URL = "https://api.live.bilibili.com/xlive/app-room/v1/dM/sendmsg";
    // 直播间挂机心跳
    static final String MOBILE_HEART_BEAT_URL = "https://live-trace.bilibili.com/xlive/data-interface/v1/heartbeat/mobileHeartBeat";
    // 进入直播间
    static final String MOBILE_ENTRY_ROOM_URL = "http://live-trace.bilibili.com/xlive/data-interface/v1/heartbeat/mobileEntry";
    // 取消穿戴粉丝牌
    static final String TAKE_OFF_MEDAL_URL = "https://api.live.bilibili.com/xlive/app-ucenter/v1/fansMedal/take_off";
    // 穿戴粉丝牌
    static final String WEAR_MEDAL_URL = "https://api.live.bilibili.com/xlive/app-ucenter/v1/fansMedal/wear";
    // 签到
    static final String SIGN_URL = "https://api.live.bilibili.com/rc/v1/Sign/doSign";
    // accessToken校验
    static final String TOKEN_VERIFY = "https://app.bilibili.com/x/v2/account/mine";

    /**
     * 获取用户信息
     * @param accessToken
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static UserInfoResp GetUserInfo(String accessToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap<String, String> map = new HashMap();

        CommonReq common = CommonReq.builder()
                .accessKey(accessToken)
                .actionKey(CST.actionKey)
                .appkey(CST.appTvkey)
                .ts(CommonUtil.getTimeStamps())
                .build();

        map.put("access_key", common.getAccessKey());
        map.put("actionKey", common.getActionKey());
        map.put("ts", common.getTs());
        map.put("appkey", common.getAppkey());

        HashMap<String, String> signature = SignUtil.signature(map);
        String url = SignUtil.buildQueryUrl(signature);
        return Forest.get(ApiWithAccessToken.GET_USERINFO_URL+url).execute(UserInfoResp.class);
    }

    /**
     * 获取所有粉丝牌
     * @param accessToken
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static MedalInfoResp GetMedalInfo(String accessToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ArrayList medalList = new ArrayList();
        List specialList = new ArrayList();
        HashMap map = new HashMap(5);
        String page = "0";
        map.put("access_key", accessToken);
        map.put("actionKey", CST.actionKey);
        map.put("ts", CommonUtil.getTimeStamps());
        map.put("appkey", CST.appTvkey);
        map.put("page", page);
        map.put("page_size", "50");
        boolean isNext = true;
        MedalInfoResp medalInfoAllResp = new MedalInfoResp();
        while (isNext){
            HashMap<String, String> signature = SignUtil.signature(map);
            String url = SignUtil.buildQueryUrl(signature);
            page = String.valueOf(Integer.parseInt(page)+1);
            map.put("page", page);
            map.put("ts", CommonUtil.getTimeStamps());
            MedalInfoResp medalInfoResp = Forest.get(ApiWithAccessToken.GET_MEDAL_URL + url).execute(MedalInfoResp.class);
            if (medalInfoResp.getData().getList()==null){
                System.out.println("medallist为空");
            }
            specialList.addAll(medalInfoResp.getData().getSpecial_list());
            medalList.addAll(medalInfoResp.getData().getList());
            if (medalInfoResp.getData().getList()==null || medalInfoResp.getData().getList().size() == 0){
                isNext = false;
                break;
            }
        }
        Data medalData = new Data();
        System.out.println("specialList"+specialList);
        medalData.setList(medalList);
        medalData.setSpecial_list(specialList);
        medalInfoAllResp.setData(medalData);
        return medalInfoAllResp;
    }

    /**
     * 直播间点赞
     * @param accessToken
     * @param roomId
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static boolean giveLike(String accessToken, long roomId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap map = new HashMap(5);
        map.put("access_key", accessToken);
        map.put("actionKey", CST.actionKey);
        map.put("ts", CommonUtil.getTimeStamps());
        map.put("appkey", CST.appTvkey);
        map.put("roomid", String.valueOf(roomId));
        HashMap<String, String> signature = SignUtil.signature(map);
        return  Forest.post(ApiWithAccessToken.LIKE_INTERACT_URL).addBody(signature).execute(GiveLikeInfoResp.class).getCode() == 0;
    }
    /**
     * 分享直播间
     * @param accessToken
     * @param roomId
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static boolean shareRoom(String accessToken, long roomId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap map = new HashMap(5);
        map.put("access_key", accessToken);
        map.put("actionKey", CST.actionKey);
        map.put("ts", CommonUtil.getTimeStamps());
        map.put("appkey", CST.appTvkey);
        map.put("interact_type", "3");
        map.put("roomid", String.valueOf(roomId));
        HashMap<String, String> signature = SignUtil.signature(map);
        return Forest.post(ApiWithAccessToken.SHARE_ROOM_URL).addBody(signature).execute(ShareRoomInfoResp.class).getCode() == 0;
    }

    /**
     *  直播间发送弹幕
     * @param accessToken
     * @param roomId
     * @param danMuKu
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public static boolean sendDanmuKu(String accessToken, long roomId, String danMuKu) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap map = new HashMap(5);
        map.put("access_key", accessToken);
        map.put("actionKey", CST.actionKey);
        map.put("ts", CommonUtil.getTimeStamps());
        map.put("appkey", CST.appTvkey);

        HashMap<String, String> signature = SignUtil.signature(map);

        HashMap<String, String> danmuKu = new HashMap<>();
        danmuKu.put("cid", String.valueOf(roomId));
        danmuKu.put("msg", danMuKu);
        danmuKu.put("rnd", CommonUtil.getTimeStamps());
        danmuKu.put("color", "16777215");
        danmuKu.put("fontsize", "25");
        return Forest.post(ApiWithAccessToken.SEND_DANMUKU_URL+SignUtil.buildQueryUrl(signature)).addBody(danmuKu).execute(SendDanmuKuInfo.class).getCode() == 0;
    }

    /**
     * 直播间心跳
     * @param accessToken
     * @param roomId
     * @param uuids
     * @param upId
     * @return
     * @throws Exception
     */
    public static boolean Heartbeat(String accessToken, long roomId, String[] uuids, String upId) throws Exception {
        HashMap map = new HashMap(45);

        map.put("platform", "android");
        map.put("uuid", uuids[0]);
        map.put("buvid", randomString(37).toUpperCase());
        map.put("seq_id", "1");
        map.put("room_id", String.valueOf(roomId));
        map.put("parent_id", "6");
        map.put("area_id", "283");
        map.put("timestamp", String.valueOf(Instant.now().getEpochSecond()-60));
        map.put("secret_key", "axoaadsffcazxksectbbb");
        map.put("watch_time", "60");
        map.put("up_id", upId);
        map.put("up_level", "40");
        map.put("jump_from", "30000");
        map.put("gu_id", randomString(43).toUpperCase());
        map.put("play_type", "0");
        map.put("play_url", "");
        map.put("s_time", "0");
        map.put("data_behavior_id", "");
        map.put("data_source_id", "");
        map.put("up_session", String.format("l:one:live:record:%d:%d", roomId,  Instant.now().getEpochSecond() - 88888));
        map.put("visit_id", randomString(32).toUpperCase());
        map.put("watch_status", "%7B%22pk_id%22%3A0%2C%22screen_status%22%3A1%7D");
        map.put("click_id", uuids[1]);
        map.put("session_id", "");
        map.put("player_type", "0");
        map.put("client_ts", String.valueOf(Instant.now().getEpochSecond()));


//        HashMap<String, Object> hash = new HashMap<>();
//        hash.put("platform", map.get("platform").toString());
//        hash.put("uuid", map.get("uuid").toString());
//        hash.put("buvid", map.get("buvid").toString());
//        hash.put("seq_id", map.get("seq_id").toString());
//        hash.put("room_id", map.get("room_id").toString());
//        hash.put("parent_id", map.get("parent_id").toString());
//        hash.put("area_id", map.get("area_id").toString());
//        hash.put("timestamp", map.get("timestamp").toString());
//        hash.put("secret_key", map.get("secret_key").toString());
//        hash.put("watch_time", map.get("watch_time").toString());
//        hash.put("up_id", map.get("up_id").toString());
//        hash.put("up_level", map.get("up_level").toString());
//        hash.put("jump_from", map.get("jump_from").toString());
//        hash.put("gu_id", map.get("play_type").toString());
//        hash.put("play_url", map.get("s_time").toString());
//        hash.put("data_behavior_id", map.get("data_source_id").toString());
//        hash.put("up_session", map.get("up_session").toString());
//        hash.put("visit_id", map.get("visit_id").toString());
//        hash.put("watch_status", map.get("watch_status").toString());
//        hash.put("click_id", map.get("click_id").toString());
//        hash.put("session_id", map.get("session_id").toString());
//        hash.put("player_type", map.get("player_type").toString());
//        hash.put("client_ts", map.get("client_ts").toString());

        String dataStr = String.format("{\"platform\":\"%s\",\"uuid\":\"%s\",\"buvid\":\"%s\",\"seq_id\":\"%s\",\"room_id\":\"%s\",\"parent_id\":\"%s\",\"area_id\":\"%s\",\"timestamp\":\"%s\",\"secret_key\":\"%s\",\"watch_time\":\"%s\",\"up_id\":\"%s\",\"up_level\":\"%s\",\"jump_from\":\"%s\",\"gu_id\":\"%s\",\"play_type\":\"%s\",\"play_url\":\"%s\",\"s_time\":\"%s\",\"data_behavior_id\":\"%s\",\"data_source_id\":\"%s\",\"up_session\":\"%s\",\"visit_id\":\"%s\",\"watch_status\":\"%s\",\"click_id\":\"%s\",\"session_id\":\"%s\",\"player_type\":\"%s\",\"client_ts\":\"%s\"}",
                map.get("platform"),
                map.get("uuid"),
                map.get("buvid"),
                map.get("seq_id"),
                map.get("room_id"),
                map.get("parent_id"),
                map.get("area_id"),
                map.get("timestamp"),
                map.get("secret_key"),
                map.get("watch_time"),
                map.get("up_id"),
                map.get("up_level"),
                map.get("jump_from"),
                map.get("gu_id"),
                map.get("play_type"),
                map.get("play_url"),
                map.get("s_time"),
                map.get("data_behavior_id"),
                map.get("data_source_id"),
                map.get("up_session"),
                map.get("visit_id"),
                map.get("watch_status"),
                map.get("click_id"),
                map.get("session_id"),
                map.get("player_type"),
                map.get("client_ts"));

        map.put("client_sign",SignUtil.clientSign(dataStr));
        map.put("access_key", accessToken);
        map.put("actionKey", CST.actionKey);
        map.put("appkey", CST.appTvkey);
        map.put("ts", String.valueOf(Instant.now().getEpochSecond()));

        HashMap signature = SignUtil.signature(map);

        HashMap<String, String> head = new HashMap<>();
        head.put("User-Agent", "Mozilla/5.0 BiliDroid/6.73.1 (bbcallen@gmail.com) os/android model/Mi 10 Pro mobi_app/android build/6731100 channel/xiaomi innerVer/6731110 osVer/12 network/2");
        head.put("Content-Type", "application/x-www-form-urlencoded");
        head.put("Accept", "application/json");
        head.put("Connection", "keep-alive");

        HeartBeat execute = Forest.post(ApiWithAccessToken.MOBILE_HEART_BEAT_URL).contentTypeMultipartFormData().addBody(signature).execute(HeartBeat.class);
        return execute.getCode() == 0;
    }

    /**
     * 进入直播间（无效）
     * @param accessToken
     * @param roomId
     * @param uuids
     * @param upId
     * @return
     * @throws Exception
     */
    public static String EntryRoom(String accessToken, long roomId, String[] uuids, String upId) throws Exception {
        HashMap<String, String> data  = new HashMap<>();
        data.put("access_key", accessToken);
        data.put("actionKey", CST.actionKey);
        data.put("appkey", CST.appTvkey);
        data.put("ts", CommonUtil.getTimeStamps());
        data.put("platform", "android");
        data.put("uuid", uuids[0]);
        data.put("buvid", randomString(37).toUpperCase());
        data.put("seq_id", "1");
        data.put("room_id", String.valueOf(roomId));
        data.put("parent_id", "6");
        data.put("area_id", "283");
        data.put("timestamp", String.valueOf(CommonUtil.getTimeStampsWithLong()-60));
        data.put("up_id", upId);
        data.put("watch_time", "60");
        data.put("up_level", "40");
        data.put("jump_from", "30000");
        data.put("gu_id", randomString(43).toUpperCase());
        data.put("visit_id", randomString(32).toUpperCase());
        data.put("click_id", uuids[1]);
        data.put("heart_beat", "[]");
        data.put("client_ts", CommonUtil.getTimeStamps());

        SignUtil.signatureWithoutReturn(data);

        return Forest.post(ApiWithAccessToken.MOBILE_ENTRY_ROOM_URL).contentTypeMultipartFormData().addBody(data).execute(String.class);
    }

    /**
     * 取消穿戴粉丝牌
     * @param accessToken
     * @param medalId
     * @return
     * @throws Exception
     */
    public static boolean TakeOffMedal(String accessToken, long medalId) throws Exception {
        HashMap<String, String> data  = new HashMap<>();
        data.put("access_key", accessToken);
        data.put("actionKey", CST.actionKey);
        data.put("appkey", CST.appTvkey);
        data.put("ts", CommonUtil.getTimeStamps());
        data.put("medal_id", String.valueOf(medalId));
        data.put("platform", "android");
        data.put("type", "1");
        data.put("version", "0");

        SignUtil.signatureWithoutReturn(data);

        return Forest.post(ApiWithAccessToken.TAKE_OFF_MEDAL_URL).addBody(data).execute(CommonResp.class).getCode() == 0;
    }

    /**
     * 穿戴粉丝牌
     * @param accessToken
     * @param medalId
     * @return
     * @throws Exception
     */
    public static boolean WearMedal(String accessToken, long medalId) throws Exception {
        HashMap<String, String> data  = new HashMap<>();
        data.put("access_key", accessToken);
        data.put("actionKey", CST.actionKey);
        data.put("appkey", CST.appTvkey);
        data.put("ts", CommonUtil.getTimeStamps());
        data.put("medal_id", String.valueOf(medalId));
        data.put("platform", "android");
        data.put("type", "1");
        data.put("version", "0");

        SignUtil.signatureWithoutReturn(data);

        return Forest.post(ApiWithAccessToken.WEAR_MEDAL_URL).addBody(data).execute(CommonResp.class).getCode() == 0;
    }

    /**
     * 签到
     * @param accessToken
     * @return
     * @throws Exception
     */
    public static String SignIn(String accessToken) throws Exception {
        HashMap<String, String> data  = new HashMap<>();
        data.put("access_key", accessToken);
        data.put("actionKey", CST.actionKey);
        data.put("appkey", CST.appTvkey);
        data.put("ts", CommonUtil.getTimeStamps());

        SignUtil.signatureWithoutReturn(data);

        return Forest.get(ApiWithAccessToken.SIGN_URL).addQuery(data).execute(String.class);
    }
    /**
     * 签到
     * @param accessToken
     * @return
     * @throws Exception
     */
    public static boolean VerifyToken(String accessToken) throws Exception {
        HashMap<String, String> data  = new HashMap<>();
        data.put("access_key", accessToken);
        data.put("actionKey", CST.actionKey);
        data.put("appkey", CST.appTvkey);
        data.put("ts", CommonUtil.getTimeStamps());

        SignUtil.signatureWithoutReturn(data);

        return Forest.get(ApiWithAccessToken.TOKEN_VERIFY).addQuery(data).execute(JSONObject.class).get("code").equals(0);
    }
}
