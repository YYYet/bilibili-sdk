package com.bilisdk.service.tv.api;

import com.dtflys.forest.Forest;
import com.bilisdk.service.tv.http.LiveReq;
import com.bilisdk.service.tv.http.InfoReq;

public abstract class LiveApi extends ApiBase{
    public LiveReq liveReq = Forest.client(LiveReq.class);
    public InfoReq infoReq = Forest.client(InfoReq.class);
}
