package com.zcw.np.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.model.dto.dietplan.DietPlanAdjustRequest;
import com.zcw.np.model.dto.dietplan.DietPlanGenerateRequest;
import com.zcw.np.model.entity.DietPlan;
import com.zcw.np.model.entity.FoodNutrition;
import com.zcw.np.model.entity.NutritionRequirement;
import com.zcw.np.model.vo.DietPlanVO;
import com.zcw.np.service.DietPlanService;
import com.zcw.np.service.FoodNutritionService;
import com.zcw.np.service.NutritionRequirementService;
import com.zcw.np.mapper.DietPlanMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author zcw
* @description 针对表【diet_plan(饮食方案表)】的数据库操作Service实现
* @createDate 2025-09-22 16:22:37
*/
@Service
@Slf4j
public class DietPlanServiceImpl extends ServiceImpl<DietPlanMapper, DietPlan> implements DietPlanService{

    @Resource
    private NutritionRequirementService nutritionRequirementService;

    @Resource
    private FoodNutritionService foodNutritionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional
    public DietPlanVO generateDietPlan(DietPlanGenerateRequest request) {
        // 1. 获取营养需求
        NutritionRequirement requirement = nutritionRequirementService.getById(request.getRequirementId());
        if (requirement == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "营养需求记录不存在");
        }

        // 2. 解析餐次分配比例
        Map<String, BigDecimal> mealRatios = parseMealRatio(request.getMealRatio());

        // 3. 生成各餐次方案 - 考虑季节性和口味偏好
        List<DietPlanVO.MealItem> breakfast = generateMealItemsWithSeasonality(requirement, mealRatios.get("breakfast"), 
                request.getDietaryPreferences(), request.getForbiddenFoods(), request.getCookingMethods(), 
                request.getTastePreference(), request.getPlanDate());
                
        List<DietPlanVO.MealItem> lunch = generateMealItemsWithSeasonality(requirement, mealRatios.get("lunch"), 
                request.getDietaryPreferences(), request.getForbiddenFoods(), request.getCookingMethods(), 
                request.getTastePreference(), request.getPlanDate());
                
        List<DietPlanVO.MealItem> dinner = generateMealItemsWithSeasonality(requirement, mealRatios.get("dinner"), 
                request.getDietaryPreferences(), request.getForbiddenFoods(), request.getCookingMethods(), 
                request.getTastePreference(), request.getPlanDate());
                
        List<DietPlanVO.MealItem> snacks = request.getIncludeSnacks() ? 
                generateMealItemsWithSeasonality(requirement, mealRatios.get("snacks"), 
                        request.getDietaryPreferences(), request.getForbiddenFoods(), request.getCookingMethods(), 
                        request.getTastePreference(), request.getPlanDate()) : 
                new ArrayList<>();

        // 4. 创建饮食方案实体
        DietPlan dietPlan = new DietPlan();
        dietPlan.setUserId(request.getUserId());
        dietPlan.setPlanDate(request.getPlanDate());
        dietPlan.setPlanName(StringUtils.hasText(request.getPlanName()) ? 
                request.getPlanName() : "个性化饮食方案-" + new Date());
        
