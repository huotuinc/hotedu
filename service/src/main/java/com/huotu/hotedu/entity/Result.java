package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shiliting on 2015/6/25.
 * 该类是结果对象
 * @author shiliting
 */
public  class Result implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;
    private int status;//状态信息
    private String message;//消息信息
    private Object body;//主体

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
