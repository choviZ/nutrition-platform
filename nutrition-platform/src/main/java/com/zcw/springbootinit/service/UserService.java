package com.zcw.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zcw.springbootinit.model.dto.user.UserQueryRequest;
import com.zcw.springbootinit.model.entity.User;
import com.zcw.springbootinit.model.vo.UserVO;

/**
* @author zcw
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2025-09-22 09:14:28
*/
public interface UserService extends IService<User> {

    /**
     * 校验用户参数
     *
     * @param user 用户信息
     * @param add  是否为添加操作
     */
    void validUser(User user, boolean add);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest 查询请求
     * @return 查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 获取用户视图对象
     *
     * @param user 用户实体
     * @return 用户视图对象
     */
    UserVO getUserVO(User user);

    /**
     * 分页获取用户视图对象
     *
     * @param userPage 用户分页数据
     * @return 用户视图对象分页数据
     */
    Page<UserVO> getUserVOPage(Page<User> userPage);

}
