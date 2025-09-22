package com.zcw.np.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zcw.np.common.BaseResponse;
import com.zcw.np.common.ErrorCode;
import com.zcw.np.common.ResultUtils;
import com.zcw.np.exception.ThrowUtils;
import com.zcw.np.model.dto.dietrecord.DietRecordAddRequest;
import com.zcw.np.model.dto.dietrecord.DietRecordQueryRequest;
import com.zcw.np.model.dto.dietrecord.DietRecordUpdateRequest;
import com.zcw.np.model.vo.DietRecordStatisticsVO;
import com.zcw.np.model.vo.DietRecordVO;
import com.zcw.np.service.DietRecordService;
import com.zcw.np.utils.UserContextUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

/**
 * 饮食记录接口
 *
 * @author zcw
 */
@RestController
@RequestMapping("/diet-record")
@Slf4j
@Api(tags = "饮食记录管理")
public class DietRecordController {

    @Resource
    private DietRecordService dietRecordService;

    /**
     * 添加饮食记录
     *
     * @param dietRecordAddRequest 添加请求
     * @return 记录ID
     */
    @PostMapping("/add")
    @ApiOperation(value = "添加饮食记录")
    public BaseResponse<Long> addDietRecord(@RequestBody @Valid DietRecordAddRequest dietRecordAddRequest) {
        ThrowUtils.throwIf(dietRecordAddRequest == null, ErrorCode.PARAMS_ERROR);
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        Long recordId = dietRecordService.addDietRecord(dietRecordAddRequest, currentUserId);
        return ResultUtils.success(recordId);
    }

