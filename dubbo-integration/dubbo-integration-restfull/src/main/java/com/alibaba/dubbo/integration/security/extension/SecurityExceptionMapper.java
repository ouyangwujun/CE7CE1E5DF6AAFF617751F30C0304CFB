/**
 * Copyright 1999-2014 dangdang.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.dubbo.integration.security.extension;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.dubbo.integration.security.exception.RestFullSecurityException;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * 认证异常
 * @author ouyang
 */
public class SecurityExceptionMapper implements ExceptionMapper<RestFullSecurityException> {
    @Override
    public Response toResponse(RestFullSecurityException e) {
        System.out.println("Exception mapper successfully got an SecurityExceptionMapper: " + e);
        System.out.println("Client IP is " + RpcContext.getContext().getRemoteAddressString());
        RestFullSecurityException.Status status = RestFullSecurityException.Status.AUTHENTICATION_ERROR;
        String jsonMessage = e.getMessage();
        return Response.status(status.getStatusCode()).entity(jsonMessage).type(ContentType.TEXT_PLAIN_UTF_8).build();
    }
}
