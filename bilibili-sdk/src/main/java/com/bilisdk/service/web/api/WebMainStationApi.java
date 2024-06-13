package com.bilisdk.service.web.api;

import com.bilisdk.common.api.ApiBase;
import com.bilisdk.service.web.http.WebLoginReq;
import com.bilisdk.service.web.http.WebMainStationReq;
import com.dtflys.forest.Forest;

/**
 * @author zhuminc
 * @date 2024/3/9
 **/
public class WebMainStationApi extends ApiBase {
    public static WebMainStationReq webMainStationReq = Forest.client(WebMainStationReq.class);
}
