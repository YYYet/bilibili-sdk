package com.bilisdk.service.tv.entity.resp.qrcodeInfo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class QRcodeInfoResp {
    private int code;
    private String message;
    private int ttl;
    private QRcodeInfo data;
}