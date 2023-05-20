package com.bilisdk.service.tv.api;

import com.bilisdk.common.api.ApiBase;
import com.dtflys.forest.Forest;

import com.bilisdk.service.tv.http.TvLoginReq;

public abstract class TvLoginApi extends ApiBase {
    public TvLoginReq tvLoginReq = Forest.client(TvLoginReq.class);
}
