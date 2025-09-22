package com.zcw.np.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 健康档案视图对象
 *
 * @author zcw
 */
@Data
public class HealthProfileVO implements Serializable {

    /**
     * 档案ID
     */
    private Long profileId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 慢性疾病史
     */
    private String chronicDiseases;

    /**
     * 过敏史
     */
    private String allergies;

    /**
     * 饮食偏好
     */
    private String dietaryPreferences;

    /**
     * 忌口食物
     */
    private String forbiddenFoods;

    /**
     * 运动习惯
     */
    private String exerciseHabits;

    /**
     * 活动水平：1-久坐，2-轻度，3-中度，4-重度
     */
    private Integer activityLevel;

    /**
     * 活动水平描述
     */
    private String activityLevelDesc;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}