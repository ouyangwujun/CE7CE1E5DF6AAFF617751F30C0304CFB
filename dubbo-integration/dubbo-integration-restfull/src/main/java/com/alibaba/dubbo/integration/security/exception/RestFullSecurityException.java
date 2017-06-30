package com.alibaba.dubbo.integration.security.exception;

import com.alibaba.dubbo.common.json.JSON;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by ouyang on 2017/6/23.
 */
public class RestFullSecurityException extends RuntimeException {

    private static final long serialVersionUID = 6151558657743342230L;

    public RestFullSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestFullSecurityException() {
        this(new StatusEntity(Status.AUTHENTICATION_ERROR));
    }

    public RestFullSecurityException(StatusEntity statusEntity) {
        this(statusEntity.toString(), (Throwable)null);
    }

    public RestFullSecurityException(String jsonStatusEntity) {
        this(jsonStatusEntity, (Throwable)null);
    }

    public static enum Status{
        AUTHENTICATION_ERROR(1000000, "Authentication error");

        private int code;
        private String reason;

        private Status(int statusCode, String reasonPhrase) {
            this.code = statusCode;
            this.reason = reasonPhrase;
        }

        public int getStatusCode() {
            return this.code;
        }

        public String getReasonPhrase() {
            return this.toString();
        }

        public String toString() {
            return this.reason;
        }
    }

    public static class StatusEntity implements Serializable{

        public StatusEntity(Status status){
            this.code = status.code;
            this.reason = status.reason;
        }

        private  int code;

        private String reason;

        private Map<String, String> requestData;


        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public Map<String, String> getRequestData() {
            return requestData;
        }

        public void setRequestData(Map<String, String> requestData) {
            this.requestData = requestData;
        }
    }
}
