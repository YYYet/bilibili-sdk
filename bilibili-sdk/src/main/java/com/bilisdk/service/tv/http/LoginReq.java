package com.bilisdk.service.tv.http;

import com.bilisdk.service.tv.entity.resp.verifyqrcodeinfo.VerifyQRcodeInfoResp;
import com.dtflys.forest.annotation.Body;
import com.dtflys.forest.annotation.Get;
import com.dtflys.forest.annotation.Header;
import com.dtflys.forest.annotation.Post;
import com.bilisdk.common.api.Api;
import com.bilisdk.service.tv.entity.resp.applycaptchainfo.ApplyCaptchaInfoResp;
import com.bilisdk.service.tv.entity.resp.qrcodeInfo.QRcodeInfoResp;

import java.util.Map;


public interface LoginReq {
    /**
     * 申请验证码
     * @return
     */
    @Get(
            url = Api.APPLY_CAPTCHA_URL,
            headers = {
                    "User-Agent: Mozilla/5.0 BiliDroid/6.73.1 (bbcallen@gmail.com) os/android model/Mi 10 Pro mobi_app/android build/6731100 channel/xiaomi innerVer/6731110 osVer/12 network/2",
                    "Accept: application/json",
                    "Accept-Language: en-US,en;q=0.5",
                    "Connection: keep-alive"
            }
    )
    ApplyCaptchaInfoResp applyCaptcha();

    /**
     * 获取图二维码
     * @param contentType
     * @param body
     * @return
     */
    @Post(
            url = Api.QRCODE_URL,
            headers = {
                    "User-Agent: Mozilla/5.0 BiliDroid/6.73.1 (bbcallen@gmail.com) os/android model/Mi 10 Pro mobi_app/android build/6731100 channel/xiaomi innerVer/6731110 osVer/12 network/2",
                    "Accept: application/json",
                    "Accept-Language: en-US,en;q=0.5",
                    "Connection: keep-alive"
            }
    )
    QRcodeInfoResp getQRcode(@Header(value = "Content-Type",defaultValue = "multipart/form-data") String contentType, @Body Map<String, String> body);

    /**
     * 获取二维码状态
     * @param contentType
     * @param body
     * @return
     */
    @Post(
            url = Api.VERIFY_QRCODE_URL,
            headers = {
                    "User-Agent: Mozilla/5.0 BiliDroid/6.73.1 (bbcallen@gmail.com) os/android model/Mi 10 Pro mobi_app/android build/6731100 channel/xiaomi innerVer/6731110 osVer/12 network/2",
                    "Accept: application/json",
                    "Accept-Language: en-US,en;q=0.5",
                    "Connection: keep-alive"
            }
    )
    VerifyQRcodeInfoResp verifyQRcode(@Header(value = "Content-Type",defaultValue = "multipart/form-data") String contentType, @Body Map<String, String> body);
}
