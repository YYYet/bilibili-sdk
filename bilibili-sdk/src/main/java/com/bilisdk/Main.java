package com.bilisdk;

import com.bilisdk.common.options.ServerOptions;
import com.google.devtools.common.options.OptionsParser;

import static com.bilisdk.common.options.ServerCommond.checkAction;


public class Main
{

    public static void main( String[] args ) {
        OptionsParser parser = OptionsParser.newOptionsParser(ServerOptions.class);
        parser.parseAndExitUponError(args);
        ServerOptions options = parser.getOptions(ServerOptions.class);
        checkAction(parser, options);
    }
}
