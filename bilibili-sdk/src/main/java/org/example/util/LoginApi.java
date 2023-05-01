package org.example.util;

import com.dtflys.forest.Forest;
import org.example.resp.qrcodeInfo.QRcodeInfo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class LoginApi {
//    final static String QRCODE_URL = "https://passport.bilibili.com/x/passport-tv-login/qrcode/auth_code";
    final static String QRCODE_URL = "https://passport.snm0516.aisee.tv/x/passport-tv-login/qrcode/auth_code";
    final static String WEB_QRCODE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate";
    final static String WEB_SCAN_QRCODE_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll";
    final static String SCAN_QRCODE_URL = "https://passport.snm0516.aisee.tv/x/passport-tv-login/qrcode/poll";
    final static String VERIFY_QRCODE_URL = "https://passport.bilibili.com/x/passport-tv-login/qrcode/poll";
    final static String SEND_SMS_URL = "https://passport.bilibili.com/x/passport-login/sms/send";

    public static void sendSms(String phoneNumber, String countryCode) throws Exception {
//        "actionKey": "appkey",
//                "appkey": AppKeyStore::Android.app_key(),
//                "build": 6510400,
//                "channel": "bili",
//                "cid": country_code,
//                "device": "phone",
//                "mobi_app": "android",
//                "platform": "android",
//                // "platform": "pc",
//                "tel": phone_number,
//                "ts": SystemTime::now().duration_since(UNIX_EPOCH).unwrap().as_secs(),
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("appkey",CST.appTvkey);
        data.put("actionKey",CST.actionKey);
        data.put("build", 6510400);
        data.put("channel", "bili");
        data.put("cid", "86");
        data.put("device", "phone");
        data.put("mobi_app", "android");
        data.put("platform", "android");
        data.put("tel", "+"+phoneNumber);
        data.put("ts", CommonUtil.getTimeStamps());
        SignUtil.signatureByAndroidWithoutReturn(data);
        System.out.println(data);

        System.out.println(Forest.post(SEND_SMS_URL).contentTypeMultipartFormData().addBody(data).execute(String.class));

    }

    public static QRcodeInfo getQRcode() throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("local_id", "0");
        data.put("ts", CommonUtil.getTimeStamps());
//        data.put("appkey", CST.appTvkey);
        HashMap<String, String> signature = SignUtil.signature(data);
        System.out.println(signature);

       return Forest.post(QRCODE_URL).contentTypeMultipartFormData().addBody(signature).execute(QRcodeInfo.class);

    }

    public static void verifyQRcode(String authCode) throws Exception {
        for (int i = 0; i < 10; i++) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("local_id", "0");
            data.put("ts", CommonUtil.getTimeStamps());
            data.put("auth_code", authCode);
            HashMap<String, String> signature = SignUtil.signature(data);
            System.out.println(signature);

            System.out.println(Forest.post(VERIFY_QRCODE_URL).contentTypeMultipartFormData().addBody(signature).execute(String.class));
            Thread.sleep(5000);
        }

    }
}
