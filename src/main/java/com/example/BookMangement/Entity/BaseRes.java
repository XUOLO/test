package com.example.BookMangement.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * BaseRes
 *
 * @author xuanl
 * @version 01-00
 * @since 5/29/2024
 */

@Getter
@Setter
@Slf4j
@Data
@AllArgsConstructor
public class BaseRes {
    private String status;
    private int code;
    private String message;
    private Object data;

    public BaseRes() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
