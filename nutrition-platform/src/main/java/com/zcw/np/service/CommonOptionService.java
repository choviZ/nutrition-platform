package com.zcw.np.service;

import java.util.List;
import java.util.Map;

/**
 * 公共选项服务接口
 *
 * @author zcw
 */
public interface CommonOptionService {

    /**
     * 获取活动水平选项列表
     *
     * @return 活动水平选项列表
     */
    List<Map<String, Object>> getActivityLevelOptions();

    /**
     * 获取健康目标选项列表
     *
     * @return 健康目标选项列表
     */
    List<Map<String, Object>> getHealthGoalOptions();

    /**
     * 根据活动水平值获取活动系数
     *
     * @param activityLevel 活动水平值
     * @return 活动系数
     */
    Double getActivityFactor(Integer activityLevel);

    /**
     * 根据健康目标值获取热量调整系数
     *
     * @param healthGoal 健康目标值
     * @return 热量调整系数
     */
    Double getCalorieAdjustmentFactor(Integer healthGoal);

    /**
     * 根据活动水平值获取描述
     *
     * @param activityLevel 活动水平值
     * @return 活动水平描述
     */
    String getActivityLevelDescription(Integer activityLevel);

    /**
     * 根据健康目标值获取描述
     *
     * @param healthGoal 健康目标值
     * @return 健康目标描述
     */
    String getHealthGoalDescription(Integer healthGoal);
}