package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.List;

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
    private int error;
    private String url;
    private String moveup_dir_path;
    private String current_dir_path;
    private String current_url;
    private int total_count;
    private List<Hashtable> file_list;

    public String getMoveup_dir_path() {
        return moveup_dir_path;
    }

    public void setMoveup_dir_path(String moveup_dir_path) {
        this.moveup_dir_path = moveup_dir_path;
    }

    public String getCurrent_dir_path() {
        return current_dir_path;
    }

    public void setCurrent_dir_path(String current_dir_path) {
        this.current_dir_path = current_dir_path;
    }

    public String getCurrent_url() {
        return current_url;
    }

    public void setCurrent_url(String current_url) {
        this.current_url = current_url;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public List<Hashtable> getFile_list() {
        return file_list;
    }

    public void setFile_list(List<Hashtable> file_list) {
        this.file_list = file_list;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

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
