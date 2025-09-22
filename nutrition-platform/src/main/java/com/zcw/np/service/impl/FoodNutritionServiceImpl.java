package com.zcw.np.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.exception.BusinessException;
import com.zcw.np.mapper.FoodNutritionMapper;
import com.zcw.np.model.dto.food.FoodNutritionAddRequest;
import com.zcw.np.model.dto.food.FoodNutritionQueryRequest;
import com.zcw.np.model.dto.food.FoodNutritionUpdateRequest;
import com.zcw.np.model.entity.FoodNutrition;
import com.zcw.np.model.vo.FoodNutritionVO;
import com.zcw.np.service.FoodNutritionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author zcw
* @description 针对表【food_nutrition(食物营养数据表)】的数据库操作Service实现
* @createDate 2025-09-22 16:05:18
*/
@Service
public class FoodNutritionServiceImpl extends ServiceImpl<FoodNutritionMapper, FoodNutrition>
    implements FoodNutritionService{

    @Override
    public long addFoodNutrition(FoodNutritionAddRequest foodNutritionAddRequest) {
        if (foodNutritionAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 检查食物名称是否已存在
        String foodName = foodNutritionAddRequest.getFoodName();
        if (StringUtils.isBlank(foodName)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "食物名称不能为空");
        }

        QueryWrapper<FoodNutrition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("food_name", foodName);
        long count = this.count(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "食物名称已存在");
        }

        // 创建食物营养数据实体
        FoodNutrition foodNutrition = new FoodNutrition();
        BeanUtils.copyProperties(foodNutritionAddRequest, foodNutrition);

        // 保存到数据库
        boolean result = this.save(foodNutrition);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "添加食物营养数据失败");
        }

        return foodNutrition.getFoodId();
    }

    @Override
    public boolean deleteFoodNutrition(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 检查食物是否存在
        FoodNutrition foodNutrition = this.getById(id);
        if (foodNutrition == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "食物营养数据不存在");
        }

        // 删除食物营养数据
        return this.removeById(id);
    }

    @Override
    public boolean updateFoodNutrition(FoodNutritionUpdateRequest foodNutritionUpdateRequest) {
        if (foodNutritionUpdateRequest == null || foodNutritionUpdateRequest.getFoodId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        long id = foodNutritionUpdateRequest.getFoodId();
        
        // 检查食物是否存在
        FoodNutrition oldFoodNutrition = this.getById(id);
        if (oldFoodNutrition == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "食物营养数据不存在");
        }

        // 检查食物名称是否与其他食物重复
        String foodName = foodNutritionUpdateRequest.getFoodName();
        if (StringUtils.isNotBlank(foodName) && !foodName.equals(oldFoodNutrition.getFoodName())) {
            QueryWrapper<FoodNutrition> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("food_name", foodName);
            queryWrapper.ne("food_id", id);
            long count = this.count(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "食物名称已存在");
            }
        }

        // 更新食物营养数据
        FoodNutrition foodNutrition = new FoodNutrition();
        BeanUtils.copyProperties(foodNutritionUpdateRequest, foodNutrition);

        return this.updateById(foodNutrition);
    }

    @Override
    public FoodNutritionVO getFoodNutritionById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        FoodNutrition foodNutrition = this.getById(id);
        if (foodNutrition == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "食物营养数据不存在");
        }

        return getFoodNutritionVO(foodNutrition);
    }

    @Override
    public Page<FoodNutritionVO> listFoodNutritionByPage(FoodNutritionQueryRequest foodNutritionQueryRequest) {
        long current = foodNutritionQueryRequest.getCurrent();
        long size = foodNutritionQueryRequest.getPageSize();

        // 查询数据库
        Page<FoodNutrition> foodNutritionPage = this.page(new Page<>(current, size),
                this.getQueryWrapper(foodNutritionQueryRequest));

        // 转换为VO
        Page<FoodNutritionVO> foodNutritionVOPage = new Page<>(current, size, foodNutritionPage.getTotal());
        List<FoodNutritionVO> foodNutritionVOList = foodNutritionPage.getRecords().stream()
                .map(this::getFoodNutritionVO)
                .collect(Collectors.toList());
        foodNutritionVOPage.setRecords(foodNutritionVOList);

        return foodNutritionVOPage;
    }

    @Override
    public QueryWrapper<FoodNutrition> getQueryWrapper(FoodNutritionQueryRequest foodNutritionQueryRequest) {
        QueryWrapper<FoodNutrition> queryWrapper = new QueryWrapper<>();
        if (foodNutritionQueryRequest == null) {
            return queryWrapper;
        }

        String foodName = foodNutritionQueryRequest.getFoodName();
        String foodCategory = foodNutritionQueryRequest.getFoodCategory();
        String sortField = foodNutritionQueryRequest.getSortField();
        String sortOrder = foodNutritionQueryRequest.getSortOrder();

        // 模糊查询食物名称
        if (StringUtils.isNotBlank(foodName)) {
            queryWrapper.like("food_name", foodName);
        }

        // 精确查询食物分类
        if (StringUtils.isNotBlank(foodCategory)) {
            queryWrapper.eq("food_category", foodCategory);
        }

        // 热量范围查询
        if (foodNutritionQueryRequest.getMinCalories() != null) {
            queryWrapper.ge("calories_per_100g", foodNutritionQueryRequest.getMinCalories());
        }
        if (foodNutritionQueryRequest.getMaxCalories() != null) {
            queryWrapper.le("calories_per_100g", foodNutritionQueryRequest.getMaxCalories());
        }

        // 蛋白质范围查询
        if (foodNutritionQueryRequest.getMinProtein() != null) {
            queryWrapper.ge("protein_per_100g", foodNutritionQueryRequest.getMinProtein());
        }
        if (foodNutritionQueryRequest.getMaxProtein() != null) {
            queryWrapper.le("protein_per_100g", foodNutritionQueryRequest.getMaxProtein());
        }

        // 碳水化合物范围查询
        if (foodNutritionQueryRequest.getMinCarbohydrate() != null) {
            queryWrapper.ge("carbohydrate_per_100g", foodNutritionQueryRequest.getMinCarbohydrate());
        }
        if (foodNutritionQueryRequest.getMaxCarbohydrate() != null) {
            queryWrapper.le("carbohydrate_per_100g", foodNutritionQueryRequest.getMaxCarbohydrate());
        }

        // 脂肪范围查询
        if (foodNutritionQueryRequest.getMinFat() != null) {
            queryWrapper.ge("fat_per_100g", foodNutritionQueryRequest.getMinFat());
        }
        if (foodNutritionQueryRequest.getMaxFat() != null) {
            queryWrapper.le("fat_per_100g", foodNutritionQueryRequest.getMaxFat());
        }

        // 排序
        if (StringUtils.isNotBlank(sortField)) {
            boolean isAsc = "asc".equals(sortOrder);
            queryWrapper.orderBy(true, isAsc, sortField);
        } else {
            // 默认按创建时间降序
            queryWrapper.orderByDesc("create_time");
        }

        return queryWrapper;
    }

    @Override
    public FoodNutritionVO getFoodNutritionVO(FoodNutrition foodNutrition) {
        if (foodNutrition == null) {
            return null;
        }

        FoodNutritionVO foodNutritionVO = new FoodNutritionVO();
        BeanUtils.copyProperties(foodNutrition, foodNutritionVO);

        return foodNutritionVO;
    }
}




