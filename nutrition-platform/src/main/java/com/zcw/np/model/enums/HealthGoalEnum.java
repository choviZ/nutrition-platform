package com.zcw.np.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 健康目标枚举
 *
 * @author zcw
 */
@Getter
@AllArgsConstructor
public enum HealthGoalEnum {

    /**
     * 减肥
     */
    LOSE_WEIGHT(1, "减肥", "减少体重和体脂", -0.2),

    /**
     * 增肌
     */
    GAIN_MUSCLE(2, "增肌", "增加肌肉量", 0.15),

    /**
     * 维持健康
     */
    MAINTAIN_HEALTH(3, "维持健康", "保持当前体重和健康状态", 0.0);

    /**
     * 健康目标值
     */
    private final Integer value;

    /**
     * 健康目标标签
     */
    private final String label;

    /**
     * 健康目标描述
     */
    private final String description;

    /**
     * 热量调整系数（相对于基础热量需求的调整比例）
     */
    private final Double calorieAdjustmentFactor;

    /**
     * 根据值获取枚举
     *
     * @param value 健康目标值
     * @return 健康目标枚举
     */
    public static HealthGoalEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (HealthGoalEnum healthGoal : values()) {
            if (healthGoal.getValue().equals(value)) {
                return healthGoal;
            }
        }
        return null;
    }

    /**
     * 根据值获取热量调整系数
     *
     * @param value 健康目标值
     * @return 热量调整系数
     */
    public static Double getCalorieAdjustmentFactorByValue(Integer value) {
        HealthGoalEnum healthGoal = getByValue(value);
        return healthGoal != null ? healthGoal.getCalorieAdjustmentFactor() : 0.0;
    }

    /**
     * 根据值获取描述
     *
     * @param value 健康目标值
     * @return 描述
     */
    public static String getDescriptionByValue(Integer value) {
        HealthGoalEnum healthGoal = getByValue(value);
        return healthGoal != null ? healthGoal.getDescription() : "未知";
    }

    /**
     * 根据值获取标签
     *
     * @param value 健康目标值
     * @return 标签
     */
    public static String getLabelByValue(Integer value) {
        HealthGoalEnum healthGoal = getByValue(value);
        return healthGoal != null ? healthGoal.getLabel() : "未知";
    }
}