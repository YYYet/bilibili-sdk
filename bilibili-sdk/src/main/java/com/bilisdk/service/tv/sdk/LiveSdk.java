package com.bilisdk.service.tv.sdk;

import com.bilisdk.service.tv.api.LiveApi;
import com.bilisdk.service.tv.entity.resp.userinfo.UserInfoResp;
import com.bilisdk.service.tv.entity.resp.heartbeatinfo.HeartBeatInfoResp;
import com.bilisdk.service.tv.entity.resp.medalInfo.MedalInfo;
import com.bilisdk.service.tv.entity.resp.medalInfo.MedalInfoResp;
import com.bilisdk.common.util.CommonUtil;
import com.bilisdk.common.util.SignUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LiveSdk extends LiveApi {

    /**
     * 获取用户信息
     * @param accessToken
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public UserInfoResp GetUserInfo(String accessToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap<String, String> map = CommonUtil.initBaseParams(accessToken);
        HashMap<String, String> signature = SignUtil.signature(map);

        return liveReq.getUserInfo(signature);
    }

    /**
     * 获取所有粉丝牌 此处需要重写
     * @param accessToken
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public  MedalInfoResp GetMedalInfo(String accessToken) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ArrayList medalList = new ArrayList();
        List specialList = new ArrayList();
        HashMap map =  CommonUtil.initBaseParams(accessToken);
        String page = "0";
        map.put("page", page);
        map.put("page_size", "50");
        boolean isNext = true;
        MedalInfoResp medalInfoAllResp = new MedalInfoResp();
        while (isNext){
            HashMap<String, String> signature = SignUtil.signature(map);

            page = String.valueOf(Integer.parseInt(page)+1);
            map.put("page", page);
            map.put("ts", CommonUtil.getTimeStamps());

            MedalInfoResp medalInfoResp = liveReq.getMedalInfo(signature);

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
        MedalInfo medalData = new MedalInfo();
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
    public  boolean giveLike(String accessToken, long roomId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap map = CommonUtil.initBaseParams(accessToken);
        CommonUtil.initGiveLikeParams(map, roomId);
        SignUtil.signature(map);

        return  liveReq.giveALike(map).getCode() == 0;
    }
    /**
     * 分享直播间
     * @param accessToken
     * @param roomId
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public  boolean shareRoom(String accessToken, long roomId) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap map = CommonUtil.initBaseParams(accessToken);
        CommonUtil.initShareRoomParams(map, roomId);
        SignUtil.signature(map);
        return liveReq.shareRoom(map).getCode() == 0;
    }

    /**
     *  直播间发送弹幕
     * @param accessToken
     * @param roomId
     * @param msg
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    public  boolean sendDanMuKu(String accessToken, long roomId, String msg) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        HashMap map =  CommonUtil.initBaseParams(accessToken);

        HashMap<String, String> signature = SignUtil.signature(map);

        HashMap<String, String> DanMuData= CommonUtil.initDanMuKuParams(roomId, msg);

        return liveReq.sendDanmuKu(signature, DanMuData).getCode() == 0;
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
    public boolean Heartbeat(String accessToken, long roomId, String[] uuids, String upId) throws Exception {

        HashMap<String, String> map  = CommonUtil.initBaseParams(accessToken);
        CommonUtil.initHeartbeatParams(map, roomId, uuids, upId);

        SignUtil.signature(map);

        HeartBeatInfoResp execute = liveReq.heartBeat("multipart/form-data",map);

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
//    public static String EntryRoom(String accessToken, long roomId, String[] uuids, String upId) throws Exception {
//        HashMap data  = CommonUtil.initBaseParams(accessToken);
//
//        CommonUtil.initEntryRoomParams(data, roomId, uuids, upId);
//
//        SignUtil.signatureWithoutReturn(data);
//
//        return Forest.post(LiveSdk.MOBILE_ENTRY_ROOM_URL).contentTypeMultipartFormData().addBody(data).execute(String.class);
//    }

    /**
     * 取消穿戴粉丝牌
     * @param accessToken
     * @param medalId
     * @return
     * @throws Exception
     */
    public boolean TakeOffMedal(String accessToken, long medalId) throws Exception {

        HashMap data  = CommonUtil.initBaseParams(accessToken);

        data.put("medal_id", String.valueOf(medalId));
        data.put("platform", "android");
        data.put("type", "1");
        data.put("version", "0");

        SignUtil.signatureWithoutReturn(data);

        return liveReq.takeOffMedal(data).getCode() == 0;
    }

    /**
     * 穿戴粉丝牌
     * @param accessToken
     * @param medalId
     * @return
     * @throws Exception
     */
    public boolean WearMedal(String accessToken, long medalId) throws Exception {
        HashMap<String, String> data  = CommonUtil.initBaseParams(accessToken);

        data.put("medal_id", String.valueOf(medalId));
        data.put("platform", "android");
        data.put("type", "1");
        data.put("version", "0");

        SignUtil.signatureWithoutReturn(data);

        return liveReq.wearMedal(data).getCode() == 0;
    }

    /**
     * 签到
     * @param accessToken
     * @return
     * @throws Exception
     */
    public String SignIn(String accessToken) throws Exception {
        HashMap<String, String> data  = CommonUtil.initBaseParams(accessToken);

        SignUtil.signatureWithoutReturn(data);

        return  liveReq.sign(data);
    }
    /**
     * 签到
     * @param accessToken
     * @return
     * @throws Exception
     */
    public boolean VerifyToken(String accessToken) throws Exception {
        HashMap<String, String> data  = CommonUtil.initBaseParams(accessToken);

        SignUtil.signatureWithoutReturn(data);

        return infoReq.verifyToken(data).get("code").equals(0);
    }
}
