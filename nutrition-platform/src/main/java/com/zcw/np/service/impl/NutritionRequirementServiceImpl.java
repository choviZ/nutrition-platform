package com.zcw.np.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcw.np.mapper.NutritionRequirementMapper;
import com.zcw.np.model.dto.nutrition.NutritionAssessmentRequest;
import com.zcw.np.model.dto.nutrition.NutritionAssessmentResponse;
import com.zcw.np.model.dto.nutrition.NutritionQueryRequest;
import com.zcw.np.model.entity.NutritionRequirement;
import com.zcw.np.service.CommonOptionService;
import com.zcw.np.service.NutritionRequirementService;
import com.zcw.np.utils.UserContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author zcw
* @description 针对表【nutrition_requirement(营养需求表)】的数据库操作Service实现
* @createDate 2025-09-22 15:22:42
*/
@Service
public class NutritionRequirementServiceImpl extends ServiceImpl<NutritionRequirementMapper, NutritionRequirement> implements NutritionRequirementService {

    @Autowired
    private CommonOptionService commonOptionService;

    /**
     * 营养需求评估
     */
    public NutritionAssessmentResponse assessNutritionRequirement(NutritionAssessmentRequest request) {
        // 获取当前用户ID
        Long userId = UserContextUtils.getCurrentUserId();
        // 计算BMR（基础代谢率）
        BigDecimal bmr = calculateBMR(request.getAge(), request.getGender(), 
                                     request.getHeight(), request.getWeight());
        // 计算每日热量需求
        BigDecimal dailyCalories = calculateDailyCalories(bmr, request.getActivityLevel(), 
                                                         request.getHealthGoal());
        // 计算各营养素需求
        NutritionRequirement requirement = calculateNutritionRequirements(
            userId, dailyCalories, request.getWeight(), request.getGender(), request.getAge());
        // 计算BMI和健康状态
        BigDecimal bmi = calculateBMI(request.getHeight(), request.getWeight());
        String bmiStatus = getBMIStatus(bmi);
        String healthAdvice = generateHealthAdvice(bmi, request.getHealthGoal());
        // 保存评估结果
        if (request.getSaveResult()) {
            requirement.setAssessmentDate(new Date());
            this.save(requirement);
        }
        
        // 构建响应
        NutritionAssessmentResponse response = new NutritionAssessmentResponse();
        BeanUtils.copyProperties(requirement, response);
        response.setBmr(bmr);
        response.setBmi(bmi);
        response.setBmiStatus(bmiStatus);
        response.setHealthAdvice(healthAdvice);
        
        return response;
    }
    
    /**
     * 查询营养需求记录
     */
    public List<NutritionAssessmentResponse> queryNutritionRequirements(NutritionQueryRequest request) {
        QueryWrapper<NutritionRequirement> queryWrapper = new QueryWrapper<>();
        // 构建查询条件
        if (request.getUserId() != null) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (request.getStartDate() != null) {
            queryWrapper.ge("assessment_date", request.getStartDate());
        }
        if (request.getEndDate() != null) {
            queryWrapper.le("assessment_date", request.getEndDate());
        }
        if (request.getMinCalories() != null) {
            queryWrapper.ge("daily_calories", request.getMinCalories());
        }
        if (request.getMaxCalories() != null) {
            queryWrapper.le("daily_calories", request.getMaxCalories());
        }
        
        queryWrapper.orderByDesc("assessment_date");
        
        List<NutritionRequirement> requirements = this.list(queryWrapper);
        
        return requirements.stream().map(requirement -> {
            NutritionAssessmentResponse response = new NutritionAssessmentResponse();
            BeanUtils.copyProperties(requirement, response);
            return response;
        }).collect(Collectors.toList());
    }
    
