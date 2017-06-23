package com.alibaba.dubbo.integration.security.exception;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

/**
 * Created by ouyang on 2017/6/23.
 */
public class RestFullSecurityException extends RuntimeException {

    private static final long serialVersionUID = 6151558657743342230L;

    public RestFullSecurityException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestFullSecurityException(Status status) {
        this(status.getReasonPhrase(), (Throwable)null);
    }

    public RestFullSecurityException() {
        this(Status.AUTHENTICATION_ERROR);
    }

    public static enum Status{
        AUTHENTICATION_ERROR(1000000, "Authentication error");

        private final int code;
        private final String reason;

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

    public static class StatusEntity{
        public StatusEntity(Status status){
            this.code = status.code;
            this.reason = status.reason;
        }

        private  int code;

        private String reason;

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
    }
}
