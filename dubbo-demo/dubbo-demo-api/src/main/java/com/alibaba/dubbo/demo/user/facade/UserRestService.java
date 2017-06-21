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
package com.alibaba.dubbo.demo.user.facade;

import com.alibaba.dubbo.demo.user.User;
import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.constraints.Min;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * This interface acts as some kind of service broker for the original UserService

 * Here we want to simulate the twitter/weibo rest api, e.g.
 *
 * http://localhost:8888/user/1.json
 * http://localhost:8888/user/1.xml
 *
 * @author lishen
 */
@Path("users")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Api("用户服务")
public interface UserRestService {

    /**
     * the request object is just used to test jax-rs injection.
     */
    @GET
    @Path("{id : \\d+}")
    @ApiOperation(value="通过主键获取用户信息")
    User getUser(@Min(value=1L, message="User ID must be greater than 1") Long id/*, HttpServletRequest request*/);

    @POST
    @Path("register")
    @ApiOperation(value="注册用户")
    RegistrationResult registerUser(User user);
}