    /**
     * 计算BMR（基础代谢率）- 使用Mifflin-St Jeor公式
     */
    private BigDecimal calculateBMR(Integer age, Integer gender, BigDecimal height, BigDecimal weight) {
        BigDecimal bmr;
        
        if (gender == 1) {
            // 男性
            bmr = weight
                    .multiply(BigDecimal.valueOf(10))
                    .add(height.multiply(BigDecimal.valueOf(6.25)))
                    .subtract(BigDecimal.valueOf(age * 5))
                    .add(BigDecimal.valueOf(5));
        } else {
            // 女性
            bmr = weight.multiply(BigDecimal.valueOf(10))
                    .add(height.multiply(BigDecimal.valueOf(6.25)))
                    .subtract(BigDecimal.valueOf(age * 5))
                    .subtract(BigDecimal.valueOf(161));
        }
        
        return bmr.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算每日热量需求
     */
    private BigDecimal calculateDailyCalories(BigDecimal bmr, Integer activityLevel, Integer healthGoal) {
        // 使用枚举获取活动系数
        Double activityFactor = commonOptionService.getActivityFactor(activityLevel);
        BigDecimal dailyCalories = bmr.multiply(BigDecimal.valueOf(activityFactor));
        
        // 使用枚举获取健康目标调整系数
        Double adjustmentFactor = commonOptionService.getCalorieAdjustmentFactor(healthGoal);
        if (adjustmentFactor != 0.0) {
            BigDecimal adjustment = dailyCalories.multiply(BigDecimal.valueOf(adjustmentFactor));
            dailyCalories = dailyCalories.add(adjustment);
        }
        
        return dailyCalories.setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 计算各营养素需求
     */
    private NutritionRequirement calculateNutritionRequirements(Long userId, BigDecimal dailyCalories, 
                                                              BigDecimal weight, Integer gender, Integer age) {
        NutritionRequirement requirement = new NutritionRequirement();
        requirement.setUserId(userId);
        requirement.setDailyCalories(dailyCalories);
        
        // 蛋白质需求：每公斤体重1.2-2.0g
        BigDecimal proteinRequirement = weight.multiply(BigDecimal.valueOf(1.6))
                .setScale(2, RoundingMode.HALF_UP);
        requirement.setProtein(proteinRequirement);
        
        // 脂肪需求：总热量的20-35%
        BigDecimal fatRequirement = dailyCalories.multiply(BigDecimal.valueOf(0.25))
                .divide(BigDecimal.valueOf(9), 2, RoundingMode.HALF_UP);
        requirement.setFat(fatRequirement);
        
        // 碳水化合物需求：总热量的45-65%
        BigDecimal carbohydrateRequirement = dailyCalories.multiply(BigDecimal.valueOf(0.55))
                .divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
        requirement.setCarbohydrate(carbohydrateRequirement);
        
        // 纤维需求：每1000kcal需要14g
        BigDecimal fiberRequirement = dailyCalories.divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(14));
        requirement.setFiber(fiberRequirement);
        
        // 维生素需求（根据性别和年龄）
        requirement.setVitaminA(gender == 1 ? BigDecimal.valueOf(900) : BigDecimal.valueOf(700));
        requirement.setVitaminC(gender == 1 ? BigDecimal.valueOf(90) : BigDecimal.valueOf(75));
        
        // 矿物质需求
        requirement.setCalcium(BigDecimal.valueOf(1000));
        requirement.setIron(gender == 1 ? BigDecimal.valueOf(8) : BigDecimal.valueOf(18));
        
        return requirement;
    }
    
    /**
     * 计算BMI
     */
    private BigDecimal calculateBMI(BigDecimal height, BigDecimal weight) {
        BigDecimal heightInMeters = height.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return weight.divide(heightInMeters.multiply(heightInMeters), 2, RoundingMode.HALF_UP);
    }
    
    /**
     * 获取BMI状态
     */
    private String getBMIStatus(BigDecimal bmi) {
        if (bmi.compareTo(BigDecimal.valueOf(18.5)) < 0) {
            return "偏瘦";
        } else if (bmi.compareTo(BigDecimal.valueOf(24)) < 0) {
            return "正常";
        } else if (bmi.compareTo(BigDecimal.valueOf(28)) < 0) {
            return "超重";
        } else {
            return "肥胖";
        }
    }
    
    /**
     * 生成健康建议
     */
    private String generateHealthAdvice(BigDecimal bmi, Integer healthGoal) {
        StringBuilder advice = new StringBuilder();
        
        // BMI建议
        String bmiStatus = getBMIStatus(bmi);
        switch (bmiStatus) {
            case "偏瘦":
                advice.append("您的体重偏轻，建议适当增加营养摄入，进行力量训练增加肌肉量。");
                break;
            case "正常":
                advice.append("您的体重在正常范围内，请保持均衡饮食和规律运动。");
                break;
            case "超重":
                advice.append("您的体重超重，建议控制热量摄入，增加有氧运动。");
                break;
            case "肥胖":
                advice.append("您的体重属于肥胖范围，建议咨询专业营养师制定减重计划。");
                break;
        }
        
        // 健康目标建议
        switch (healthGoal) {
            case 1: // 减肥
                advice.append(" 减肥期间建议多吃蔬菜水果，控制精制糖和高脂食物摄入。");
                break;
            case 2: // 增肌
                advice.append(" 增肌期间建议增加优质蛋白质摄入，配合力量训练。");
                break;
            case 3: // 维持健康
                advice.append(" 建议保持多样化饮食，定期进行体检。");
                break;
        }
        
        return advice.toString();
    }
}




