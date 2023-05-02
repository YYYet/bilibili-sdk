package com.bilisdk.service.tv.sdk;

import com.bilisdk.common.util.CommonUtil;
import com.bilisdk.common.util.SignUtil;
import com.bilisdk.service.tv.entity.resp.applycaptchainfo.ApplyCaptchaInfoResp;
import com.bilisdk.service.tv.entity.resp.qrcodeInfo.QRcodeInfoResp;
import com.bilisdk.service.tv.api.LoginApi;

import java.util.HashMap;

public class LoginSdk extends LoginApi {
//    final static String QRCODE_URL = "https://passport.bilibili.com/x/passport-tv-login/qrcode/auth_code";
    final static String QRCODE_URL = "https://passport.snm0516.aisee.tv/x/passport-tv-login/qrcode/auth_code";
    final static String WEB_QRCODE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate";
    final static String WEB_SCAN_QRCODE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll";
    final static String SCAN_QRCODE_URL = "https://passport.snm0516.aisee.tv/x/passport-tv-login/qrcode/poll";
    final static String VERIFY_QRCODE_URL = "https://passport.bilibili.com/x/passport-tv-login/qrcode/poll";
    final static String SEND_SMS_URL = "https://passport.bilibili.com/x/passport-login/sms/send";
    final static String APPLY_CAPTCHA_URL = "https://passport.bilibili.com/x/passport-login/captcha?source=main_web";



    public ApplyCaptchaInfoResp applyCaptcha(){
//       return Forest.get(APPLY_CAPTCHA_URL).execute(ApplyCaptchaInfoResp.class);
       return loginReq.applyCaptcha();
    }

//    public static void sendSms(String phoneNumber, String countryCode) throws Exception {
//        HashMap<String, Object> data = new HashMap<String, Object>();
//        data.put("appkey", BaseConstant.appTvkey);
//        data.put("actionKey",BaseConstant.actionKey);
//        data.put("build", 6510400);
//        data.put("channel", "bili");
//        data.put("cid", countryCode);
//        data.put("device", "phone");
//        data.put("mobi_app", "android");
//        data.put("platform", "android");
//        data.put("tel", "+"+phoneNumber);
//        data.put("ts", CommonUtil.getTimeStamps());
//        SignUtil.signatureByAndroidWithoutReturn(data);
//
//        System.out.println(Forest.post(SEND_SMS_URL).contentTypeMultipartFormData().addBody(data).execute(String.class));
//
//    }

    public QRcodeInfoResp getQRcode() throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("local_id", "0");
        data.put("ts", CommonUtil.getTimeStamps());
//        data.put("appkey", BaseConstant.appTvkey);
        HashMap<String, String> signature = SignUtil.signature(data);

       return loginReq.getQRcode("multipart/form-data",signature);

    }

    public void verifyQRcode(String authCode) throws Exception {
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("local_id", "0");
            data.put("ts", CommonUtil.getTimeStamps());
            data.put("auth_code", authCode);
            HashMap<String, String> signature = SignUtil.signature(data);
            loginReq.verifyQRcode("multipart/form-data",signature);
//            System.out.println(Forest.post(VERIFY_QRCODE_URL).contentTypeMultipartFormData().addBody(signature).execute(String.class));
            Thread.sleep(5000);
        }

    }
}
