package com.zcw.np.model.dto.user;

import com.zcw.np.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.io.Serializable;

/**
 * 用户查询请求
 *
 * @author zcw
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名（支持模糊查询）
     */
    private String username;

    /**
     * 邮箱（支持模糊查询）
     */
    private String email;

    /**
     * 手机号（支持模糊查询）
     */
    private String phone;

    /**
     * 真实姓名（支持模糊查询）
     */
    private String realName;

    /**
     * 性别（0-女，1-男）
     */
    @Min(value = 0, message = "性别值必须为0或1")
    @Max(value = 1, message = "性别值必须为0或1")
    private Integer gender;

    /**
     * 最小年龄
     */
    @Min(value = 1, message = "最小年龄必须大于0")
    @Max(value = 150, message = "最小年龄不能超过150")
    private Integer minAge;

    /**
     * 最大年龄
     */
    @Min(value = 1, message = "最大年龄必须大于0")
    @Max(value = 150, message = "最大年龄不能超过150")
    private Integer maxAge;

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