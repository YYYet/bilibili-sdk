package com.bilisdk.service.web.sdk;

import com.bilisdk.common.threadPool.ThreadPool;
import com.bilisdk.common.util.CommonUtil;
import com.bilisdk.service.web.api.WebLiveApi;

import java.util.HashMap;

public class WebLiveSdk extends WebLiveApi {

    public void entryRoom(){

        HashMap<String, String> map = new HashMap<>();
        map.put("roomId", "27588966");
        map.put("parent_id", "6");
        map.put("area_id", "283");
        map.put("area_id", "283");
//        liveReq.entryRoom();
    }

    /**
     * 监视器
     * @param roomId
     * @param webHook
     */
    public void LiveMonitor(int roomId, String webHook){
        ThreadPool.getThreadPool().executor(roomId, webHook);
    }
}
