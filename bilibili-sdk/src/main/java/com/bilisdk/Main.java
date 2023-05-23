package com.bilisdk;


import com.bilisdk.common.cache.CacheSingleton;
import com.bilisdk.common.netty.live.service.BilibiliBinaryFrameHandlerService;
import com.bilisdk.common.options.ServerOptions;
import com.bilisdk.common.threadPool.ThreadPool;
import com.google.common.cache.Cache;
import com.google.devtools.common.options.OptionsParser;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static com.bilisdk.common.options.ServerCommond.checkAction;


public class Main
{

    public static void main( String[] args ) throws UnsupportedEncodingException, NoSuchAlgorithmException, InterruptedException {
//        OptionsParser parser = OptionsParser.newOptionsParser(ServerOptions.class);
//        parser.parseAndExitUponError(args);
//        ServerOptions options = parser.getOptions(ServerOptions.class);
//        checkAction(parser, options);

//        BilibiliBinaryFrameHandlerService bilibiliBinaryFrameHandlerService = new BilibiliBinaryFrameHandlerService();
//        bilibiliBinaryFrameHandlerService.exec(26295814, "");
        ThreadPool.getThreadPool().executor(26295814, "");
        ThreadPool.getThreadPool().executor(22557294, ""); // CV


 
//        TvLiveSdk tvLiveSdk = new TvLiveSdk();
//        MedalList medalList = tvLiveSdk.GetOneMedalInfoByRoomId("f4a47b0526e13659d9d0aa6230adda42", "27588966");
//        System.out.println(JSONUtil.toJsonStr(medalList));

//        new WebLiveSdk()

    }
}
