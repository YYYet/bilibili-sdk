package com.bilisdk;

import com.bilisdk.common.options.ServerOptions;
import com.google.devtools.common.options.OptionsParser;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import static com.bilisdk.common.options.ServerCommond.checkAction;


public class Main
{

    public static void main( String[] args ) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        OptionsParser parser = OptionsParser.newOptionsParser(ServerOptions.class);
        parser.parseAndExitUponError(args);
        ServerOptions options = parser.getOptions(ServerOptions.class);
        checkAction(parser, options);
//        TvLiveSdk tvLiveSdk = new TvLiveSdk();
//        MedalList medalList = tvLiveSdk.GetOneMedalInfoByRoomId("f4a47b0526e13659d9d0aa6230adda42", "27588966");
//        System.out.println(JSONUtil.toJsonStr(medalList));

//        new WebLiveSdk()

    }
}
