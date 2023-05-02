package com.bilisdk;


import com.bilisdk.common.constant.BaseConstant;
import com.bilisdk.service.tv.entity.resp.medalInfo.MedalInfoResp;
import com.bilisdk.service.tv.entity.resp.medalInfo.MedalList;
import com.bilisdk.service.tv.entity.resp.medalInfo.SpecialList;
import com.bilisdk.service.tv.sdk.LiveSdk;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;


public class Main
{
    public static void main( String[] args ) throws Exception {


        LiveSdk liveSdk = new LiveSdk();
        //  获取个人信息
//        TestGetUserInfo(liveSdk);

        // 测试获取所有粉丝牌
        TestAllMedal(liveSdk);

//        String[] UUIDS = new String[]{UUID.randomUUID().toString(), UUID.randomUUID().toString()};


    }
    public static void TestAllMedal(LiveSdk liveSdk) throws UnsupportedEncodingException, NoSuchAlgorithmException {
//        // 获取所有粉丝牌
//        MedalInfoResp medalInfoResp = liveSdk.GetMedalInfo(BaseConstant.accessKey);
//        for (MedalList medal : medalInfoResp.getData().getList()) {
//            System.out.println("粉丝牌名称 "+medal.getMedal().getMedal_name()+" 主播名称 "+medal.getAnchor_info().getNick_name()+" targetId "+medal.getMedal().getTarget_id()+" medalId "+medal.getMedal().getMedal_id());
//        }
//        for (SpecialList medal : medalInfoResp.getData().getSpecial_list()) {
//            System.out.println("粉丝牌名称 "+medal.getMedal().getMedal_name()+" 主播名称 "+medal.getAnchor_info().getNick_name()+" targetId "+medal.getMedal().getTarget_id()+" medalId "+medal.getMedal().getMedal_id());
//        }
    }
    public static void TestGetUserInfo(LiveSdk liveSdk) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // 获取个人信息
//        System.out.println(liveSdk.GetUserInfo(BaseConstant.accessKey).getData().getMedal().toString());
    }
}
