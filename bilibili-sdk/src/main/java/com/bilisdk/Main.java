package com.bilisdk;


import com.bilisdk.common.cache.CacheSingleton;
import com.bilisdk.common.netty.live.service.BilibiliBinaryFrameHandlerService;
import com.bilisdk.common.options.ServerOptions;
import com.bilisdk.common.threadPool.ThreadPool;
import com.bilisdk.service.tv.sdk.TvLiveSdk;
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

        TvLiveSdk tvLiveSdk = new TvLiveSdk();
        tvLiveSdk.GetUserInfoWithIpByAndroid("2c38761b8135a545a60339dee09fdc41", "22148666");
    }
}
