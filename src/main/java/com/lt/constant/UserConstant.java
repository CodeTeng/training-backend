package com.lt.constant;

/**
 * 用户常量
 *
 * @author teng
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    String PASSWORD_SALT = "teng";

    //  region 权限

    /**
     * 默认权限-学员
     */
    String DEFAULT_ROLE = "user";

    /**
     * 超级管理员-拥有所有权限
     */
    String SUPER_ADMIN_ROLE = "super_admin";

    /**
     * 机构管理员
     */
    String INS_ADMIN_ROLE = "ins_admin";

    /**
     * 发布管理员
     */
    String PUBLISH_ADMIN_ROLE = "publish_admin";

    /**
     * 审核管理员
     */
    String AUDIT_ADMIN_ROLE = "audit_admin";

    // endregion
}
