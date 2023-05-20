package com.bilisdk.service.web.api;

import com.bilisdk.common.api.ApiBase;
import com.bilisdk.service.web.http.WebLiveReq;
import com.dtflys.forest.Forest;

public abstract class WebLiveApi extends ApiBase {
    public WebLiveReq liveReq = Forest.client(WebLiveReq.class);
}
