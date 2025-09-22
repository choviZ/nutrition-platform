package com.zcw.springbootinit.model.dto.user;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户更新请求
 *
 * @author zcw
 */
@Data
public class UserUpdateRequest implements Serializable {

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正数")
    private Long userId;

    /**
     * 用户名
     */
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 真实姓名
     */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    private String realName;

    /**
     * 性别（0-女，1-男）
     */
    @Min(value = 0, message = "性别值必须为0或1")
    @Max(value = 1, message = "性别值必须为0或1")
    private Integer gender;

    /**
     * 年龄
     */
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄不能超过150")
    private Integer age;

    /**
     * 身高（cm）
     */
    @DecimalMin(value = "50.0", message = "身高不能小于50cm")
    @DecimalMax(value = "300.0", message = "身高不能超过300cm")
    private BigDecimal height;

    /**
     * 体重（kg）
     */
    @DecimalMin(value = "10.0", message = "体重不能小于10kg")
    @DecimalMax(value = "500.0", message = "体重不能超过500kg")
    private BigDecimal weight;

    /**
     * 健康目标（0-减重，1-增重，2-维持）
     */
    @Min(value = 0, message = "健康目标值必须为0、1或2")
    @Max(value = 2, message = "健康目标值必须为0、1或2")
    private Integer healthGoal;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Min(value = 0, message = "状态值必须为0或1")
    @Max(value = 1, message = "状态值必须为0或1")
    private Integer status;

    private static final long serialVersionUID = 1L;
}