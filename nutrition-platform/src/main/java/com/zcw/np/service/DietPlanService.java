package com.zcw.np.service;

import com.zcw.np.model.entity.DietPlan;
import com.zcw.np.model.dto.dietplan.DietPlanGenerateRequest;
import com.zcw.np.model.dto.dietplan.DietPlanAdjustRequest;
import com.zcw.np.model.vo.DietPlanVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;
import java.util.List;

/**
* @author zcw
* @description 针对表【diet_plan(饮食方案表)】的数据库操作Service
* @createDate 2025-09-22 16:22:37
*/
public interface DietPlanService extends IService<DietPlan> {

    /**
     * 生成个性化饮食方案
     *
     * @param request 方案生成请求
     * @return 生成的饮食方案
     */
    DietPlanVO generateDietPlan(DietPlanGenerateRequest request);

    /**
     * 调整饮食方案
     *
     * @param request 方案调整请求
     * @return 调整后的饮食方案
     */
    DietPlanVO adjustDietPlan(DietPlanAdjustRequest request);

    /**
     * 根据方案ID获取饮食方案
     *
     * @param planId 方案ID
     * @return 饮食方案
     */
    DietPlanVO getDietPlanById(Long planId);

    /**
     * 获取用户的饮食方案
     *
     * @param userId 用户ID
     * @param planDate 方案日期
     * @return 饮食方案
     */
    DietPlanVO getDietPlanByUserAndDate(Long userId, LocalDate planDate);

    /**
     * 获取用户的饮食方案列表
     *
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 饮食方案列表
     */
    List<DietPlanVO> getDietPlansByUser(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 复制饮食方案
     *
     * @param planId 原方案ID
     * @param targetDate 目标日期
     * @return 复制的饮食方案
     */
    DietPlanVO copyDietPlan(Long planId, LocalDate targetDate);

    /**
     * 删除饮食方案
     *
     * @param planId 方案ID
     * @return 是否删除成功
     */
    boolean deleteDietPlan(Long planId);

    /**
     * 确认饮食方案
     *
     * @param planId 方案ID
     * @return 是否确认成功
     */
    boolean confirmDietPlan(Long planId);

    /**
     * 计算方案营养成分
     *
     * @param planId 方案ID
     * @return 更新后的饮食方案
     */
    DietPlanVO calculateNutrition(Long planId);
}
