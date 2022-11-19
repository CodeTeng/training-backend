package com.lt.modules.sys.model.dto.organ;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @description: 添加机构请求体
 * @author: ~Teng~
 * @date: 2022/11/19 19:13
 */
@Data
public class OrganAddRequest implements Serializable {
    /**
     * 机构名称
     */
    @NotBlank(message = "机构名称不能为空")
    private String name;

    /**
     * 类型名称
     */
    @NotBlank(message = "机构类型名称不能为空")
    private String typeName;

    /**
     * 机构logo图片地址
     */
    private String organLogo;

    /**
     * 机构联系电话
     */
    @NotBlank(message = "联系电话不能为空")
    private String phone;

    /**
     * 机构负责人
     */
    @NotBlank(message = "负责人不能为空")
    private String chargePerson;

    /**
     * 邮箱
     */
    @Email(message = "不符合邮箱格式")
    private String email;

    /**
     * 机构简介
     */
    private String intro;

    private static final long serialVersionUID = 1L;
}
