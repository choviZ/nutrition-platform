package com.zcw.np.service.impl;

import com.zcw.np.model.enums.ActivityLevelEnum;
import com.zcw.np.model.enums.HealthGoalEnum;
import com.zcw.np.service.CommonOptionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公共选项服务实现类
 *
 * @author zcw
 */
@Service
public class CommonOptionServiceImpl implements CommonOptionService {

    @Override
    public List<Map<String, Object>> getActivityLevelOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        for (ActivityLevelEnum activityLevel : ActivityLevelEnum.values()) {
            Map<String, Object> option = new HashMap<>();
            option.put("value", activityLevel.getValue());
            option.put("label", activityLevel.getLabel());
            option.put("description", activityLevel.getDescription());
            option.put("activityFactor", activityLevel.getActivityFactor());
            options.add(option);
        }
        
        return options;
    }

    @Override
    public List<Map<String, Object>> getHealthGoalOptions() {
        List<Map<String, Object>> options = new ArrayList<>();
        
        for (HealthGoalEnum healthGoal : HealthGoalEnum.values()) {
            Map<String, Object> option = new HashMap<>();
            option.put("value", healthGoal.getValue());
            option.put("label", healthGoal.getLabel());
            option.put("description", healthGoal.getDescription());
            option.put("calorieAdjustmentFactor", healthGoal.getCalorieAdjustmentFactor());
            options.add(option);
        }
        
        return options;
    }

    @Override
    public Double getActivityFactor(Integer activityLevel) {
        return ActivityLevelEnum.getActivityFactorByValue(activityLevel);
    }

    @Override
    public Double getCalorieAdjustmentFactor(Integer healthGoal) {
        return HealthGoalEnum.getCalorieAdjustmentFactorByValue(healthGoal);
    }

    @Override
    public String getActivityLevelDescription(Integer activityLevel) {
        return ActivityLevelEnum.getDescriptionByValue(activityLevel);
    }

    @Override
    public String getHealthGoalDescription(Integer healthGoal) {
        return HealthGoalEnum.getDescriptionByValue(healthGoal);
    }
}