    /**
     * 更新饮食记录
     *
     * @param dietRecordUpdateRequest 更新请求
     * @return 是否成功
     */
    @PostMapping("/update")
    @ApiOperation(value = "更新饮食记录")
    public BaseResponse<Boolean> updateDietRecord(@RequestBody @Valid DietRecordUpdateRequest dietRecordUpdateRequest) {
        ThrowUtils.throwIf(dietRecordUpdateRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(dietRecordUpdateRequest.getRecordId() == null, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        Boolean result = dietRecordService.updateDietRecord(dietRecordUpdateRequest, currentUserId);
        return ResultUtils.success(result);
    }

    /**
     * 删除饮食记录
     *
     * @param recordId 记录ID
     * @return 是否成功
     */
    @DeleteMapping("/{recordId}")
    @ApiOperation(value = "删除饮食记录")
    public BaseResponse<Boolean> deleteDietRecord(@ApiParam(value = "记录ID", required = true) @PathVariable Long recordId) {
        ThrowUtils.throwIf(recordId == null || recordId <= 0, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        Boolean result = dietRecordService.deleteDietRecord(recordId, currentUserId);
        return ResultUtils.success(result);
    }

    /**
     * 根据ID获取饮食记录
     *
     * @param recordId 记录ID
     * @return 饮食记录详情
     */
    @GetMapping("/get/{recordId}")
    @ApiOperation(value = "根据ID获取饮食记录")
    public BaseResponse<DietRecordVO> getDietRecordById(@ApiParam(value = "记录ID", required = true) @PathVariable Long recordId) {
        ThrowUtils.throwIf(recordId == null || recordId <= 0, ErrorCode.PARAMS_ERROR, "记录ID不能为空");
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        DietRecordVO dietRecordVO = dietRecordService.getDietRecordById(recordId, currentUserId);
        return ResultUtils.success(dietRecordVO);
    }

    /**
     * 分页查询饮食记录（GET方式，便于前端调用）
     *
     * @param dietRecordQueryRequest 查询请求
     * @return 分页结果
     */
    @GetMapping("/list/page")
    @ApiOperation(value = "分页查询饮食记录（GET方式）")
    public BaseResponse<IPage<DietRecordVO>> queryDietRecordsGet(DietRecordQueryRequest dietRecordQueryRequest) {
        if (dietRecordQueryRequest == null) {
            dietRecordQueryRequest = new DietRecordQueryRequest();
        }
        // 获取当前用户ID，如果查询请求中没有指定用户ID，则使用当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        if (dietRecordQueryRequest.getUserId() == null) {
            dietRecordQueryRequest.setUserId(currentUserId);
        }
        IPage<DietRecordVO> dietRecordVOPage = dietRecordService.queryDietRecords(dietRecordQueryRequest);
        return ResultUtils.success(dietRecordVOPage);
    }

    /**
     * 获取指定日期的饮食记录
     *
     * @param recordDate 记录日期
     * @return 饮食记录列表
     */
    @GetMapping("/list/date")
    @ApiOperation(value = "获取指定日期的饮食记录")
    public BaseResponse<List<DietRecordVO>> getDietRecordsByDate(
            @ApiParam(value = "记录日期", required = true, example = "2025-01-22")
            @RequestParam("recordDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate recordDate) {
        ThrowUtils.throwIf(recordDate == null, ErrorCode.PARAMS_ERROR, "记录日期不能为空");
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        List<DietRecordVO> dietRecordVOList = dietRecordService.getDietRecordsByDate(currentUserId, recordDate);
        return ResultUtils.success(dietRecordVOList);
    }

    /**
     * 获取饮食记录统计报表
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 统计结果
     */
    @GetMapping("/statistics")
    @ApiOperation(value = "获取饮食记录统计报表")
    public BaseResponse<List<DietRecordStatisticsVO>> getDietRecordStatistics(
            @ApiParam(value = "开始日期", required = true, example = "2025-01-01")
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @ApiParam(value = "结束日期", required = true, example = "2025-01-31")
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        ThrowUtils.throwIf(startDate == null, ErrorCode.PARAMS_ERROR, "开始日期不能为空");
        ThrowUtils.throwIf(endDate == null, ErrorCode.PARAMS_ERROR, "结束日期不能为空");
        ThrowUtils.throwIf(startDate.isAfter(endDate), ErrorCode.PARAMS_ERROR, "开始日期不能晚于结束日期");
        
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        List<DietRecordStatisticsVO> statisticsList = dietRecordService.getDietRecordStatistics(currentUserId, startDate, endDate);
        return ResultUtils.success(statisticsList);
    }

    /**
     * 获取今日饮食记录
     *
     * @return 今日饮食记录列表
     */
    @GetMapping("/today")
    @ApiOperation(value = "获取今日饮食记录")
    public BaseResponse<List<DietRecordVO>> getTodayDietRecords() {
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        
        LocalDate today = LocalDate.now();
        List<DietRecordVO> dietRecordVOList = dietRecordService.getDietRecordsByDate(currentUserId, today);
        return ResultUtils.success(dietRecordVOList);
    }

    /**
     * 获取本周饮食记录统计
     *
     * @return 本周统计结果
     */
    @GetMapping("/statistics/week")
    @ApiOperation(value = "获取本周饮食记录统计")
    public BaseResponse<List<DietRecordStatisticsVO>> getWeeklyStatistics() {
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1); // 本周一
        LocalDate endOfWeek = startOfWeek.plusDays(6); // 本周日
        
        List<DietRecordStatisticsVO> statisticsList = dietRecordService.getDietRecordStatistics(currentUserId, startOfWeek, endOfWeek);
        return ResultUtils.success(statisticsList);
    }

    /**
     * 获取本月饮食记录统计
     *
     * @return 本月统计结果
     */
    @GetMapping("/statistics/month")
    @ApiOperation(value = "获取本月饮食记录统计")
    public BaseResponse<List<DietRecordStatisticsVO>> getMonthlyStatistics() {
        // 获取当前用户ID
        Long currentUserId = UserContextUtils.getRequiredCurrentUserId();
        
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.withDayOfMonth(1); // 本月第一天
        LocalDate endOfMonth = today.withDayOfMonth(today.lengthOfMonth()); // 本月最后一天
        
        List<DietRecordStatisticsVO> statisticsList = dietRecordService.getDietRecordStatistics(currentUserId, startOfMonth, endOfMonth);
        return ResultUtils.success(statisticsList);
    }
}