        try {
            dietPlan.setBreakfast(objectMapper.writeValueAsString(breakfast));
            dietPlan.setLunch(objectMapper.writeValueAsString(lunch));
            dietPlan.setDinner(objectMapper.writeValueAsString(dinner));
            dietPlan.setSnacks(objectMapper.writeValueAsString(snacks));
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "方案数据序列化失败");
        }

        // 5. 计算总营养成分
        calculateTotalNutrition(dietPlan, breakfast, lunch, dinner, snacks);
        
        dietPlan.setStatus(1); // 草稿状态
        dietPlan.setCreateTime(LocalDateTime.now());
        dietPlan.setUpdateTime(LocalDateTime.now());

        // 6. 保存方案
        if (request.getAutoSave()) {
            this.save(dietPlan);
        }

        // 7. 转换为VO并返回
        return convertToVO(dietPlan, breakfast, lunch, dinner, snacks, requirement);
    }

    @Override
    @Transactional
    public DietPlanVO adjustDietPlan(DietPlanAdjustRequest request) {
        // 1. 获取原方案
        DietPlan dietPlan = this.getById(request.getPlanId());
        if (dietPlan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "饮食方案不存在");
        }

        try {
            // 2. 解析原方案内容
            List<DietPlanVO.MealItem> breakfast = objectMapper.readValue(dietPlan.getBreakfast(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> lunch = objectMapper.readValue(dietPlan.getLunch(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> dinner = objectMapper.readValue(dietPlan.getDinner(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> snacks = objectMapper.readValue(dietPlan.getSnacks(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});

            // 3. 执行调整操作
            if (!CollectionUtils.isEmpty(request.getAdjustItems())) {
                // 批量调整
                for (DietPlanAdjustRequest.AdjustItem item : request.getAdjustItems()) {
                    adjustMealItems(item.getMealType(), item.getAdjustType(), item.getOriginalFoodId(), 
                            item.getNewFoodId(), item.getOriginalAmount(), item.getNewAmount(),
                            breakfast, lunch, dinner, snacks);
                }
            } else {
                // 单项调整
                adjustMealItems(request.getMealType(), request.getAdjustType(), request.getOriginalFoodId(), 
                        request.getNewFoodId(), request.getOriginalAmount(), request.getNewAmount(),
                        breakfast, lunch, dinner, snacks);
            }

            // 4. 更新方案内容
            dietPlan.setBreakfast(objectMapper.writeValueAsString(breakfast));
            dietPlan.setLunch(objectMapper.writeValueAsString(lunch));
            dietPlan.setDinner(objectMapper.writeValueAsString(dinner));
            dietPlan.setSnacks(objectMapper.writeValueAsString(snacks));

            // 5. 重新计算营养成分
            if (request.getRecalculateNutrition()) {
                calculateTotalNutrition(dietPlan, breakfast, lunch, dinner, snacks);
            }

            dietPlan.setUpdateTime(LocalDateTime.now());

            // 6. 保存调整结果
            if (request.getAutoSave()) {
                this.updateById(dietPlan);
            }

            // 7. 获取营养需求用于计算达标情况
            NutritionRequirement requirement = nutritionRequirementService.lambdaQuery()
                    .eq(NutritionRequirement::getUserId, dietPlan.getUserId())
                    .orderByDesc(NutritionRequirement::getAssessmentDate)
                    .last("LIMIT 1")
                    .one();

            return convertToVO(dietPlan, breakfast, lunch, dinner, snacks, requirement);

        } catch (Exception e) {
            log.error("调整饮食方案失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "调整饮食方案失败");
        }
    }

    @Override
    public DietPlanVO getDietPlanById(Long planId) {
        DietPlan dietPlan = this.getById(planId);
        if (dietPlan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "饮食方案不存在");
        }
        return convertToVO(dietPlan);
    }

    @Override
    public DietPlanVO getDietPlanByUserAndDate(Long userId, LocalDate planDate) {
        QueryWrapper<DietPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId)
                   .eq("plan_date", planDate);
        
        DietPlan dietPlan = this.getOne(queryWrapper);
        if (dietPlan == null) {
            return null;
        }

        return convertToVO(dietPlan);
    }

    @Override
    public List<DietPlanVO> getDietPlansByUser(Long userId, LocalDate startDate, LocalDate endDate) {
        QueryWrapper<DietPlan> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        
        if (startDate != null) {
            queryWrapper.ge("plan_date", startDate);
        }
        if (endDate != null) {
            queryWrapper.le("plan_date", endDate);
        }
        
        queryWrapper.orderByDesc("plan_date");
        
        List<DietPlan> dietPlans = this.list(queryWrapper);
        return dietPlans.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DietPlanVO copyDietPlan(Long planId, LocalDate targetDate) {
        DietPlan originalPlan = this.getById(planId);
        if (originalPlan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "原方案不存在");
        }

        DietPlan newPlan = new DietPlan();
        BeanUtils.copyProperties(originalPlan, newPlan);
        newPlan.setPlanId(null);
        newPlan.setPlanDate(targetDate);
        newPlan.setPlanName(originalPlan.getPlanName() + "-复制");
        newPlan.setStatus(1); // 草稿状态
        newPlan.setCreateTime(LocalDateTime.now());
        newPlan.setUpdateTime(LocalDateTime.now());

        this.save(newPlan);
        return convertToVO(newPlan);
    }

    @Override
    @Transactional
    public boolean deleteDietPlan(Long planId) {
        return this.removeById(planId);
    }

    @Override
    @Transactional
    public boolean confirmDietPlan(Long planId) {
        DietPlan dietPlan = this.getById(planId);
        if (dietPlan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "饮食方案不存在");
        }

        dietPlan.setStatus(2); // 已确认状态
        dietPlan.setUpdateTime(LocalDateTime.now());
        return this.updateById(dietPlan);
    }

    @Override
    public DietPlanVO calculateNutrition(Long planId) {
        DietPlan dietPlan = this.getById(planId);
        if (dietPlan == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "饮食方案不存在");
        }

        try {
            List<DietPlanVO.MealItem> breakfast = objectMapper.readValue(dietPlan.getBreakfast(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> lunch = objectMapper.readValue(dietPlan.getLunch(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> dinner = objectMapper.readValue(dietPlan.getDinner(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> snacks = objectMapper.readValue(dietPlan.getSnacks(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});

            calculateTotalNutrition(dietPlan, breakfast, lunch, dinner, snacks);
            dietPlan.setUpdateTime(LocalDateTime.now());
            this.updateById(dietPlan);

            NutritionRequirement requirement = nutritionRequirementService.lambdaQuery()
                    .eq(NutritionRequirement::getUserId, dietPlan.getUserId())
                    .orderByDesc(NutritionRequirement::getAssessmentDate)
                    .last("LIMIT 1")
                    .one();

            return convertToVO(dietPlan, breakfast, lunch, dinner, snacks, requirement);

        } catch (Exception e) {
            log.error("计算营养成分失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "计算营养成分失败");
        }
    }

    /**
     * 解析餐次分配比例
     */
    private Map<String, BigDecimal> parseMealRatio(String mealRatio) {
        Map<String, BigDecimal> ratios = new HashMap<>();
        
        if (!StringUtils.hasText(mealRatio)) {
            // 默认比例：早餐25%，午餐35%，晚餐30%，加餐10%
            ratios.put("breakfast", BigDecimal.valueOf(0.25));
            ratios.put("lunch", BigDecimal.valueOf(0.35));
            ratios.put("dinner", BigDecimal.valueOf(0.30));
            ratios.put("snacks", BigDecimal.valueOf(0.10));
        } else {
            String[] parts = mealRatio.split(":");
            if (parts.length >= 3) {
                ratios.put("breakfast", new BigDecimal(parts[0]));
                ratios.put("lunch", new BigDecimal(parts[1]));
                ratios.put("dinner", new BigDecimal(parts[2]));
                ratios.put("snacks", parts.length > 3 ? new BigDecimal(parts[3]) : BigDecimal.ZERO);
            }
        }
        
        return ratios;
    }

    /**
     * 生成餐次食物项目
     */
    private List<DietPlanVO.MealItem> generateMealItems(NutritionRequirement requirement, 
                                                       BigDecimal mealRatio,
                                                       List<String> dietaryPreferences,
                                                       List<String> forbiddenFoods,
                                                       List<String> cookingMethods) {
        // 计算该餐次的营养需求
        BigDecimal mealCalories = requirement.getDailyCalories().multiply(mealRatio);
        BigDecimal mealProtein = requirement.getProtein().multiply(mealRatio);
        BigDecimal mealCarbohydrate = requirement.getCarbohydrate().multiply(mealRatio);
        BigDecimal mealFat = requirement.getFat().multiply(mealRatio);
        
        // 获取符合条件的食物
        QueryWrapper<FoodNutrition> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(forbiddenFoods)) {
            queryWrapper.notIn("food_name", forbiddenFoods);
        }
        List<FoodNutrition> availableFoods = foodNutritionService.list(queryWrapper);
        if (CollectionUtils.isEmpty(availableFoods)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "没有可用的食物数据");
        }
        
        // 根据饮食偏好过滤食物
        if (!CollectionUtils.isEmpty(dietaryPreferences)) {
            availableFoods = filterFoodsByDietaryPreferences(availableFoods, dietaryPreferences);
        }
        
        // 改进的食物搭配算法：增加多样性和随机性
        List<DietPlanVO.MealItem> mealItems = new ArrayList<>();
        Random random = new Random();
        
        // 1. 选择主食（碳水化合物来源）- 从符合条件的食物中随机选择
        List<FoodNutrition> mainFoods = availableFoods.stream()
                .filter(food -> food.getCarbohydratePer100g().compareTo(BigDecimal.valueOf(15)) > 0)
                .collect(Collectors.toList());
        
        if (!CollectionUtils.isEmpty(mainFoods)) {
            // 随机选择一种主食，增加多样性
            FoodNutrition mainFood = mainFoods.get(random.nextInt(mainFoods.size()));
            // 根据餐次调整主食比例
            BigDecimal carbRatio = getRandomCarbRatio(mealRatio, random);
            BigDecimal amount = calculateFoodAmount(mainFood, mealCarbohydrate.multiply(carbRatio), "carbohydrate");
            mealItems.add(createMealItem(mainFood, amount, getRandomCookingMethod(cookingMethods)));
        }
        
        // 2. 选择蛋白质来源 - 从符合条件的食物中随机选择
        List<FoodNutrition> proteinFoods = availableFoods.stream()
                .filter(food -> food.getProteinPer100g().compareTo(BigDecimal.valueOf(10)) > 0)
                .collect(Collectors.toList());
        
        if (!CollectionUtils.isEmpty(proteinFoods)) {
            // 随机选择一种蛋白质食物
            FoodNutrition proteinFood = proteinFoods.get(random.nextInt(proteinFoods.size()));
            // 根据餐次调整蛋白质比例
            BigDecimal proteinRatio = getRandomProteinRatio(mealRatio, random);
            BigDecimal amount = calculateFoodAmount(proteinFood, mealProtein.multiply(proteinRatio), "protein");
            mealItems.add(createMealItem(proteinFood, amount, getRandomCookingMethod(cookingMethods)));
        }
        
        // 3. 选择蔬菜 - 从多种蔬菜中随机选择2-3种
        List<FoodNutrition> vegetables = availableFoods.stream()
                .filter(food -> food.getFoodCategory() != null && 
                        (food.getFoodCategory().contains("蔬菜") || 
                         food.getFoodCategory().contains("菌菇") ||
                         food.getFoodCategory().contains("藻类")))
                .collect(Collectors.toList());
        
        if (!CollectionUtils.isEmpty(vegetables)) {
            // 随机选择2-3种蔬菜，增加多样性
            int vegCount = Math.min(2 + random.nextInt(2), vegetables.size());
            Collections.shuffle(vegetables);
            
            for (int i = 0; i < vegCount; i++) {
                FoodNutrition vegetable = vegetables.get(i);
                // 每种蔬菜的分量在50-150g之间随机
                BigDecimal amount = BigDecimal.valueOf(50 + random.nextInt(101));
                mealItems.add(createMealItem(vegetable, amount, getRandomCookingMethod(cookingMethods)));
            }
        }
        
        // 4. 根据餐次和营养需求，可能添加水果或坚果
        if (random.nextBoolean() && mealRatio.compareTo(BigDecimal.valueOf(0.3)) < 0) { // 加餐或早餐时可能添加
            List<FoodNutrition> fruits = availableFoods.stream()
                    .filter(food -> food.getFoodCategory() != null && 
                            (food.getFoodCategory().contains("水果")))
                    .collect(Collectors.toList());
            
            if (!CollectionUtils.isEmpty(fruits)) {
                FoodNutrition fruit = fruits.get(random.nextInt(fruits.size()));
                BigDecimal amount = BigDecimal.valueOf(100 + random.nextInt(100)); // 100-200g水果
                mealItems.add(createMealItem(fruit, amount, "生食")); // 水果通常是生食
            }
        }
        
        // 5. 根据餐次和营养需求，可能添加健康脂肪来源
        if (random.nextBoolean() && mealFat.compareTo(BigDecimal.valueOf(10)) > 0) {
            List<FoodNutrition> fatSources = availableFoods.stream()
                    .filter(food -> food.getFatPer100g().compareTo(BigDecimal.valueOf(15)) > 0 &&
                            !food.getFoodCategory().contains("肉类")) // 排除肉类，主要选择植物性脂肪
                    .collect(Collectors.toList());
            
            if (!CollectionUtils.isEmpty(fatSources)) {
                FoodNutrition fatSource = fatSources.get(random.nextInt(fatSources.size()));
                BigDecimal amount = BigDecimal.valueOf(10 + random.nextInt(20)); // 10-30g健康脂肪
                mealItems.add(createMealItem(fatSource, amount, getRandomCookingMethod(cookingMethods)));
            }
        }
        
        return mealItems;
    }
    
    /**
     * 生成餐次食物项目（考虑口味偏好）
     */
    private List<DietPlanVO.MealItem> generateMealItemsWithTastePreference(NutritionRequirement requirement, 
                                                                         BigDecimal mealRatio,
                                                                         List<String> dietaryPreferences,
                                                                         List<String> forbiddenFoods,
                                                                         List<String> cookingMethods,
                                                                         String tastePreference) {
        // 先生成基础餐次
        List<DietPlanVO.MealItem> mealItems = generateMealItems(requirement, mealRatio, 
                dietaryPreferences, forbiddenFoods, cookingMethods);
        
        // 根据口味偏好调整烹饪方法和调味料
        if (StringUtils.hasText(tastePreference)) {
            adjustMealItemsByTastePreference(mealItems, tastePreference);
        }
        
        return mealItems;
    }
    
    /**
     * 生成餐次食物项目（考虑季节性）
     */
    private List<DietPlanVO.MealItem> generateMealItemsWithSeasonality(NutritionRequirement requirement, 
                                                                      BigDecimal mealRatio,
                                                                      List<String> dietaryPreferences,
                                                                      List<String> forbiddenFoods,
                                                                      List<String> cookingMethods,
                                                                      Integer tastePreference,
                                                                      LocalDate planDate) {
        // 将Integer类型的tastePreference转换为String
        String tastePreferenceStr = convertTastePreferenceToString(tastePreference);
        
        // 先生成基础餐次（考虑口味偏好）
        List<DietPlanVO.MealItem> mealItems = StringUtils.hasText(tastePreferenceStr) ?
                generateMealItemsWithTastePreference(requirement, mealRatio, 
                        dietaryPreferences, forbiddenFoods, cookingMethods, tastePreferenceStr) :
                generateMealItems(requirement, mealRatio, 
                        dietaryPreferences, forbiddenFoods, cookingMethods);
        
        // 根据季节调整食物选择
        adjustMealItemsBySeason(mealItems, planDate);
        
        return mealItems;
    }
    
    /**
     * 将Integer类型的口味偏好转换为String类型
     */
    private String convertTastePreferenceToString(Integer tastePreference) {
        if (tastePreference == null) {
            return null;
        }
        
        switch (tastePreference) {
            case 1:
                return "清淡";
            case 2:
                return "适中";
            case 3:
                return "重口味";
            default:
                return "适中";
        }
    }
    
    /**
     * 根据季节调整餐次项目
     */
    private void adjustMealItemsBySeason(List<DietPlanVO.MealItem> mealItems, LocalDate planDate) {
        // 获取当前月份
        int month = planDate.getMonthValue();
        String season = getSeason(month);
        
        // 根据季节调整食物
        for (DietPlanVO.MealItem item : mealItems) {
            switch (season) {
                case "春季":
                    // 春季宜食清淡、疏肝理气食物
                    if (item.getFoodName().contains("油腻") || item.getFoodName().contains("肥肉")) {
                        // 替换为春季适宜食物
                        item.setFoodName(item.getFoodName().replace("肥肉", "鸡胸肉"));
                        item.setCookingMethod("清炒");
                    }
                    break;
                case "夏季":
                    // 夏季宜食清凉解暑食物
                    if (item.getFoodName().contains("热性") || item.getFoodName().contains("辛辣")) {
                        // 替换为夏季适宜食物
                        item.setFoodName(item.getFoodName().replace("辛辣", "清淡"));
                        item.setCookingMethod("凉拌");
                    }
                    // 夏季增加瓜类食物
                    if (item.getFoodCategory() != null && item.getFoodCategory().contains("蔬菜") && 
                        Math.random() > 0.7) { // 30%概率替换为瓜类
                        item.setFoodName("黄瓜");
                        item.setCookingMethod("凉拌");
                    }
                    break;
                case "秋季":
                    // 秋季宜食润燥食物
                    if (item.getFoodName().contains("寒凉") || item.getFoodName().contains("生冷")) {
                        // 替换为秋季适宜食物
                        item.setFoodName(item.getFoodName().replace("寒凉", "温润"));
                        item.setCookingMethod("炖煮");
                    }
                    // 秋季增加梨、银耳等润燥食物
                    if (item.getFoodCategory() != null && item.getFoodCategory().contains("水果") && 
                        Math.random() > 0.7) { // 30%概率替换为润燥水果
                        item.setFoodName("梨");
                    }
                    break;
                case "冬季":
                    // 冬季宜食温补食物
                    if (item.getFoodName().contains("寒凉") || item.getFoodName().contains("生冷")) {
                        // 替换为冬季适宜食物
                        item.setFoodName(item.getFoodName().replace("寒凉", "温补"));
                        item.setCookingMethod("红烧");
                    }
                    // 冬季增加根茎类食物
                    if (item.getFoodCategory() != null && item.getFoodCategory().contains("蔬菜") && 
                        Math.random() > 0.7) { // 30%概率替换为根茎类蔬菜
                        item.setFoodName("萝卜");
                        item.setCookingMethod("炖煮");
                    }
                    break;
            }
        }
    }
    
    /**
     * 根据月份获取季节
     */
    private String getSeason(int month) {
        if (month >= 3 && month <= 5) {
            return "春季";
        } else if (month >= 6 && month <= 8) {
            return "夏季";
        } else if (month >= 9 && month <= 11) {
            return "秋季";
        } else {
            return "冬季";
        }
    }
    
    /**
     * 根据口味偏好调整餐次项目
     */
    private void adjustMealItemsByTastePreference(List<DietPlanVO.MealItem> mealItems, String tastePreference) {
        for (DietPlanVO.MealItem item : mealItems) {
            switch (tastePreference) {
                case "清淡":
                    // 清淡口味：减少油盐，多蒸煮
                    if (item.getCookingMethod().contains("炒") || item.getCookingMethod().contains("炸")) {
                        item.setCookingMethod(item.getCookingMethod().replace("炒", "蒸")
                                .replace("炸", "煮"));
                    }
                    break;
                case "适中":
                    // 适中口味：保持原有烹饪方法，不做调整
                    break;
                case "重口味":
                    // 重口味：增加调味，适合红烧、香煎等烹饪方法
                    if (item.getCookingMethod().contains("蒸") || item.getCookingMethod().contains("煮")) {
                        item.setCookingMethod(item.getCookingMethod().replace("蒸", "红烧")
                                .replace("煮", "香煎"));
                    } else if (!item.getCookingMethod().contains("红烧") && !item.getCookingMethod().contains("香煎")) {
                        item.setCookingMethod(item.getCookingMethod() + "（重口味）");
                    }
                    break;
                case "偏咸":
                    // 偏咸口味：适合腌制、酱烧等烹饪方法
                    if (item.getCookingMethod().contains("蒸") || item.getCookingMethod().contains("煮")) {
                        item.setCookingMethod(item.getCookingMethod().replace("蒸", "酱烧")
                                .replace("煮", "红烧"));
                    }
                    break;
                case "偏辣":
                    // 偏辣口味：适合辣炒、麻辣等烹饪方法
                    item.setCookingMethod(item.getCookingMethod() + "（微辣）");
                    break;
                case "偏甜":
                    // 偏甜口味：适合糖醋、红烧等烹饪方法
                    if (item.getCookingMethod().contains("炒") || item.getCookingMethod().contains("煮")) {
                        item.setCookingMethod(item.getCookingMethod().replace("炒", "糖醋")
                                .replace("煮", "蜜汁"));
                    }
                    break;
                default:
                    // 默认口味，不做调整
                    break;
            }
        }
    }
    
    /**
     * 根据饮食偏好过滤食物
     */
    private List<FoodNutrition> filterFoodsByDietaryPreferences(List<FoodNutrition> foods, List<String> preferences) {
        List<FoodNutrition> filteredFoods = new ArrayList<>(foods);
        
        for (String preference : preferences) {
            switch (preference) {
                case "素食":
                    // 过滤掉肉类和鱼类
                    filteredFoods = filteredFoods.stream()
                            .filter(food -> food.getFoodCategory() == null || 
                                    (!food.getFoodCategory().contains("肉类") && 
                                     !food.getFoodCategory().contains("鱼类") &&
                                     !food.getFoodCategory().contains("海鲜")))
                            .collect(Collectors.toList());
                    break;
                case "无乳糖":
                    // 过滤掉乳制品
                    filteredFoods = filteredFoods.stream()
                            .filter(food -> food.getFoodCategory() == null || 
                                    !food.getFoodCategory().contains("乳制品"))
                            .collect(Collectors.toList());
                    break;
                case "低盐":
                    // 过滤掉高盐食物
                    filteredFoods = filteredFoods.stream()
                            .filter(food -> food.getFoodName() == null || 
                                    (!food.getFoodName().contains("咸") && 
                                     !food.getFoodName().contains("酱") &&
                                     !food.getFoodName().contains("腊")))
                            .collect(Collectors.toList());
                    break;
                // 可以添加更多饮食偏好的处理逻辑
            }
        }
        
        return filteredFoods;
    }
    
    /**
     * 获取随机碳水化合物比例
     */
    private BigDecimal getRandomCarbRatio(BigDecimal mealRatio, Random random) {
        // 早餐和晚餐碳水化合物比例较高，午餐适中
        if (mealRatio.compareTo(BigDecimal.valueOf(0.25)) == 0) { // 早餐
            return BigDecimal.valueOf(0.6 + random.nextDouble() * 0.2); // 0.6-0.8
        } else if (mealRatio.compareTo(BigDecimal.valueOf(0.35)) == 0) { // 午餐
            return BigDecimal.valueOf(0.5 + random.nextDouble() * 0.2); // 0.5-0.7
        } else { // 晚餐或加餐
            return BigDecimal.valueOf(0.4 + random.nextDouble() * 0.2); // 0.4-0.6
        }
    }
    
    /**
     * 获取随机蛋白质比例
     */
    private BigDecimal getRandomProteinRatio(BigDecimal mealRatio, Random random) {
        // 午餐蛋白质比例较高，早餐和晚餐适中
        if (mealRatio.compareTo(BigDecimal.valueOf(0.35)) == 0) { // 午餐
            return BigDecimal.valueOf(0.7 + random.nextDouble() * 0.2); // 0.7-0.9
        } else { // 早餐、晚餐或加餐
            return BigDecimal.valueOf(0.6 + random.nextDouble() * 0.2); // 0.6-0.8
        }
    }

    /**
     * 根据营养素选择食物
     */
    private FoodNutrition selectFoodByNutrient(List<FoodNutrition> foods, String nutrientType) {
        return foods.stream()
                .filter(food -> {
                    switch (nutrientType) {
                        case "protein":
                            return food.getProteinPer100g().compareTo(BigDecimal.valueOf(10)) > 0;
                        case "carbohydrate":
                            return food.getCarbohydratePer100g().compareTo(BigDecimal.valueOf(20)) > 0;
                        case "fat":
                            return food.getFatPer100g().compareTo(BigDecimal.valueOf(5)) > 0;
                        default:
                            return true;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据分类选择食物
     */
    private FoodNutrition selectFoodByCategory(List<FoodNutrition> foods, String category) {
        return foods.stream()
                .filter(food -> food.getFoodCategory().contains(category))
                .findFirst()
                .orElse(foods.get(0)); // 如果没找到指定分类，返回第一个
    }

    /**
     * 计算食物分量
     */
    private BigDecimal calculateFoodAmount(FoodNutrition food, BigDecimal targetNutrient, String nutrientType) {
        BigDecimal nutrientPer100g;
        switch (nutrientType) {
            case "protein":
                nutrientPer100g = food.getProteinPer100g();
                break;
            case "carbohydrate":
                nutrientPer100g = food.getCarbohydratePer100g();
                break;
            case "fat":
                nutrientPer100g = food.getFatPer100g();
                break;
            default:
                nutrientPer100g = food.getCaloriesPer100g();
        }
        
        if (nutrientPer100g.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100); // 默认100g
        }
        
        return targetNutrient.divide(nutrientPer100g, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .max(BigDecimal.valueOf(50))  // 最少50g
                .min(BigDecimal.valueOf(500)); // 最多500g
    }

    /**
     * 创建餐次项目
     */
    private DietPlanVO.MealItem createMealItem(FoodNutrition food, BigDecimal amount, String cookingMethod) {
        DietPlanVO.MealItem item = new DietPlanVO.MealItem();
        item.setFoodId(food.getFoodId());
        item.setFoodName(food.getFoodName());
        item.setFoodCategory(food.getFoodCategory());
        item.setAmount(amount);
        item.setCookingMethod(cookingMethod);
        
        // 计算实际营养成分
        BigDecimal ratio = amount.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        item.setCalories(food.getCaloriesPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
        item.setProtein(food.getProteinPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
        item.setCarbohydrate(food.getCarbohydratePer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
        item.setFat(food.getFatPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
        item.setFiber(food.getFiberPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
        
        return item;
    }

    /**
     * 获取随机烹饪方式
     */
    private String getRandomCookingMethod(List<String> cookingMethods) {
        if (CollectionUtils.isEmpty(cookingMethods)) {
            return "蒸";
        }
        return cookingMethods.get(new Random().nextInt(cookingMethods.size()));
    }

    /**
     * 调整餐次项目
     */
    private void adjustMealItems(Integer mealType, Integer adjustType, Long originalFoodId, 
                               Long newFoodId, Double originalAmount, Double newAmount,
                               List<DietPlanVO.MealItem> breakfast, List<DietPlanVO.MealItem> lunch,
                               List<DietPlanVO.MealItem> dinner, List<DietPlanVO.MealItem> snacks) {
        
        List<DietPlanVO.MealItem> targetMeal;
        switch (mealType) {
            case 1:
                targetMeal = breakfast;
                break;
            case 2:
                targetMeal = lunch;
                break;
            case 3:
                targetMeal = dinner;
                break;
            case 4:
                targetMeal = snacks;
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的餐次类型");
        }

        switch (adjustType) {
            case 1: // 替换食物
                replaceFood(targetMeal, originalFoodId, newFoodId, newAmount);
                break;
            case 2: // 调整分量
                adjustAmount(targetMeal, originalFoodId, newAmount);
                break;
            case 3: // 添加食物
                addFood(targetMeal, newFoodId, newAmount);
                break;
            case 4: // 删除食物
                removeFood(targetMeal, originalFoodId);
                break;
            default:
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "无效的调整类型");
        }
    }

    /**
     * 替换食物
     */
    private void replaceFood(List<DietPlanVO.MealItem> mealItems, Long originalFoodId, Long newFoodId, Double newAmount) {
        FoodNutrition newFood = foodNutritionService.getById(newFoodId);
        if (newFood == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "新食物不存在");
        }

        for (int i = 0; i < mealItems.size(); i++) {
            if (mealItems.get(i).getFoodId().equals(originalFoodId)) {
                BigDecimal amount = newAmount != null ? BigDecimal.valueOf(newAmount) : mealItems.get(i).getAmount();
                mealItems.set(i, createMealItem(newFood, amount, mealItems.get(i).getCookingMethod()));
                break;
            }
        }
    }

    /**
     * 调整分量
     */
    private void adjustAmount(List<DietPlanVO.MealItem> mealItems, Long foodId, Double newAmount) {
        for (DietPlanVO.MealItem item : mealItems) {
            if (item.getFoodId().equals(foodId)) {
                BigDecimal amount = BigDecimal.valueOf(newAmount);
                BigDecimal ratio = amount.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
                
                // 重新计算营养成分
                FoodNutrition food = foodNutritionService.getById(foodId);
                if (food != null) {
                    item.setAmount(amount);
                    item.setCalories(food.getCaloriesPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
                    item.setProtein(food.getProteinPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
                    item.setCarbohydrate(food.getCarbohydratePer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
                    item.setFat(food.getFatPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
                    item.setFiber(food.getFiberPer100g().multiply(ratio).setScale(2, RoundingMode.HALF_UP));
                }
                break;
            }
        }
    }

    /**
     * 添加食物
     */
    private void addFood(List<DietPlanVO.MealItem> mealItems, Long foodId, Double amount) {
        FoodNutrition food = foodNutritionService.getById(foodId);
        if (food == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "食物不存在");
        }

        BigDecimal foodAmount = amount != null ? BigDecimal.valueOf(amount) : BigDecimal.valueOf(100);
        mealItems.add(createMealItem(food, foodAmount, "蒸"));
    }

    /**
     * 删除食物
     */
    private void removeFood(List<DietPlanVO.MealItem> mealItems, Long foodId) {
        mealItems.removeIf(item -> item.getFoodId().equals(foodId));
    }

    /**
     * 计算总营养成分
     */
    private void calculateTotalNutrition(DietPlan dietPlan, List<DietPlanVO.MealItem> breakfast,
                                       List<DietPlanVO.MealItem> lunch, List<DietPlanVO.MealItem> dinner,
                                       List<DietPlanVO.MealItem> snacks) {
        BigDecimal totalCalories = BigDecimal.ZERO;
        BigDecimal totalProtein = BigDecimal.ZERO;
        BigDecimal totalCarbohydrate = BigDecimal.ZERO;
        BigDecimal totalFat = BigDecimal.ZERO;

        List<List<DietPlanVO.MealItem>> allMeals = Arrays.asList(breakfast, lunch, dinner, snacks);
        for (List<DietPlanVO.MealItem> meal : allMeals) {
            for (DietPlanVO.MealItem item : meal) {
                totalCalories = totalCalories.add(item.getCalories());
                totalProtein = totalProtein.add(item.getProtein());
                totalCarbohydrate = totalCarbohydrate.add(item.getCarbohydrate());
                totalFat = totalFat.add(item.getFat());
            }
        }

        dietPlan.setTotalCalories(totalCalories.setScale(2, RoundingMode.HALF_UP));
        dietPlan.setTotalProtein(totalProtein.setScale(2, RoundingMode.HALF_UP));
        dietPlan.setTotalCarbohydrate(totalCarbohydrate.setScale(2, RoundingMode.HALF_UP));
        dietPlan.setTotalFat(totalFat.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * 转换为VO对象
     */
    private DietPlanVO convertToVO(DietPlan dietPlan) {
        try {
            List<DietPlanVO.MealItem> breakfast = objectMapper.readValue(dietPlan.getBreakfast(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> lunch = objectMapper.readValue(dietPlan.getLunch(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> dinner = objectMapper.readValue(dietPlan.getDinner(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});
            List<DietPlanVO.MealItem> snacks = objectMapper.readValue(dietPlan.getSnacks(), 
                    new TypeReference<List<DietPlanVO.MealItem>>() {});

            return convertToVO(dietPlan, breakfast, lunch, dinner, snacks, null);
        } catch (Exception e) {
            log.error("转换VO失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据转换失败");
        }
    }

    /**
     * 转换为VO对象（带营养达标情况）
     */
    private DietPlanVO convertToVO(DietPlan dietPlan, List<DietPlanVO.MealItem> breakfast,
                                 List<DietPlanVO.MealItem> lunch, List<DietPlanVO.MealItem> dinner,
                                 List<DietPlanVO.MealItem> snacks, NutritionRequirement requirement) {
        DietPlanVO vo = new DietPlanVO();
        BeanUtils.copyProperties(dietPlan, vo);
        
        vo.setBreakfast(breakfast);
        vo.setLunch(lunch);
        vo.setDinner(dinner);
        vo.setSnacks(snacks);

        // 计算营养达标情况
        if (requirement != null) {
            vo.setNutritionCompliance(calculateNutritionCompliance(dietPlan, requirement));
        }

        return vo;
    }

    /**
     * 计算营养达标情况
     */
    private DietPlanVO.NutritionComplianceVO calculateNutritionCompliance(DietPlan dietPlan, 
                                                                        NutritionRequirement requirement) {
        DietPlanVO.NutritionComplianceVO compliance = new DietPlanVO.NutritionComplianceVO();
        
        // 计算达标率
        compliance.setCaloriesComplianceRate(calculateComplianceRate(dietPlan.getTotalCalories(), requirement.getDailyCalories()));
        compliance.setProteinComplianceRate(calculateComplianceRate(dietPlan.getTotalProtein(), requirement.getProtein()));
        compliance.setCarbohydrateComplianceRate(calculateComplianceRate(dietPlan.getTotalCarbohydrate(), requirement.getCarbohydrate()));
        compliance.setFatComplianceRate(calculateComplianceRate(dietPlan.getTotalFat(), requirement.getFat()));

        // 计算整体评分
        BigDecimal avgCompliance = compliance.getCaloriesComplianceRate()
                .add(compliance.getProteinComplianceRate())
                .add(compliance.getCarbohydrateComplianceRate())
                .add(compliance.getFatComplianceRate())
                .divide(BigDecimal.valueOf(4), 2, RoundingMode.HALF_UP);
        compliance.setOverallScore(avgCompliance);

        // 生成营养建议
        List<String> advice = new ArrayList<>();
        if (compliance.getCaloriesComplianceRate().compareTo(BigDecimal.valueOf(90)) < 0) {
            advice.add("热量摄入不足，建议增加主食或健康脂肪");
        }
        if (compliance.getProteinComplianceRate().compareTo(BigDecimal.valueOf(90)) < 0) {
            advice.add("蛋白质摄入不足，建议增加瘦肉、鱼类或豆制品");
        }
        if (compliance.getCarbohydrateComplianceRate().compareTo(BigDecimal.valueOf(90)) < 0) {
            advice.add("碳水化合物摄入不足，建议增加全谷物食品");
        }
        compliance.setNutritionAdvice(advice);

        return compliance;
    }

    /**
     * 计算达标率
     */
    private BigDecimal calculateComplianceRate(BigDecimal actual, BigDecimal target) {
        if (target.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.valueOf(100);
        }
        return actual.divide(target, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}




