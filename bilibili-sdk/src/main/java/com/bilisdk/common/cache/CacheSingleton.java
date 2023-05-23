package com.bilisdk.common.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.EventLoopGroup;

public class CacheSingleton {
    private CacheSingleton(){
    }
    public static enum SingletonEnum {
        SINGLETON;
        private Cache<Long, EventLoopGroup> instance = null;
        private SingletonEnum(){
            instance = CacheBuilder.newBuilder().build();;
        }
        public Cache<Long, EventLoopGroup> getInstance(){
            return instance;
        }
    }
}
