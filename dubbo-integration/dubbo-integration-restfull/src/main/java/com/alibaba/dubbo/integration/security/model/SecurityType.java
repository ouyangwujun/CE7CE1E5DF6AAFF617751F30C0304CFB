package com.alibaba.dubbo.integration.security.model;

import javax.ws.rs.core.MediaType;

/**
 * 认证参数key
 * Created by PUZE81 on 2017/6/29.
 */
public class SecurityType {
    /**
     * App Key
     */
    public static final String APP_KEY = "app_key";
    /**
     * token
     */
    public static final String TOKEN = "x-csrf-token";

}
