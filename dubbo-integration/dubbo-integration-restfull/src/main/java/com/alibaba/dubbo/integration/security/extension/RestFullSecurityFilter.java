package com.alibaba.dubbo.integration.security.extension;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.integration.security.exception.RestFullSecurityException;
import org.jboss.resteasy.core.MethodInjectorImpl;
import org.jboss.resteasy.core.ResourceMethodInvoker;
import org.jboss.resteasy.core.interception.jaxrs.PostMatchContainerRequestContext;
import org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext;

import javax.ws.rs.CookieParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MultivaluedMap;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * app_key : qazWSXedcRFVtgbYHNujm
 * app_secret : 01ebd161c3f8fdc8   --md5_16x(app_key)
 * access_token : 98fa2528887e4f52431e9a18c2333824  --md5_32x(app_secret_yyyymmddhhsssss)
 * Created by ouyang on 2017/6/27.
 */
public class RestFullSecurityFilter implements ContainerRequestFilter{

    private final String app_key_name = "app_key";
    private final String app_key_value = "qazWSXedcRFVtgbYHNujm";
    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {

        //dubbox rest 跨域问题处理
        if ("POST,GET".contains(containerRequestContext.getMethod())) {
            containerRequestContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            containerRequestContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type,x-requested-with,Authorization,Access-Control-Allow-Origin");
            containerRequestContext.getHeaders().add("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
            containerRequestContext.getHeaders().add("Access-Control-Max-Age" ,"360");
        }
        //如果方法上有@HeaderParam和@CookieParam注解,表示需要认证
        if(containerRequestContext instanceof PostMatchContainerRequestContext){
            PostMatchContainerRequestContext matchContainerRequestContext = (PostMatchContainerRequestContext)containerRequestContext;
            Annotation[][] methodAnnotations = matchContainerRequestContext.getResourceMethod().getMethod().getParameterAnnotations();
            boolean isSecurity = false;
            for (Annotation[] annotations : methodAnnotations) {
                if(annotations[0] instanceof HeaderParam || annotations[0] instanceof CookieParam){
                    isSecurity = true;
                    break;
                }
            }
            if(isSecurity){
                boolean isHasAppKey = false;
                //Cookie认证
                Map<String, Cookie> cookies = containerRequestContext.getCookies();
                Cookie appKeyCookie = cookies.get(app_key_name);
                if(appKeyCookie!=null){
                    String appKeyValue = appKeyCookie.getValue();
                    if(app_key_value.equals(appKeyValue)){
                        isHasAppKey = true;
                    }
                }
                //Header认证
                MultivaluedMap<String, String> headers = containerRequestContext.getHeaders();
                String appKeyHeader = headers.getFirst(app_key_name);
                if(!StringUtils.isBlank(appKeyHeader)){
                    if(app_key_value.equals(appKeyHeader)){
                        isHasAppKey = true;
                    }
                }
                if(!isHasAppKey){
                    Map<String, String> dataMap = new HashMap<String, String>();
                    RestFullSecurityException.StatusEntity authenticationError = new RestFullSecurityException.StatusEntity(RestFullSecurityException.Status.AUTHENTICATION_ERROR);
                    //query param
                    MultivaluedMap<String, String> queryParam = containerRequestContext.getUriInfo().getQueryParameters();
                    Iterator<String> iterator = queryParam.keySet().iterator();
                    while (iterator.hasNext()){
                        String key = iterator.next();
                        String value = queryParam.getFirst(key);
                        dataMap.put(key, value);
                    }
                    authenticationError.setRequestData(dataMap);
                    throw new RestFullSecurityException(JSON.json(authenticationError));
                }
            }
        }
    }
}
