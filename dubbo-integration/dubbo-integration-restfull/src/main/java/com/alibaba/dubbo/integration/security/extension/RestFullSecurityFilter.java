package com.alibaba.dubbo.integration.security.extension;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.integration.security.exception.RestFullSecurityException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import java.io.IOException;
import java.util.Map;

/**
 * Created by PUZE81 on 2017/6/27.
 */
public class RestFullSecurityFilter implements ContainerRequestFilter{

    private final String app_key_name = "app_key";

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        //dubbox rest 跨域问题处理
        if (containerRequestContext.getMethod().equals("OPTIONS")) {
            containerRequestContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            containerRequestContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization,Access-Control-Allow-Origin");
            containerRequestContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            containerRequestContext.getHeaders().add("Access-Control-Max-Age" ,"360");
        }
        Map<String, Cookie> cookies = containerRequestContext.getCookies();
        Cookie appKeyCookie = cookies.get(app_key_name);
        boolean isCookieHasAppKey = false;
        if(appKeyCookie!=null){
            String appKeyValue = appKeyCookie.getValue();
            if("qazWSXedcRFVtgbYHNujm".equals(appKeyValue)){
                isCookieHasAppKey = true;
            }
        }
        if(!isCookieHasAppKey){
            RestFullSecurityException.StatusEntity<Object> authenticationError = new RestFullSecurityException.StatusEntity<Object>(RestFullSecurityException.Status.AUTHENTICATION_ERROR);
            Object obj = new Object();
            authenticationError.setData(obj);
            throw new RestFullSecurityException(authenticationError);
        }
    }
}
