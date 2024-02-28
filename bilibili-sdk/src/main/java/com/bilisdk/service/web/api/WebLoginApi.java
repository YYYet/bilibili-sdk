package com.bilisdk.service.web.api;

import com.bilisdk.common.api.ApiBase;
import com.bilisdk.service.tv.http.TvLoginReq;
import com.bilisdk.service.web.http.WebLoginReq;
import com.dtflys.forest.Forest;

/**
 * @author zhuminc
 * @date 2024/2/28
 **/
public abstract class WebLoginApi extends ApiBase {
    public WebLoginReq webLoginReq = Forest.client(WebLoginReq.class);
}
