package com.bilisdk.service.tv.api;

import com.dtflys.forest.Forest;

import com.bilisdk.service.tv.http.LoginReq;

public abstract class LoginApi extends ApiBase{
    public LoginReq loginReq = Forest.client(LoginReq.class);
}
