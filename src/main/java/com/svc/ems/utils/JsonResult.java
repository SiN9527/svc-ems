package com.svc.ems.utils;

/**
 * ClassName: com.sweetolive.exhibition_backend.util.JsonResult
 * Package: com.sweetolive.exhibition_backend.util
 * Description:
 *
 * @Author 郭庭安
 * @Create 2025/1/23 下午8:56
 * @Version 1.0
 */

import java.io.Serializable;

/**
 * Json格式的數據進行響應
 */
public class JsonResult<E> implements Serializable {
    /**
     * 狀態碼
     */
    private Integer state;
    /**
     * 描述信息
     */
    private String message;
    /**
     * 數據，因為不確定data數據類型，所以使用泛型，E代表佔位符號使用時可以替換類型
     */
    private E data;

    public JsonResult() {
    }

    public JsonResult(Integer state) {
        this.state = state;
    }

    public JsonResult(Throwable e) {
        this.message = e.getMessage();
    }

    public JsonResult(Integer state, E data) {
        this.state = state;
        this.data = data;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
