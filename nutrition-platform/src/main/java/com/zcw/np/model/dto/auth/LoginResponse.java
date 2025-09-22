package com.zcw.np.model.dto.auth;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 登录响应DTO
 *
 * @author zcw
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "登录响应")
public class LoginResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "JWT Token", example = "eyJhbGciOiJIUzUxMiJ9...")
    private String token;

    @ApiModelProperty(value = "Token类型", example = "Bearer")
    private String tokenType = "Bearer";

    @ApiModelProperty(value = "用户ID", example = "1")
    private Long userId;

    @ApiModelProperty(value = "用户名", example = "admin")
    private String username;

    @ApiModelProperty(value = "邮箱", example = "admin@example.com")
    private String email;

    @ApiModelProperty(value = "手机号", example = "13800138000")
    private String phone;

    @ApiModelProperty(value = "用户角色", example = "admin")
    private String userRole;

    @ApiModelProperty(value = "Token过期时间（毫秒）", example = "86400000")
    private Long expiresIn;
}