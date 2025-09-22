package com.zcw.np.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcw.np.model.dto.dietrecord.DietRecordAddRequest;
import com.zcw.np.model.dto.dietrecord.DietRecordQueryRequest;
import com.zcw.np.model.dto.dietrecord.DietRecordUpdateRequest;
import com.zcw.np.model.entity.DietRecord;
import com.zcw.np.model.vo.DietRecordStatisticsVO;
import com.zcw.np.model.vo.DietRecordVO;

import java.time.LocalDate;
import java.util.List;

/**
* @author zcw
* @description 针对表【diet_record(饮食记录表)】的数据库操作Service
* @createDate 2025-09-22 16:22:37
*/
public interface DietRecordService extends IService<DietRecord> {

    /**
     * 添加饮食记录
     * @param dietRecordAddRequest 添加请求
     * @param userId 用户ID
     * @return 记录ID
     */
    Long addDietRecord(DietRecordAddRequest dietRecordAddRequest, Long userId);

    /**
     * 更新饮食记录
     * @param dietRecordUpdateRequest 更新请求
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean updateDietRecord(DietRecordUpdateRequest dietRecordUpdateRequest, Long userId);

    /**
     * 删除饮食记录
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 是否成功
     */
    Boolean deleteDietRecord(Long recordId, Long userId);

    /**
     * 分页查询饮食记录
     * @param dietRecordQueryRequest 查询请求
     * @return 分页结果
     */
    IPage<DietRecordVO> queryDietRecords(DietRecordQueryRequest dietRecordQueryRequest);

    /**
     * 根据ID获取饮食记录
     * @param recordId 记录ID
     * @param userId 用户ID
     * @return 饮食记录VO
     */
    DietRecordVO getDietRecordById(Long recordId, Long userId);

    /**
     * 获取用户指定日期范围的饮食记录统计
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果列表
     */
    List<DietRecordStatisticsVO> getDietRecordStatistics(Long userId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取用户指定日期的饮食记录
     * @param userId 用户ID
     * @param recordDate 记录日期
     * @return 饮食记录列表
     */
    List<DietRecordVO> getDietRecordsByDate(Long userId, LocalDate recordDate);
}
