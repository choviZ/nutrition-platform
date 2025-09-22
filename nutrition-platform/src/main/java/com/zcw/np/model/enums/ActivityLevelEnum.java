package com.zcw.np.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动水平枚举
 *
 * @author zcw
 */
@Getter
@AllArgsConstructor
public enum ActivityLevelEnum {

    /**
     * 久坐
     */
    SEDENTARY(1, "久坐", "很少或没有运动", 1.2),

    /**
     * 轻度活动
     */
    LIGHT(2, "轻度活动", "轻度运动/每周1-3天", 1.375),

    /**
     * 中度活动
     */
    MODERATE(3, "中度活动", "中度运动/每周3-5天", 1.55),

    /**
     * 重度活动
     */
    HEAVY(4, "重度活动", "重度运动/每周6-7天", 1.725);

    /**
     * 活动水平值
     */
    private final Integer value;

    /**
     * 活动水平标签
     */
    private final String label;

    /**
     * 活动水平描述
     */
    private final String description;

    /**
     * 活动系数（用于计算每日热量需求）
     */
    private final Double activityFactor;

    /**
     * 根据值获取枚举
     *
     * @param value 活动水平值
     * @return 活动水平枚举
     */
    public static ActivityLevelEnum getByValue(Integer value) {
        if (value == null) {
            return null;
        }
        for (ActivityLevelEnum activityLevel : values()) {
            if (activityLevel.getValue().equals(value)) {
                return activityLevel;
            }
        }
        return null;
    }

    /**
     * 根据值获取活动系数
     *
     * @param value 活动水平值
     * @return 活动系数
     */
    public static Double getActivityFactorByValue(Integer value) {
        ActivityLevelEnum activityLevel = getByValue(value);
        return activityLevel != null ? activityLevel.getActivityFactor() : 1.2;
    }

    /**
     * 根据值获取描述
     *
     * @param value 活动水平值
     * @return 描述
     */
    public static String getDescriptionByValue(Integer value) {
        ActivityLevelEnum activityLevel = getByValue(value);
        return activityLevel != null ? activityLevel.getDescription() : "未知";
    }

    /**
     * 根据值获取标签
     *
     * @param value 活动水平值
     * @return 标签
     */
    public static String getLabelByValue(Integer value) {
        ActivityLevelEnum activityLevel = getByValue(value);
        return activityLevel != null ? activityLevel.getLabel() : "未知";
    }
}