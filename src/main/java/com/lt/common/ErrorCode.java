package com.lt.common;

/**
 * 错误码
 *
 * @author teng
 */
public enum ErrorCode {

    /**
     * 错误码
     */
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "请求参数错误"),
    NOT_LOGIN_ERROR(40100, "未登录"),
    NO_AUTH_ERROR(40101, "无权限"),
    NOT_FOUND_ERROR(40400, "请求数据不存在"),
    FORBIDDEN_ERROR(40300, "禁止访问"),
    SYSTEM_ERROR(50000, "系统内部异常"),
    OPERATION_ERROR(50001, "操作失败"),
    SC_UNAUTHORIZED(50002, "非法token"),
    CAPTCHA_ERROR(50003, "验证码错误"),
    LOGIN_ERROR(50004, "账号或密码错误"),
    UPDATE_ERROR(50005, "修改失败"),

    UPLOAD_ERROR(50006, "上传文件失败");

    /**
     * 状态码
     */
    private final int code;

    /**
     * 信息
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
