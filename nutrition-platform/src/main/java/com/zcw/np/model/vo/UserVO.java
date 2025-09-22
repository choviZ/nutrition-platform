package com.zcw.np.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户视图对象
 *
 * @author zcw
 */
@Data
public class UserVO implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别（0-女，1-男）
     */
    private Integer gender;

    /**
     * 性别描述
     */
    private String genderDesc;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 身高（cm）
     */
    private BigDecimal height;

    /**
     * 体重（kg）
     */
    private BigDecimal weight;

    /**
     * 健康目标（0-减重，1-增重，2-维持）
     */
    private Integer healthGoal;

    /**
     * 健康目标描述
     */
    private String healthGoalDesc;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 状态（0-禁用，1-启用）
     */
    private Integer status;

    /**
     * 状态描述
     */
    private String statusDesc;

    private static final long serialVersionUID = 1L;
}