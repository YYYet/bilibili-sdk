package com.bilisdk.service.tv.entity.resp.heartbeatinfo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class HeartBeatInfoResp {
    private long code;
    private String message;
    private int ttl;
    private HeartBeatInfo data;
}