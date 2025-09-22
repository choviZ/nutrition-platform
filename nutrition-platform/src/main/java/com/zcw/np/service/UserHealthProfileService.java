package com.zcw.np.service;

import com.zcw.np.model.dto.health.HealthProfileCreateRequest;
import com.zcw.np.model.dto.health.HealthProfileUpdateRequest;
import com.zcw.np.model.entity.UserHealthProfile;
import com.zcw.np.model.vo.HealthProfileVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zcw
* @description 针对表【user_health_profile(用户健康档案表)】的数据库操作Service
* @createDate 2025-09-22 09:14:28
*/
public interface UserHealthProfileService extends IService<UserHealthProfile> {

    /**
     * 创建健康档案
     *
     * @param healthProfileCreateRequest 创建请求
     * @param userId 用户ID
     * @return 健康档案VO
     */
    HealthProfileVO createHealthProfile(HealthProfileCreateRequest healthProfileCreateRequest, Long userId);

    /**
     * 更新健康档案
     *
     * @param healthProfileUpdateRequest 更新请求
     * @param userId 用户ID
     * @return 健康档案VO
     */
    HealthProfileVO updateHealthProfile(HealthProfileUpdateRequest healthProfileUpdateRequest, Long userId);

    /**
     * 获取用户健康档案
     *
     * @param userId 用户ID
     * @return 健康档案VO
     */
    HealthProfileVO getHealthProfileByUserId(Long userId);

    /**
     * 删除健康档案
     *
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteHealthProfile(Long userId);

    /**
     * 检查用户是否已有健康档案
     *
     * @param userId 用户ID
     * @return 是否存在
     */
    boolean hasHealthProfile(Long userId);

    /**
     * 获取活动水平描述
     *
     * @param activityLevel 活动水平
     * @return 描述
     */
    String getActivityLevelDesc(Integer activityLevel);
}
