package com.zcw.np.model.dto.health;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 健康档案更新请求
 *
 * @author zcw
 */
@Data
public class HealthProfileUpdateRequest implements Serializable {

    /**
     * 档案ID
     */
    @NotNull(message = "档案ID不能为空")
    private Long profileId;

    /**
     * 慢性疾病史
     */
    @Size(max = 1000, message = "慢性疾病史不能超过1000个字符")
    private String chronicDiseases;

    /**
     * 过敏史
     */
    @Size(max = 1000, message = "过敏史不能超过1000个字符")
    private String allergies;

    /**
     * 饮食偏好
     */
    @Size(max = 1000, message = "饮食偏好不能超过1000个字符")
    private String dietaryPreferences;

    /**
     * 忌口食物
     */
    @Size(max = 1000, message = "忌口食物不能超过1000个字符")
    private String forbiddenFoods;

    /**
     * 运动习惯
     */
    @Size(max = 1000, message = "运动习惯不能超过1000个字符")
    private String exerciseHabits;

    /**
     * 活动水平：1-久坐，2-轻度，3-中度，4-重度
     */
    @Min(value = 1, message = "活动水平必须在1-4之间")
    @Max(value = 4, message = "活动水平必须在1-4之间")
    private Integer activityLevel;

    private static final long serialVersionUID = 1L;
}