package org.example.DTO;

import lombok.Data;

@Data
public class Common {
    // 签名
    String sign;
    // 时间戳
    String ts;
    // key
    String appkey;
    // 验证标识
    String actionKey = "appKey";
    // 访问token
    String accessKey;
}
