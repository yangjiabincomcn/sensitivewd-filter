package cn.modo.sensitive.util;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


//@JsonSerialize
//保证序列化json的时候,如果是null的对象,key也会消失
public class ResponseMsg<T> implements Serializable {

    private int status;
    private String msg;
    private T data;

    private ResponseMsg(int status) {
        this.status = status;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    private ResponseMsg(int status, T data) {
        this.status = status;
        this.data = data;
    }

    private ResponseMsg(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    private ResponseMsg(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    @JsonIgnore
    //使之不在json序列化结果当中
    public boolean isSuccess() {
        return this.status == cn.modo.sensitive.util.ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }


    public static <T> cn.modo.sensitive.util.ResponseMsg<T> success() {
        return new cn.modo.sensitive.util.ResponseMsg<T>(cn.modo.sensitive.util.ResponseCode.SUCCESS.getCode());
    }

    public static <T> cn.modo.sensitive.util.ResponseMsg<T> successMsg(String msg) {
        return new cn.modo.sensitive.util.ResponseMsg<T>(cn.modo.sensitive.util.ResponseCode.SUCCESS.getCode(), msg);
    }

    public static <T> cn.modo.sensitive.util.ResponseMsg<T> successData(T data) {
        return new cn.modo.sensitive.util.ResponseMsg<T>(cn.modo.sensitive.util.ResponseCode.SUCCESS.getCode(), data);
    }

    public static <T> cn.modo.sensitive.util.ResponseMsg<T> success(String msg, T data) {
        return new cn.modo.sensitive.util.ResponseMsg<T>(cn.modo.sensitive.util.ResponseCode.SUCCESS.getCode(), msg, data);
    }


    public static <T> cn.modo.sensitive.util.ResponseMsg<T> error() {
        return new cn.modo.sensitive.util.ResponseMsg<T>(cn.modo.sensitive.util.ResponseCode.ERROR.getCode(), cn.modo.sensitive.util.ResponseCode.ERROR.getDesc());
    }


    public static <T> cn.modo.sensitive.util.ResponseMsg<T> error(String errorMessage) {
        return new cn.modo.sensitive.util.ResponseMsg<T>(cn.modo.sensitive.util.ResponseCode.ERROR.getCode(), errorMessage);
    }

    public static <T> cn.modo.sensitive.util.ResponseMsg<T> errorTokenLose(String errorMessage) {
        return new cn.modo.sensitive.util.ResponseMsg<T>(cn.modo.sensitive.util.ResponseCode.TOKEN_LOSE.getCode(), errorMessage);
    }

    public static <T> cn.modo.sensitive.util.ResponseMsg<T> error(int errorCode, String errorMessage) {
        return new cn.modo.sensitive.util.ResponseMsg<T>(errorCode, errorMessage);
    }


}
