package com.bilisdk.service.tv.api;

import com.bilisdk.common.api.ApiBase;
import com.dtflys.forest.Forest;
import com.bilisdk.service.tv.http.TvLiveReq;
import com.bilisdk.service.tv.http.TvInfoReq;

public abstract class TvLiveApi extends ApiBase {
    public TvLiveReq tvLiveReq = Forest.client(TvLiveReq.class);
    public TvInfoReq tvInfoReq = Forest.client(TvInfoReq.class);
}
