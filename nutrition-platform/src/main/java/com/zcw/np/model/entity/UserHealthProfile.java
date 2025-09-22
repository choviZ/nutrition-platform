package com.zcw.np.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户健康档案表
 * @TableName user_health_profile
 */
@TableName(value ="user_health_profile")
@Data
public class UserHealthProfile implements Serializable {
    /**
     * 档案ID
     */
    @TableId(value = "profile_id", type = IdType.ASSIGN_ID)
    private Long profileId;

    /**
     * 用户ID
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 慢性疾病史
     */
    @TableField(value = "chronic_diseases")
    private String chronicDiseases;

    /**
     * 过敏史
     */
    @TableField(value = "allergies")
    private String allergies;

    /**
     * 饮食偏好
     */
    @TableField(value = "dietary_preferences")
    private String dietaryPreferences;

    /**
     * 忌口食物
     */
    @TableField(value = "forbidden_foods")
    private String forbiddenFoods;

    /**
     * 运动习惯
     */
    @TableField(value = "exercise_habits")
    private String exerciseHabits;

    /**
     * 活动水平：1-久坐，2-轻度，3-中度，4-重度
     */
    @TableField(value = "activity_level")
    private Integer activityLevel;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}