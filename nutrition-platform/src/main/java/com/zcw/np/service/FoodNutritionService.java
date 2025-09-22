package com.zcw.np.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcw.np.model.dto.food.FoodNutritionAddRequest;
import com.zcw.np.model.dto.food.FoodNutritionQueryRequest;
import com.zcw.np.model.dto.food.FoodNutritionUpdateRequest;
import com.zcw.np.model.entity.FoodNutrition;
import com.zcw.np.model.vo.FoodNutritionVO;

/**
* @author zcw
* @description 针对表【food_nutrition(食物营养数据表)】的数据库操作Service
* @createDate 2025-09-22 16:05:18
*/
public interface FoodNutritionService extends IService<FoodNutrition> {

    /**
     * 添加食物营养数据
     *
     * @param foodNutritionAddRequest 添加请求
     * @return 食物ID
     */
    long addFoodNutrition(FoodNutritionAddRequest foodNutritionAddRequest);

    /**
     * 删除食物营养数据
     *
     * @param id 食物ID
     * @return 是否删除成功
     */
    boolean deleteFoodNutrition(long id);

    /**
     * 更新食物营养数据
     *
     * @param foodNutritionUpdateRequest 更新请求
     * @return 是否更新成功
     */
    boolean updateFoodNutrition(FoodNutritionUpdateRequest foodNutritionUpdateRequest);

    /**
     * 根据ID获取食物营养数据
     *
     * @param id 食物ID
     * @return 食物营养数据VO
     */
    FoodNutritionVO getFoodNutritionById(long id);

    /**
     * 分页查询食物营养数据
     *
     * @param foodNutritionQueryRequest 查询请求
     * @return 分页结果
     */
    Page<FoodNutritionVO> listFoodNutritionByPage(FoodNutritionQueryRequest foodNutritionQueryRequest);

    /**
     * 获取查询条件
     *
     * @param foodNutritionQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper<FoodNutrition> getQueryWrapper(FoodNutritionQueryRequest foodNutritionQueryRequest);

    /**
     * 获取食物营养数据VO
     *
     * @param foodNutrition 食物营养数据实体
     * @return 食物营养数据VO
     */
    FoodNutritionVO getFoodNutritionVO(FoodNutrition foodNutrition);
}
